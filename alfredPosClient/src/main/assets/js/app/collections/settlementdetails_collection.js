/**
 * Created by Administrator on 14-3-26.
 */
var app = app || {};
(function () {

    app.settlementDetailsCollection = Backbone.Collection.extend({
        model: app.settlementDetailsModel,
        initialize: function () {
        //加载JSON数据
        //this.add(settlementDetailsJSON);
    }
    });

    app.settlementDteailsC = new app.settlementDetailsCollection();
})();
