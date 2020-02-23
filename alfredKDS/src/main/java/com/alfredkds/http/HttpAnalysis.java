package com.alfredkds.http;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.alfredbase.KDSLog;
import com.alfredbase.ParamConst;
import com.alfredbase.global.CoreData;
import com.alfredbase.http.ResultCode;
import com.alfredbase.javabean.KDSHistory;
import com.alfredbase.javabean.KotItemDetail;
import com.alfredbase.javabean.KotItemModifier;
import com.alfredbase.javabean.KotSummary;
import com.alfredbase.javabean.KotSummaryLog;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.OrderModifier;
import com.alfredbase.javabean.Printer;
import com.alfredbase.javabean.PrinterGroup;
import com.alfredbase.javabean.User;
import com.alfredbase.javabean.model.KDSDevice;
import com.alfredbase.javabean.model.MainPosInfo;
import com.alfredbase.javabean.model.SessionStatus;
import com.alfredbase.store.Store;
import com.alfredbase.store.sql.KotItemDetailSQL;
import com.alfredbase.store.sql.KotItemModifierSQL;
import com.alfredbase.store.sql.KotSummarySQL;
import com.alfredbase.store.sql.OrderModifierSQL;
import com.alfredbase.store.sql.PrinterGroupSQL;
import com.alfredbase.store.sql.PrinterSQL;
import com.alfredbase.utils.KDSLogUtil;
import com.alfredkds.global.App;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/* Parsing data in HTTP response */
public class HttpAnalysis {

    public static String login(int statusCode, Header[] headers,
                               byte[] responseBody, Context context) {
        try {
            Gson gson = new Gson();
            JSONObject object = new JSONObject(new String(responseBody));
            MainPosInfo mainPosInfo = gson.fromJson(
                    object.optString("mainPosInfo"), MainPosInfo.class);
            String userKey = object.optString("userKey");
            SessionStatus receiveSessionStatus = gson.fromJson(
                    object.optString("session"), SessionStatus.class);
            Long receiveBusinessDate = object.optLong("businessDate");
            int trainType = object.optInt("trainType");

            // SessionStatus oldSessionStatus = (SessionStatus)
            // Store.getObject(context, Store.SESSION_STATUS);
            // Long oldBusinessDate = (Long) Store.getObject(context,
            // Store.BUSINESS_DATE);
            // if (oldSessionStatus == null && oldBusinessDate == null) {
            // Store.saveObject(context, Store.SESSION_STATUS,
            // receiveSessionStatus);
            // Store.saveObject(context, Store.BUSINESS_DATE,
            // receiveBusinessDate);
            // }else {
            // if (oldSessionStatus.getSession_status() !=
            // receiveSessionStatus.getSession_status()
            // && oldBusinessDate != receiveBusinessDate) {
            List<KotSummary> kotSummaryList = gson.fromJson(
                    object.optString("kotSummaryList"),
                    new TypeToken<ArrayList<KotSummary>>() {
                    }.getType());
            List<KotItemDetail> kotItemDetails = gson.fromJson(
                    object.optString("kotItemDetails"),
                    new TypeToken<ArrayList<KotItemDetail>>() {
                    }.getType());
            List<KotItemModifier> kotItemModifiers = gson.fromJson(
                    object.optString("kotItemModifiers"),
                    new TypeToken<ArrayList<KotItemModifier>>() {
                    }.getType());

            if (kotSummaryList != null && kotItemDetails != null
                    && kotItemModifiers != null) {

                //region delete kot by rvc
                List<KotSummary> kotSummariesLocal = KotSummarySQL.getUndoneKotSummary(mainPosInfo.getRevenueId(), receiveBusinessDate);

                if (kotSummariesLocal.size() > 0) {
                    for (KotSummary kotSummary : kotSummariesLocal) {
                        List<KotItemDetail> kotItemDetailList = KotItemDetailSQL.getKotItemDetailBySummaryIdRvcId(kotSummary.getId(), kotSummary.getRevenueCenterId());

                        for (KotItemDetail kotItemDetail : kotItemDetailList) {
                            List<KotItemModifier> kotItemModifierList = KotItemModifierSQL.getKotItemModifiersByKotItemDetail(kotItemDetail);
                            for (KotItemModifier kotItemModifier : kotItemModifierList) {
                                KotItemModifierSQL.deleteKotItemModifier(kotItemModifier);
                            }
                        }

                        KotItemDetailSQL.deleteAllKotItemDetailByKotSummary(kotSummary);
                    }
                    KotSummarySQL.deleteAllKotSummaryByRvcId(mainPosInfo.getRevenueId());
                }
                //endregion

//                KotSummarySQL.deleteAllKotSummary();
//                KotItemDetailSQL.deleteAllKotItemDetail();
//                KotItemModifierSQL.deleteAllKotItemModifier();

                for (int i = 0; i < kotSummaryList.size(); i++) {


                    boolean flag = false;
                    KotSummary kotSummary = kotSummaryList.get(i);

                    for (int j = 0; j < kotItemDetails.size(); j++) {
                        KotItemDetail kotItemDetail = kotItemDetails.get(j);
                        //

                        //对未完成的item和完成了一部分但status未done的item做处理
                        if (kotSummary.getId().intValue() == kotItemDetail.getKotSummaryId().intValue()
                                && kotItemDetail.getUnFinishQty().intValue() > 0) {
                            flag = true;
                        }
//						if (kotItemDetail.getKotStatus() < ParamConst.KOT_STATUS_DONE
//								 && kotSummary.getId().intValue() == kotItemDetail
//										.getKotSummaryId().intValue()) {
//							flag = true;
//						}else if (kotItemDetail.getKotStatus() >= ParamConst.KOT_STATUS_DONE
//								&& kotSummary.getId().intValue() == kotItemDetail.getKotSummaryId().intValue()
//							&& kotItemDetail.getUnFinishQty().intValue() > 0
//								 ) {
//							kotItemDetail.setFinishQty(kotItemDetail.getItemNum()-kotItemDetail.getUnFinishQty());
//							kotItemDetail.setKotStatus(ParamConst.KOT_STATUS_UNDONE);
//							flag = true;
//						}
                    }
                    if (!flag) {
                        kotSummary.setStatus(ParamConst.KOTS_STATUS_DONE);
                    }
                    KotSummarySQL.update(kotSummary);
                }

                // KotSummarySQL.addKotSummaryList(kotSummaryList);
                KotItemDetailSQL.addKotItemDetailList(kotItemDetails);
                KotItemModifierSQL.addKotItemModifierList(kotItemModifiers);


            }
            Store.saveObject(context, Store.SESSION_STATUS,
                    receiveSessionStatus);
            Store.putLong(context, Store.BUSINESS_DATE, receiveBusinessDate);
            Store.putInt(context, Store.TRAIN_TYPE, trainType);
            // }
            // }
            App.instance.addMainPosInfo(mainPosInfo);
            CoreData.getInstance().setUserKey(mainPosInfo.getRevenueId(), userKey);
            // check business data and session
            // Long bizDate = object.optLong("businessDate");
            // SessionStatus session =
            // gson.fromJson(object.optString("session"), SessionStatus.class);
            return userKey;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void saveConnectedKDS(int statusCode,
                                        Header[] headers, byte[] responseBody) {
        try {
            JSONObject object = new JSONObject(new String(responseBody));
            Gson gson = new Gson();
            List<KDSDevice> kdsDeviceList = gson.fromJson(object.getString("kdsList"),
                    new TypeToken<List<KDSDevice>>() {
                    }.getType());

            saveConnectedKDS(kdsDeviceList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void saveConnectedKDS(List<KDSDevice> kdsDeviceList) {
        String kdsLogs = Store.getString(App.instance, Store.KDS_LOGS);
        String kdsLogsStr = KDSLogUtil.putKdsLog(kdsLogs, kdsDeviceList);
        Store.putString(App.instance, Store.KDS_LOGS, kdsLogsStr);
    }

    public static List<Printer> getPrinterList(int statusCode,
                                               Header[] headers, byte[] responseBody) {
        List<Printer> printers = new ArrayList<Printer>();
        try {
            JSONObject object = new JSONObject(new String(responseBody));
            Gson gson = new Gson();
            printers = gson.fromJson(
                    object.getString("printers"),
                    new TypeToken<ArrayList<Printer>>() {
                    }.getType());

            CoreData.getInstance().setPrinters(printers);
            PrinterSQL.deleteAllPrinter();
            PrinterSQL.addPrinters(printers);

            List<PrinterGroup> printerGroups = gson.fromJson(object.getString("printer_group"),
                    new TypeToken<ArrayList<PrinterGroup>>() {
                    }.getType());

            if (printerGroups != null) {
                PrinterGroupSQL.deletePrinter();
                PrinterGroupSQL.addPrinterGroups(printerGroups);
                CoreData.getInstance().setPrinterGroups(printerGroups);
            }

            User user = gson.fromJson(object.getString("user"), User.class);
            Store.saveObject(App.instance, Store.KDS_USER, user);
            return printers;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return printers;
    }

    public static void getMainPosInfo(int statusCode, Header[] headers,
                                      byte[] responseBody, Handler handler) {
        try {
            Gson gson = new Gson();
            JSONObject object = new JSONObject(new String(responseBody));
            MainPosInfo mainPosInfo = gson.fromJson(
                    object.optString("mainPosInfo"), MainPosInfo.class);

            App.instance.addMainPosInfo(mainPosInfo);

            handler.sendEmptyMessage(ResultCode.SUCCESS);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void deleteKotItemDetails(int statusCode, Header[] headers,
                                            byte[] responseBody, Handler handler) {
        try {
            Gson gson = new Gson();
            JSONObject jsonObject = new JSONObject(new String(responseBody));
            final KotSummary kotSummary = gson.fromJson(
                    jsonObject.getString("kotSummary"), KotSummary.class);
            final ArrayList<KotItemDetail> kotItemDetails = gson.fromJson(
                    jsonObject.optString("kotItemDetails"),
                    new TypeToken<ArrayList<KotItemDetail>>() {
                    }.getType());

            for (KotItemDetail kotItemDetail : kotItemDetails) {
                kotItemDetail.setKotStatus(ParamConst.KOT_STATUS_DONE);//done local only
                KotItemDetailSQL.update(kotItemDetail);

//                List<KotItemModifier> kotItemModifierList =
//                        KotItemModifierSQL.getKotItemModifiersByKotItemDetail(kotItemDetail.getId());
//
//                for (KotItemModifier kotItemModifier : kotItemModifierList) {
//                    KotItemModifierSQL.deleteKotItemModifier(kotItemModifier);
//                }
            }

//            KotItemDetailSQL.deleteKotItemDetail(kotItemDetails);

//            List<KotItemDetail> kotItemDetailList = KotItemDetailSQL.getKotItemDetailBySummaryId(kotSummary.getId());
//            if (kotItemDetailList.size() <= 0)
//                KotSummarySQL.deleteKotSummary(kotSummary);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void deleteKot(int statusCode, Header[] headers,
                                 byte[] responseBody, Handler handler) {
        try {
            Gson gson = new Gson();
            JSONObject jsonObject = new JSONObject(new String(responseBody));
            final KotSummary kotSummary = gson.fromJson(
                    jsonObject.getString("kotSummary"), KotSummary.class);

            ArrayList<KotItemDetail> kotItemDetails = KotItemDetailSQL.getKotItemDetailBySummaryIdRvcId(kotSummary.getId(), kotSummary.getRevenueCenterId());
            for (KotItemDetail kotItemDetail : kotItemDetails) {
                List<KotItemModifier> kotItemModifierList =
                        KotItemModifierSQL.getKotItemModifiersByKotItemDetail(kotItemDetail);

                for (KotItemModifier kotItemModifier : kotItemModifierList) {
                    KotItemModifierSQL.deleteKotItemModifier(kotItemModifier);
                }
            }

            KotItemDetailSQL.deleteAllKotItemDetailByKotSummary(kotSummary);
            KotSummarySQL.deleteKotSummary(kotSummary);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        handler.sendMessage(handler.obtainMessage(App.HANDLER_REFRESH_KOT, null));
    }

    public static void getKotItemDetail(int statusCode, Header[] headers,
                                        byte[] responseBody, Handler handler) {
        try {
            Gson gson = new Gson();
            JSONObject object = new JSONObject(new String(responseBody));
            List<KotItemDetail> subKotItemDetails = gson.fromJson(
                    object.optString("resultKotItemDetails"),
                    new TypeToken<ArrayList<KotItemDetail>>() {
                    }.getType());
            KotItemDetailSQL.addKotItemDetailList(subKotItemDetails);
            if (object.has("kotSummaryId")) {
                if (object.has("kotSummary")) {
                    KotSummary kotSummary = gson.fromJson(object.optString("kotSummary"), KotSummary.class);
//                    int count = KotItemDetailSQL.getKotItemDetailCountBySummaryId(kotSummary.getId(), kotSummary.getRevenueCenterId());
                    int count = KotItemDetailSQL.getKotItemDetailCountBySummaryUniqueId(kotSummary.getUniqueId());
                    if (count == 0) {
                        KotSummarySQL.updateKotSummaryStatusByUniqueId(ParamConst.KOTS_STATUS_DONE, kotSummary.getUniqueId());
                    }
                } else {
                    int id = object.getInt("kotSummaryId");
                    int count = KotItemDetailSQL.getKotItemDetailCountBySummaryId(id);
                    if (count == 0) {
                        KotSummarySQL.updateKotSummaryStatusById(ParamConst.KOTS_STATUS_DONE, id);
                    }
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getNewKotItemDetail(int statusCode, Header[] headers,
                                           byte[] responseBody, Handler handler) {
        try {
            Gson gson = new Gson();
            JSONObject object = new JSONObject(new String(responseBody));
            List<KotItemDetail> newKotItemDetails = gson.fromJson(
                    object.optString("newKotItemDetails"),
                    new TypeToken<ArrayList<KotItemDetail>>() {
                    }.getType());
            if (newKotItemDetails == null) {
                return;
            }
            KotItemDetailSQL.addKotItemDetailList(newKotItemDetails);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
