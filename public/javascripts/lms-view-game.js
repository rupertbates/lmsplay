// Load the application once the DOM is ready, using `jQuery.ready`:
//var Games = null
$(function () {
    // The DOM element for a player ...
    var PlayerView = Backbone.View.extend({

        //... is a list tag.
        tagName:"li",

        // Cache the template function for a single item.
        template:_.template($('#player-template').html()),

        // Re-render the name of the player.
        render:function () {
            this.$el.html(this.template({username:this.model}));
            return this;
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
            //check there is a players array
            if (!this.get("players")) {
                var players = [""];
                this.set({players:players});
            }
        },

        start:function () {
            if (this.get("started") == undefined) {
                this.save({"started":new Date()});
            }
        },

        // Toggle the `started` state of this game item.
        toggle:function () {
            this.save({started:!this.get("started")});
        },
        addPlayer:function (value) {
            var players = this.get("players");
            players.push(value);
            this.save({"players":players});
        },
        removePlayer:function (value) {
            var players = _.reject(this.get("players"), function (player) {
                return player == value;
            });
            this.save({"players":players});
        },
        set:function (attributes, options) {
            if (attributes.started !== undefined) {
                attributes.started = new Date(attributes.started);
            }
            return Backbone.Model.prototype.set.call(this, attributes, options);
        }

    });

    // Game Item View
    // The DOM element for a game item...
    var GameView = Backbone.View.extend({

        //... is a list tag.
        el:$("#game"),

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
            //this.players.bind('add', this.onPlayerAdded, this);

        },

        // Re-render the names of the game item.
        render:function () {
            this.$el.html(this.template(this.model.toJSON()));
            var parent = this;

            var list = this.$("#player-list");
            list.empty();
            _.each(this.model.get("game").players, function (player) {
                var view = new PlayerView({ model:player, parent:parent });
                list.append(view.render().el);
            });

            list = this.$("#match-list");
            list.empty();
            _.each(this.model.get("thisWeeksMatches"), function (match) {
                list.append("<li>" + match.homeTeam.name + " - " + match.awayTeam.name + "</li>");
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

//        onPlayerAdded: function(player) {
//            var view = new PlayerView({model:player});
//            this.$("#player-list").append(view.render().el);
//        },

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
    var game = new Game(data);
    gameView = new GameView({model:game});
    gameView.render();
});
