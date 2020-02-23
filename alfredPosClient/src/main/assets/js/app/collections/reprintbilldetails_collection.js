/**
 * Created by Administrator on 14-3-26.
 */
var app = app || {};
(function () {

    app.reprintDteaCol = Backbone.Collection.extend({
        model: app.reprintbillDetailsModel,
        initialize: function () {
        //加载JSON数据
       // this.add(json);
    }
    });
    app.billDetailsCollection=new app.reprintDteaCol();
})();
