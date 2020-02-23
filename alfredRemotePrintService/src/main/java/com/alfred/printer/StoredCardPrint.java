package com.alfred.printer;

import com.alfred.print.jobs.PrintJob;
import com.alfred.print.jobs.Priority;
import com.birbit.android.jobqueue.Params;

/**
 * Created by Alex on 16/10/20.
 */

public class StoredCardPrint extends PrintJob {

    public StoredCardPrint(String uuid, Long bizDate) {
        super(new Params(Priority.MID).requireNetwork().persist().groupBy("storedCard"),
                "storedCard",  uuid, bizDate);
    }

    public void AddTitle(String titleName) {
        StringBuilder sbr = new StringBuilder();
        sbr.append(titleName + reNext);

        PrintData header = new PrintData();
        header.setDataFormat(PrintData.FORMAT_TXT);
        header.setFontsize(2);
        header.setTextAlign(PrintData.ALIGN_CENTRE);
        header.setText(sbr.toString());
        this.data.add(header);
        addHortionalLine(this.charSize);
    }

    private String getTwoColHeader(String col1Title, String col2Title) {
        String title1 = StringUtil.padRight(col1Title, this.charSize-KOTPrint.COL2_ITEMNAME);
        String title2 = StringUtil.padRight(col2Title, KOTPrint.COL2_ITEMNAME);
        String result = title1.concat(title2).concat(reNext);
        return result;
    }

    public void AddItem(String name, String context){
        PrintData item = new PrintData();
        item.setDataFormat(PrintData.FORMAT_TXT);
        item.setFontsize(1);
        item.setTextAlign(PrintData.ALIGN_LEFT);
        item.setText(name+context + reNext);
        this.data.add(item);
    }

    public void AddFooter(String op) {
        addHortionalLine(this.charSize);
        addSingleLineText(this.charSize,op, 0);
        addHortionalLine(this.charSize);
        AddCut();
        AddSing();

    }
}
