/**
 * Created by Administrator on 14-3-26.
 */
var app = app || {};
(function () {
    //订单列表模型
    app.orderListModel = Backbone.Model.extend({
        default:{
            orderId:'0',
            kotId:'0',
            location:'',
            tableId:'',
            waiter:'',
            cashier:''
        }
    });
})();
