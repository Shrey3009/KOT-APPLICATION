var app = app || {};

(function ($) {
    app.cashInOutView = Backbone.View.extend({
        el: '#cashinoutContent',
        events: {'click .button': 'keybutton',
				 'click #cashInTitle':'selectCashIn',
				 'click #cashOutTitle':'selectCashOut',
                 'click #backBtn':'backMainIndex',
                 'click #continueBtn':'continueStep',
                 'click #confirmsaveBtn':"saveCashInOut"},
        cashinoutvalue: [],
        initialize: function () {
            $('.commentwin').on("click", '#confirmsaveBtn',{self:this}, this.saveCashInOut);
            $('.commentwin').on("click", '#cancelBtn', {self:this},this.cancelSaveCashInOut);
        }, 
		selectCashIn:function(){
			$('#cashInTitle').removeClass('btn-info');
			$('#cashInTitle').addClass('btn-success');
			$('#cashOutTitle').removeClass('btn-success');
			$('#cashOutTitle').addClass('btn-info');
		},
		selectCashOut:function(){
			$('#cashInTitle').removeClass('btn-success');
			$('#cashInTitle').addClass('btn-info');
			$('#cashOutTitle').removeClass('btn-info');
			$('#cashOutTitle').addClass('btn-success');
		},
        keybutton:function(evt){
          var keyval = -1;
          var key = $(evt.currentTarget).attr("data");

          if (key!=undefined) {
            keyval = parseInt(key);
          }
          $('.button').removeClass('buttonDown');
          $(evt.currentTarget).addClass('buttonDown');
          switch (keyval) {
            case -1:
               this.cashinoutvalue = [];
               break;
            case 10:
               if (this.cashinoutvalue.length>0){
                 this.cashinoutvalue.push('0');
                 this.cashinoutvalue.push('0');
               }
               break;
            case 0:
               if (this.cashinoutvalue.length>0){
                 this.cashinoutvalue.push('0');
               }
               break;
             default:
               this.cashinoutvalue.push(key);
           }
           this.updateCashKeyedin();         
        },
        updateCashKeyedin:function() {
          var value = this.cashinoutvalue.join('');
          var keylen = value.length;
          if (keylen >0) {
            var cashvalue ='';
            if (keylen>2)
              cashvalue= value.substr(0, keylen-2)+'.'+value.substr(keylen-2, keylen);
            else if(keylen==2)
              cashvalue= '0.'+value.substr(0, keylen); 
            else
              cashvalue= '0.0'+value.substr(0, keylen);
            $('.cashvalue').html(cashvalue);
          }else{
            $('.cashvalue').html('0.00');
          }
        },
        continueStep:function(evt){
          $('.button').removeClass('buttonDown');
          $('.commentwin').css('top',"64px");
          $('#confirmedvalue').html($('.cashvalue').html());
		  $('#confirmedaction').html($('.btn-success').html());
        },
        saveCashInOut:function(evt){
		  var cashInOrOut = $('#confirmedaction').html();
		  var cashValue = $('.cashvalue').html();
		  var commnetbox = $('#commnetbox').val();
		  var saveData = {"type":cashInOrOut,
					   "cash":cashValue,
				       "comment":commnetbox};
		  var param = {"js_callback":"","saveData":saveData};
		  window.JavaConnectJS.send("ClickCashSave",JSON.stringify(param));
        },
        cancelSaveCashInOut:function(evt){
          var self = evt.data.self;
          $('.commentwin').css('top',"100%");
          self.cashinoutvalue = [];
          self.updateCashKeyedin();           
        },
        backMainIndex:function(evt){
            evt.stopImmediatePropagation();            
            window.JavaConnectJS.send("SystemBack",null);
        }
    });

    //exposed for android
    function JsConnectAndroid(funcname, param) {
       if (funcname == "RefreshTables") {
          _refreshTables(param); 
       }
       else if (funcname == "RefreshTableStatus") {
         _refreshTableStatus(param); 
       }
       else if (funcname == "UpdateCashDefault"){
        _refreshCash(param);
       }
    }

    function _refreshCash(param){
        $('#cashdefault').html(param);
    }

    function _loadcashdefault() {
            var param = {"js_callback":"UpdateCashDefault"};
            window.JavaConnectJS.send("Loadcashdefault",JSON.stringify(param));
        }
    
    app.cashinoutview = new app.cashInOutView();
    document.addEventListener('touchmove', function (e) {
        e.preventDefault();
    }, false);
    FastClick.attach(document.body);
    //expose interface to Android
    window.JsConnectAndroid = JsConnectAndroid;
     _loadcashdefault();
})(jQuery);