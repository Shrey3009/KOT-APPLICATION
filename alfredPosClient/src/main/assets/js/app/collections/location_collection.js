/**
 * Created by Administrator on 14-3-20.
 */
var app = app || {};
(function () {
    app.loColection = Backbone.Collection.extend({
        model: app.locationModel,
        initialize: function () {
            //加载JSON数据
            //this.add(locationTableJson);
        }
    });
    app.locationColection = new app.loColection();
})();

