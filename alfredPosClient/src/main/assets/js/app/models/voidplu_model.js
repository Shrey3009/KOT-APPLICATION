/**
 * Created by Administrator on 14-3-26.
 */
var app = app || {};
(function () {
    //订单列表模型
    app.voidPluModel = Backbone.Model.extend({
        default:{
            itemName:'',
            itemQty:'',
            price:'',
            amount:''
        }
    });
})();
