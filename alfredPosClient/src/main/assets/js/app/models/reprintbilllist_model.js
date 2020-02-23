/**
 * Created by Administrator on 14-3-26.
 */
var app = app || {};
(function () {
    app.reprintbillListModel = Backbone.Model.extend({
        default:{
            orderId:'0',
            orderNo:'0',
            placeName:'0',
            tableName:'',
            revenueId:'',
            total:'',
            cashier:''
        }
    });
})();
