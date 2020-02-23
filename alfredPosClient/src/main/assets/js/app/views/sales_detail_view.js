
SESSIONS = {'0': "Whole Day",
			'1': "Breakfast",
			'2': "Lunch",
			'3': "Dinner",
			'4': "Supper"};

Date.prototype.addDays = function(days) {
       var dat = new Date(this.valueOf())
       dat.setDate(dat.getDate() + days);
       return dat;
}
   
/**
 * Created by Administrator on 14-3-26.
 */
var app = app || {};
(function ($) {
    app.salesDetailListItemView = Backbone.View.extend({
        tagName: 'li',
        className: 'col-md-3 divLi',
        template: _.template($('#salesDetailListItem-template').html()),
        events:{'click .zsection': 'showZReportDetail',
                'click .divSession': 'showDetail',
                'click .divDownload':'downloadReport'},
        initialize: function () {
            this.listenTo(this.model, 'change', this.render);
        },
        render: function () {
            this.$el.append(this.template(this.model.toJSON()));
            return this;
        },
        showDetail:function(event){
            event.stopImmediatePropagation();
            var date = $(event.currentTarget).attr("bizdate");
            var sid = $(event.currentTarget).attr("sid");
            _showDetail(this.model,date,sid);
        },
        downloadReport:function(event) {
          event.stopImmediatePropagation();
          var date = $(event.currentTarget).attr("bizdate");
          _downloadZReport(date);
        },
        showZReportDetail:function(event) {
            event.stopImmediatePropagation();
            var date = $(event.currentTarget).attr("bizdate");
            document.location = "reportdetail.html?date="+date+"&x=&z=1";       
        }
    });

    //绑定DOM节点
    app.sdlListAppView = Backbone.View.extend({
        el: '#salesDetailDiv',
        events: {'click .settlementlistitem': 'showDetailsInfo'},
        initialize: function () {
            this.listenTo(app.salesDetailC, 'add', this.addOne);
        },
        addOne: function (todo) {
            var view = new app.salesDetailListItemView({ model: todo });
            $('#salesDetailUl').append(view.render().el);
        },
        addAll: function () {
            app.salesDetailC.each(this.addOne, this);
        }
    });
    app.salesDetailtView = new app.sdlListAppView();
    app.salesDetailtView.render();

})(jQuery);

function _refreshSalesDetailList(param) {
    var data = $.parseJSON(decodeURIComponent(param).replace(new RegExp("\\+","gm")," "));
    var datas=  returnMouthDate(data);
    app.salesDetailC.add(datas);
    app.salesDetailtView.render();
}

function _parseSalesDetails(param){
    var datas = $.parseJSON(decodeURIComponent(param).replace(new RegExp("\\+","gm")," "));
    return datas.time;
}

function _showSalesDetails(param) {
    var data = _parseSalesDetails(param);
}

function _showDetail(param,date,sid) {
  document.location = "reportdetail.html?date="+date+"&x="+sid+"&z=";

}

function _loadSalesDetailList(){
    var param = {"js_callback":"GetLoadSaleDetailReport"};
    window.JavaConnectJS.send("LoadSaleDetailReport",JSON.stringify(param));
}

function _downloadZReport(bizDate) {
    var param = {"js_callback":"ShowZReportDetail", "bizDate":bizDate};
    window.JavaConnectJS.send("DownloadZReport",JSON.stringify(param));
}

function _showZReportDetail(param) {
  if (param != null) {
	  var data = $.parseJSON(decodeURIComponent(param).replace(new RegExp("\\+","gm")," "));
	  var date = data['bizDate'];
	  
	  if (data['sales']==undefined || data['sales']==null) {
	     var ll = $.find('.divDownload');
	     $('.divDownload').each(function(index){
	       if ($(this).attr('bizDate')==date) {
	         var label = $($(this).find('.loadLabel')[0]);
	         label.html('No Sales');
	         return;
	       }
	     });
	     
	  } else {
	
	    document.location = "reportdetail.html?date="+date+"&x=&z=1"; 
	  }
  }
}
//exposed for android
function _JsConnectAndroid(funcname, param) {
    if (funcname == "GetLoadSaleDetailReport") {
        _refreshSalesDetailList(param);
    }
    if (funcname == "ShowZReportDetail") {
      _showZReportDetail(param);
    }
}

function getSaleData(reportData, time) {
   var item = reportData[time];
   var obj={};

   obj.bizDate = time;
   obj.wholedayAmount = null;
   obj.breakfastAmount = null;
   obj.lunchAmount = null;
   obj.dinnerAmount=null;
   obj.supperAmount = null;
   obj.zNettSale = null;
   
   if (item !=undefined && item!=null) {
      
      if (item['x']!=null) {
        var session = item['x'];
        if (session!=null) {
		   obj.wholedayAmount = session['0'];
		   obj.breakfastAmount = session['1'];
		   obj.lunchAmount = session['2'];
		   obj.dinnerAmount=session['3'];
		   obj.supperAmount = session['4'];
        }
      }
      if (item['z']!=null) {
        obj.zNettSale = item['z'];
      }            		
   }
   return obj;
}
function returnMouthDate(data){
    
    if (data==null)
      return;
      
    var time = data.bizDateNow;
    var reportData = data.result;
    
    var t = new Date(time);
    var iToDay=t.getDay();
    var iToMon=t.getMonth();
    var iToYear=t.getFullYear();
    var dataArr=new Array();

    for(var i=0;i<30;i++){
        var dateOffset = (24*60*60*1000) * i; //5 days
        var myDate = new Date();
        myDate.setTime(t.getTime() - dateOffset);
        var time = myDate.getTime();
        var obj=getSaleData(reportData,time);
        
        if (i==0) {
	        obj.time= "Today";

        }else if (i==1) {
           obj.time = "Yesterday";
        }else{
        	var time=myDate.toLocaleDateString();
        	obj.time=time;

        }
        dataArr.push(obj);
    }
  return dataArr;

}


$(document).ready(function(){
/*
    document.addEventListener('touchmove', function (e) {
        e.preventDefault();
    }, false);
*/
    $("#backBtn").bind('click', function(e){
        e.stopImmediatePropagation();
        window.JavaConnectJS.send("SystemBack",null);
    });
    
    FastClick.attach(document.body);

    window.JsConnectAndroid = _JsConnectAndroid;
    _loadSalesDetailList();


});