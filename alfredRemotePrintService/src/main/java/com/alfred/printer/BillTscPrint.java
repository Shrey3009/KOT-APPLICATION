package com.alfred.printer;

import android.text.TextUtils;
import android.util.Base64;

import com.alfred.print.jobs.PrintJob;
import com.alfred.print.jobs.Priority;
import com.alfred.remote.printservice.PrintService;
import com.alfred.remote.printservice.R;
import com.alfredbase.utils.BH;
import com.alfredbase.utils.TimeUtil;
import com.birbit.android.jobqueue.Params;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BillTscPrint extends PrintJob {


    public int nameSize = 17;
    public int modSize = 19;
    public int tianSize = 15;
    public int lableSize;
    int size = 0;// 标签长度
    public static int COL4_ITEMNAME; // Width = CharSize/scale - FIXED_COL2_QTY/scale -
    // FIXED_COL2_PRICE/scale- FIXED_COL2_TOTAL/scale- FIXED_COL2_SPACE *3

    public BillTscPrint(String uuid, Long bizDate, int size) {
        super(new Params(Priority.HIGH).requireNetwork().persist().groupBy("lable"), "lable", uuid, bizDate);
        this.size = size;

    }


    public void AddRestaurantInfo(String logo, String name, String id, int num, String tNum, String itemName, String modifier, String price, boolean Identification) {

        if (logo != null && logo.length() > 0) {
            PrintTscData logoimg = new PrintTscData();
            logoimg.setDataFormat(PrintTscData.FORMAT_IMG);
            logoimg.addImage(Base64.decode(logo, Base64.DEFAULT));
//			logoimg.setTextAlign(PrintTscData.ALIGN_CENTRE);
            this.tdata.add(logoimg);
        }
        //address

        //大标签
        if (size == 26) {
            String strno;
            StringBuilder addbuf = new StringBuilder();
            //  int padlen = size - id.getBytes("GBK").length*2;
            // strnum = StringUtil.padLeft(num + "/" + tNum+" ", padlen/2);
            strno = StringUtil.padLeft(id + "", size / 2 - 1);
            addbuf.append(strno);

            PrintTscData radd = new PrintTscData();
            radd.setDataFormat(PrintTscData.FORMAT_TXT);
            radd.setFontsizeX(2);
            radd.setFontsizeY(1);
            radd.setX(0);
            radd.setY(10);
            radd.setText(addbuf.toString());
            this.tdata.add(radd);


            ++num;
            String strnum;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd ", Locale.US);// HH:mm:ss
            Date date = new Date(System.currentTimeMillis());
            StringBuilder numbuf = new StringBuilder();
            String dates = simpleDateFormat.format(date).toString().trim();
            numbuf.append(dates);
            int nsize = 52;
            int padlen = 0;
            try {
                padlen = size - dates.getBytes("GBK").length * 2;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            strnum = StringUtil.padLeft(num + "/" + tNum + " ", padlen * 2 + 1);

            numbuf.append(strnum);
            //name
            PrintTscData rInfo = new PrintTscData();
            rInfo.setDataFormat(PrintTscData.FORMAT_TXT);
            rInfo.setFontsizeX(1);
            rInfo.setFontsizeY(1);
            rInfo.setX(25);
            rInfo.setY(45);
            rInfo.setText(numbuf.toString());
            this.tdata.add(rInfo);


            StringBuilder totalbuf = new StringBuilder();
            String strPrice = StringUtil.padLeft(price + "", size - 2);
            totalbuf.append(strPrice);

            PrintTscData total = new PrintTscData();
            total.setDataFormat(PrintTscData.FORMAT_TXT);
            total.setFontsizeX(1);
            total.setFontsizeY(1);
            total.setX(10);
            total.setY(80);
            total.setText(totalbuf.toString());
            this.tdata.add(total);
            toMultiLine(itemName, nameSize, 80);
            if (!TextUtils.isEmpty(modifier)) {
                String newmod = modifier.substring(0, modifier.length() - 1);
                modMultiLine(itemName, modSize, 175, newmod);
            }


            StringBuilder rename = new StringBuilder();

            rename.append(name);

            PrintTscData names = new PrintTscData();
            names.setDataFormat(PrintTscData.FORMAT_TXT);
            names.setFontsizeX(1);
            names.setFontsizeY(1);
            names.setX(25);
            names.setY(210);
            //radd.setTextAlign(PrintTscData.ALIGN_CENTRE);
            names.setText(rename.toString());
            this.tdata.add(names);

//

            PrintTscData reset = new PrintTscData();
            reset.setDataFormat(PrintTscData.FORMAT_RESET);
            this.tdata.add(reset);
//
//		//	addtdata(this.tdata);
//		//addHortionalLine(this.charSize);
        }


        if (size == 25) {
            String strno;
            StringBuilder addbuf = new StringBuilder();
            //  int padlen = size - id.getBytes("GBK").length*2;
            // strnum = StringUtil.padLeft(num + "/" + tNum+" ", padlen/2);
            strno = StringUtil.padLeft(id + "", size / 2 - 1);
            addbuf.append(strno);

            PrintTscData radd = new PrintTscData();
            radd.setDataFormat(PrintTscData.FORMAT_TXT);
            radd.setFontsizeX(2);
            radd.setFontsizeY(1);
            radd.setX(3);
            radd.setY(10);
            radd.setText(addbuf.toString());
            this.tdata.add(radd);

            //  日期
            ++num;
            String strnum;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd ", Locale.US);// HH:mm:ss
            Date date = new Date(System.currentTimeMillis());
            StringBuilder datebuf = new StringBuilder();
            String dates = simpleDateFormat.format(date).toString().trim();
            datebuf.append(dates);

//            int padlen = 0;
//            try {
//                padlen = size - dates.getBytes("GBK").length * 2;
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//            strnum = StringUtil.padLeft(num + "/" + tNum + " ", padlen * 2 + 1);
//
//            numbuf.append(strnum);
            //name
            PrintTscData rInfo = new PrintTscData();
            rInfo.setDataFormat(PrintTscData.FORMAT_TXT);
            rInfo.setFontsizeX(1);
            rInfo.setFontsizeY(1);
            rInfo.setX(20);
            rInfo.setY(10);
            rInfo.setText(datebuf.toString());
            this.tdata.add(rInfo);

            //数量
            StringBuilder numbuf = new StringBuilder();
            String strNum = StringUtil.padLeft(num + "/" + tNum + " " + "", size - 3);
            numbuf.append(strNum);

            PrintTscData printNum = new PrintTscData();
            printNum.setDataFormat(PrintTscData.FORMAT_TXT);
            printNum.setFontsizeX(1);
            printNum.setFontsizeY(1);
            printNum.setX(10);
            printNum.setY(45);
            printNum.setText(strNum.toString());
            this.tdata.add(printNum);


            toTianMultiLine(itemName, nameSize, 50);
            if (!TextUtils.isEmpty(modifier)) {
                String newmod = modifier.substring(0, modifier.length() - 1);
                modTianMultiLine(itemName, modSize, 170, newmod);
            }

            StringBuilder totalbuf = new StringBuilder();
            String strPrice = StringUtil.padLeft(price + "", size - 3);
            totalbuf.append(strPrice);

            PrintTscData total = new PrintTscData();
            total.setDataFormat(PrintTscData.FORMAT_TXT);
            total.setFontsizeX(1);
            total.setFontsizeY(1);
            total.setX(10);
            total.setY(80);
            total.setText(totalbuf.toString());
            this.tdata.add(total);
            toTianMultiLine(itemName, nameSize, 50);
            if (!TextUtils.isEmpty(modifier)) {
                String newmod = modifier.substring(0, modifier.length() - 1);
                modTianMultiLine(itemName, modSize, 170, newmod);
            }


//            StringBuilder rename = new StringBuilder();
//
//            rename.append(name);
//
//            PrintTscData names = new PrintTscData();
//            names.setDataFormat(PrintTscData.FORMAT_TXT);
//            names.setFontsizeX(1);
//            names.setFontsizeY(1);
//            names.setX(25);
//            names.setY(170);
//            //radd.setTextAlign(PrintTscData.ALIGN_CENTRE);
//            names.setText(rename.toString());
//            this.tdata.add(names);

//

            PrintTscData reset = new PrintTscData();
            reset.setDataFormat(PrintTscData.FORMAT_RESET);
            this.tdata.add(reset);
//
//		//	addtdata(this.tdata);
//		//addHortionalLine(this.charSize);
        }


    }


    //商品名换行
    private void toMultiLine(String str, int len, int s) {
        String[] splitedcontents = new String[0];
        int size;
        int y;
        y = s;
        try {
            splitedcontents = StringUtil.formatLn(nameSize, str);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (splitedcontents.length >= 3) {
            size = 3;
        } else {

            size = splitedcontents.length;
        }
        for (int i = 0; i < size; i++) {
            StringBuilder itemnbuf = new StringBuilder();
            itemnbuf.append(splitedcontents[i]);
            PrintTscData itemi = new PrintTscData();
            itemi.setDataFormat(PrintTscData.FORMAT_TXT);
            itemi.setFontsizeX(1);
            itemi.setFontsizeY(1);
            itemi.setX(10);
            itemi.setY(y);
            //radd.setTextAlign(PrintTscData.ALIGN_CENTRE);
            itemi.setText(itemnbuf.toString());
            this.tdata.add(itemi);
            y = y + 30;

        }
//

    }


    //配料换行
    private void modMultiLine(String itemname, int len, int s, String mod) {
        String[] splitedmod = new String[0];
        String[] splitedcontents = new String[0];
        try {
            splitedcontents = StringUtil.formatLn(nameSize, itemname);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        int size;
        int y;
        y = s;
        try {
            splitedmod = StringUtil.formatLn(22, mod);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        size = splitedmod.length;

        if (splitedcontents.length >= 3) {
            size = 1;
        } else if (splitedcontents.length > 1 && splitedcontents.length <= 2) {
            if (size >= 2) {
                size = 2;
            }
            y = y - 30;
        } else {
            if (size >= 3) {
                size = 3;
            }
            y = y - 60;

        }

        for (int i = 0; i < size; i++) {
            StringBuilder itemnbuf = new StringBuilder();
            itemnbuf.append(splitedmod[i]);
            PrintTscData itemi = new PrintTscData();
            itemi.setDataFormat(PrintTscData.FORMAT_TXT);
            itemi.setFontsizeX(1);
            itemi.setFontsizeY(1);
            itemi.setX(25);
            itemi.setY(y);
            //radd.setTextAlign(PrintTscData.ALIGN_CENTRE);
            itemi.setText(itemnbuf.toString());
            this.tdata.add(itemi);
            y = y + 30;

        }


    }
//


    //商品名换行
    private void toTianMultiLine(String str, int len, int s) {
        String[] splitedcontents = new String[0];
        int size;
        int y;
        y = s;
        try {
            splitedcontents = StringUtil.formatLn(15, str);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (splitedcontents.length >= 3) {
            size = 3;
        } else {
            size = splitedcontents.length;
        }
        for (int i = 0; i < size; i++) {
            StringBuilder itemnbuf = new StringBuilder();
            itemnbuf.append(splitedcontents[i]);
            PrintTscData itemi = new PrintTscData();
            itemi.setDataFormat(PrintTscData.FORMAT_TXT);
            itemi.setFontsizeX(1);
            itemi.setFontsizeY(1);
            itemi.setX(10);
            itemi.setY(y);
            //radd.setTextAlign(PrintTscData.ALIGN_CENTRE);
            itemi.setText(itemnbuf.toString());
            this.tdata.add(itemi);
            y = y + 30;

        }
//

    }


    //配料换行
    private void modTianMultiLine(String itemname, int len, int s, String mod) {
        String[] splitedmod = new String[0];
        String[] splitedcontents = new String[0];
        try {
            splitedcontents = StringUtil.formatLn(nameSize, itemname);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        int size;
        int y;
        y = s;
        try {
            splitedmod = StringUtil.formatLn(20, mod);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        size = splitedmod.length;

        if (splitedcontents.length >= 3) {
            size = 2;
        } else if (splitedcontents.length > 1 && splitedcontents.length <= 2) {
            if (size >= 2) {
                size = 3;
            }
            y = y - 60;
        } else {
            if (size >= 3) {
                size = 3;
            }
            y = y - 60;

        }

//        if (splitedcontents.length >= 2) {
//            size = 1;
//            y = y - 30;
//        } else {
//            if (size >= 2) {
//                size = 2;
//            }
//            y = y - 60;
//
//        }

        for (int i = 0; i < size; i++) {
            StringBuilder itemnbuf = new StringBuilder();
            itemnbuf.append(splitedmod[i]);
            PrintTscData itemi = new PrintTscData();
            itemi.setDataFormat(PrintTscData.FORMAT_TXT);
            itemi.setFontsizeX(1);
            itemi.setFontsizeY(1);
            itemi.setX(25);
            itemi.setY(y);
            //radd.setTextAlign(PrintTscData.ALIGN_CENTRE);
            itemi.setText(itemnbuf.toString());
            this.tdata.add(itemi);
            y = y + 30;

        }


    }
//

}
