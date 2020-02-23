/**
 * Created by Administrator on 14-3-26.
 */
var app = app || {};
(function () {

    app.orderDetailsCollection = Backbone.Collection.extend({
        model: app.orderDetailsModel,
        initialize: function () {
        //加载JSON数据
        //this.add(json);
    }});

    app.orderDteailsC = new app.orderDetailsCollection();
})();
