package com.alfredposclient.jobs;

import android.content.Context;
import android.text.TextUtils;

import com.alfredbase.BaseActivity;
import com.alfredbase.ParamConst;
import com.alfredbase.global.CoreData;
import com.alfredbase.http.APIName;
import com.alfredbase.javabean.ItemDetail;
import com.alfredbase.javabean.KDSTracking;
import com.alfredbase.javabean.KotItemDetail;
import com.alfredbase.javabean.KotItemModifier;
import com.alfredbase.javabean.KotSummary;
import com.alfredbase.javabean.KotSummaryLog;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.javabean.Printer;
import com.alfredbase.javabean.PrinterGroup;
import com.alfredbase.javabean.model.KDSDevice;
import com.alfredbase.javabean.model.PrinterDevice;
import com.alfredbase.store.sql.CommonSQL;
import com.alfredbase.store.sql.ItemDetailSQL;
import com.alfredbase.store.sql.KotItemDetailSQL;
import com.alfredbase.store.sql.KotItemModifierSQL;
import com.alfredbase.store.sql.KotSummarySQL;
import com.alfredbase.store.sql.OrderDetailSQL;
import com.alfredbase.store.sql.cpsql.CPOrderDetailSQL;
import com.alfredbase.utils.KDSLogUtil;
import com.alfredbase.utils.LogUtil;
import com.alfredposclient.R;
import com.alfredposclient.activity.MainPage;
import com.alfredposclient.global.App;
import com.google.gson.Gson;
import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KotJobManager {

    private JobManager kotJobManager;
    private Context context;

    public KotJobManager(Context mContext) {
        super();
        this.context = mContext;
        configureJobManager();
    }

    private void configureJobManager() {
        Configuration kotconfiguration = new Configuration.Builder(context)
                .customLogger(new AlfredJobLogger("KOT_JOBS")).id("kot_jobs")
                .minConsumerCount(1) // always keep at least one consumer alive
                .maxConsumerCount(3) // up to 3 consumers at a time
                .loadFactor(3) // 3 jobs per consumer
                .consumerKeepAlive(120) // wait 2 minute
                .build();
        this.kotJobManager = new JobManager(this.context, kotconfiguration);
    }

    private ArrayList<Printer> getPrinters(int printerGroupId, int kdsId, boolean isAssemblyLine) {
        ArrayList<Printer> printerResult = new ArrayList<>();
        ArrayList<Printer> printersData = CoreData.getInstance()
                .getPrintersInGroup(printerGroupId);

        if (isAssemblyLine) {
            //region assemblyline
            Printer printer = null;

            if (kdsId > 0) {
                boolean isPrinterFound = false;
                int i = 0;

                for (Printer mPrinter : printersData) {
                    if (mPrinter.getId().equals(kdsId)) {
                        isPrinterFound = true;
                    } else {
                        if (isPrinterFound) {
                            if (i < printersData.size() - 1) {
                                int position = i + 1;
                                Printer nextPrinter = printersData.get(position);

                                if (nextPrinter.getPrinterUsageType() == Printer.KDS_EXPEDITER) {
                                    mPrinter.isShowNext = 1;
                                } else {
                                    mPrinter.isShowNext = 0;//reset reference value
                                }
                            } else {
                                mPrinter.isShowNext = 0;//reset reference value
                            }

                            printer = mPrinter;
                            break;
                        }
                    }
                    i++;
                }
            } else {
                if (printersData.size() > 0) {
                    Printer currentPrinter = printersData.get(0);

                    if (printersData.size() > 1) {
                        Printer nextPrinter = printersData.get(1);

                        if (nextPrinter.getPrinterUsageType() == Printer.KDS_EXPEDITER) {
                            currentPrinter.isShowNext = 1;
                        } else {
                            currentPrinter.isShowNext = 0;//reset reference value
                        }
                    }

                    printer = currentPrinter;
                }
            }

            if (printer != null) {
                printerResult.add(printer);
            }
            //endregion
        } else {
            //normal behavior
            printerResult.addAll(printersData);
        }

        return printerResult;
    }

    private ArrayList<Printer> getPrinterEx(int printerGroupId) {
        ArrayList<Printer> printerResult = new ArrayList<>();
        ArrayList<Printer> printersData = CoreData.getInstance()
                .getPrintersInGroup(printerGroupId);

        for (Printer mPrinter : printersData) {
            if (Printer.KDS_EXPEDITER == mPrinter.getPrinterUsageType()) {
                printerResult.add(mPrinter);
            }
        }

        return printerResult;
    }

    private boolean isBalancerExists() {
        return App.instance.getBalancerKDSDevice() != null;
    }

    private List<Printer> getPrinterSummary(int printerGroupId) {
        List<Printer> printerResult = new ArrayList<>();

        for (Printer mPrinter : getPrinters(printerGroupId, 0, false)) {
            if (Printer.KDS_SUMMARY == mPrinter.getPrinterUsageType()) {
                printerResult.add(mPrinter);
            }
        }

        return printerResult;
    }

    private boolean isAssemblyLine(int printerGroupId) {
        Printer printer = CoreData.getInstance()
                .getPrinterByGroupId(printerGroupId);

        if (printer == null) return false;

        return printer.getPrinterGroupType() == PrinterGroup.KDS_ASMBLY_LINE;
    }

    public void sendKOTTmpToKDS(KotSummary kotSummary,
                                ArrayList<KotItemDetail> kotItemDetails, ArrayList<KotItemModifier> modifiers,
                                String method, Map<String, Object> orderMap) {

        boolean isUsed = false;
        if (!isUsed) return;
        ArrayList<Integer> printerGroupIds = new ArrayList<>();
        Map<Integer, ArrayList<KotItemDetail>> mapKOT = new HashMap<>();
        Map<Integer, ArrayList<KotItemModifier>> mods = new HashMap<>();

        //region collect kotItem by printerGroupId
        for (KotItemDetail items : kotItemDetails) {

            Integer pgid = items.getPrinterGroupId();

            if (pgid.equals(0)) continue;

            int kotItemDetailId = items.getId();

            // Get all Group ids that KOT belongs to
            if (!printerGroupIds.contains(pgid))
                printerGroupIds.add(pgid);

            // kot
            if (mapKOT.containsKey(pgid)) {
                ArrayList<KotItemDetail> tmp = mapKOT.get(pgid);
                tmp.add(items);
            } else {
                ArrayList<KotItemDetail> tmp = new ArrayList<>();
                tmp.add(items);
                mapKOT.put(pgid, tmp);
            }

            // modifier
            if (mods.containsKey(pgid)) {
                ArrayList<KotItemModifier> tmp = mods.get(pgid);
                for (KotItemModifier mof : modifiers) {
                    if (mof.getKotItemDetailId().equals(kotItemDetailId)) {
                        tmp.add(mof);
                    }
                }
            } else {
                ArrayList<KotItemModifier> tmp = new ArrayList<>();
                for (KotItemModifier mof : modifiers) {
                    if (mof.getKotItemDetailId().equals(kotItemDetailId)) {
                        tmp.add(mof);
                    }
                }
                mods.put(items.getPrinterGroupId(), tmp);
            }
        }
        //endregion

        //region add job to send it to KDS
        for (Integer prgid : printerGroupIds) {
            PrinterGroup printerGroup = CoreData.getInstance().getPrinterGroup(prgid);

            ArrayList<Printer> printers = getPrinters(printerGroup.getPrinterGroupId(), 0, isAssemblyLine(prgid));//if isAssemblyLine = true, will return 1 printer

            for (Printer printer : printers) {
                if (printer == null) continue;

                // KDS device
                KDSDevice kdsDevice = App.instance.getKDSDevice(printer.getId());
                // physical printer
                PrinterDevice printerDevice = App.instance.getPrinterDeviceById(printer
                        .getId());

                if (kdsDevice == null && printerDevice == null) {
                    continue;
                }

                if (kdsDevice != null && kotSummary != null) {
                    kdsDevice.setKdsType(printer.getPrinterUsageType());
                    kotSummary.setOriginalId(kotSummary.getId());
                    KotJob kotjob = new KotJob(kdsDevice, kotSummary,
                            mapKOT.get(prgid), mods.get(prgid), method, orderMap, APIName.SUBMIT_TMP_KOT);

                    kotJobManager.addJob(kotjob);
                }
            }
        }
        //endregion
    }

    public void updateKDSStatus(KDSDevice kdsDevice) {
        Printer printerBalancer = App.instance.getPrinterBalancer();
        if (printerBalancer != null) {
            KDSDevice kdsDeviceBalancer = App.instance.getKDSDevice(printerBalancer.getId());
            if (kdsDevice == null) return;

            KotJob kotjob = new KotJob(kdsDeviceBalancer, kdsDevice, APIName.UPDATE_KDS_STATUS);
            kotJobManager.addJob(kotjob);
        }
    }

    /**
     * Delete kot summary
     *
     * @param kotSummary
     * @param kotItemDetails
     */
    public void deleteKotItemDetailLogOnBalancer(KotSummary kotSummary, List<KotItemDetail> kotItemDetails, KDSDevice deletedKdsLog) {
        Printer printerBalancer = App.instance.getPrinterBalancer();
        if (printerBalancer != null) {
            KDSDevice kdsDevice = App.instance.getKDSDevice(printerBalancer.getId());
            if (kdsDevice == null) return;

            KotJob kotjob = new KotJob(kdsDevice, deletedKdsLog, kotSummary, kotItemDetails, APIName.DELETE_KDS_LOG_BALANCER);
            kotJobManager.addJob(kotjob);
        }
    }

    public void deleteKotItemDetailLogOnBalancer(KotSummary kotSummary) {
        List<KotItemDetail> kotItemDetails = KotItemDetailSQL.getKotItemDetailBySummaryId(kotSummary.getId());
        deleteKotItemDetailLogOnBalancer(kotSummary, kotItemDetails, null);
    }

    public void deleteKotSummary(KotSummary kotSummary, List<KotItemDetail> kotItemDetails) {
        ArrayList<Integer> printerGroupIds = new ArrayList<>();

        for (KotItemDetail kotItemDetail : kotItemDetails) {
            int pgId = kotItemDetail.getPrinterGroupId();
            if (!printerGroupIds.contains(pgId))
                printerGroupIds.add(pgId);
        }

        for (int pgId : printerGroupIds) {
            List<Printer> printerList = getPrinterSummary(pgId);

            for (Printer printer : printerList) {
                if (printer == null) continue;

                KDSDevice kdsDevice = App.instance.getKDSDevice(printer.getId());
                if (kdsDevice == null) continue;

                KotJob kotjob = new KotJob(kdsDevice, kotSummary, ParamConst.JOB_DELETE_KOT_SUMMARY, APIName.DELETE_KOT_KDS);
                kotJobManager.addJob(kotjob);
            }
        }
    }

    public void deleteKotSummaryAllKds(KotSummary kotSummary, List<KotItemDetail> kotItemDetails) {
//        KotSummaryLog kotSummaryLogs = new Gson().fromJson(kotSummary.getKotSummaryLog(), KotSummaryLog.class);
//        ArrayList<Integer> printerGroupIds = new ArrayList<>();
//
//        for (KotItemDetail kotItemDetail : kotItemDetails) {
//            int pgId = kotItemDetail.getPrinterGroupId();
//            if (!printerGroupIds.contains(pgId))
//                printerGroupIds.add(pgId);
//        }
//
//        for (int pgId : printerGroupIds) {
//            List<Printer> printerList = getPrinterSummary(pgId);
//
//            for (Printer printer : printerList) {
//                if (printer == null) continue;
//
//                KDSDevice kdsDevice = App.instance.getKDSDevice(printer.getId());
//                if (kdsDevice == null) continue;
//
//                KotJob kotjob = new KotJob(kdsDevice, kotSummary, ParamConst.JOB_DELETE_KOT_SUMMARY, APIName.DELETE_KOT_KDS);
//                kotJobManager.addJob(kotjob);
//            }
//        }
    }

    public void sendKOTToKDSSummary(KotSummary kotSummary,
                                    ArrayList<KotItemDetail> kotItemDetails, ArrayList<KotItemModifier> modifiers,
                                    String method) {
        ArrayList<Integer> printerGroupIds = new ArrayList<>();
        Map<Integer, ArrayList<KotItemDetail>> mapKOT = new HashMap<>();
        Map<Integer, ArrayList<KotItemModifier>> mods = new HashMap<>();

        //region collect kotItem by printerGroupId
        for (KotItemDetail items : kotItemDetails) {

            Integer pgid = items.getPrinterGroupId();

            if (pgid.equals(0)) continue;

            int kotItemDetailId = items.getId();

            // Get all Group ids that KOT belongs to
            if (!printerGroupIds.contains(pgid))
                printerGroupIds.add(pgid);

            // kot
            if (mapKOT.containsKey(pgid)) {
                ArrayList<KotItemDetail> tmp = mapKOT.get(pgid);
                tmp.add(items);
            } else {
                ArrayList<KotItemDetail> tmp = new ArrayList<>();
                tmp.add(items);
                mapKOT.put(pgid, tmp);
            }

            // modifier
            if (mods.containsKey(pgid)) {
                ArrayList<KotItemModifier> tmp = mods.get(pgid);
                for (KotItemModifier mof : modifiers) {
                    if (mof.getKotItemDetailId().equals(kotItemDetailId)) {
                        tmp.add(mof);
                    }
                }
            } else {
                ArrayList<KotItemModifier> tmp = new ArrayList<>();
                for (KotItemModifier mof : modifiers) {
                    if (mof.getKotItemDetailId().equals(kotItemDetailId)) {
                        tmp.add(mof);
                    }
                }
                mods.put(items.getPrinterGroupId(), tmp);
            }
        }
        //endregion

        //region add job to send it to KDS
        for (Integer prgid : printerGroupIds) {

            List<PrinterGroup> printerGroupAsChildes = CoreData.getInstance()
                    .getPrinterGroupInGroup(prgid);//group printer as child

            List<Printer> printers = new ArrayList<>();

            if (printerGroupAsChildes.size() > 0) {//printer group
                for (PrinterGroup pg : printerGroupAsChildes) {
                    printers.addAll(getPrinterSummary(pg.getPrinterId()));
                }
            } else {
                printers = getPrinterSummary(prgid);
            }

            for (Printer printer : printers) {
                if (printer == null) continue;

                // KDS device
                KDSDevice kdsDevice = App.instance.getKDSDevice(printer.getId());
                // physical printer
                PrinterDevice printerDevice = App.instance.getPrinterDeviceById(printer.getId());

                if (kdsDevice == null && printerDevice == null) {
                    continue;
                }

                if (kdsDevice != null && kotSummary != null) {
                    KotJob kotjob = new KotJob(kdsDevice, kotSummary,
                            mapKOT.get(prgid), mods.get(prgid), method, null, APIName.SUBMIT_SUMMARY_KDS);

                    kotJobManager.addJob(kotjob);
                }
            }
        }
        //endregion
    }

    public void sendKOTToNextKDS(KotSummary kotSummary,
                                 ArrayList<KotItemDetail> kotItemDetails, ArrayList<KotItemModifier> modifiers,
                                 String method, Map<String, Object> orderMap, int kdsId) {

        ArrayList<Integer> printerGroupIds = new ArrayList<>();
//        Map<Integer, ArrayList<KotItemDetail>> mapKOT = new HashMap<>();
//        Map<Integer, ArrayList<KotItemModifier>> mods = new HashMap<>();
        Map<Integer, ArrayList<KotItemModifier>> modCombo = new HashMap<>();

        //region collect kotItem by printerGroupId
        for (KotItemDetail items : kotItemDetails) {

            Integer pgid = items.getPrinterGroupId();

            OrderDetail orderDetail = OrderDetailSQL.getOrderDetail(items.getOrderDetailId());
            ItemDetail itemDetail = ItemDetailSQL.getItemDetailById(orderDetail.getItemId());
            if (itemDetail.getItemType() == ParamConst.ITEMDETAIL_COMBO_ITEM) {//package item
                modCombo = getComboModifiers(items, modifiers, modCombo);
                continue;
            } else {
                if (pgid.equals(0)) continue;
            }

            int kotItemDetailId = items.getId();

            // Get all Group ids that KOT belongs to
            if (!printerGroupIds.contains(pgid))
                printerGroupIds.add(pgid);

//            // kot
//            if (mapKOT.containsKey(pgid)) {
//                ArrayList<KotItemDetail> tmp = mapKOT.get(pgid);
//                tmp.add(items);
//            } else {
//                ArrayList<KotItemDetail> tmp = new ArrayList<>();
//                tmp.add(items);
//                mapKOT.put(pgid, tmp);
//            }
//
//            // modifier
//            if (mods.containsKey(pgid)) {
//                ArrayList<KotItemModifier> tmp = mods.get(pgid);
//                for (KotItemModifier mof : modifiers) {
//                    if (mof.getKotItemDetailId().equals(kotItemDetailId)) {
//                        tmp.add(mof);
//                    }
//                }
//            } else {
//                ArrayList<KotItemModifier> tmp = new ArrayList<>();
//                for (KotItemModifier mof : modifiers) {
//                    if (mof.getKotItemDetailId().equals(kotItemDetailId)) {
//                        tmp.add(mof);
//                    }
//                }
//                mods.put(items.getPrinterGroupId(), tmp);
//            }
        }
        //endregion

        if (kotSummary != null) {
            int kotSummaryId = CommonSQL.isFakeId(kotSummary.getId()) ? kotSummary.getOriginalId() : kotSummary.getId();
            KotSummary kotSummaryLocal = KotSummarySQL.getKotSummaryById(kotSummaryId);

            if (kotSummaryLocal != null)
                kotSummary.setOrderDetailCount(kotSummaryLocal.getOrderDetailCount());
        }

        if (modCombo.size() > 0) {
            printerGroupIds.addAll(modCombo.keySet());
        }

        //region add job to send it to KDS
        for (Integer prgid : printerGroupIds) {

            PrinterGroup printerGroup = CoreData.getInstance().getPrinterGroup(prgid);
            List<PrinterGroup> printerGroupAsChildes = CoreData.getInstance()
                    .getPrinterGroupInGroup(printerGroup.getPrinterGroupId());//group printer as child

            List<Printer> printers = new ArrayList<>();

            if (printerGroupAsChildes.size() > 0) {//printer group
                for (PrinterGroup pg : printerGroupAsChildes) {
                    printers.addAll(getPrinters(pg.getPrinterId(), kdsId, isAssemblyLine(pg.getPrinterId())));
                }
            } else {
                printers = getPrinters(prgid, kdsId, isAssemblyLine(prgid));
            }

//            ArrayList<Printer> printers = getPrinters(prgid, kdsId, isAssemblyLine(prgid));//if isAssemblyLine = true, will return 1 printer

            for (Printer printer : printers) {
                if (printer == null) continue;

                // KDS device
                KDSDevice kdsDevice = App.instance.getKDSDevice(printer.getId());
                // physical printer
                PrinterDevice printerDevice = App.instance.getPrinterDeviceById(printer.getId());

                if (kdsDevice == null && printerDevice == null) {
                    continue;
                }

                if (kdsDevice != null && kotSummary != null) {
                    kotSummary.setNext(printer.isShowNext);//don't save to db here

                    KotJob kotjob = new KotJob(kdsDevice, kotSummary,
                            kotItemDetails, modifiers, method, orderMap, APIName.SUBMIT_NEXT_KOT);

                    kotJobManager.addJob(kotjob);
                }
            }
        }
        //endregion
    }

    private Map<Integer, ArrayList<KotItemModifier>> getComboModifiers(KotItemDetail kotItemDetail, ArrayList<KotItemModifier> modifiers,
                                                                       Map<Integer, ArrayList<KotItemModifier>> modCombo) {
        String kotItemDetailUniqueId = kotItemDetail.getUniqueId();

        for (KotItemModifier kotItemModifier : modifiers) {
            int printerGroupId = kotItemModifier.getPrinterId();

            if (printerGroupId <= 0) continue;

            if (kotItemModifier.getKotItemDetailUniqueId().equals(kotItemDetailUniqueId)) {
                if (modCombo.containsKey(printerGroupId)) {
                    ArrayList<KotItemModifier> tmp = modCombo.get(printerGroupId);
                    tmp.add(kotItemModifier);
                } else {
                    ArrayList<KotItemModifier> tmp = new ArrayList<>();
                    tmp.add(kotItemModifier);
                    modCombo.put(printerGroupId, tmp);
                }
            }
        }

        return modCombo;

    }

    private void sendModifierToKds(Map<Integer, ArrayList<KotItemModifier>> modCombo,
                                   KotSummary kotSummary, String method, Map<String, Object> orderMap, int kdsId, String url) {

        BaseActivity context = App.getTopActivity();

        for (Map.Entry<Integer, ArrayList<KotItemModifier>> entry : modCombo.entrySet()) {
            int printerGroupId = entry.getKey();
            ArrayList<KotItemModifier> kotItemModifiers = entry.getValue();
            Map<Integer, KotItemDetail> mapKotItemDetail = new HashMap<>();
            ArrayList<KotItemDetail> kotItemDetails = new ArrayList<>();

            for (KotItemModifier kotItemModifier : kotItemModifiers) {
                //parent package item
                KotItemDetail kotItemDetail = KotItemDetailSQL.getKotItemDetailById(kotItemModifier.getKotItemDetailId());
//                Modifier modifier = ModifierSQL.getModifierById(kotItemModifier.getModifierId());
//                if (modifier != null) {
//                    ItemDetail itemDetail = CoreData.getInstance().getItemDetailByTemplateId(modifier.getItemId());
//                }

                if (kotItemDetail != null) {
                    if (!mapKotItemDetail.containsKey(kotItemDetail.getId())) {
                        kotItemDetail.setItemType(ParamConst.ITEMDETAIL_COMBO_ITEM);

                        mapKotItemDetail.put(kotItemDetail.getId(), kotItemDetail);
                    }
                }
            }

            for (Map.Entry<Integer, KotItemDetail> map : mapKotItemDetail.entrySet()) {
                kotItemDetails.add(map.getValue());
            }

            PrinterGroup printerGroup = CoreData.getInstance().getPrinterGroup(printerGroupId);
            List<PrinterGroup> printerGroupAsChildes = CoreData.getInstance()
                    .getPrinterGroupInGroup(printerGroup.getPrinterGroupId());//group printer as child

            List<Printer> printers = new ArrayList<>();

            if (ParamConst.JOB_VOID_KOT.equals(method)) {

                if (printerGroupAsChildes.size() > 0) {//printer group
                    for (PrinterGroup pg : printerGroupAsChildes) {
                        printers.addAll(CoreData.getInstance().getPrintersInGroup(pg.getPrinterId()));//printerId is group id
                    }
                } else {
                    printers = CoreData.getInstance()
                            .getPrintersInGroup(printerGroupId);
                }

                if (isBalancerExists()) {
                    Printer printerBalancer = App.instance.getPrinterBalancer();
                    if (printerBalancer != null)
                        printers.add(printerBalancer);
                }
            } else {
                if (printerGroupAsChildes.size() > 0) {//printer group
                    for (PrinterGroup pg : printerGroupAsChildes) {
                        printers.addAll(getPrinters(pg.getPrinterId(), 0, isAssemblyLine(pg.getPrinterId())));
                    }
                } else {
                    printers = getPrinters(printerGroupId, 0, isAssemblyLine(printerGroupId));
                }
            }

            boolean isCheckBalancer = false;

            if (isBalancerExists()) {
                if ((ParamConst.JOB_NEW_KOT.equals(method) || ParamConst.JOB_UPDATE_KOT.equals(method))
                        && printers.size() > 1) {
                    List<KDSDevice> kdsDevicesOnline = new ArrayList<>();

                    for (Printer printer : printers) {
                        KDSDevice kdsDevice = App.instance.getKDSDevice(printer.getId());
                        if (kdsDevice.getKdsStatus() == 0) {
                            kdsDevicesOnline.add(kdsDevice);
                        }

                        if (kdsDevicesOnline.size() > 1) {
                            isCheckBalancer = true;
                            break;
                        }
                    }
                }
            }

            for (Printer printer : printers) {
                KDSDevice kdsDevice = App.instance.getKDSDevice(printer.getId());
                PrinterDevice printerDevice = App.instance.getPrinterDeviceById(printer.getId());

                if (kdsDevice == null && printerDevice == null) {
                    if (context != null)
                        context.kotPrintStatus(MainPage.KOT_PRINT_NULL, null);
                    return;
                }

                if (kdsDevice != null && kotSummary != null) {
                    if (kdsDevice.getKdsStatus() == -1) continue;//offline kds

                    kdsDevice.setKdsType(printer.getPrinterUsageType());
                    kotSummary.setNext(printer.isShowNext);//don't save to db here

                    int kotSummaryId = CommonSQL.isFakeId(kotSummary.getId()) ? kotSummary.getOriginalId() : kotSummary.getId();
                    kotSummary.setOriginalId(kotSummaryId);

                    KotJob kotjob;
                    if (!TextUtils.isEmpty(url)) {
                        kotjob = new KotJob(kdsDevice, kotSummary,
                                kotItemDetails, kotItemModifiers, method, orderMap, url);
                    } else {
                        kotjob = new KotJob(kdsDevice, kotSummary,
                                kotItemDetails, kotItemModifiers, method, orderMap, false, isCheckBalancer);
                    }

                    kotJobManager.addJob(kotjob);
                }

                if (isCheckBalancer) break;
            }
        }
    }

    public void sendToSelectedKDS(KotSummary kotSummary,
                                  ArrayList<KotItemDetail> kotItemDetails, ArrayList<KotItemModifier> modifiers,
                                  String method, Map<String, Object> orderMap, KDSDevice kdsDevice) {
        KotJob kotjob = new KotJob(kdsDevice, kotSummary,
                kotItemDetails, modifiers, method, orderMap, false, false);
        kotJobManager.addJob(kotjob);

    }

    /* convert KOT to kot jobs */
    public void tearDownKot(KotSummary kotSummary,
                            ArrayList<KotItemDetail> kot, ArrayList<KotItemModifier> modifiers,
                            String method, Map<String, Object> orderMap) {
        LogUtil.d("开始时间", "时间");
        ArrayList<Integer> printerGrougIds = new ArrayList<Integer>();
        // map printergroudId to Kot: Group ID --> Details
        Map<Integer, ArrayList<KotItemDetail>> kots = new HashMap<Integer, ArrayList<KotItemDetail>>();
        // map printerGroudId to Modifiers
        Map<Integer, ArrayList<KotItemModifier>> mods = new HashMap<Integer, ArrayList<KotItemModifier>>();
        //Map printerGroupId to combo modifier
        Map<Integer, ArrayList<KotItemModifier>> modCombo = new HashMap<>();

        BaseActivity context = App.getTopActivity();
        int kotSize = 0;
        int kotVoidSize = 0;

        //region Collect Printer group
        for (KotItemDetail items : kot) {

            Integer pgid = items.getPrinterGroupId();

            OrderDetail orderDetail = OrderDetailSQL.getOrderDetail(items.getOrderDetailId());
            ItemDetail itemDetail = ItemDetailSQL.getItemDetailById(orderDetail.getItemId());

            if (itemDetail.getItemType() == ParamConst.ITEMDETAIL_COMBO_ITEM) {//package item
                if (items.getKotStatus() == ParamConst.KOT_STATUS_VOID) {
                    kotVoidSize += KotItemModifierSQL.getKotItemModifiersByKotItemDetail(items.getId()).size();
                } else {
                    items.setItemType(ParamConst.ITEMDETAIL_COMBO_ITEM);
                    KotItemDetailSQL.updateKotItemDetailField("itemType",
                            ParamConst.ITEMDETAIL_COMBO_ITEM, items.getId());

                    kotSize += KotItemModifierSQL.getKotItemModifiersByKotItemDetail(items.getId()).size();
                }

                modCombo = getComboModifiers(items, modifiers, modCombo);
                continue;
            } else {

                if (items.getKotStatus() == ParamConst.KOT_STATUS_VOID) {
                    kotVoidSize++;
                } else {
                    kotSize++;
                }

                if (pgid.intValue() == 0) {
                    if (context != null)
                        context.kotPrintStatus(MainPage.KOT_ITEM_PRINT_NULL,
                                items.getItemName());
                    return;
                }
            }

            int kotItemDetailId = items.getId().intValue();

            // Get all Group ids that KOT blongs to
            if (!printerGrougIds.contains(pgid))
                printerGrougIds.add(pgid);

            // kot
            if (kots.containsKey(pgid)) {
                ArrayList<KotItemDetail> tmp = kots.get(pgid);
                tmp.add(items);
            } else {
                ArrayList<KotItemDetail> tmp = new ArrayList<KotItemDetail>();
                tmp.add(items);
                kots.put(pgid, tmp);
            }

            // modifier
            if (mods.containsKey(pgid)) {
                ArrayList<KotItemModifier> tmp = mods.get(pgid);
                for (KotItemModifier mof : modifiers) {
                    if (mof.getKotItemDetailId().intValue() == kotItemDetailId) {
                        tmp.add(mof);
                    }
                }
            } else {
                ArrayList<KotItemModifier> tmp = new ArrayList<KotItemModifier>();
                for (KotItemModifier mof : modifiers) {
                    if (mof.getKotItemDetailId().intValue() == kotItemDetailId) {
                        tmp.add(mof);
                    }
                }
                mods.put(items.getPrinterGroupId(), tmp);
            }
        }
        //endregion

        //region modify kotsummary
        if (kotSummary != null) {
            KotSummary kotSummaryLocal = KotSummarySQL.getKotSummaryById(kotSummary.getId());

            kotSummary.setOriginalId(kotSummary.getId());
            kotSummary.setOriginalUniqueId(kotSummary.getUniqueId());
            KotSummarySQL.update(kotSummary);

            kotSummary.setStatus(ParamConst.KOTS_STATUS_UNDONE);
            KotSummarySQL.updateKotSummaryStatusById(ParamConst.KOTS_STATUS_UNDONE, kotSummary.getId().intValue());

            int count = kotSummaryLocal != null ? kotSummaryLocal.getOrderDetailCount() - kotVoidSize + kotSize : kotSize;
            kotSummary.setOrderDetailCount(count);
//            KotSummarySQL.updateKotSummaryOrderCountById(count, kotSummary.getId().intValue());

            //region kotSummary logs initialization
            if (TextUtils.isEmpty(kotSummary.getKotSummaryLog())) {
                List<KDSDevice> kdsDeviceList = new ArrayList<>();
                List<Integer> printerGroupIdList = new ArrayList<>(printerGrougIds);
                printerGroupIdList.addAll(modCombo.keySet());

                for (Integer printerGroupId : printerGroupIdList) {
                    ArrayList<Printer> printersData = CoreData.getInstance()
                            .getPrintersInGroup(printerGroupId);

                    for (Printer printer : printersData) {
                        KDSDevice kdsDevice = App.instance.getKDSDevice(printer.getId());
                        if (kdsDevice != null) {
                            kdsDeviceList.add(kdsDevice);
                        }
                    }
                }

                kotSummary.setKotSummaryLog(KDSLogUtil.initLog(kdsDeviceList));
                KotSummarySQL.updateKotSummaryLog(kotSummary);
            }
            //endregion
        }
        //endregion

        if (modCombo.size() > 0)
            sendModifierToKds(modCombo, kotSummary, method, orderMap, 0, "");

        // add job to send it to KDS
        for (Integer prgid : printerGrougIds) {

            PrinterGroup printerGroup = CoreData.getInstance().getPrinterGroup(prgid);
            List<PrinterGroup> printerGroupAsChildes = CoreData.getInstance()
                    .getPrinterGroupInGroup(printerGroup.getPrinterGroupId());//group printer as child

            List<Printer> printers = new ArrayList<>();
            List<Printer> exPrinters = new ArrayList<>();

            if (printerGroupAsChildes.size() > 0) {//printer group
                for (PrinterGroup pg : printerGroupAsChildes) {
                    printers.addAll(getPrinters(pg.getPrinterId(), 0, isAssemblyLine(pg.getPrinterId())));//printer id is groupId
                    exPrinters.addAll(getPrinterEx(pg.getPrinterId()));
                }
            } else {
                printers = getPrinters(prgid, 0, isAssemblyLine(prgid));
                exPrinters = getPrinterEx(prgid);
            }

            //region Broadcast data to expediter printer
            for (int i = 0; i < exPrinters.size() - 1; i++) {//exclude last position
                Printer printer = exPrinters.get(i);
                KDSDevice kdsDevice = App.instance.getKDSDevice(printer.getId());
                PrinterDevice printerDevice = App.instance.getPrinterDeviceById(printer
                        .getId());

                if (kdsDevice == null && printerDevice == null) continue;

                if (kdsDevice != null && kotSummary != null) {
                    KotJob kotjob = new KotJob(kdsDevice, kotSummary, ParamConst.JOB_KOT_UPDATE_ORDER_COUNT);
                    kotJobManager.addJob(kotjob);
                }
            }
            //endregion

            if (ParamConst.JOB_VOID_KOT.equals(method)) {
                if (printerGroupAsChildes.size() > 0) {//printer group
                    printers.clear();

                    for (PrinterGroup pg : printerGroupAsChildes) {
                        printers.addAll(CoreData.getInstance()
                                .getPrintersInGroup(pg.getPrinterId()));//printer id is groupId
                    }
                } else {
                    printers = CoreData.getInstance()
                            .getPrintersInGroup(prgid);
                }

                //also delete on balancer
                if (isBalancerExists()) {
                    Printer printerBalancer = App.instance.getPrinterBalancer();
                    if (printerBalancer != null)
                        printers.add(printerBalancer);
                }
            }

            boolean isCheckBalancer = false;

            if (isBalancerExists()) {
                if ((ParamConst.JOB_NEW_KOT.equals(method) || ParamConst.JOB_UPDATE_KOT.equals(method))
                        && printers.size() > 1) {
                    List<KDSDevice> kdsDevicesOnline = new ArrayList<>();

                    for (Printer printer : printers) {
                        KDSDevice kdsDevice = App.instance.getKDSDevice(printer.getId());
                        if (kdsDevice != null && kdsDevice.getKdsStatus() == 0) {
                            kdsDevicesOnline.add(kdsDevice);
                        }

                        if (kdsDevicesOnline.size() > 1) {
                            isCheckBalancer = true;
                            break;
                        }
                    }
                }
            }

            for (Printer printer : printers) {
                // KDS device
                KDSDevice kdsDevice = App.instance.getKDSDevice(printer.getId());
                // physical printer
                PrinterDevice printerDevice = App.instance.getPrinterDeviceById(printer
                        .getId());
                if (kdsDevice == null && printerDevice == null) {
                    if (context != null)
                        context.kotPrintStatus(MainPage.KOT_PRINT_NULL, null);
                    return;
                }

                if (kdsDevice != null && kotSummary != null) {
                    if (kdsDevice.getKdsStatus() == -1) continue;//kds device offline

                    kdsDevice.setKdsType(printer.getPrinterUsageType());
                    kotSummary.setNext(printer.isShowNext);//don't save to db here

                    KotJob kotjob;
                    kotjob = new KotJob(kdsDevice, kotSummary,
                            kots.get(prgid), mods.get(prgid), method, orderMap, false, isCheckBalancer);
                    kotJobManager.addJob(kotjob);
                }

                if (printerDevice != null) {
                    printerDevice.setGroupId(prgid.intValue());

                    boolean printed;

                    if ((!printerDevice.getIP().contains(":") && !printerDevice.getIP().contains(",")) || printerDevice.getIsLablePrinter() != 1) {
                        printed = App.instance.remoteKotPrint(printerDevice,
                                kotSummary, kots.get(prgid), mods.get(prgid), false);

                        if (printed) {
                            List<Integer> orderDetailIds = (List<Integer>) orderMap
                                    .get("orderDetailIds");
//                            boolean refreshStock = false;
                            if (orderDetailIds != null && orderDetailIds.size() != 0) {
                                ArrayList<OrderDetail> orderDetails = new ArrayList<OrderDetail>();
                                synchronized (orderDetails) {
                                    for (int i = 0; i < orderDetailIds.size(); i++) {
                                        OrderDetail orderDetail = OrderDetailSQL
                                                .getOrderDetail(orderDetailIds
                                                        .get(i));
                                        orderDetail
                                                .setOrderDetailStatus(ParamConst.ORDERDETAIL_STATUS_KOTPRINTERD);
                                        orderDetails.add(orderDetail);

//                                        if(!doStockMap.contains(orderDetail.getId())){
//                                            boolean refresh = RemainingStockHelper.updateRemainingStockNumByOrderDetail(orderDetail);
//                                            if(!refreshStock) {
//                                                refreshStock = refresh;
//                                            }
//                                            doStockMap.add(orderDetail.getId());
//                                        }
                                    }
                                }
                                OrderDetailSQL.addOrderDetailList(orderDetails);
                                LogUtil.e("成功时间", "时间");
                                context.kotPrintStatus(MainPage.KOT_PRINT_SUCCEED,
                                        orderMap.get("orderId"));
//                                App.instance.getSyncJob().updateRemainingStock((Integer) orderMap.get("orderId"));
//                                if(refreshStock){
//                                    App.getTopActivity().httpRequestAction(MainPage.REFRESH_STOCK_NUM, null);
//                                }
                            }
                        } else {
                            context.kotPrintStatus(MainPage.KOT_PRINT_FAILED,
                                    orderMap.get("orderId"));
                        }
                    }
//
//                    }
                }

                //send it just once for balancer
                if (isCheckBalancer) break;
            }

        }
    }

    public void refreshAllKDS(ArrayList<KotItemDetail> kot, String method) {
        LogUtil.d("开始时间", "时间");
        ArrayList<Integer> printerGrougIds = new ArrayList<Integer>();
        Map<Integer, ArrayList<KotItemDetail>> kots = new HashMap<Integer, ArrayList<KotItemDetail>>();
        BaseActivity context = App.getTopActivity();
        for (KotItemDetail items : kot) {
            Integer pgid = items.getPrinterGroupId();
            if (pgid.intValue() == 0) {
                context.kotPrintStatus(MainPage.KOT_ITEM_PRINT_NULL,
                        items.getItemName());
                return;
            }
            // Get all Group ids that KOT blongs to
            if (!printerGrougIds.contains(pgid))
                printerGrougIds.add(pgid);

            // kot
            if (kots.containsKey(pgid)) {
                ArrayList<KotItemDetail> tmp = kots.get(pgid);
                tmp.add(items);
            } else {
                ArrayList<KotItemDetail> tmp = new ArrayList<KotItemDetail>();
                tmp.add(items);
                kots.put(pgid, tmp);
            }

        }

//        List<Integer> doStockMap = new ArrayList<>();
        // add job to send it to KDS

        for (Integer prgid : printerGrougIds) {
            ArrayList<Printer> printers = CoreData.getInstance()
                    .getPrintersInGroup(prgid.intValue());
            for (Printer prnt : printers) {
                // KDS device
                KDSDevice kds1 = App.instance.getKDSDevice(prnt.getId());
                if (kds1 != null) {
                    KotJob kotjob = new KotJob(kds1, kots.get(prgid), method);
                    kotJobManager.addJob(kotjob);
                }

            }

        }
    }

    public void tearDownKotForSub(KotSummary kotSummary,
                                  ArrayList<KotItemDetail> kot, ArrayList<KotItemModifier> modifiers,
                                  String method, Map<String, Object> orderMap) {
        LogUtil.d("开始时间", "时间");
        ArrayList<Integer> printerGrougIds = new ArrayList<Integer>();
        // map printergroudId to Kot: Group ID --> Details
        Map<Integer, ArrayList<KotItemDetail>> kots = new HashMap<Integer, ArrayList<KotItemDetail>>();
        // map printerGroudId to Modifiers
        Map<Integer, ArrayList<KotItemModifier>> mods = new HashMap<Integer, ArrayList<KotItemModifier>>();
        BaseActivity context = App.getTopActivity();
        for (KotItemDetail items : kot) {
            Integer pgid = items.getPrinterGroupId();
            if (pgid.intValue() == 0) {
                context.kotPrintStatus(MainPage.KOT_ITEM_PRINT_NULL,
                        items.getItemName());
                return;
            }
            int kotItemDetailId = items.getId().intValue();

            // Get all Group ids that KOT blongs to
            if (!printerGrougIds.contains(pgid))
                printerGrougIds.add(pgid);

            // kot
            if (kots.containsKey(pgid)) {
                ArrayList<KotItemDetail> tmp = kots.get(pgid);
                tmp.add(items);
            } else {
                ArrayList<KotItemDetail> tmp = new ArrayList<KotItemDetail>();
                tmp.add(items);
                kots.put(pgid, tmp);
            }

            // modifier
            if (mods.containsKey(pgid)) {
                ArrayList<KotItemModifier> tmp = mods.get(pgid);
                for (KotItemModifier mof : modifiers) {
                    if (mof.getKotItemDetailId().intValue() == kotItemDetailId) {
                        tmp.add(mof);
                    }
                }
            } else {
                ArrayList<KotItemModifier> tmp = new ArrayList<KotItemModifier>();
                for (KotItemModifier mof : modifiers) {
                    if (mof.getKotItemDetailId().intValue() == kotItemDetailId) {
                        tmp.add(mof);
                    }
                }
                mods.put(items.getPrinterGroupId(), tmp);
            }
        }

        if (printerGrougIds != null && printerGrougIds.size() > 0 && kotSummary != null) {
            kotSummary.setStatus(ParamConst.KOTS_STATUS_UNDONE);
            KotSummarySQL.updateKotSummaryStatusById(ParamConst.KOTS_STATUS_UNDONE, kotSummary.getId().intValue());
        }


        // add job to send it to KDS
        for (Integer prgid : printerGrougIds) {
            ArrayList<Printer> printers = CoreData.getInstance()
                    .getPrintersInGroup(prgid.intValue());
            for (Printer prnt : printers) {
                // KDS device
                KDSDevice kds1 = App.instance.getKDSDevice(prnt.getId());
                // physical printer
                PrinterDevice prntd = App.instance.getPrinterDeviceById(prnt
                        .getId());
                if (kds1 == null && prntd == null) {
                    context.kotPrintStatus(MainPage.KOT_PRINT_NULL, null);
                    return;
                }
                if (kds1 != null) {
                    KotJob kotjob = new KotJob(kds1, kotSummary,
                            kots.get(prgid), mods.get(prgid), method, orderMap);
                    kotJobManager.addJob(kotjob);
                }
                if (prntd != null) {
                    prntd.setGroupId(prgid.intValue());

                    boolean printed = false;

                    if ((!prntd.getIP().contains(":") && !prntd.getIP().contains(",")) || prntd.getIsLablePrinter() != 1) {
                        printed = App.instance.remoteKotPrint(prntd,
                                kotSummary, kots.get(prgid), mods.get(prgid), false);

                        if (printed) {
                            List<Integer> orderDetailIds = (List<Integer>) orderMap
                                    .get("orderDetailIds");
                            if (orderDetailIds != null && orderDetailIds.size() != 0) {
                                ArrayList<OrderDetail> orderDetails = new ArrayList<OrderDetail>();
                                synchronized (orderDetails) {
                                    for (int i = 0; i < orderDetailIds.size(); i++) {
                                        OrderDetail orderDetail = CPOrderDetailSQL
                                                .getOrderDetail(orderDetailIds
                                                        .get(i));
                                        orderDetail
                                                .setOrderDetailStatus(ParamConst.ORDERDETAIL_STATUS_KOTPRINTERD);
                                        orderDetails.add(orderDetail);
                                    }
                                }
                                CPOrderDetailSQL.addOrderDetailList(orderDetails);
                                LogUtil.e("成功时间", "时间");
                                context.kotPrintStatus(MainPage.KOT_PRINT_SUCCEED,
                                        orderMap.get("orderId"));
                            }
                        } else {
                            context.kotPrintStatus(MainPage.KOT_PRINT_FAILED,
                                    orderMap.get("orderId"));
                        }
                    }
                }
            }

        }
    }

    public void tearDownKotFire(KotSummary kotSummary,
                                ArrayList<KotItemDetail> kot, ArrayList<KotItemModifier> modifiers,
                                String method, Map<String, Object> orderMap) {
        LogUtil.d("开始时间", "时间");
        ArrayList<Integer> printerGrougIds = new ArrayList<Integer>();
        // map printergroudId to Kot: Group ID --> Details
        Map<Integer, ArrayList<KotItemDetail>> kots = new HashMap<Integer, ArrayList<KotItemDetail>>();
        // map printerGroudId to Modifiers
        Map<Integer, ArrayList<KotItemModifier>> mods = new HashMap<Integer, ArrayList<KotItemModifier>>();
        BaseActivity context = App.getTopActivity();
        for (KotItemDetail items : kot) {
            Integer pgid = items.getPrinterGroupId();
            if (pgid.intValue() == 0) {
                if (context != null)
                    context.kotPrintStatus(MainPage.KOT_ITEM_PRINT_NULL,
                            items.getItemName());
                return;
            }
            int kotItemDetailId = items.getId().intValue();

            // Get all Group ids that KOT blongs to
            if (!printerGrougIds.contains(pgid))
                printerGrougIds.add(pgid);

            // kot
            if (kots.containsKey(pgid)) {
                ArrayList<KotItemDetail> tmp = kots.get(pgid);
                tmp.add(items);
            } else {
                ArrayList<KotItemDetail> tmp = new ArrayList<KotItemDetail>();
                tmp.add(items);
                kots.put(pgid, tmp);
            }

            // modifier
            if (mods.containsKey(pgid)) {
                ArrayList<KotItemModifier> tmp = mods.get(pgid);
                for (KotItemModifier mof : modifiers) {
                    if (mof.getKotItemDetailId().intValue() == kotItemDetailId) {
                        tmp.add(mof);
                    }
                }
            } else {
                ArrayList<KotItemModifier> tmp = new ArrayList<KotItemModifier>();
                for (KotItemModifier mof : modifiers) {
                    if (mof.getKotItemDetailId().intValue() == kotItemDetailId) {
                        tmp.add(mof);
                    }
                }
                mods.put(items.getPrinterGroupId(), tmp);
            }
        }


        // add job to send it to KDS
        for (Integer prgid : printerGrougIds) {

            PrinterGroup printerGroup = CoreData.getInstance().getPrinterGroup(prgid);
            List<PrinterGroup> printerGroupAsChildes = CoreData.getInstance()
                    .getPrinterGroupInGroup(printerGroup.getPrinterGroupId());//group printer as child

            List<Printer> printers = new ArrayList<>();

            if (printerGroupAsChildes.size() > 1) {
                for (PrinterGroup pg : printerGroupAsChildes) {
                    printers.addAll(CoreData.getInstance().getPrintersInGroup(pg.getPrinterId()));//printer id is groupId
                }
            } else {
                printers = CoreData.getInstance()
                        .getPrintersInGroup(prgid.intValue());
            }

            for (Printer prnt : printers) {
                // KDS device
                KDSDevice kds1 = App.instance.getKDSDevice(prnt.getId());
                // physical printer
                PrinterDevice prntd = App.instance.getPrinterDeviceById(prnt
                        .getId());
                if (kds1 == null && prntd == null) {
                    if (context != null)
                        context.kotPrintStatus(MainPage.KOT_PRINT_NULL, null);
                    return;
                }
                if (kds1 != null) {
                    KotJob kotjob = new KotJob(kds1, kotSummary,
                            kots.get(prgid), mods.get(prgid), method, orderMap, true);
                    kotJobManager.addJob(kotjob);
                }
                if (prntd != null) {
                    prntd.setGroupId(prgid.intValue());


                    boolean printed = App.instance.remoteKotPrint(prntd,
                            kotSummary, kots.get(prgid), mods.get(prgid), true);

                    if (printed) {
                        List<Integer> orderDetailIds = (List<Integer>) orderMap
                                .get("orderDetailIds");
                        if (orderDetailIds != null && orderDetailIds.size() != 0) {
                            ArrayList<OrderDetail> orderDetails = new ArrayList<OrderDetail>();
                            synchronized (orderDetails) {
                                for (int i = 0; i < orderDetailIds.size(); i++) {
                                    OrderDetailSQL.updateOrderDetailFireStatus(1, orderDetailIds.get(i));
                                }
                            }
                            LogUtil.e("成功时间", "时间");
                            if (context != null)
                                context.kotPrintStatus(MainPage.KOT_PRINT_SUCCEED,
                                        orderMap.get("orderId"));
                        }
                    } else {
                        if (context != null)
                            context.kotPrintStatus(MainPage.KOT_PRINT_FAILED,
                                    orderMap.get("orderId"));
                    }
                }
            }

        }
    }

    /* convert KOT to kot jobs */
    public void transferTableDownKot(String action, KotSummary toKotSummary,
                                     KotSummary fromKotSummary, Map<String, Object> orderMap) {
        transferTableDownKot(action, toKotSummary, fromKotSummary, orderMap, false);
    }

    public void transferTableDownKot(String action, KotSummary toKotSummary,
                                     KotSummary fromKotSummary, Map<String, Object> orderMap, boolean isFromOtherRvc) {

        ArrayList<Printer> printers = new ArrayList<>();
//        ArrayList<KotItemDetail> kotItemDetails = KotItemDetailSQL.getKotItemDetailBySummaryId(fromKotSummary.getId());
        ArrayList<KotItemDetail> kotItemDetails = KotItemDetailSQL.getKotItemDetailByKotSummaryUniqueId(fromKotSummary.getUniqueId());
        KotSummaryLog kotSummaryLogs = new Gson().fromJson(fromKotSummary.getKotSummaryLog(), KotSummaryLog.class);

        //region start loop for all kotItemDetails
        for (KotItemDetail kotItemDetail : kotItemDetails) {

            //region find all printer position
            if (kotItemDetail.getItemType() == ParamConst.ITEMDETAIL_COMBO_ITEM) {
                //region Package item
                ArrayList<KotItemModifier> kotItemModifiers;

                if (isFromOtherRvc) {
                    kotItemModifiers = KotItemModifierSQL.
                            getKotItemModifiersByKotItemDetail(kotItemDetail);
                } else {
                    kotItemModifiers = KotItemModifierSQL.
                            getKotItemModifiersByKotItemDetail(kotItemDetail.getId());
                }

                Map<Integer, ArrayList<KotItemModifier>> modCombo = getComboModifiers(kotItemDetail, kotItemModifiers,
                        new HashMap<Integer, ArrayList<KotItemModifier>>());

                Map<Integer, List<Printer>> mapPrinter = new HashMap<>();

                for (Integer printerGroupId : modCombo.keySet()) {//get all printer from all group
                    mapPrinter.put(printerGroupId, CoreData.getInstance().getPrintersInGroup(printerGroupId));
                }

                //region loop all kds to find the item
                for (KDSTracking kdsTracking : kotSummaryLogs.kdsTrackingList) {

                    KDSDevice kdsDevice;

                    //region check the item is exists in the current kds
                    for (KotItemDetail kid : kdsTracking.kotItemDetails) {
                        if (kid.getId().equals(kotItemDetail.getId()) || kid.getUniqueId().equals(kotItemDetail.getUniqueId())) {

                            kdsDevice = kdsTracking.kdsDevice;//found kds position of item

                            //region check position and status kds
                            boolean isPrinterFound = false;
                            for (Map.Entry<Integer, List<Printer>> map : mapPrinter.entrySet()) {
                                for (Printer printer : map.getValue()) {
                                    //make sure the printer assigned in the item
                                    if (printer.getId().equals(kdsDevice.getDevice_id())) {

                                        Printer nextPrinter = getNextPrinter(map.getKey(), printer.getId());

                                        if (nextPrinter != null)
                                            printer.isShowNext = nextPrinter.isShowNext;

                                        printers.add(printer);

                                        isPrinterFound = true;
                                        break;
                                    }
                                }
                                if (isPrinterFound) break;
                            }
                            //endregion

                            break;
                        }
                    }
                    //endregion
                }
                //endregion
                //endregion

            } else {

                //region item normal
                for (KDSTracking kdsTracking : kotSummaryLogs.kdsTrackingList) {

                    KDSDevice kdsDevice = null;

                    //region check the item is exists in the current kds
                    for (KotItemDetail kid : kdsTracking.kotItemDetails) {
                        if (kid.getId().equals(kotItemDetail.getId()) || kid.getUniqueId().equals(kotItemDetail.getUniqueId())) {

                            kdsDevice = kdsTracking.kdsDevice;//found kds position of item

                            //region check position and status kds
                            ArrayList<Printer> printerList = CoreData.getInstance()
                                    .getPrintersInGroup(kotItemDetail.getPrinterGroupId());

                            for (Printer printer : printerList) {
                                //make sure the printer assigned in the item
                                if (printer.getId().equals(kdsDevice.getDevice_id())) {

                                    Printer nextPrinter = getNextPrinter(kotItemDetail.getPrinterGroupId(), printer.getId());

                                    if (nextPrinter != null)
                                        printer.isShowNext = nextPrinter.isShowNext;

                                    printers.add(printer);
                                    break;
                                }
                            }
                            //endregion
                            break;
                        }
                    }
                    //endregion

                    if (kdsDevice != null) {
                        break; //continue to next item
                    }
                }
                //endregion
            }
            //endregion
        }
        //endregion

        // add job to send it to KDS
        for (Printer printer : printers) {
            KDSDevice kds1 = App.instance.getKDSDevice(printer.getId());
            if (kds1 != null) {

                fromKotSummary.setNext(printer.isShowNext);//don't save to db here

                KotJob kotjob = new KotJob(kds1, action, toKotSummary,
                        fromKotSummary, orderMap);
                kotJobManager.addJob(kotjob);
            }
        }

        boolean ret = transferTableItemToPrinter(fromKotSummary,
                toKotSummary, orderMap, isFromOtherRvc);

        if (!ret) {
            if (App.getTopActivity() != null)
                App.getTopActivity().kotPrintStatus(
                        MainPage.KOT_PRINT_FAILED, null);
        }
    }

    /* transfer table`s item to printer */
    private boolean transferTableItemToPrinter(KotSummary fromKotSummary,
                                               KotSummary toKotSummary, Map<String, Object> orderMap) {
        return transferTableItemToPrinter(fromKotSummary, toKotSummary, orderMap, false);
    }

    private boolean transferTableItemToPrinter(KotSummary fromKotSummary,
                                               KotSummary toKotSummary, Map<String, Object> orderMap, boolean isFromOtherRvc) {
        BaseActivity context = App.getTopActivity();
        ArrayList<Integer> printerGrougIds = new ArrayList<>();
        KotSummary printKotSummary = null;
        Map<Integer, ArrayList<KotItemDetail>> kots = new HashMap<>();
        Map<Integer, ArrayList<KotItemModifier>> mods = new HashMap<>();
        List<KotItemModifier> kotItemModifiers = new ArrayList<>();
        List<KotItemDetail> kotItemDetails = new ArrayList<>();
        String transferAction = (String) orderMap.get("action");

        if (ParamConst.JOB_MERGER_KOT.equals(transferAction)) {

            kotItemDetails = KotItemDetailSQL.getKotItemDetailByKotSummaryUniqueId(fromKotSummary.getUniqueId());

                kotItemModifiers = new ArrayList<>();

                for (KotItemDetail kotItemDetail : kotItemDetails) {
                    if (!isFromOtherRvc) {
                        kotItemDetail.setKotSummaryId(toKotSummary.getId().intValue());
                        kotItemDetail.setOrderId(toKotSummary.getOrderId().intValue());
                        KotItemDetailSQL.update(kotItemDetail);
                    }

                    kotItemModifiers.addAll(KotItemModifierSQL
                            .getKotItemModifiersByKotItemDetail(kotItemDetail));
                }

                KotSummarySQL.deleteKotSummary(fromKotSummary);

                if (context != null && !isFromOtherRvc)
                    context.kotPrintStatus(ParamConst.JOB_TYPE_POS_MERGER_TABLE, null);

            printKotSummary = toKotSummary;

        } else if (ParamConst.JOB_TRANSFER_KOT.equals(transferAction)) {
            KotSummarySQL.update(fromKotSummary);
            Order order = (Order) orderMap.get("fromOrder");

            kotItemDetails = KotItemDetailSQL
                    .getKotItemDetailByKotSummaryUniqueId(fromKotSummary.getUniqueId());

            for (KotItemDetail kotItemDetail : kotItemDetails) {
                kotItemModifiers.addAll(KotItemModifierSQL
                        .getKotItemModifiersByKotItemDetail(kotItemDetail));
            }

            if (context != null && !isFromOtherRvc)
                context.kotPrintStatus(ParamConst.JOB_TYPE_POS_TRANSFER_TABLE, order);
            printKotSummary = KotSummarySQL.getKotSummary(fromKotSummary.getOrderId(), fromKotSummary.getNumTag());
        }

        boolean printed = false;

        if (App.instance.getSystemSettings().isTransferPrint()) {
            for (KotItemDetail items : kotItemDetails) {
                Integer pgid = items.getPrinterGroupId();
                int kotItemDetailId = items.getId().intValue();

                // Get all Group ids that KOT blongs to
                if (!printerGrougIds.contains(pgid))
                    printerGrougIds.add(pgid);

                // kot
                if (kots.containsKey(pgid)) {
                    ArrayList<KotItemDetail> tmp = kots.get(pgid);
                    tmp.add(items);
                } else {
                    ArrayList<KotItemDetail> tmp = new ArrayList<KotItemDetail>();
                    tmp.add(items);
                    kots.put(pgid, tmp);
                }

                // modifier
                if (mods.containsKey(pgid)) {
                    ArrayList<KotItemModifier> tmp = mods.get(pgid);
                    for (KotItemModifier mof : kotItemModifiers) {
                        if (mof.getKotItemDetailId().intValue() == kotItemDetailId) {
                            tmp.add(mof);
                        }
                    }
                } else {
                    ArrayList<KotItemModifier> tmp = new ArrayList<KotItemModifier>();
                    for (KotItemModifier mof : kotItemModifiers) {
                        if (mof.getKotItemDetailId().intValue() == kotItemDetailId) {
                            tmp.add(mof);
                        }
                    }
                    mods.put(items.getPrinterGroupId(), tmp);
                }
            }


            if (App.countryCode == ParamConst.CHINA) {
                return true;
            }
            for (Integer prgid : printerGrougIds) {
                ArrayList<Printer> printers = CoreData.getInstance()
                        .getPrintersInGroup(prgid.intValue());
                for (Printer prnt : printers) {
                    // physical printer
                    PrinterDevice prntd = App.instance.getPrinterDeviceById(prnt
                            .getId());

                    if (prntd != null && printKotSummary != null) {
                        prntd.setGroupId(prgid.intValue());
                        String fromTableName = (String) orderMap.get("fromTableName");
                        String tableTransferFrom = context.getResources().getString(R.string.table_transfer_from);
                        printKotSummary.setDescription(String.format(tableTransferFrom + "", fromTableName));
                        printed = App.instance.remoteKotPrint(prntd, printKotSummary,
                                kots.get(prgid), mods.get(prgid), false);

                    } else {
                        printed = true;
                    }
                }
            }
        } else {
            printed = true;
        }
        return printed;
    }

    /* convert KOT to kot jobs */
    public void transferItemDownKot(KotSummary toKotSummary,
                                    KotSummary fromKotSummary, Map<String, Object> orderMap, KotItemDetail kotItemDetail) {
        transferItemDownKot(toKotSummary, fromKotSummary, orderMap, kotItemDetail, false);
    }

    public void transferItemDownKot(KotSummary toKotSummary,
                                    KotSummary fromKotSummary, Map<String, Object> orderMap, KotItemDetail kotItemDetail, boolean isFromOtherRvc) {

        if (toKotSummary == null || fromKotSummary == null) return;

        ArrayList<Printer> printers = new ArrayList<>();
        KotSummaryLog kotSummaryLogs = new Gson().fromJson(fromKotSummary.getKotSummaryLog(), KotSummaryLog.class);

        if (TextUtils.isEmpty(toKotSummary.getKotSummaryLog())) {
            toKotSummary.setKotSummaryLog(new Gson().toJson(kotSummaryLogs));
        }

        //transfer only one item
        int toCount = toKotSummary.getOrderDetailCount();
        int fromCount = fromKotSummary.getOrderDetailCount();

        //region find all printer position
        if (kotItemDetail.getItemType() == ParamConst.ITEMDETAIL_COMBO_ITEM) {
            //region Package item
            ArrayList<KotItemModifier> kotItemModifiers;

            if (isFromOtherRvc) {
                kotItemModifiers = KotItemModifierSQL.
                        getKotItemModifiersByKotItemDetail(kotItemDetail);
            } else {
                kotItemModifiers = KotItemModifierSQL.
                        getKotItemModifiersByKotItemDetail(kotItemDetail.getId());
            }

            Map<Integer, ArrayList<KotItemModifier>> modCombo = getComboModifiers(kotItemDetail, kotItemModifiers,
                    new HashMap<Integer, ArrayList<KotItemModifier>>());

            Map<Integer, List<Printer>> mapPrinter = new HashMap<>();

            for (Map.Entry<Integer, ArrayList<KotItemModifier>> map : modCombo.entrySet()) {//get all printer from all group
                mapPrinter.put(map.getKey(), CoreData.getInstance().getPrintersInGroup(map.getKey()));

                toCount += map.getValue().size();
                fromCount -= map.getValue().size();
            }

            for (KDSTracking kdsTracking : kotSummaryLogs.kdsTrackingList) {

                KDSDevice kdsDevice;

                //region check the item is exists in the current kds
                boolean isPrinterFound = false;
                for (KotItemDetail kid : kdsTracking.kotItemDetails) {
                    if (kid.getUniqueId().equals(kotItemDetail.getUniqueId())) {

                        kdsDevice = kdsTracking.kdsDevice;//found kds position of item

                        //region check position and status kds
                        for (Map.Entry<Integer, List<Printer>> map : mapPrinter.entrySet()) {
                            for (Printer printer : map.getValue()) {
                                //make sure the printer assigned in the item
                                if (printer.getId().equals(kdsDevice.getDevice_id())) {

                                    Printer nextPrinter = getNextPrinter(map.getKey(), printer.getId());

                                    if (nextPrinter != null)
                                        printer.isShowNext = nextPrinter.isShowNext;

                                    printers.add(printer);

                                    isPrinterFound = true;
                                    break;
                                }
                            }

                            if (isPrinterFound) break;
                        }
                        //endregion
                        break;
                    }
                }
                //endregion
            }

            //endregion

        } else {

            //region item normal
            //transfer only one item
            toCount += 1;
            fromCount -= 1;

            for (KDSTracking kdsTracking : kotSummaryLogs.kdsTrackingList) {

                KDSDevice kdsDevice = null;

                //region check the item is exists in the current kds
                for (KotItemDetail kid : kdsTracking.kotItemDetails) {
                    if (kid.getUniqueId().equals(kotItemDetail.getUniqueId())) {

                        kdsDevice = kdsTracking.kdsDevice;//found kds position of item

                        //region check position and status kds
                        ArrayList<Printer> printerList = CoreData.getInstance()
                                .getPrintersInGroup(kotItemDetail.getPrinterGroupId());

                        for (Printer printer : printerList) {
                            //make sure the printer assigned in the item
                            if (printer.getId().equals(kdsDevice.getDevice_id())) {

                                Printer nextPrinter = getNextPrinter(kotItemDetail.getPrinterGroupId(), printer.getId());

                                if (nextPrinter != null)
                                    printer.isShowNext = nextPrinter.isShowNext;

                                printers.add(printer);
                                break;
                            }
                        }
                        //endregion
                        break;
                    }
                }
                //endregion

                if (kdsDevice != null) {
                    break; //continue to next item
                }
            }
            //endregion
        }
        //endregion

        toKotSummary.setOrderDetailCount(toCount);
        toKotSummary.setOriginalId(toKotSummary.getId());
        fromKotSummary.setOrderDetailCount(fromCount);
        fromKotSummary.setOriginalId(fromKotSummary.getId());

        KotSummarySQL.update(toKotSummary);
        KotSummarySQL.update(fromKotSummary);

        for (Printer prnt : printers) {
            KDSDevice kds1 = App.instance.getKDSDevice(prnt.getId());
            if (kds1 != null) {
                kds1.setKdsType(prnt.getPrinterUsageType());

                fromKotSummary.setNext(prnt.isShowNext);//don't save to db here
                toKotSummary.setNext(prnt.isShowNext);//don't save to db here

                KotJob kotjob = new KotJob(kds1, toKotSummary,
                        fromKotSummary, orderMap, kotItemDetail);
                kotJobManager.addJob(kotjob);
            }
        }

        boolean ret = transferItemToPrinter(fromKotSummary,
                toKotSummary, orderMap, kotItemDetail, isFromOtherRvc);
        if (!ret) {
            if (App.getTopActivity() != null)
                App.getTopActivity().kotPrintStatus(
                        MainPage.KOT_PRINT_FAILED, null);
        }
    }

    private Printer getNextPrinter(int printerGroupId, int currentPrinterId) {
        ArrayList<Printer> printersData = CoreData.getInstance()
                .getPrintersInGroup(printerGroupId);

        boolean isPrinterFound = false;

        for (Printer mPrinter : printersData) {

            if (isPrinterFound) {//next printer
                if (mPrinter.getPrinterUsageType() == Printer.KDS_EXPEDITER) {
                    mPrinter.isShowNext = 1;
                } else {
                    mPrinter.isShowNext = 0;
                }

                return mPrinter;
            }

            if (mPrinter.getId().equals(currentPrinterId))
                isPrinterFound = true;
        }

        return null;
    }

    /* transfer item to printer */
    private boolean transferItemToPrinter(KotSummary fromKotSummary,
                                          KotSummary toKotSummary, Map<String, Object> orderMap, KotItemDetail kotItemDetail) {
        return transferItemToPrinter(fromKotSummary, toKotSummary, orderMap, kotItemDetail, false);
    }

    private boolean transferItemToPrinter(KotSummary fromKotSummary,
                                          KotSummary toKotSummary, Map<String, Object> orderMap, KotItemDetail kotItemDetail, boolean isFromOtherRvc) {
        BaseActivity context = App.getTopActivity();
        ArrayList<Integer> printerGrougIds = new ArrayList<Integer>();
        KotSummary printKotSummary = null;
        // map printergroudId to Kot: Group ID --> Details
        Map<Integer, ArrayList<KotItemDetail>> kots = new HashMap<Integer, ArrayList<KotItemDetail>>();
        // map printerGroudId to Modifiers
        Map<Integer, ArrayList<KotItemModifier>> mods = new HashMap<Integer, ArrayList<KotItemModifier>>();
        List<KotItemModifier> kotItemModifiers = new ArrayList<KotItemModifier>();
        kotItemModifiers.addAll(KotItemModifierSQL
                .getKotItemModifiersByKotItemDetail(kotItemDetail));
        List<KotItemDetail> kotItemDetails = KotItemDetailSQL.getKotItemDetailBySummaryId(fromKotSummary.getId());
        if (kotItemDetails == null || kotItemDetails.size() == 0) {
            KotSummarySQL.deleteKotSummary(fromKotSummary);
        }

        if (context != null && !isFromOtherRvc)
            context.kotPrintStatus(ParamConst.JOB_TYPE_POS_MERGER_TABLE, null);

        printKotSummary = toKotSummary;
        boolean printed = false;
        if (App.instance.getSystemSettings().isTransferPrint()) {
            Integer pgid = kotItemDetail.getPrinterGroupId();
            int kotItemDetailId = kotItemDetail.getId().intValue();

            // Get all Group ids that KOT blongs to
            if (!printerGrougIds.contains(pgid))
                printerGrougIds.add(pgid);

            // kot
            if (kots.containsKey(pgid)) {
                ArrayList<KotItemDetail> tmp = kots.get(pgid);
                tmp.add(kotItemDetail);
            } else {
                ArrayList<KotItemDetail> tmp = new ArrayList<KotItemDetail>();
                tmp.add(kotItemDetail);
                kots.put(pgid, tmp);
            }

            // modifier
            if (mods.containsKey(pgid)) {
                ArrayList<KotItemModifier> tmp = mods.get(pgid);
                for (KotItemModifier mof : kotItemModifiers) {
                    if (mof.getKotItemDetailId().intValue() == kotItemDetailId) {
                        tmp.add(mof);
                    }
                }
            } else {
                ArrayList<KotItemModifier> tmp = new ArrayList<KotItemModifier>();
                for (KotItemModifier mof : kotItemModifiers) {
                    if (mof.getKotItemDetailId().intValue() == kotItemDetailId) {
                        tmp.add(mof);
                    }
                }
                mods.put(kotItemDetail.getPrinterGroupId(), tmp);
            }
            if (App.countryCode == ParamConst.CHINA) {
                return true;
            }
            for (Integer prgid : printerGrougIds) {
                ArrayList<Printer> printers = CoreData.getInstance()
                        .getPrintersInGroup(prgid.intValue());
                for (Printer prnt : printers) {
                    // physical printer
                    PrinterDevice prntd = App.instance.getPrinterDeviceById(prnt
                            .getId());
                    if (prntd != null) {
                        prntd.setGroupId(prgid.intValue());
                        String fromTableName = (String) orderMap.get("fromTableName");
                        String tableTransferFrom = context.getResources().getString(R.string.table_transfer_from);
                        printKotSummary.setDescription(String.format(tableTransferFrom + "", fromTableName));
                        printed = App.instance.remoteKotPrint(prntd, printKotSummary,
                                kots.get(prgid), mods.get(prgid), false);

                    } else {
                        printed = true;
                    }
                }
            }
        } else {
            printed = true;
        }
        return printed;
    }

    public void clear() {
        this.kotJobManager.clear();
    }

    public void stop() {
        this.kotJobManager.stop();
    }
}
