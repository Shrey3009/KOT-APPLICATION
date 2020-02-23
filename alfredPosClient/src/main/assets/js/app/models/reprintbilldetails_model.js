/**
 * Created by Administrator on 14-3-26.
 */

var app = app || {};
(function () {
    //订单列表模型
    app.reprintbillDetailsModel = Backbone.Model.extend({
        default:{
            name:'0',
            price:'0',
            qty:'',
            subTotal:'',
            discount:'',
            total:''
        }
    });
})();

