
var app = app || {};
(function () {
    app.tablesModel = Backbone.Model.extend({
        default:{
            placesId:'0',
            id:'0',
            tableName:'',
            orders:0,
            tableStatus:0
        },
        getTableObj: function() {
            var ret = {
                "id":this.get('id'),
                "isActive":this.get('isActive'),
                "placesId":this.get('placesId'),
                "restaurantId":this.get('restaurantId'),
                "revenueId":this.get('revenueId'),
                "tableStatus":this.get('tableStatus'),
                "tableName":this.get('tableName'),
                "orders":this.get("orders")
            };
            return ret;          
        }
    });
})();
