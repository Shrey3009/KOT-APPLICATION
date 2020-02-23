var javaConnectJS = function(){
   function sendFunc(func, param){
      var a=param;
      if (func == "LoadTables") {
        window.JsConnectAndroid("RefreshTables",JSON.stringify(locationTableJson));
      }else if (func=="SelectTable") {
         a = 1;
      }else if(func == "LoadOrders") {
        window.JsConnectAndroid("UpdateOrderList",JSON.stringify(orderlist));
      }else if(func=="LoadOrderDetails") {
        window.JsConnectAndroid("UpdateOrderDetails",JSON.stringify(orderDetails));
      }else if(func=="LoadSettlementList"){
          //load settlement list
          window.JsConnectAndroid("UpdateSettlementList",JSON.stringify(settlementListJson));
      }else if(func=="LoadSettlementDetails"){
          //load settlement details
          window.JsConnectAndroid("UpdateSettlementDetails",JSON.stringify(settlementDetailsJSON));
      }else if(func=="LoadBohSettlementList"){
          //load BohSettlement list
          window.JsConnectAndroid("UpdateBohSettlementList",JSON.stringify(bohremoteJSON));
      }else if(func=="LoadLocalBohSettlementList"){
          //load BohSettlement list
          window.JsConnectAndroid("UpdateLocalBohSettlementList",JSON.stringify(bohSettlementListJSON));
      }else if(func=="SaveLocalBOHPayment"){
          //load BohSettlement list
          alert("save Local BOH:"+param);
      }else if(func=="SaveBOHPayment"){
          //load BohSettlement list
          alert("save Remote BOH:"+param);
      }else if(func=="LoadBillList"){
          //load printBill list
          window.JsConnectAndroid("UpdateBillList",JSON.stringify(printBillListJson));
      }else if(func=="LoadBillDetails"){
          //load printBill details
          window.JsConnectAndroid("UpdateBillDetails",JSON.stringify(printBillDetailsJson));
      } else if(func=="LoadDevices"){
        window.JsConnectAndroid("RefreshDevices",JSON.stringify(params));
      }else if(func=="LoadPrinters"){
        window.JsConnectAndroid("RefreshPrinters",JSON.stringify(allprinters));
      }else if(func=="LoadWaiterDevices"){
        window.JsConnectAndroid("RefreshWaiterDevices",JSON.stringify(waiterTablets));
      }else if (func == "LoadKdsDevices"){
        window.JsConnectAndroid("RefreshKdsDevices",JSON.stringify(kdsTablets));
      }else if(func=="LoadXZReport"){
          window.JsConnectAndroid("GetOderReport",JSON.stringify(getJson));
      }else if (func=="PrintBill") {
         alert("Start Print Bill");
      }else if(func=="LoadSaleDetailReport"){
          window.JsConnectAndroid("GetLoadSaleDetailReport",JSON.stringify(sales_detail));
      }else if(func=="LoadDashboard"){
          window.JsConnectAndroid("UpdateDashboard",JSON.stringify(dashboard));
      }else if (func=="Add_printer_device") {
        alert("sfdasdf");
      }else if(func=="LoadVoidPlu"){
         window.JsConnectAndroid("UpdateVoidPlu",JSON.stringify(voidplu));
      }
      else if(func =="SystemBack"){
        alert("android back");
      } else if (func == "DownloadZReport") {
        window.JsConnectAndroid("ShowZReportDetail", null);
      }
   }; 
   return ({
           send: sendFunc
   });
};

window.JavaConnectJS = new javaConnectJS();


function UpdateSingleTableStatus() {
  var c={"id":25, tables:[{"id":35,"isActive":1,"placesId":25,"restaurantId":19,"revenueId":26,"tableName":"table1","tablePacks":4,"tableStatus":1}]};
  var d = JSON.stringify(c);
  JsConnectAndroid('RefreshTableStatus', d);
}

function RefreshWholeTalbes(){
var ptparams = {"tables":[{"id":35,"isActive":1,"placesId":25,"restaurantId":19,"revenueId":26,"tableName":"table1","tablePacks":4,"tableStatus":1},{"id":36,"isActive":1,"placesId":27,"restaurantId":19,"revenueId":26,"tableName":"ksk","tablePacks":4,"tableStatus":0},{"id":37,"isActive":1,"placesId":25,"restaurantId":19,"revenueId":26,"tableName":"table2","tablePacks":5,"tableStatus":0},{"id":39,"isActive":1,"placesId":25,"restaurantId":19,"revenueId":26,"tableName":"table3","tablePacks":4,"tableStatus":0},{"id":40,"isActive":1,"placesId":25,"restaurantId":19,"revenueId":26,"tableName":"table4","tablePacks":4,"tableStatus":0},{"id":41,"isActive":1,"placesId":25,"restaurantId":19,"revenueId":26,"tableName":"table5","tablePacks":9,"tableStatus":1},{"id":47,"isActive":1,"placesId":27,"restaurantId":19,"revenueId":26,"tableName":"ssssd","tablePacks":4,"tableStatus":0},{"id":77,"isActive":1,"placesId":27,"restaurantId":19,"revenueId":26,"tableName":"sss","tablePacks":4,"tableStatus":0},{"id":82,"isActive":1,"placesId":27,"restaurantId":19,"revenueId":26,"tableName":"ssss","tablePacks":4,"tableStatus":0},{"id":83,"isActive":1,"placesId":27,"restaurantId":19,"revenueId":26,"tableName":"ss","tablePacks":4,"tableStatus":0},{"id":84,"isActive":1,"placesId":27,"restaurantId":19,"revenueId":26,"tableName":"sdsd","tablePacks":4,"tableStatus":0},{"id":85,"isActive":1,"placesId":27,"restaurantId":19,"revenueId":26,"tableName":"sddddddd","tablePacks":4,"tableStatus":0},{"id":86,"isActive":1,"placesId":27,"restaurantId":19,"revenueId":26,"tableName":"sdsdsd","tablePacks":4,"tableStatus":0},{"id":87,"isActive":1,"placesId":27,"restaurantId":19,"revenueId":26,"tableName":"sssss","tablePacks":4,"tableStatus":0},{"id":88,"isActive":1,"placesId":27,"restaurantId":19,"revenueId":26,"tableName":"sssf","tablePacks":4,"tableStatus":0}],"tableStatusInfo":[{"diningNum":0,"id":25,"idleNum":5,"inCheckoutNum":0},{"diningNum":0,"id":27,"idleNum":10,"inCheckoutNum":0},{"diningNum":1,"id":25,"idleNum":4,"inCheckoutNum":0},{"diningNum":0,"id":27,"idleNum":10,"inCheckoutNum":0},{"diningNum":1,"id":25,"idleNum":4,"inCheckoutNum":0},{"diningNum":0,"id":27,"idleNum":10,"inCheckoutNum":0},{"diningNum":1,"id":25,"idleNum":4,"inCheckoutNum":0},{"diningNum":0,"id":27,"idleNum":10,"inCheckoutNum":0}],"places":[{"id":25,"isActive":1,"placeDescription":"32","placeName":"kk","restaurantId":19,"revenueId":26},{"id":27,"isActive":1,"placeDescription":"lll","placeName":"kkk","restaurantId":19,"revenueId":26}]}
   window.JsConnectAndroid("RefreshTables",JSON.stringify(ptparams));
}