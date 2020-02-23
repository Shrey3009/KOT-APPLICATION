package com.alfred.callnum.global;

import android.app.Activity;
import android.content.SharedPreferences;

import com.alfred.callnum.adapter.CallBean;
import com.alfred.callnum.http.server.CallNumHttpServer;
import com.alfred.callnum.utils.TvPref;
import com.alfredbase.BaseApplication;
import com.alfredbase.global.BugseeHelper;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.Store;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App extends BaseApplication {
    private static final String TAG = App.class.getSimpleName();
    public static App instance;
    boolean mbPlayIMG;
    public String VERSION;
    private String posIp;
    private int mainPageType = 1;

    public static final int HANDLER_REFRESH_CALL = 1;
    public static final int HANDLER_CLEAN_CALL = 3;
    public static final int HANDLER_REFRESH_CALL_ON = 2;

    public static final String CALL_NUMBER_TYPE = "CALL_NUMBER_TYPE";

    private static final String DATABASE_NAME = "com.alfred.callnum";
    SharedPreferences sp;

    private List<CallBean> save;
    private List<CallBean> callList;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        SQLExe.init(this, DATABASE_NAME, DATABASE_VERSION);
        TvPref.init();

        mbPlayIMG = TvPref.readPlayIMGEn();
        VERSION = getAppVersionName();
        CallNumHttpServer callNumHttpServer = new CallNumHttpServer();
        sp = getSharedPreferences("call", Activity.MODE_PRIVATE);
        callList = new ArrayList<CallBean>();
        BugseeHelper.init(this, "855edcc3-0ec8-40f7-b3e8-31ef79540932");
        try {
            if (!callNumHttpServer.isAlive()) {
                callNumHttpServer.start();
            }
        } catch (IOException e) {
            callNumHttpServer.stop();
        }
    }

    public List<CallBean> getCallList() {
        if (callList == null || callList.size() == 0) {
            callList = getSave();
        }
        return callList;
    }

    public void setCall(CallBean call) {

        if (callList != null) {
            callList.add(call);
            if (callList.size() > 30) {
                callList.remove(0);
            }
        }


    }

    public void setCallList(List<CallBean> call) {
        this.callList = call;

    }

    public List<CallBean> getSave() {


        //用getString获取值
        String name = sp.getString("calllist", "");
        Gson gson = new Gson();
        ArrayList<CallBean> list = gson.fromJson(name, new TypeToken<ArrayList<CallBean>>() {
        }.getType());

        return list;
    }

    public void setSave() {


        // 获取Editor对象
        SharedPreferences.Editor editor = sp.edit();
        List<String> list = new ArrayList<String>();
        Gson gson = new Gson();
        String data = gson.toJson(getCallList());

        editor.putString("calllist", data);
        editor.commit();

    }

    public boolean getPlayIMGEn() {
        return mbPlayIMG;
    }

    public void setPlayIMGEn(boolean enable) {
        mbPlayIMG = enable;
        TvPref.savePlayIMGEn(enable);
    }

    public String getPosIp() {

        posIp = Store.getString(instance,
                Store.CALL_IP);
        return posIp;
    }

    public void setPosIp(String posIp) {
        Store.putString(instance, Store.CALL_IP, posIp);

    }

    public int getMainPageType() {

        Integer value = Store.getInt(instance,
                Store.CALL_NUMBER_TYPE);

        if (value != null && value != Store.DEFAULT_INT_TYPE) {
            return value.intValue();
        } else {
            return 1;
        }

    }

    public void setMainPageType(int mainPageType) {
        Store.putInt(instance, Store.CALL_NUMBER_TYPE, mainPageType);
    }

    /**
     * 数据存放在本地
     *
     * @param tArrayList
     */


}