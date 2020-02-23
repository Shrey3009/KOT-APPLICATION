package com.alfredposclient.jobs;

import android.content.Context;

import com.alfredbase.ParamConst;
import com.alfredbase.global.SharedPreferencesHelper;
import com.alfredbase.javabean.OrderBill;
import com.alfredbase.javabean.RevenueCenter;
import com.alfredbase.javabean.SyncMsg;
import com.alfredbase.javabean.model.SessionStatus;
import com.alfredbase.store.sql.SyncMsgSQL;
import com.alfredbase.store.sql.UploadSQL;
import com.alfredbase.utils.IntegerUtils;
import com.alfredbase.utils.LogUtil;
import com.alfredposclient.global.App;
import com.alfredposclient.global.ReportObjectFactory;
import com.alfredposclient.http.HttpAPI;
import com.google.gson.Gson;
import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CloudSyncJobManager {
	
	public static final int OPEN_SESSION = 1;
	public static final int CLOSE_SESSION = 2;
	public static final int OPEN_RESTAURANT = 3;
	public static final int CLOSE_RESTAURANT = 4;
	private JobManager syncJobManager;
	private Context context;
    private ScheduledExecutorService scheduler = null;
    int trainType;  //-1. 第一次打开   0.正常模式  1. 培训模式
    public CloudSyncJobManager(Context mContext) {
		super();
		this.context = mContext;
		scheduler = Executors.newSingleThreadScheduledExecutor();
		configureJobManager();
		setupSyncScheduler();
        trainType= SharedPreferencesHelper.getInt(context,SharedPreferencesHelper.TRAINING_MODE);

	}

    private void configureJobManager() {
        Configuration cloudConfiguration = new Configuration.Builder(context)
        .customLogger(new AlfredJobLogger("CLOUD_JOBS"))
        .id("sync_jobs")
        .minConsumerCount(1)     //always keep at least one consumer alive
        .maxConsumerCount(3)     //up to 3 consumers at a time
        .loadFactor(3)           //3 jobs per consumer
        .consumerKeepAlive(120)   //wait 2 minute
        .build();
        
        this.syncJobManager = new JobManager(this.context, cloudConfiguration);
        
    }
    
    private void setupSyncScheduler() {
		//sync scheduler
        if(trainType!=1){
    	scheduler.scheduleAtFixedRate
	      (new Runnable() {
	         public void run() {
	         	RevenueCenter rvc = App.instance.getRevenueCenter();
	        	if (rvc != null) {
	    	    	ArrayList<SyncMsg> messages = SyncMsgSQL.getTenUnsentSyncMsg(rvc.getId(), HttpAPI.REPORT_DATA);
	    	    	for (SyncMsg msg:messages) {
	    	    		SyncMsgJob syncOrderJob = new SyncMsgJob(msg.getRevenueId(),
	    	    													msg.getMsgType(), msg.getId(),
	    	    															msg.getOrderId(), 
	    	    															msg.getBusinessDate(), 
	    	    															msg.getCreateTime());
	    	    		syncJobManager.addJob(syncOrderJob);  
	    	    	}
					ArrayList<SyncMsg> orderMessages = SyncMsgSQL.getTenUnsentSyncMsg(rvc.getId(), HttpAPI.ORDER_DATA);
					for (SyncMsg msg:orderMessages) {
						SyncMsgJob syncOrderJob = new SyncMsgJob(msg.getRevenueId(),
								msg.getMsgType(), msg.getId(),
								msg.getOrderId(),
								msg.getBusinessDate(),
								msg.getCreateTime());
						syncJobManager.addJob(syncOrderJob);
					}
	        	}	        	 
	         }
	      }, 0, 20, TimeUnit.SECONDS);
        }
    }
    private String getDataUUID(int rvcId){
    	return String.valueOf(rvcId) +"-"+UUID.randomUUID().toString();
    }
    
    //v1.0.4 migration purpose. BE CAFEFUL TO USE IT
    public void syncAllUnsentMsg_migration() {
        if(trainType!=1) {
            RevenueCenter rvc = App.instance.getRevenueCenter();
            if (rvc != null) {
                ArrayList<SyncMsg> messages = SyncMsgSQL.getUnsentSyncMsg(rvc.getId());
                for (SyncMsg msg : messages) {

                    SyncMsgJob syncOrderJob = new SyncMsgJob(msg.getRevenueId(),
                            HttpAPI.ORDER_DATA, msg.getId(),
                            msg.getOrderId(),
                            msg.getBusinessDate(),
                            msg.getCreateTime());
                    this.syncJobManager.addJob(syncOrderJob);
                    msg.setStatus(ParamConst.SYNC_MSG_UN_SEND);
                    SyncMsgSQL.add(msg);
                    LogUtil.d("TEST", "add");
                }
            }
        }
    }
    
    //Sync Order Info
    public void syncOrderInfo(Integer orderId, int revenueCenterId, Long bizDate) {
		SyncMsgJob syncOrderJob = null;
        if(trainType!=1){
    	if (SyncMsgSQL.getSyncMsgByOrderIdBizDate(orderId, bizDate) == null) {
            Map<String, Object> orderInfo = UploadSQL.getOrderInfo(orderId);
            if (orderInfo != null) {
                List<OrderBill> orderBills = (List<OrderBill>) orderInfo.get("orderBills");
                int billNo = 0;
                if (orderBills != null && orderBills.size() > 0) {
                    for (OrderBill orderBill : orderBills) {
                        if (orderBill.getBillNo() != null && orderBill.getBillNo().intValue() > billNo) {
                            billNo = orderBill.getBillNo().intValue();
                        }
                    }
                }
                if (orderInfo != null) {
                    Gson gson = new Gson();
                    SyncMsg syncMsg = new SyncMsg();
                    String uuid = getDataUUID(revenueCenterId);
                    syncMsg.setId(uuid);
                    syncMsg.setOrderId(orderId);
                    syncMsg.setMsgType(HttpAPI.ORDER_DATA);
                    syncMsg.setData(gson.toJson(orderInfo));
                    syncMsg.setCreateTime(System.currentTimeMillis());
                    syncMsg.setStatus(ParamConst.SYNC_MSG_UN_SEND);
                    syncMsg.setRevenueId(revenueCenterId);
                    syncMsg.setBusinessDate(bizDate);
                    syncMsg.setBillNo(billNo);
                    SyncMsgSQL.add(syncMsg);
                    syncOrderJob = new SyncMsgJob(revenueCenterId, HttpAPI.ORDER_DATA, uuid,
                            orderId, bizDate, syncMsg.getCreateTime());
                    this.syncJobManager.addJobInBackground(syncOrderJob);
                }
            }
        }
    	}
    }

    public void updateRemainingStock(int orderId){
        if(trainType!=1) {
            String uuid = UUID.randomUUID().toString();
            SyncMsgJob syncOrderJob = new SyncMsgJob(uuid, orderId);
            this.syncJobManager.addJobInBackground(syncOrderJob);
        }
	}
      //修改单个菜数量
	public void updateRemainingStockNum(int itemTemplateId){
        if(trainType!=1) {
            String uuid = UUID.randomUUID().toString();
            SyncMsgJob syncOrderJob = new SyncMsgJob(uuid, itemTemplateId);
            this.syncJobManager.addJobInBackground(syncOrderJob);
        }
	}

	//Sync Sub Pos Order Info
	public void syncSubPosOrderInfo(Integer orderId, int revenueCenterId, Long bizDate) {
		SyncMsgJob syncOrderJob = null;

        if(trainType!=1){
		if (SyncMsgSQL.getSubPosSyncMsgByOrderIdBizDate(orderId, bizDate) == null) {
            Map<String, Object> orderInfo = UploadSQL.getSubPosOrderInfo(orderId);
            if (orderInfo != null) {
                List<OrderBill> orderBills = (List<OrderBill>) orderInfo.get("orderBills");
                int billNo = 0;
                if (orderBills != null && orderBills.size() > 0) {
                    for (OrderBill orderBill : orderBills) {
                        if (orderBill.getBillNo() != null && orderBill.getBillNo().intValue() > billNo) {
                            billNo = orderBill.getBillNo().intValue();
                        }
                    }
                }
                if (orderInfo != null) {
                    Gson gson = new Gson();
                    SyncMsg syncMsg = new SyncMsg();
                    String uuid = getDataUUID(revenueCenterId);
                    syncMsg.setId(uuid);
                    syncMsg.setOrderId(orderId);
                    syncMsg.setMsgType(HttpAPI.SUBPOS_ORDER_DATA);
                    syncMsg.setData(gson.toJson(orderInfo));
                    syncMsg.setCreateTime(System.currentTimeMillis());
                    syncMsg.setStatus(ParamConst.SYNC_MSG_UN_SEND);
                    syncMsg.setRevenueId(revenueCenterId);
                    syncMsg.setBusinessDate(bizDate);
                    syncMsg.setBillNo(billNo);
                    SyncMsgSQL.add(syncMsg);
                    syncOrderJob = new SyncMsgJob(revenueCenterId, HttpAPI.SUBPOS_ORDER_DATA, uuid,
                            orderId, bizDate, syncMsg.getCreateTime());
                    this.syncJobManager.addJobInBackground(syncOrderJob);
                }
            }
        }
		}
	}

 // Sync Order Info Log
    public void syncOrderInfoForLog(Integer orderId, int revenueCenterId, Long bizDate, int currCount) {
		SyncMsgJob syncOrderJob = null;
        if(trainType!=1){
    	if (SyncMsgSQL.getSyncMsgByOrderIdBizDateCurrCount(orderId, bizDate, currCount) == null) {
            Map<String, Object> orderInfo = UploadSQL.getOrderInfo(orderId);
            if (orderInfo != null) {
                List<OrderBill> orderBills = (List<OrderBill>) orderInfo.get("orderBills");
                int billNo = 0;
                if (orderBills != null && orderBills.size() > 0) {
                    for (OrderBill orderBill : orderBills) {
                        if (orderBill.getBillNo() != null && orderBill.getBillNo().intValue() > billNo) {
                            billNo = orderBill.getBillNo().intValue();
                        }
                    }
                }
                if (orderInfo != null) {
                    Gson gson = new Gson();
                    SyncMsg syncMsg = new SyncMsg();
                    String uuid = getDataUUID(revenueCenterId);
                    syncMsg.setId(uuid);
                    syncMsg.setOrderId(orderId);
                    syncMsg.setMsgType(HttpAPI.LOG_DATA);
                    syncMsg.setData(gson.toJson(orderInfo));
                    syncMsg.setCreateTime(System.currentTimeMillis());
                    syncMsg.setStatus(ParamConst.SYNC_MSG_UN_SEND);
                    syncMsg.setRevenueId(revenueCenterId);
                    syncMsg.setBusinessDate(bizDate);
                    syncMsg.setCurrCount(currCount);
                    syncMsg.setBillNo(billNo);
                    SyncMsgSQL.add(syncMsg);
                    syncOrderJob = new SyncMsgJob(revenueCenterId, HttpAPI.LOG_DATA, uuid,
                            orderId, bizDate, syncMsg.getCreateTime());
                    this.syncJobManager.addJobInBackground(syncOrderJob);
                }
            }
        }
    	}
    }
    
    //Sync ZReport
    public void syncZReport(int revenueCenterId, Long bizDate) {
		SyncMsgJob syncZReportJob = null;
        if(trainType!=1) {
            Map<String, Object> reportInfo = ReportObjectFactory.getInstance()
                    .getAllReportInfo(bizDate);
            Gson gson = new Gson();
            SyncMsg syncMsg = new SyncMsg();
            String uuid = getDataUUID(revenueCenterId);
            syncMsg.setId(uuid);
            syncMsg.setOrderId(0);
            syncMsg.setMsgType(HttpAPI.REPORT_DATA);
            syncMsg.setData(gson.toJson(reportInfo));
            syncMsg.setCreateTime(System.currentTimeMillis());
            syncMsg.setStatus(ParamConst.SYNC_MSG_UN_SEND);
            syncMsg.setRevenueId(revenueCenterId);
            syncMsg.setBusinessDate(bizDate);
            SyncMsgSQL.add(syncMsg);
            syncZReportJob = new SyncMsgJob(revenueCenterId, HttpAPI.REPORT_DATA, uuid,
                    0, bizDate, syncMsg.getCreateTime());
            this.syncJobManager.addJobInBackground(syncZReportJob);
        }
    }

    //Sync XReport
    public void syncXReport(Map<String, Object> reportInfo, int revenueCenterId, 
    							Long bizDate, SessionStatus sessionStatus, int reportNo) {
        if(trainType!=1) {
            SyncMsgJob syncXReportJob = null;
            Gson gson = new Gson();
            SyncMsg syncMsg = new SyncMsg();
            String uuid = getDataUUID(revenueCenterId);
            syncMsg.setId(uuid);
            syncMsg.setOrderId(0);
            syncMsg.setMsgType(HttpAPI.REPORT_DATA);
            syncMsg.setData(gson.toJson(reportInfo));
            syncMsg.setCreateTime(System.currentTimeMillis());
            syncMsg.setStatus(ParamConst.SYNC_MSG_UN_SEND);
            syncMsg.setRevenueId(revenueCenterId);
            syncMsg.setReportNo(reportNo);
            syncMsg.setBusinessDate(bizDate);
            SyncMsgSQL.add(syncMsg);
            syncXReportJob = new SyncMsgJob(revenueCenterId, HttpAPI.REPORT_DATA, uuid,
                    0, bizDate, syncMsg.getCreateTime());
            this.syncJobManager.addJobInBackground(syncXReportJob);
        }
    }
    
    // session and restaurant open close log
    public void syncOpenOrCloseSessionAndRestaurant(int revenueCenterId, 
			Long bizDate, SessionStatus sessionStatus, int type){
        if(trainType!=1) {
            SyncMsgJob syncXReportJob = null;
            Gson gson = new Gson();
            SyncMsg syncMsg = new SyncMsg();
            String uuid = getDataUUID(revenueCenterId);
            syncMsg.setId(uuid);
            syncMsg.setOrderId(0);
            syncMsg.setMsgType(HttpAPI.OPEN_CLOSE_SESSION_RESTAURANT);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("type", type);
            map.put("time", System.currentTimeMillis());
            if (sessionStatus != null) {
                map.put("sessionType", sessionStatus.getSession_status());
            }
            syncMsg.setData(gson.toJson(map));
            syncMsg.setCreateTime(System.currentTimeMillis());
            syncMsg.setStatus(ParamConst.SYNC_MSG_UN_SEND);
            syncMsg.setRevenueId(revenueCenterId);
            syncMsg.setBusinessDate(bizDate);
            SyncMsgSQL.add(syncMsg);
            syncXReportJob = new SyncMsgJob(revenueCenterId, HttpAPI.OPEN_CLOSE_SESSION_RESTAURANT, uuid,
                    0, bizDate, syncMsg.getCreateTime());
            this.syncJobManager.addJobInBackground(syncXReportJob);
        }
    }

	public void checkAppOrderStatus(int revenueCenterId, int appOrderId, int orderStatus, String reason, Long bizDate, Integer orderNum) {
        if(trainType!=1) {
            if (SyncMsgSQL.getSyncMsgByAppOrderId(appOrderId, orderStatus) == null) {
                SyncMsg syncMsg = new SyncMsg();
                String uuid = getDataUUID(revenueCenterId);
                syncMsg.setId(uuid);
                syncMsg.setOrderId(0);
                syncMsg.setMsgType(HttpAPI.NETWORK_ORDER_STATUS_UPDATE);
                syncMsg.setData(reason);
                syncMsg.setCreateTime(System.currentTimeMillis());
                syncMsg.setStatus(ParamConst.SYNC_MSG_UN_SEND);
                syncMsg.setRevenueId(revenueCenterId);
                syncMsg.setCreateTime(System.currentTimeMillis());
                syncMsg.setAppOrderId(appOrderId);
                syncMsg.setOrderStatus(orderStatus);
                syncMsg.setOrderNum(IntegerUtils.fromat(App.instance.getRevenueCenter().getIndexId(), orderNum));
                syncMsg.setBusinessDate(bizDate);
                SyncMsgSQL.add(syncMsg);
                SyncMsgJob syncXReportJob = new SyncMsgJob(uuid, revenueCenterId, HttpAPI.NETWORK_ORDER_STATUS_UPDATE, appOrderId, orderStatus, bizDate, syncMsg.getCreateTime());
                this.syncJobManager.addJobInBackground(syncXReportJob);
            }
        }
	}
    
    public void clear(){
    	this.syncJobManager.clear();
    }
    
    public void stop(){
        this.syncJobManager.stop();    	
    }
}
