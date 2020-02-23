/**
 * Created by Administrator on 14-3-26.
 */
var app = app || {};
(function () {
    app.vpluCollection = Backbone.Collection.extend({
        model: app.voidPluModel,
        initialize: function () {
            //加载JSON数据
            //this.add(json);
        }
    });
    app.voidPluCollection=new app.vpluCollection();
})();
