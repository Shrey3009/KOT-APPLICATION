(function ($) {
    "use strict";
    var t;
    var WaiterSaleData = null;
    var CurrentSaleData = {};
    var AllSaleData = null;
    var ItemSales = null;
    var CategorySales = null;

    function size(animate) {
        if (animate == undefined) {
            animate = false;
        }
        clearTimeout(t);
        t = setTimeout(function () {
            $("canvas").each(function (i, el) {
                $(el).attr({
                    "width": $(el).parent().width(),
                    "height": $(el).parent().outerHeight()
                });
            });
            redraw(animate);
            var m = 0;
            $(".chartJS").height("");
            $(".chartJS").each(function (i, el) {
                m = Math.max(m, $(el).height());
            });
            $(".chartJS").height(m);
        }, 30);
    }

    //$(window).on('resize', function () {

    //});

    function fmtAllSaleData() {
        $.each(AllSaleData, function(idx, obj) {
            var locdate = new Date(obj.businessDateStr);
            AllSaleData[idx]['bizdate'] = locdate.toLocaleDateString();
        });
    }

    function setTotalData(){
        var idx = 0;
        
        if (AllSaleData!=undefined && AllSaleData.length>0)
           idx = AllSaleData.length-1;
           
        $(".subtotal").html("￥ "+AllSaleData[idx].subTotal);
        $(".totaltax").html("￥ "+AllSaleData[idx].totalTax);
        $(".totaldiscount").html("￥ "+AllSaleData[idx].totalDiscount);
        $(".totalamount").html("￥ "+AllSaleData[idx].totalAmount);

    }

    function DateAdd(strInterval, NumDay, dtDate) {
        var dtTmp = new Date(dtDate);
        if (isNaN(dtTmp)) dtTmp = new Date();
        switch (strInterval) {
            case "s":return new Date(Date.parse(dtTmp) + (1000 * NumDay));
            case "n":return new Date(Date.parse(dtTmp) + (60000 * NumDay));
            case "h":return new Date(Date.parse(dtTmp) + (3600000 * NumDay));
            case "d":return new Date(Date.parse(dtTmp) + (86400000 * NumDay));
            case "w":return new Date(Date.parse(dtTmp) + ((86400000 * 7) * NumDay));
            case "m":return new Date(dtTmp.getFullYear(), (dtTmp.getMonth()) + NumDay, dtTmp.getDate(), dtTmp.getHours(), dtTmp.getMinutes(), dtTmp.getSeconds());
            case "y":return new Date((dtTmp.getFullYear() + NumDay), dtTmp.getMonth(), dtTmp.getDate(), dtTmp.getHours(), dtTmp.getMinutes(), dtTmp.getSeconds());
        }
    }
    Date.prototype.Format = function (fmt) { //author: meizz
        var o = {
            "M+": this.getMonth() + 1, //月份
            "d+": this.getDate(), //日
            "h+": this.getHours(), //小时
            "m+": this.getMinutes(), //分
            "s+": this.getSeconds(), //秒
            "q+": Math.floor((this.getMonth() + 3) / 3), //季度
            "S": this.getMilliseconds() //毫秒
        };
        if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    }
    function drawDailySalesChart() {
        var allSaleDataList=[];
        var tempAllSaleDataList=[];
        //数据筛选
       if(AllSaleData!=null&&AllSaleData.length>0){
           if(AllSaleData.length>=3){
               //获取最大日期

               var maxDayTime=0;
               for(var i=0;i<AllSaleData.length;i++){

                 if(maxDayTime<AllSaleData[i].businessDateStr){
                     maxDayTime= AllSaleData[i].businessDateStr;
                 }
               }
               for(var i=0;i<AllSaleData.length;i++){
                   var times=maxDayTime - AllSaleData[i].businessDateStr;
                   var days = parseInt(times/ (1000 * 60 * 60 * 24));//计算出天数
                   if(0<=days&&days<=2){
                      // alert(new Date(AllSaleData[i].businessDateStr).Format("yyyy-MM-dd"));
                       tempAllSaleDataList.push(AllSaleData[i]);
                   }
               }
           }else{
               tempAllSaleDataList=AllSaleData;
           }
       }
        //计算出日期
        if(tempAllSaleDataList==null||tempAllSaleDataList.length==0){
            //
            var allSaleDataObject={};
            var allSaleDataObject1={};
            var allSaleDataObject2={};
            allSaleDataObject.businessDateStr=new Date().Format("yyyy-MM-dd");
            allSaleDataObject.subTotal=0.00;
            allSaleDataObject.totalTax=0.00;
            allSaleDataObject.totalDiscount=0.00;
            allSaleDataObject.totalAmount=0.00;
            allSaleDataObject1.businessDateStr=DateAdd('d', -1,new Date()).Format("yyyy-MM-dd");
            allSaleDataObject1.subTotal=0.00;
            allSaleDataObject1.totalTax=0.00;
            allSaleDataObject1.totalDiscount=0.00;
            allSaleDataObject1.totalAmount=0.00;
            allSaleDataObject2.businessDateStr=DateAdd('d', -2,new Date()).Format("yyyy-MM-dd");
            allSaleDataObject2.subTotal=0.00;
            allSaleDataObject2.totalTax=0.00;
            allSaleDataObject2.totalDiscount=0.00;
            allSaleDataObject2.totalAmount=0.00;
            allSaleDataList.push(allSaleDataObject);
            allSaleDataList.push(allSaleDataObject1);
            allSaleDataList.push(allSaleDataObject2);
        }else if(tempAllSaleDataList.length==1){
            var allSaleDataObject={};
            var allSaleDataObject1={};
            var allSaleDataObject2={};
            allSaleDataObject.businessDateStr=new Date(tempAllSaleDataList[0].businessDateStr).Format("yyyy-MM-dd");
            allSaleDataObject.subTotal=tempAllSaleDataList[0].subTotal;
            allSaleDataObject.totalTax=tempAllSaleDataList[0].totalTax;
            allSaleDataObject.totalDiscount=tempAllSaleDataList[0].totalDiscount;
            allSaleDataObject.totalAmount=tempAllSaleDataList[0].totalAmount;
            allSaleDataObject1.businessDateStr=DateAdd('d', -1,new Date(tempAllSaleDataList[0].businessDateStr)).Format("yyyy-MM-dd");
            allSaleDataObject1.subTotal=0.00;
            allSaleDataObject1.totalTax=0.00;
            allSaleDataObject1.totalDiscount=0.00;
            allSaleDataObject1.totalAmount=0.00;
            allSaleDataObject2.businessDateStr=DateAdd('d', -2,new Date(tempAllSaleDataList[0].businessDateStr)).Format("yyyy-MM-dd");
            allSaleDataObject2.subTotal=0.00;
            allSaleDataObject2.totalTax=0.00;
            allSaleDataObject2.totalDiscount=0.00;
            allSaleDataObject2.totalAmount=0.00;
            allSaleDataList.push(allSaleDataObject);
            allSaleDataList.push(allSaleDataObject1);
            allSaleDataList.push(allSaleDataObject2);

        }else if(tempAllSaleDataList.length==2){
            if( tempAllSaleDataList[1].businessDateStr>tempAllSaleDataList[0].businessDateStr){
                var times= tempAllSaleDataList[1].businessDateStr - tempAllSaleDataList[0].businessDateStr;
                var days = parseInt(times/ (1000 * 60 * 60 * 24));//计算出天数
                if(days==0){
                    //前后一天数据 加载最小日期的前一天的数据
                    var allSaleDataObjecttemp={};
                    allSaleDataObjecttemp.businessDateStr=DateAdd('d', -1,new Date(tempAllSaleDataList[0].businessDateStr)).Format("yyyy-MM-dd");
                    allSaleDataObjecttemp.subTotal=0.00;
                    allSaleDataObjecttemp.totalTax=0.00;
                    allSaleDataObjecttemp.totalDiscount=0.00;
                    allSaleDataObjecttemp.totalAmount=0.00;
                    allSaleDataList.push(allSaleDataObjecttemp);
                    for(var i=0;i<tempAllSaleDataList.length;i++){
                        var allSaleDataObject={};
                        allSaleDataObject.totalAmount=tempAllSaleDataList[i].totalAmount;
                        allSaleDataObject.subTotal=tempAllSaleDataList[i].subTotal;
                        allSaleDataObject.totalTax=tempAllSaleDataList[i].totalTax;
                        allSaleDataObject.totalDiscount=tempAllSaleDataList[i].totalDiscount;
                        allSaleDataObject.businessDateStr=new Date(tempAllSaleDataList[i].businessDateStr).Format("yyyy-MM-dd");
                        //alert(new Date(AllSaleData[i].businessDateStr).Format("yyyy-MM-dd"));
                        allSaleDataList.push(allSaleDataObject);
                    }
                }else if(days==1){
                   //前后日期相差一天 加上中间遗漏的一天的数据
                    var allSaleDataObjecttemp={};
                    allSaleDataObjecttemp.businessDateStr=DateAdd('d', -1,new Date(tempAllSaleDataList[1].businessDateStr)).Format("yyyy-MM-dd");
                    allSaleDataObjecttemp.subTotal=0.00;
                    allSaleDataObjecttemp.totalTax=0.00;
                    allSaleDataObjecttemp.totalDiscount=0.00;
                    allSaleDataObjecttemp.totalAmount=0.00;
                    allSaleDataList.push(allSaleDataObjecttemp);

                    for(var i=0;i<tempAllSaleDataList.length;i++){
                        var allSaleDataObject={};
                        allSaleDataObject.totalAmount=tempAllSaleDataList[i].totalAmount
                        allSaleDataObject.subTotal=tempAllSaleDataList[i].subTotal
                        allSaleDataObject.totalTax=tempAllSaleDataList[i].totalTax
                        allSaleDataObject.totalDiscount=tempAllSaleDataList[i].totalDiscount;
                        allSaleDataObject.businessDateStr=new Date(tempAllSaleDataList[i].businessDateStr).Format("yyyy-MM-dd");
                        //alert(new Date(AllSaleData[i].businessDateStr).Format("yyyy-MM-dd"));
                        allSaleDataList.push(allSaleDataObject);
                    }
                }else {
                   //前后相差两天的数据 清除最小的那比记录 添加最大日期前两天的数据
                    var allSaleDataObjecttemp={};
                    allSaleDataObjecttemp.businessDateStr=DateAdd('d', -1,new Date(tempAllSaleDataList[1].businessDateStr)).Format("yyyy-MM-dd");
                    allSaleDataObjecttemp.subTotal=0.00;
                    allSaleDataObjecttemp.totalTax=0.00;
                    allSaleDataObjecttemp.totalDiscount=0.00;
                    allSaleDataObjecttemp.totalAmount=0.00;
                    allSaleDataList.push(allSaleDataObjecttemp);

                    var allSaleDataObjecttemp1={};
                    allSaleDataObjecttemp1.businessDateStr=DateAdd('d', -2,new Date(tempAllSaleDataList[1].businessDateStr)).Format("yyyy-MM-dd");
                    allSaleDataObjecttemp1.subTotal=0.00;
                    allSaleDataObjecttemp1.totalTax=0.00;
                    allSaleDataObjecttemp1.totalDiscount=0.00;
                    allSaleDataObjecttemp1.totalAmount=0.00;
                    allSaleDataList.push(allSaleDataObjecttemp1);

                    var allSaleDataObject={};
                    allSaleDataObject.totalAmount=tempAllSaleDataList[1].totalAmount
                    allSaleDataObject.subTotal=tempAllSaleDataList[1].subTotal
                    allSaleDataObject.totalTax=tempAllSaleDataList[1].totalTax
                    allSaleDataObject.totalDiscount=tempAllSaleDataList[1].totalDiscount;
                    allSaleDataObject.businessDateStr=new Date(tempAllSaleDataList[1].businessDateStr).Format("yyyy-MM-dd");
                    //alert(new Date(AllSaleData[i].businessDateStr).Format("yyyy-MM-dd"));
                    allSaleDataList.push(allSaleDataObject);

                }
            }else{
                var times= tempAllSaleDataList[0].businessDateStr - tempAllSaleDataList[1].businessDateStr;
                var days = parseInt(times/ (1000 * 60 * 60 * 24));//计算出天数

                if(days==0){
                    //前后一天数据 加载最小日期的前一天的数据
                    var allSaleDataObjecttemp={};
                    allSaleDataObjecttemp.businessDateStr=DateAdd('d', -1,new Date(tempAllSaleDataList[1].businessDateStr)).Format("yyyy-MM-dd");
                    allSaleDataObjecttemp.subTotal=0.00;
                    allSaleDataObjecttemp.totalTax=0.00;
                    allSaleDataObjecttemp.totalDiscount=0.00;
                    allSaleDataObjecttemp.totalAmount=0.00;
                    allSaleDataList.push(allSaleDataObjecttemp);
                    for(var i=0;i<tempAllSaleDataList.length;i++){
                        var allSaleDataObject={};
                        allSaleDataObject.totalAmount=tempAllSaleDataList[i].totalAmount;
                        allSaleDataObject.subTotal=tempAllSaleDataList[i].subTotal;
                        allSaleDataObject.totalTax=tempAllSaleDataList[i].totalTax;
                        allSaleDataObject.totalDiscount=tempAllSaleDataList[i].totalDiscount;
                        allSaleDataObject.businessDateStr=new Date(tempAllSaleDataList[i].businessDateStr).Format("yyyy-MM-dd");
                        //alert(new Date(AllSaleData[i].businessDateStr).Format("yyyy-MM-dd"));
                        allSaleDataList.push(allSaleDataObject);
                    }
                }else if(days==1){
                    //前后日期相差一天 加上中间遗漏的一天的数据
                    var allSaleDataObjecttemp={};
                    allSaleDataObjecttemp.businessDateStr=DateAdd('d', -1,new Date(tempAllSaleDataList[0].businessDateStr)).Format("yyyy-MM-dd");
                    allSaleDataObjecttemp.subTotal=0.00;
                    allSaleDataObjecttemp.totalTax=0.00;
                    allSaleDataObjecttemp.totalDiscount=0.00;
                    allSaleDataObjecttemp.totalAmount=0.00;
                    allSaleDataList.push(allSaleDataObjecttemp);

                    for(var i=0;i<tempAllSaleDataList.length;i++){
                        var allSaleDataObject={};
                        allSaleDataObject.totalAmount=tempAllSaleDataList[i].totalAmount
                        allSaleDataObject.subTotal=tempAllSaleDataList[i].subTotal
                        allSaleDataObject.totalTax=tempAllSaleDataList[i].totalTax
                        allSaleDataObject.totalDiscount=tempAllSaleDataList[i].totalDiscount;
                        allSaleDataObject.businessDateStr=new Date(tempAllSaleDataList[i].businessDateStr).Format("yyyy-MM-dd");
                        //alert(new Date(AllSaleData[i].businessDateStr).Format("yyyy-MM-dd"));
                        allSaleDataList.push(allSaleDataObject);
                    }
                }else {
                    //前后相差两天的数据 清除最小的那比记录 添加最大日期前两天的数据
                    var allSaleDataObjecttemp={};
                    allSaleDataObjecttemp.businessDateStr=DateAdd('d', -1,new Date(tempAllSaleDataList[0].businessDateStr)).Format("yyyy-MM-dd");
                    allSaleDataObjecttemp.subTotal=0.00;
                    allSaleDataObjecttemp.totalTax=0.00;
                    allSaleDataObjecttemp.totalDiscount=0.00;
                    allSaleDataObjecttemp.totalAmount=0.00;
                    allSaleDataList.push(allSaleDataObjecttemp);

                    var allSaleDataObjecttemp1={};
                    allSaleDataObjecttemp1.businessDateStr=DateAdd('d', -2,new Date(tempAllSaleDataList[0].businessDateStr)).Format("yyyy-MM-dd");
                    allSaleDataObjecttemp1.subTotal=0.00;
                    allSaleDataObjecttemp1.totalTax=0.00;
                    allSaleDataObjecttemp1.totalDiscount=0.00;
                    allSaleDataObjecttemp1.totalAmount=0.00;
                    allSaleDataList.push(allSaleDataObjecttemp1);

                    var allSaleDataObject={};
                    allSaleDataObject.totalAmount=tempAllSaleDataList[0].totalAmount
                    allSaleDataObject.subTotal=tempAllSaleDataList[0].subTotal
                    allSaleDataObject.totalTax=tempAllSaleDataList[0].totalTax
                    allSaleDataObject.totalDiscount=tempAllSaleDataList[0].totalDiscount;
                    allSaleDataObject.businessDateStr=new Date(tempAllSaleDataList[0].businessDateStr).Format("yyyy-MM-dd");
                    //alert(new Date(AllSaleData[i].businessDateStr).Format("yyyy-MM-dd"));
                    allSaleDataList.push(allSaleDataObject);

                }
               }
        }else{
            for(var i=0;i<tempAllSaleDataList.length;i++){
                var allSaleDataObject={};
                allSaleDataObject.totalAmount=tempAllSaleDataList[i].totalAmount;
                allSaleDataObject.subTotal=tempAllSaleDataList[i].subTotal;
                allSaleDataObject.totalTax=tempAllSaleDataList[i].totalTax;
                allSaleDataObject.totalDiscount=tempAllSaleDataList[i].totalDiscount;
                allSaleDataObject.businessDateStr=new Date(tempAllSaleDataList[i].businessDateStr).Format("yyyy-MM-dd");
                //alert(new Date(AllSaleData[i].businessDateStr).Format("yyyy-MM-dd"));
                allSaleDataList.push(allSaleDataObject);
            }
        }
        Morris.Line({
            element: 'graph-area-line',
            behaveLikeLine: true,
            data: allSaleDataList,
            xkey: 'businessDateStr',
            ykeys: ['totalAmount','totalTax','totalDiscount','subTotal' ],
            labels: ['净销售','税','折扣','总销售'],
            xLabels :'day',
            pointSize:4,
            lineWidth:2,
            lineColors: ['#ED5D5D', '#D6D23A', '#32D2C9']
        });
        $('.progress-stat-bar li').each(function () {
            $(this).find('.progress-stat-percent').animate({
                height: $(this).attr('data-percent')
            }, 1000);
        });
    }

    function generateCtyPieData() {
        var pieData = [
            {
                value: 0,
                color: "#d9edf7"
            },
            {
                value: 0,
                color: "#f2dede"
            },
            {
                value: 0,
                color: "#dff0d8"
            },
            {
                value: 0,
                color: "#fcf8e3"
            },
            {
                value: 0,
                color: "rgb(239, 179, 230)"
            }
        ];
        $.each(CategorySales, function(idx, obj) {
            pieData[idx].value = obj.qty;
            pieData[idx].index = idx;
        });
        return pieData;
    }
    function drawCategoryPieChart() {

        var myPie = new Chart(document.getElementById("ctychart").getContext("2d")).Pie(generateCtyPieData());
        var tmpl = _.template($('#ctysales-board-template').html());
        var html = tmpl({'ctylist': CategorySales});
        $('#ctysales').html(html);
    }

    function generateItemPieData() {
        var pieData = [
            {
                value: 0,
                color: "#d9edf7"
            },
            {
                value: 0,
                color: "#f2dede"
            },
            {
                value: 0,
                color: "#dff0d8"
            },
            {
                value: 0,
                color: "#fcf8e3"
            },
            {
                value: 0,
                color: "rgb(239, 179, 230)"
            }
        ];
        $.each(ItemSales, function(idx, obj) {
            pieData[idx].value = obj.qty;
            pieData[idx].index = idx;
        });
        return pieData;
    }

    function drawItemSalePieChart() {
        var myPie = new Chart(document.getElementById("itemchart").getContext("2d")).Pie(generateItemPieData());
        var tmpl = _.template($('#itemsales-board-template').html());
        var html = tmpl({'itemlist': ItemSales});
        $('#itemsale').html(html);
    }

    function redraw(animation) {
        var options = {};
        if (!animation) {
            options.animation = false;
        } else {
            options.animation = true;
        }

        drawCategoryPieChart();
        drawItemSalePieChart();

    }

    function _backtomain(evt) {
        evt.stopImmediatePropagation();
        window.JavaConnectJS.send("SystemBack",null);
    }

    function _refreshDashboard(param) {
        var data = $.parseJSON(decodeURIComponent(param).replace(new RegExp("\\+","gm")," "));

        WaiterSaleData = data['waiterAndSales'];
        CurrentSaleData = {'cash': data['cash'], 'cards':data['cards'],'others':data['others']};
        ItemSales = data['itemList'];
        CategorySales = data['categoryItemList'];
        AllSaleData = data['totalDetailInfos'];

        fmtAllSaleData();
        drawDailySalesChart();
        size(false);
        setTotalData();
        //setTotalPercent
        var tempCheckHtml="";
        var tempOrderHtml="";
        tempCheckHtml+='<li data-percent="'+ ((parseFloat(data['cards'])/parseFloat(data['totalChecks']))*100).toFixed(1)+'%"><span class="progress-stat-percent pink" style="height:'+ ((parseFloat(data['cards'])/parseFloat(data['totalChecks']))*100).toFixed(2)+'%" ></span></li>';
        tempCheckHtml+='<li data-percent="'+((parseFloat(data['cash'])/parseFloat(data['totalChecks']))*100).toFixed(1)+'%"><span class="progress-stat-percent" style="height:'+ ((parseFloat(data['cash'])/parseFloat(data['totalChecks']))*100).toFixed(2)+'%"></span></li>';
        tempCheckHtml+='<li data-percent="'+ ((parseFloat(data['others'])/parseFloat(data['totalChecks']))*100).toFixed(1)+'%"><span class="progress-stat-percent yellow-b" style="height:'+ ((parseFloat(data['others'])/parseFloat(data['totalChecks']))*100).toFixed(2)+'%"></span></li>';
        $("#prsCheck").html(tempCheckHtml);
        $(".salesPrsCheck").html(data['totalChecks']);
        tempOrderHtml+='<li data-percent="'+((parseInt(data['breakfast'])/parseInt(data['totalOrders']))*100)+'%"><span class="progress-stat-percent pink"style="height:'+((parseFloat(data['breakfast'])/parseInt(data['totalOrders']))*100)+'%"></span></li>';
        tempOrderHtml+='<li data-percent="'+((parseFloat(data['lunch'])/parseInt(data['totalOrders']))*100)+'%"><span class="progress-stat-percent" style="height:'+((parseFloat(data['lunch'])/parseInt(data['totalOrders']))*100)+'%"></span></li></span></li>';
        tempOrderHtml+='<li data-percent="'+((parseFloat(data['dinner'])/parseInt(data['totalOrders']))*100)+'%"><span class="progress-stat-percent yellow-b" style="height:'+((parseFloat(data['dinner'])/parseInt(data['totalOrders']))*100)+'%"</span></li>';
        $("#prsOrder").html(tempOrderHtml);
        $(".salesPrsOrder").html(data['totalOrders']);

    }

    //exposed for android
    function _JsConnectAndroid(funcname, param) {
        if (funcname == "UpdateDashboard") {
            _refreshDashboard(param);
        }
    }

    function _loaddashboard() {
        var param = {"js_callback":"UpdateDashboard"};
        window.JavaConnectJS.send("LoadDashboard",JSON.stringify(param));
    }

    $(document).ready(function () {
        FastClick.attach(document.body);

        $("#backBtn").bind('click', _backtomain);
        window.JsConnectAndroid = _JsConnectAndroid;
        _loaddashboard();
    });
})(jQuery);



