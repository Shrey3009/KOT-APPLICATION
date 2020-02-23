package com.alfredselfhelp.global;

import android.os.Handler;
import android.text.TextUtils;

import com.alfredbase.BaseApplication;
import com.alfredbase.ParamConst;
import com.alfredbase.utils.CommonUtil;
import com.alfredbase.utils.LogUtil;
import com.alfredselfhelp.activity.MenuActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okio.BufferedSink;
import okio.BufferedSource;

public class CCCentre {
    private static final String TAG = "TAG";
    private  String HOST;
    private static final int PORT = 8582;
    private static final int LOCAL_PORT = 8583;
    private BufferedSink mSink;
    private BufferedSource mSource;
    private OutputStream out;
    private BufferedReader bff;
    private ExecutorService mExecutorService = null;
    private static CCCentre instance;
    private String paymentMsg;
    private static final String sales = "C200";
    private static final String ntesSales = "C610";
    private static final String amount = "0412%012d";
    private static final String identifier = "5706%06d";
    private Handler handler;
    private Socket socket;
    private String lastRespont;
    public static CCCentre getInstance(){
        if(instance == null){
            instance = new CCCentre();
        }
        return instance;
    }

    private CCCentre() {
        mExecutorService = Executors.newCachedThreadPool();
    }

    public void init(Handler handler){
        this.handler = handler;
    }

    public void connect(String ip) {
//        UIHelp.showToast(App.instance, "try to connect " + ip);
        if(!TextUtils.isEmpty(ip)) {
            this.HOST = ip;
            mExecutorService.execute(new ConnectService());
        }
    }

    int i = 1;

    public void startPay(String price, int timeOut) {

        if (mExecutorService != null && !TextUtils.isEmpty(HOST)) {
            StringBuffer str = new StringBuffer(sales);
            str.append(String.format(Locale.US,amount, Integer.parseInt(price)));
            str.append(String.format(Locale.US,identifier, i++));
            send(str.toString(), timeOut);
            paymentMsg = str.toString();
        }
    }

    public void startEZLinkPay(String price, int timeOut) {

        if (mExecutorService != null && !TextUtils.isEmpty(HOST)) {
            StringBuffer str = new StringBuffer(ntesSales);
            str.append(String.format(Locale.US,amount, Integer.parseInt(price)));
            str.append(String.format(Locale.US,identifier, i++));
            send(str.toString(), timeOut);
            paymentMsg = str.toString();
        }
    }

    public void send(String sendMsg, int timeOut) {
        if(mExecutorService != null && !TextUtils.isEmpty(HOST)) {
            mExecutorService.execute(new SendService(sendMsg, timeOut));
        }
    }

    public void disconnect() {
        if(mExecutorService != null && !TextUtils.isEmpty(HOST)) {
            mExecutorService.execute(new DisConnectService());
        }
    }

    private class SendService implements Runnable {
        private String msg;
        private int timeOut;
        SendService(String msg, int timeOut) {
            this.msg = msg;
            this.timeOut = timeOut;
        }

        @Override
        public void run() {
            if(BaseApplication.isOpenLog){
                Map<String, Object> cardInfoMap = new HashMap<>();
                int a =  (int)(1+Math.random()*(10-1+1));
                int paymentType = ParamConst.SETTLEMENT_TYPE_VISA;
                switch (a%3){
                    case 0 :
                        paymentType = ParamConst.SETTLEMENT_TYPE_VISA;
                        break;
                    case 1:
                        paymentType = ParamConst.SETTLEMENT_TYPE_MASTERCARD;
                        break;
                    case 2:
                        paymentType = ParamConst.SETTLEMENT_TYPE_EZLINK;
                        break;
                }
                cardInfoMap.put("paymentType", paymentType);
                cardInfoMap.put("cardNum", "22223");
                handler.sendMessage(handler.obtainMessage(MenuActivity.VIEW_CC_PAYMENT_HAS_CARDNUM_SUCCEED, cardInfoMap));
            }else {
                try {
                    if (out != null) {
                        socket.setSoTimeout(timeOut);
                        out.write(this.msg.getBytes());
                        out.flush();
                        bff = new BufferedReader(new InputStreamReader(
                                socket.getInputStream()));
                        String line;
                        StringBuffer buffer = new StringBuffer();
                        while ((line = bff.readLine()) != null) {
                            buffer.append(line);
                        }
                        lastRespont = buffer.toString();
                        lastRespont = lastRespont.toUpperCase();
                        if (lastRespont.contains("R200")
                                && lastRespont.contains("390200")) {
                            int paymentType = ParamConst.SETTLEMENT_TYPE_VISA;
                            if (lastRespont.contains("VISA")) {
                                paymentType = ParamConst.SETTLEMENT_TYPE_VISA;
                            }
                            if (lastRespont.contains("MASTERCARD")) {
                                paymentType = ParamConst.SETTLEMENT_TYPE_MASTERCARD;
                            }
                            if (lastRespont.contains("AMERICAN EXPRESS")) {
                                paymentType = ParamConst.SETTLEMENT_TYPE_AMEX;
                            }
                            if (lastRespont.contains("JCB")) {
                                paymentType = ParamConst.SETTLEMENT_TYPE_JCB;
                            }
                            if (lastRespont.contains("DINERS")) {
                                paymentType = ParamConst.SETTLEMENT_TYPE_DINNER_INTERMATIONAL;
                            }
                            if (lastRespont.contains("UNIONPAY")) {
                                paymentType = ParamConst.SETTLEMENT_TYPE_UNIPAY;
                            }
//                        if(lastRespont.contains("NETS")){
//                            paymentType = ParamConst.SETTLEMENT_TYPE_NETS;
//                        }
                            String x = "XXXXXXXXXXXX";
                            String cardNum = "";
                            if (lastRespont.contains(x)) {
                                try {
                                    String[] strings = lastRespont.split(x);
                                    String last = strings[1];
                                    cardNum = last.substring(0, 4);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            Map<String, Object> cardInfoMap = new HashMap<>();
                            cardInfoMap.put("paymentType", paymentType);
                            cardInfoMap.put("cardNum", cardNum);

                            handler.sendMessage(handler.obtainMessage(MenuActivity.VIEW_CC_PAYMENT_HAS_CARDNUM_SUCCEED, cardInfoMap));
                        } else if (lastRespont.contains("R610")
                                ) {
                            if (lastRespont.contains("390200")) {
                                handler.sendMessage(handler.obtainMessage(MenuActivity.VIEW_CC_PAYMENT_NO_CARDNUM_SUCCEED, ParamConst.SETTLEMENT_TYPE_EZLINK));
                            } else {
                                handler.sendEmptyMessage(MenuActivity.VIEW_CC_PAYMENT_FAILED);
                            }
                        } else {
                            handler.sendEmptyMessage(MenuActivity.VIEW_CC_PAYMENT_FAILED);
                        }
//
//                    App.getTopActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//
////                            UIHelp.showToast(App.instance, "sending Command");
//                            UIHelp.showToast(App.instance, "Response:" + lastRespont);
//                        }
//                    });
                    } else {
                        handler.sendEmptyMessage(MenuActivity.VIEW_CC_PAYMENT_FAILED);
                    }
                } catch (final Exception e) {
//                App.getTopActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            UIHelp.showToast(App.instance, "connectService:" + e.getMessage());
////                            UIHelp.showToast(App.instance, "Response:" + lastRespont);
//                        }
//                    });
                    LogUtil.e(TAG, ("connectService:" + e.getMessage()));
                    handler.sendEmptyMessage(MenuActivity.VIEW_CC_PAYMENT_FAILED);
                }
            }
        }
    }

    public boolean canCancel(){
        if(out != null){
            if(!TextUtils.isEmpty(lastRespont) && lastRespont.contains("INSERT")){
                return true;
            }
        }
        return false;
    }

    public void cancel(int timeOut){
        send(new String(new byte[]{0x18}), timeOut);
    }

    private class ConnectService implements Runnable {
        @Override
        public void run() {
            try {
                if(!BaseApplication.isOpenLog){
                    InetAddress inetAddress = InetAddress.getByName(CommonUtil.getLocalIpAddress());
                    socket = new Socket(HOST, PORT, inetAddress, LOCAL_PORT);
//                socket.setSoTimeout(10*1000);
                    socket.setKeepAlive(true);
                    socket.setReuseAddress(true);
                    socket.setTcpNoDelay(true);
                    out = socket.getOutputStream();
                }
                handler.sendEmptyMessage(MenuActivity.VIEW_CC_CONNECT_SUCCEED);
//                App.getTopActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                            UIHelp.showToast(App.instance, "ConnectService Command");
//                    }
//                });
            } catch (final Exception e) {
                LogUtil.e(TAG, ("connectService:" + e.getMessage()));
                handler.sendEmptyMessage(MenuActivity.VIEW_CC_CONNECT_FAILED);
            }
        }
    }


    private class DisConnectService implements Runnable {
        @Override
        public void run() {
            try {
                if (bff != null) {
                    bff.close();
                }
                if (out != null) {
                    out.close();
                }
                if (socket != null) {
                    socket.close();
                }
                paymentMsg = null;
                lastRespont = null;
            } catch (final Exception e) {
                LogUtil.e(TAG, ("disConnectService:" + e.getMessage()));
            }
        }
    }


}
