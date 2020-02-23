/**
 * Created by Administrator on 14-3-26.
 */
var app = app || {};
(function () {
    app.settlementCollection = Backbone.Collection.extend({
        model: app.settlementListModel,
        initialize: function () {
            //加载JSON数据
           // this.add(json);
        }
    });
    app.settlementListCollection=new app.settlementCollection();
})();
