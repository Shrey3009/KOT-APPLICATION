package com.alfred.printer;

import android.text.TextUtils;
import android.util.Base64;

import com.alfred.print.jobs.PrintJob;
import com.alfred.print.jobs.Priority;
import com.alfred.remote.printservice.PrintService;
import com.alfred.remote.printservice.R;
import com.alfredbase.javabean.OrderPromotion;
import com.alfredbase.utils.BH;
import com.alfredbase.utils.TimeUtil;
import com.birbit.android.jobqueue.Params;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BillPrint extends PrintJob {

    public static int FIXED_COL4_SPACE = 2;
    public static int FIXED_COL4_PRICE = 10; //in case of 48 dots width, QTY col = 10dots
    public static int FIXED_COL4_QTY = 6; //in case of 48 dots width, QTY col = 10dots
    public static int FIXED_COL4_TOTAL = 12; //in case of 48 dots width, QTY col = 10dots //incase for devtest34 is 15

    public static int COL4_ITEMNAME; // Width = CharSize/scale - FIXED_COL2_QTY/scale -
    // FIXED_COL2_PRICE/scale- FIXED_COL2_TOTAL/scale- FIXED_COL2_SPACE *3

    public BillPrint(String uuid, Long bizDate) {
        super(new Params(Priority.HIGH).requireNetwork().persist().groupBy("bill"), "bill", uuid, bizDate);
        if (this.charSize == 33) {
            BillPrint.FIXED_COL4_TOTAL = 8;
            BillPrint.FIXED_COL4_PRICE = 7;
            BillPrint.FIXED_COL4_QTY = 4;
            BillPrint.FIXED_COL4_SPACE = 1;
        }
    }


    public void AddRestaurantInfo(String logo, String name, String address, String customized) {

        if (logo != null && logo.length() > 0) {
            PrintData logoimg = new PrintData();
            logoimg.setDataFormat(PrintData.FORMAT_IMG);
            logoimg.addImage(Base64.decode(logo, Base64.DEFAULT));
            logoimg.setTextAlign(PrintData.ALIGN_CENTRE);
            this.data.add(logoimg);
        }

        StringBuilder rname = new StringBuilder();
        rname.append(name.trim());

        //name
        PrintData rInfo = new PrintData();
        rInfo.setDataFormat(PrintData.FORMAT_TXT);
        rInfo.setFontsize(2);
        rInfo.setTextAlign(PrintData.ALIGN_CENTRE);
        rInfo.setText(rname.toString() + reNext);
        this.data.add(rInfo);

        //address
        StringBuilder addbuf = new StringBuilder();
        addbuf.append(address);

        PrintData radd = new PrintData();
        radd.setDataFormat(PrintData.FORMAT_TXT);
        radd.setFontsize(1);
        radd.setMarginTop(10);
        radd.setTextAlign(PrintData.ALIGN_CENTRE);
        radd.setText(addbuf.toString() + reNext);
        this.data.add(radd);

        ////customized fields
        if (customized != null) {
            StringBuilder ctbuf = new StringBuilder();
            String[] options = customized.split(";", -1);
            for (int i = 0; i < options.length; i++) {
                ctbuf.append(options[i]).append(reNext);
            }

            PrintData ctadd = new PrintData();
            ctadd.setDataFormat(PrintData.FORMAT_TXT);
            ctadd.setFontsize(1);
            ctadd.setTextAlign(PrintData.ALIGN_CENTRE);
            ctadd.setText(ctbuf.toString());

            this.data.add(ctadd);
        }


        addHortionalLine(this.charSize);
    }

    public void addHor() {
        addHortionalLine(this.charSize);
    }


    public void AddHeader(int isTakeAway, String table, int pax, String billNo,
                          String posNo, String cashier, String dateTime, String orderNo, String info,int appOrderId,String trainString,String waiterName) {


        if (!TextUtils.isEmpty(info)) {
            PrintData appOrderPrint = new PrintData();
            String appStr = StringUtil.padRight(PrintService.instance.getResources().getString(R.string.online_app_no), this.FIXED_COL4_TOTAL - 1);
            String appOrderStr = appStr + appOrderId + reNext;
            appOrderPrint.setDataFormat(PrintData.FORMAT_TXT);
            appOrderPrint.setTextAlign(PrintData.ALIGN_LEFT);
            appOrderPrint.setFontsize(2);
            appOrderPrint.setText(appOrderStr);
            this.data.add(appOrderPrint);

            PrintData addressPrint = new PrintData();
            //   String deliveryStr = StringUtil.padRight(PrintService.instance.getResources().getString(R.string.app_delivery), this.FIXED_COL4_TOTAL - 1);
            String infoStr = info + reNext;
//            String orderNoStr = StringUtil.padRight(PrintService.instance.getResources().getString(R.string.order_no_), this.FIXED_COL4_TOTAL - 1);
//            String padorderNo = orderNoStr + orderNo + reNext;
            addressPrint.setDataFormat(PrintData.FORMAT_TXT);
            addressPrint.setTextAlign(PrintData.ALIGN_LEFT);
            addressPrint.setText(infoStr);
            this.data.add(addressPrint);
            addHortionalLine(this.charSize);
        } else {
            if (appOrderId > 0) {

                PrintData appOrderPrint = new PrintData();
                String appStr = StringUtil.padRight(PrintService.instance.getResources().getString(R.string.online_app_no), this.FIXED_COL4_TOTAL - 1);
                String appOrderStr = appStr + appOrderId + reNext;
                appOrderPrint.setDataFormat(PrintData.FORMAT_TXT);
                appOrderPrint.setTextAlign(PrintData.ALIGN_LEFT);
                appOrderPrint.setFontsize(2);
                appOrderPrint.setText(appOrderStr);
                this.data.add(appOrderPrint);
                addHortionalLine(this.charSize);
            }

        }


        //流水号 NO
        PrintData orderNoPrint = new PrintData();
        String orderNoStr = StringUtil.padRight(PrintService.instance.getResources().getString(R.string.order_no), this.FIXED_COL4_TOTAL - 1);
        String padorderNo = orderNoStr + orderNo + trainString + reNext;
        orderNoPrint.setDataFormat(PrintData.FORMAT_TXT);
        orderNoPrint.setTextAlign(PrintData.ALIGN_LEFT);
//		orderNoPrint.setFontsize(2);
        orderNoPrint.setText(padorderNo);
        this.data.add(orderNoPrint);


        //waiterName
        String cashierStr="";
        if (!TextUtils.isEmpty(waiterName)) {
            PrintData cashierPrint = new PrintData();
            String cashierLabel = StringUtil.padRight("waiter", this.FIXED_COL4_TOTAL - 1);
            cashierStr = cashierLabel + ":" + waiterName + "\t\t";
        }else {
            //cashier
            PrintData cashierPrint = new PrintData();
            String cashierLabel = StringUtil.padRight(PrintService.instance.getResources().getString(R.string.cashier), this.FIXED_COL4_TOTAL - 1);
            cashierStr = cashierLabel + ":" + cashier + "\t\t";
        }
//		cashierPrint.setDataFormat(PrintData.FORMAT_TXT);
//		cashierPrint.setTextAlign(PrintData.ALIGN_LEFT);
//		cashierPrint.setText(cashierStr);
//		this.data.add(cashierPrint);

        //POS
        PrintData posPrint = new PrintData();
        String posLabel = StringUtil.padLeft(PrintService.instance.getResources().getString(R.string.pos), this.FIXED_COL4_TOTAL - 1);
        String posStr = posLabel + ":" + posNo + reNext;
        posPrint.setDataFormat(PrintData.FORMAT_TXT);
        posPrint.setTextAlign(PrintData.ALIGN_LEFT);
        posPrint.setText(cashierStr + posStr);
        this.data.add(posPrint);

        //Date
        PrintData datePrint = new PrintData();
        String dateLabel = StringUtil.padRight(PrintService.instance.getResources().getString(R.string.date), this.FIXED_COL4_TOTAL - 1);
        String dateStr = dateLabel + ":" + dateTime + " ";

        //Bill NO
        PrintData billNoPrint = new PrintData();
        String billNoStr = StringUtil.padRight(PrintService.instance.getResources().getString(R.string.bill_no_), this.FIXED_COL4_TOTAL - 1);
        String padBillNo = billNoStr + ":" + billNo + reNext;
        billNoPrint.setDataFormat(PrintData.FORMAT_TXT);
        billNoPrint.setTextAlign(PrintData.ALIGN_LEFT);
        billNoPrint.setText(dateStr + padBillNo);
        this.data.add(billNoPrint);


        //Table & PAX

        PrintData tabPrint = new PrintData();
        String tabLabel = StringUtil.padRight(PrintService.instance.getResources().getString(R.string.table), this.FIXED_COL4_TOTAL / 2);
        String tabStr = tabLabel + ":" + table;
        tabPrint.setDataFormat(PrintData.FORMAT_TXT);
        tabPrint.setTextAlign(PrintData.ALIGN_LEFT);
        tabPrint.setFontsize(2);
        tabPrint.setText(tabStr);
        this.data.add(tabPrint);

        PrintData paxPrint = new PrintData();

        try {
            int padlen = this.charSize - tabStr.getBytes("GBK").length * 2;
            String paxLabel = StringUtil.padRight(PrintService.instance
                            .getResources().getString(R.string.pax),
                    this.FIXED_COL4_TOTAL / 2);
            String paxStr = paxLabel + ":" + pax;
            paxStr = StringUtil.padLeft(paxStr, padlen) + reNext;
            paxPrint.setDataFormat(PrintData.FORMAT_TXT);
            paxPrint.setTextAlign(PrintData.ALIGN_LEFT);
            paxPrint.setTextBold(1);
            paxPrint.setText(paxStr);
            this.data.add(paxPrint);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
//        if (isTakeAway == 1) {
//            PrintData takeAwayPrint = new PrintData();
//            String str = PrintService.instance.getResources().getString(R.string.takeaway_print) + reNext;
//            takeAwayPrint.setDataFormat(PrintData.FORMAT_TXT);
//            takeAwayPrint.setTextAlign(PrintData.ALIGN_LEFT);
//            takeAwayPrint.setFontsize(2);
//            takeAwayPrint.setText(str);
//            this.data.add(takeAwayPrint);
//        }

        if (isTakeAway == 2) {
            PrintData takeAwayPrint = new PrintData();
            String str = PrintService.instance.getResources().getString(R.string.takeaway) + reNext;
            takeAwayPrint.setDataFormat(PrintData.FORMAT_TXT);
            takeAwayPrint.setTextAlign(PrintData.ALIGN_LEFT);
            takeAwayPrint.setFontsize(2);
            takeAwayPrint.setText(str);
            this.data.add(takeAwayPrint);
            //PrintData appOrderPrint = new PrintData();
//            String appStr = StringUtil.padRight(PrintService.instance.getResources().getString(R.string.order_app_no_), this.FIXED_COL4_TOTAL - 1);
//            String appOrderStr = appStr+appOrderId+ reNext;
//            appOrderPrint.setDataFormat(PrintData.FORMAT_TXT);
//            appOrderPrint.setTextAlign(PrintData.ALIGN_LEFT);
//            appOrderPrint.setFontsize(2);
//            appOrderPrint.setText(appOrderStr);
//            this.data.add(appOrderPrint);
        } else if (isTakeAway == 3) {
            PrintData deliveryPrint = new PrintData();
            String str = PrintService.instance.getResources().getString(R.string.delivery) + reNext;
            deliveryPrint.setDataFormat(PrintData.FORMAT_TXT);
            deliveryPrint.setTextAlign(PrintData.ALIGN_LEFT);
            deliveryPrint.setFontsize(2);
            deliveryPrint.setText(str);
            this.data.add(deliveryPrint);
        } else if (isTakeAway == 1) {
//            PrintData appOrderPrint = new PrintData();
//            String appStr = StringUtil.padRight(PrintService.instance.getResources().getString(R.string.order_app_no_), this.FIXED_COL4_TOTAL - 1);
//            String appOrderStr = appStr+appOrderId+ reNext;
//            appOrderPrint.setDataFormat(PrintData.FORMAT_TXT);
//            appOrderPrint.setTextAlign(PrintData.ALIGN_LEFT);
//            appOrderPrint.setFontsize(2);
//            appOrderPrint.setText(appOrderStr);
//            this.data.add(appOrderPrint);
        }
        //addHortionalLine();
        addHortionaDoublelLine(this.charSize);
    }


    public void AddHeaderCash(int id, String time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.US);// HH:mm:ss

        Date date = new Date(System.currentTimeMillis());
        String dates = simpleDateFormat.format(date).toString().trim();
        String dateLabel = StringUtil.padRight
                (PrintService.instance.getResources().getString(R.string.date), this.FIXED_COL4_TOTAL - 1);
        String dateStr = dateLabel + ":" + dates + " ";
        //Bill NO
        PrintData billEmpPrint = new PrintData();
        String emp = StringUtil.padRight(PrintService.instance.getResources
                ().getString(R.string.emp), this.FIXED_COL4_TOTAL - 1);
        String padBillNo = " " + emp + ":" + id + reNext;
        billEmpPrint.setDataFormat(PrintData.FORMAT_TXT);
        billEmpPrint.setTextAlign(PrintData.ALIGN_LEFT);
        billEmpPrint.setText(dateStr + padBillNo);

        this.data.add(billEmpPrint);
        addHortionaDoublelLine(this.charSize);

    }

    public void AddCashIn(String cash, String com, int type) {


        PrintData cashPrint = new PrintData();
        String cashType;
        if (type == 0) {
            cashType = PrintService.instance.getResources().getString(R.string.cash_in) + " : ";
        } else {
            cashType = PrintService.instance.getResources().getString(R.string.cash_out) + " : ";
        }
        //	String orderNoStr = StringUtil.padRight("Cash In :", this.FIXED_COL4_TOTAL-1);
        String padorderNo = cashType + cash + reNext;
        cashPrint.setDataFormat(PrintData.FORMAT_TXT);
        cashPrint.setTextAlign(PrintData.ALIGN_CENTRE);
        cashPrint.setFontsize(2);
        cashPrint.setText(padorderNo);
        this.data.add(cashPrint);

        if (!TextUtils.isEmpty(com)) {
            PrintData cashComPrint = new PrintData();

            String comment = "" + com + reNext;
            cashComPrint.setDataFormat(PrintData.FORMAT_TXT);
            cashComPrint.setTextAlign(PrintData.ALIGN_CENTRE);
            cashComPrint.setFontsize(2);
            cashComPrint.setText(comment);
            this.data.add(cashComPrint);
        }
        addHortionaDoublelLine(this.charSize);

    }


    /*Kiosk uses only*/
    public void AddKioskHeader(int isTakeAway, String table, int pax, String billNo,
                               String posNo, String cashier, String dateTime, String orderNo, String groupNum, int trainType) {
        if (!TextUtils.isEmpty(table)) {
            PrintData tableNamePrint = new PrintData();
            tableNamePrint.setDataFormat(PrintData.FORMAT_TXT);
            tableNamePrint.setTextAlign(PrintData.ALIGN_CENTRE);
            tableNamePrint.setFontsize(1);
            tableNamePrint.setText(PrintService.instance.getResources().getString(R.string.your_table_number) + " " + reNext);
            this.data.add(tableNamePrint);
            PrintData tableNumPrint = new PrintData();
            tableNumPrint.setDataFormat(PrintData.FORMAT_TXT);
            tableNumPrint.setTextAlign(PrintData.ALIGN_CENTRE);
            tableNumPrint.setFontsize(3);
            tableNumPrint.setText(table + reNext + reNext);
            this.data.add(tableNumPrint);
            addHortionalLine(this.charSize);
        }
        String trainString = "";
        if (trainType == 1) {
            trainString = PrintService.instance.getResources().getString(R.string.training);
        }


        //流水号 NO
        PrintData orderNoPrint = new PrintData();
        String orderNoStr = StringUtil.padRight(PrintService.instance.getResources().getString(R.string.order_no), this.FIXED_COL4_TOTAL - 1);
        String padorderNo = orderNoStr + orderNo+trainString + reNext;
        orderNoPrint.setDataFormat(PrintData.FORMAT_TXT);
        orderNoPrint.setTextAlign(PrintData.ALIGN_LEFT);
        orderNoPrint.setFontsize(2);
        orderNoPrint.setText(padorderNo);
        this.data.add(orderNoPrint);
        if (isTakeAway == 1) {
            PrintData takeAwayPrint = new PrintData();
            String str = PrintService.instance.getResources().getString(R.string.takeaway) + reNext;
            takeAwayPrint.setDataFormat(PrintData.FORMAT_TXT);
            takeAwayPrint.setTextAlign(PrintData.ALIGN_LEFT);
            takeAwayPrint.setFontsize(2);
            takeAwayPrint.setText(str);
            this.data.add(takeAwayPrint);
        }
        //group num
        if (!TextUtils.isEmpty(groupNum)) {
            PrintData groupNumPrint = new PrintData();
            String groupNumStr = StringUtil.padRight(PrintService.instance.getResources().getString(R.string.groupnum), this.FIXED_COL4_TOTAL);
            String padGroupNum = groupNumStr + ":" + groupNum + reNext;
            groupNumPrint.setDataFormat(PrintData.FORMAT_TXT);
            groupNumPrint.setTextAlign(PrintData.ALIGN_LEFT);
            groupNumPrint.setText(padGroupNum);
            this.data.add(groupNumPrint);
        }

        //Bill NO
        PrintData billNoPrint = new PrintData();
        String billNoStr = StringUtil.padRight(PrintService.instance.getResources().getString(R.string.bill_no_), this.FIXED_COL4_TOTAL);
        String padBillNo = billNoStr + ":" + billNo + reNext;
        billNoPrint.setDataFormat(PrintData.FORMAT_TXT);
        billNoPrint.setTextAlign(PrintData.ALIGN_LEFT);
        billNoPrint.setText(padBillNo);
        this.data.add(billNoPrint);

        //trainType
//        if(trainType==1){
//            PrintData trainPrint = new PrintData();
//            String trainLabel = StringUtil.padRight(PrintService.instance.getResources().getString(R.string.mode), this.FIXED_COL4_TOTAL);
//            String trainStr = trainLabel + ":" + PrintService.instance.getResources().getString(R.string.train) + reNext;
//            trainPrint.setDataFormat(PrintData.FORMAT_TXT);
//            trainPrint.setTextAlign(PrintData.ALIGN_LEFT);
//            trainPrint.setText(trainStr);
//            this.data.add(trainPrint);
//        }
        //cashier
        PrintData cashierPrint = new PrintData();
        String cashierLabel = StringUtil.padRight(PrintService.instance.getResources().getString(R.string.cashier), this.FIXED_COL4_TOTAL);
        String cashierStr = cashierLabel + ":" + cashier + reNext;
        cashierPrint.setDataFormat(PrintData.FORMAT_TXT);
        cashierPrint.setTextAlign(PrintData.ALIGN_LEFT);
        cashierPrint.setText(cashierStr);
        this.data.add(cashierPrint);

        //POS
        PrintData posPrint = new PrintData();
        String posLabel = StringUtil.padRight(PrintService.instance.getResources().getString(R.string.pos), this.FIXED_COL4_TOTAL);
        String posStr = posLabel + ":" + posNo + reNext;
        posPrint.setDataFormat(PrintData.FORMAT_TXT);
        posPrint.setTextAlign(PrintData.ALIGN_LEFT);
        posPrint.setText(posStr);
        this.data.add(posPrint);

        //Date
        PrintData datePrint = new PrintData();
        String dateLabel = StringUtil.padRight(PrintService.instance.getResources().getString(R.string.date), this.FIXED_COL4_TOTAL);
        String dateStr = dateLabel + ":" + dateTime + reNext;
        datePrint.setDataFormat(PrintData.FORMAT_TXT);
        datePrint.setTextAlign(PrintData.ALIGN_LEFT);
        datePrint.setText(dateStr);
        this.data.add(datePrint);

        //addHortionalLine();
        addHortionaDoublelLine(this.charSize);
    }
    /* Four columns layout (Width = 48dots)
     * |item Name    38/scale   | QTY 10/scale  |
     *
     **/


    public void AddKioskHeaderAddress(int isTakeAway, String table, int pax, String billNo,
                                      String posNo, String cashier, String dateTime, String orderNo, String groupNum, String info, int appOrderId) {


        if (!TextUtils.isEmpty(table)) {
            PrintData tableNamePrint = new PrintData();
            tableNamePrint.setDataFormat(PrintData.FORMAT_TXT);
            tableNamePrint.setTextAlign(PrintData.ALIGN_CENTRE);
            tableNamePrint.setFontsize(1);
            tableNamePrint.setText(PrintService.instance.getResources().getString(R.string.your_table_number) + " " + reNext);
            this.data.add(tableNamePrint);
            PrintData tableNumPrint = new PrintData();
            tableNumPrint.setDataFormat(PrintData.FORMAT_TXT);
            tableNumPrint.setTextAlign(PrintData.ALIGN_CENTRE);
            tableNumPrint.setFontsize(3);
            tableNumPrint.setText(table + reNext + reNext);
            this.data.add(tableNumPrint);
            addHortionalLine(this.charSize);
        }
        if (!TextUtils.isEmpty(info)) {
            PrintData appOrderPrint = new PrintData();
            String appStr = StringUtil.padRight(PrintService.instance.getResources().getString(R.string.online_app_no), this.FIXED_COL4_TOTAL - 1);
            String appOrderStr = appStr + appOrderId + reNext;
            appOrderPrint.setDataFormat(PrintData.FORMAT_TXT);
            appOrderPrint.setTextAlign(PrintData.ALIGN_LEFT);
            appOrderPrint.setFontsize(2);
            appOrderPrint.setText(appOrderStr);
            this.data.add(appOrderPrint);

            PrintData addressPrint = new PrintData();
            //   String deliveryStr = StringUtil.padRight(PrintService.instance.getResources().getString(R.string.app_delivery), this.FIXED_COL4_TOTAL - 1);
            String infoStr = info + reNext;
//            String orderNoStr = StringUtil.padRight(PrintService.instance.getResources().getString(R.string.order_no_), this.FIXED_COL4_TOTAL - 1);
//            String padorderNo = orderNoStr + orderNo + reNext;
            addressPrint.setDataFormat(PrintData.FORMAT_TXT);
            addressPrint.setTextAlign(PrintData.ALIGN_LEFT);
            addressPrint.setText(infoStr);
            this.data.add(addressPrint);
            addHortionalLine(this.charSize);
        } else {
            PrintData appOrderPrint = new PrintData();
            String appStr = StringUtil.padRight(PrintService.instance.getResources().getString(R.string.online_app_no), this.FIXED_COL4_TOTAL - 1);
            String appOrderStr = appStr + appOrderId + reNext;
            appOrderPrint.setDataFormat(PrintData.FORMAT_TXT);
            appOrderPrint.setTextAlign(PrintData.ALIGN_LEFT);
            appOrderPrint.setFontsize(2);
            appOrderPrint.setText(appOrderStr);
            this.data.add(appOrderPrint);
            addHortionalLine(this.charSize);
        }

        //流水号 NO
        PrintData orderNoPrint = new PrintData();
        String orderNoStr = StringUtil.padRight(PrintService.instance.getResources().getString(R.string.order_no), this.FIXED_COL4_TOTAL - 1);
        String padorderNo = orderNoStr + orderNo + reNext;
        orderNoPrint.setDataFormat(PrintData.FORMAT_TXT);
        orderNoPrint.setTextAlign(PrintData.ALIGN_LEFT);
        orderNoPrint.setFontsize(2);
        orderNoPrint.setText(padorderNo);
        this.data.add(orderNoPrint);


        if (isTakeAway == 2) {
            PrintData takeAwayPrint = new PrintData();
            String str = PrintService.instance.getResources().getString(R.string.takeaway) + reNext;
            takeAwayPrint.setDataFormat(PrintData.FORMAT_TXT);
            takeAwayPrint.setTextAlign(PrintData.ALIGN_LEFT);
            takeAwayPrint.setFontsize(2);
            takeAwayPrint.setText(str);
            this.data.add(takeAwayPrint);
            //PrintData appOrderPrint = new PrintData();
//            String appStr = StringUtil.padRight(PrintService.instance.getResources().getString(R.string.order_app_no_), this.FIXED_COL4_TOTAL - 1);
//            String appOrderStr = appStr+appOrderId+ reNext;
//            appOrderPrint.setDataFormat(PrintData.FORMAT_TXT);
//            appOrderPrint.setTextAlign(PrintData.ALIGN_LEFT);
//            appOrderPrint.setFontsize(2);
//            appOrderPrint.setText(appOrderStr);
//            this.data.add(appOrderPrint);
        } else if (isTakeAway == 3) {
            PrintData deliveryPrint = new PrintData();
            String str = PrintService.instance.getResources().getString(R.string.delivery) + reNext;
            deliveryPrint.setDataFormat(PrintData.FORMAT_TXT);
            deliveryPrint.setTextAlign(PrintData.ALIGN_LEFT);
            deliveryPrint.setFontsize(2);
            deliveryPrint.setText(str);
            this.data.add(deliveryPrint);
        } else if (isTakeAway == 1) {
//            PrintData appOrderPrint = new PrintData();
//            String appStr = StringUtil.padRight(PrintService.instance.getResources().getString(R.string.order_app_no_), this.FIXED_COL4_TOTAL - 1);
//            String appOrderStr = appStr+appOrderId+ reNext;
//            appOrderPrint.setDataFormat(PrintData.FORMAT_TXT);
//            appOrderPrint.setTextAlign(PrintData.ALIGN_LEFT);
//            appOrderPrint.setFontsize(2);
//            appOrderPrint.setText(appOrderStr);
//            this.data.add(appOrderPrint);
        }
        //group num
        if (!TextUtils.isEmpty(groupNum)) {
            PrintData groupNumPrint = new PrintData();
            String groupNumStr = StringUtil.padRight(PrintService.instance.getResources().getString(R.string.groupnum), this.FIXED_COL4_TOTAL);
            String padGroupNum = groupNumStr + ":" + groupNum + reNext;
            groupNumPrint.setDataFormat(PrintData.FORMAT_TXT);
            groupNumPrint.setTextAlign(PrintData.ALIGN_LEFT);
            groupNumPrint.setText(padGroupNum);
            this.data.add(groupNumPrint);
        }

        //Bill NO
        PrintData billNoPrint = new PrintData();
        String billNoStr = StringUtil.padRight(PrintService.instance.getResources().getString(R.string.bill_no_), this.FIXED_COL4_TOTAL);
        String padBillNo = billNoStr + ":" + billNo + reNext;
        billNoPrint.setDataFormat(PrintData.FORMAT_TXT);
        billNoPrint.setTextAlign(PrintData.ALIGN_LEFT);
        billNoPrint.setText(padBillNo);
        this.data.add(billNoPrint);

        //cashier
        PrintData cashierPrint = new PrintData();
        String cashierLabel = StringUtil.padRight(PrintService.instance.getResources().getString(R.string.cashier), this.FIXED_COL4_TOTAL);
        String cashierStr = cashierLabel + ":" + cashier + reNext;
        cashierPrint.setDataFormat(PrintData.FORMAT_TXT);
        cashierPrint.setTextAlign(PrintData.ALIGN_LEFT);
        cashierPrint.setText(cashierStr);
        this.data.add(cashierPrint);

        //POS
        PrintData posPrint = new PrintData();
        String posLabel = StringUtil.padRight(PrintService.instance.getResources().getString(R.string.pos), this.FIXED_COL4_TOTAL);
        String posStr = posLabel + ":" + posNo + reNext;
        posPrint.setDataFormat(PrintData.FORMAT_TXT);
        posPrint.setTextAlign(PrintData.ALIGN_LEFT);
        posPrint.setText(posStr);
        this.data.add(posPrint);

        //Date
        PrintData datePrint = new PrintData();
        String dateLabel = StringUtil.padRight(PrintService.instance.getResources().getString(R.string.date), this.FIXED_COL4_TOTAL);
        String dateStr = dateLabel + ":" + dateTime + reNext;
        datePrint.setDataFormat(PrintData.FORMAT_TXT);
        datePrint.setTextAlign(PrintData.ALIGN_LEFT);
        datePrint.setText(dateStr);
        this.data.add(datePrint);

        //addHortionalLine();
        addHortionaDoublelLine(this.charSize);
    }

    /* Four columns layout (Width = 48dots)
     * |item Name    38/scale   | QTY 10/scale  |
     *
     **/
    private String getFourColHeader(String col1Title, String col2Title,
                                    String col3Title, String col4Title) {

        StringBuffer ret = new StringBuffer();
        BillPrint.COL4_ITEMNAME = this.charSize - BillPrint.FIXED_COL4_PRICE - BillPrint.FIXED_COL4_QTY - BillPrint.FIXED_COL4_TOTAL;
        String title1 = StringUtil.padRight(col1Title, BillPrint.COL4_ITEMNAME);
        String title2 = StringUtil.padRight(col2Title, BillPrint.FIXED_COL4_PRICE);
        String title3 = StringUtil.padRight(col3Title, BillPrint.FIXED_COL4_QTY);
        String title4 = StringUtil.padLeft(col4Title, BillPrint.FIXED_COL4_TOTAL);
        ret.append(title1).append(title2).append(title3).append(title4).append(reNext);

        return ret.toString();
    }

    /* Four columns layout (Width = 48dots)
     * |item Name   Dynamical | Price |  |2| QTY 10/scale  | Total|
     *
     **/
    private String getFourColContent(String col1Content, String col2Content,
                                     String col3Content, String col4Content, int charScale) {

        StringBuffer result = new StringBuffer();

        int col1Lines = 1;
        int col2Lines = 1;
        int col3Lines = 1;
        int col4Lines = 1;

        BillPrint.COL4_ITEMNAME = (this.charSize - BillPrint.FIXED_COL4_TOTAL - BillPrint.FIXED_COL4_QTY - BillPrint.FIXED_COL4_PRICE) / charScale - 3 * BillPrint.FIXED_COL4_SPACE;

        int ln1 = 1;
        String[] splitedcontents = {col1Content};

        try {
            //ln1 = (col1Content.getBytes("GBK").length)/(BillPrint.COL4_ITEMNAME*1.0);
            splitedcontents = StringUtil.formatLn(BillPrint.COL4_ITEMNAME * 1, col1Content);
            ln1 = splitedcontents.length;
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        col1Lines = splitedcontents.length;
        //String col1PadContent = StringUtil.padRight(col1Content, col1Lines*BillPrint.COL4_ITEMNAME);
        //ArrayList<String> splittedCol1Content = StringUtil.splitEqually(col1PadContent, BillPrint.COL4_ITEMNAME);

        double ln2 = (col2Content.length()) / (BillPrint.FIXED_COL4_PRICE * 1.0 / charScale);
        col2Lines = StringUtil.nearestTen(ln2);
        String col2PadContent = StringUtil.padLeft(col2Content, col2Lines * BillPrint.FIXED_COL4_PRICE / charScale);
        ArrayList<String> splittedCol2Content = StringUtil.splitEqually(col2PadContent, BillPrint.FIXED_COL4_PRICE / charScale);

        double ln3 = (col3Content.length()) / (BillPrint.FIXED_COL4_QTY * 1.0 / charScale);
        col3Lines = StringUtil.nearestTen(ln3);
        String col3PadContent = StringUtil.padLeft(col3Content, col3Lines * BillPrint.FIXED_COL4_QTY / charScale);
        ArrayList<String> splittedCol3Content = StringUtil.splitEqually(col3PadContent, BillPrint.FIXED_COL4_QTY / charScale);

        double ln4 = (col4Content.length()) / (BillPrint.FIXED_COL4_TOTAL * 1.0 / charScale);
        col4Lines = StringUtil.nearestTen(ln4);
        String col4PadContent = StringUtil.padLeft(col4Content, col4Lines * BillPrint.FIXED_COL4_PRICE / charScale);
        ArrayList<String> splittedCol4Content = StringUtil.splitEqually(col4PadContent, (BillPrint.FIXED_COL4_PRICE )/ charScale);


        for (int i = 0; i < Math.max(Math.max(col1Lines, col2Lines), Math.max(col3Lines, col4Lines)); i++) {
            if (i < col1Lines) {
                //result.append(splittedCol1Content.get(i));
                result.append(StringUtil.padRight(splitedcontents[i], BillPrint.COL4_ITEMNAME));
            } else {
                result.append(StringUtil.padRight(" ", BillPrint.COL4_ITEMNAME));
            }
            //padding
            result.append(StringUtil.padRight(" ", BillPrint.FIXED_COL4_SPACE));

            if (i < col2Lines) {
                result.append(splittedCol2Content.get(i));
            } else {
                result.append(StringUtil.padLeft(" ", (BillPrint.FIXED_COL4_PRICE) / charScale));
            }

            //padding
            result.append(StringUtil.padRight(" ", BillPrint.FIXED_COL4_SPACE));

            if (i < col3Lines) {
                result.append(splittedCol3Content.get(i));
            } else {
                result.append(StringUtil.padLeft(" ", (BillPrint.FIXED_COL4_QTY) / charScale));
            }

            //padding
            result.append(StringUtil.padRight(" ", BillPrint.FIXED_COL4_SPACE));

            if (i < col4Lines) {
                result.append(splittedCol4Content.get(i));
            } else {
                result.append(StringUtil.padLeft(" ", (BillPrint.FIXED_COL4_PRICE) / charScale));
            }
            result.append(reNext);
        }
        return result.toString();
    }

    public void AddOrderNo(String orderNo) {
        PrintData header = new PrintData();
        header.setDataFormat(PrintData.FORMAT_TXT);
        header.setFontsize(2);
        StringBuilder sbr = new StringBuilder(String.format(Locale.US,"%1$" + this.charSize / 2 + "s", ""));
        String ord = PrintService.instance.getResources().getString(R.string.order_no) + orderNo;
        sbr.replace(this.charSize / 2 - ord.length() * 2 - 1, this.charSize / 2 - 1, ord);
        header.setTextAlign(PrintData.ALIGN_RIGHT);
        header.setText(sbr.toString() + reNext);
        this.data.add(header);
    }

    public void AddContentListHeader(String itemName, String price, String qty, String total) {
        PrintData header = new PrintData();
        header.setDataFormat(PrintData.FORMAT_TXT);
        header.setText(this.getFourColHeader(itemName, price, qty, total));
        this.data.add(header);
        addHortionalLine(this.charSize);
    }

    public void AddOrderItem(String itemName, String price, String qty, String total, int scale, String weight, String currencySymbol)
    {
        AddOrderItem(itemName, price, qty, total, scale, weight, currencySymbol, true);
    }

    public void AddOrderItem(String itemName, String price, String qty, String total, int scale, String weight, String currencySymbol, Boolean isInstructions) {
        PrintData order = new PrintData();
        order.setDataFormat(PrintData.FORMAT_TXT);
        order.setFontsize(scale);
        order.setLanguage(PrintData.LANG_CN);
        order.setMarginTop(20);
        if(currencySymbol.equals("Rp"))
        {
            price = NumberFormat.getNumberInstance(Locale.US).format(new Double(price.replace(",", "")).intValue());
            total = NumberFormat.getNumberInstance(Locale.US).format(new Double(total.replace(",", "")).intValue());
        }

        order.setText(this.getFourColContent(itemName, price, qty, total, scale));
        this.data.add(order);
        if(isInstructions)
        {
            this.addWeight(weight);
        }
    }

    public void addOrderModifier(String itemName, int scale, String price, String currencySymbol)
    {
        addOrderModifier(itemName, scale, price, currencySymbol, true);
    }

    public void addOrderModifier(String itemName, int scale, String price, String currencySymbol, Boolean isInstructions) {
        String bigDecimal = BH.formatMoney(price);
        PrintData orderMod = new PrintData();
        orderMod.setDataFormat(PrintData.FORMAT_TXT);
        orderMod.setFontsize(scale);
        orderMod.setLanguage(PrintData.LANG_CN);
        if(currencySymbol.equals("Rp"))
        {
            bigDecimal =  NumberFormat.getNumberInstance(Locale.US).format(BH.getBD(price));
        }
        orderMod.setText(this.getFourColContent("  " + itemName + reNext, bigDecimal, "", "", scale));
        orderMod.setTextAlign(PrintData.ALIGN_LEFT);
        if(isInstructions)
        {
            this.data.add(orderMod);
        }
    }

    public void AddBillSummary(String subtotal, String discount,
                               List<Map<String, String>> taxes, String total, String grandTotal, String rounding, String currencySymbol,List<OrderPromotion> promotionDatas) {
        AddBillSummary(subtotal, discount, taxes, total, grandTotal, rounding, currencySymbol, 0,promotionDatas);
    }

    public void AddBillSummary(String subtotal, String discount,
                               List<Map<String, String>> taxes, String total,String grandTotal, String rounding, String currencySymbol, int splitByPax,List<OrderPromotion> promotionDatas) {
        if ("¥".equals(currencySymbol)) {
            currencySymbol = "￥";
        }
        this.addHortionalLine(this.charSize);
        //subtotal
        PrintData subTotalPrint = new PrintData();
        if(currencySymbol.equals("Rp"))
        {
            subtotal = NumberFormat.getNumberInstance(Locale.US).format(new Double(subtotal.replace(",", "")).intValue());
        }
        String subTotal = StringUtil.padLeft(subtotal, this.FIXED_COL4_TOTAL);
        String padSubtotal = PrintService.instance.getResources().getString(R.string.subtotal) + " : " + currencySymbol + subTotal + reNext;
        subTotalPrint.setDataFormat(PrintData.FORMAT_TXT);
        subTotalPrint.setTextAlign(PrintData.ALIGN_RIGHT);
        subTotalPrint.setText(padSubtotal);
        this.data.add(subTotalPrint);


        //discount
        PrintData discPrint = new PrintData();
        if(currencySymbol.equals("Rp"))
        {
            discount = NumberFormat.getNumberInstance(Locale.US).format(new Double(discount.replace(",", "")).intValue());
        }
        String discountStr = StringUtil.padLeft(discount, this.FIXED_COL4_TOTAL);
        String padDiscount = PrintService.instance.getResources().getString(R.string.discount) + " :" + currencySymbol + discountStr + reNext;
        discPrint.setDataFormat(PrintData.FORMAT_TXT);
        discPrint.setTextAlign(PrintData.ALIGN_RIGHT);
        discPrint.setText(padDiscount);
        this.data.add(discPrint);

        //taxes
        if (taxes != null) {
//			ArrayList<String> taxPriceSUM = taxes.get("taxPriceSum");
//			ArrayList<String> taxNames = taxes.get("taxNames");
//			ArrayList<String> taxPercentages = taxes.get("taxPercentages");
            for (Map<String, String> map : taxes) {
                PrintData taxPrint = new PrintData();
                String taxvalue = StringUtil.padLeft(BH.formatMoney(map.get("taxPriceSum")), this.FIXED_COL4_TOTAL);
                if(currencySymbol.equals("Rp"))
                {
                    taxvalue = StringUtil.padLeft(NumberFormat.getNumberInstance(Locale.US).format(BH.getBD(map.get("taxPriceSum"))), this.FIXED_COL4_TOTAL);
                }

                String padTax = map.get("taxName")
                        + "("
                        + (int) (Double.parseDouble(map.get("taxPercentage")) * 100)
                        + "%) :" + currencySymbol + taxvalue + reNext;

                taxPrint.setDataFormat(PrintData.FORMAT_TXT);
                taxPrint.setTextAlign(PrintData.ALIGN_RIGHT);
                taxPrint.setText(padTax);
                this.data.add(taxPrint);
            }
//			for (int i = 0; i < taxPriceSUM.size(); i++) {
//				PrintData taxPrint = new PrintData();
//	            String taxvalue = StringUtil.padLeft(BH.doubleFormat.format(BH.getBD(taxPriceSUM
//							.get(i))), this.FIXED_COL4_TOTAL);
//
//	            String padTax = taxNames.get(i)
//							+ "("
//							+ (int) (Double.parseDouble(taxPercentages.get(i)) * 100)
//							+ "%) : $" + taxvalue +"\r\n";
//
//	            taxPrint.setDataFormat(PrintData.FORMAT_TXT);
//	            taxPrint.setTextAlign(PrintData.ALIGN_RIGHT);
//	            taxPrint.setText(padTax);
//	        	this.data.add(taxPrint);
//			}
        }

        // promotion

        if(promotionDatas!=null&&promotionDatas.size()>0){

            for (int i = 0; i <promotionDatas.size() ; i++) {
                OrderPromotion promotionData=promotionDatas.get(i);
                PrintData promotionPrint = new PrintData();
                String promotionStr = StringUtil.padLeft(BH.formatMoney(promotionData.getPromotionAmount()).toString(),
                        this.FIXED_COL4_TOTAL);
                String promotiontTotal = promotionData.getPromotionName()+" :" + currencySymbol + promotionStr + reNext;
                promotionPrint.setDataFormat(PrintData.FORMAT_TXT);
                promotionPrint.setTextAlign(PrintData.ALIGN_RIGHT);
                promotionPrint.setText(promotiontTotal);
                this.data.add(promotionPrint);

            }

        }
        // total
        Map<String, String> roundMap = new HashMap<String, String>();
        Gson gson = new Gson();
        roundMap = gson.fromJson(rounding,
                new TypeToken<Map<String, String>>() {
                }.getType());
        PrintData totalPrint = new PrintData();
        String totalStr = StringUtil.padLeft(BH.formatMoney(BH.getBD(roundMap.get("Total")).toString()), this.FIXED_COL4_TOTAL);
        if(currencySymbol.equals("Rp"))
        {
            totalStr = StringUtil.padLeft(NumberFormat.getNumberInstance(Locale.US).format(BH.getBD(roundMap.get("Total"))),
                    this.FIXED_COL4_TOTAL);
            if (splitByPax > 0) {
                totalStr = StringUtil.padLeft(NumberFormat.getNumberInstance(Locale.US).format(BH.getBD(total)), this.FIXED_COL4_TOTAL);
            }
        }
        else
        {
            if (splitByPax > 0)
            {
                DecimalFormat df = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.US));
                df.setRoundingMode(RoundingMode.HALF_UP);
                totalStr = StringUtil.padLeft(df.format(Double.parseDouble(total)), this.FIXED_COL4_TOTAL);
            }
        }
        String totaling = PrintService.instance.getResources().getString(R.string.total_) + currencySymbol + totalStr + reNext;
        totalPrint.setDataFormat(PrintData.FORMAT_TXT);
        totalPrint.setTextAlign(PrintData.ALIGN_RIGHT);
        totalPrint.setMarginTop(10);
        totalPrint.setText(totaling);
        this.data.add(totalPrint);

//        if(!TextUtils.isEmpty(promotion)) {
//            PrintData promotionPrint = new PrintData();
//            String promotionStr = StringUtil.padLeft(BH.getBD(promotion).toString(),
//                    this.FIXED_COL4_TOTAL);
//            String promotiontTotal = PrintService.instance.getResources().getString(R.string.promotion_print) + currencySymbol + promotionStr + reNext;
//            promotionPrint.setDataFormat(PrintData.FORMAT_TXT);
//            promotionPrint.setTextAlign(PrintData.ALIGN_RIGHT);
//            promotionPrint.setText(promotiontTotal);
//            this.data.add(promotionPrint);
//        }
        // rounding
        PrintData roundingPrint = new PrintData();
        String roundingStr = StringUtil.padLeft(BH.formatMoney(BH.getBD(roundMap.get("Rounding")).toString()),
                this.FIXED_COL4_TOTAL);
        if(currencySymbol.equals("Rp"))
        {
            roundingStr = StringUtil.padLeft(NumberFormat.getNumberInstance(Locale.US).format(BH.getBD(roundMap.get("Rounding"))), this.FIXED_COL4_TOTAL);
        }
        String padRounding = PrintService.instance.getResources().getString(R.string.rounding_print) + currencySymbol + roundingStr + reNext;
        roundingPrint.setDataFormat(PrintData.FORMAT_TXT);
        roundingPrint.setTextAlign(PrintData.ALIGN_RIGHT);
        roundingPrint.setText(padRounding);
        this.data.add(roundingPrint);


        //grand total
        PrintData gtPrint = new PrintData();
        if(currencySymbol.equals("Rp"))
        {
            grandTotal =  NumberFormat.getNumberInstance(Locale.US).format(new Double(grandTotal.replace(",", "")));
        }
        else
        {
            DecimalFormat df = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.US));
            df.setRoundingMode(RoundingMode.HALF_UP);
            grandTotal = StringUtil.padLeft(df.format(Double.parseDouble(grandTotal)), this.FIXED_COL4_TOTAL);
        }
        String gtotalStr = StringUtil.padLeft(grandTotal, this.FIXED_COL4_TOTAL);
        String padTotal = PrintService.instance.getResources().getString(R.string.grand_total) + " : " + currencySymbol + gtotalStr + reNext;
        if (splitByPax > 0) {
            padTotal = PrintService.instance.getResources().getString(R.string.split_by_pax) + " " + PrintService.instance.getResources().getString(R.string.grand_total) + "/" + splitByPax + " : " + currencySymbol + gtotalStr + reNext;
        }
        gtPrint.setDataFormat(PrintData.FORMAT_TXT);
        gtPrint.setTextAlign(PrintData.ALIGN_RIGHT);
        gtPrint.setMarginTop(10);
        gtPrint.setFontsize(1);
        gtPrint.setTextBold(1);
        gtPrint.setText(padTotal);
        this.data.add(gtPrint);

        this.addHortionaDoublelLine(this.charSize);
    }

    public void AddSettlementDetails(List<LinkedHashMap<String, String>> settlementList, String currencySymbol) {
        if ("¥".equals(currencySymbol)) {
            currencySymbol = "￥";
        }
        for (LinkedHashMap<String, String> settlement : settlementList) {
            //subtotal
            for (Map.Entry<String, String> entry : settlement.entrySet()) {
                PrintData toPrint = new PrintData();
                String lable;
                String toPrintStr;
                if (PrintService.instance.getResources().getString(R.string.card_no).equals(entry.getKey())) {
                    lable = StringUtil.padLeft(entry.getValue().toString(), this.FIXED_COL4_TOTAL);
                    toPrintStr = entry.getKey() + " : " + lable + reNext;
                } else {
//                    lable = StringUtil.padLeft(BH.getBD(entry.getValue()).toString(), this.FIXED_COL4_TOTAL);
                    if(currencySymbol.equals("Rp"))
                    {
                        lable = StringUtil.padLeft(NumberFormat.getNumberInstance(Locale.US).format(Double.parseDouble(entry.getValue())), this.FIXED_COL4_TOTAL);
                    }
                    else
                    {
                        DecimalFormat df = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.US));
                        df.setRoundingMode(RoundingMode.HALF_UP);
                        lable = StringUtil.padLeft(df.format(Double.parseDouble(entry.getValue())), this.FIXED_COL4_TOTAL);
                    }
                    toPrintStr = entry.getKey() + " : " + currencySymbol + lable + reNext;
                }
                toPrint.setDataFormat(PrintData.FORMAT_TXT);
                toPrint.setTextAlign(PrintData.ALIGN_RIGHT);
                toPrint.setFontsize(2);
                toPrint.setText(toPrintStr);
                this.data.add(toPrint);
            }
        }
    }

    public void AddModifierItem(String modifiers) {
        if (modifiers != null) {

            PrintData mod = new PrintData();
            mod.setDataFormat(PrintData.FORMAT_TXT);
            mod.setFontsize(1);
            mod.setLanguage(PrintData.LANG_CN);
            modifiers = "( " + modifiers + " )";
            mod.setText(modifiers);
            this.data.add(mod);
            addBlankLine();
        }
    }

    public void addWeight(String weight) {
        if (BH.getBDThirdFormat(weight).compareTo(BH.getBD("0")) > 0) {
            PrintData orderMod = new PrintData();
            orderMod.setDataFormat(PrintData.FORMAT_TXT);
            orderMod.setFontsize(1);
            orderMod.setLanguage(PrintData.LANG_CN);
            orderMod.setText(" * " + weight + "(" + PrintService.instance.getResources().getString(R.string.kg) + ")" + reNext);
            orderMod.setTextAlign(PrintData.ALIGN_LEFT);
            this.data.add(orderMod);
        }
    }

    public void getDollarSign(String sign, int lang) {
        PrintData datePrint = new PrintData();
        String dateStr = sign;
        datePrint.setDataFormat(PrintData.FORMAT_TXT);
        datePrint.setTextAlign(PrintData.ALIGN_LEFT);
        datePrint.setLanguage(lang);
        datePrint.setText(dateStr);
        this.data.add(datePrint);
    }

    public void addCloseBillDate() {
        this.addBlankLine();
        this.addSingleLineCenteredTextPaddingWithDash(this.charSize,
                PrintService.instance.getResources().getString(R.string.check_closed), 0);

        Calendar now = Calendar.getInstance(Locale.US);
        String nowstr = TimeUtil.getCloseBillDataTime(now.getTimeInMillis());
        addSingleLineCenteredText(this.charSize, nowstr, 0);
    }


    public void addBillOrderStr(String str) {


//        StringBuilder sbr = new StringBuilder();
//        sbr.append(txt).append(reNext);
        PrintData orderStr = new PrintData();
        orderStr.setDataFormat(PrintData.FORMAT_TXT);
        orderStr.setTextAlign(PrintData.ALIGN_LEFT);
        orderStr.setText(str.toString());

        this.data.add(orderStr);

    }

    public void printDeliveryList(String orderNO, String appOrderNo, String customerInfo, String address, String time, String remarks) {
//        scale = 1;
//        PrintData kot = new PrintData();
//        kot.setDataFormat(PrintData.FORMAT_TXT);
//        kot.setFontsize(scale);
//        kot.setMarginTop(20);
//        kot.setLanguage(PrintData.LANG_CN);
        // kot.setText(this.getTwoAutoReLine(orderNO, customerInfo, scale));
//        this.data.add(kot);

        //流水号 NO
//        PrintData orderNoPrint = new PrintData();
//        orderNoPrint.setDataFormat(PrintData.FORMAT_TXT);
//        orderNoPrint.setTextAlign(PrintData.ALIGN_LEFT);
//		orderNoPrint.setFontsize(2);
//        orderNoPrint.setText(orderNO+reNext);
//        this.data.add(orderNoPrint);
        PrintData appPrint = new PrintData();
        String appStr = appOrderNo + reNext;
        appPrint.setDataFormat(PrintData.FORMAT_TXT);
        appPrint.setTextAlign(PrintData.ALIGN_LEFT);
        appPrint.setFontsize(2);
        appPrint.setText(appStr);
        this.data.add(appPrint);

        PrintData custPrint = new PrintData();

        String custStr = customerInfo + reNext;
        custPrint.setDataFormat(PrintData.FORMAT_TXT);
        custPrint.setTextAlign(PrintData.ALIGN_LEFT);
        custPrint.setText(custStr);
        this.data.add(custPrint);

        PrintData addressPrint = new PrintData();

        String addressStr = this.getTwoAutoReLine("", address, 1);
        addressPrint.setDataFormat(PrintData.FORMAT_TXT);
        addressPrint.setTextAlign(PrintData.ALIGN_LEFT);
        addressPrint.setText(addressStr);
        this.data.add(addressPrint);


        //Date
        PrintData datePrint = new PrintData();
        String dateStr = time + " " + reNext;
        datePrint.setDataFormat(PrintData.FORMAT_TXT);
        datePrint.setTextAlign(PrintData.ALIGN_LEFT);
        datePrint.setText(dateStr);
        this.data.add(datePrint);
        //Date
        PrintData rePrint = new PrintData();
        String reStr = remarks + " " + reNext;
        rePrint.setDataFormat(PrintData.FORMAT_TXT);
        rePrint.setTextAlign(PrintData.ALIGN_LEFT);
        rePrint.setText(reStr);
        this.data.add(rePrint);

    }


//    private String getTwoAutoReLine(String col1Title, String col2Title) {
//
//        String title1 = StringUtil.padRight(col1Title, 12);
//        String title2 = StringUtil.padLeft(col2Title, this.charSize - 12);
//        String result = title1.concat(title2).concat(reNext);
//        return result;
//    }

    public void AddAddress(String address) {

        PrintData subAddress = new PrintData();
        String orderAddress = StringUtil.padLeft(address, this.FIXED_COL4_TOTAL);
        String padSubtotal = orderAddress + reNext;
        subAddress.setDataFormat(PrintData.FORMAT_TXT);
        subAddress.setTextAlign(PrintData.ALIGN_RIGHT);
        subAddress.setText(padSubtotal);
        this.data.add(subAddress);
    }

    private String getTwoAutoReLine(String col1Content, String col2Content, int charScale) {
        StringBuffer result = new StringBuffer();

        int col1Lines = 1;

        int col2Lines = 1;

        int qtyLen = charScale * col1Content.length();
//        int qtyLen = 0;
        int addressSize = this.charSize / charScale - qtyLen / charScale - this.FIXED_COL4_SPACE;

        //double ln1 = col1Content.length();
        int ln1 = 1;
        String[] splitedcontents = {col1Content};

        try {
            //ln1 = (col1Content.getBytes("GBK").length)/(KOTPrint.COL2_ITEMNAME*1.0);
            splitedcontents = StringUtil.formatLn(qtyLen * 1, col1Content);
            ln1 = splitedcontents.length;
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        col1Lines = splitedcontents.length - 1;
        //col1Lines = StringUtil.nearestTen(ln1);
        //String col1PadContent = StringUtil.padRight(col1Content, col1Lines*KOTPrint.COL2_ITEMNAME);
        //ArrayList<String> splittedCol1Content = StringUtil.splitEqually(col1PadContent, KOTPrint.COL2_ITEMNAME);

//        double ln2 = (col2Content.length()) / (addressSize * 1.0 / charScale);
//        col2Lines = StringUtil.nearestTen(ln2);
        String col2PadContent = StringUtil.padRight(col2Content, col2Lines * addressSize / charScale);
        String[] splittedCol2Content = new String[0];
        try {
            splittedCol2Content = StringUtil.formatLn(addressSize, col2PadContent);
            col2Lines = splittedCol2Content.length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        for (int i = 0; i < Math.max(col1Lines, col2Lines); i++) {
            if (i < col1Lines) {
                //result.append(splittedCol1Content.get(i));
                //   result.append(StringUtil.padRight(splitedcontents[i], qtyLen));
            } else {
                //result.append(StringUtil.padLeft("", qtyLen));
            }
            //padding
            //  result.append(StringUtil.padRight("", this.FIXED_COL4_SPACE));

            if (i < col2Lines) {
                result.append(splittedCol2Content[i]);
            } else {
                result.append(StringUtil.padRight(" ", (addressSize) / charScale));
            }

            result.append(reNext);
        }
        return result.toString();
    }


    public void addBillDate() {

        this.addBlankLine();
        this.addHortionaDoublelLine(this.charSize);
        this.addBlankLine();
        Calendar now = Calendar.getInstance(Locale.US);
        String nowstr = TimeUtil.getCloseBillDataTime(now.getTimeInMillis());
        addSingleLineCenteredText(this.charSize, nowstr, 0);
        AddCut();
    }

    public void addCustomizedFieldAtFooter(String customized) {
        ////customized fields
        if (customized != null && customized.length() > 0) {
            StringBuilder ctbuf = new StringBuilder();
            String[] options = customized.split(";", -1);
            for (int i = 0; i < options.length; i++) {
                ctbuf.append(options[i]).append(reNext);
            }

            PrintData ctadd = new PrintData();
            ctadd.setDataFormat(PrintData.FORMAT_TXT);
            ctadd.setFontsize(1);
            ctadd.setTextAlign(PrintData.ALIGN_CENTRE);
            ctadd.setText(ctbuf.toString());
            this.data.add(ctadd);
        }
    }

    public void AddFooter(String op, boolean blankline) {
        if (blankline) {
            this.addBlankLine();
            this.addBlankLine();
        }
        addSingleLineCenteredText(this.charSize, op, 0);
        AddCut();
    }

    public void addWelcomeMsg() {
        this.addBlankLine();
        this.addBlankLine();
        addSingleLineCenteredText(this.charSize, PrintService.instance.getResources().getString(R.string.thank_you_and_see_you), 0);
    }

    public void AddQRCode(String qr) {
        PrintData qrCode = new PrintData();
        qrCode.setDataFormat(PrintData.FORMAT_QR);
        qrCode.setText(qr + reNext);
        qrCode.setTextAlign(PrintData.ALIGN_CENTRE);
        qrCode.setFontsize(2);
        this.data.add(qrCode);
    }

    public void setData(ArrayList<PrintData> data) {
        this.data = data;

    }
}
