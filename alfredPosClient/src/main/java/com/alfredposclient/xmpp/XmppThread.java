package com.alfredposclient.xmpp;

import com.alfredbase.javabean.RevenueCenter;
import com.alfredposclient.activity.OpenRestaruant;
import com.alfredposclient.global.App;

/**
 * Created by Alex on 2017/6/23.
 */

public class XmppThread extends Thread {
    @Override
    public void run() {
        super.run();
        while(App.instance.getIndexOfActivity(OpenRestaruant.class) == -1){
            try {
                Thread.sleep(3000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try{
            RevenueCenter revenueCenter = App.instance.getRevenueCenter();
            String name = "alfred_" + revenueCenter.getId().intValue() + "_b";
            String roomName = "alfred" + revenueCenter.getRestaurantId().intValue() + "g";
            String pass = "amp" + name;
            XMPP.getInstance().login(name, pass, revenueCenter.getId().toString(), roomName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {

        XMPP.getInstance().close();
        super.destroy();
    }

}
