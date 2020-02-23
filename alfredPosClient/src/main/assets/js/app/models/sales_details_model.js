/**
 * Created by Administrator on 14-3-26.
 */

var app = app || {};
(function () {
    //ssession 销售详情
    app.salesDetailDetaModel = Backbone.Model.extend({
        default:{
            bizDate:'09/12/2012',
            wholedayAmount:'0.00',
            breakfastAmount:'0.00',
            lunchAmount:'0.00',
            dinnerAmount:'0.00',
            supperAmount:'0.00',
            zNettSale:'0.00',
        }
    });
})();

