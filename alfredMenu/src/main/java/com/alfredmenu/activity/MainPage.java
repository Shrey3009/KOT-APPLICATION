package com.alfredmenu.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alfredbase.BaseActivity;
import com.alfredbase.LoadingDialog;
import com.alfredbase.ParamConst;
import com.alfredbase.global.BugseeHelper;
import com.alfredbase.global.CoreData;
import com.alfredbase.http.ResultCode;
import com.alfredbase.javabean.ItemCategory;
import com.alfredbase.javabean.ItemDetail;
import com.alfredbase.javabean.ItemMainCategory;
import com.alfredbase.javabean.ItemModifier;
import com.alfredbase.javabean.Modifier;
import com.alfredbase.javabean.ModifierCheck;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.javabean.OrderModifier;
import com.alfredbase.javabean.PlaceInfo;
import com.alfredbase.javabean.PrinterTitle;
import com.alfredbase.javabean.RemainingStock;
import com.alfredbase.javabean.RevenueCenter;
import com.alfredbase.javabean.TableInfo;
import com.alfredbase.javabean.User;
import com.alfredbase.javabean.model.PrintOrderItem;
import com.alfredbase.javabean.model.PrintOrderModifier;
import com.alfredbase.store.Store;
import com.alfredbase.store.TableNames;
import com.alfredbase.store.sql.CommonSQL;
import com.alfredbase.store.sql.ModifierSQL;
import com.alfredbase.store.sql.OrderDetailSQL;
import com.alfredbase.store.sql.OrderDetailTaxSQL;
import com.alfredbase.store.sql.OrderModifierSQL;
import com.alfredbase.store.sql.OrderSQL;
import com.alfredbase.store.sql.PlaceInfoSQL;
import com.alfredbase.store.sql.RemainingStockSQL;
import com.alfredbase.store.sql.TableInfoSQL;
import com.alfredbase.store.sql.temporaryforapp.ModifierCheckSql;
import com.alfredbase.utils.BH;
import com.alfredbase.utils.CommonUtil;
import com.alfredbase.utils.LogUtil;
import com.alfredbase.utils.ObjectFactory;
import com.alfredbase.utils.RemainingStockHelper;
import com.alfredbase.utils.RxBus;
import com.alfredbase.utils.TextTypeFace;

import com.alfredmenu.global.App;
import com.alfredmenu.global.SyncCentre;
import com.alfredmenu.global.UIHelp;
import com.alfredmenu.javabean.ItemCategoryAndDetails;
import com.alfredmenu.javabean.ModifierCPVariance;
import com.alfredmenu.javabean.ModifierVariance;
import com.alfredmenu.popupwindow.SearchMenuItemWindow;
import com.alfredmenu.popupwindow.SetItemCountWindow;
import com.alfredmenu.popupwindow.SubCategoryWindow;
import com.alfredmenu.popupwindow.WaiterModifierCPWindow;
import com.alfredmenu.popupwindow.WaiterModifierWindow;
import com.alfredmenu.utils.WaiterUtils;
import com.alfredmenu.view.CountView;
import com.alfredmenu.view.ModifierCountView;
import com.alfredmenu.view.MoneyKeyboard;
import com.alfredmenu.view.SelectPersonDialog;
import com.alfredmenu.view.SlidePanelView;
import com.alfredmenu.R;
import com.alfredmenu.adapter.ItemDetailAdapter;
import com.alfredmenu.adapter.ItemHeaderDetailDecoration;
import com.alfredmenu.adapter.MainCategoryAdapter;
import com.alfredmenu.adapter.OrderAdapter;
import com.alfredmenu.adapter.RvListener;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.alfredmenu.activity.OrderDetailsTotal.VIEW_EVENT_GET_BILL;

public class MainPage extends BaseActivity implements CheckListener, CallBackMove, MoneyKeyboard.KeyBoardClickListener {
    public static final int VIEW_EVENT_CLICK_MAIN_CATEGORY = 1000;
    public static final int VIEW_EVENT_CLICK_SUB_CATEGORY = 9;
    public static final int VIEW_EVENT_MODIFY_ITEM_COUNT = 500;
    public static final int VIEW_EVENT_SET_PERSON_INDEX = 502;
    public static final int VIEW_EVENT_SET_QTY = 3;
    public static final int VIEW_ENVENT_KOTNOTIFICATION_LIST = 4;
    public static final int VIEW_EVENT_CLICK_ALL_MAIN_CATEGORY = 5;

    public static final int VIEW_EVENT_FIRST_COLLAPSE = 6;
    public static final int VIEW_EVENT_COLLAPSE = 7;

    public static final int VIEW_EVENT_MODIFIER_COUNT = 8;

    public static final int VIEW_ENVENT_GET_ORDERDETAILS = 101;
    public static final int TRANSFER_TABLE_NOTIFICATION = 102;

    public static final int VIEW_EVENT_SHOW_SEARCH = 200;
    public static final int VIEW_EVENT_SEARCH = 201;
    public static final int VIEW_EVENT_DISMISS_SEARCH = 202;
    public static final int VIEW_EVENT_ADD_ORDER_DETAIL = 203;

    public static final int VIEW_EVENT_SLIDE = 204;
    public static final int VIEW_EVENT_SLIDE_CLICK = 205;
    public static final int VIEW_EVENT_ADD_ORDER_DETAIL_AND_MODIFIER = 206;
    public static final int VIEW_EVENT_GET_BILL = 2;

    public static final int VIEW_EVENT_CURRENT_ORDER_DETAIL = 500;
    private Observable<String> observable, observableOrder;
    //	public static final int VIEW_EVENT_SET_ITEM_NUM = 103;
    private ExpandableListView expandableListView;
    private OrderAdapter adapter;
    private List<ItemCategoryAndDetails> itemCategoryAndDetailsList = new ArrayList<ItemCategoryAndDetails>();
    private SlidePanelView panel;
    private LinearLayout container;
    private TextView tv_person_index;
    private TextView tv_title_name;
    private TextView tv_detail_qty;
    private SelectPersonDialog dialog;
    private Order currentOrder;
    private SetItemCountWindow setItemCountWindow;
    private LinearLayout ll_last_detail;
    private TextView tv_last_order_detail;
    private TextView tv_more_detail;
    private ImageView iv_notes;
    private int currentGroupId = 0;

    private List<OrderDetail> orderDetails = new ArrayList<OrderDetail>();
    private List<OrderDetail> orderDetailsList = new ArrayList<OrderDetail>();
    private TextView tv_notification_qty;

    private SearchMenuItemWindow searchPopUp;
    private SubCategoryWindow subCategoryWindow;
    private Button btn_slide;
    //	private ModifierWindow modifierWindow;
//	private WaiterModifierWindow modifierWindow;
    private WaiterModifierCPWindow modifierWindow;
    List<ItemCategory> itemCategorys = new ArrayList<ItemCategory>();
    private RecyclerView reItemCate;
    private RecyclerView reItemdetail;
    MainCategoryAdapter itemCateAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    List<ItemDetail> itemDetails = new ArrayList<ItemDetail>();

    ItemDetailFragment itemDetailFragment;
    private GridLayoutManager mManager;
    private ItemDetailAdapter detailAdapter;
    private boolean move = false;
    private boolean needScrollNext = false;
    private int mIndex = 0;
    private static ItemHeaderDetailDecoration mDecoration;
    private static CheckListener checkListener;

    private TextView txtTest ;
    /// order detail
    private ListView lv_dishes;
    private TextView tv_place_order;
    private Button btn_printbill;
    private Button btn_get_bill;
    private Button btn_reprintbill;
    private TextView tv_item_count;
    private TextView tv_grand_total;
    private TextView tv_sub_total;
    private TextView tv_discount;
    private TextView tv_taxes,tv_promotion;
    private OrderDetailListAdapter orderDetailListAdapter;
    private List<OrderDetail> newOrderDetails = new ArrayList<OrderDetail>();
    private List<OrderDetail> oldOrderDetails = new ArrayList<OrderDetail>();
//    private Order currentOrderDetailList;
    private LinearLayout ll_bill_action;
    private OrderDetail selectedOrderDetail;
    public LoadingDialog loadingDialog;
    private OrderDetailsTotal orderDetailsTotal;
    private boolean getBill;
    private ItemDetail itemDetail;
    private int num;
    private OrderDetail orderDetail;
    private CountView countView;
    private boolean flag;
    private RelativeLayout rl_countKeyboard;
    private RelativeLayout rl_content;
    private WaiterModifierWindow waiterModifierWindow;
    private StringBuffer buffer = new StringBuffer();
    private DismissCall dismissCall;
    private ModifierCountView modifierCountView;

    private String id, itemMainCategoryId;
    private int idMenuCategory,cid, idcategory,currentcategory;
    boolean isFlagPlaceOrder ;
    private boolean isCommitOrder;
    boolean showPlace = false;
    private List<PlaceInfo> placesList;
    private TableInfo currenTables;
    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_main_page);
        initTextTypeFace();
        initTitle();
        searchPopUp = new SearchMenuItemWindow(context, handler, findViewById(R.id.rl_root));
        subCategoryWindow = new SubCategoryWindow(context, handler, findViewById(R.id.rl_root));
        modifierWindow = new WaiterModifierCPWindow(context, handler, findViewById(R.id.rl_root));
        waiterModifierWindow = new WaiterModifierWindow(context, handler, findViewById(R.id.rl_root));

        setItemCountWindow = new SetItemCountWindow(this, findViewById(R.id.rl_root),
                handler);

        dialog = new SelectPersonDialog(context, handler);

        Store.putBoolean(MainPage.this, Store.PLACEORDER_SET_LOCK, false);

        getIntentData();
        getIntentDataId();

        idMenuCategory = Integer.parseInt(id);
        idcategory = Integer.parseInt(itemMainCategoryId);

        // order detail
        View includedLayout = findViewById(R.id.lv_orderdetail);
        rl_content = (RelativeLayout) findViewById(R.id.rl_content);
        rl_countKeyboard = (RelativeLayout) findViewById(R.id.rl_countKeyboard);
        rl_countKeyboard.setVisibility(View.GONE);

        rl_countKeyboard = (RelativeLayout) includedLayout.findViewById(R.id.rl_countKeyboard);
        rl_countKeyboard.setVisibility(View.GONE);
        MoneyKeyboard moneyKeyboard = (MoneyKeyboard) findViewById(R.id.countKeyboard);
        moneyKeyboard.setMoneyPanel(View.GONE);
        moneyKeyboard.setMoneyRoot(Color.BLACK);
        moneyKeyboard.setKeyBoardClickListener(this);

        lv_dishes = (ListView) includedLayout.findViewById(R.id.lv_dishes);
        orderDetailListAdapter = new OrderDetailListAdapter(context);
        lv_dishes.setAdapter(orderDetailListAdapter);
        lv_dishes.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

            }
        });


        tv_sub_total = (TextView) includedLayout.findViewById(R.id.tv_sub_total);
        tv_discount = (TextView) includedLayout.findViewById(R.id.tv_discount);
        tv_taxes = (TextView) includedLayout.findViewById(R.id.tv_taxes);

        btn_get_bill = (Button) includedLayout.findViewById(R.id.btn_get_bill);
        btn_get_bill.setOnClickListener(this);

       /* btn_printbill = (Button) includedLayout.findViewById(R.id.btn_print_bill);
        btn_printbill.setOnClickListener(this);

        btn_reprintbill = (Button) includedLayout.findViewById(R.id.btn_reprint_kot);
        btn_reprintbill.setOnClickListener(this);*/

        tv_place_order = (Button) includedLayout.findViewById(R.id.tv_place_order);
        tv_place_order.setOnClickListener(this);

        tv_item_count = (TextView) includedLayout.findViewById(R.id.tv_item_count) ;
        tv_grand_total = (TextView) includedLayout.findViewById(R.id.tv_grand_total);

        ll_bill_action = (LinearLayout) findViewById(R.id.ll_bill_action);
        ((TextView) includedLayout.findViewById(R.id.tv_tables_name)).setText(TableInfoSQL.getTableById(currentOrder.getTableId())
                .getName());

        //   itemDetails.clear();
        itemDetails.addAll(getItemDetail());

//        modifierCountView = (ModifierCountView) findViewById(R.id.countView_item1);



            //菜单列表

        reItemdetail = (RecyclerView) findViewById(R.id.rv_item_detail);


        tv_title_name = (TextView) findViewById(R.id.tv_title_name);
//        tv_detail_qty = (TextView) findViewById(R.id.tv_detail_qty);

        findViewById(R.id.ll_dish_total).setOnClickListener(this);

        container = (LinearLayout) findViewById(R.id.ll_container);
        container.setOnClickListener(this);

        panel = new SlidePanelView(this, expandableListView, 200,
                LayoutParams.MATCH_PARENT, handler);
        container.addView(panel, 0);// 加入Panel控件

//        tv_person_index = (TextView) findViewById(R.id.tv_person_index);
        loadingDialog = new LoadingDialog(context);
        loadingDialog.setTitle(context.getResources().getString(R.string.loading));
        loadingDialog.show();
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("order", currentOrder);
        SyncCentre.getInstance().handlerGetOrderDetails(context, parameters,
                handler);
        createAdapter();

        observableOrder = RxBus.getInstance().register(RxBus.RX_REFRESH_TABLE);
        observableOrder.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
            @Override
            public void call(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    int tableId = jsonObject.getInt("tableId");

                    int status = jsonObject.getInt("status");


                    int table = Store.getInt(context, Store.TABLEID, 0);
                    if(table == tableId) {
                        TableInfoSQL.updateTableStatusById(tableId, status);
                        placesList = PlaceInfoSQL.getAllPlaceInfo();
                        currenTables = TableInfoSQL.getTableById(tableId);
                        handler.sendMessage(handler.obtainMessage(
                                TablesPage.VIEW_EVENT_SET_PAX, currenTables.getPacks().toString()));
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        observable = RxBus.getInstance().register(RxBus.RX_REFRESH_STOCK);
        observable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
            @Override
            public void call(String data) {
                try {

                    if (detailAdapter != null) {
                        refreshList();
                    }

                    if (currentOrder != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            int orderId = jsonObject.getInt("orderId");
                            if (orderId == currentOrder.getId().intValue()) {
                                loadOrder(TableInfoSQL.getTableById(currentOrder.getTableId().intValue()));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (observable != null)
            RxBus.getInstance().unregister(RxBus.RX_REFRESH_STOCK, observable);
        super.onDestroy();
    }

    public static void setListener(CheckListener listener) {
        checkListener = listener;
        mDecoration.setCheckListener(checkListener);
    }

    public void createAdapter() {

        reItemdetail.addOnScrollListener(new RecyclerViewListener());
        SlidePanelView.setCallBackMove(this);
        mManager = new GridLayoutManager(context, 2);// set column listview
        mManager.setOrientation(LinearLayout.HORIZONTAL);
        reItemdetail.setLayoutManager(mManager);
        reItemdetail.scrollToPosition(1);
        //通过isTitle的标志来判断是否是title
        mManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
               return 1;
            }
        });
        //mManager.setRecycleChildrenOnDetach(true);
        reItemdetail.setLayoutManager(mManager);//listview menu
        final DividerItemDecoration itemdecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);//original is vertical
        reItemdetail.addItemDecoration(itemdecoration);//test listview menu
        detailAdapter = new ItemDetailAdapter(context, itemDetails, setItemCountWindow, modifierWindow, new RvListener() {
            @Override
            public void onItemClick(View view, int position) {


                ItemDetail itemDetail = itemDetails.get(position);
                 orderDetail = getItemOrderDetail(itemDetail);
                Log.d("numberr",""+orderDetail.getItemNum());
                modifierWindow = new WaiterModifierCPWindow(context, handler, findViewById(R.id.rl_root));
                List<Integer> modifierIds = OrderModifierSQL.getOrderModifierIdsByOrderDetailId(orderDetail.getId());
//                waiterModifierWindow.show(itemDetail, modifierIds, "");

                modifierWindow.show(itemDetail, modifierIds, currentOrder, orderDetail);

//                }

            }
        }, new CountView.OnCountChange() {
            @Override
            public void onChange(ItemDetail selectedItemDetail, int count, boolean isAdd, boolean isDesc) {

//                Log.d("changecount",""+orderDetail.getId());
                OrderDetail orderDetail = OrderDetailSQL.getUnFreeOrderDetail(
                        currentOrder, itemDetail, currentGroupId,
                        ParamConst.ORDERDETAIL_STATUS_WAITER_ADD);

                Map<String, Object> map = new HashMap<String, Object>();
                map.put("itemDetail", selectedItemDetail);
                map.put("count", count);
                map.put("isAdd", isAdd);
                map.put("isDesc", isDesc);
                handler.sendMessage(handler.obtainMessage(
                        MainPage.VIEW_EVENT_MODIFY_ITEM_COUNT, map));

            }
        }, new CountView.OnNotesChange() {
            @Override
            public void onChangeNotes(ItemDetail selectedItemDetail, int count, boolean isAdd) {


                if (orderDetails != null && orderDetails.size() > 0) {

                    Log.d("onnotes",""+orderDetails.size());


                    for(int i= 0; i<orderDetails.size(); i++ ){

                         int a = orderDetails.get(i).getItemId();
                         int b = selectedItemDetail.getId();
                         if(a == b){
                             Log.d("Item" ,"Bener"+orderDetails.get(i).getId());
//                             updateOrderDetail(selectedItemDetail, count);
                             orderDetail = orderDetails.get(i);
                             List<Integer> modifierIds = OrderModifierSQL.getOrderModifierIdsByOrderDetailId(orderDetail.getId());
                             modifierWindow.show(selectedItemDetail, modifierIds, currentOrder, orderDetail);
                         }
                     }

                    /* OrderDetail orderDetail2 = orderDetails.get()
                     updateOrderDetail(selectedItemDetail, count);
                    List<Integer> modifierIds = OrderModifierSQL.getOrderModifierIdsByOrderDetailId(orderDetail.getId());
                    modifierWindow.show(selectedItemDetail, modifierIds, currentOrder, orderDetail);
*/

                }

                }
//                isShow(selectedItemDetail);

//            }
        });
        reItemdetail.setAdapter(detailAdapter);
        mDecoration = new ItemHeaderDetailDecoration(context, itemDetails);
//        reItemdetail.addItemDecoration(mDecoration);
//
    }

    private void getIntentData() {
        Intent intent = getIntent();
        currentOrder = (Order) intent.getExtras().get("order");
        currentOrder = OrderSQL.getOrder(currentOrder.getId());
        newOrderDetails = OrderDetailSQL
                .getOrderDetailByOrderIdAndOrderDetailStatus(
                        currentOrder.getId(),
                        ParamConst.ORDERDETAIL_STATUS_WAITER_CREATE);
        oldOrderDetails = OrderDetailSQL
                .getOrderDetailByOrderIdAndOrderDetailStatus(currentOrder
                        .getId());
        orderDetails.clear();
        orderDetails.addAll(newOrderDetails);
        orderDetails.addAll(oldOrderDetails);
    }


    public void initTitle() {
        findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) findViewById(R.id.tv_title_name)).setText(context.getResources().getString(R.string.menu));
        findViewById(R.id.ll_kot_notification).setVisibility(View.VISIBLE);
        tv_notification_qty = (TextView) findViewById(R.id.tv_notification_qty);
        ((ImageView) findViewById(R.id.iv_refresh)).setImageDrawable(getResources().getDrawable(R.drawable.icon_set));
        findViewById(R.id.iv_refresh).setVisibility(View.VISIBLE);
        findViewById(R.id.ll_kot_notification).setOnClickListener(this);
        findViewById(R.id.iv_refresh).setOnClickListener(this);
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case VIEW_EVENT_CLICK_MAIN_CATEGORY: {

                    ItemMainCategory itemMainCategory = (ItemMainCategory) msg.obj;

                    List<ItemCategory> itemCategories = CoreData.getInstance().getItemCategories(itemMainCategory);
                    subCategoryWindow.show(itemCategories);
                    break;
                }
                case VIEW_EVENT_CLICK_SUB_CATEGORY: {
                    ItemCategory itemCategory = (ItemCategory) msg.obj;
                    if (itemCategory == null) {
                        return;
                    }
                    List<ItemDetail> itemDetails = detailAdapter.getData();
                    int index = -1;
                    if (itemDetails != null && itemDetails.size() > 0) {
                        for (ItemDetail itemDetail : itemDetails) {
                            if (itemDetail.getItemCategoryId() != null
                                    && itemCategory.getId() != null
                                    && itemDetail.getItemCategoryId().intValue() == itemCategory.getId().intValue()) {
                                index = itemDetails.indexOf(itemDetail);
                                break;

                            }
                        }
                    }
                    if (index != -1) {
                        fastMove(index);
                    }
                }
                break;

                case VIEW_EVENT_CLICK_ALL_MAIN_CATEGORY: {
                    itemCategoryAndDetailsList.clear();
                    itemCategoryAndDetailsList.addAll(getItemCategoryAndDetails(null));
                    for (int i = 0; i < itemCategoryAndDetailsList.size(); i++) {
                        expandableListView.expandGroup(i);
                    }
                    refreshList();
                    break;
                }
                case VIEW_EVENT_MODIFY_ITEM_COUNT: {
                    LogUtil.d("444444444444--->", "4444444444444");

                    Map<String, Object> map = (Map<String, Object>) msg.obj;
                    ItemDetail itemDetail = (ItemDetail) map.get("itemDetail");
//                      itemDetail = (ItemDetail) map.get("itemDetail");

                    int num = (int) map.get("count");
//                     num = (int) map.get("count");
                    RemainingStock remainingStock = RemainingStockSQL.getRemainingStockByitemId(itemDetail.getItemTemplateId());
                    if (remainingStock != null) {

                        int existedOrderDetailNum = OrderDetailSQL.getOrderAddDetailCountByOrderIdAndItemDetailId(currentOrder.getId(), itemDetail.getId());
                        int reNum = remainingStock.getQty() - existedOrderDetailNum - num;
                        if (reNum >= 0) {

                            updateOrderDetail(itemDetail,
                                    num);
//                            addOrderDetailAndOrderModifier(orderDetail, orderDetail.getItemNum(), null, "");
                            refreshTotal();
                            refreshList();
//                            refreshListDesc();

                            OrderDetailsTotal.getInstance().setCurrentOrder(currentOrder);
                            OrderDetailsTotal.getInstance().refreshList();
                            OrderDetailsTotal.getInstance().getOrderDetails();
                            int a = OrderDetailsTotal.getInstance().getOldOrderDetails().size();

                            tv_taxes.setText(context.getResources().getString(R.string.taxes) + " : " + App.instance.getCurrencySymbol() + BH.formatMoney(OrderDetailsTotal.getInstance().getTaxesAmount()));
                            tv_grand_total.setText(context.getResources().getString(R.string.grand_total) + " : " +  App.instance.getCurrencySymbol() + BH.formatMoney(OrderDetailsTotal.getInstance().getGrandTotal()));
                            tv_item_count.setText(context.getResources().getString(R.string.item_count)+" : "+OrderDetailsTotal.getInstance().getItemCount());;
                            tv_place_order.setVisibility(View.VISIBLE);
                            btn_get_bill.setVisibility(View.VISIBLE);
                             Store.putBoolean(MainPage.this,Store.ISCOMMITORDER, false);


                            boolean isadd = (boolean) map.get("isAdd");
                            if (isadd) {
                                isShow((ItemDetail) map.get("itemDetail"));
                            }



//                            refreshListData();
//                            refreshOrderTotal();
                        } else {
                            UIHelp.showToast(MainPage.this,MainPage.this.getString(R.string.out_of_stock));
                            //  return;
                        }

                    } else {

//                        refreshTotal();
//                        refreshList();
                        updateOrderDetail(itemDetail,
                                num);

//                        refreshListData();
//                        refreshOrderTotal();
                        refreshTotal();
                        refreshList();
//                        refreshListDesc();

                        OrderDetailsTotal.getInstance().setCurrentOrder(currentOrder);
                        OrderDetailsTotal.getInstance().refreshList();
                        OrderDetailsTotal.getInstance().getOrderDetails();
                        int a = OrderDetailsTotal.getInstance().getOldOrderDetails().size();

                        tv_taxes.setText(context.getResources().getString(R.string.taxes) + " : " + App.instance.getCurrencySymbol() + BH.formatMoney(OrderDetailsTotal.getInstance().getTaxesAmount()));
                        tv_grand_total.setText(context.getResources().getString(R.string.grand_total) + " : " +  App.instance.getCurrencySymbol() + BH.formatMoney(OrderDetailsTotal.getInstance().getGrandTotal()));
                        tv_item_count.setText(context.getResources().getString(R.string.item_count)+" : "+OrderDetailsTotal.getInstance().getItemCount());;
                        tv_place_order.setVisibility(View.VISIBLE);
                        btn_get_bill.setVisibility(View.VISIBLE);
                        lv_dishes.setAdapter(orderDetailListAdapter);

//                        refreshList();

                        Store.putBoolean(MainPage.this,Store.ISCOMMITORDER, false);


                        boolean isadd = (boolean) map.get("isAdd");
                        if (isadd) {
                            isShow((ItemDetail) map.get("itemDetail"));
                        }
                    }


                    break;
                }
                case VIEW_EVENT_MODIFIER_COUNT: {
                    Map<String, Object> map = (Map<String, Object>) msg.obj;
                    int count = (Integer) map.get("count");
                    ItemDetail itemDetail = (ItemDetail) map.get("itemDetail");
                    ModifierVariance modifierVariance = (ModifierVariance) map.get("modifierVariance");
//				modifierWindow.setList(modifierVariance);

                    break;
                }
                case VIEW_EVENT_SET_PERSON_INDEX: {
                    currentGroupId = (Integer) msg.obj;
                    if (currentGroupId == 0) {//TODO
//                        tv_person_index.setText("?");
                    } else {
//                        tv_person_index.setText(currentGroupId + "");
                    }
                    refreshTotal();
                    refreshList();
                    break;
                }
                case App.VIEW_EVENT_SET_QTY: {
                    int kotNotificationQty = App.instance.getKotNotificationQty();
                    if (kotNotificationQty != 0) {
                        tv_notification_qty.setVisibility(View.VISIBLE);
                        tv_notification_qty.setText(String
                                .valueOf(kotNotificationQty));
                    } else {
                        tv_notification_qty.setVisibility(View.GONE);
                    }
                }
                break;
                case VIEW_ENVENT_GET_ORDERDETAILS:// after click add menu
                    loadingDialog.dismiss();
                    refreshTotal();
                    refreshList();
                    break;
                case MainPage.TRANSFER_TABLE_NOTIFICATION:
                    WaiterUtils.showTransferTableDialog(context);
                    break;
                case ResultCode.CONNECTION_FAILED:
                    loadingDialog.dismiss();
                    UIHelp.showToast(context, ResultCode.getErrorResultStr(context, (Throwable) msg.obj,
                            context.getResources().getString(R.string.revenue_center)));
                    break;

                case MainPage.VIEW_EVENT_SHOW_SEARCH:
                    searchPopUp.show(currentOrder, CoreData.getInstance().getItemDetails());
                    break;
                case MainPage.VIEW_EVENT_SEARCH:
                    if (searchPopUp != null && searchPopUp.isShowing()) {
                        search((String) msg.obj);
                    }
                    break;
                case MainPage.VIEW_EVENT_DISMISS_SEARCH:
                    dismissSearchView();
                    searchPopUp.dismiss();
                    searchPopUp.cancelSearch();
                    dismissSoftInput();
                    break;
                case VIEW_EVENT_SLIDE:
                    btn_slide.setBackgroundResource(R.drawable.btn_slide_click);
                    break;
                case VIEW_EVENT_SLIDE_CLICK:
                    btn_slide.setBackgroundResource(R.drawable.btn_slide);
                    break;
                case VIEW_EVENT_FIRST_COLLAPSE: {
                    itemCategoryAndDetailsList.clear();
                    itemCategoryAndDetailsList.addAll(getItemCategoryAndDetails(null));
                    for (int i = 0; i < itemCategoryAndDetailsList.size(); i++) {
                        expandableListView.collapseGroup(i);
                    }
                    refreshList();
                    break;
                }
                case VIEW_EVENT_COLLAPSE:
                    ItemMainCategory itemMainCategory = (ItemMainCategory) msg.obj;
                    itemCategoryAndDetailsList.clear();
                    itemCategoryAndDetailsList
                            .addAll(getItemCategoryAndDetails(itemMainCategory));
                    for (int i = 0; i < itemCategoryAndDetailsList.size(); i++) {
                        expandableListView.collapseGroup(i);
                    }
                    refreshList();
                    break;
                case VIEW_EVENT_ADD_ORDER_DETAIL_AND_MODIFIER:

                    if(selectedOrderDetail != null){
                         orderDetail = selectedOrderDetail;
                    }

                    Map<String, Object> map = (Map<String, Object>) msg.obj;

                    List<ModifierCPVariance> variances = (List<ModifierCPVariance>) map.get("variances");

                    String description = (String) map.get("description");
                    ItemDetail itemDetail = (ItemDetail) map.get("itemDetail");
                    int count = (int) map.get("number");


                    if(count == 0 ){
                        updateOrderDetail(orderDetail, count);
                        ModifierCheckSql.deleteModifierCheck(orderDetail.getId(), currentOrder.getId());
                        /*getIntentData();
                        refreshTotal();
                        refreshList();*/
                    }else {

                        /*for ( orderDetail : orderDetails) {
                            OrderDetailSQL.updateOrderDetailStatusById(ParamConst.ORDERDETAIL_STATUS_WAITER_CREATE, orderDetail.getId().intValue());
                        }
*/
                        updateOrderDetail(orderDetail, count);

                        //ModifierCheckSql.deleteModifierCheck(orderDetail.getId(), currentOrder.getId());

                        addOrderDetailAndOrderModifier(orderDetail, count, variances, description);

                        /*getIntentData();
                        refreshTotal();
                        refreshList();*/


                        OrderDetailsTotal.getInstance().setCurrentOrder(currentOrder);
                        OrderDetailsTotal.getInstance().refreshList();
                        OrderDetailsTotal.getInstance().getOrderDetails();

                        tv_taxes.setText(context.getResources().getString(R.string.taxes) + " : " + App.instance.getCurrencySymbol() +BH.formatMoney(OrderDetailsTotal.getInstance().getTaxesAmount()));
                        tv_grand_total.setText(context.getResources().getString(R.string.grand_total) + " : " + App.instance.getCurrencySymbol() + BH.formatMoney(OrderDetailsTotal.getInstance().getGrandTotal()));
                        tv_item_count.setText(context.getResources().getString(R.string.item_count) + "  " + OrderDetailsTotal.getInstance().getItemCount());

                        tv_place_order.setVisibility(View.VISIBLE);
                        btn_get_bill.setVisibility(View.VISIBLE);

                    }


                    selectedOrderDetail = null;
                    Store.putBoolean(MainPage.this,Store.ISCOMMITORDER, false);
                    refreshTotal();
                    refreshList();

//                    updateOrderDetail(orderDetail,orderDetail.getItemNum());
//                    getIntentData();

                    break;

                case ResultCode.SUCCESS:
                    loadingDialog.dismiss();
                    if (currentOrder != null) {
                        currentOrder = OrderSQL.getOrder(OrderDetailsTotal.getInstance().getOrder().getId());
                        currentOrder.setOrderStatus(ParamConst.ORDER_STATUS_OPEN_IN_POS);
                        OrderSQL.update(currentOrder);
                    }

                    orderDetails = OrderDetailSQL.getCreatedOrderDetails(OrderDetailsTotal.getInstance().getOrder().getId());

                    refreshList();
                    UIHelp.showToast(context, context.getResources().getString(R.string.place_succ));

                    break;
                case OrderDetailsTotal.VIEW_EVENT_GET_BILL:
                    loadingDialog.dismiss();
                    UIHelp.startMenuMainCategoryPage(context,currentOrder);
//                        UIHelp.startOrderReceiptDetails(context, currentOrder);
                    break;

                case EmployeeID.SYNC_DATA_TAG : {
                    loadingDialog.dismiss();
                    placesList = PlaceInfoSQL.getAllPlaceInfo();
                    break;
                }

                case TablesPage.VIEW_EVENT_SET_PAX: {
                    currenTables.setStatus(ParamConst.TABLE_STATUS_DINING);
                    loadOrderTable();
                    break;
                }


                case TablesPage.VIEW_EVENT_SELECT_TABLES:
//                    dismissLoadingDialog();
                    currentOrder = (Order) msg.obj;
                    refreshList();
                    break;

                default:
                    break;
            }
        }

        ;
    };



    @Override
    public void onKeyBoardClick(String key) {
        BugseeHelper.buttonClicked(key);
        if ("Cancel".equals(key)) {
            endAnimation();
        } else if ("Enter".equals(key)) {
            endAnimation();
            if (buffer.length() > 0) {
                dismissCall.call(key, Integer.parseInt(buffer.toString()));
            } else {
                return;
            }
        } else if ("Clear".equals(key)) {
            buffer.delete(0, buffer.length());
            dismissCall.call(key, 0);
        } else {
            if (buffer.length() < 4) {
                buffer.append(key);
            }
            dismissCall.call(key, Integer.parseInt(buffer.toString()));
        }

    }


    class OrderDetailListAdapter extends BaseAdapter {
        private LayoutInflater inflater;


        public OrderDetailListAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return orderDetails.size();
        }

        @Override
        public Object getItem(int arg0) {
            return orderDetails.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(final int position, View arg1, ViewGroup arg2) {
            OrderDetailListAdapter.ViewHolder holder = null;
            isCommitOrder = Store.getBoolean(MainPage.this, Store.ISCOMMITORDER, true);
            if (arg1 == null) {
                arg1 = inflater.inflate(R.layout.item_order_detail, null);
                holder = new OrderDetailListAdapter.ViewHolder();
                holder.ll_title = (LinearLayout) arg1
                        .findViewById(R.id.ll_title);


                holder.name = (TextView) arg1.findViewById(R.id.name);
                holder.price = (TextView) arg1.findViewById(R.id.price);
                holder.tv_qty = (TextView) arg1.findViewById(R.id.tv_qty);
                holder.subtotal = (TextView) arg1.findViewById(R.id.subtotal);
                holder.tv_modifier = (TextView) arg1.findViewById(R.id.tv_modifier);
                arg1.setTag(holder);
            } else {
                holder = (OrderDetailListAdapter.ViewHolder) arg1.getTag();

            }

//            final OrderDetail orderDetail = OrderDetailsTotal.getInstance().getOrderDetails().get(position);
            final OrderDetail orderDetail = orderDetails.get(position);

            Log.d("Order size",""+orderDetails.size());
            final ItemDetail itemDetail = CoreData.getInstance().getItemDetailById(
                    orderDetail.getItemId());

            final List<OrderModifier> modifiers = OrderModifierSQL.getAllOrderModifierByOrderDetailAndNormal(orderDetail);
            StringBuffer stringBuffer = new StringBuffer();
            if (modifiers.size() > 0) {
                for (OrderModifier orderModifier : modifiers) {
                    Modifier modifier = ModifierSQL.getModifierById(orderModifier.getModifierId());
                    stringBuffer.append(modifier.getModifierName() + "  ");
                }
            }
            stringBuffer.append(orderDetail.getSpecialInstractions());
            if (!TextUtils.isEmpty(stringBuffer.toString())) {
                holder.tv_modifier.setVisibility(View.VISIBLE);
                holder.tv_modifier.setText(stringBuffer.toString());
            } else {
                holder.tv_modifier.setVisibility(View.GONE);
            }



            if (orderDetail.getOrderDetailStatus() == ParamConst.ORDERDETAIL_STATUS_WAITER_CREATE) {
                showPlace = true;
                isCommitOrder =  Store.getBoolean(MainPage.this,Store.ISCOMMITORDER, true);
                holder.ll_title.setBackgroundColor(context.getResources()
                        .getColor(R.color.white));
                Store.putBoolean(context,Store.PLACEORDER_SET_LOCK, false );
                holder.tv_qty.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.icon_orderdetail_number));

                holder.tv_qty.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        final OrderDetail tag = (OrderDetail) arg0.getTag();


                        if (tag.getIsFree().intValue() == ParamConst.FREE) {
                            return;
                        }

                        final TextView textView = (TextView) arg0;
                      /*  textView.setBackgroundColor(context.getResources()
                                .getColor(R.color.gray));*/
                        startAnimation(position);
                        buffer.delete(0, buffer.length());
                        dismissCall = new DismissCall() {

                            @Override
                            public void call(String key, int num) {
                                if (num == orderDetail.getItemNum()) {
                                    return;
                                }
                                if (num < 0)
                                    num = 0;
                                if (num > 999)
                                    num = 999;
                                textView.setText(num + "");
                                if (key.equals("Enter")) {
                                    textView.setBackgroundColor(context
                                            .getResources().getColor(
                                                    R.color.white));
                                    final int itemTempId = CoreData.getInstance().getItemDetailById(tag.getItemId()).getItemTemplateId();
                                    final RemainingStock remainingStock = RemainingStockSQL.getRemainingStockByitemId(itemTempId);
                                    int detailNum = OrderDetailSQL.getOrderNotSubmitDetailCountByOrderIdAndItemDetailId(currentOrder.getId(), tag.getItemId());
                                    if (remainingStock != null) {
                                        if (remainingStock.getQty() >= detailNum) {
                                            int newNum = remainingStock.getQty() - detailNum + tag.getItemNum();
                                            if (num <= newNum) {
                                                updateOrderDetail(tag, num);
                                            } else {
                                                UIHelp.showToast(context, MainPage.this.getString(R.string.out_of_stock));
                                            }
                                        } else {
                                            textView.setText(tag.getItemName() + "");
                                        }
                                    } else {
                                        updateOrderDetail(tag, num);
                                    }

                                    if (num == 0) {
                                        updateOrderDetail(tag, num);
                                        final ItemDetail itemDetail = CoreData.getInstance().getItemDetailById(
                                                orderDetail.getItemId());
                                        ModifierCheckSql.deleteModifierCheck(orderDetail.getId(), currentOrder.getId());
                                    }

                                      refreshList();
                                    OrderDetailsTotal.getInstance().setCurrentOrder(currentOrder);
                                    OrderDetailsTotal.getInstance().refreshList();
                                    OrderDetailsTotal.getInstance().getOrderDetails();

                                    tv_taxes.setText(context.getResources().getString(R.string.taxes) + " : " + App.instance.getCurrencySymbol() + BH.formatMoney(OrderDetailsTotal.getInstance().getTaxesAmount()));
                                    tv_grand_total.setText(context.getResources().getString(R.string.grand_total) + " : " + App.instance.getCurrencySymbol() + BH.formatMoney(OrderDetailsTotal.getInstance().getGrandTotal()));
                                    tv_item_count.setText(context.getResources().getString(R.string.item_count) + "  " + OrderDetailsTotal.getInstance().getItemCount());

                                }
                            }
                        };

                    }
                });
            } else {
                    holder.tv_qty.setOnClickListener(null);
                    holder.tv_qty.setBackgroundColor(context.getResources()
                            .getColor(R.color.gray));
                    holder.ll_title.setBackgroundColor(context.getResources()
                            .getColor(R.color.gray));
                Store.putBoolean(context,Store.PLACEORDER_SET_LOCK, true );
            }
            holder.name.setText(orderDetail.getItemName());
            holder.price.setText(App.instance.getCurrencySymbol()
                    + BH.formatMoney(orderDetail.getItemPrice()).toString());
            holder.tv_qty.setText(orderDetail.getItemNum() + "");
            holder.tv_qty.setTag(orderDetail);
            holder.subtotal.setText(App.instance.getCurrencySymbol()
                    + BH.formatMoney(orderDetail.getRealPrice()).toString());
            arg1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (orderDetail.getOrderDetailStatus().intValue() == ParamConst.ORDERDETAIL_STATUS_WAITER_CREATE) {
                        List<Integer> modifierIds = new ArrayList<Integer>();
                        for (OrderModifier orderModifier : modifiers) {
                            modifierIds.add(orderModifier.getModifierId().intValue());
                        }
                        selectedOrderDetail = orderDetail;
                        modifierWindow.show(itemDetail, modifierIds, currentOrder, orderDetail.getSpecialInstractions() == null ? "" : orderDetail.getSpecialInstractions(), orderDetail);
                    }
                }
            });
            return arg1;
        }

        class ViewHolder {
            public TextView name;
            public TextView price;
            public TextView tv_qty;
            public TextView subtotal;
            public LinearLayout ll_title;
            public TextView tv_modifier;
        }
    }



    public void startAnimation(final int position) {
        rl_countKeyboard.setVisibility(View.VISIBLE);
        lv_dishes.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                lv_dishes.setSelection(position);
            }
        });
    }

    public void endAnimation() {
        rl_countKeyboard.setVisibility(View.GONE);
    }

    private void loadOrder(TableInfo tableInfo) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("tables", tableInfo);
        loadingDialog.setTitle(context.getString(R.string.updating));
        loadingDialog.show();
        SyncCentre.getInstance().selectTables(context, parameters,
                handler);
    }

    private void loadOrderTable(){
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("tables", currenTables);
       /* loadingDialog.setTitle(context.getResources().getString(R.string.loading));
        loadingDialog.show();*/
        SyncCentre.getInstance().selectTables(context, parameters,
                handler);
    }


    private void isShow(ItemDetail itemDetail) {

        Log.d("isshow",""+orderDetails.size());
         orderDetail = OrderDetailSQL.getUnFreeOrderDetail(// it will show after "add" selected menu
                currentOrder, itemDetail, currentGroupId,
                ParamConst.ORDERDETAIL_STATUS_WAITER_ADD);
        List<ItemModifier> itemModifiers = CoreData.getInstance()
                .getItemModifiers(itemDetail);
        Log.d("isshowafter",""+orderDetails.size());
        if (!itemModifiers.isEmpty()) {
            for (ItemModifier itemModifier : itemModifiers) {
                Modifier modifier = CoreData.getInstance().getModifier(
                        itemModifier);

                if (modifier.getMinNumber() > 0) {
                    List<Integer> modifierIds = OrderModifierSQL.getOrderModifierIdsByOrderDetailId(orderDetail.getId());

                    modifierWindow.show(itemDetail, modifierIds, currentOrder, orderDetail);
                    return;
                }
            }
        }

    }

    protected void onResume() {
        super.onResume();
        refreshTotal();
        refreshList();
        OrderDetailsTotal.getInstance().setCurrentOrder(currentOrder);
        OrderDetailsTotal.getInstance().refreshList();
        OrderDetailsTotal.getInstance().getOrderDetails();
        OrderDetailsTotal.getInstance().getOrder();

        if(OrderDetailsTotal.getInstance().getItemCount() > 0){
            tv_place_order.setVisibility(View.VISIBLE);
            btn_get_bill.setVisibility(View.VISIBLE);
            Log.d("tes", "tes");
        }
        tv_taxes.setText(context.getResources().getString(R.string.taxes) + " : " + App.instance.getCurrencySymbol() + BH.formatMoney(OrderDetailsTotal.getInstance().getTaxesAmount()));
        tv_grand_total.setText(context.getResources().getString(R.string.grand_total) + " : " +  App.instance.getCurrencySymbol() + BH.formatMoney(OrderDetailsTotal.getInstance().getGrandTotal()));
        tv_item_count.setText(context.getResources().getString(R.string.item_count)+" : "+OrderDetailsTotal.getInstance().getItemCount());;

        httpRequestAction(App.VIEW_EVENT_SET_QTY, null);
    }


    private void refreshList() {
       detailAdapter.setParams(currentOrder, orderDetails, currentGroupId);
//        detailAdapter.notifyDataSetChanged();

        orderDetails.clear();
        newOrderDetails.clear();
        oldOrderDetails.clear();
       if (currentGroupId < 0) {
            newOrderDetails = OrderDetailSQL
                    .getOrderDetailByOrderIdAndOrderDetailStatus(
                            currentOrder.getId(),
                            ParamConst.ORDERDETAIL_STATUS_WAITER_CREATE);
            oldOrderDetails = OrderDetailSQL
                    .getOrderDetailByOrderIdAndOrderDetailStatus(currentOrder
                            .getId());
        } else {
            newOrderDetails = OrderDetailSQL.getOrderDetails(currentOrder,
                    currentGroupId, ParamConst.ORDERDETAIL_STATUS_WAITER_CREATE);
            oldOrderDetails = OrderDetailSQL.getOrderDetailsUnZero(
                    currentOrder, currentGroupId);
        }




        orderDetails.addAll(newOrderDetails);
        orderDetails.addAll(oldOrderDetails);

        orderDetailListAdapter.notifyDataSetChanged();
        detailAdapter.notifyDataSetChanged();


//        adapter.notifyDataSetChanged();
//        refreshOrderTotal();
//        refreshOrder();

    }

    private void refreshListDesc(){
        orderDetailListAdapter.notifyDataSetChanged();
    }

    private OrderDetail getItemOrderDetail(ItemDetail itemDetail) {

        OrderDetail orderDetail = OrderDetailSQL.getUnFreeOrderDetail(
                currentOrder, itemDetail, currentGroupId,
                ParamConst.ORDERDETAIL_STATUS_WAITER_ADD);
        if (orderDetail == null) {
            orderDetail = ObjectFactory.getInstance()
                    .createOrderDetailForWaiter(currentOrder, itemDetail,
                            currentGroupId, App.instance.getUser());
        }

        return orderDetail;
    }

    private void updateOrderDetail(ItemDetail itemDetail, int count) {

            orderDetail = OrderDetailSQL.getUnFreeOrderDetail(
                currentOrder, itemDetail, currentGroupId,
                ParamConst.ORDERDETAIL_STATUS_WAITER_ADD); // currentorder before

      Log.d("orderdetail1111" ,""+ orderDetails.size());
        if (count == 0) {// 删除

            Log.d("Countt >>>>>", ""+currentOrder.getId());
            OrderDetailSQL.deleteOrderDetail(orderDetail);
            OrderModifierSQL.deleteOrderModifierByOrderDetail(orderDetail);
        } else {// 添加
//			count = count - oldCount;
            currentOrder.setOrderStatus(ParamConst.ORDER_STATUS_OPEN_IN_WAITER);//currentorder
            OrderSQL.update(currentOrder);// currentorder
//            OrderSQL.update(currentOrder);
            if (orderDetail == null) {
                orderDetail = ObjectFactory.getInstance()
                        .createOrderDetailForWaiter(currentOrder, itemDetail,
                                currentGroupId, App.instance.getUser());// currentorder
                orderDetail.setItemNum(count);// before
                OrderDetailSQL.addOrderDetailETCForWaiterFirstAdd(orderDetail);
//                setOrderDetail(orderDetail);
                Log.d( "updateorder >>>>>", ""+currentOrder.getId()+"   "+orderDetails.size());
            } else {
                orderDetail.setItemNum(count);
                orderDetail.setUpdateTime(System.currentTimeMillis());
                OrderDetailSQL.updateOrderDetailAndOrderForWaiter(orderDetail);
//                setOrderDetail(orderDetail);
            }
        }
    }

    private void setOrderDetail(OrderDetail orderDetail){
        this.orderDetail = orderDetail;
    }





    private void addOrderDetailAndOrderModifier(OrderDetail orderDetail, int count, List<ModifierCPVariance> modifierIds, String description) {
        currentOrder.setOrderStatus(ParamConst.ORDER_STATUS_OPEN_IN_WAITER);
        orderDetail.setOrderDetailStatus(ParamConst.ORDERDETAIL_STATUS_WAITER_CREATE);
//		OrderDetailSQL.updateOrderDetailStatusById(ParamConst.ORDERDETAIL_STATUS_WAITER_CREATE, orderDetail.getItemId().intValue());
        OrderSQL.update(currentOrder);
//		OrderDetail orderDetail = ObjectFactory.getInstance()
//				.createOrderDetailForWaiter(currentOrder, itemDetail,
//						currentGroupId, App.instance.getUser());
        orderDetail.setItemNum(count);
        orderDetail.setSpecialInstractions(description);
        OrderModifierSQL.deleteOrderModifierByOrderDetail(orderDetail);
        for (ModifierCPVariance modifierVariance : modifierIds) {
            Modifier modifier = CoreData.getInstance().getModifier(modifierVariance.getModifierId());
            OrderModifier orderModifier = new OrderModifier();
            orderModifier.setId(CommonSQL
                    .getNextSeq(TableNames.OrderModifier));
            orderModifier.setOrderId(currentOrder.getId());
            orderModifier.setOrderDetailId(orderDetail.getId());
            orderModifier
                    .setOrderOriginId(ParamConst.ORDER_ORIGIN_POS);
            orderModifier.setUserId(currentOrder.getUserId());
            orderModifier.setItemId(orderDetail.getItemId());
            orderModifier.setModifierId(modifier.getId());
            orderModifier.setModifierNum(1);
            orderModifier
                    .setStatus(ParamConst.ORDER_MODIFIER_STATUS_NORMAL);
            orderModifier.setModifierPrice(BH.mul(BH.getBD(modifier.getPrice()), BH.getBD(orderDetail.getItemNum()), false).toString());
            Long time = System.currentTimeMillis();
            orderModifier.setCreateTime(time);
            orderModifier.setUpdateTime(time);
            OrderModifierSQL.addOrderModifierForWaiter(orderModifier);
        }
        OrderDetailSQL.addOrderDetailETCForWaiter(orderDetail);// original release thi mark

    }

    private void refreshTotal() {
        Log.d("currentordertotal",""+orderDetails.size());


         orderDetails = OrderDetailSQL.getUnFreeOrderDetailsForWaiter(currentOrder);
        Log.d("currentotalafter",""+orderDetails.size());

    }

    private void search(String key) {
        List<ItemDetail> itemDetails = CoreData.getInstance()
                .getItemDetails();
        List<ItemDetail> itemDetailList = new ArrayList<ItemDetail>();
        if (key != null) {
            key = key.trim().replaceAll("\\s+", "");
            for (ItemDetail itemDtail : itemDetails) {
                String name = CommonUtil.getInitial(itemDtail.getItemName());
                if (name.contains(key) || name.contains(key.toUpperCase())) {
                    itemDetailList.add(itemDtail);
                    continue;
                }
            }
        }
        searchPopUp.setAdapterData(currentOrder, itemDetailList, currentGroupId);
    }

    private List<ItemCategory> getItemCategory(
            ItemMainCategory itemMainCategory) {
        List<ItemCategory> ItemCategory = new ArrayList<ItemCategory>();
        if (itemMainCategory == null) {

            ItemCategory = CoreData.getInstance()
                    .getItemCategories();
        } else {
            ItemCategory = CoreData.getInstance()
                    .getItemCategories(itemMainCategory);
        }
        return ItemCategory;
    }


    private List<ItemDetail> getItemDetail(
    ) {
        List<ItemDetail> itemDetaillist = new ArrayList<ItemDetail>();
         List<ItemDetail> itemDetailandCate = new ArrayList<ItemDetail>();

        List<ItemCategory> itemCategorylist = CoreData.getInstance().getItemCategoriesorDetail();
        List<ItemMainCategory> itemMainCategorielist = CoreData.getInstance().getItemMainCategories();

        for (int i = 0; i < itemMainCategorielist.size(); i++) {

            if(Integer.parseInt(itemMainCategoryId) == itemMainCategorielist.get(i).getId()) {
                ItemDetail detail = new ItemDetail();
//            detail.setItemCategoryName(itemMainCategorielist.get(i).getMainCategoryName());
                // detail.setId(list.get(j).getId());
                detail.setTag(String.valueOf(i));
//                detail.setViewType(1);
//                itemDetailandCate.add(detail);

                for (int j = 0; j < itemCategorylist.size(); j++) {
                    int id, cid;

//                id = itemMainCategorielist.get(i).getId();
//                ItemCategory itemCategory = itemCategorylist.get(j);
//                cid = itemCategory.getItemMainCategoryId();

                    idcategory = itemMainCategorielist.get(i).getId();
                    ItemCategory itemCategory = itemCategorylist.get(j);
                    currentcategory = itemCategory.getId();
                    cid = itemCategory.getItemMainCategoryId();
                    if (idMenuCategory == currentcategory) {
//                if (id == cid) {

                        ItemDetail itemCateDetail = new ItemDetail();
//                        itemCateDetail.setItemCategoryName(itemMainCategorielist.get(i).getMainCategoryName());
                        // detail.setId(list.get(j).getId());
//                        itemCateDetail.setItemName(itemCategory.getItemCategoryName());
                        Log.d("Makanan SUb", itemCategory.getItemCategoryName());

                        if (itemCategory.getImgUrl() != null) {
                            itemCateDetail.setImgUrl(itemCategory.getImgUrl());
                            Log.d("image url", itemCategory.getImgUrl());
                        }

                        itemCateDetail.setItemCategoryId(itemCategory.getId());
                        itemCateDetail.setTag(String.valueOf(i));
//                        itemCateDetail.setViewType(2);
//                        itemDetailandCate.add(itemCateDetail);
                        itemDetaillist.clear();
                        itemDetaillist = CoreData.getInstance().getItemDetails(itemCategory);
                        for (int d = 0; d < itemDetaillist.size(); d++) {
                            Log.d("Makanan Detail List", itemDetaillist.get(d).getItemName());
                            itemDetaillist.get(d).setItemCategoryName(itemMainCategorielist.get(i).getMainCategoryName());
                            itemDetaillist.get(d).setTag(String.valueOf(i));
                            itemDetaillist.get(d).setViewType(3);
                            itemDetailandCate.add(itemDetaillist.get(d));

                        }


                    }

                }
            }

        }

        return itemDetailandCate;
    }


    private List<ItemCategoryAndDetails> getItemCategoryAndDetails(
            ItemMainCategory itemMainCategory) {
        List<ItemCategoryAndDetails> result = new ArrayList<ItemCategoryAndDetails>();
        ItemCategoryAndDetails itemCategoryAndDetails = null;
        if (itemMainCategory == null) {
//			for(ItemMainCategory mItemMainCategory:CoreData.getInstance()
//					.getItemMainCategories()){
            for (ItemCategory itemCategory : CoreData.getInstance()
                    .getItemCategories()) {
                itemCategoryAndDetails = new ItemCategoryAndDetails();
                itemCategoryAndDetails.setItemCategory(itemCategory);
                itemCategoryAndDetails.setItemDetails(CoreData.getInstance()
                        .getItemDetails(itemCategory));
                result.add(itemCategoryAndDetails);
            }
//			}
        } else {
            for (ItemCategory itemCategory : CoreData.getInstance()
                    .getItemCategories(itemMainCategory)) {
                itemCategoryAndDetails = new ItemCategoryAndDetails();
                itemCategoryAndDetails.setItemCategory(itemCategory);
                itemCategoryAndDetails.setItemDetails(CoreData.getInstance()
                        .getItemDetails(itemCategory));
                result.add(itemCategoryAndDetails);
            }
        }
        return result;
    }

    @Override
    public void handlerClickEvent(View v) {
        super.handlerClickEvent(v);
        switch (v.getId()) {

            case R.id.iv_back:
                this.finish();
                break;
            case R.id.ll_kot_notification: {
//                UIHelp.startKOTNotification(context);
                break;
            }
            case R.id.ll_dish_total: {
//                OpenOrderDetailsTotal();
                break;
            }
            /*case R.id.iv_person_index: {
                int maxGroupId = OrderDetailSQL.getMaxGroupId(currentOrder);

                dialog.show(currentOrder.getPersons() > maxGroupId ? currentOrder
                        .getPersons() : maxGroupId);
                break;
            }*/
            case R.id.iv_refresh:// click setting button
                 if(flag){
                    UIHelp.showToast(context, "call waiter for assistence");
                    UIHelp.startLogin(context);
                }else {

                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("visibilityMap", View.VISIBLE);
                    UIHelp.startSetting(context, map, currentOrder);
                }
                break;
            case R.id.btn_get_bill: {

                isFlagPlaceOrder = Store.getBoolean(context,Store.PLACEORDER_SET_LOCK, true );

                if(orderDetails.size() == 0){
                    UIHelp.showToast(context, "Item(s) is empty");
                }
                else if (!isFlagPlaceOrder) {
                    UIHelp.showToast(context, "Please Place Order");
                }
                else {


                    Map<String, Object> parameters = new HashMap<String, Object>();
                    parameters
                            .put("table",
                                    TableInfoSQL.getTableById(
                                            currentOrder.getTableId()));
                    parameters.put("order", currentOrder);
                    SyncCentre.getInstance().getBillPrint(context, parameters, handler);

                    UIHelp.showToast(context, "Get Bill Successfull");

//                    UIHelp.startTables(context);
                }

                break;
            }

            case R.id.tv_place_order :
                if(orderDetails.size() == 0){
                    UIHelp.showToast(context, "Item(s) is empty");
                    Store.putBoolean(context,Store.PLACEORDER_SET_LOCK, false );
                }else{

//                List<ModifierCheck> allModifierCheck = ModifierCheckSql.getAllModifierCheck(OrderDetailsTotal.getInstance().getOrder().getId());
                    List<ModifierCheck> allModifierCheck = ModifierCheckSql.getAllModifierCheck(currentOrder.getId());

                    Map<Integer, String> categorMap = new HashMap<Integer, String>();
                    Map<String, Map<Integer, String>> checkMap = new HashMap<String, Map<Integer, String>>();
                    for (int i = 0; i < allModifierCheck.size(); i++) {
                        ModifierCheck modifierCheck;
                        modifierCheck = allModifierCheck.get(i);
                        boolean needCheck = false;
                        if (OrderDetailsTotal.getInstance().getOrderDetails() != null && OrderDetailsTotal.getInstance().getOrderDetails().size() > 0) {
                            for (OrderDetail orderDetail : OrderDetailsTotal.getInstance().getOrderDetails()) {
                                if (orderDetail.getId().intValue() == modifierCheck.getOrderDetailId()) {
                                    needCheck = true;
                                }
                            }
                        }
                        if (modifierCheck.getNum() > 0 && needCheck) {
                            //  checkMap.put(modifierCheck.getItemName() + "," + modifierCheck.getModifierCategoryName(), modifierCheck.getNum() + "");
                            if (checkMap.containsKey(modifierCheck.getItemName())) {
//                                 if(checkMap.get(modifierCheck.getItemName()) !=null)
//                                 {
//                                    categorMap=checkMap.get(modifierCheck.getItemName());
//                                     categorMap.put(modifierCheck.getModifierCategoryId(),modifierCheck.getModifierCategoryName()+" 不能少于"+modifierCheck.getMinNum()+"种");
//                                     checkMap.put(modifierCheck.getItemName(),categorMap);
                                categorMap.put(modifierCheck.getModifierCategoryId(), modifierCheck.getModifierCategoryName() + " ");
                                checkMap.put(modifierCheck.getItemName(), categorMap);

                            } else {
                                categorMap = new HashMap<Integer, String>();
                                categorMap.put(modifierCheck.getModifierCategoryId(), modifierCheck.getModifierCategoryName() + " ");
                                checkMap.put(modifierCheck.getItemName(), categorMap);
                            }
                        }
                    }
                    if (checkMap.size() == 0) {

                        isCommitOrder =  Store.getBoolean(MainPage.this,Store.ISCOMMITORDER, true);

                        if (isCommitOrder) {

                            UIHelp.showToast(context, "Item(s) have been sent to KDS");
                        }

                        Log.d("orderdetail", "" + orderDetails);

                        if (!isCommitOrder && orderDetails.size() != 0) {
                            commitOrderToPOS();

                        }
                        //	UIHelp.showToast(context,"成功");
                    } else {
                        StringBuffer checkbuf = new StringBuffer();
                        Iterator iter = checkMap.entrySet().iterator();
                        while (iter.hasNext()) {
                            Map.Entry entry = (Map.Entry) iter.next();
                            String key = (String) entry.getKey();
                            checkbuf.append(" " + key + ":");
                            Map<Integer, String> val = (Map<Integer, String>) entry.getValue();
                            Iterator iter2 = val.entrySet().iterator();
                            while (iter2.hasNext()) {
                                Map.Entry entry2 = (Map.Entry) iter2.next();
                                String val2 = (String) entry2.getValue();
                                checkbuf.append(val2 + " ");
//                      String val = (String) entry.getValue();
//                      checkbuf.append("不能少于"+val+"种 .");
                            }
                        }

                        UIHelp.showToast(context, checkbuf.toString());


                    }
                }

            break;
            default:
                break;
        }
    }

    private void dismissSoftInput() {
        // 得到InputMethodManager的实例
        if (getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED) {
            // 如果开启
            getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            getWindow().getAttributes().softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED;
        }
    }

    private void dismissSearchView() {
        searchPopUp.setAdapterData(currentOrder, CoreData.getInstance().getItemDetails(""), currentGroupId);
    }

    @Override
    public void httpRequestAction(int action, Object obj) {
        super.httpRequestAction(action, obj);
        if (TRANSFER_TABLE_NOTIFICATION == action) {
            Order mOrder = (Order) obj;
            if (mOrder.getId().intValue() == currentOrder.getId().intValue()) {
                handler.sendEmptyMessage(TRANSFER_TABLE_NOTIFICATION);
            }
        }
        handler.sendEmptyMessage(action);
    }

    private void initTextTypeFace() {
        TextTypeFace textTypeFace = TextTypeFace.getInstance();
        textTypeFace.setTrajanProBlod((TextView) findViewById(R.id.tv_title_name));
        textTypeFace.setTrajanProBlod((TextView) findViewById(R.id.tv_notification_qty));
//        textTypeFace.setTrajanProBlod((TextView) findViewById(R.id.tv_person_index));
        textTypeFace.setTrajanProBlod((TextView) findViewById(R.id.tv_table_name));
    }


    @Override
    public void check(int position, boolean isScroll) {
        checkListener.check(position, isScroll);
    }


    @Override
    public void move(int n) {
        mIndex = n;
        reItemdetail.stopScroll();
        fastMoveToPosition(n);
    }

    private void fastMove(int n) {
        mIndex = n;
        reItemdetail.stopScroll();
        fastMoveToPosition(n);
    }

    private void smoothMoveToPosition(int n) {
        int firstItem = mManager.findFirstVisibleItemPosition();
        int lastItem = mManager.findLastVisibleItemPosition();
        LogUtil.d("first--->", String.valueOf(firstItem));
        LogUtil.d("last--->", String.valueOf(lastItem));
        if (n <= firstItem) {
            reItemdetail.smoothScrollToPosition(n);
            // ((LinearLayoutManager)mRv.getLayoutManager()).scrollToPositionWithOffset(n,0);
        } else if (n <= lastItem) {
            LogUtil.d("pos---->", String.valueOf(n) + "VS" + firstItem);
            int top = reItemdetail.getChildAt(n - firstItem).getTop();
            LogUtil.d("top---->", String.valueOf(top));
            reItemdetail.scrollBy(0, top);
        } else {
            //((LinearLayoutManager)mRv.getLayoutManager()).scrollToPositionWithOffset(n,0);
            reItemdetail.smoothScrollToPosition(n);
            move = true;
        }
    }

    private void fastMoveToPosition(int n) {
        needScrollNext = false;
        int firstItem = mManager.findFirstVisibleItemPosition();
        int lastItem = mManager.findLastVisibleItemPosition();
        LogUtil.d("first--->", String.valueOf(firstItem));
        LogUtil.d("last--->", String.valueOf(lastItem));
        LogUtil.d("n--->", String.valueOf(n));
        if (n <= firstItem) {
            reItemdetail.scrollToPosition(n);
            LogUtil.d("<<<first--->", String.valueOf(n));
            // ((LinearLayoutManager)mRv.getLayoutManager()).scrollToPositionWithOffset(n,0);
        } else if (n <= lastItem) {
            LogUtil.d("pos---->", String.valueOf(n) + "VS" + firstItem);
            int top = reItemdetail.getChildAt(n - firstItem).getTop();
            LogUtil.d("top---->", String.valueOf(top));
            reItemdetail.scrollBy(0, top);
        } else {
            //((LinearLayoutManager)mRv.getLayoutManager()).scrollToPositionWithOffset(n,0);
            reItemdetail.scrollToPosition(n);
            move = true;
            needScrollNext = true;
            LogUtil.d(">>>lastItem--->", String.valueOf(n));
        }
    }

    private class RecyclerViewListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                LogUtil.d("SCROLL--->", "拖拽中");
                App.isleftMoved = false;
            }
            if (move && newState == RecyclerView.SCROLL_STATE_IDLE) {
                App.isleftMoved = false;
                move = false;
                int n = mIndex - mManager.findFirstVisibleItemPosition();
                LogUtil.d("n---->", String.valueOf(n));
                if (0 <= n && n < reItemdetail.getChildCount()) {
                    int top = reItemdetail.getChildAt(n).getTop();
                    LogUtil.d("top---Changed>", String.valueOf(top));
                    reItemdetail.smoothScrollBy(0, top);
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (needScrollNext) {
                needScrollNext = false;
                LogUtil.d("needScrollNext--->", "1111111");
                int n = mIndex - mManager.findFirstVisibleItemPosition();
                if (0 <= n && n < recyclerView.getChildCount()) {
                    int top = recyclerView.getChildAt(n).getTop();
                    recyclerView.scrollBy(0, top);
                }
            }
//            if (move) {
//                move = false;
//                int n = mIndex - mManager.findFirstVisibleItemPosition();
//                if (0 <= n && n < mRv.getChildCount()) {
//                    int top = mRv.getChildAt(n).getTop();
//                    mRv.scrollBy(0, top);
//                }
//            }
        }
    }

    @Override
    public void onBackPressed() {
//        if (subCategoryWindow != null) {
//            subCategoryWindow.dismiss();
//            return;
//        }
        this.finish();
        super.onBackPressed();
    }

    private void getIntentDataId() {
        Bundle extras = getIntent().getExtras();
        id = extras.getString("itemId");
        itemMainCategoryId = extras.getString("itemMainCategoryId");
    }

    private void commitOrderToPOS() {
       /* for (OrderDetail orderDetail : OrderDetailsTotal.getInstance().getOrderDetails()) {
            OrderDetailSQL.updateOrderDetailStatusById(ParamConst.ORDERDETAIL_STATUS_WAITER_CREATE, orderDetail.getId().intValue());
        }
*/
        /*OrderDetailsTotal.getInstance().setCurrentOrder(currentOrder);
        OrderDetailsTotal.getInstance().refreshList();
        OrderDetailsTotal.getInstance().getOrderDetails();
        OrderDetailsTotal.getInstance().getOrder();
        if (OrderDetailsTotal.getInstance().getNewOrderDetails() == null || OrderDetailsTotal.getInstance().getNewOrderDetails().isEmpty()) {
            return;
        }*/

        if (newOrderDetails == null || newOrderDetails.isEmpty()) {
            return;
        }
        Map<String, Object> parameters = new HashMap<String, Object>();
        List<OrderDetail> orderDetails = new ArrayList<OrderDetail>();
        List<OrderModifier> orderModifiers = OrderModifierSQL
                .getAllOrderModifier(OrderDetailsTotal.getInstance().getOrder());
        for (OrderDetail orderDetail : OrderDetailsTotal.getInstance().getNewOrderDetails()) {
            if (orderDetail.getIsFree() != ParamConst.FREE) {
                orderDetails.add(orderDetail);
                Store.putBoolean(MainPage.this,Store.ISCOMMITORDER, true);
                orderDetailListAdapter.notifyDataSetChanged();
            }
        }
        parameters.put("order", OrderDetailsTotal.getInstance().getOrder());
        parameters.put("orderDetails", orderDetails);
        parameters.put("orderModifiers", orderModifiers);
        loadingDialog.setTitle(context.getResources().getString(R.string.loading));
        loadingDialog.show();
        SyncCentre.getInstance().commitOrderAndOrderDetails(context,
                parameters, handler);

//        UIHelp.startMenuCategoryPage(context,currentOrder);

    }


    private void updateOrderDetail(OrderDetail orderDetail, int count) {
        if (count <= 0) {// 删除
            OrderDetailSQL.deleteOrderDetail(orderDetail);
            OrderModifierSQL.deleteOrderModifierByOrderDetail(orderDetail);
        } else {// 添加
            orderDetail.setItemNum(count);
            OrderDetailSQL.updateOrderDetailAndOrderForWaiter(orderDetail);
        }
        currentOrder = OrderSQL.getOrder(orderDetail.getOrderId());
    }


    public interface DismissCall {
        public void call(String key, int num);
    }

}
