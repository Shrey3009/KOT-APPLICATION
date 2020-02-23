package com.alfred.printer;

import android.util.Base64;

import com.alfred.print.jobs.PrintJob;
import com.alfred.print.jobs.Priority;
import com.alfred.remote.printservice.PrintService;
import com.alfred.remote.printservice.R;
import com.birbit.android.jobqueue.Params;

/**
 * Created by Alex on 16/10/20.
 */

public class TableQRCodePrint extends PrintJob {

    public TableQRCodePrint(String uuid, Long bizDate) {
        super(new Params(Priority.MID).requireNetwork().persist().groupBy("tableQrCode"),
                "tableQrCode",  uuid, bizDate);
    }

    public void AddRestaurantInfo(String logo, String name, String address, String dateTime) {

        if (logo!=null && logo.length()>0) {
            PrintData logoimg = new PrintData();
            logoimg.setDataFormat(PrintData.FORMAT_IMG);
            logoimg.addImage(Base64.decode(logo,  Base64.DEFAULT));
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
        rInfo.setText(rname.toString()+reNext);
        this.data.add(rInfo);

        //address
        StringBuilder addbuf = new StringBuilder();
        addbuf.append(address);

        PrintData radd = new PrintData();
        radd.setDataFormat(PrintData.FORMAT_TXT);
        radd.setFontsize(1);
        radd.setMarginTop(10);
        radd.setTextAlign(PrintData.ALIGN_CENTRE);
        radd.setText(addbuf.toString()+reNext);
        this.data.add(radd);

        PrintData datePrint = new PrintData();
        String dateStr = PrintService.instance.getResources().getString(R.string.date)+":"+dateTime+reNext;
        datePrint.setDataFormat(PrintData.FORMAT_TXT);
        datePrint.setTextAlign(PrintData.ALIGN_CENTRE);
        datePrint.setMarginTop(10);
        datePrint.setText(dateStr);
        this.data.add(datePrint);
        addHortionalLine(this.charSize);

    }


    public void AddTitle(String Title) {
        StringBuilder sbr = new StringBuilder();
        sbr.append(Title + reNext);

        PrintData header = new PrintData();
        header.setDataFormat(PrintData.FORMAT_TXT);
        header.setFontsize(1);
        header.setTextAlign(PrintData.ALIGN_CENTRE);
        header.setText(sbr.toString());
        this.data.add(header);
    }

    public void AddQRCode(String name, String qrCodeText){
        PrintData header = new PrintData();
        header.setDataFormat(PrintData.FORMAT_TXT);
        header.setFontsize(1);
        header.setTextAlign(PrintData.ALIGN_CENTRE);
        header.setText(reNext+reNext+name.toString()+reNext);
        this.data.add(header);
        PrintData qrcode = new PrintData();
        qrcode.setDataFormat(PrintData.FORMAT_QR);
        qrcode.setTextAlign(PrintData.ALIGN_CENTRE);
        qrcode.setQrCode(qrCodeText);
        this.data.add(qrcode);

    }

    public void AddFooter(String op) {
        addSingleLineText(this.charSize,op, 0);
        AddCut();
        AddSing();

    }
}
