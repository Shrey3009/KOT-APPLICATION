package com.alfredkds.global;

import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;

import com.alfredbase.BaseActivity;
import com.alfredbase.BaseApplication;
import com.alfredbase.ParamConst;
import com.alfredbase.UnCEHandler;
import com.alfredbase.global.BugseeHelper;
import com.alfredbase.javabean.KotItemDetail;
import com.alfredbase.javabean.KotItemModifier;
import com.alfredbase.javabean.KotSummary;
import com.alfredbase.javabean.Printer;
import com.alfredbase.javabean.User;
import com.alfredbase.javabean.model.KDSDevice;
import com.alfredbase.javabean.model.MainPosInfo;
import com.alfredbase.javabean.model.SessionStatus;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.Store;
import com.alfredbase.store.sql.KotItemDetailSQL;
import com.alfredbase.store.sql.KotItemModifierSQL;
import com.alfredbase.store.sql.KotSummarySQL;
import com.alfredbase.utils.CommonUtil;
import com.alfredbase.utils.LogUtil;
import com.alfredbase.utils.RingUtil;
import com.alfredkds.R;
import com.alfredkds.activity.Welcome;
import com.alfredkds.http.server.KdsHttpServer;
import com.alfredkds.javabean.Kot;
import com.alfredkds.view.SystemSettings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class App extends BaseApplication {

    public static final int HANDLER_RECONNECT_POS = 10;
    public static final int HANDLER_SEND_FAILURE = 11;
    public static final int HANDLER_SEND_FAILURE_SHOW = 12;
    public static final int HANDLER_REFRESH_KOT = 13;
    public static final int HANDLER_VERIFY_MAINPOS = 14;
    public static final int HANDLER_RETURN_ERROR = 15;
    public static final int HANDLER_RETURN_ERROR_SHOW = 16;
    public static final int HANDLER_KOTSUMMARY_IS_UNREAL = 17;
    public static final int HANDLER_KOT_COMPLETE_USER_FAILED = 18;
    public static final int HANDLER_KOT_ITEM_CALL = 19;

    public static final int HANDLER_KOT_CALL_NUM = 101;
    public static final int HANDLER_KOT_CALL_NUM_OLD = 100;
    public static final int HANDLER_KOT_COMPLETE_ALL = 102;
    public static final int HANDLER_KOT_COMPLETE = 103;
    public static final int HANDLER_KOT_NEXT = 104;
    public static final int HANDLER_KOT_NEXT_SUCCESS = 105;
    public static final int HANDLER_KOT_NEXT_FAILED = 106;

    public static final int HANDLER_NEXT_KOT = 22;
    public static final int HANDLER_TMP_KOT = 21;
    public static final int HANDLER_NEW_KOT = 20;
    public static final int HANDLER_UPDATE_KOT = 8;
    public static final int HANDLER_DELETE_KOT = 2;
    public static final int HANDLER_REFRESH_LOG = 107;
    private static final String DATABASE_NAME = "com.alfredkds";
    public static final int HANDLER_RELOAD_KOT = 23;
    public static App instance;
    //for pairing
    private List<String> pairingIps = new ArrayList<>();
    private User user;
    private KDSDevice kdsDevice;
    private Map<Integer, MainPosInfo> mainPosInfos = new HashMap<>();
    private Integer currentMainPosId = -1;
    public RingUtil ringUtil;
    private Printer printer;
    public static String TAG = App.class.getName();

    private SessionStatus sessionStatus;
    private Long businessDate;
    private SystemSettings systemSettings;
    public String VERSION = "0.0.0";
    private Map<Integer, KDSDevice> kdsDeviceRVCMap = new HashMap<>();
    private Map<Integer, Integer> rvcIdentifier = new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        SQLExe.init(this, DATABASE_NAME, DATABASE_VERSION);
        BugseeHelper.init(this, "856f76df-ba87-4e0a-9049-cfcd31a33a42");

        systemSettings = new SystemSettings(this);
        update15to16();
        if (kdsDevice == null) {
            kdsDevice = new KDSDevice();
        }

        VERSION = getAppVersionName();


        KdsHttpServer kdssrv = new KdsHttpServer();
        try {
            if (!kdssrv.isAlive()) {
                kdssrv.start();
            }
        } catch (IOException e) {
            kdssrv.stop();
        }
        UnCEHandler catchExcep = new UnCEHandler(this, Welcome.class);
        Thread.setDefaultUncaughtExceptionHandler(catchExcep);
        CrashReport.initCrashReport(getApplicationContext(), "900043724", isOpenLog);
        wifiPolicyNever();
        kdsDevice = Store.getObject(this, Store.KDS_DEVICE, KDSDevice.class);
    }

    public void setRing() {
        ringUtil = new RingUtil();
        ringUtil.init(this, R.raw.desk_bell_ringing);
    }

//	protected void updateKDSNetworkInfo(String ip, String mac) {
//		
//		this.kdsDevice.setIP(ip);
//		this.kdsDevice.setMac(mac);
//		Store.saveObject(this, Store.KDS_DEVICE, this.kdsDevice);
//	}

    public boolean isBalancer() {
        return getKdsDevice() != null && getKdsDevice().getKdsType() == Printer.KDS_BALANCER;
    }

    @Override
    protected void onIPChanged() {
        super.onIPChanged();
//        if (isBalancer()) {
        if (getCurrentConnectedMainPosList().size() <= 0 || Store.getObject(App.instance, Store.KDS_USER, User.class) == null) {
            return;
        }
//        } else {
//            if (getCurrentConnectedMainPos() == null || Store.getObject(App.instance, Store.KDS_USER, User.class) == null) {
//                return;
//            }
//        }

        if (getKdsDevice() != null) {
            this.kdsDevice.setIP(CommonUtil.getLocalIpAddress());
            this.setKdsDevice(kdsDevice);
            LogUtil.d(TAG, "onIPChanged: " + this.kdsDevice.toString());
            SyncCentre.getInstance().kdsIpChange(this.getApplicationContext(),
                    this.kdsDevice.toMap(), null);
        }

    }

    @Override
    protected void onNetworkConnectionUpdate() {
        super.onNetworkConnectionUpdate();
        SyncCentre.getInstance().setNetworkStatus(this.network_connected);
    }


    public User getUser() {
        if (user == null) {
            user = Store.getObject(instance, Store.KDS_USER, User.class);
        }
        return user;
    }

    public Map<Integer, KDSDevice> getKdsDeviceRVCMap() {
        return kdsDeviceRVCMap;
    }

    public void setKdsDeviceRVCMap(Integer id, KDSDevice kdsDevice) {
        if (!kdsDeviceRVCMap.containsKey(id)) {
            kdsDeviceRVCMap.put(id, kdsDevice);
        }
    }

    public void setUser(User user) {
        this.user = user;
    }

    public KDSDevice getKdsDevice() {
        if (kdsDevice == null) {
            kdsDevice = Store.getObject(this, Store.KDS_DEVICE, KDSDevice.class);
        }
        return kdsDevice;
    }

    public void setKdsDevice(KDSDevice kdsDevice) {
        this.kdsDevice = kdsDevice;
        Store.saveObject(this, Store.KDS_DEVICE, kdsDevice);
    }

    public SystemSettings getSystemSettings() {
        return systemSettings;
    }

    public synchronized Map<Integer, MainPosInfo> getMainPosInfos() {

        if (mainPosInfos.isEmpty()) {
            Map<Integer, MainPosInfo> mainPosInfoMap = Store.getObject(instance, Store.MAINPOSINFO_MAP, new TypeToken<Map<Integer, MainPosInfo>>() {
            }.getType());
            if (mainPosInfoMap != null) {
                mainPosInfos = mainPosInfoMap;
            }
        }
        return mainPosInfos;
    }

    public synchronized void addMainPosInfo(MainPosInfo mainPosInfo) {
        getMainPosInfos();
        this.mainPosInfos.put(mainPosInfo.getRevenueId(), mainPosInfo);
        currentMainPosId = mainPosInfo.getRevenueId();
        Store.saveObject(instance, Store.MAINPOSINFO_MAP, this.mainPosInfos);
        Store.putInt(this, Store.CURRENT_MAIN_POS_ID_CONNECTED, currentMainPosId);

        List<Integer> connectedRVCIds = getRVCConnectedId();
        if (connectedRVCIds == null) {
            connectedRVCIds = new ArrayList<>();
        }

        if (!connectedRVCIds.contains(currentMainPosId)) {
            connectedRVCIds.add(currentMainPosId);
            Store.saveObject(instance, Store.CURRENT_MAIN_POS_IDS_CONNECTED, connectedRVCIds);
        }
    }

    public Integer getRvcIdentifier(int rvcId) {
        if (rvcIdentifier.size() <= 0) {
            int i = 0;

            for (int rvcId1 : getRVCConnectedId()) {
                rvcIdentifier.put(rvcId1, i);
                i++;
            }
        }

        return rvcIdentifier.get(rvcId);
    }

    public MainPosInfo getCurrentConnectedMainPos() {
        getMainPosInfos();
        Integer cid = Store.getInt(this, Store.CURRENT_MAIN_POS_ID_CONNECTED);
        if (cid != null && cid.intValue() > 0) {
            return this.mainPosInfos.get(cid.intValue());
        } else {
            return null;
        }

    }

    public MainPosInfo getCurrentConnectedMainPos(int rvcId) {
        getMainPosInfos();
        return this.mainPosInfos.get(rvcId);
    }

    public List<Integer> getRVCConnectedId() {
        return Store.getObject(this, Store.CURRENT_MAIN_POS_IDS_CONNECTED,
                new TypeToken<List<Integer>>() {
                }.getType());
    }

    public List<MainPosInfo> getCurrentConnectedMainPosList() {
        getMainPosInfos();
        List<Integer> cids = getRVCConnectedId();
        List<MainPosInfo> mainPosInfoList = new ArrayList<>();

        if (cids != null) {
            for (Integer cid : cids) {
                mainPosInfoList.add(this.mainPosInfos.get(cid));
            }
        }

        return mainPosInfoList;
    }

    public Printer getPrinter() {
        if (printer == null) {
            printer = Store.getObject(this, Store.KDS_PRINTER, Printer.class);
        }
        return printer;
    }

    public void setPrinter(Printer printer) {
        this.printer = printer;
        Store.saveObject(this, Store.KDS_PRINTER, printer);
    }

    public List<String> getAllPairingIp() {
        return pairingIps;
    }

    public void setAllPairingIp(List<String> pairingIps) {
        this.pairingIps = pairingIps;
    }

    public String getPairingIp() {
        if (pairingIps.size() > 0)
            return pairingIps.get(0);
        return "";
    }

    public void setPairingIp(String pairingIp) {
        this.pairingIps.clear();
        this.pairingIps.add(pairingIp);
    }

    public SessionStatus getSessionStatus() {
        if (sessionStatus == null) {
            sessionStatus = Store.getObject(instance, Store.SESSION_STATUS, SessionStatus.class);
        }
        return sessionStatus;
    }

    public void setSessionStatus(SessionStatus sessionStatus) {
        this.sessionStatus = sessionStatus;
    }

    public Long getBusinessDate() {
        if (businessDate == null) {
            businessDate = Store.getLong(this, Store.BUSINESS_DATE);
        }
        return businessDate;
    }

    public void setBusinessDate(Long businessDate) {
        this.businessDate = businessDate;
    }

    /**
     * 每次刷新KitchenOrder，从数据库取出数据
     */
    public List<Kot> getRefreshKots() {
        List<Kot> kotList = new ArrayList<>();
        Kot kot;
        boolean flag = false;
        List<KotSummary> kotSummaries;
        List<KotItemDetail> kotItemDetails;
        kotSummaries = KotSummarySQL.getUndoneKotSummary();
//        List<KotItemModifier> kotItemModifiers = new ArrayList<>();

        for (int i = 0; i < kotSummaries.size(); i++) {

            kot = new Kot();
            kotItemDetails = KotItemDetailSQL.getKotItemDetailBySummaryIdandOrderIdForMainPage(
                    kotSummaries.get(i).getRevenueCenterId(), kotSummaries.get(i).getUniqueId(),
                    kotSummaries.get(i).getOrderId());

            boolean isPlaceOrder = false;
            List<KotItemModifier> kotItemModifiers = new ArrayList<>();

            for (int j = 0; j < kotItemDetails.size(); j++) {
                if (kotSummaries.get(i).getStatus() == ParamConst.KOTS_STATUS_UNDONE) {
                    flag = true;
                }

                if (kotItemDetails.get(j).getKotStatus() != ParamConst.KOT_STATUS_TMP) {
                    isPlaceOrder = true;
                }

                kotItemModifiers.addAll(KotItemModifierSQL.getKotItemModifiersByKotItemDetail(kotItemDetails.get(j)));
            }

            if (flag) {
                kot.setKotItemDetails(kotItemDetails);
                kot.setKotItemModifiers(kotItemModifiers);
                kot.setKotSummary(kotSummaries.get(i));
                kot.setPlaceOrder(isPlaceOrder);
                kotList.add(kot);
                flag = false;
            }
        }
        return kotList;
    }

    /**
     * KitchenOrder--Login初始化数据填充Adapter
     *
     * @return
     */
    public List<Kot> getInitKots() {
        List<Kot> kotList = new ArrayList<Kot>();
        Kot kot = null;
        boolean flag = false;
        List<KotSummary> kotSummaries = new ArrayList<KotSummary>();
        List<KotItemDetail> kotItemDetails = new ArrayList<KotItemDetail>();
        kotSummaries = KotSummarySQL.getAllKotSummary();
        List<KotItemModifier> kotItemModifiers = new ArrayList<KotItemModifier>();
        for (int i = 0; i < kotSummaries.size(); i++) {
            kot = new Kot();
            kotItemDetails = KotItemDetailSQL.getKotItemDetailBySummaryIdandOrderId(
                    kotSummaries.get(i).getRevenueCenterId(),
                    kotSummaries.get(i).getId(),
                    kotSummaries.get(i).getOrderId());
            for (int j = 0; j < kotItemDetails.size(); j++) {
                if (kotItemDetails.get(j).getKotStatus() < ParamConst.KOT_STATUS_DONE
                        || kotSummaries.get(i).getStatus() == ParamConst.KOTS_STATUS_UNDONE) {
                    flag = true;
                }
                kotItemModifiers.addAll(KotItemModifierSQL.getKotItemModifiersByKotItemDetail(kotItemDetails.get(j)));
            }
            if (flag) {
                kot.setKotItemDetails(kotItemDetails);
                kot.setKotItemModifiers(kotItemModifiers);
                kot.setKotSummary(kotSummaries.get(i));
                kotList.add(kot);
                flag = false;
            }
        }
        return kotList;
    }

    /**
     * 查询出所有的kot信息
     * <p>
     * added by
     *
     * @return
     */
    public List<Kot> getAllKots() {
        List<Kot> kotList = new ArrayList<Kot>();
        Kot kot = null;
        List<KotSummary> kotSummaries = new ArrayList<KotSummary>();
        List<KotItemDetail> kotItemDetails = new ArrayList<KotItemDetail>();
        kotSummaries = KotSummarySQL.getAllKotSummary();
        List<KotItemModifier> kotItemModifiers = new ArrayList<KotItemModifier>();
        for (int i = 0; i < kotSummaries.size(); i++) {
            kot = new Kot();
            kotItemDetails = KotItemDetailSQL.getKotItemDetailBySummaryIdRvcId(kotSummaries.get(i).getId(), kotSummaries.get(i).getRevenueCenterId());
            for (int j = 0; j < kotItemDetails.size(); j++) {
                kotItemModifiers.addAll(KotItemModifierSQL.getKotItemModifiersByKotItemDetail(kotItemDetails.get(j)));
            }
            kot.setKotItemDetails(kotItemDetails);
            kot.setKotItemModifiers(kotItemModifiers);
            kot.setKotSummary(kotSummaries.get(i));
            kotList.add(kot);
        }
        return kotList;
    }

    /**
     * 数据库取数据，初始化KotHistory及refresh页面
     * <p>
     * added by
     *
     * @return
     */
    public List<Object[]> getKotHistoryData() {
        List<Object[]> kotHistoryItems = new ArrayList<Object[]>();
//		List<KotItemDetail> kotItemDetails = KotItemDetailSQL.getAllKotItemDetail();
        List<KotItemDetail> kotItemDetails = KotItemDetailSQL.getHistoryKotItemDetail();
        List<KotItemModifier> kotItemModifiers = null;
        for (int i = 0; i < kotItemDetails.size(); i++) {
            KotSummary kotSummary = KotSummarySQL.getKotSummaryById(kotItemDetails.get(i).getKotSummaryId());
            kotItemModifiers = KotItemModifierSQL.getKotItemModifiersByKotItemDetail(kotItemDetails.get(i));
            String kotItem = "";
            if (kotItemModifiers.size() == 0) {
                kotItem = kotItemDetails.get(i).getItemName();
            } else {
                kotItem = kotItemDetails.get(i).getItemName() + "(";
                for (int j = 0; j < kotItemModifiers.size(); j++) {
                    kotItem += kotItemModifiers.get(j).getModifierName() + "、";
                }
                kotItem = kotItem.substring(0, kotItem.length() - 1) + ")";
            }
            Object[] obj = new Object[3];
            obj[0] = kotSummary;
            obj[1] = kotItemDetails.get(i);
            obj[2] = kotItem;
            kotHistoryItems.add(obj);
        }
        return kotHistoryItems;
    }

    /**
     * Summary ：菜名初始化
     * <p>
     * added by
     *
     * @return
     */
    public List<String> getDishNames() {
        List<String> result = new ArrayList<String>();
        List<KotItemDetail> kotItemDetails = KotItemDetailSQL.getAllKotItemDetail();
        for (int i = 0; i < kotItemDetails.size(); i++) {
            result.add(kotItemDetails.get(i).getItemName());
        }
        Set<String> kotHashSet = new HashSet<String>(result);//排序
        result.clear();
        result.addAll(kotHashSet);
        return result;
    }

    /**
     * Summary ： 每个菜有多少个及详细信息
     * <p>
     * added by XieJF, 2014-8-14
     *
     * @param dishName
     * @return
     */
    public List<Object[]> getKotDishDetail(String dishName) {
        List<Object[]> result = new ArrayList<Object[]>();
        List<KotItemDetail> kotItemDetails = KotItemDetailSQL.getKotItemDetailByDishName(dishName);
        List<KotItemModifier> kotItemModifiers = null;
        for (int i = 0; i < kotItemDetails.size(); i++) {
            kotItemModifiers = KotItemModifierSQL.getKotItemModifiersByKotItemDetail(kotItemDetails.get(i));
            String kotItem = "";
            if (kotItemModifiers.size() == 0) {
                kotItem = kotItemDetails.get(i).getItemName();
            } else {
                kotItem = kotItemDetails.get(i).getItemName() + "(";
                for (int j = 0; j < kotItemModifiers.size(); j++) {
                    kotItem += kotItemModifiers.get(j).getModifierName() + "、";
                }
                kotItem = kotItem.substring(0, kotItem.length() - 1) + ")";
            }
            KotSummary kotSummary = KotSummarySQL.getKotSummaryById(kotItemDetails.get(i).getKotSummaryId());
            Object[] obj = new Object[3];
            obj[0] = kotSummary;
            obj[1] = kotItemDetails.get(i);
            obj[2] = kotItem;
            result.add(obj);
        }
        return result;
    }

    /**
     * Summary中popupWindow数据
     *
     * @param kotSummary
     * @return
     */
    public Kot getKot(KotSummary kotSummary) {
        Kot kot = new Kot();
        List<KotItemDetail> kotItemDetails = KotItemDetailSQL.getKotItemDetailBySummaryIdRvcId(kotSummary.getId(), kotSummary.getRevenueCenterId());
        List<KotItemModifier> kotItemModifiers = new ArrayList<KotItemModifier>();
        for (int j = 0; j < kotItemDetails.size(); j++) {
            kotItemModifiers.addAll(KotItemModifierSQL.getKotItemModifiersByKotItemDetail(kotItemDetails.get(j)));
        }
        kot.setKotSummary(kotSummary);
        kot.setKotItemDetails(kotItemDetails);
        kot.setKotItemModifiers(kotItemModifiers);
        return kot;
    }

    public void reload(BaseActivity context, Handler handler) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("employee_ID", Store.getString(context, Store.EMPLOYEE_ID));
        parameters.put("password", Store.getString(context, Store.PASSWORD));
        parameters.put("type", ParamConst.USER_TYPE_KOT);
        parameters.put("device", App.instance.getKdsDevice());

        List<MainPosInfo> connectedMainPos = new ArrayList<>();
//        if (isBalancer()) {
        connectedMainPos = App.instance.getCurrentConnectedMainPosList();
//        } else {
//            connectedMainPos.add(App.instance.getCurrentConnectedMainPos());
//        }

        if (connectedMainPos.size() > 0) {
            for (MainPosInfo mainPosInfo : connectedMainPos) {
                SyncCentre.getInstance().login(context, mainPosInfo.getIP(), parameters, handler);
            }
        } else {
            UIHelp.showToast(context, context.getResources().getString(R.string.reconn_pos));
        }
    }

    /**
     * 判断是否使用的是商米设备  商米副屏设置是否展示
     */
    public boolean isSUNMIShow() {
        String brand = Build.BRAND;
        String model = Build.MODEL;
        String manufacturer = Build.MANUFACTURER;
        LogUtil.d(TAG, brand + "**************" + model);
        if ("SUNMI".equals(brand.toUpperCase())) {
            return true;
        }
        return false;
    }

    public int getBalancerMode() {
        SystemSettings settings = App.instance.getSystemSettings();
        int mode = settings.getBalancerMode();
        int selectedMode = SystemSettings.MODE_NORMAL;

        if (mode == SystemSettings.MODE_BALANCE) {
            if (settings.getBalancerTime(0) > 0) {
                if (settings.isBalancerTimeHasCome() && !settings.isBalancerTimeEnded())
                    selectedMode = SystemSettings.MODE_BALANCE;
            } else {
                selectedMode = SystemSettings.MODE_BALANCE;
            }
        } else if (mode == SystemSettings.MODE_STACK) {
            int stackCount = settings.getStackCount();

            if (stackCount > 0) {
                if (settings.getBalancerTime(0) > 0) {
                    if (settings.isBalancerTimeHasCome() && !settings.isBalancerTimeEnded())
                        selectedMode = SystemSettings.MODE_STACK;
                } else {
                    selectedMode = SystemSettings.MODE_STACK;
                }
            }
        }

        return selectedMode;
    }
}