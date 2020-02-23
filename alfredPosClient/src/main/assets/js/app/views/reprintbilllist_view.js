/**
 * Created by Administrator on 14-3-26.
 */
var app = app || {};
app.maginLeft = $("#billTables").offset().left;
app.currentModel = null;
app.printBillScroll="";
(function ($) {
    //往模板里面添加信息(左侧栏)
    app.reprintbillListItemView = Backbone.View.extend({
        tagName: 'li',
        className:'billlistitem',
        events: {'click': 'showDetailsInfo'},
        template: _.template($('#reprintbillListItem-template').html()),
        initialize: function () {
            this.listenTo(this.model, 'change', this.render);
        },
        showDetailsInfo: function (event) {
         // show detail
            event.stopImmediatePropagation();
            _loadBillDetails(this.model);
            app.reprintbillListAppViews.hidetitle();
            app.detailsView.setBillModel(this.model);
        },        
        render: function () {
            this.$el.append(this.template(this.model.toJSON()));
            return this;
        }
    });
    //绑定DOM节点
    app.reprintbillListAppView = Backbone.View.extend({
        el: '#billTables',
        events: {'click .billlistitem': 'showDetailsInfo'},
        initialize: function () {
            this.listenTo(app.reprintbillListCollection, 'add', this.addOne);
           // this.listenTo(app.reprintbillListCollection, 'reset', this.addAll);
            //this.listenTo(app.reprintbillListCollection, 'all', this.render);
        },
        render: function () {
            this.addAll();
        },
        addOne: function (todo) {
            var view = new app.reprintbillListItemView({ model: todo });
            $('#reprintbillli').append(view.render().el);
        },
        addAll: function () {
            this.$('#reprintbillli').html('');
            app.reprintbillListCollection.each(this.addOne, this);
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
    app.reprintbillListAppViews = new app.reprintbillListAppView();
    app.reprintbillListAppViews.render();

})(jQuery);


function _refreshBillList(param) {
    //load Bill list data
    var data = $.parseJSON(decodeURIComponent(param).replace(new RegExp("\\+","gm")," "));
    app.reprintbillListCollection.add(data);
    app.reprintbillListAppViews.render();
    var heigth=$(window).height()-101;
    $("#tableswrapper").css("height",heigth+"px");
    if(app.printBillScroll==""){
        app.printBillScroll =new IScroll('#tableswrapper', {
            scrollbars: true,
            mouseWheel: true,
            interactiveScrollbars: true,
            shrinkScrollbars: 'scale',
            fadeScrollbars: true });
    }
    app.printBillScroll.refresh();

}


function _parseBillDetails(param){
    //load details data
    var datas = $.parseJSON(decodeURIComponent(param).replace(new RegExp("\\+","gm")," "));
    return datas;
}


function _backtoMain(evt){
    evt.stopImmediatePropagation();
    window.JavaConnectJS.send("SystemBack",null);
}
function _refreshBillDetails(param) {
    var data = _parseBillDetails(param);
    app.billDetailsCollection.reset();
    app.billDetailsCollection.add(data);

    $("#billListContent").addClass('merge-right');
    $("#billDetailsContent").addClass('show-bill-detail');

    $("#billTables").removeClass("maxCol");
    $("#billTables").addClass("minCol");
}

function _loadBillDetails(param) {
    var param = {"js_callback":"UpdateBillDetails","Bill":param};
    window.JavaConnectJS.send("LoadBillDetails",JSON.stringify(param));
}

function _loadBillList(){
    var param = {"js_callback":"UpdateBillList"};

    window.JavaConnectJS.send("LoadBillList",JSON.stringify(param));
}

function _printBill(){
    var param = {"js_callback":null,"Bill":app.currentModel};
    window.JavaConnectJS.send("PrintBill",JSON.stringify(param));
}

//exposed for android
function _JsConnectAndroid(funcname, param) {
    if (funcname == "UpdateBillList") {
        _refreshBillList(param);
    }
    else if (funcname == "UpdateBillDetails") {
        _refreshBillDetails(param);
    }
}
$(document).ready(function(){
    document.addEventListener('touchmove', function (e) {
        e.preventDefault();
    }, false);
    FastClick.attach(document.body);
    window.JsConnectAndroid = _JsConnectAndroid;
        
    app.detailsView = new app.repdAppView({collection: app.billDetailsCollection});

    $("#backBtn").bind('click', _backtoMain);
    _loadBillList();
    if(app.printBillScroll==""){
        app.printBillScroll =new IScroll('#tableswrapper', {
            scrollbars: true,
            mouseWheel: true,
            interactiveScrollbars: true,
            shrinkScrollbars: 'scale',
            fadeScrollbars: true });
    }
    app.printBillScroll.refresh();

});