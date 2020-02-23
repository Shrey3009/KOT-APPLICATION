/**
 * Created by Administrator on 14-3-26.
 */

var app = app || {};
(function () {
    //order details
    app.orderDetailsModel = Backbone.Model.extend({
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

