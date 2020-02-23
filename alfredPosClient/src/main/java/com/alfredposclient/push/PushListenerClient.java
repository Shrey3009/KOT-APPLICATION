package com.alfredposclient.push;

import android.content.Context;
import android.text.TextUtils;

import com.alfredbase.ParamConst;
import com.alfredbase.global.SharedPreferencesHelper;
import com.alfredbase.javabean.model.AlipayPushMsgDto;
import com.alfredbase.javabean.model.PushMessage;
import com.alfredbase.javabean.model.ThirdpartyPayPushMsgDto;
import com.alfredbase.javabean.temporaryforapp.AppOrder;
import com.alfredbase.javabean.temporaryforapp.TempOrder;
import com.alfredbase.store.Store;
import com.alfredbase.store.sql.SyncMsgSQL;
import com.alfredbase.store.sql.temporaryforapp.AppOrderSQL;
import com.alfredbase.store.sql.temporaryforapp.TempOrderSQL;
import com.alfredbase.utils.CommonUtil;
import com.alfredbase.utils.LogUtil;
import com.alfredbase.utils.TimeUtil;
import com.alfredposclient.activity.NetWorkOrderActivity;
import com.alfredposclient.global.App;
import com.alfredposclient.global.SyncCentre;
import com.alfredposclient.xmpp.XMPP;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PushListenerClient implements XMPP.PushListener {
    private String TAG = PushListenerClient.class.getSimpleName();
    Context context;

    public PushListenerClient(Context context) {
        super();
        this.context = context;
    }

    @Override
    public void onPushMessageReceived(PushMessage msg, boolean canCheck) {
        LogUtil.d(TAG, msg.toString());
        if (msg == null)
            return;
        if (TextUtils.isEmpty(msg.getMsg()))
            return;

        //发送邮件

        LogUtil.d("PushListenerClient-", msg.toString());
        if (PushMessage.REAL_TIME_REPORT.equals(msg.getMsg())) {
            if (CommonUtil.isNull(msg.getRestId())) {
                return;
            }
            if (msg.getRestId().intValue() == App.instance.getRevenueCenter().getRestaurantId()) {

                long timeStamp = System.currentTimeMillis();
                long afterTime = timeStamp - 1 * 60 * 60 * 1000;
                long frontTime = timeStamp + 1 * 60 * 60 * 1000;
                if(!TextUtils.isEmpty(msg.getSendTime().toString())) {
                    if (msg.getSendTime() <= frontTime && msg.getSendTime() >= afterTime) {
                        SendEmailThread thread = new SendEmailThread();
                        thread.start();
                    }
                }
            }
        }

        if (PushMessage.RE_SYNC_DATA_BY_BUSINESS_DATE.equals(msg.getMsg())) {
            if (CommonUtil.isNull(msg.getRevenueId()) || TextUtils.isEmpty(msg.getBusinessStr()) || App.instance.getRevenueCenter() == null) {
                return;
            }
        } else {

          int  trainType= SharedPreferencesHelper.getInt(context,SharedPreferencesHelper.TRAINING_MODE);
            if (msg.getRevenueId() == 0 || msg.getRevenueId() == App.instance.getRevenueCenter().getId().intValue()) {
                if (msg != null && PushMessage.PUSH_ORDER.equals(msg.getMsg())
                        && !TextUtils.isEmpty(msg.getContent())&&trainType!=1) {
                    try {
                        JSONObject jsonObject = new JSONObject(msg.getContent());
                        int type = 0;
                        if (jsonObject.has("refundType")) {
                            type = jsonObject.getInt("refundType");
                        }
                        int appOrderId = jsonObject.getInt("appOrderId");

                        if (type == -2016) {
                            AppOrderSQL.updateAppOrderStatusById(appOrderId, ParamConst.APP_ORDER_STATUS_REFUND);
                            App.instance.setAppOrderNum(AppOrderSQL.getNewAppOrderCountByTime(App.instance.getBusinessDate()), 2);
                            if (App.getTopActivity() instanceof NetWorkOrderActivity) {
                                App.getTopActivity().httpRequestAction(type, null);
                            }
                        } else {
                            AppOrder appOrder = AppOrderSQL.getAppOrderById(appOrderId);
                            if (appOrder == null) {
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("appOrderId", appOrderId);
                                SyncCentre.getInstance().getAppOrderById(context, map, null, canCheck);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //				String [] contentArray = msg.getContent().split(PushMessage.CONTENT_DELIMITER);
                    //				String restaurantKeyMd5 = contentArray[0];
                    ////				String QRCodeStr = contentArray[1];
                    //				String localRestaurantKeyMd5 = MD5Util.md5(CoreData.getInstance().getLoginResult().getRestaurantKey());
                    //				if(localRestaurantKeyMd5.equals(restaurantKeyMd5)){
                    //					Map<String, Object> parameters = new HashMap<String, Object>();
                    //					parameters.put("QRCodeStr",msg.getContent());
                    //					SyncCentre.getInstance().getOrderFromApp(App.instance.getTopActivity(), parameters);
                    //				}
                } else if (msg != null && PushMessage.ALIPAY_RESULT.equals(msg.getMsg())) {
                    if (!TextUtils.isEmpty(msg.getContent())) {
                        Gson gson = new Gson();
                        AlipayPushMsgDto alipayPushMsgDto = gson.fromJson(msg.getContent(), AlipayPushMsgDto.class);
                        App.instance.addAlipayPushMessage(alipayPushMsgDto);
                        LogUtil.d(TAG, alipayPushMsgDto.toString());
                    }
                } else if (msg != null && PushMessage.THIRDPARTYPAY_RESULT.equals(msg.getMsg())) {
                    if (!TextUtils.isEmpty(msg.getContent())) {
                        Gson gson = new Gson();
                        ThirdpartyPayPushMsgDto thirdpartyPayPushMsgDto = gson.fromJson(msg.getContent(), ThirdpartyPayPushMsgDto.class);
                        TempOrder tempOrder = TempOrderSQL.getTempOrderByAppOrderId(Integer.parseInt(thirdpartyPayPushMsgDto.getSysOrderId()));
                        if (tempOrder == null)
                            return;
                        tempOrder.setPaied(ParamConst.TEMPORDER_PAIED);
                        TempOrderSQL.updateTempOrder(tempOrder);
                        LogUtil.d(TAG, thirdpartyPayPushMsgDto.toString());
                    }
                } else {
                    /**
                     * Map<String, Integer> map = new HashMap<String, Integer>();
                     map.put(PushMessage.HAPPY_HOURS, 1);
                     map.put(PushMessage.PRINTER, 1);
                     map.put(PushMessage.ITEM, 1);
                     map.put(PushMessage.MODIFIER, 1);
                     map.put(PushMessage.USER, 1);
                     //									map.put(PushMessage.PLACE_TABLE, 1);
                     map.put(PushMessage.TAX, 1);
                     */
                    if (PushMessage.HAPPY_HOURS.equals(msg.getMsg())
                            || PushMessage.ITEM.equals(msg.getMsg())
                            || PushMessage.MODIFIER.equals(msg.getMsg())
                            || PushMessage.USER.equals(msg.getMsg())
                            //						|| PushMessage.PLACE_TABLE.equals(msg.getMsg())
                            || PushMessage.TAX.equals(msg.getMsg())
                            || PushMessage.PAYMENT_METHOD.equals(msg.getMsg())
                            ||PushMessage.STOCK.equals(msg.getMsg())
                            ||PushMessage.PROMOTION.equals(msg.getMsg())
                            ) {
                        saveUpdateInfo(msg);
                    }
                    if (PushMessage.RESTAURANT.equals(msg.getMsg())
                            || PushMessage.PRINTER.equals(msg.getMsg())
                            || PushMessage.REST_CONFIG.equals(msg.getMsg())) {
                        msg.setMsg(PushMessage.PRINTER);
                        saveUpdateInfo(msg);
                    }

                }
            }
        }
    }

    private void saveUpdateInfo(PushMessage msg) {
        Map<String, Integer> pushMsgMap = App.instance.getPushMsgMap();
        if (pushMsgMap.containsKey(msg.getMsg())) {
            if (pushMsgMap.get(pushMsgMap) != null) {
                pushMsgMap.put(msg.getMsg(), pushMsgMap.get(pushMsgMap) + 1);
            } else {
                pushMsgMap.put(msg.getMsg(), 1);
            }

        } else {
            pushMsgMap.put(msg.getMsg(), 1);
        }
        App.instance.setPushMsgMap(pushMsgMap);
        Store.saveObject(context, Store.PUSH_MESSAGE, App.instance.getPushMsgMap());
    }

}
