/**
 * Created by Administrator on 14-3-26.
 */
var app = app || {};
(function () {
    app.olCollection = Backbone.Collection.extend({
        model: app.orderListModel,
        initialize: function () {
            //加载JSON数据
            //this.add(json);
        }
    });
    app.orderListCollection=new app.olCollection();
})();
