SESSIONS = {'0': "全天",
			'1': "早餐",
			'2': "午餐",
			'3': "晚餐",
			'4': "夜宵"};
			
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
       $("#reporttitle").html('日销售报表');
    } else {
       report_z = false;
       $("#reporttitle").html(SESSIONS[report_session]+' 报表');
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
                summaryLi+= '<div class="col-md-4 wordWarp">￥'+((obj[i].amount==''||obj[i].amount=='0')?'0.00':parseFloat(obj[i].amount).toFixed(2))+'</div>';
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
                detailLi+= '<div class="col-md-4 wordWarp">￥'+((objs[is].amount==''||objs[is].amount=='0')?'0.00':parseFloat(objs[is].amount).toFixed(2)) +'</div>';
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


    detailsTal+='<div class="col-md-5">总计</div>' ;
    detailsTal+='<div class="col-md-3">'+detailTotalQty+'</div>' ;
    detailsTal+='<div class="col-md-4">￥'+parseFloat(datailTotalAmount).toFixed(2)+'</div>';
    $("#detailsTotal").html(detailsTal);

    summaryTal+='<div class="col-md-5">总计</div>' ;
    summaryTal+='<div class="col-md-3">'+summaryTotalQty+'</div>' ;
    summaryTal+='<div class="col-md-4">￥'+parseFloat(summaryTotalAmount).toFixed(2)+'</div>';
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
        liList+='<div class="col-md-6">销售毛额</div>';
        liList+='<div class="col-md-2">'+(getJson.itemSalesQty==''?'0':getJson.itemSalesQty)+'</div>';
        liList+='<div class="col-md-4">￥'+((getJson.itemSales==''||getJson.itemSales=='0')?'0.00':getJson.itemSales)+'</div>';
        liList+='</div>';
        liList+='</li>';
        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">储值卡销售</div>';
        liList+='<div class="col-md-2">'+(getJson.topUpsQty==''?'0':getJson.topUpsQty)+'</div>';
        liList+='<div class="col-md-4">￥'+((getJson.topUps==''||getJson.topUps=='0')?'0.00':getJson.topUps)+'</div>';
        liList+='</div>';
        liList+='</li>';
        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">免费菜</div>';
        liList+='<div class="col-md-2">'+(getJson.focItemQty==''?'0':getJson.focItemQty)+'</div>';
        liList+='<div class="col-md-4">￥'+((getJson.focItem==''||getJson.focItem=='0')?'0.00':getJson.focItem)+'</div>';
        liList+='</div>';
        liList+='</li>';
        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">免费单</div>';
        liList+='<div class="col-md-2">'+(getJson.focBillQty==''?'0':getJson.focBillQty)+'</div>';
        liList+='<div class="col-md-4">￥'+((getJson.focBill==''||getJson.focBill=='0')?'0.00':getJson.focBill)+'</div>';
        liList+='</div>';
        liList+='</li>';
        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">退菜</div>';
        liList+='<div class="col-md-2">'+(getJson.itemVoidQty==''?'0':getJson.itemVoidQty)+'</div>';
        liList+='<div class="col-md-4">￥'+((getJson.itemVoid==''||getJson.itemVoid=='0')?'0.00':getJson.itemVoid)+'</div>';
        liList+='</div>';
        liList+='</li>';
        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">退单</div>';
        liList+='<div class="col-md-2">'+(getJson.billVoidQty==''?'0':getJson.billVoidQty)+'</div>';
        liList+='<div class="col-md-4">￥'+((getJson.billVoid==''||getJson.billVoid=='0')?'0.00':getJson.billVoid)+'</div>';
        liList+='</div>';
        liList+='</li>';
        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">销售净额</div>';
        liList+='<div class="col-md-2"></div>';
        liList+='<div class="col-md-4">￥'+((getJson.totalSales==''||getJson.totalSales=='0')?'0.00':getJson.totalSales)+'</div>';
        liList+='</div>';
        liList+='</li>';

        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-12">---------支付 汇总-------------</div>';
        liList+='</div>';
        liList+='</li>';

        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">现金</div>';
        liList+='<div class="col-md-2">'+(getJson.totalCashQty==''?'0':getJson.totalCashQty)+'</div>';
        liList+='<div class="col-md-4">￥'+((getJson.totalCash==''||getJson.totalCash=='0')?'0.00':getJson.totalCash)+'</div>';
        liList+='</div>';
        liList+='</li>';

        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">支付宝</div>';
        liList+='<div class="col-md-2">'+(getJson.alipayQty==''?'0':getJson.alipayQty)+'</div>';
        liList+='<div class="col-md-4">￥'+((getJson.alipay==''||getJson.alipay=='0')?'0.00':getJson.alipay)+'</div>';
        liList+='</div>';
        liList+='</li>';

        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">微信支付</div>';
        liList+='<div class="col-md-2">'+(getJson.weixinpayQty==''?'0':getJson.weixinpayQty)+'</div>';
        liList+='<div class="col-md-4">￥'+((getJson.weixinpay==''||getJson.weixinpay=='0')?'0.00':getJson.weixinpay)+'</div>';
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
        liList+='<div class="col-md-6">卡合计</div>';
        liList+='<div class="col-md-2">'+(getJson.totalCardQty==''?'0':getJson.totalCardQty)+'</div>';
        liList+='<div class="col-md-4">￥'+((getJson.totalCard==''||getJson.totalCard=='0')?'0.00':getJson.totalCard)+'</div>';
        liList+='</div>';
        liList+='</li>';
        	                
        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">免费</div>';
        liList+='<div class="col-md-2">'+(getJson.focBillQty==''?'0':getJson.focBillQty)+'</div>';
        liList+='<div class="col-md-4">￥'+((getJson.focBill==''||getJson.focBill=='0')?'0.00':getJson.focBill)+'</div>';
        liList+='</div>';
        liList+='</li>';





        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-12">-------退单/菜 汇总--------</div>';
        liList+='</div>';
        liList+='</li>';
        liList+='</div>';
        liList+='</li>';

        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">退菜</div>';
        liList+='<div class="col-md-2">'+(getJson.itemVoidQty==''?'0':getJson.itemVoidQty)+'</div>';
        liList+='<div class="col-md-4">￥'+((getJson.itemVoid==''||getJson.itemVoid=='0')?'0.00':getJson.itemVoid)+'</div>';
        liList+='</div>';
        liList+='</li>';

        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">退单</div>';
        liList+='<div class="col-md-2">'+(getJson.billVoidQty==''?'0':getJson.billVoidQty)+'</div>';
        liList+='<div class="col-md-4">￥'+((getJson.billVoid==''||getJson.billVoid=='0')?'0.00':getJson.billVoid)+'</div>';
        liList+='</div>';
        liList+='</li>';

        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">合计</div>';
        liList+='<div class="col-md-2">'+((getJson.itemVoidQty==''||getJson.billVoid=="")?'0':(getJson.itemVoidQty+getJson.billVoidQty))+'</div>';
        liList+='<div class="col-md-4">￥'+((getJson.itemVoid==''&&getJson.billVoid=='')?'0.00':parseFloat((parseFloat(getJson.itemVoid)+parseFloat(getJson.billVoid))).toFixed(2))+'</div>';
        liList+='</div>';
        liList+='</li>';

        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-12">---------折扣 汇总-------------</div>';
        liList+='</div>';
        liList+='</li>';


        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">百分比折扣</div>';
        liList+='<div class="col-md-2">'+(getJson.discountPerQty==''?'0':getJson.discountPerQty)+'</div>';
        liList+='<div class="col-md-4">￥'+((getJson.discountPer==''||getJson.discountPer=='0')?'0.00':getJson.discountPer)+'</div>';
        liList+='</div>';
        liList+='</li>';

        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">现金折扣</div>';
        liList+='<div class="col-md-2">'+(getJson.discountQty==''?'0':getJson.discountQty)+'</div>';
        liList+='<div class="col-md-4">￥'+((getJson.discount==''||getJson.discount=='0')?'0.00':getJson.discount)+'</div>';
        liList+='</div>';
        liList+='</li>';

        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">总折扣</div>';
        liList+='<div class="col-md-2"></div>';
        liList+='<div class="col-md-4">￥'+((getJson.discountPer==''||getJson.discountPer=='0'||getJson.discount==''||getJson.discount=='0')?'0.00':parseFloat(parseFloat(getJson.discountPer)+parseFloat(getJson.discount)).toFixed(2))+'</div>';

        liList+='</div>';
        liList+='</li>';

        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-12">----存入/支出-------</div>';
        liList+='</div>';
        liList+='</li>';
        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">存入</div>';
        liList+='<div class="col-md-2"></div>';
        liList+='<div class="col-md-4">￥'+((getJson.cashInAmt==''||getJson.cashInAmt=='0')?'0.00':getJson.cashInAmt)+'</div>';
        liList+='</div>';
        liList+='</li>';
        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">支出</div>';
        liList+='<div class="col-md-2"></div>';
        liList+='<div class="col-md-4">￥'+((getJson.cashOutAmt==''||getJson.cashOutAmt=='0')?'0.00':getJson.cashOutAmt)+'</div>';
        liList+='</div>';
        liList+='</li>';
        liList+='<li class="summaryitemworp">';
        liList+='<div class="row">';
        liList+='<div class="col-md-6">差值</div>';
        liList+='<div class="col-md-2"></div>';
        liList+='<div class="col-md-4">￥'+((getJson.varianceAmt==''||getJson.varianceAmt=='0')?'0.00':getJson.varianceAmt)+'</div>';

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
            hourlyLi+='<div class="col-md-4 wordWarp">'+((getReportHourly[j].amountPrice==""||getReportHourly[j].amountPrice=="0")?'￥0.00':("￥"+parseFloat(getReportHourly[j].amountPrice).toFixed(2)))+'</div>';
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


    hourlyTal+='<div class="col-md-5">合计</div>' ;
    hourlyTal+='<div class="col-md-3">'+totalqty+'</div>' ;
    hourlyTal+='<div class="col-md-4">￥'+parseFloat(totalprice).toFixed(2)+'</div>';
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
