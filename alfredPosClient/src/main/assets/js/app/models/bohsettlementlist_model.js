/**
 * Created by Administrator on 14-3-26.
 */
var app = app || {};
(function () {
    //settlement
    app.bohSetListModel = Backbone.Model.extend({
        default:{
            id:'',
            paidDate:'0',
            billNo:'0',
            orderId:'',
            amount:'',
            nameOfPerson:'',
            authorizedUserId:'',
            daysDue:'',
            remark:'',
            sysCreateTime:'',
            status:0
        },
        initialize: function() {
          var start = moment(this.get('daysDue'));
          var now = moment();
          var dues = now.from(start, true);
          this.set({'daysDue':dues});
          
          var createtime = start.format("MMM Do YYYY, HH:mm");
          this.set({'sysCreateTime':createtime});
        }
    });
    
})();
