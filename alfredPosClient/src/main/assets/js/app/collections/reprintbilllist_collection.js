/**
 * Created by Administrator on 14-3-26.
 */
var app = app || {};
(function () {

    app.rePrintCollection = Backbone.Collection.extend({
        model: app.reprintbillListModel,
        initialize: function () {
            //加载JSON数据
          //  this.add(json);
        }
    });
    app.reprintbillListCollection=new app.rePrintCollection();
})();
