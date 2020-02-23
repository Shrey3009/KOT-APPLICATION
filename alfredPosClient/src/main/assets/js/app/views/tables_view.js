var app = app || {};

(function ($) {

    //Add into template
    app.tableItemView = Backbone.View.extend({
        className : 'tablecell',
        events:{'click': 'clickTable'},
        template: _.template($('#tablesItem-template').html()),
        initialize: function () {
            this.listenTo(this.model, 'change', this.render);
        },
        render: function () {
            var tst = this.model.get('tableStatus');
            var orders = this.model.get('orders');
            this.$el.html(this.template(this.model.toJSON()));
            if (tst == 1) {
                this.$el.removeClass("pendingtbl");
                if (orders ==0)
                   this.$el.addClass("noordertbl");
                else
                   this.$el.addClass("billingtbl");
            } else if (tst == 2) {
                this.$el.removeClass("billingtbl");
                this.$el.addClass("pendingtbl");
            } else {
                this.$el.removeClass("pendingtbl");
                this.$el.removeClass("billingtbl");
            }
            return this;
        },
  
        clickTable:function(evt){
           var param = {"js_callback":"",
           				 "table":{},
           			   }
           evt.stopImmediatePropagation();
           var tbldiv = $(evt.currentTarget);
	       
           param.table = this.model.getTableObj();
           window.JavaConnectJS.send("SelectTable",JSON.stringify(param));
           
        }

    });
    //bind DOM
    app.tablelistView = Backbone.View.extend({
        el: '#tableList',
        
        initialize: function () {
            this.listenTo(this.collection, 'add', this.addOne);
            this.listenTo(this.collection, 'reset', this.addAll);
            this.listenTo(this.collection, 'all', this.render);
        },
        render: function () {
            this.addAll();
        },
        addOne: function (tableobj) {
            var view = new app.tableItemView({ model: tableobj });
            this.$el.append(view.render().el);
        },
        addAll: function () {
            this.$el.html('');
            this.collection.each(this.addOne, this);
        },
        
        changeMoudel: function (tables) {
            app.collections = this.collection;
            _.each(tables, function (changeTable) {
                app.collections.each(function (model) {
                    if (changeTable.id == model.get('id')) {
                        model.set({"tableStatus": changeTable.tableStatus,"orders":changeTable.orders});
                    }
                });
            });
            app.tableCollection = app.collections;
        }
    });
})(jQuery);
