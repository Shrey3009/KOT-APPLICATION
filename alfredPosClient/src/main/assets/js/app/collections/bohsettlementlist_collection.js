/**
 * Created by Administrator on 14-3-26.
 */
var app = app || {};
(function () {

    app.bohSetCollection = Backbone.Collection.extend({
        model: app.bohSetListModel,
        initialize: function () {
            //加载JSON数据
          //  this.add(json);
        }
    });
    app.bohSettlementListCollection = new app.bohSetCollection();
})();
