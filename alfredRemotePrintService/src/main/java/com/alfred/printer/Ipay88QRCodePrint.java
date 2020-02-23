package com.alfred.printer;

import android.text.TextUtils;
import android.util.Base64;

import com.alfred.print.jobs.PrintJob;
import com.alfred.print.jobs.Priority;
import com.alfred.remote.printservice.PrintService;
import com.alfred.remote.printservice.R;
import com.birbit.android.jobqueue.Params;


public class Ipay88QRCodePrint extends PrintJob {

    public static String IPAY88PRINTKEY = "IPAY88QRCODE";

    public Ipay88QRCodePrint(String uuid, Long bizDate) {
        super(new Params(Priority.MID).requireNetwork().persist().groupBy(IPAY88PRINTKEY),
                IPAY88PRINTKEY, uuid, bizDate);
    }


    public void AddRestaurantInfo(String logo, String name, String address, String dateTime) {

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

        if (!TextUtils.isEmpty(address)) {
            PrintData radd = new PrintData();
            radd.setDataFormat(PrintData.FORMAT_TXT);
            radd.setFontsize(1);
            radd.setMarginTop(10);
            radd.setTextAlign(PrintData.ALIGN_CENTRE);
            radd.setText(address + reNext);
            this.data.add(radd);
        }

        this.AddNewLine();
        PrintData datePrint = new PrintData();
        String dateStr = PrintService.instance.getResources().getString(R.string.date) + ":" + dateTime + reNext;
        datePrint.setDataFormat(PrintData.FORMAT_TXT);
        datePrint.setTextAlign(PrintData.ALIGN_LEFT);
        datePrint.setMarginTop(40);
        datePrint.setText(dateStr);
        this.data.add(datePrint);

    }

    public void AddBillNo(String billNo) {

        PrintData billNoPrint = new PrintData();
        String bill = PrintService.instance.getResources().getString(R.string.bill_no_) + ":" + billNo + reNext;
        billNoPrint.setDataFormat(PrintData.FORMAT_TXT);
        billNoPrint.setTextAlign(PrintData.ALIGN_LEFT);
        billNoPrint.setText(bill);
        this.data.add(billNoPrint);
    }

    public void addTableName(String tableName) {

        if (!TextUtils.isEmpty(tableName)) {
            PrintData tNamePrint = new PrintData();
            String tName = PrintService.instance.getResources().getString(R.string.table_name) + tableName + reNext;
            tNamePrint.setDataFormat(PrintData.FORMAT_TXT);
            tNamePrint.setTextAlign(PrintData.ALIGN_LEFT);
            tNamePrint.setText(tName);
            this.data.add(tNamePrint);
        }
    }

    public void addPax(Integer pax) {
        if (pax > 0) {
            PrintData paxPrint = new PrintData();
            String p = PrintService.instance.getResources().getString(R.string.pax) + ":" + pax + reNext;
            paxPrint.setDataFormat(PrintData.FORMAT_TXT);
            paxPrint.setTextAlign(PrintData.ALIGN_LEFT);
            paxPrint.setText(p);
            this.data.add(paxPrint);
        }

    }

    public void AddString(String Title, int fontSize) {
        StringBuilder sbr = new StringBuilder();
        sbr.append(Title + reNext);
        PrintData header = new PrintData();
        header.setDataFormat(PrintData.FORMAT_TXT);
        header.setFontsize(fontSize);
        header.setTextAlign(PrintData.ALIGN_CENTRE);
        header.setText(sbr.toString());
        this.data.add(header);
    }

    public void AddQRCode(byte[] bitmap) {
        PrintData header = new PrintData();
        PrintData qrcode = new PrintData();
        qrcode.setDataFormat(PrintData.FORMAT_QR_BITMAP);
        qrcode.setQrCodeBitmap(bitmap);
        qrcode.setTextAlign(PrintData.ALIGN_CENTRE);
        this.data.add(qrcode);

    }

    public void AddNewLine() {
        StringBuilder sbr = new StringBuilder();
        sbr.append(reNext);
        PrintData line = new PrintData();
        line.setDataFormat(PrintData.FORMAT_TXT);
        line.setMarginTop(40);
        line.setText(sbr.toString());
        this.data.add(line);
    }


    public void close() {
        this.AddNewLine();
        AddCut();
        AddSing();

    }


}
