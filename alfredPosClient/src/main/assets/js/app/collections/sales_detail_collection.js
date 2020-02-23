/**
 * Created by Administrator on 14-3-26.
 */
var app = app || {};
(function () {

    app.salesDetailDetaCollection = Backbone.Collection.extend({
        model: app.salesDetailDetaModel,
        initialize: function () {
        }
    });
    app.salesDetailC=new app.salesDetailDetaCollection();
})();
