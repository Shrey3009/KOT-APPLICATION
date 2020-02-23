package com.alfredposclient.push;

import android.content.Context;

import com.alfredbase.ParamConst;
import com.alfredbase.javabean.ReportDayPayment;
import com.alfredbase.javabean.ReportDaySales;
import com.alfredbase.javabean.ReportDayTax;
import com.alfredbase.utils.BH;
import com.alfredposclient.activity.OpenRestaruant;
import com.alfredposclient.global.App;
import com.alfredposclient.global.ReportObjectFactory;
import com.alfredposclient.global.SyncCentre;

import java.util.List;

/**
 * Created by Alex on 2017/6/23.
 */

public class SendEmailThread extends Thread {
    Context context;



    @Override
    public void run() {
        super.run();
        while(App.instance.getIndexOfActivity(OpenRestaruant.class) == -1){
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();

            }
        }
        try{
//
        ReportDaySales reportDaySales;
        List<ReportDayTax> reportDayTaxs;
            reportDaySales = ReportObjectFactory.getInstance().loadShowReportDaySales(App.instance.getBusinessDate());
            reportDayTaxs = ReportObjectFactory.getInstance().loadShowReportDayTax(App.instance.getBusinessDate());
            List<ReportDayPayment> reportDayPayments = ReportObjectFactory.getInstance().loadShowReportDayPayment(App.instance.getBusinessDate());
            if(reportDayTaxs != null && BH.getBD(reportDaySales.getTotalSales()).compareTo(BH.getBD(ParamConst.DOUBLE_ZERO)) == 1) {
                SyncCentre.getInstance().syncSendEmail(App.instance, reportDaySales, reportDayTaxs, reportDayPayments,null);
            }
           // SyncCentre.getInstance().syncMedia(App.instance,  null);
//            RevenueCenter revenueCenter = App.instance.getRevenueCenter();
//            String name = "alfred_" + revenueCenter.getId().intValue() + "_b";
//            String roomName = "alfred" + revenueCenter.getRestaurantId().intValue() + "g";
//            String pass = "amp" + name;
//            XMPP.getInstance().login(name, pass, revenueCenter.getId().toString(), roomName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {

        super.destroy();
    }

}
