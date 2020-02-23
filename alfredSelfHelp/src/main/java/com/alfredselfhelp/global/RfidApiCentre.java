package com.alfredselfhelp.global;

import com.alfredbase.utils.LogUtil;
import com.nordicid.nurapi.NurApi;
import com.nordicid.nurapi.NurApiListener;
import com.nordicid.nurapi.NurApiUiThreadRunner;
import com.nordicid.nurapi.NurApiUsbAutoConnect;
import com.nordicid.nurapi.NurEventClientInfo;
import com.nordicid.nurapi.NurEventDeviceInfo;
import com.nordicid.nurapi.NurEventFrequencyHop;
import com.nordicid.nurapi.NurEventIOChange;
import com.nordicid.nurapi.NurEventInventory;
import com.nordicid.nurapi.NurEventNxpAlarm;
import com.nordicid.nurapi.NurEventProgrammingProgress;
import com.nordicid.nurapi.NurEventTraceTag;
import com.nordicid.nurapi.NurEventTriggeredRead;
import com.nordicid.nurapi.NurTag;
import com.nordicid.nurapi.NurTagStorage;

import java.util.HashMap;

public class RfidApiCentre {
    private static final String TAG = RfidApiCentre.class.getSimpleName();
    // RFID API
    private NurApi nurApi;
    private NurTagStorage nurTagStorage;
    private boolean mInventoryIsRunning;
    private static RfidApiCentre instance;
    private RfidCallBack callBack;
    private NurApiUsbAutoConnect mUsbAC;
    private boolean canConnectThenStart = false;
    private int mTagsCounter;
    private long mLastInventoryTime;
    private int mTpsUpdateInterval = 100;
    // Tags Per Second
    private double mLastTPS = 0;
    // Max performance.
    private double mPeakTPS = 0;
    private RfidApiCentre(){
    }
    public static RfidApiCentre getInstance() {
        if (instance == null)
            instance = new RfidApiCentre();
        return instance;
    }

//    public void setNurTagStorage(NurTagStorage nurTagStorage) {
//        this.nurTagStorage = nurTagStorage;
//    }

    public boolean isConnected(){
        if(nurApi != null && nurApi.isConnected()){
            return true;
        }else{
            return false;
        }
    }

    public void setCallBack(RfidCallBack callBack) {
        this.callBack = callBack;
    }

    public void initApi(NurApiUiThreadRunner nurApiUiThreadRunner){
        nurApi = new NurApi();
        nurTagStorage = new NurTagStorage();
        nurApi.setListener(new NurApiListener() {
            @Override
            public void logEvent(int i, String s) {

            }

            @Override
            public void connectedEvent() {
                LogUtil.d(TAG, "NurApi connected");
                enableItems(true);
            }

            @Override
            public void disconnectedEvent() {
                LogUtil.d(TAG, "NurApi disconnected");
                enableItems(false);
            }

            @Override
            public void bootEvent(String s) {

            }

            @Override
            public void inventoryStreamEvent(NurEventInventory nurEventInventory) {
//                mLastInventoryTime = System.currentTimeMillis();
//                CalcTPS(nurEventInventory.tagsAdded); // Add tag count to tags per second counter
//                mInventoryRounds += nurEventInventory.roundsDone;
//                if (nurEventInventory.tagsAdded > 0) {
//                    inventory();
//                }
//                if (nurEventInventory.stopped && mInventoryIsRunning) {
//                    try {
//                        nurApi.startInventoryStream();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
            }

            @Override
            public void IOChangeEvent(NurEventIOChange nurEventIOChange) {

            }

            @Override
            public void traceTagEvent(NurEventTraceTag nurEventTraceTag) {

            }

            @Override
            public void triggeredReadEvent(NurEventTriggeredRead nurEventTriggeredRead) {

            }

            @Override
            public void frequencyHopEvent(NurEventFrequencyHop nurEventFrequencyHop) {

            }

            @Override
            public void debugMessageEvent(String s) {

            }

            @Override
            public void inventoryExtendedStreamEvent(NurEventInventory nurEventInventory) {

            }

            @Override
            public void programmingProgressEvent(NurEventProgrammingProgress nurEventProgrammingProgress) {

            }

            @Override
            public void deviceSearchEvent(NurEventDeviceInfo nurEventDeviceInfo) {

            }

            @Override
            public void clientConnectedEvent(NurEventClientInfo nurEventClientInfo) {

            }

            @Override
            public void clientDisconnectedEvent(NurEventClientInfo nurEventClientInfo) {

            }

            @Override
            public void nxpEasAlarmEvent(NurEventNxpAlarm nurEventNxpAlarm) {

            }
        });

        nurApi.setUiThreadRunner(nurApiUiThreadRunner);
        mUsbAC = new NurApiUsbAutoConnect(App.instance, nurApi);
        mUsbAC.setEnabled(true);
    }


    private void enableItems(boolean isConnected){
        if(isConnected){
//            UIHelp.showToast(App.instance, "RFID scanner is Connected");
            try {
                nurApi.setSetupOpFlags(NurApi.OPFLAGS_INVSTREAM_ZEROS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
//            UIHelp.showToast(App.instance, "RFID scanner is Disconnected");
        }
//        if(canConnectThenStart && isConnected){
//            startRFIDScan();
//        }

    }

    public void onResume() {
        if(mUsbAC != null) {
            mUsbAC.onResume(); // Tell Android USB Autoconnection object to do resume things
        }
    }

    public void onDestroy() {
        if (mUsbAC != null) {
            mUsbAC.onDestroy(); // Tell Android USB Autoconnection object to do onDestroy things
        }
        if (nurApi != null) {
            if (nurApi.isConnected()) {
                try {
                    nurApi.stopAllContinuousCommands(); // We don't want to let any streams open when closing activity
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            nurApi.dispose();
        }
        canConnectThenStart = false;
    }

//    public void startRFIDScan(){
//        canConnectThenStart = true;
//        if(nurApi == null){
////            UIHelp.showToast(App.instance, "Please restart app");
//            return;
//        }
//        if(!nurApi.isConnected()){
////            UIHelp.showToast(App.instance, "Please check RFID scanner is Disconnected ");
//            return;
//        }
//        if(!nurApi.isInventoryStreamRunning()){
//            try {
//                nurTagStorage.clear();
//                nurApi.startInventoryStream();
//                mInventoryIsRunning = true;
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

    public void stopRFIDScan(){
        canConnectThenStart = false;
        if(nurApi != null && nurApi.isConnected() && nurApi.isInventoryStreamRunning()){
            try {
                nurApi.stopInventoryStream();
                mInventoryIsRunning = false;
                nurTagStorage.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public NurTagStorage getNurTagStorage() {
        return nurTagStorage;
    }

    private void inventory(){
        synchronized (nurApi.getStorage()){
            boolean tagAdded = false;
            NurTagStorage tagStorage = nurApi.getStorage();
            LogUtil.e("TAG", "before add =======" + tagStorage.size());
            for (int n = 0; n < tagStorage.size(); n++){
                NurTag tag = tagStorage.get(n);
                if(nurTagStorage.addTag(tag)) {
                    HashMap<String, String> temp = new HashMap<>();
                    temp.put("epc", tag.getEpcString());
                    temp.put("rssi", "" + tag.getRssi());
                    temp.put("timestamp", "" + tag.getTimestamp());
                    temp.put("freq", "" + tag.getFreq() + " kHz (Ch: " + tag.getChannel() + ")");
                    temp.put("found", "1");
                    temp.put("foundpercent", "100");
                    tag.setUserdata(temp);
                    tagAdded = true;
                }else{
                    tag = nurTagStorage.getTag(tag.getEpc());
                    @SuppressWarnings("unchecked")
                    HashMap<String, String> temp = (HashMap<String, String>) tag.getUserdata(); // Get tag instance from the tag storage
                    temp.put("rssi", "" + tag.getRssi());
                    temp.put("timestamp", "" + tag.getTimestamp());
                    temp.put("freq", "" + tag.getFreq() + " kHz (Ch: " + tag.getChannel() + ")");
                    temp.put("found", "" + tag.getUpdateCount());
                    temp.put("foundpercent", "100" );
                }
            }
            nurApi.clearTagStorage();
            if(callBack != null){
                callBack.inventoryStreamEvent();
            }
        }
    }

//    private void CalcTPS(int ft) {
//        mTagsCounter += ft;
//        long elapsed = System.currentTimeMillis() - mLastInventoryTime;
//
//        if (elapsed >= mTpsUpdateInterval) {
//            mLastTPS = (double) mTagsCounter / (double) elapsed * 1000.0;
//            if (mLastTPS > mPeakTPS) {
//                mPeakTPS = mLastTPS;
//            }
////            mInventoryTPSTextView.setText("" + (int)mLastTPS + ", peak: " + (int)mPeakTPS);
//            if((int)mLastTPS == 0 && (int)mPeakTPS == 0){
//
//            }
//            mLastInventoryTime = System.currentTimeMillis();
//            mTagsCounter = 0;
//        }
//    }


    public NurApi getNurApi() {
        return nurApi;
    }

    public interface RfidCallBack{
        void inventoryStreamEvent();
    }

}
