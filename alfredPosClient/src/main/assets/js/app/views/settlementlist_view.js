/**
 * Created by Administrator on 14-3-26.
 */
var app = app || {};
app.maginLeft = $("#billTables").offset().left;
app.setmentScroll="";
(function ($) {
    //往模板里面添加信息(左侧栏)
    app.settlementListItemView = Backbone.View.extend({
        tagName: 'li',
        className: 'settlementlistitem',
        template: _.template($('#settlementListItem-template').html()),
        events:{'click':'editSettlement'},
        initialize: function () {
            this.listenTo(this.model, 'change', this.render);
        },
        render: function () {
            this.$el.append(this.template(this.model.toJSON()));
            return this;
        },
        editSettlement:function(event){
            event.stopImmediatePropagation();
            _editSettlement(this.model);       
        }
    });

    //绑定DOM节点
    app.settlementListAppView = Backbone.View.extend({
        el: '#billTables',
        events: {'click .settlementlistitem': 'showDetailsInfo'},
        initialize: function () {
            this.listenTo(app.settlementListCollection, 'add', this.addOne);
           // this.listenTo(app.settlementListCollection, 'reset', this.addAll);
           // this.listenTo(app.settlementListCollection, 'all', this.render);
        },
        addOne: function (todo) {
            var view = new app.settlementListItemView({ model: todo });
            $('#settlementli').append(view.render().el);
        },
        addAll: function () {
            this.$('#settlementli').html('');
            app.settlementListCollection.each(this.addOne, this);
        }
    });
    app.settlementListAppViews = new app.settlementListAppView();
    app.settlementListAppViews.render();


})(jQuery);

function _refreshSettlementList(param) {
    //load settlement list data
    var data = $.parseJSON(decodeURIComponent(param).replace(new RegExp("\\+","gm")," "));
    app.settlementListCollection.add(data);
    app.settlementListAppViews.render();

    if(app.setmentScroll==""){
        app.setmentScroll= new IScroll('#tableswrapper', {
            scrollbars: true,
            mouseWheel: true,
            interactiveScrollbars: true,
            shrinkScrollbars: 'scale',
            fadeScrollbars: true });
    }
    app.setmentScroll.refresh();


}


function _parseSettlementDetails(param){
    //load details data
    var datas = $.parseJSON(decodeURIComponent(param).replace(new RegExp("\\+","gm")," "));
    return datas;
}


function _backtoMain(evt){
    evt.stopImmediatePropagation();
    window.JavaConnectJS.send("SystemBack",null);
}
function _editSettlementDetails(param) {
    var data = _parseSettlementDetails(param);
}

function _editSettlement(param) {
    var param = {"js_callback":"EditSettlementDone","settlement":param};
    window.JavaConnectJS.send("EditSettlement",JSON.stringify(param));
}

function _loadSettlementList(){
    var param = {"js_callback":"UpdateSettlementList"};
    window.JavaConnectJS.send("LoadSettlementList",JSON.stringify(param));
}

//exposed for android
function _JsConnectAndroid(funcname, param) {
    if (funcname == "UpdateSettlementList") {
        _refreshSettlementList(param);
    }
    else if (funcname == "EditSettlementDone") {
        _editSettlementDetails(param);
    }
}


$(document).ready(function(){
    document.addEventListener('touchmove', function (e) {
        e.preventDefault();
    }, false);
    FastClick.attach(document.body);
    $("#backBtn").bind('click', _backtoMain);
    window.JsConnectAndroid = _JsConnectAndroid;
    _loadSettlementList();
    if(app.setmentScroll==""){
        app.setmentScroll= new IScroll('#tableswrapper', {
            scrollbars: true,
            mouseWheel: true,
            interactiveScrollbars: true,
            shrinkScrollbars: 'scale',
            fadeScrollbars: true });
    }
    app.setmentScroll.refresh();

/*    var heigth=$(window).height()-101;
    $("#tableswrapper").css("height",heigth+"px");*/

});