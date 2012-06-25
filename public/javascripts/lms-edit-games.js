$(function () {
    var Player = Backbone.Model.extend({
        set: function(attributes, options) {
            this.raw = attributes;
//            if(attributes.name == undefined)
//                attributes = {name : attributes};
//            return Backbone.Model.prototype.set.call(this, attributes, options);
        }
    });

    var PlayerView = Backbone.View.extend({
        tagName:"li",
        // Cache the template function for a single item.
        template:_.template($('#player-template').html()),
        // The DOM events specific to an item.
        events:{
            "click a.destroy-player":"clearPlayer"
        },
        // Re-render the name of the player.
        render:function () {
            this.$el.html(this.template({username: this.model}));
            return this;
        },
        // Remove the item, destroy the model.
        clearPlayer:function () {
            this.options.parent.model.removePlayer(this.model);
            this.options.parent.render();
        }
    });

    // Game Model
    var Game = Backbone.Model.extend({
        // Default attributes for the game item.
        defaults:function () {
            return {
                name:"New game...",
                started:undefined
            };
        },

        // Ensure that each game created has `name`.
        initialize:function () {
            if (!this.get("name")) {
                this.set({"name":this.defaults.name});
            }
            //check there is a players array
            if(!this.get("players")){
                var players = [""];
                this.set({players: players});
            }
        },
        start:function () {
            if (this.get("started") == 0) {
                $.ajax({
                    type: 'GET',
                    url: '/games/start/' + this.get("id")
                });
                //this.save({"started":new Date()});
            }
        },
        // Remove this Game from the server and delete its view.
        clear:function () {
            this.destroy();
        },
        addPlayer:function(value){
            var players = this.get("players");
            players.push(value);
            this.save({"players" : players});
        },
        removePlayer:function(value){
            var players = _.reject(this.get("players"), function(player){return player == value;});
            this.save({"players" : players});
        }
        //Make sure that started is a date type
//        set: function(attributes, options) {
//            if (attributes.started !== undefined) {
//                attributes.started = new Date(attributes.started);
//            }
//            return Backbone.Model.prototype.set.call(this, attributes, options);
//        }
    });
    // Game Collection
    // ---------------
    var GameList = Backbone.Collection.extend({
        // Reference to this collection's model.
        model:Game,
        url:'/games',
        // Filter down the list of all game items that are started.
        started:function () {
            return this.filter(function (game) {
                return game.get('started');
            });
        },
        // Filter down the list to only game items that are still not started.
        unstarted:function () {
            return this.without.apply(this, this.started());
        }
    });

    // Create our global collection of **Games**.
    var Games = new GameList;

    // Game Item View
    var GameView = Backbone.View.extend({
        tagName:"li",
        // Cache the template function for a single item.
        template:_.template($('#game-template').html()),
        // The DOM events specific to an item.
        events:{
            "click a.add-player":"addPlayer",
            "click a.start":"start",
            "dblclick .view":"editName",
            "click a.destroy-game":"clearGame",
            "keypress .edit-name":"updateOnEnterName",
            "blur .edit-name":"closeName",
            "keypress input.add-player":"updateOnEnterAddPlayer",
            "change input.add-player":"closeAddPlayer"
        },
        // The GameView listens for changes to its model, re-rendering. Since there's
        // a one-to-one correspondence between a **Game** and a **GameView** in this
        // app, we set a direct reference on the model for convenience.
        initialize:function () {
            this.model.bind('change', this.render, this);
            this.model.bind('destroy', this.remove, this);
            this.players = this.model.get('players');
        },

        // Re-render the names of the game item.
        render:function () {
            this.$el.html(this.template(this.model.toJSON()));
            var playerList = this.$("#player-list");
            playerList.empty();
            var parent = this;
            //render the players list
            _.each(this.model.get("players"), function(player){
                var view = new PlayerView({ model: player, parent: parent });
                playerList.append(view.render().el);
            });

            //this.$("#game-list").append(view.render().el);
            this.editName = this.$('.edit-name');
            this.addPlayer = this.$('input.add-player');
            return this;
        },

        // Remove the item, destroy the model.
        clearPlayer:function () {
            this.model.clear();
        },

        //Add a player to the game
        addPlayer:function () {
            this.$el.addClass("add-player");
            this.addPlayer.focus();
        },

        // Close the `"addPlayer"` mode, saving changes to the game.
        closeAddPlayer:function () {
            var value = this.addPlayer.val();
            this.addPlayer.val("");
            if (value) {
                this.model.addPlayer(value);
            }
            this.$el.removeClass("add-player");
            this.render(); //need to call render because players is not a backbone collection
        },

        // If you hit `enter`, we're through editing the item.
        updateOnEnterAddPlayer:function (e) {
            if (e.keyCode == 13) this.closeAddPlayer();
        },

        // Set the `"started"` date of the model.
        start:function () {
            this.model.start();
        },

        // Switch this view into `"editing"` mode, displaying the input field.
        editName:function () {
            this.$el.addClass("editing");
            this.editName.focus();
        },

        // Close the `"editing"` mode, saving changes to the game.
        closeName:function () {
            var value = this.editName.val();
            if (!value) this.clear();
            this.model.save({name:value});
            this.$el.removeClass("editing");
        },

        // If you hit `enter`, we're through editing the item.
        updateOnEnterName:function (e) {
            if (e.keyCode == 13) this.closeName();
        },

        // Remove the item, destroy the model.
        clearGame:function () {
            this.model.clear();
        }

    });

    // The Application
    // ---------------
    // Our overall **AppView** is the top-level piece of UI.
    var AppView = Backbone.View.extend({
        // Instead of generating a new element, bind to the existing skeleton of
        // the App already present in the HTML.
        el:$("#gameapp"),

        // Our template for the line of statistics at the bottom of the app.
        statsTemplate:_.template($('#stats-template').html()),

        // Delegated events for creating new items, and clearing completed ones.
        events:{
            "keypress #new-game":"createOnEnter"
        },

        // At initialization we bind to the relevant events on the `Games`
        // collection, when items are added or changed. Kick things off by
        // loading any preexisting games that might be saved in *localStorage*.
        initialize:function () {
            this.input = this.$("#new-game");
            Games.bind('add', this.addOne, this);
            Games.bind('reset', this.addAll, this);
            Games.bind('all', this.render, this);

            this.footer = this.$('footer');
            this.main = $('#main');

        },

        // Re-rendering the App just means refreshing the statistics -- the rest
        // of the app doesn't change.
        render:function () {
            var started = Games.started().length;
            var unstarted = Games.unstarted().length;

            if (Games.length) {
                this.main.show();
                this.footer.show();
                this.footer.html(this.statsTemplate({started:started, unstarted:unstarted}));
            } else {
                this.main.hide();
                this.footer.hide();
            }
        },

        // Add a single game item to the list by creating a view for it, and
        // appending its element to the `<ul>`.
        addOne:function (game) {
            var view = new GameView({model:game});
            this.$("#game-list").append(view.render().el);
        },

        // Add all items in the **Games** collection at once.
        addAll:function () {
            Games.each(this.addOne);
        },

        // If you hit return in the main input field, create new **Game** model,
        // persisting it to *localStorage*.
        createOnEnter:function (e) {
            if (e.keyCode != 13) return;
            if (!this.input.val()) return;

            Games.create({name:this.input.val()});
            this.input.val('');
        }

    });
    // Finally, we kick things off by creating the **App**.
    window.App = new AppView;
    Games.reset(data);
});
