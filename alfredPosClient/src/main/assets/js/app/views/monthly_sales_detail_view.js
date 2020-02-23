/**
 * Created by Administrator on 14-3-26.
 */
var app = app || {};


function _parseSalesDetails(param){
    var datas = $.parseJSON(decodeURIComponent(param).replace(new RegExp("\\+","gm")," "));
    return datas.time;
}

function _printMonthlyReport(date) {
    var param = {"month":date};
    window.JavaConnectJS.send("LoadMonthlySaleReport",JSON.stringify(param));
}


//exposed for android
function _JsConnectAndroid(funcname, param) {

}


$(document).ready(function(){
/*
    document.addEventListener('touchmove', function (e) {
        e.preventDefault();
    }, false);
*/
    $("#backBtn").bind('click', function(e){
        e.stopImmediatePropagation();
        window.JavaConnectJS.send("SystemBack",null);
    });
    
    FastClick.attach(document.body);

    window.JsConnectAndroid = _JsConnectAndroid;
    $(".divSession").bind("click", function(e){
         var date = $(e.currentTarget).attr("month");
         _printMonthlyReport(date);
    });
});