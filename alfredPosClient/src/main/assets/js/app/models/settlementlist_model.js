/**
 * Created by Administrator on 14-3-26.
 */
var app = app || {};
(function () {
    //settlement列表模型
    app.settlementListModel = Backbone.Model.extend({
        default:{
           "orderId":'',
	       "billNo":'',
	       "paymentTypeId":0,
	       "placeName":0,
	       "tableId":'',
	       "totalAmount":"",
	       "paymentCreateTime":'',
	       "userName":'',
            "paymentId":0,
            "type":0
        }
    });
})();
