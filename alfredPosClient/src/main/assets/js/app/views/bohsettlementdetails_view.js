/**
 * Created by Administrator on 14-3-26.
 */

var app = app || {};

(function ($) {
    //绑定DOM节点
    app.bohSettDetailsAppView = Backbone.View.extend({
        el: '#billDetails',
        events:{'click #closeBtn':'closebohSettList',
                'click #saveBohBtn':'saveBOH',
                'click #cancelBohBtn':'cancelBOH'},
        template:_.template($('#bohSettdetailsItem-template').html()),
        initialize: function () {
            $("#bohSettDetailsContent").on('click', '#closeBtn',{self:this}, this.closebohSettList);           
        },        
        render: function () {
           this.$el.html(this.template(this.model.toJSON()));  
		   $('input').iCheck({
		    checkboxClass: 'icheckbox_square',
		    radioClass: 'iradio_square',
		    increaseArea: '50%' // optional
		   });                                  
        },
        closebohSettList:function(event){
            var self = this;
            if (event) {
               event.stopImmediatePropagation();
               self = event.data.self;
            }
            self.opened = false;
            $("#billTables").removeClass("minCol");
            $("#billTables").addClass("maxCol");
            $("#bohSettListContent").removeClass('merge-right');
            $("#bohSettDetailsContent").removeClass('show-bohsettlement-detail');
            app.bohSetListAppViews.showtitle();
        },
        saveBOH:function(event) {
          event.stopImmediatePropagation();
          var payname = $('.iradio_square.checked > input').attr('id');//$("input[type=radio][checked]").attr('id');
          var amount = $('#bohamountpaid').val();
          var bohobj = {};
          var paymethod = 0;
          if (payname == "cardpay"){
            paymethod = 1;
          }
          bohobj = {"paymentType": paymethod,
                    "id":$(event.currentTarget).attr('data'),     
                    "amount": amount};
          this.closebohSettList();
          var param = {"js_callback":"","bohSettlement":bohobj};
          if (app.bohSetListAppViews.isApiModeLocal()) {
             window.JavaConnectJS.send("SaveLocalBOHPayment",JSON.stringify(param));
          }else
              window.JavaConnectJS.send("SaveBOHPayment",JSON.stringify(param));
        },
        cancelBOH: function(event) {
          event.stopImmediatePropagation();
          this.closebohSettList(null);
        }
    });
})(jQuery);