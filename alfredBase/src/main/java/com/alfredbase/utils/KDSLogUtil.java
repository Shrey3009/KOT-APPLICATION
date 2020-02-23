package com.alfredbase.utils;

import com.alfredbase.KDSLog;
import com.alfredbase.javabean.KDSHistory;
import com.alfredbase.javabean.KDSTracking;
import com.alfredbase.javabean.KotItemDetail;
import com.alfredbase.javabean.KotSummaryLog;
import com.alfredbase.javabean.model.KDSDevice;
import com.alfredbase.store.Store;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arif S. on 7/18/19
 */
public class KDSLogUtil {

    public static String putItemKdsLog(String kdsLogStr, KDSDevice kdsDevice, List<KotItemDetail> kotItemDetailList) {
        Gson gson = new Gson();
        KDSLog kdsLogs = gson.fromJson(kdsLogStr, new TypeToken<KDSLog>() {
        }.getType());

        KDSHistory selectedKdsHistory = getKdsHistoryByKDS(kdsDevice, kdsLogs);

        if (selectedKdsHistory != null) {
            selectedKdsHistory.kotItemDetails.addAll(kotItemDetailList);
        }

        return gson.toJson(kdsLogs);
    }

    public static String removeItemKdsLog(String kdsLogStr, KDSDevice kdsDevice, List<KotItemDetail> kotItemDetailList) {
        Gson gson = new Gson();
        KDSLog kdsLogs = gson.fromJson(kdsLogStr, new TypeToken<KDSLog>() {
        }.getType());

        KDSHistory selectedKdsHistory = getKdsHistoryByKDS(kdsDevice, kdsLogs);

        if (selectedKdsHistory != null) {

            for (KotItemDetail kotItemDetail : kotItemDetailList) {
                for (KotItemDetail kid : selectedKdsHistory.kotItemDetails) {
                    if (kid.getId().equals(kotItemDetail.getId())) {
                        selectedKdsHistory.kotItemDetails.remove(kid);
                        break;
                    }
                }
            }
        } else {
            for (KDSHistory kdsHistory : kdsLogs.kdsHistories) {
                List<KotItemDetail> kidToDelete = new ArrayList<>();

                for (KotItemDetail kid : kdsHistory.kotItemDetails) {
                    for (KotItemDetail kotItemDetail : kotItemDetailList) {
                        if (kid.getId().equals(kotItemDetail.getId())) {
                            kidToDelete.add(kid);
                        }
                    }
                }

                kdsHistory.kotItemDetails.removeAll(kidToDelete);
            }
        }

        return gson.toJson(kdsLogs);
    }

    public static String resetKdsLog(String kdsLogStr) {
        Gson gson = new Gson();
        KDSLog kdsLogs = gson.fromJson(kdsLogStr, new TypeToken<KDSLog>() {
        }.getType());

        for (KDSHistory kdsHistory : kdsLogs.kdsHistories) {
            if (kdsHistory.kotItemDetails != null)
                kdsHistory.kotItemDetails.clear();
        }

        return gson.toJson(kdsLogs);
    }

    public static String putDestKdsLog(String kdsLogStr, KDSDevice kdsDevice, KDSDevice kdsDeviceDest) {
        Gson gson = new Gson();
        KDSLog kdsLogs = gson.fromJson(kdsLogStr, new TypeToken<KDSLog>() {
        }.getType());

        KDSHistory selectedKdsHistory = getKdsHistoryByKDS(kdsDevice, kdsLogs);

        if (selectedKdsHistory != null) {
            selectedKdsHistory.kdsDeviceDest = kdsDeviceDest;
        }

        return gson.toJson(kdsLogs);
    }

    private static KDSHistory getKdsHistoryByKDS(KDSDevice kdsDevice, KDSLog kdsLogs) {

        if (kdsDevice == null) return null;

        for (KDSHistory kdsHistory : kdsLogs.kdsHistories) {
            KDSDevice kds = kdsHistory.kdsDevice;

            if (kds.getDevice_id() == kdsDevice.getDevice_id() &&
                    kds.getIP().equals(kdsDevice.getIP())) {
                return kdsHistory;
            }
        }

        return null;
    }

    public static String putKdsLog(String kdsLogStr, List<KDSDevice> kdsDeviceList) {
        Gson gson = new Gson();
        KDSLog kdsLogs = gson.fromJson(kdsLogStr, new TypeToken<KDSLog>() {
        }.getType());

        if (kdsLogs == null) {
            kdsLogs = new KDSLog();
        }

        //region remove existing data
        if (kdsLogs.kdsHistories != null) {
            for (KDSHistory kdsHistory : kdsLogs.kdsHistories) {
                KDSDevice kdsDevice = kdsHistory.kdsDevice;

                List<KDSDevice> kdsRemove = new ArrayList<>();
                for (KDSDevice kdsDevice1 : kdsDeviceList) {
                    if (kdsDevice.getDevice_id() == kdsDevice1.getDevice_id() &&
                            kdsDevice.getIP().equals(kdsDevice1.getIP())) {
                        kdsRemove.add(kdsDevice1);
                    }
                }

                if (kdsRemove.size() > 0) {
                    kdsDeviceList.removeAll(kdsRemove);
                }
            }
        } else {
            kdsLogs.kdsHistories = new ArrayList<>();
        }
        //endregion

        List<KDSHistory> kdsHistoryList = new ArrayList<>();

        for (KDSDevice kdsDevice : kdsDeviceList) {
            KDSHistory kdsHistory = new KDSHistory();
            kdsHistory.kdsDevice = kdsDevice;
            kdsHistoryList.add(kdsHistory);
        }

        kdsLogs.kdsHistories.addAll(kdsHistoryList);

        return gson.toJson(kdsLogs);
    }

    public static String initLog(List<KDSDevice> kdsDeviceList) {
        KotSummaryLog kotSummaryLog = new KotSummaryLog();
        List<KDSHistory> kdsHistoryList = new ArrayList<>();
        List<KDSTracking> kdsTrackingList = new ArrayList<>();

        for (KDSDevice kdsDevice : kdsDeviceList) {
            KDSHistory kdsHistory = new KDSHistory();
            kdsHistory.kdsDevice = kdsDevice;
            kdsHistoryList.add(kdsHistory);

            KDSTracking kdsTracking = new KDSTracking();
            kdsTracking.kdsDevice = kdsDevice;
            kdsTrackingList.add(kdsTracking);
        }

        kotSummaryLog.kdsHistoryList = kdsHistoryList;
        kotSummaryLog.kdsTrackingList = kdsTrackingList;

        return new Gson().toJson(kotSummaryLog);
    }

    public static String putLog(String kotSummaryLogStr, List<KotItemDetail> kotItemDetails, KDSDevice kdsDevice) {
        Gson gson = new Gson();
        KotSummaryLog kotSummaryLog = gson.fromJson(kotSummaryLogStr, KotSummaryLog.class);
        KDSHistory kdsHistory = getKdsHistory(kotSummaryLog.kdsHistoryList, kdsDevice.getDevice_id());
        KDSTracking kdsTracking = getKdsTracking(kotSummaryLog.kdsTrackingList, kdsDevice.getDevice_id());

        if (kdsHistory != null) {
            List<KotItemDetail> kotItemDetailListCopy = new ArrayList<>(kdsHistory.kotItemDetails);

            if (kotItemDetailListCopy.size() > 0) {
                for (KotItemDetail kotItemDetailUpdate : kotItemDetails) {
                    KotItemDetail kotItemDetailOld = null;

                    for (KotItemDetail kotItemDetail : kotItemDetailListCopy) {
                        if (kotItemDetailUpdate.getId().equals(kotItemDetail.getId())) {
                            kotItemDetailOld = kotItemDetail;
                            break;
                        }
                    }

                    if (kotItemDetailOld != null) {
                        kdsHistory.kotItemDetails.remove(kotItemDetailOld);
                        kdsHistory.kotItemDetails.add(kotItemDetailUpdate);
                        kotItemDetailListCopy.remove(kotItemDetailOld);
                    } else {
                        kdsHistory.kotItemDetails.add(kotItemDetailUpdate);
                    }
                }
            } else {
                kdsHistory.kotItemDetails.addAll(kotItemDetails);
            }
        } else {
            kdsHistory = new KDSHistory();
            kdsHistory.kdsDevice = kdsDevice;
            kdsHistory.kotItemDetails = new ArrayList<>();
            kdsHistory.kotItemDetails.addAll(kotItemDetails);
            kotSummaryLog.kdsHistoryList.add(kdsHistory);
        }

        if (kdsTracking != null) {

            List<KotItemDetail> kotItemDetailListCopy = new ArrayList<>(kdsTracking.kotItemDetails);

            if (kotItemDetailListCopy.size() > 0) {
                for (KotItemDetail kotItemDetailUpdate : kotItemDetails) {
                    KotItemDetail kotItemDetailOld = null;

                    for (KotItemDetail kotItemDetail : kotItemDetailListCopy) {
                        if (kotItemDetailUpdate.getId().equals(kotItemDetail.getId())) {
                            kotItemDetailOld = kotItemDetail;
                            break;
                        }
                    }

                    if (kotItemDetailOld != null) {
                        kdsTracking.kotItemDetails.remove(kotItemDetailOld);
                        kdsTracking.kotItemDetails.add(kotItemDetailUpdate);
                        kotItemDetailListCopy.remove(kotItemDetailOld);
                    } else {
                        kdsTracking.kotItemDetails.add(kotItemDetailUpdate);
                    }
                }
            } else {
                kdsTracking.kotItemDetails.addAll(kotItemDetails);
            }
        } else {
            kdsTracking = new KDSTracking();
            kdsTracking.kdsDevice = kdsDevice;
            kdsTracking.kotItemDetails = new ArrayList<>();
            kdsTracking.kotItemDetails.addAll(kotItemDetails);
            kotSummaryLog.kdsTrackingList.add(kdsTracking);
        }

        return gson.toJson(kotSummaryLog);
    }

    public static String removeTrackerLog(String kotSummaryLogStr, List<KotItemDetail> kotItemDetails, KDSDevice kdsDevice) {
        Gson gson = new Gson();
        KotSummaryLog kotSummaryLog = gson.fromJson(kotSummaryLogStr, KotSummaryLog.class);
        KDSTracking kdsTracking = getKdsTracking(kotSummaryLog.kdsTrackingList, kdsDevice.getDevice_id());

        if (kdsTracking != null) {
            for (KotItemDetail kotItemDetail : kotItemDetails) {
                for (KotItemDetail kid : kdsTracking.kotItemDetails) {
                    if (kotItemDetail.getId().equals(kid.getId())) {
                        kdsTracking.kotItemDetails.remove(kid);
                        break;
                    }
                }
            }
        }

        return gson.toJson(kotSummaryLog);
    }

    private static KDSHistory getKdsHistory(List<KDSHistory> kdsHistoryList, int id) {
        for (KDSHistory kdsHistory : kdsHistoryList) {
            if (kdsHistory.kdsDevice.getDevice_id() == id)
                return kdsHistory;
        }

        return null;
    }

    private static KDSTracking getKdsTracking(List<KDSTracking> kdsTrackingList, int id) {
        for (KDSTracking kdsTracking : kdsTrackingList) {
            if (kdsTracking.kdsDevice.getDevice_id() == id)
                return kdsTracking;
        }

        return null;
    }

}
