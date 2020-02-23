/**
 * Created by Administrator on 14-3-20.
 */
var app = app || {};
(function () {
    app.locationModel = Backbone.Model.extend({
        default: {
            id: '0',
            placeName: '',
            locationBilling: '',
            locationPending: '',
            tables: ''
        }
    });
})();
