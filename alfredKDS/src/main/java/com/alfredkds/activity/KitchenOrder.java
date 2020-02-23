package com.alfredkds.activity;

import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.BaseApplication;
import com.alfredbase.LoadingDialog;
import com.alfredbase.ParamConst;
import com.alfredbase.global.CoreData;
import com.alfredbase.http.ResultCode;
import com.alfredbase.javabean.KotItemDetail;
import com.alfredbase.javabean.KotItemModifier;
import com.alfredbase.javabean.KotSummary;
import com.alfredbase.javabean.Printer;
import com.alfredbase.javabean.model.MainPosInfo;
import com.alfredbase.store.sql.KotItemDetailSQL;
import com.alfredbase.utils.DialogFactory;
import com.alfredbase.utils.IntegerUtils;
import com.alfredbase.utils.ScreenSizeUtil;
import com.alfredbase.utils.TextTypeFace;
import com.alfredbase.utils.TimeUtil;
import com.alfredbase.utils.ViewTouchUtil;
import com.alfredkds.R;
import com.alfredkds.global.App;
import com.alfredkds.global.SyncCentre;
import com.alfredkds.global.UIHelp;
import com.alfredkds.javabean.Kot;
import com.alfredkds.javabean.KotItem;
import com.alfredkds.view.FinishQtyWindow;
import com.alfredkds.view.KOTArrayAdapter;
import com.alfredkds.view.KOTArrayLanAdapter;
import com.alfredkds.view.PendingListAdapter;
import com.alfredkds.view.PopItemAdapter;
import com.alfredkds.view.PopItemListView;
import com.alfredkds.view.PopItemListView.RemoveDirection;
import com.alfredkds.view.PopItemListView.RemoveListener;
import com.alfredkds.view.SwipeItemLayout;
import com.alfredkds.view.TopBarView;
import com.alfredkds.view.Type;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KitchenOrder extends BaseActivity {
    public static final int HANDLER_TRANSFER_KOT = 3;
    public static final int HANDLER_MERGER_KOT = 4;

    private RecyclerView ll_orders, rcvPendingList;    //水平列表
    public KOTArrayAdapter adapter;
    public KOTArrayLanAdapter madapter;
    private List<Kot> kots = new ArrayList<Kot>();
    private PendingListAdapter pendingListAdapter;

    private TopBarView topBarView;    //页面顶部view

    private IntentFilter filter;
    private KotSummary kotSummary;
    private TextTypeFace textTypeFace;
    private FinishQtyWindow finishQtyPop;
    private List<MainPosInfo> mainPosInfo = new ArrayList<>();
    private boolean doubleBackToExitPressedOnce = false;
    private TextView tv_order_qyt;
    private List<KotItem> kotItems = new ArrayList<KotItem>();

    private LinearLayout li_title;
    Kot kot;
    KotAdapter kadapter;

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_kitchen_order);
        loadingDialog = new LoadingDialog(context);
        loadingDialog.setTitle(context.getResources().getString(R.string.loading));
        mainPosInfo = App.instance.getCurrentConnectedMainPosList();
        App.instance.setRing();

//		filter = new IntentFilter();
//		filter.addAction(Intent.ACTION_TIME_TICK);
//		registerReceiver(receiver, filter);

        //ll_progress_list = (LinearLayout) findViewById(R.id.ll_progress_list);
        //initProgressList();
        ll_orders = (RecyclerView) findViewById(R.id.ll_orders);
        rcvPendingList = (RecyclerView) findViewById(R.id.rcvPendingList);
        tv_order_qyt = (TextView) findViewById(R.id.tv_order_qyt);
        li_title = (LinearLayout) findViewById(R.id.li_lan_title);

        pendingListAdapter = new PendingListAdapter(this, getKotItemsData());
        rcvPendingList.setLayoutManager(new LinearLayoutManager(this));
        rcvPendingList.setAdapter(pendingListAdapter);

        finishQtyPop = new FinishQtyWindow(context, findViewById(R.id.rl_root), handler);

        if (App.instance.getSystemSettings().isPendingList()) {
            rcvPendingList.setVisibility(View.VISIBLE);
        } else {
            rcvPendingList.setVisibility(View.GONE);
        }

        initTopBarView();
        initTextTypeFace();
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private List<KotItem> getKotItemsData() {
        kots = App.instance.getRefreshKots();
        Map<Integer, KotItem> map = new HashMap<>();

        for (KotItem kotItem : getKotItem(kots)) {
            if (map.containsKey(kotItem.getItemId())) {
                int qty = map.get(kotItem.getItemId()).getQty() + kotItem.getQty();
                map.get(kotItem.getItemId()).setQty(qty);
            } else {
                map.put(kotItem.getItemId(), kotItem);
            }
        }

        return new ArrayList<>(map.values());
    }

    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (pendingListAdapter != null) {
                pendingListAdapter.setData(getKotItemsData());
            }

            switch (msg.what) {
                case ResultCode.USER_NO_PERMIT:
                    loadingDialog.dismiss();
                    UIHelp.showToast(context, context.getResources().getString(R.string.pairing_fails));
                    break;
                case App.HANDLER_NEW_KOT:
                    kots = App.instance.getRefreshKots();
                    if (App.instance.getSystemSettings().isKdsLan()) {
                        madapter.setKots(getKotItem(kots));
                        madapter.setAddFirstItem(true);
                        madapter.notifyDataSetChanged();

                        tv_order_qyt.setText(kotItems.size() + "");
                    } else {
                        adapter.setKots(kots);
                        adapter.setAddFirstItem(true);
                        adapter.notifyDataSetChanged();

                        tv_order_qyt.setText(kots.size() + "");
                    }

                    if (itemPopupWindow != null && itemPopupWindow.isShowing()) {
                        kadapter.setKot(App.instance.getKot(kotSummary));
                        kadapter.notifyDataSetChanged();
                    }
                    break;
                case App.HANDLER_NEXT_KOT:
                    kots = App.instance.getRefreshKots();
                    if (App.instance.getSystemSettings().isKdsLan()) {
                        madapter.setKots(getKotItem(kots));
                        madapter.setAddFirstItem(true);
                        madapter.notifyDataSetChanged();

                        tv_order_qyt.setText(kotItems.size() + "");
                    } else {
                        adapter.setKots(kots);
                        adapter.setAddFirstItem(true);
                        adapter.notifyDataSetChanged();

                        tv_order_qyt.setText(kots.size() + "");
                    }

                    if (itemPopupWindow != null && itemPopupWindow.isShowing()) {
                        kadapter.setKot(App.instance.getKot(kotSummary));
                        kadapter.notifyDataSetChanged();
                    }
                    break;
                case App.HANDLER_TMP_KOT:
                    kots = App.instance.getRefreshKots();

                    if (App.instance.getSystemSettings().isKdsLan()) {
                        madapter.setKots(getKotItem(kots));
                        madapter.setAddFirstItem(true);
                        madapter.notifyDataSetChanged();

                        tv_order_qyt.setText(kotItems.size() + "");
                    } else {
                        adapter.setKots(kots);
                        adapter.setAddFirstItem(true);
                        adapter.notifyDataSetChanged();

                        tv_order_qyt.setText(kots.size() + "");
                    }

                    if (itemPopupWindow != null && itemPopupWindow.isShowing()) {
                        kadapter.setKot(App.instance.getKot(kotSummary));
                        kadapter.notifyDataSetChanged();
                    }
                    break;
                case App.HANDLER_UPDATE_KOT:
                    kots = App.instance.getRefreshKots();
                    if (App.instance.getSystemSettings().isKdsLan()) {
                        madapter.setKots(getKotItem(kots));
                        madapter.notifyDataSetChanged();
                        tv_order_qyt.setText(kotItems.size() + "");
                    } else {
                        adapter.setKots(kots);
                        adapter.notifyDataSetChanged();
                        tv_order_qyt.setText(kots.size() + "");
                    }

                    if (itemPopupWindow != null && itemPopupWindow.isShowing()) {
                        kadapter.setKot(App.instance.getKot(kotSummary));
                        kadapter.notifyDataSetChanged();
                    }
                    break;
                case App.HANDLER_DELETE_KOT:
                    refresh();
                    break;
                case App.HANDLER_RECONNECT_POS:
                    loadingDialog.dismiss();
                    DialogFactory.commonTwoBtnDialog(context, "", getString(R.string.reconnect_pos),
                            getString(R.string.cancel), getString(R.string.ok), null,
                            new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    UIHelp.startConnectPOS(context);
                                    finish();
                                }
                            });
                    break;
                case App.HANDLER_SEND_FAILURE:
                    loadingDialog.dismiss();
                    if (App.instance.getSystemSettings().isKdsLan()) {
                        madapter.notifyDataSetChanged();
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                    //  adapter.notifyDataSetChanged();
                    refresh();
                    break;
                case App.HANDLER_RETURN_ERROR:
                    UIHelp.showToast(context, ResultCode.getErrorResultStrByCode(context, (Integer) msg.obj, null));
                    refresh();
                    break;
                case App.HANDLER_SEND_FAILURE_SHOW:
                    UIHelp.showToast(context, context.getResources().getString(R.string.send_failed));
                    break;

                case App.HANDLER_REFRESH_KOT:
                    dismissLoadingDialog();
                    refresh();
                    break;
                case HANDLER_TRANSFER_KOT:
                    refresh();
                    break;
                case HANDLER_MERGER_KOT:
                    refresh();
                    break;
                case ResultCode.CONNECTION_FAILED:
                    loadingDialog.dismiss();
                    UIHelp.showToast(context, ResultCode.getErrorResultStr(context, (Throwable) msg.obj,
                            context.getResources().getString(R.string.revenue_center)));
                    break;
                case App.HANDLER_VERIFY_MAINPOS:
                    UIHelp.showToast(context, context.getResources().getString(R.string.other_pos_data));
                    break;
                case App.HANDLER_KOTSUMMARY_IS_UNREAL:
                    loadingDialog.dismiss();
                    UIHelp.showToast(context, context.getResources().getString(R.string.order_discarded));
                    //kots = App.instance.getRefreshKots();
                    List<Kot> kots = App.instance.getRefreshKots();
                    if (App.instance.getSystemSettings().isKdsLan()) {
                        madapter.setKots(getKotItem(kots));
                        madapter.notifyDataSetChanged();
                    } else {
                        adapter.setKots(kots);
                        adapter.notifyDataSetChanged();
                    }

                    if (itemPopupWindow != null) {
                        if (kots.isEmpty()) {
                            itemPopupWindow.dismiss();
                        }
                        if (itemPopupWindow != null && itemPopupWindow.isShowing()) {
                            itemPopupWindow.dismiss();
                        }
                    }
                    break;
                case App.HANDLER_KOT_COMPLETE_USER_FAILED:
                    App.instance.reload(context, handler);
                    break;
                case Login.HANDLER_LOGIN:
                    refresh();
                    break;
                case ResultCode.SUCCESS:
                    loadingDialog.dismiss();


                    break;

                case App.HANDLER_KOT_ITEM_CALL:
                    if (loadingDialog != null) {
                        kots = App.instance.getRefreshKots();
                        //kots = App.instance.getInitKots();
                        loadingDialog.dismiss();

                        if (App.instance.getSystemSettings().isKdsLan()) {
                            madapter.setKots(getKotItem(kots));
                            madapter.notifyDataSetChanged();
                        } else {
                            adapter.setKots(kots);
                            adapter.notifyDataSetChanged();
                        }
//                        madapter.setKots(getKotItem(kots));
//                        madapter.notifyDataSetChanged();
                        tv_order_qyt.setText(kotItems.size() + "");

                    }

                    break;
                case App.HANDLER_KOT_CALL_NUM: {
                    KotItem kotItem = (KotItem) msg.obj;
                    String str = kotItem.getNumTag() + IntegerUtils.formatLocale(kotItem.getRevenueCenterIndex(), kotItem.getOrderNo() + "");
                    String numTag = kotItem.getNumTag();

                    int id = msg.arg2;
                    if (!TextUtils.isEmpty(str)) {
                        loadingDialog.show();
                        Map<String, Object> parameters = new HashMap<String, Object>();
                        parameters.put("callNumber", str);
                        parameters.put("numTag", numTag);

                        Printer printer = App.instance.getPrinter();
                        if (printer != null) {
                            parameters.put("printerGroupId", printer.getId());
                            parameters.put("printerName", printer.getPrinterName());
                        }

                        SyncCentre.getInstance().callSpecifyNum(KitchenOrder.this, App.instance.getCurrentConnectedMainPos(), parameters, handler, kotItem.getKotItemDetailUniqueId());
                    } else {
                        UIHelp.showToast(KitchenOrder.this, getString(R.string.order_number_cannot_empty));
                    }
                }
                break;
                case App.HANDLER_KOT_CALL_NUM_OLD: {
                    Kot kot = (Kot) msg.obj;
                    KotSummary k = kot.getKotSummary();
                    String str = k.getNumTag() + IntegerUtils.formatLocale(k.getRevenueCenterIndex(), k.getOrderNo() + "");
                    String numTag = k.getNumTag();
                    int id = msg.arg2;
                    if (!TextUtils.isEmpty(str)) {
                        loadingDialog.show();
                        Map<String, Object> parameters = new HashMap<String, Object>();
                        parameters.put("callNumber", str);
                        parameters.put("numTag", numTag);

                        Printer printer = App.instance.getPrinter();
                        if (printer != null) {
                            parameters.put("printerGroupId", printer.getId());
                            parameters.put("printerName", printer.getPrinterName());
                        }

                        SyncCentre.getInstance().callSpecifyNum(KitchenOrder.this, App.instance.getCurrentConnectedMainPos(), parameters, handler, null);
                    } else {
                        UIHelp.showToast(KitchenOrder.this, getString(R.string.order_number_cannot_empty));
                    }
                }
                break;
                case App.HANDLER_KOT_COMPLETE_ALL:
                    Bundle bundle = msg.getData();
                    final KotSummary kotSummary = (KotSummary) bundle.getSerializable("kotSummary");
                    String title = getResources().getString(R.string.warning);
                    String content = getResources().getString(R.string.complete_all_item);
                    String left = getResources().getString(R.string.no);
                    String right = getResources().getString(R.string.yes);
                    DialogFactory.commonTwoBtnDialog(KitchenOrder.this, title, content, left, right, null, new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            List<KotItemDetail> itemDetails = new ArrayList<KotItemDetail>();
                            for (KotItemDetail kotItemDetail : App.instance.getKot(kotSummary).getKotItemDetails()) {
                                kotItemDetail.setFinishQty(kotItemDetail.getUnFinishQty());
                                kotItemDetail.setUnFinishQty(0);
                                kotItemDetail.setKotStatus(ParamConst.KOT_STATUS_DONE);
                                KotItemDetailSQL.update(kotItemDetail);
                                itemDetails.add(kotItemDetail);
                            }
                            Map<String, Object> parameters = new HashMap<String, Object>();
                            parameters.put("kotSummary", kotSummary);
                            parameters.put("kotItemDetails", itemDetails);
                            parameters.put("kdsId", App.instance.getKdsDevice().getDevice_id());
                            parameters.put("type", 1);
                            parameters.put("userKey", CoreData.getInstance().getUserKey(kotSummary.getRevenueCenterId()));

                            SyncCentre.getInstance().kotComplete(KitchenOrder.this,
                                    App.instance.getCurrentConnectedMainPos(kotSummary.getRevenueCenterId()), parameters, handler, -1);
                            loadingDialog.show();
                        }
                    });
                    break;

                case App.HANDLER_KOT_COMPLETE:
                    Bundle bundle1 = msg.getData();
                    final KotSummary kotSummary1 = (KotSummary) bundle1.getSerializable("kotSummary");
                    final int kotItemDetailId = bundle1.getInt("kotItemDetailId");

                    final int kotItemId = bundle1.getInt("id");
                    String title1 = getResources().getString(R.string.warning);
                    String content1 = context.getString(R.string.complete_all_item);
                    String left1 = getResources().getString(R.string.no);
                    String right1 = getResources().getString(R.string.yes);
                    DialogFactory.commonTwoBtnDialog(KitchenOrder.this, title1, content1, left1, right1, null, new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            List<KotItemDetail> itemDetails = new ArrayList<KotItemDetail>();
                            for (KotItemDetail kotItemDetail : App.instance.getKot(kotSummary1).getKotItemDetails()) {

                                if (kotItemDetail.getId() == kotItemDetailId) {
                                    kotItemDetail.setFinishQty(kotItemDetail.getUnFinishQty());
                                    kotItemDetail.setUnFinishQty(0);
                                    kotItemDetail.setKotStatus(ParamConst.KOT_STATUS_DONE);
//									kotItemDetail.setFinishQty(kotItemDetail.getFinishQty()+1);
//									kotItemDetail.setUnFinishQty(kotItemDetail.getUnFinishQty()-1);
//									if(kotItemDetail.getUnFinishQty()==0) {
//										kotItemDetail.setKotStatus(ParamConst.KOT_STATUS_DONE);
//									}

                                    KotItemDetailSQL.update(kotItemDetail);
                                    itemDetails.add(kotItemDetail);
                                }
                            }
                            Map<String, Object> parameters = new HashMap<String, Object>();
                            parameters.put("kotSummary", kotSummary1);
                            parameters.put("kotItemDetails", itemDetails);
                            parameters.put("kdsId", App.instance.getKdsDevice().getDevice_id());
                            parameters.put("type", 1);
                            parameters.put("userKey", CoreData.getInstance().getUserKey(kotSummary1.getRevenueCenterId()));
                            SyncCentre.getInstance().kotComplete(KitchenOrder.this,
                                    App.instance.getCurrentConnectedMainPos(kotSummary1.getRevenueCenterId()), parameters, handler, 1);
                            loadingDialog.show();
                        }
                    });
                case App.HANDLER_KOT_NEXT:
                    //region next kds
                    Bundle bundle2 = msg.getData();
                    String kotStr = bundle2.getString("kot");
                    String kidStr = bundle2.getString("kotItemDetails");
                    String kimStr = bundle2.getString("kotItemModifiers");

                    final Kot kot = new Gson().fromJson(kotStr, Kot.class);
                    if (kot == null) return;

                    final List<KotItemDetail> kotItemDetails = new Gson().fromJson(kidStr, new TypeToken<List<KotItemDetail>>() {
                    }.getType());
                    final List<KotItemModifier> kotItemModifiers = new Gson().fromJson(kimStr, new TypeToken<List<KotItemModifier>>() {
                    }.getType());

                    String titleNext = getResources().getString(R.string.warning);
                    String contentNext = "Are you sure bring to next kds?";
                    String leftNext = getResources().getString(R.string.no);
                    String rightNext = getResources().getString(R.string.yes);
                    DialogFactory.commonTwoBtnDialog(KitchenOrder.this, titleNext, contentNext,
                            leftNext, rightNext, null, new OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Map<String, Object> parameters = new HashMap<>();
                                    parameters.put("kotSummary", kot.getKotSummary());
//                                    parameters.put("kotItemDetails", kot.getKotItemDetails());
                                    parameters.put("kotItemDetails", kotItemDetails);
//                                    parameters.put("kotModifiers", kot.getKotItemModifiers());
                                    parameters.put("kotModifiers", kotItemModifiers);
                                    parameters.put("kdsId", App.instance.getKdsDevice().getDevice_id());
                                    parameters.put("type", 1);

                                    MainPosInfo mainPosInfo = App.instance.getCurrentConnectedMainPos(kot.getKotSummary().getRevenueCenterId());

                                    if (mainPosInfo != null) {
                                        parameters.put("userKey", CoreData.getInstance().getUserKey(mainPosInfo.getRevenueId()));
                                        SyncCentre.getInstance().kotNextKDS(KitchenOrder.this,
                                                mainPosInfo, parameters, handler, -1);

                                        loadingDialog.show();
                                    }
                                }
                            });
                    //endregion
                    break;
                case App.HANDLER_KOT_NEXT_SUCCESS:
                    loadingDialog.dismiss();
                    kots = App.instance.getRefreshKots();
                    if (App.instance.getSystemSettings().isKdsLan()) {
                        madapter.setKots(getKotItem(kots));
                        madapter.setAddFirstItem(true);
                        madapter.notifyDataSetChanged();

                        tv_order_qyt.setText(kotItems.size() + "");
                    } else {
                        adapter.setKots(kots);
                        adapter.setAddFirstItem(true);
                        adapter.notifyDataSetChanged();

                        tv_order_qyt.setText(kots.size() + "");
                    }

                    if (itemPopupWindow != null && itemPopupWindow.isShowing()) {
                        kadapter.setKot(App.instance.getKot(KitchenOrder.this.kotSummary));
                        kadapter.notifyDataSetChanged();
                    }
                    break;
                case App.HANDLER_KOT_NEXT_FAILED:
                    loadingDialog.dismiss();
                    refresh();
                    break;
                default:
                    break;
            }
        }

    };

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public void refresh() {
        if (loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
        List<Kot> kots = App.instance.getRefreshKots();
        if (App.instance.getSystemSettings().isKdsLan()) {
            madapter.setKots(getKotItem(kots));
            madapter.notifyDataSetChanged();
        } else {
            adapter.setKots(kots);
            adapter.notifyDataSetChanged();
        }

        tv_order_qyt.setText(kots.size() + "");
        if (kots.isEmpty()) {
//			itemPopupWindow.dismiss();
            if (itemPopupWindow != null && itemPopupWindow.isShowing()) {
                itemPopupWindow.dismiss();
            }
        }
        if (itemPopupWindow != null && itemPopupWindow.isShowing()) {
            Kot kot = App.instance.getKot(kotSummary);
            kadapter.setKot(App.instance.getKot(kotSummary));
            kadapter.notifyDataSetChanged();
            ArrayList<KotItemDetail> list = new ArrayList<KotItemDetail>();
            if (list != null && list.size() != 0) {
                list.clear();
            }
            for (int i = 0; i < kot.getKotItemDetails().size(); i++) {
                KotItemDetail kotItemDetail = kot.getKotItemDetails().get(i);
                if (kotItemDetail.getKotStatus() == ParamConst.KOT_STATUS_DONE) {
                    list.add(kotItemDetail);
                }
            }

            if (list.size() == kot.getKotItemDetails().size()) {
                if (itemPopupWindow != null && itemPopupWindow.isShowing()) {
                    itemPopupWindow.dismiss();
                }
            }
        }
    }

    @Override
    public void httpRequestAction(int action, Object obj) {
        handler.sendMessage(handler.obtainMessage(action, obj));
    }

//	private BroadcastReceiver receiver = new BroadcastReceiver() {
//
//		@Override
//		public void onReceive(Context arg0, Intent arg1) {
//			adapter.notifyDataSetChanged();
//		}
//	}


    @Override
    protected void onResume() {
        if (App.instance.getSystemSettings().isKdsLan()) {
            li_title.setVisibility(View.VISIBLE);
            //设置为横屏
            if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        } else {
            li_title.setVisibility(View.GONE);
            if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        }

        doubleBackToExitPressedOnce = false;

        initKOTList();
//		if(App.instance.getSystemSettings().isKdsLan()){
//			kots=App.instance.getRefreshKots();
//			kots.clear();
//			kotItems=	getKotItem();
//			madapter.setKots(kotItems);
//			madapter.notifyDataSetChanged();
//		}else {
//
//		}

        if (itemPopupWindow != null && itemPopupWindow.isShowing()) {
            kadapter.setKot(App.instance.getKot(kotSummary));
            kadapter.notifyDataSetChanged();
        }
        super.onResume();
    }

    private List<KotItem> getKotItem(List<Kot> kotlist) {
        kotItems.clear();
        for (int i = 0; i < kotlist.size(); i++) {
            Kot kot = kotlist.get(i);
            List<KotItemDetail> detailList = kot.getKotItemDetails();
            if (detailList != null && detailList.size() > 0) {
                for (int j = 0; j < detailList.size(); j++) {
                    KotItemDetail kotItemDetail = detailList.get(j);
                    if (kotItemDetail.getKotStatus() < ParamConst.KOT_STATUS_DONE) {
                        int unFinishQty = kotItemDetail.getUnFinishQty();
                        KotItem item = new KotItem();

                        if (kotItemDetail.getKotStatus() != ParamConst.KOT_STATUS_TMP) {
                            item.setPlaceOrder(true);
                        }

                        KotSummary kotSummary = kot.getKotSummary();
                        item.setOrderNo(kotSummary.getOrderNo());
                        if (TextUtils.isEmpty(kotSummary.getTableName())) {
                            item.setTableName("");
                        } else {
                            item.setTableName(kotSummary.getTableName());
                        }

                        item.setSummaryId(kotSummary.getId());
                        item.setNumTag(kotSummary.getNumTag());
                        item.setRevenueCenterIndex(kotSummary.getRevenueCenterIndex());
                        StringBuffer sBuffer = new StringBuffer();
                        sBuffer.append("");
                        item.setKotStatus(kotItemDetail.getKotStatus());
                        if (TextUtils.isEmpty(kotItemDetail.getItemName())) {
                            item.setItemDetailName("");
                        } else {
                            String itemName = kotItemDetail.getItemName();
                            if (kotSummary.getIsTakeAway() == ParamConst.TAKE_AWAY || kotItemDetail.getIsTakeAway() == ParamConst.TAKE_AWAY) {
                                itemName = itemName + "(Take Away)";
                            }
                            item.setItemDetailName(itemName);
                        }

                        item.setUpdateTime(kotItemDetail.getUpdateTime());
                        item.setCallType(kotItemDetail.getCallType());
                        item.setQty(unFinishQty);
                        item.setItemDetailId(kotItemDetail.getId());
                        item.setKotItemDetailUniqueId(kotItemDetail.getUniqueId());
                        item.setItemId(kotItemDetail.getItemId());
                        List<KotItemModifier> itemModifierlist = kot.getKotItemModifiers();
                        if (itemModifierlist != null && itemModifierlist.size() > 0) {
                            for (int s = 0; s < itemModifierlist.size(); s++) {
                                KotItemModifier kotItemModifier = itemModifierlist.get(s);
                                if (kotItemModifier != null
                                        && kotItemDetail.getId().intValue() == kotItemModifier.getKotItemDetailId().intValue()) {
                                    sBuffer.append(kotItemModifier.getModifierName() + ";");

                                }
                            }
                            item.setItemModName(sBuffer.toString());
                        }
                        kotItems.add(item);
                    }
                }
            }

        }
        return kotItems;

    }

    private void initTopBarView() {
        topBarView = (TopBarView) findViewById(R.id.tv_title);
    }

    @Override
    protected void onDestroy() {
//		unregisterReceiver(receiver);
        super.onDestroy();
    }

    private void initKOTList() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        if (App.instance.getSystemSettings().isKdsLan()) {
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        } else {
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        }

        ll_orders.setLayoutManager(linearLayoutManager);
        // Assign adapter to the HorizontalListView
        if (App.instance.getSystemSettings().isKdsLan()) {

            madapter = new KOTArrayLanAdapter(context, handler);
            //	Kot k=new Kot();
            //kots.add(k);
            kots = App.instance.getRefreshKots();
            //kots = App.instance.getInitKots();
            madapter.setKots(getKotItem(kots));
            ll_orders.setAdapter(madapter);
            tv_order_qyt.setText(kotItems.size() + "");
        } else {

            adapter = new KOTArrayAdapter(context, handler);
            kots = App.instance.getRefreshKots();
            //	kots = App.instance.getInitKots();
            adapter.setKots(kots);
            ll_orders.setAdapter(adapter);
            tv_order_qyt.setText(kots.size() + "");

            if (pendingListAdapter == null) {
                pendingListAdapter = new PendingListAdapter(context, getKotItemsData());
                rcvPendingList.setAdapter(pendingListAdapter);
            }

            if (App.instance.getSystemSettings().isPendingList()) {
                rcvPendingList.setVisibility(View.VISIBLE);
            } else {
                rcvPendingList.setVisibility(View.GONE);
            }

            pendingListAdapter.setData(getKotItemsData());
        }

    }


    @Override
    public void handlerClickEvent(View v) {
        super.handlerClickEvent(v);
        switch (v.getId()) {
            case R.id.iv_complete:
                if (itemPopupWindow != null && itemPopupWindow.isShowing()) {
                    kots = App.instance.getRefreshKots();

                    if (App.instance.getSystemSettings().isKdsLan()) {
                        madapter.setKots(getKotItem(kots));

                        madapter.notifyDataSetChanged();
                    } else {
                        adapter.setKots(kots);
                        adapter.notifyDataSetChanged();
                    }

                    itemPopupWindow.dismiss();
//				refresh();
                }
                break;
            case R.id.iv_back:
                if (itemPopupWindow != null && itemPopupWindow.isShowing()) {
                    if (App.instance.getSystemSettings().isKdsLan()) {
                        madapter.notifyDataSetChanged();
                    } else {
                        adapter.notifyDataSetChanged();
                    }

                    itemPopupWindow.dismiss();
                }
                break;
            case R.id.ll_bottom: {
                String title = getResources().getString(R.string.warning);
                String content = getResources().getString(R.string.complete_all_item);
                String left = getResources().getString(R.string.no);
                String right = getResources().getString(R.string.yes);
                DialogFactory.commonTwoBtnDialog(this, title, content, left, right, null, new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        List<KotItemDetail> itemDetails = new ArrayList<KotItemDetail>();
                        for (KotItemDetail kotItemDetail : App.instance.getKot(kotSummary).getKotItemDetails()) {
                            kotItemDetail.setFinishQty(kotItemDetail.getUnFinishQty());
                            kotItemDetail.setUnFinishQty(0);
                            kotItemDetail.setKotStatus(ParamConst.KOT_STATUS_DONE);
                            KotItemDetailSQL.update(kotItemDetail);
                            itemDetails.add(kotItemDetail);
                        }
                        Map<String, Object> parameters = new HashMap<String, Object>();
                        parameters.put("kotSummary", kotSummary);
                        parameters.put("kotItemDetails", itemDetails);
                        parameters.put("type", 1);
                        parameters.put("kdsId", App.instance.getKdsDevice().getDevice_id());
                        parameters.put("userKey", CoreData.getInstance().getUserKey(kotSummary.getRevenueCenterId()));
                        SyncCentre.getInstance().kotComplete(KitchenOrder.this,
                                App.instance.getCurrentConnectedMainPos(kotSummary.getRevenueCenterId()), parameters, handler, -1);
                        loadingDialog.show();


                    }
                });
            }
            break;
            default:
                break;
        }
    }

    private PopupWindow itemPopupWindow;
    private PopItemListView popItemListView;
    private PopItemAdapter popItemAdapter;
    private RecyclerView reKot;

    public void showOrderItem(final KotSummary kotSummary) {
        kot = App.instance.getKot(kotSummary);
        this.kotSummary = kotSummary;
        View view = getLayoutInflater().inflate(R.layout.kitche_order_item_popupwindow, null);
        ImageView iv_back = (ImageView) view.findViewById(R.id.iv_back);
        ImageView iv_complete = (ImageView) view.findViewById(R.id.iv_complete);
        iv_back.setOnClickListener(this);

        iv_complete.setOnClickListener(this);
        ViewTouchUtil.expandViewTouchDelegate(iv_back);
        ViewTouchUtil.expandViewTouchDelegate(iv_complete);
        view.findViewById(R.id.ll_bottom).setOnClickListener(this);
        //kotTop
        TextView kotId = (TextView) view.findViewById(R.id.tv_kot_id);
        TextView orderId = (TextView) view.findViewById(R.id.tv_order_id);
        TextView table = (TextView) view.findViewById(R.id.tv_table);
        TextView posName = (TextView) view.findViewById(R.id.tv_pos);
        TextView date = (TextView) view.findViewById(R.id.tv_date);
        TextView time = (TextView) view.findViewById(R.id.tv_time);
        TextView tv_kds_delivery = (TextView) view.findViewById(R.id.tv_kds_delivery);
        LinearLayout kitchen_ll_orderRemark = (LinearLayout) view.findViewById(R.id.kitchen_ll_orderRemark);
        TextView kitchen_tv_orderremark = (TextView) view.findViewById(R.id.kitchen_tv_orderremark);
        kitchen_tv_orderremark.setMovementMethod(ScrollingMovementMethod.getInstance());

        TextView tv_kiosk_order_id = (TextView) view.findViewById(R.id.tv_kiosk_order_id);
        MainPosInfo pos = App.instance.getCurrentConnectedMainPos(kotSummary.getRevenueCenterId());
        if (pos.getIsKiosk() == ParamConst.MAINPOSINFO_IS_KIOSK) {
            tv_kiosk_order_id.setVisibility(View.VISIBLE);
            orderId.setVisibility(View.GONE);
        } else {
            tv_kiosk_order_id.setVisibility(View.GONE);
            orderId.setVisibility(View.VISIBLE);
        }

        if (kot.getKotSummary().getEatType() == ParamConst.APP_ORDER_DELIVERY) {
            StringBuffer deliverys = new StringBuffer();
            tv_kds_delivery.setVisibility(View.VISIBLE);
            deliverys.append(context.getResources().getString(R.string.delivery) + "\n");
            if (!TextUtils.isEmpty(kot.getKotSummary().getAddress())) {
                deliverys.append(kot.getKotSummary().getAddress() + "\n");
            }
            if (!TextUtils.isEmpty(kot.getKotSummary().getContact())) {
                deliverys.append(kot.getKotSummary().getContact() + "\n");
            }
            if (!TextUtils.isEmpty(kot.getKotSummary().getMobile())) {
                deliverys.append(kot.getKotSummary().getMobile() + "\n");
            }
            if (!TextUtils.isEmpty(kot.getKotSummary().getDeliveryTime() + "")) {
                deliverys.append(TimeUtil.getDeliveryDataTime(kot.getKotSummary().getDeliveryTime()) + "");
            }
//            if(!TextUtils.isEmpty(kot.getKotSummary().getOrderRemark()+"")){
//                deliverys.append(kot.getKotSummary().getOrderRemark());
//            }
            tv_kds_delivery.setText(deliverys.toString());
        } else {
            tv_kds_delivery.setVisibility(View.GONE);
        }
        kotId.setText(kot.getKotSummary().getId() + "");
        orderId.setText(context.getResources().getString(R.string.order_no) + kot.getKotSummary().getNumTag() + kot.getKotSummary().getOrderNo() + "");
        tv_kiosk_order_id.setText(context.getResources().getString(R.string.order_no) + kot.getKotSummary().getNumTag() + IntegerUtils.formatLocale(kot.getKotSummary().getRevenueCenterIndex(), kot.getKotSummary().getOrderNo() + ""));
        table.setText(context.getResources().getString(R.string.table) + " - " + kot.getKotSummary().getTableName() + "");
        posName.setText(kot.getKotSummary().getRevenueCenterName() + "");
        date.setText(TimeUtil.getPrintDate(kot.getKotSummary().getCreateTime()));
        time.setText(TimeUtil.getPrintTime(kot.getKotSummary().getCreateTime()));

        String remark = kot.getKotSummary().getOrderRemark();
        if (TextUtils.isEmpty(remark)) {
            kitchen_ll_orderRemark.setVisibility(View.GONE);
        } else {
            kitchen_ll_orderRemark.setVisibility(View.VISIBLE);
            kitchen_tv_orderremark.setText(remark);
        }

//		textTypeFace.setTrajanProBlod(kotId);
//		textTypeFace.setTrajanProRegular(orderId);
//		textTypeFace.setTrajanProRegular(table);
//		textTypeFace.setTrajanProRegular(posName);
//		textTypeFace.setTrajanProRegular(date);
//		textTypeFace.setTrajanProRegular(time);
//		textTypeFace.setTrajanProRegular(tv_kiosk_order_id);
//		textTypeFace.setTrajanProRegular(kitchen_tv_orderremark);
//		textTypeFace.setTrajanProRegular(kitchen_tv_remark);

        popItemListView = (PopItemListView) view.findViewById(R.id.lv_kot);
        popItemAdapter = new PopItemAdapter(context);
        popItemAdapter.setKot(kot);
        popItemListView.setAdapter(popItemAdapter);
        reKot = (RecyclerView) view.findViewById(R.id.re_kot);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        reKot.setLayoutManager(linearLayoutManager);
//        DividerItemDecoration dividerItemDecoration =
//                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
//        reKot.addItemDecoration(dividerItemDecoration);

        kadapter = new KotAdapter(mItemTouchListener);
        kadapter.setKot(kot);
        reKot.setAdapter(kadapter);
//        popItemListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
//
//                DialogFactory.commonTwoBtnDialog(context, "Waring", "Out of stock ?",
//                        getString(R.string.cancel), getString(R.string.ok), new OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//                            }
//                        },
//                        new OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                int   orderDetailId =   kot.getKotItemDetails().get(position).getOrderDetailId();
//                                Map<String, Object> parameters = new HashMap<String, Object>();
//                                parameters.put("orderDetailId",orderDetailId);
//
//                                SyncCentre.getInstance().updateRemainingStock(context,
//                                        App.instance.getCurrentConnectedMainPos(), parameters, handler);
//                            }
//                        });
//                return true;
//            }
//        });
        popItemListView.setRemoveListener(new RemoveListener() {
            @Override
            public void removeItem(RemoveDirection direction, final int position) {
                PopItemAdapter currentPopItems = (PopItemAdapter) popItemListView.getAdapter();
                Kot popKot = currentPopItems.getKot();
				/*
				kotItemDetail.setFinishQty(kotItemDetail.getUnFinishQty());
				kotItemDetail.setUnFinishQty(0);
				kotItemDetail.setKotStatus(ParamConst.KOT_STATUS_DONE);
				KotItemDetailSQL.update(kotItemDetail);
				 */
                switch (direction) {
                    case LEFT:
                        if (position >= popKot.getKotItemDetails().size()) {
//						return;
                        }
                        if (popKot.getKotItemDetails().get(position).getKotStatus() < ParamConst.KOT_STATUS_DONE
                                && popKot.getKotItemDetails().get(position).getCategoryId() == ParamConst.KOTITEMDETAIL_CATEGORYID_MAIN) {
                            KotItemDetail kotItemDetail = popKot.getKotItemDetails().get(position);
                            if (kotItemDetail.getUnFinishQty() > 1) {
                                finishQtyPop.show(kotItemDetail.getUnFinishQty() + "", kotSummary, kotItemDetail, loadingDialog);
                                return;
                            }
                            loadingDialog.show();
                            kotItemDetail.setKotStatus(ParamConst.KOT_STATUS_DONE);
                            kotItemDetail.setUnFinishQty(0);
                            kotItemDetail.setFinishQty(1);
                            KotItemDetailSQL.update(kotItemDetail);
                            List<KotItemDetail> itemDetails = new ArrayList<KotItemDetail>();
                            itemDetails.add(kotItemDetail);
                            Map<String, Object> parameters = new HashMap<String, Object>();
                            parameters.put("kotSummary", popKot.getKotSummary());
                            parameters.put("kotItemDetails", itemDetails);
                            parameters.put("kdsId", App.instance.getKdsDevice().getDevice_id());
                            parameters.put("userKey", CoreData.getInstance().getUserKey(popKot.getKotSummary().getRevenueCenterId()));
                            SyncCentre.getInstance().kotComplete(context,
                                    App.instance.getCurrentConnectedMainPos(popKot.getKotSummary().getRevenueCenterId()), parameters, handler, -1);
                        }

                        break;
                    case RIGHT:

                        DialogFactory.commonTwoBtnDialog(context, context.getString(R.string.warning), context.getString(R.string.out_of_stock),
                                getString(R.string.cancel), getString(R.string.ok), new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                },
                                new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        int orderDetailId = kot.getKotItemDetails().get(position).getOrderDetailId();
                                        Map<String, Object> parameters = new HashMap<String, Object>();
                                        parameters.put("orderDetailId", orderDetailId);

                                        List<MainPosInfo> connectedMainPos = App.instance.getCurrentConnectedMainPosList();
                                        for (MainPosInfo mainPosInfo : connectedMainPos) {
                                            parameters.put("userKey", CoreData.getInstance().getUserKey(mainPosInfo.getRevenueId()));

                                            SyncCentre.getInstance().updateRemainingStock(context,
                                                    App.instance.getCurrentConnectedMainPos(), parameters, handler);
                                        }
                                    }
                                });
                        break;

                    default:
                        break;
                }
            }
        });

//		TextTypeFace.setTypeface(context, (TextView) view.findViewById(R.id.tv_order_item_name));
        itemPopupWindow = new PopupWindow(view,
                LinearLayout.LayoutParams.MATCH_PARENT,
                (int) (ScreenSizeUtil.height - ScreenSizeUtil
                        .getStatusBarHeight(context)));
        itemPopupWindow.setFocusable(true);
        if (itemPopupWindow != null && !itemPopupWindow.isShowing())
            itemPopupWindow.showAtLocation(findViewById(R.id.ll_orders),
                    Gravity.TOP | Gravity.LEFT, 0,
                    ScreenSizeUtil.getStatusBarHeight(context));
    }

    private void initTextTypeFace() {
        textTypeFace = TextTypeFace.getInstance();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        UIHelp.showToast(this, context.getResources().getString(R.string.exit_program));

        BaseApplication.postHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("KitchenOrder Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }


    ItemTouchListener mItemTouchListener = new ItemTouchListener() {
        @Override
        public void onItemClick(String str) {

        }

        @Override
        public void onLeftMenuClick(String str) {
            final int position = Integer.valueOf(str).intValue();


            DialogFactory.commonTwoBtnDialog(context, getString(R.string.warning), getString(R.string.out_of_stock),
                    getString(R.string.cancel), getString(R.string.ok), new OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    },
                    new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int orderDetailId = kot.getKotItemDetails().get(position).getOrderDetailId();
                            Map<String, Object> parameters = new HashMap<String, Object>();
                            parameters.put("orderDetailId", orderDetailId);

                            List<MainPosInfo> connectedMainPos = App.instance.getCurrentConnectedMainPosList();
                            for (MainPosInfo mainPosInfo : connectedMainPos) {
                                parameters.put("userKey", CoreData.getInstance().getUserKey(mainPosInfo.getRevenueId()));

                                SyncCentre.getInstance().updateRemainingStock(context,
                                        App.instance.getCurrentConnectedMainPos(), parameters, handler);
                            }
                        }
                    });


        }

        @Override
        public void onRightMenuClick(String str) {
            int position = Integer.valueOf(str).intValue();
            KotAdapter currentPopItems = (KotAdapter) reKot.getAdapter();
            final Kot popKot = currentPopItems.getKot();

            if (position >= popKot.getKotItemDetails().size()) {
//						return;
            }
            if (popKot.getKotItemDetails().get(position).getKotStatus() < ParamConst.KOT_STATUS_DONE
                    && popKot.getKotItemDetails().get(position).getCategoryId() == ParamConst.KOTITEMDETAIL_CATEGORYID_MAIN) {
                KotItemDetail kotItemDetail = popKot.getKotItemDetails().get(position);
                if (kotItemDetail.getUnFinishQty() > 1) {
                    finishQtyPop.show(kotItemDetail.getUnFinishQty() + "", kotSummary, kotItemDetail, loadingDialog);
                    return;
                }
                loadingDialog.show();
                kotItemDetail.setKotStatus(ParamConst.KOT_STATUS_DONE);
                kotItemDetail.setUnFinishQty(0);
                kotItemDetail.setFinishQty(1);
                KotItemDetailSQL.update(kotItemDetail);
                List<KotItemDetail> itemDetails = new ArrayList<KotItemDetail>();
                itemDetails.add(kotItemDetail);
                final Map<String, Object> parameters = new HashMap<String, Object>();
                parameters.put("kotSummary", popKot.getKotSummary());
                parameters.put("kotItemDetails", itemDetails);
                parameters.put("kdsId", App.instance.getKdsDevice().getDevice_id());
                parameters.put("userKey", CoreData.getInstance().getUserKey(popKot.getKotSummary().getRevenueCenterId()));
                SyncCentre.getInstance().kotComplete(context,
                        App.instance.getCurrentConnectedMainPos(popKot.getKotSummary().getRevenueCenterId()),
                        parameters, handler, -1);
            }
        }
    };


    private static class KotAdapter extends RecyclerView.Adapter<SimpleViewHolder> {

        private ItemTouchListener mItemTouchListener;
        //  private List<Data> mData;
        public Kot kot;

        KotAdapter(ItemTouchListener itemTouchListener) {
            // this.kot = data;
            this.mItemTouchListener = itemTouchListener;
        }


        public Kot getKot() {
            return kot;
        }

        public void setKot(Kot kot) {
            this.kot = kot;
        }

        @Override
        public int getItemViewType(int position) {
            return 0;
        }

        @Override
        public int getItemCount() {
            return kot.getKotItemDetails().size();
        }

        @Override
        public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            @LayoutRes
            int layout;
            layout = R.layout.item_left_and_right_menu;

//            switch (viewType) {
////                case Type.LEFT_MENU:
////                    layout = R.layout.item_left_menu;
////                    break;
////                case Type.RIGHT_MENU:
////                    layout = R.layout.item_right_menu;
////                    break;
//                case Type.LEFT_AND_RIGHT_MENU:
//                    layout = R.layout.item_left_and_right_menu;
//                    break;
////                case Type.LEFT_LONG_MENU:
////                    layout = R.layout.item_left_long_menu;
////                    break;
////                case Type.RIGHT_LONG_MENU:
////                    layout = R.layout.item_right_long_menu;
////                    break;
////                case Type.LEFT_AND_RIGHT_LONG_MENU:
////                    layout = R.layout.item_left_and_right_long_menu;
////                    break;
////                case Type.DISABLE_SWIPE_MENU:
////                    layout = R.layout.item_disable_swipe_menu;
////                    break;
////                default:
////                    layout = R.layout.item_left_menu;
////                    break;
//            }
            View rootView = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
            return new SimpleViewHolder(rootView);
        }

        @Override
        public void onBindViewHolder(final SimpleViewHolder holder, int position) {
            KotItemDetail kotItemDetail = kot.getKotItemDetails().get(position);
            holder.mContent.setText(kotItemDetail.getItemName() + "");
            holder.num.setText(kotItemDetail.getUnFinishQty() + "");
            //holder.itemName.setText(kotItemDetail.getItemName());
            // holder.mContent.setText(mData.get(position).content.concat(" " + position));
            holder.mSwipeItemLayout.setSwipeEnable(getItemViewType(position) != Type.DISABLE_SWIPE_MENU);
            if (kotItemDetail.getKotStatus() == ParamConst.KOT_STATUS_DONE) {
                holder.itemView.setBackgroundResource(R.color.bg_complete_item);
                holder.mContent.getPaint().setFlags(0);
            } else if (kotItemDetail.getKotStatus() == ParamConst.KOT_STATUS_VOID) {
                holder.itemView.setBackgroundResource(R.color.white);
                holder.mContent.setPaintFlags(holder.mContent.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else if (kotItemDetail.getFireStatus() == 1) {
                holder.itemView.setBackgroundResource(R.color.viewfinder_laser);
            } else {
                if (kotItemDetail.getKotStatus() == ParamConst.KOT_STATUS_UPDATE) {
                    holder.itemView.setBackgroundResource(R.color.bg_update_item);
                    holder.mContent.getPaint().setFlags(0);
                } else {
                    holder.itemView.setBackgroundResource(R.color.white);
                    holder.mContent.getPaint().setFlags(0);
                }
            }

            /*---kotModifier显示---*/
            StringBuffer sBuffer = new StringBuffer();
            for (int j = 0; j < kot.getKotItemModifiers().size(); j++) {
                KotItemModifier kotItemModifier = kot.getKotItemModifiers().get(j);
                if (kotItemModifier != null
                        && kotItemDetail.getId().intValue() == kotItemModifier.getKotItemDetailId().intValue()) {
                    sBuffer.append("--" + kotItemModifier.getModifierName() + "\n");
                }
            }
            /* show special instructions */
            if (!TextUtils.isEmpty(kotItemDetail.getSpecialInstractions())) {
                sBuffer.append("*" + kotItemDetail.getSpecialInstractions()
                        + "*");
            }
            if (sBuffer != null && !sBuffer.equals("")) {
                holder.modifiers.setText(sBuffer + "");
            } else {
                holder.modifiers.setText("");
            }
            holder.mSwipeItemLayout.close();
            if (mItemTouchListener != null) {
                //  holder.itemView.setOnClickListener(v -> mItemTouchListener.onItemClick(holder.mContent.getText().toString()));
                holder.itemView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemTouchListener.onItemClick(holder.mContent.getText().toString());

                    }
                });
                if (holder.mLeftMenu != null) {
                    holder.mLeftMenu.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mItemTouchListener.onLeftMenuClick("" + holder.getAdapterPosition());
                            holder.mSwipeItemLayout.close();
                        }
                    });
                }

                if (holder.mRightMenu != null) {
                    holder.mRightMenu.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            holder.mSwipeItemLayout.close();
                            mItemTouchListener.onRightMenuClick("" + holder.getAdapterPosition());
                        }
                    });
                }
            }
        }

    }

    private static class SimpleViewHolder extends RecyclerView.ViewHolder {

        private final View mLeftMenu;
        private final View mRightMenu;
        private final TextView mContent, num, modifiers;
        private final SwipeItemLayout mSwipeItemLayout;

        SimpleViewHolder(View itemView) {
            super(itemView);
            mSwipeItemLayout = (SwipeItemLayout) itemView.findViewById(R.id.swipe_layout);
            mContent = (TextView) itemView.findViewById(R.id.tv_text_re);
            num = (TextView) itemView.findViewById(R.id.tv_order_num_re);
            modifiers = (TextView) itemView.findViewById(R.id.tv_dish_introduce_re);
            mLeftMenu = itemView.findViewById(R.id.left_menu);
            mRightMenu = itemView.findViewById(R.id.right_menu);

        }
    }
}
