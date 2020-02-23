/**
 * Created by Administrator on 14-3-26.
 */
var app = app || {};
app.orderScroll="";
app.maginLeft = $("#billTables").offset().left;
(function ($) {
    //往模板里面添加信息(左侧栏)
    app.orderListItemView = Backbone.View.extend({
        tagName: 'li',
        className:'orderlistitem',
        events: {'click': 'showDetailsInfo'},
        template: _.template($('#orderListItem-template').html()),
        initialize: function () {
            this.listenTo(this.model, 'change', this.render);
        },
        showDetailsInfo: function (event) {
            event.stopImmediatePropagation();
            _loadOrderDetails(this.model);
            app.orderListAppViews.hidetitle();
        },
        render: function () {
            this.$el.append(this.template(this.model.toJSON()));
            return this.el;
        }
    });
    //绑定DOM节点
    app.orderListAppView = Backbone.View.extend({
        el: '#billTables',
        titlewidth:0,
        initialize: function () {
            this.listenTo(app.orderListCollection, 'add', this.addOne);
/*
            this.listenTo(app.orderListCollection, 'reset', this.addAll);
            this.listenTo(app.orderListCollection, 'all', this.render);
*/
        },
        render: function () {
            this.addAll();

        },
        addOne: function (todo) {
            var view = new app.orderListItemView({ model: todo });
            $('#orderli').append(view.render());
        },
        addAll: function () {
            this.$('#orderli').html('');
            app.orderListCollection.each(this.addOne, this);
        },
        hidetitle: function(){
            this.titlewidth = $('#wintitle').width();
            $('#wintitle').css("width",'0px');
            $('#wintitle').css("opacity",'0');
        },
        showtitle: function() {
            $('#wintitle').css("width",this.titlewidth);
            $('#wintitle').css("opacity",'1');        
        }          
    });
    app.orderListAppViews = new app.orderListAppView();


})(jQuery);

function _refreshOrderList(param) {
    var data = $.parseJSON(decodeURIComponent(param).replace(new RegExp("\\+","gm")," "));
    app.orderListCollection.add(data['orders']);
    app.orderListAppViews.render();
    var heigth=$(window).height()-101;
    $("#tableswrapper").css("height",heigth+"px");
    if(app.orderScroll==""){
        app.orderScroll  = new IScroll('#tableswrapper', {
            scrollbars: true,
            mouseWheel: true,
            interactiveScrollbars: true,
            shrinkScrollbars: 'scale',
            fadeScrollbars: true });
    }
    app.orderScroll.refresh();
}

function _parseOrderDetails(param){
  var data = $.parseJSON(decodeURIComponent(param).replace(new RegExp("\\+","gm")," "));
  var items = data['ItemDetail'];
  var orders = data['OrderDetails'];
  var itemdetails = {}
  $.each(items, function( idx, item ) {
     itemdetails[item.id] = {'name':item.itemName, "price":item.price};
  });
  
  var ordermodes = [];
  $.each(orders, function( idx, order ) {
     order['name'] = itemdetails[order.itemId].name;
     order['price'] = Number(itemdetails[order.itemId].price).toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
     order['subTotal']= (Number(order['price']) * Number(order.itemNum)).toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
     order['total']= Number(order['price']) * Number(order.itemNum) - Number(order.discountPrice);
     order['total']=order['total'].toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
     ordermodes.push(new app.orderDetailsModel(order));
  });
	    
  return ordermodes;

}
function _backtoMain(evt){
     evt.stopImmediatePropagation();            
     window.JavaConnectJS.send("SystemBack",null);
}
function _refreshOrderDetails(param) {
    var data = _parseOrderDetails(decodeURIComponent(param).replace(new RegExp("\\+","gm")," "));
    app.orderDteailsC.reset();    
    app.orderDteailsC.add(data);
    
    $("#orderListContent").addClass('merge-right');
    $("#orderDetailsContent").addClass('show-order-detail');
    $("#billTables").removeClass("maxCol");
    $("#billTables").addClass("minCol");    
}

function _loadOrderDetails(param) {
       var param = {"js_callback":"UpdateOrderDetails","order":param};
       window.JavaConnectJS.send("LoadOrderDetails",JSON.stringify(param));
}

function _loadOrderList(){
       var param = {"js_callback":"UpdateOrderList"};
       window.JavaConnectJS.send("LoadOrders",JSON.stringify(param));
}  
    
//exposed for android
function _JsConnectAndroid(funcname, param) {
   if (funcname == "UpdateOrderList") {
      _refreshOrderList(param); 
   }
   else if (funcname == "UpdateOrderDetails") {
     _refreshOrderDetails(param); 
   }    
}
    
$(document).ready(function(){
    
    document.addEventListener('touchmove', function (e) {
        e.preventDefault();
    }, false);
       
   FastClick.attach(document.body);
   app.detailsView = new app.orderDetailsAppView({collection: app.orderDteailsC});
   $("#backBtn").bind('click', _backtoMain);

   window.JsConnectAndroid = _JsConnectAndroid;
   _loadOrderList();
    if(app.orderScroll==""){
        app.orderScroll  = new IScroll('#tableswrapper', {
            scrollbars: true,
            mouseWheel: true,
            interactiveScrollbars: true,
            shrinkScrollbars: 'scale',
            fadeScrollbars: true });
    }
    app.orderScroll.refresh();
});
    