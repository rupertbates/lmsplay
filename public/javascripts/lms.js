// Load the application once the DOM is ready, using `jQuery.ready`:
//var Games = null
$(function(){

    // Game Model
    // ----------

    // Our  **Game** model has a `name`, 'created', `order`, and `started` attributes.
    var Game = Backbone.Model.extend({

        // Default attributes for the game item.
        defaults: function() {
            return {
                name: "New game...",
                order: Games.nextOrder(),
                started: undefined
            };
        },

        // Ensure that each game created has `name`.
        initialize: function() {
            if (!this.get("name")) {
                this.set({"name": this.defaults.name});
            }
        },

        start: function(){
            if(this.get("started") == undefined){
                this.set({"started": new Date()});
            }
        },

        // Toggle the `started` state of this game item.
        toggle: function() {
            this.save({started: !this.get("started")});
        },

        // Remove this Game from *localStorage* and delete its view.
        clear: function() {
            this.destroy();
        }

    });

    // Game Collection
    // ---------------

    // The collection of games is backed by *localStorage* instead of a remote
    // server.
    var GameList = Backbone.Collection.extend({

        // Reference to this collection's model.
        model: Game,
        url: '/games',

//        // Save all of the game items under the `"games"` namespace.
//        localStorage: new Store("games-backbone"),

        // Filter down the list of all game items that are finished.
        started: function() {
            return this.filter(function(game){ return game.get('started'); });
        },

        // Filter down the list to only game items that are still not finished.
        remaining: function() {
            return this.without.apply(this, this.started());
        },

        // We keep the Games in sequential order, despite being saved by unordered
        // GUID in the database. This generates the next order number for new items.
        nextOrder: function() {
            if (!this.length) return 1;
            return this.last().get('order') + 1;
        },

        // Games are sorted by their original insertion order.
        comparator: function(game) {
            return game.get('order');
        }

    });

    // Create our global collection of **Games**.
    var Games = new GameList;

    // Game Item View
    // --------------

    // The DOM element for a game item...
    var GameView = Backbone.View.extend({

        //... is a list tag.
        tagName:  "li",

        // Cache the template function for a single item.
        template: _.template($('#item-template').html()),

        // The DOM events specific to an item.
        events: {
            "click a.start"   : "start",
            "click .toggle"   : "toggleDone",
            "dblclick .view"  : "edit",
            "click a.destroy" : "clear",
            "keypress .edit"  : "updateOnEnter",
            "blur .edit"      : "close"
        },

        // The GameView listens for changes to its model, re-rendering. Since there's
        // a one-to-one correspondence between a **Game** and a **GameView** in this
        // app, we set a direct reference on the model for convenience.
        initialize: function() {
            this.model.bind('change', this.render, this);
            this.model.bind('destroy', this.remove, this);
        },

        // Re-render the names of the game item.
        render: function() {
            this.$el.html(this.template(this.model.toJSON()));
            this.$el.toggleClass('started', this.model.get('started'));
            this.input = this.$('.edit');
            return this;
        },

        // Set the `"started"` date of the model.
        start: function() {
            this.model.start();
        },

        // Toggle the `"started"` state of the model.
        toggleDone: function() {
            this.model.toggle();
        },

        // Switch this view into `"editing"` mode, displaying the input field.
        edit: function() {
            this.$el.addClass("editing");
            this.input.focus();
        },

        // Close the `"editing"` mode, saving changes to the game.
        close: function() {
            var value = this.input.val();
            if (!value) this.clear();
            this.model.save({name: value});
            this.$el.removeClass("editing");
        },

        // If you hit `enter`, we're through editing the item.
        updateOnEnter: function(e) {
            if (e.keyCode == 13) this.close();
        },

        // Remove the item, destroy the model.
        clear: function() {
            this.model.clear();
        }

    });

    // The Application
    // ---------------

    // Our overall **AppView** is the top-level piece of UI.
    var AppView = Backbone.View.extend({

        // Instead of generating a new element, bind to the existing skeleton of
        // the App already present in the HTML.
        el: $("#gameapp"),

        // Our template for the line of statistics at the bottom of the app.
        statsTemplate: _.template($('#stats-template').html()),

        // Delegated events for creating new items, and clearing completed ones.
        events: {
            "keypress #new-game":  "createOnEnter",
            "click #clear-completed": "clearCompleted",
            "click #toggle-all": "toggleAllComplete"
        },

        // At initialization we bind to the relevant events on the `Games`
        // collection, when items are added or changed. Kick things off by
        // loading any preexisting games that might be saved in *localStorage*.
        initialize: function() {

            this.input = this.$("#new-game");
            this.allCheckbox = this.$("#toggle-all")[0];

            Games.bind('add', this.addOne, this);
            Games.bind('reset', this.addAll, this);
            Games.bind('all', this.render, this);

            this.footer = this.$('footer');
            this.main = $('#main');

            Games.reset(data);
        },

        // Re-rendering the App just means refreshing the statistics -- the rest
        // of the app doesn't change.
        render: function() {
            var started = Games.started().length;
            var remaining = Games.remaining().length;

            if (Games.length) {
                this.main.show();
                this.footer.show();
                this.footer.html(this.statsTemplate({started: started, remaining: remaining}));
            } else {
                this.main.hide();
                this.footer.hide();
            }

            this.allCheckbox.checked = !remaining;
        },

        // Add a single game item to the list by creating a view for it, and
        // appending its element to the `<ul>`.
        addOne: function(game) {
            var view = new GameView({model: game});
            this.$("#game-list").append(view.render().el);
        },

        // Add all items in the **Games** collection at once.
        addAll: function() {
            Games.each(this.addOne);
        },

        // If you hit return in the main input field, create new **Game** model,
        // persisting it to *localStorage*.
        createOnEnter: function(e) {
            if (e.keyCode != 13) return;
            if (!this.input.val()) return;

            Games.create({name: this.input.val()});
            this.input.val('');
        },

        // Clear all started game items, destroying their models.
        clearCompleted: function() {
            _.each(Games.started(), function(game){ game.clear(); });
            return false;
        },

        toggleAllComplete: function () {
            var started = this.allCheckbox.checked;
            Games.each(function (game) { game.save({'started': started}); });
        }

    });

    // Finally, we kick things off by creating the **App**.
    window.App = new AppView;


});
