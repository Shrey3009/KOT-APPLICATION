/**
 * Created by Administrator on 14-3-26.
 */

var app = app || {};

(function ($) {
    //往模板里面添加信息
    app.settlementDetailsItemView = Backbone.View.extend({
        tagName: 'li',
        className: 'settlementlistdetaiksitem',
        template: _.template($('#settlementdetailsItem-template').html()),
        events:{'click .glyphicon-edit':'editSettlement'},
        initialize: function () {
            this.listenTo(this.model, 'change', this.render);
        },
        render: function () {
            this.$el.html(this.template(this.model.toJSON()));
            return this;
        },
        editSettlement:function(){
            var  mod=this.model;
            mod.set({billId:'02020202'});
            this.$el.html(this.template(mod.toJSON()));
        }
    });
    //绑定DOM节点
    app.settlementDetailsAppView = Backbone.View.extend({
        el: '#settlementDetailsContent',
        events:{'click #closeBtn':'closeSettlementList'},
        initialize: function () {
            this.listenTo(this.collection, 'add', this.addOne);
            this.listenTo(this.collection, 'reset', this.reset);
        },
        render: function () {
         //  this.addAll();
        },
        addOne: function (todo) {
            var view = new app.settlementDetailsItemView({ model: todo });
            $('#listdetails').append(view.render().el);
        },
        addAll: function () {
            this.$('#listdetails').html('');
            this.collection.each(this.addOne, this);
        },
        closeSettlementList:function(){

               this.opened = false;
                $("#billTables").removeClass("minCol");
                $("#billTables").addClass("maxCol");
                $("#settlementListContent").removeClass('merge-right');
                $("#settlementDetailsContent").removeClass('show-settlement-detail');
               app.settlementListAppViews.showtitle();
        }
    });



})(jQuery);