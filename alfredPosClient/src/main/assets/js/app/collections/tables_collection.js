/**
 * Created by Administrator on 14-3-20.
 */
var app = app || {};
(function () {
    app.TaCollection = Backbone.Collection.extend({
        model: app.tablesModel
    });

})();
