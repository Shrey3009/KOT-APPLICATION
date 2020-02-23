package com.alfred.printer;

import android.text.TextUtils;

import com.alfred.print.jobs.PrintJob;
import com.alfred.print.jobs.Priority;
import com.alfred.remote.printservice.PrintService;
import com.alfred.remote.printservice.R;
import com.birbit.android.jobqueue.Params;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by Arif S. on 2019-09-11
 */
public class TransferOrder extends PrintJob {
    public TransferOrder(String uuid, Long bizDate) {
        super(new Params(Priority.MID).requireNetwork().persist().groupBy("transfer"),
                "transfer", uuid, bizDate);
        if (this.charSize == 33) {
            KOTPrint.FIXED_COL2_QTY = 4;
            KOTPrint.FIXED_COL2_SPACE = 1;
        }
    }

    public void AddTitle(String title, String revenueCenterName, String tableName, Integer orderNo) {
        addFeed();
        StringBuilder sbr = new StringBuilder();
        sbr.append(title)
                .append(reNext)
                .append(revenueCenterName)
                .append(reNext)
                .append(tableName)
                .append(reNext)
                .append("Order No : " + orderNo)
                .append(reNext);

        PrintData header = new PrintData();
        header.setDataFormat(PrintData.FORMAT_TXT);
        header.setFontsize(2);
        header.setTextAlign(PrintData.ALIGN_CENTRE);
        header.setText(sbr.toString());
        this.data.add(header);

        addHortionalLine(this.charSize);
    }

    public void addItem(String itemName, int qty) {
        PrintData printData = new PrintData();
        printData.setDataFormat(PrintData.FORMAT_TXT);
        printData.setFontsize(2);
        printData.setMarginTop(20);
        printData.setLanguage(PrintData.LANG_EN);
        printData.setText(this.getTwoColContent(itemName, new Integer(qty).toString(), 2));
        this.data.add(printData);
    }

    public void AddModifierItem(String modifiers) {
        AddModifierItem(modifiers, 2);
    }

    public void AddModifierItem(String modifiers, int kotFontSize) {
        if (modifiers != null) {

            PrintData kot = new PrintData();
            kot.setDataFormat(PrintData.FORMAT_TXT);
            kot.setFontsize(10);
            kot.setUnderline(true);
            kot.setLanguage(PrintData.LANG_CN);
            kot.setTextAlign(PrintData.ALIGN_LEFT);
            kot.setMarginTop(2);
            modifiers = modifiers + reNext;
            kot.setText(modifiers);
            this.data.add(kot);
        }
    }

    public void AddFooter(String op, String time) {
        addHortionalLine(this.charSize);
        if (!TextUtils.isEmpty(time)) {
            addSingleLineText(0, "Transfer Time : " + time, 0);
            addLineSpace(1);
        }
        addSingleLineText(this.charSize, op, 0);
        addHortionalLine(this.charSize);
        AddCut();
        AddSing();

    }

    /* Two columns layout (Width = 48dots)
     * |item Name   Dynamical   |2| QTY 10/scale  |
     *
     **/
    private String getTwoColContent(String col1Content, String col2Content, int charScale) {
        StringBuffer result = new StringBuffer();

        int col1Lines = 1;

        int col2Lines = 1;

//		int qtyLen = KOTPrint.FIXED_COL2_QTY;
//		if(charScale > 1){
        int qtyLen = charScale * col2Content.length();
//		}

        KOTPrint.COL2_ITEMNAME = this.charSize / charScale - qtyLen / charScale - KOTPrint.FIXED_COL2_SPACE;

        //double ln1 = col1Content.length();
        int ln1 = 1;
        String[] splitedcontents = {col1Content};

        try {
            //ln1 = (col1Content.getBytes("GBK").length)/(KOTPrint.COL2_ITEMNAME*1.0);
            splitedcontents = StringUtil.formatLn(KOTPrint.COL2_ITEMNAME * 1, col1Content);
            ln1 = splitedcontents.length;
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        col1Lines = splitedcontents.length;
        //col1Lines = StringUtil.nearestTen(ln1);
        //String col1PadContent = StringUtil.padRight(col1Content, col1Lines*KOTPrint.COL2_ITEMNAME);
        //ArrayList<String> splittedCol1Content = StringUtil.splitEqually(col1PadContent, KOTPrint.COL2_ITEMNAME);

        double ln2 = (col2Content.length()) / (qtyLen * 1.0 / charScale);
        col2Lines = StringUtil.nearestTen(ln2);
        String col2PadContent = StringUtil.padRight(col2Content, col2Lines * qtyLen / charScale);
        ArrayList<String> splittedCol2Content = StringUtil.splitEqually(col2PadContent, qtyLen / charScale);


        for (int i = 0; i < Math.max(col1Lines, col2Lines); i++) {
            if (i < col1Lines) {
                //result.append(splittedCol1Content.get(i));
                result.append(StringUtil.padRight(splitedcontents[i], KOTPrint.COL2_ITEMNAME));
            } else {
                result.append(StringUtil.padRight(" ", KOTPrint.COL2_ITEMNAME));
            }
            //padding
            result.append(StringUtil.padRight(" ", KOTPrint.FIXED_COL2_SPACE));

            if (i < col2Lines) {
                result.append(splittedCol2Content.get(i));
            } else {
                result.append(StringUtil.padRight(" ", (qtyLen) / charScale));
            }

            result.append(reNext);
        }
        return result.toString();
    }

    public void addLineSpace(int lines) {
        PrintData kot = new PrintData();
        kot.setDataFormat(PrintData.FORMAT_TXT);
        kot.setFontsize(1);
        kot.setLanguage(PrintData.LANG_CN);
        kot.setText(reNext);
        this.data.add(kot);
    }

    public void addFeed() {
        PrintData kot = new PrintData();
        kot.setDataFormat(PrintData.FORMAT_FEED);
        kot.setMarginTop(3);
        this.data.add(kot);
    }

}
