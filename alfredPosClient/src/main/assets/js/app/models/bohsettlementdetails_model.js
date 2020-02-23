/**
 * Created by Administrator on 14-3-26.
 */

var app = app || {};
(function () {
    //settlement详情列表模型
    app.bohSetDetaModel = Backbone.Model.extend({
        default:{
            billNo:'0',
            billId:'0',
            tableId:'',
            restaurantName:'',
            discount:'',
            total:''
        }
    });
})();

