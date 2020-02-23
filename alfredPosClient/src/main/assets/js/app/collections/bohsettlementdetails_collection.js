/**
 * Created by Administrator on 14-3-26.
 */
var app = app || {};
(function () {

    app.bohSetDetaCollection = Backbone.Collection.extend({
        model: app.bohSetDetaModel,
        initialize: function () {
            //加载JSON数据
           // this.add(json);
        }
    });
    app.bohSettlementetailsCollection=new app.bohSetDetaCollection();
})();
