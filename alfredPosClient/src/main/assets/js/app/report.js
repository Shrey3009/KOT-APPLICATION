SESSIONS = {'0': "Whole Day",
			'1': "Breakfast",
			'2': "Lunch",
			'3': "Dinner",
			'4': "Supper"};
			
function getUrlVars()
{
    var vars = [], hash;
    var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
    for(var i = 0; i < hashes.length; i++)
    {
        hash = hashes[i].split('=');
        vars.push(hash[0]);
        vars[hash[0]] = hash[1];
    }
    return vars;
}

/**
 * Created by Administrator on 14-6-9.
 */
//点击事件
$("#clickButLi").click(function(){
$("#selectBut").css("display","block");
});

$("#selectBut li").click(function(){
    $("#selectBut").css("display","none");
    var  liVal=$(this).val();
    if(liVal==0){
        $("#setValue").text('Today');
    }else if(liVal==-1){
        $("#setValue").text('Yesterday');
    }else if(liVal==-2){
        $("#setValue").text('The Day Before Yesterday');
    }
    var param = {"js_callback":"GetOderReport","code":liVal};
    window.JavaConnectJS.send("LoadXZReport",JSON.stringify(param));
});
var salesAnScroll="";
var hourScroll= "";
var detailScroll="";
var summaryScroll="";

//report details;
var report_bizDate = null;
var report_session = null;
var report_z = false; 

    $(document).ready(function() {

    document.addEventListener('touchmove', function (e) {
        e.preventDefault();
    }, false);
    FastClick.attach(document.body);  
    
    var param = getUrlVars();
    
    report_bizDate = param['date'];
    report_session = param['x'];
    var rz = param['z'].length;
    if (rz>0) {
       report_session = null;
       report_z = true;
       $("#reporttitle").html('Day Sales Report');
    } else {
       report_z = false;
       $("#reporttitle").html(SESSIONS[report_session]+' Report');
    }
          
    //call android
    window.JsConnectAndroid = _JsConnectAndroid;
    _loadOrderReport(report_bizDate,report_z, report_session);

    salesAnScroll= new IScroll('#salesAnalysisWrapper', {scrollY: true, mouseWheel: true });
    hourScroll=  new IScroll('#hourlySalesWrapper', {scrollY: true, mouseWheel: true });
    detailScroll=new IScroll('#detailAnalysisWrapper', {scrollY: true, mouseWheel: true });
    summaryScroll= new IScroll('#summaryAnalysisWrapper', {scrollY: true, mouseWheel: true });


    $("#backBtn").click(function(e){
        e.stopImmediatePropagation();
        e.preventDefault();
        history.back(1);
    });

    $("#clickPrint").click(function(e){
        e.stopImmediatePropagation();
        var param = {"bizDate":report_bizDate,"z":report_z};
        window.JavaConnectJS.send("ClickPrint",JSON.stringify(param));
    });

});
//load order report
function _refreshOrderReoprt(param) {

    var data  = $.parseJSON(decodeURIComponent(param).replace(new RegExp("\\+","gm")," "));
    var getJson=data.reportDaySales;
    var getReportPluDayItem=data.reportPluDayItems;
    var getReportDayTaxs=data.reportDayTaxs;
    var getReportHourly=data.reportHourlys;

    showItems(getReportPluDayItem);
    showSalesAnalysis(getJson,getReportDayTaxs);
    showHourly(getReportHourly);


}

/**
 *PLU
 * @param getReportPluDayItem
 */
function showItems(getReportPluDayItem){
    var detailLi="";
    var summaryLi="";
    var detailsTal='';
    var summaryTal='';
    var detailTotalQty=0;
    var datailTotalAmount=0.00;
    var summaryTotalQty=0;
    var summaryTotalAmount=0.00;
    if(getReportPluDayItem!=undefined && getReportPluDayItem.length>0){
        var result = {};
        var detailsResult={};
        for(var j=0;j<getReportPluDayItem.length;j++){
            var summary = getReportPluDayItem[j];
            var itemMainCategoryName=summary.itemMainCategoryName;
            if (detailsResult.hasOwnProperty(itemMainCategoryName)){
                var item = {};
                item['itemName'] = summary['itemName'];
                item['amount'] =parseFloat(summary['itemAmount']).toFixed(2);
                item['itemCount'] = summary['itemCount'];
                detailsResult[itemMainCategoryName].push(item);
            }else {
                detailsResult[itemMainCategoryName] = [];
                var item = {};
                item['itemName'] = summary['itemName'];
                item['amount'] =parseFloat(summary['itemAmount']).toFixed(2);
                item['itemCount'] = summary['itemCount'];
                detailsResult[itemMainCategoryName].push(item);
            }
        }
        for(var i=0;i<getReportPluDayItem.length;i++){
            var catgory = getReportPluDayItem[i];
            var main=getReportPluDayItem[i].itemMainCategoryName;
            if (result.hasOwnProperty(main)){
                var item = {};
                item['itemName'] = catgory['itemCategoryName'];
                item['itemPrice'] = catgory['itemPrice'];
                item['amount'] =parseFloat(catgory['itemAmount']).toFixed(2);
                item['itemCount'] = catgory['itemCount'];
                result[main].push(item);
            }else {
                result[main] = [];
                var item = {};
                item['itemName'] = catgory['itemCategoryName'];
                item['itemPrice'] = parseFloat(catgory['itemPrice']);
                item['amount'] =parseFloat(catgory['itemAmount']).toFixed(2);
                item['itemCount'] =parseInt(catgory['itemCount']);
                result[main].push(item);
            }
        }

//数据存储
        var resultTemplete={};
        var temp={};
        for (var key in result) {
            var obj = result[key];
            for (var prop in obj) {
                var temName=obj[prop].itemName;
                if(temp.hasOwnProperty(temName)){
                }else{
                    var items={};
                    if(resultTemplete[key]==undefined){
                        resultTemplete[key]=[];
                    };
                    temp[temName]=[];
                    items['itemName'] = temName;
                    temp[temName].push(items);
                    resultTemplete[key].push(items);
                }
            }
        }
        //数据遍历
        var resultFinaly={};
        for (var keys in resultTemplete) {
            var objs = resultTemplete[keys];
            resultFinaly[keys]=[];
            for (var is=0;is<objs.length;is++) {

                var subcategoryname= objs[is].itemName;

                var item = {};
                item['itemName'] =subcategoryname;
                item['amount'] =0.00;
                item['itemCount'] =0;

                for(var i=0;i<getReportPluDayItem.length;i++){
                    //统计相同的菜
                    var catgory = getReportPluDayItem[i];
                    var main=getReportPluDayItem[i].itemCategoryName;

                    if(main==subcategoryname){
                        item['amount']  +=parseFloat(catgory['itemAmount']);
                        item['itemCount'] += (parseInt(catgory['itemCount']));

                    }
                }
                resultFinaly[keys].push(item);
            }
        }


        for (var key in resultFinaly) {
            summaryLi+='<li class="reportitem">';
            summaryLi+= '<div class="row">';
            summaryLi+= '<div class="col-md-12">'+key+'</div>';
            summaryLi+=  '</div>'
            summaryLi+= '</li>'
            var obj = resultFinaly[key];
            for (var i=0;i<obj.length;i++) {
                summaryTotalQty+=obj[i].itemCount;
                summaryTotalAmount+=parseFloat(obj[i].amount);
                summaryLi+='<li class="summaryitemworp">';
                summaryLi+= '<div class="row">';
                summaryLi+= '<div class="col-md-5 wordWarp">'+obj[i].itemName+'</div>';
                summaryLi+=' <div class="col-md-3 wordWarp">'+obj[i].itemCount+'</div>';
                summaryLi+= '<div class="col-md-4 wordWarp">$'+((obj[i].amount==''||obj[i].amount=='0')?'0.00':parseFloat(obj[i].amount).toFixed(2))+'</div>';
                summaryLi+=  '</div>'
                summaryLi+= '</li>'
            }
        }

        for (var keys in detailsResult) {
            detailLi+='<li class="reportitemworp">';
            detailLi+= '<div class="row">';
            detailLi+= '<div class="col-md-12">'+keys+'</div>';
            detailLi+=  '</div>'
            detailLi+= '</li>'
            var objs = detailsResult[keys];
            for (var is=0;is<objs.length;is++) {
                detailTotalQty+=parseInt(objs[is].itemCount);
                datailTotalAmount+=((objs[is].amount==''||objs[is].amount=='0')?'0.00':parseFloat(objs[is].amount));
                detailLi+='<li class="reportitemworp">';
                detailLi+= '<div class="row">';
                detailLi+= '<div class="col-md-5 wordWarp">'+objs[is].itemName+'</div>';
                detailLi+=' <div class="col-md-3 wordWarp">'+objs[is].itemCount+'</div>';
                detailLi+= '<div class="col-md-4 wordWarp">$'+((objs[is].amount==''||objs[is].amount=='0')?'0.00':parseFloat(objs[is].amount).toFixed(2)) +'</div>';
                detailLi+=  '</div>'
                detailLi+= '</li>'
            }
        }
        $("#detailAnalysisli").html(detailLi);
        $("#summaryAnalysisli").html(summaryLi);
        if(detailScroll==""){
            detailScroll=new IScroll('#detailAnalysisWrapper', {scrollY: true, mouseWheel: true });
        }
        detailScroll.refresh();
        if(summaryScroll==""){
            summaryScroll= new IScroll('#summaryAnalysisWrapper', {scrollY: true, mouseWheel: true });
        }
        summaryScroll.refresh();
        //统计总数
    }else{

        summaryLi+='<li>';
        summaryLi+= '<div class="row">';
        summaryLi+= '<div class="col-md-12"></div>';
        summaryLi+=  '</div>'
        summaryLi+= '</li>'
        detailLi+='<li>';
        detailLi+= '<div class="row">';
        detailLi+= '<div class="col-md-12"></div>';
        detailLi+=  '</div>'
        detailLi+= '</li>'
        $("#detailAnalysisli").html(detailLi);
        $("#summaryAnalysisli").html(summaryLi);


    }


    detailsTal+='<div class="col-md-5">Total</div>' ;
    detailsTal+='<div class="col-md-3">'+detailTotalQty+'</div>' ;
    detailsTal+='<div class="col-md-4">$'+parseFloat(datailTotalAmount).toFixed(2)+'</div>';
    $("#detailsTotal").html(detailsTal);

    summaryTal+='<div class="col-md-5">Total</div>' ;
    summaryTal+='<div class="col-md-3">'+summaryTotalQty+'</div>' ;
    summaryTal+='<div class="col-md-4">$'+parseFloat(summaryTotalAmount).toFixed(2)+'</div>';
    $("#summaryTotal").html(summaryTal);


}


/**
 * 菜的销售详情
 * @param getJson
 * @param getReportDayTaxs
 */
function showSalesAnalysis(getJson,getReportDayTaxs){
    var liList="";
    var bilvalue="0";
    var totalOpenItem="0.00";
    if(getJson!=undefined){
        //数据封装
        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">Item Sales</div>';
        liList+='<div class="col-md-2">'+(getJson.itemSalesQty==''?'0':getJson.itemSalesQty)+'</div>';
        liList+='<div class="col-md-4">$'+((getJson.itemSales==''||getJson.itemSales=='0')?'0.00':getJson.itemSales)+'</div>';
        liList+='</div>';
        liList+='</li>';

        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">Stored-Card Sales</div>';
        liList+='<div class="col-md-2">'+(getJson.topUpsQty==''?'0':getJson.topUpsQty)+'</div>';
        liList+='<div class="col-md-4">$'+((getJson.topUps==''||getJson.topUps=='0')?'0.00':getJson.topUps)+'</div>';
        liList+='</div>';
        liList+='</li>';

        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">ENT Items</div>';
        liList+='<div class="col-md-2">'+(getJson.focItemQty==''?'0':getJson.focItemQty)+'</div>';
        liList+='<div class="col-md-4">$'+((getJson.focItem==''||getJson.focItem=='0')?'0.00':getJson.focItem)+'</div>';
        liList+='</div>';
        liList+='</li>';

        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">ENT Bills/$</div>';
        liList+='<div class="col-md-2">'+(getJson.focBillQty==''?'0':getJson.focBillQty)+'</div>';
        liList+='<div class="col-md-4">$'+((getJson.focBill==''||getJson.focBill=='0')?'0.00':getJson.focBill)+'</div>';
        liList+='</div>';
        liList+='</li>';

        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">VOID/Items</div>';
        liList+='<div class="col-md-2">'+(getJson.itemVoidQty==''?'0':getJson.itemVoidQty)+'</div>';
        liList+='<div class="col-md-4">$'+((getJson.itemVoid==''||getJson.itemVoid=='0')?'0.00':getJson.itemVoid)+'</div>';
        liList+='</div>';
        liList+='</li>';
                
        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">VOID Bills$</div>';
        liList+='<div class="col-md-2">'+(getJson.billVoidQty==''?'0':getJson.billVoidQty)+'</div>';
        liList+='<div class="col-md-4">$'+((getJson.billVoid==''||getJson.billVoid=='0')?'0.00':getJson.billVoid)+'</div>';
        liList+='</div>';
        liList+='</li>';


        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">REFUND Bills$</div>';
        liList+='<div class="col-md-2">'+(getJson.billRefundQty==''?'0':getJson.billRefundQty)+'</div>';
        liList+='<div class="col-md-4">$'+((getJson.billRefund==''||getJson.billRefund=='0')?'0.00':getJson.billRefund)+'</div>';
        liList+='</div>';
        liList+='</li>';


        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">REFUND Taxes$</div>';
        liList+='<div class="col-md-2"></div>';
        liList+='<div class="col-md-4">$'+((getJson.refundTax==''||getJson.refundTax=='0')?'0.00':getJson.refundTax)+'</div>';
        liList+='</div>';
        liList+='</li>';

        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">Discount on %</div>';
        liList+='<div class="col-md-2">'+(getJson.discountPerQty==''?'0':getJson.discountPerQty)+'</div>';
        liList+='<div class="col-md-4">$'+((getJson.discountPer==''||getJson.discountPer=='0')?'0.00':getJson.discountPer)+'</div>';
        liList+='</div>';
        liList+='</li>';

        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">Discount on $</div>';
        liList+='<div class="col-md-2">'+(getJson.discountQty==''?'0':getJson.discountQty)+'</div>';
        liList+='<div class="col-md-4">$'+((getJson.discount==''||getJson.discount=='0')?'0.00':getJson.discount)+'</div>';
        liList+='</div>';
        liList+='</li>';

        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">Exlusive Tax$</div>';
        liList+='<div class="col-md-2"></div>';
        var totalTax = Number(getJson.totalTax);
        liList+='<div class="col-md-4">$'+((totalTax==''||totalTax=='0')?'0.00':totalTax)+'</div>';
        liList+='</div>';
        liList+='</li>';

        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">Inclusive Tax$</div>';
        liList+='<div class="col-md-2"></div>';
        var inclusiveTaxAmt = Number(getJson.inclusiveTaxAmt);
        liList+='<div class="col-md-4">$'+((inclusiveTaxAmt==''|| inclusiveTaxAmt=='0')?'0.00':inclusiveTaxAmt)+'</div>';
        liList+='</div>';
        liList+='</li>';

        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">Total/Sales</div>';
        liList+='<div class="col-md-2"></div>';
        liList+='<div class="col-md-4">$'+((getJson.totalSales==''||getJson.totalSales=='0')?'0.00':getJson.totalSales)+'</div>';

        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-12">---------MEDIA-------------</div>';
        liList+='</div>';
        liList+='</li>';

        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">CASH</div>';
        liList+='<div class="col-md-2">'+(getJson.cashQty==''?'0':getJson.cashQty)+'</div>';
        liList+='<div class="col-md-4">$'+((getJson.cash==''||getJson.cash=='0')?'0.00':getJson.cash)+'</div>';
        liList+='</div>';
        liList+='</li>';

        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">Diner App</div>';
        liList+='<div class="col-md-2">'+(getJson.paypalpayQty==''?'0':getJson.paypalpayQty)+'</div>';
        liList+='<div class="col-md-4">$'+((getJson.paypalpay==''||getJson.paypalpay=='0')?'0.00':getJson.paypalpay)+'</div>';
        liList+='</div>';
        liList+='</li>';

        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">Stored-Card Use</div>';
        liList+='<div class="col-md-2">'+(getJson.storedCardQty==''?'0':getJson.storedCardQty)+'</div>';
        liList+='<div class="col-md-4">$'+((getJson.storedCard==''||getJson.storedCard=='0')?'0.00':getJson.storedCard)+'</div>';
        liList+='</div>';
        liList+='</li>';

        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">Stored-Card Charge</div>';
        liList+='<div class="col-md-2">'+(getJson.topUpsQty==''?'0':getJson.topUpsQty)+'</div>';
        liList+='<div class="col-md-4">$'+((getJson.topUps==''||getJson.topUps=='0')?'0.00':getJson.topUps)+'</div>';
        liList+='</div>';
        liList+='</li>';

        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">NETS</div>';
        liList+='<div class="col-md-2">'+(getJson.netsQty==''?'0':getJson.netsQty)+'</div>';
        liList+='<div class="col-md-4">$'+((getJson.nets==''||getJson.nets=='0')?'0.00':getJson.nets)+'</div>';
        liList+='</div>';
        liList+='</li>';


        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">VISA</div>';
        liList+='<div class="col-md-2">'+(getJson.visaQty==''?'0':getJson.visaQty)+'</div>';
        liList+='<div class="col-md-4">$'+((getJson.visa==''||getJson.visa=='0')?'0.00':getJson.visa)+'</div>';
        liList+='</div>';
        liList+='</li>';


        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">MC</div>';
        liList+='<div class="col-md-2">'+(getJson.mcQty==''?'0':getJson.mcQty)+'</div>';
        liList+='<div class="col-md-4">$'+((getJson.mc==''||getJson.mc=='0')?'0.00':getJson.mc)+'</div>';
        liList+='</div>';
        liList+='</li>';


        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">AMEX</div>';
        liList+='<div class="col-md-2">'+(getJson.amexQty==''?'0':getJson.amexQty)+'</div>';
        liList+='<div class="col-md-4">$'+((getJson.amex==''||getJson.amex=='0')?'0.00':getJson.amex)+'</div>';
        liList+='</div>';
        liList+='</li>';



        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">JBL</div>';
        liList+='<div class="col-md-2">'+(getJson.jblQty==''?'0':getJson.jblQty)+'</div>';
        liList+='<div class="col-md-4">$'+((getJson.jbl==''||getJson.jbl=='0')?'0.00':getJson.jbl)+'</div>';
        liList+='</div>';
        liList+='</li>';


        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">unionPayQty</div>';
        liList+='<div class="col-md-2">'+(getJson.unionPayQty==''?'0':getJson.unionPayQty)+'</div>';
        liList+='<div class="col-md-4">$'+((getJson.unionPay==''||getJson.unionPay=='0')?'0.00':getJson.unionPay)+'</div>';
        liList+='</div>';
        liList+='</li>';


        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">Diner</div>';
        liList+='<div class="col-md-2">'+(getJson.dinerQty==''?'0':getJson.dinerQty)+'</div>';
        liList+='<div class="col-md-4">$'+((getJson.diner==''||getJson.diner=='0')?'0.00':getJson.diner)+'</div>';
        liList+='</div>';
        liList+='</li>';


        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">BOH</div>';
        liList+='<div class="col-md-2">'+(getJson.holdldQty==''?'0':getJson.holdldQty)+'</div>';
        liList+='<div class="col-md-4">$'+((getJson.holdld==''||getJson.holdld=='0')?'0.00':getJson.holdld)+'</div>';
        liList+='</div>';
        liList+='</li>';

        var totalDeliveryQty=0;
        var totalDelivery=0.00;

        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">DELIVEROO</div>';
        liList+='<div class="col-md-2">'+(getJson.deliverooQty==''?'0':getJson.deliverooQty)+'</div>';
        liList+='<div class="col-md-4">$'+((getJson.deliveroo==''||getJson.deliveroo=='0')?'0.00':getJson.deliveroo)+'</div>';
        liList+='</div>';
        liList+='</li>';
        totalDeliveryQty += parseInt(getJson.deliverooQty);
        totalDelivery += parseFloat(getJson.deliveroo);

        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">UBEREATS</div>';
        liList+='<div class="col-md-2">'+(getJson.ubereatsQty==''?'0':getJson.ubereatsQty)+'</div>';
        liList+='<div class="col-md-4">$'+((getJson.ubereats==''||getJson.ubereats=='0')?'0.00':getJson.ubereats)+'</div>';
        liList+='</div>';
        liList+='</li>';
        totalDeliveryQty += parseInt(getJson.ubereatsQty);
        totalDelivery += parseFloat(getJson.ubereats);

        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">FOODPANDA</div>';
        liList+='<div class="col-md-2">'+(getJson.foodpandaQty==''?'0':getJson.foodpandaQty)+'</div>';
        liList+='<div class="col-md-4">$'+((getJson.foodpanda==''||getJson.foodpanda=='0')?'0.00':getJson.foodpanda)+'</div>';
        liList+='</div>';
        liList+='</li>';
        totalDeliveryQty += parseInt(getJson.foodpandaQty);
        totalDelivery += parseFloat(getJson.foodpanda);

        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">VOUCHER</div>';
        liList+='<div class="col-md-2">'+(getJson.voucherQty==''?'0':getJson.voucherQty)+'</div>';
        liList+='<div class="col-md-4">$'+((getJson.voucher==''||getJson.voucher=='0')?'0.00':getJson.voucher)+'</div>';
        liList+='</div>';
        liList+='</li>';

        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">TOTAL DELIVERY</div>';
        liList+='<div class="col-md-2">'+totalDeliveryQty+'</div>';
        liList+='<div class="col-md-4">$'+parseFloat(totalDelivery).toFixed(2)+'</div>';
        liList+='</div>';
        liList+='</li>';

        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">TOTAL/CARD</div>';
        liList+='<div class="col-md-2">'+(getJson.totalCardQty==''?'0':getJson.totalCardQty)+'</div>';
        liList+='<div class="col-md-4">$'+((getJson.totalCard==''||getJson.totalCard=='0')?'0.00':getJson.totalCard)+'</div>';
        liList+='</div>';
        liList+='</li>';

        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">TOTAL/NETS</div>';
        liList+='<div class="col-md-2">'+(getJson.netsQty==''?'0':getJson.netsQty)+'</div>';
        liList+='<div class="col-md-4">$'+((getJson.nets==''||getJson.nets=='0')?'0.00':getJson.nets)+'</div>';
        liList+='</div>';
        liList+='</li>';

        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">TOTAL/CASH</div>';
        liList+='<div class="col-md-2">'+(getJson.totalCashQty==''?'0':getJson.totalCashQty)+'</div>';
        liList+='<div class="col-md-4">$'+((getJson.totalCash==''||getJson.totalCash=='0')?'0.00':getJson.totalCash)+'</div>';
        liList+='</div>';
        liList+='</li>';

        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">TOTAL/BOH</div>';
        liList+='<div class="col-md-2">'+(getJson.holdldQty==''?'0':getJson.holdldQty)+'</div>';
        liList+='<div class="col-md-4">$'+((getJson.holdld==''||getJson.holdld=='0')?'0.00':getJson.holdld)+'</div>';

        liList+='</div>';
        liList+='</li>';


        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">Nett/Sales</div>';
        liList+='<div class="col-md-2"></div>';
        liList+='<div class="col-md-4">$'+((getJson.holdld==''||getJson.holdld=='0')?'0.00':(parseFloat(getJson.totalCash)+parseFloat(getJson.holdld)+parseFloat(getJson.nets)+parseFloat(getJson.totalCard)).toFixed(2))+'</div>';
        liList+='</div>';
        liList+='</li>';

        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-12">-------VOID/REFUND SUMMARY--------</div>';
        liList+='</div>';
        liList+='</li>';
        liList+='</div>';
        liList+='</li>';

        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">VOID/Items</div>';
        liList+='<div class="col-md-2">'+(getJson.itemVoidQty==''?'0':getJson.itemVoidQty)+'</div>';
        liList+='<div class="col-md-4">$'+((getJson.itemVoid==''||getJson.itemVoid=='0')?'0.00':getJson.itemVoid)+'</div>';
        liList+='</div>';
        liList+='</li>';

        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">VOID Bills$</div>';
        liList+='<div class="col-md-2">'+(getJson.billVoidQty==''?'0':getJson.billVoidQty)+'</div>';
        liList+='<div class="col-md-4">$'+((getJson.billVoid==''||getJson.billVoid=='0')?'0.00':getJson.billVoid)+'</div>';
        liList+='</div>';
        liList+='</li>';
        var titalvoid = Number(getJson.itemVoid) + Number(getJson.billVoid);
        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">TOTAL VOID$</div>';
        liList+='<div class="col-md-2"></div>';
        liList+='<div class="col-md-4">$'+((titalvoid==''||titalvoid=='0')?'0.00':titalvoid)+'</div>';
        liList+='</div>';
        liList+='</li>';

        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">REFUND Bills$</div>';
        liList+='<div class="col-md-2">'+(getJson.billRefundQty==''?'0':getJson.billRefundQty)+'</div>';
        liList+='<div class="col-md-4">$'+((getJson.billRefund==''||getJson.billRefund=='0')?'0.00':getJson.billRefund)+'</div>';
        liList+='</div>';
        liList+='</li>';


        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">REFUND Taxes$</div>';
        liList+='<div class="col-md-2"></div>';
        liList+='<div class="col-md-4">$'+((getJson.refundTax==''||getJson.refundTax=='0')?'0.00':getJson.refundTax)+'</div>';
        liList+='</div>';
        liList+='</li>';

        var titalrefund = Number(getJson.billRefund) + Number(getJson.refundTax);

        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">TOTAL REFUND$</div>';
        liList+='<div class="col-md-2"></div>';
        liList+='<div class="col-md-4">$'+((titalrefund==''||titalrefund=='0')?'0.00':titalrefund)+'</div>';
        liList+='</div>';
        liList+='</li>';

        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-12">---------DISCOUNTS-------------</div>';
        liList+='</div>';
        liList+='</li>';


        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">Discount on%</div>';
        liList+='<div class="col-md-2">'+(getJson.discountPerQty==''?'0':getJson.discountPerQty)+'</div>';
        liList+='<div class="col-md-4">$'+((getJson.discountPer==''||getJson.discountPer=='0')?'0.00':getJson.discountPer)+'</div>';
        liList+='</div>';
        liList+='</li>';

        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">Discount on$</div>';
        liList+='<div class="col-md-2">'+(getJson.discountQty==''?'0':getJson.discountQty)+'</div>';
        liList+='<div class="col-md-4">$'+((getJson.discount==''||getJson.discount=='0')?'0.00':getJson.discount)+'</div>';
        liList+='</div>';
        liList+='</li>';

        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">Total Discount</div>';
        liList+='<div class="col-md-2"></div>';
        liList+='<div class="col-md-4">$'+((getJson.discountPer==''||getJson.discountPer=='0'||getJson.discount==''||getJson.discount=='0')?'0.00':parseFloat(parseFloat(getJson.discountPer)+parseFloat(getJson.discount)).toFixed(2))+'</div>';

        liList+='</div>';
        liList+='</li>';





        //
        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-12">-----Tax-------</div>';
        liList+='</div>';
        liList+='</li>';

        for(var j=0;j<getReportDayTaxs.length;j++){
            liList+='<li class="summaryitemworp">';
            liList+='<div class="row">';
            liList+='<div class="col-md-6">'+getReportDayTaxs[j].taxName+'</div>';
            liList+='<div class="col-md-2"></div>';
            liList+='<div class="col-md-4">$'+((getReportDayTaxs[j].taxAmount==''||getReportDayTaxs[j].taxAmount=='0')?'0.00':getReportDayTaxs[j].taxAmount)+'</div>';
            liList+='</div>';
            liList+='</li>';
        }
        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">Inclusive Tax</div>';
        liList+='<div class="col-md-2"></div>';
        liList+='<div class="col-md-4">$'+((getJson.inclusiveTaxAmt==''||getJson.inclusiveTaxAmt=='0')?'0.00':getJson.inclusiveTaxAmt)+'</div>';
        liList+='</div>';
        liList+='</li>';
        
        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">Total Tax</div>';
        liList+='<div class="col-md-2"></div>';
        var taxTtl = totalTax + inclusiveTaxAmt;
        liList+='<div class="col-md-4">$'+((taxTtl==0||taxTtl=='')?'0.00':taxTtl)+'</div>';
        liList+='</div>';
        liList+='</li>';                
        //
        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-12">----Drawer-------</div>';
        liList+='</div>';
        liList+='</li>';

        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">Start Drawer</div>';
        liList+='<div class="col-md-2"></div>';
        liList+='<div class="col-md-4">$'+((getJson.startDrawerAmount==''||getJson.startDrawerAmount=='0')?'0.00':getJson.startDrawerAmount)+'</div>';
        liList+='</div>';
        liList+='</li>';

        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">TOTAL CASH</div>';
        liList+='<div class="col-md-2"></div>';
        liList+='<div class="col-md-4">$'+((getJson.totalCash==''||getJson.totalCash=='0')?'0.00':getJson.totalCash)+'</div>';
        liList+='</div>';
        liList+='</li>';
        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">Stored-Card Cash Charge</div>';
        liList+='<div class="col-md-2"></div>';
        liList+='<div class="col-md-4">$'+((getJson.cashTopUp==''||getJson.cashTopUp=='0')?'0.00':getJson.cashTopUp)+'</div>';
        liList+='</div>';
        liList+='</li>';

        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">Cash In</div>';
        liList+='<div class="col-md-2"></div>';
        liList+='<div class="col-md-4">$'+((getJson.cashInAmt==''||getJson.cashInAmt=='0')?'0.00':getJson.cashInAmt)+'</div>';
        liList+='</div>';
        liList+='</li>';

        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">Cash Out</div>';
        liList+='<div class="col-md-2"></div>';
        liList+='<div class="col-md-4">$'+((getJson.cashOutAmt==''||getJson.cashOutAmt=='0')?'0.00':getJson.cashOutAmt)+'</div>';
        liList+='</div>';
        liList+='</li>';
        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">Expected in Drawer</div>';
        liList+='<div class="col-md-2"></div>';
        liList+='<div class="col-md-4">$'+((getJson.expectedAmount==''||getJson.expectedAmount=='0')?'0.00':getJson.expectedAmount)+'</div>';
        liList+='</div>';
        liList+='</li>';
        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">Actual in Drawer</div>';
        liList+='<div class="col-md-2"></div>';
        liList+='<div class="col-md-4">$'+((getJson.waiterAmount==''||getJson.waiterAmount=='0')?'0.00':getJson.waiterAmount)+'</div>';
        liList+='</div>';
        liList+='</li>';
        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">Difference</div>';
        liList+='<div class="col-md-2"></div>';
        liList+='<div class="col-md-4">$'+((getJson.difference==''||getJson.difference=='0')?'0.00':getJson.difference)+'</div>';
        liList+='</div>';
        liList+='</li>';
        bilvalue=""+getJson.totalBills==''?'0':""+getJson.totalBills;
        totalOpenItem=getJson.openCount==''?'0':""+getJson.openCount;

        $("#salesAnalysisli").html(liList);
        $("#totalBillValue").html(bilvalue);
        $("#totalOpenItemValue").html(totalOpenItem);
        if(salesAnScroll==""){
         salesAnScroll= new IScroll('#salesAnalysisWrapper', {scrollY: true, mouseWheel: true });
         }
        salesAnScroll.refresh();
    }else{
        liList+='<li>';
        liList+='<div class="row">';
        liList+='<div class="col-md-6"></div>';
        liList+='<div class="col-md-2"></div>';
        liList+='<div class="col-md-4"></div>';
        liList+='</div>';
        liList+='</li>';
        $("#salesAnalysisli").html(liList);
        $("#totalBillValue").html(bilvalue);
        $("#totalOpenItemValue").html(totalOpenItem);

    }

}

/**
 * getHourlyJson
 */
    //数据封装
function showHourly(getReportHourly){
    var hourlyLi="";
    var totalprice=0.00;
    var totalqty=0;
    var hourlyTal='';
    if(getReportHourly!=undefined && getReportHourly.length>0){
        for (var j=0;j<getReportHourly.length;j++) {
            totalprice+=parseFloat(getReportHourly[j].amountPrice);
            totalqty+=parseInt(getReportHourly[j].amountQty);
            hourlyLi+='<li class="reportitemworp">';
            hourlyLi+='<div class="row">';
            hourlyLi+='<div class="col-md-5 wordWarp">'+getReportHourly[j].hour+'</div>';
            hourlyLi+='<div class="col-md-3 wordWarp">'+getReportHourly[j].amountQty+'</div>';
            hourlyLi+='<div class="col-md-4 wordWarp">'+((getReportHourly[j].amountPrice==""||getReportHourly[j].amountPrice=="0")?'$0.00':("$"+parseFloat(getReportHourly[j].amountPrice).toFixed(2)))+'</div>';
            hourlyLi+='</div>';
            hourlyLi+='</li>';
        }

        $("#hourlySalesli").html(hourlyLi);
        if(hourScroll==""){
            hourScroll=  new IScroll('#hourlySalesWrapper', {scrollY: true, mouseWheel: true });
        }
        hourScroll.refresh();
    }else{
        hourlyLi+='<li>';
        hourlyLi+='<div class="row">';
        hourlyLi+='<div class="col-md-5"></div>';
        hourlyLi+='<div class="col-md-3"></div>';
        hourlyLi+='<div class="col-md-4"></div>';
        hourlyLi+='</div>';
        hourlyLi+='</li>';
        $("#hourlySalesli").html(hourlyLi);


    }


    hourlyTal+='<div class="col-md-5">Total</div>' ;
    hourlyTal+='<div class="col-md-3">'+totalqty+'</div>' ;
    hourlyTal+='<div class="col-md-4">$'+parseFloat(totalprice).toFixed(2)+'</div>';
    $("#hourlyTotal").html(hourlyTal);
}

//exposed for android
function _JsConnectAndroid(funcname, param) {
    if (funcname == "GetOderReport") {
        _refreshOrderReoprt(param);
    }
}
function _loadOrderReport(bizDate, zFlag, xSession){
    var param = {"js_callback":"GetOderReport","bizDate":bizDate, "z":zFlag, "x":xSession};
    window.JavaConnectJS.send("LoadXZReport",JSON.stringify(param));
}
