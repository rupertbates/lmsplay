//(function(exports) {
//    var set = Ember.set, get = Ember.get;
//
//
//})({});

//Create the App namespace
window.App = Ember.Application.create();

Game = Ember.Object.extend({
//    thisWeeksSelection: function() {
//       return "Liverpool";
//    }.property('gameRounds')
    userPickChanged: function() {
        //alert(this.game.id);
        var jsonData = JSON.stringify(this.get("game"));
        $.ajax({
            type: 'GET',
            url: '/pick-team/' + this.game.id + "/" + this.competitionWeek + "/" + this.userPick
        });
    }.observes('userPick')
});

App.game = Game.create(data);

var view = Ember.View.create({
    templateName: 'game-template',
    model: App.game
});

Ember.RadioButton = Ember.View.extend({
    tagName: "span",
    title: null,
    checked: false,
    group: "radio_button",
    disabled: false,
    classNames: ['ember-radio-button'],
    defaultTemplate: Ember.Handlebars.compile('<label><input type="radio" {{ bindAttr disabled="disabled" name="group" value="option" checked="checked"}} />{{title}}</label>'),
    bindingChanged: function(){
        if(this.get("option") == Ember.get(this, 'value')){
            this.set("checked", true);
        }
    }.observes("value"),

    change: function() {
        Ember.run.once(this, this._updateElementValue);
    },

    _updateElementValue: function() {
        var input = this.$('input:radio');
        Ember.set(this, 'value', input.attr('value'));
    }
});
view.appendTo("#game");