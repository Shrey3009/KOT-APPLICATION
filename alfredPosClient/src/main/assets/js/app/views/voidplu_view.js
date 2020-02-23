/**
 * Created by Administrator on 14-3-26.
 */
var app = app || {};
(function ($) {

    app.voidPluListItemView = Backbone.View.extend({
        tagName: 'li',
        className:'bohSetListitem',
        template: _.template($('#voidListItem-template').html()),
        initialize: function () {
            this.listenTo(this.model, 'change', this.render);
        },
        render: function () {
            this.model.set("bizDate",app.bizDate);
            this.$el.html(this.template(this.model.toJSON()));
            return this;
        }
    });

    app.bohSetListAppView = Backbone.View.extend({
        el: '#pluListContent',
        apimode: "local",

        initialize: function () {
            this.listenTo(app.voidPluCollection, 'add', this.addOne);
        },
        render: function () {
            this.addAll();
        },
        addOne: function (todo) {
            var view = new app.voidPluListItemView({ model: todo });
            $('#bohSettli').append(view.render().el);
        },
        addAll: function () {
            this.$('#bohSettli').html('');
            app.voidPluCollection.each(this.addOne, this);
        }
    });
    app.bohSetListAppViews = new app.bohSetListAppView();
    app.bohSetListAppViews.render();

})(jQuery);

function _refreshVoidPlu(param) {
    var data = $.parseJSON(decodeURIComponent(param).replace(new RegExp("\\+","gm")," "));
    app.voidPluCollection.reset();
    if (data!=null) {
      app.total = data.total;
      app.bizDate = data.bizDate;
      app.voidPluCollection.add(data.plu);   
    }
    app.bohSetListAppViews.render(); 
    var myScroll = new IScroll('#tableswrapper', {
        scrollbars: true,
        mouseWheel: true,
        interactiveScrollbars: true,
        shrinkScrollbars: 'scale',
        fadeScrollbars: true });     
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

function _loadVoidPlu(day){
    var param = {"code": 0, "js_callback":"UpdateVoidPlu"};
    window.JavaConnectJS.send("LoadVoidPlu",JSON.stringify(param));
}

//exposed for android
function _JsConnectAndroid(funcname, param) {

    if (funcname == "UpdateVoidPlu") {
        _refreshVoidPlu(param);
    }
}


$(document).ready(function(){
    document.addEventListener('touchmove', function (e) {
        e.preventDefault();
    }, false);
    FastClick.attach(document.body);

    $("#backBtn").bind('click', _backtoMain);
    window.JsConnectAndroid = _JsConnectAndroid;
    _loadVoidPlu(0);
    
    $("#clickPrint").click(function(e){
        e.stopImmediatePropagation();
        window.JavaConnectJS.send("ClickPrint",null);
    });

});
