/**
 * Created by Administrator on 14-3-26.
 */
var app = app || {};
app.bohScroll="";
(function ($) {

    app.bohSetListItemView = Backbone.View.extend({
        tagName: 'li',
        className:'bohSetListitem',
        events: {'click': 'showDetailsInfo'},
        template: _.template($('#bohSettListItem-template').html()),
        initialize: function () {
            this.listenTo(this.model, 'change', this.render);
        },
        render: function () {
            this.$el.html(this.template(this.model.toJSON()));
            return this;
        },
        showDetailsInfo: function (event) {

            event.stopImmediatePropagation();
            app.bohSetListAppViews.hidetitle();

		    $("#bohSettListContent").addClass('merge-right');
		    $("#bohSettDetailsContent").addClass('show-bohsettlement-detail');
		    $("#billTables").removeClass("maxCol");
		    $("#billTables").addClass("minCol");            
            app.bohSettlementViews = new app.bohSettDetailsAppView({"model":this.model});
            app.bohSettlementViews.render();
        }
    });

    app.bohSetListAppView = Backbone.View.extend({
        el: '#bohSettListContent',
        apimode: "local",

        initialize: function () {
            this.listenTo(app.bohSettlementListCollection, 'add', this.addOne);
        },
        render: function () {
            this.addAll();
        },
        addOne: function (todo) {
            var view = new app.bohSetListItemView({ model: todo });
            $('#bohSettli').append(view.render().el);
        },
        addAll: function () {
            this.$('#bohSettli').html('');

            app.bohSettlementListCollection.each(this.addOne, this);
        },
        hidetitle: function(){
            
            this.titlewidth = $('#wintitle').width();
            $('#wintitle').css("width",'0px');
            $('#wintitle').css("opacity",'0');
        },
        showtitle: function() {

            $('#wintitle').css("width",this.titlewidth);
            $('#wintitle').css("opacity",'1');
        },
        setApiMode:function(mode){
          this.apimode = mode;
        },
        isApiModeLocal:function() {
          if (this.apimode == "local")
             return true;
          else
             return false;
        }
    });
    app.bohSetListAppViews = new app.bohSetListAppView();
    app.bohSetListAppViews.render();

})(jQuery);

function _refreshBohSettlementList(param) {
    //load BohSettlement list data
    var data = $.parseJSON(decodeURIComponent(param).replace(new RegExp("\\+","gm")," "));

    app.bohSetListAppViews.setApiMode('remote');
   // if (data.resultCode == 1) {
       app.bohSettlementListCollection.reset();
       app.bohSettlementListCollection.add(data.bohUnpaidList);
    var heigth=$(window).height()-101;
    $("#tableswrapper").css("height",heigth+"px");
       app.bohSetListAppViews.render();
    if(app.bohScroll==""){
        app.bohScroll= new IScroll('#tableswrapper', {
            scrollbars: true,
            mouseWheel: true,
            interactiveScrollbars: true,
            shrinkScrollbars: 'scale',
            fadeScrollbars: true });
    }
    app.bohScroll.refresh();
    //}
}
function _refreshLocalBohSettlementList(param) {
    //load BohSettlement list data
    var data = $.parseJSON(decodeURIComponent(param).replace(new RegExp("\\+","gm")," "));
    app.bohSetListAppViews.setApiMode('local');
    app.bohSettlementListCollection.reset();
    app.bohSettlementListCollection.add(data);
    var heigth=$(window).height()-101;
    $("#tableswrapper").css("height",heigth+"px");
    app.bohSetListAppViews.render();
    if(app.bohScroll==""){
        app.bohScroll= new IScroll('#tableswrapper', {
            scrollbars: true,
            mouseWheel: true,
            interactiveScrollbars: true,
            shrinkScrollbars: 'scale',
            fadeScrollbars: true });
    }
    app.bohScroll.refresh();
}


function _parseBohSettlementDetails(param){
    //load details data
    var datas = $.parseJSON(decodeURIComponent(param).replace(new RegExp("\\+","gm")," "));
    return datas;
}

function _backtoMain(evt){
    evt.stopImmediatePropagation();
    window.JavaConnectJS.send("SystemBack",null);
}

function _loadLocalBohSettlementList() {
    var param = {"js_callback":"UpdateLocalBohSettlementList"};
    window.JavaConnectJS.send("LoadLocalBohSettlementList",JSON.stringify(param));

}
function _loadBohSettlementList(){
    var param = {"js_callback":"UpdateBohSettlementList"};
    window.JavaConnectJS.send("LoadBohSettlementList",JSON.stringify(param));
}

//exposed for android
function _JsConnectAndroid(funcname, param) {

    if (funcname == "UpdateBohSettlementList") {
        _refreshBohSettlementList(param);
    }
    else if (funcname == "UpdateLocalBohSettlementList") {
        _refreshLocalBohSettlementList(param);
    }
}


$(document).ready(function(){
    document.addEventListener('touchmove', function (e) {
        e.preventDefault();
    }, false);
    FastClick.attach(document.body);

    $("#backBtn").bind('click', _backtoMain);
    window.JsConnectAndroid = _JsConnectAndroid;
   //_loadLocalBohSettlementList();
    _loadBohSettlementList();

    //滚动条
    if(app.bohScroll==""){
        app.bohScroll= new IScroll('#tableswrapper', {
            scrollbars: true,
            mouseWheel: true,
            interactiveScrollbars: true,
            shrinkScrollbars: 'scale',
            fadeScrollbars: true });
    }
    app.bohScroll.refresh();
});
