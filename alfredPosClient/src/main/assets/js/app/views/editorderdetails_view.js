/**
 * Created by Administrator on 14-3-26.
 */

var app = app || {};
(function ($) {
    //往模板里面添加信息

    app.orderDetailsItemView = Backbone.View.extend({
        opened: false,
        tagName:'li',
        className : 'orderlistdetaiksitem',
        events:{ 'click .badge':'editQty',
            'click .glyphicon-edit ':'modifyOrder',
            'click .glyphicon-tag':'remarkOrder',
            'click .glyphicon-trash':'deleteOrder'
        },
        template: _.template($('#orderdetailsItem-template').html()),
        initialize: function () {
            this.opened = false;
            this.listenTo(this.model, 'change', this.render);
            this.listenTo(this.model, 'destroy', this.remove);
        },
        render: function () {
            this.$el.append(this.template(this.model.toJSON()));
            return this;
        },
        editQty:function(e){
            /* var s=  this.model;
             var delm= new  app.orderDetailsModel({id:s.cid});
             alert(delm.name);*/
            var  mod=this.model;
           //send par get model

            var param= _editOrderQty(mod);
            mod.set(param);
            this.$el.html(this.template(mod.toJSON()));
        },
        modifyOrder:function(){

            alert('modify');

        },
        remarkOrder:function(){
            alert('remark');

        },
        deleteOrder:function(){

            var  mod=this.model;
            // mod.destroy();
            this.$el.hide();

        }

    });


    //绑定DOM节点
    app.orderDetailsAppView = Backbone.View.extend({
        el: '#orderDetailsContent',
        events:{'click #closeBtn':'closeOrderList'},
        initialize: function () {
            this.listenTo(this.collection, 'add', this.addOne);
            this.listenTo(this.collection, 'reset', this.reset);
        },
        render: function () {
            this.addAll();
            var heigth=$(window).height()-101;
            $("#tablesdetailswrapper").css("height",heigth+"px");
            var myScroll2 = new IScroll('#tablesdetailswrapper', {
                scrollbars: true,
                mouseWheel: true,
                interactiveScrollbars: true,
                shrinkScrollbars: 'scale',
                fadeScrollbars: true });

        },
        addOne: function (todo) {
            var view = new app.orderDetailsItemView({ model: todo });
            $('#listdetails').append(view.render().el);
        },
        reset:function() {
         this.$('#listdetails').html('');
        },
        addAll: function () {
            this.$('#listdetails').html('');
            this.collection.each(this.addOne, this);
        },
        closeOrderList:function(){
            this.opened = false; 
            $("#billTables").removeClass("minCol");
            $("#billTables").addClass("maxCol");
            $("#orderListContent").removeClass('merge-right');
            $("#orderDetailsContent").removeClass('show-order-detail');
            app.orderListAppViews.showtitle();
        }        
    });



})(jQuery);

//点击事件后调用安卓方法
function _editOrderQty(param){
    var param = {"js_callback":"UpdateOrderQty","order":param};
    return param;
}








