/**
 * Created by Administrator on 14-3-26.
 */

var app = app || {};

(function ($) {
    app.reprintbillliDetailsItemView = Backbone.View.extend({
        tagName: 'li',
        className:'billlistdetaiksitem',
        template: _.template($('#reprintbilldetailsItem-template').html()),
        initialize: function () {
            this.listenTo(this.model, 'change', this.render);
        },
        render: function () {
            this.$el.html(this.template(this.model.toJSON()));
            return this;
        }
      });

    app.repdAppView = Backbone.View.extend({
        el: '#billDetailsContent',
        events:{'click #closeBtn':'closeBillList',
                'click #printBillBtn': "printBill"},
        currentBill:null,
        initialize: function () {
            this.listenTo(this.collection, 'add', this.addOne);
            this.listenTo(this.collection, 'reset', this.addAll);
            this.listenTo(this.collection, 'all', this.render);
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
        setBillModel:function(bill){
          this.currentBill = bill;
        },
        addOne: function (todo) {
            var view = new app.reprintbillliDetailsItemView({ model: todo });
            $('#listdetails').append(view.render().el);
            
        },
        addAll: function () {
            this.$('#listdetails').html('');
            this.collection.each(this.addOne, this);
        },
        closeBillList:function(evt){
            evt.stopImmediatePropagation(); 
            this.opened = false;
            $("#billTables").removeClass("minCol");
            $("#billTables").addClass("maxCol");
            $("#billListContent").removeClass('merge-right');
            $("#billDetailsContent").removeClass('show-bill-detail');
            app.reprintbillListAppViews.showtitle();
        },
        printBill:function(evt){
            evt.stopImmediatePropagation(); 
		    var param = {"js_callback":null,"Bill":this.currentBill.toJSON()};
    		window.JavaConnectJS.send("PrintBill",JSON.stringify(param));  
        }
    });



})(jQuery);