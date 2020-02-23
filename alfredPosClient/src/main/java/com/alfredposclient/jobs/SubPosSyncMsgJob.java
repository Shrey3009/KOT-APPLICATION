package com.alfredposclient.jobs;

import com.alfredbase.BaseActivity;
import com.alfredbase.ParamConst;
import com.alfredbase.javabean.SyncMsg;
import com.alfredbase.store.sql.SyncMsgSQL;
import com.alfredbase.utils.LogUtil;
import com.alfredposclient.global.App;
import com.alfredposclient.global.SubPosSyncCentre;
import com.alfredposclient.http.HttpAPI;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

public class SubPosSyncMsgJob extends Job {
	private String TAG = SubPosSyncMsgJob.class.getSimpleName();
    private long localId;
    private String msgUUID;
    private int msgType;
    private int msgId; //orderid, report data is 0; or others
    private Long created;
    private Long bizDate;
    private int revenueId;
    private int appOrderId;
    private int orderStatus;

    public SubPosSyncMsgJob(int revenueId, int msgType, String uuid,
                            int msgId, Long bizDate, Long created) {
        super(new Params(Priority.MID).requireNetwork().persist().groupBy("sync_"+bizDate.toString()));
        localId = -System.currentTimeMillis();
        this.revenueId = revenueId;
        this.msgId = msgId;
        this.msgType = msgType;
        this.msgUUID = uuid;
        this.bizDate = bizDate;
        this.created = created;
    }

    // 用作网络订单修改状态
    public SubPosSyncMsgJob(String uuid, int revenueId, int msgType, int appOrderId, int orderStatus, Long bizDate, Long created){
        super(new Params(Priority.MID).requireNetwork().persist().groupBy("net_order_"+bizDate.toString()));
        this.msgUUID = uuid;
        this.revenueId = revenueId;
        this.msgType = msgType;
        this.appOrderId = appOrderId;
        this.orderStatus = orderStatus;
        this.created = created;
    }
	
    @Override
    public void onAdded() {
        //job has been secured to disk, add item to database
    	SyncMsg content = SyncMsgSQL.getSyncMsgById(this.msgUUID, this.revenueId, this.created);
    	if (content != null) {
    		content.setStatus(ParamConst.SYNC_MSG_QUEUED);
    		SyncMsgSQL.add(content);
            LogUtil.d("TEST","onAdd");
    	}
    	LogUtil.d(TAG, "onAdded");
    }

    @Override
    public void onRun() throws Throwable {
    	//LogUtil.d(TAG, payload);
    	BaseActivity context = App.getTopActivity();
    	try {
            if(this.msgType != HttpAPI.NETWORK_ORDER_STATUS_UPDATE){
                SyncMsg content = SyncMsgSQL.getSyncMsgById(this.msgUUID, this.revenueId, this.created);
                if (content == null)
                    return;

                if (this.msgType == HttpAPI.ORDER_DATA) {
                    //sync order data
                    SubPosSyncCentre.getInstance().cloudSyncUploadOrderInfo(context, content);
                }
                if(this.msgType == HttpAPI.LOG_DATA){
                    SubPosSyncCentre.getInstance().cloudSyncUploadOrderInfoLog(context, content);

                }
                LogUtil.d(TAG, "Cloud MSG SYNC JOB Successful");
            }


    	}catch(Throwable e) {
    		LogUtil.d(TAG, "Cloud MSG SYNC:" + e.getMessage());
    		throw new RuntimeException("Cloud MSG SYNC failed"+e.getMessage());
    	}
    }

    @Override
    protected void onCancel() {
    	LogUtil.d(TAG, "onCancel");
    	SyncMsg content = SyncMsgSQL.getSyncMsgById(this.msgUUID, this.revenueId, this.created);
    	if (content != null) {
    		content.setStatus(ParamConst.SYNC_MSG_UN_SEND);
    		SyncMsgSQL.add(content);
    	}    	
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
       return true;
    }
}
