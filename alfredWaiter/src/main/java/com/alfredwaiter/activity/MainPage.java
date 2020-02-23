package com.alfredwaiter.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.LoadingDialog;
import com.alfredbase.ParamConst;
import com.alfredbase.global.CoreData;
import com.alfredbase.http.ResultCode;
import com.alfredbase.javabean.ItemCategory;
import com.alfredbase.javabean.ItemDetail;
import com.alfredbase.javabean.ItemMainCategory;
import com.alfredbase.javabean.ItemModifier;
import com.alfredbase.javabean.Modifier;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.javabean.OrderModifier;
import com.alfredbase.javabean.RemainingStock;
import com.alfredbase.store.TableNames;
import com.alfredbase.store.sql.CommonSQL;
import com.alfredbase.store.sql.OrderDetailSQL;
import com.alfredbase.store.sql.OrderModifierSQL;
import com.alfredbase.store.sql.OrderSQL;
import com.alfredbase.store.sql.RemainingStockSQL;
import com.alfredbase.utils.BH;
import com.alfredbase.utils.CommonUtil;
import com.alfredbase.utils.LogUtil;
import com.alfredbase.utils.ObjectFactory;
import com.alfredbase.utils.RemainingStockHelper;
import com.alfredbase.utils.RxBus;
import com.alfredbase.utils.TextTypeFace;
import com.alfredwaiter.R;
import com.alfredwaiter.adapter.ItemDetailAdapter;
import com.alfredwaiter.adapter.ItemHeaderDetailDecoration;
import com.alfredwaiter.adapter.MainCategoryAdapter;
import com.alfredwaiter.adapter.OrderAdapter;
import com.alfredwaiter.adapter.RvListener;
import com.alfredwaiter.global.App;
import com.alfredwaiter.global.SyncCentre;
import com.alfredwaiter.global.UIHelp;
import com.alfredwaiter.javabean.ItemCategoryAndDetails;
import com.alfredwaiter.javabean.ModifierCPVariance;
import com.alfredwaiter.javabean.ModifierVariance;
import com.alfredwaiter.popupwindow.SearchMenuItemWindow;
import com.alfredwaiter.popupwindow.SetItemCountWindow;
import com.alfredwaiter.popupwindow.SubCategoryWindow;
import com.alfredwaiter.popupwindow.WaiterModifierCPWindow;
import com.alfredwaiter.utils.WaiterUtils;
import com.alfredwaiter.view.CountView;
import com.alfredwaiter.view.SelectPersonDialog;
import com.alfredwaiter.view.SlidePanelView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class MainPage extends BaseActivity implements CheckListener, CallBackMove {
    public static final int VIEW_EVENT_CLICK_MAIN_CATEGORY = 0;
    public static final int VIEW_EVENT_CLICK_SUB_CATEGORY = 9;
    public static final int VIEW_EVENT_MODIFY_ITEM_COUNT = 1;
    public static final int VIEW_EVENT_SET_PERSON_INDEX = 2;
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
    private Observable<String> observable;
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
    private int currentGroupId = 0;

    private List<OrderDetail> orderDetails = new ArrayList<OrderDetail>();
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

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_main_page);
        initTextTypeFace();
        initTitle();
        searchPopUp = new SearchMenuItemWindow(context, handler, findViewById(R.id.rl_root));
        subCategoryWindow = new SubCategoryWindow(context, handler, findViewById(R.id.rl_root));
//		modifierWindow = new ModifierWindow(context, handler, findViewById(R.id.rl_root));
//		modifierWindow = new WaiterModifierWindow(context, handler, findViewById(R.id.rl_root));
        modifierWindow = new WaiterModifierCPWindow(context, handler, findViewById(R.id.rl_root));
//		searchPopUp.setParams(this, handler, findViewById(R.id.rl_root));
//		searchPopUp.setParams(currentOrder,CoreData.getInstance().getItemDetails());
        setItemCountWindow = new SetItemCountWindow(this, findViewById(R.id.rl_root),
                handler);
        dialog = new SelectPersonDialog(context, handler);
        getIntentData();
        LogUtil.d("111111111111--->", "1111111111");
        //   itemDetails.clear();
        itemDetails.addAll(getItemDetail());

//        expandableListView = (ExpandableListView) findViewById(R.id.expandedListViewEx);
//        expandableListView.setDividerHeight(0);
        //  itemCategoryAndDetailsList.addAll(getItemCategoryAndDetails(null));
        //右侧子分类列表recyclerView
//        reItemCate=(RecyclerView)findViewById(R.id.recyc_itemCate) ;
//		mLinearLayoutManager = new LinearLayoutManager(context);
//		reItemCate.setLayoutManager(mLinearLayoutManager);
//		DividerItemDecoration decoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
//		reItemCate.addItemDecoration(decoration);
//        itemCateAdapter=new MainCategoryAdapter(context, itemCategorys, new RvItemClickListener() {
//
//            public void onItemClick(View v, int position) {
//            //	itemCateAdapter.setCheckedPosition(position);
////				if (itemDetailFragment != null) {
////					isMoved = true;
////					targetPosition = position;
////					setChecked(position, true);
////				}
//
//
//            }
//        });
//     reItemCate.setAdapter(itemCateAdapter);

        //菜单列表
        reItemdetail = (RecyclerView) findViewById(R.id.rv_item_detail);


        tv_title_name = (TextView) findViewById(R.id.tv_title_name);
        tv_detail_qty = (TextView) findViewById(R.id.tv_detail_qty);
//
        findViewById(R.id.ll_dish_total).setOnClickListener(this);
        findViewById(R.id.iv_person_index).setOnClickListener(this);
        ll_last_detail = (LinearLayout) findViewById(R.id.ll_last_detail);
        tv_last_order_detail = (TextView) findViewById(R.id.tv_last_order_detail);
        tv_more_detail = (TextView) findViewById(R.id.tv_more_detail);
        tv_more_detail.setOnClickListener(this);
//		findViewById(R.id.iv_setting).setOnClickListener(this);
        container = (LinearLayout) findViewById(R.id.ll_container);
        panel = new SlidePanelView(this, expandableListView, 200,
                LayoutParams.MATCH_PARENT, handler);
        container.addView(panel, 0);// 加入Panel控件
        btn_slide = (Button) findViewById(R.id.btn_slide);
        btn_slide.setOnTouchListener(panel.handlerTouchEvent);
//		((TextView) findViewById(R.id.tv_tables_name)).setText(CoreData
//				.getInstance().getTables(currentOrder.getTableId())
//				.getTableName());
        tv_person_index = (TextView) findViewById(R.id.tv_person_index);
//
        loadingDialog = new LoadingDialog(context);
        loadingDialog.setTitle(context.getResources().getString(R.string.loading));
        loadingDialog.show();
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("order", currentOrder);
        SyncCentre.getInstance().handlerGetOrderDetails(context, parameters,
                handler);
        createAdapter();
        observable = RxBus.getInstance().register(RxBus.RX_REFRESH_STOCK);
        observable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
            @Override
            public void call(String data) {
                try {
                    if (detailAdapter != null) {
                        refreshList();
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
        mManager = new GridLayoutManager(context, 1);
        //通过isTitle的标志来判断是否是title
        mManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });
        //mManager.setRecycleChildrenOnDetach(true);
        reItemdetail.setLayoutManager(mManager);
        DividerItemDecoration itemdecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        reItemdetail.addItemDecoration(itemdecoration);
        detailAdapter = new ItemDetailAdapter(context, itemDetails, setItemCountWindow, new RvListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        }, new CountView.OnCountChange() {
            @Override
            public void onChange(ItemDetail selectedItemDetail, int count, boolean isAdd) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("itemDetail", selectedItemDetail);
                map.put("count", count);
                map.put("isAdd", isAdd);
                handler.sendMessage(handler.obtainMessage(
                        MainPage.VIEW_EVENT_MODIFY_ITEM_COUNT, map));
            }
        });
        reItemdetail.setAdapter(detailAdapter);
        mDecoration = new ItemHeaderDetailDecoration(context, itemDetails);
        reItemdetail.addItemDecoration(mDecoration);
//
    }

    private void getIntentData() {
        Intent intent = getIntent();
        currentOrder = (Order) intent.getExtras().get("order");
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
//                    itemCategoryAndDetailsList.clear();
//                    itemCategoryAndDetailsList
//                            .addAll(getItemCategoryAndDetails(itemMainCategory));
//                    for (int i = 0; i < itemCategoryAndDetailsList.size(); i++) {
//                        expandableListView.expandGroup(i);
//                    }
//                    refreshList();
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
//                            }else if(itemDetail.getItemName().equals(itemCategory.getItemCategoryName())){
//                                index = itemDetails.indexOf(itemDetail);
//                                break;
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

//                    Map<String, Object> map = (Map<String, Object>) msg.obj;
//                    ItemDetail itemDetail = (ItemDetail) map.get("itemDetail");
//                    RemainingStock remainingStock = RemainingStockSQL.getRemainingStockByitemId(itemDetail.getItemTemplateId());
//                    if (remainingStock != null) {
//                        OrderDetail orderDetail = getItemOrderDetail(itemDetail);
//                        if (remainingStock.getQty() > orderDetail.getItemNum()) {
//                            updateOrderDetail(itemDetail,
//                                    (Integer) map.get("count"));
//                            refreshTotal();
//                            refreshList();
//                        }
//                    } else {
//                        updateOrderDetail(itemDetail,
//                                (Integer) map.get("count"));
//                        refreshTotal();
//                        refreshList();
//                    }
                    Map<String, Object> map = (Map<String, Object>) msg.obj;
                    ItemDetail itemDetail = (ItemDetail) map.get("itemDetail");

                    int num = (int) map.get("count");
                    RemainingStock remainingStock = RemainingStockSQL.getRemainingStockByitemId(itemDetail.getItemTemplateId());
                    if (remainingStock != null) {

                        int existedOrderDetailNum = OrderDetailSQL.getOrderAddDetailCountByOrderIdAndItemDetailId(currentOrder.getId(), itemDetail.getId());
                        int reNum = remainingStock.getQty() - existedOrderDetailNum - num;
                        if (reNum >= 0) {
                            updateOrderDetail(itemDetail,
                                    num);
                            refreshTotal();
                            refreshList();
                            boolean isadd = (boolean) map.get("isAdd");
                            if (isadd) {
                                isShow((ItemDetail) map.get("itemDetail"));
                            }
                        } else {
                            UIHelp.showToast(MainPage.this,MainPage.this.getString(R.string.out_of_stock));
                            //  return;
                        }

                    } else {
                        updateOrderDetail(itemDetail,
                                num);
                        refreshTotal();
                        refreshList();
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
                        tv_person_index.setText("?");
                    } else {
                        tv_person_index.setText(currentGroupId + "");
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
                case VIEW_ENVENT_GET_ORDERDETAILS:
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
//			case VIEW_EVENT_SET_ITEM_NUM:
//				Map<String, Object> map = (Map<String, Object>) msg.obj;
//				updateOrderDetail((ItemDetail) map.get("itemDetail"),
//						Integer.parseInt((String) map.get("count")));
//				refreshTotal();
//				refreshList();
//				break;
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
                    Map<String, Object> map = (Map<String, Object>) msg.obj;
//				ItemDetail itemDetail = (ItemDetail) map.get("itemDetail");
                    List<ModifierCPVariance> variances = (List<ModifierCPVariance>) map.get("variances");
                    String description = (String) map.get("description");
                    OrderDetail orderDetail = (OrderDetail) tv_more_detail.getTag();
                    addOrderDetailAndOrderModifier(orderDetail, orderDetail.getItemNum(), variances, description);
                    refreshTotal();
                    refreshList();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    private void isShow(ItemDetail itemDetail) {

        OrderDetail orderDetail = OrderDetailSQL.getUnFreeOrderDetail(
                currentOrder, itemDetail, currentGroupId,
                ParamConst.ORDERDETAIL_STATUS_WAITER_ADD);
        List<ItemModifier> itemModifiers = CoreData.getInstance()
                .getItemModifiers(itemDetail);
//        List<ItemModifier> itemModifiers = CoreData.getInstance()
//                .getItemModifiers(
//                        CoreData.getInstance().getItemDetailById(
//                                itemDetail.getId()));
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
        httpRequestAction(App.VIEW_EVENT_SET_QTY, null);
    }

    ;

    private void refreshList() {
//        adapter.setParams(currentOrder, orderDetails, currentGroupId);
////        adapter.notifyDataSetChanged();

        detailAdapter.setParams(currentOrder, orderDetails, currentGroupId);
        detailAdapter.notifyDataSetChanged();
        if (orderDetails != null && orderDetails.size() > 0) {
            ll_last_detail.setVisibility(View.VISIBLE);
            OrderDetail orderDetail = orderDetails.get(orderDetails.size() - 1);
            tv_more_detail.setTag(orderDetail);
            tv_last_order_detail.setText(orderDetail.getItemName());
        } else {
            ll_last_detail.setVisibility(View.GONE);
            tv_more_detail.setTag(null);
        }
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
        OrderDetail orderDetail = OrderDetailSQL.getUnFreeOrderDetail(
                currentOrder, itemDetail, currentGroupId,
                ParamConst.ORDERDETAIL_STATUS_WAITER_ADD);
//		int oldCount = OrderDetailSQL.getUnFreeOrderDetailsNumInKOTOrPOS(
//				currentOrder, itemDetail, currentGroupId);
        if (count == 0) {// 删除
            OrderDetailSQL.deleteOrderDetail(orderDetail);
            OrderModifierSQL.deleteOrderModifierByOrderDetail(orderDetail);
        } else {// 添加
//			count = count - oldCount;
            currentOrder.setOrderStatus(ParamConst.ORDER_STATUS_OPEN_IN_WAITER);
            OrderSQL.update(currentOrder);
            if (orderDetail == null) {
                orderDetail = ObjectFactory.getInstance()
                        .createOrderDetailForWaiter(currentOrder, itemDetail,
                                currentGroupId, App.instance.getUser());
                orderDetail.setItemNum(count);
                OrderDetailSQL.addOrderDetailETCForWaiterFirstAdd(orderDetail);
            } else {
                orderDetail.setItemNum(count);
                orderDetail.setUpdateTime(System.currentTimeMillis());
                OrderDetailSQL.updateOrderDetailAndOrderForWaiter(orderDetail);
            }
        }
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
        OrderDetailSQL.addOrderDetailETCForWaiter(orderDetail);

    }

    private void refreshTotal() {
        orderDetails = OrderDetailSQL.getUnFreeOrderDetailsForWaiter(currentOrder);
        int itemCount = OrderDetailSQL.getCreatedOrderDetailCountForWaiter(currentOrder.getId().intValue());
        if (itemCount > 0) {
            tv_detail_qty.setVisibility(View.VISIBLE);
            tv_detail_qty.setText(getString(R.string.item)+" : " + itemCount);
        } else {
            tv_detail_qty.setVisibility(View.INVISIBLE);
        }
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


    //	private List<ItemDetail> getItemDetail(
//			) {
//		List<ItemDetail> itemDetaillist =new ArrayList<ItemDetail>();
//		List<ItemDetail> itemDetailandCate =new ArrayList<ItemDetail>();
//		itemDetaillist=CoreData.getInstance().getItemDetails();
//		List<ItemCategory> list=new ArrayList<ItemCategory>();
//
//		 for(int i=0; i< CoreData.getInstance().getItemMainCategories().size();i++) {
//			 list=CoreData.getInstance()
//					 .getItemCategoriesorDetail(CoreData.getInstance().getItemMainCategories().get(i));
//			 for (int j = 0; j < list.size(); i++) {
//				 ItemDetail detail = new ItemDetail();
//				 detail.setItemCategoryName(list.get(j).getItemCategoryName());
//				 detail.setId(list.get(j).getId());
//				 detail.setTag(String.valueOf(i));
//				 itemDetailandCate.add(detail);
//
//				 for (ItemDetail itemDetail : itemDetaillist) {
//					 if (itemDetail.getItemCategoryId().intValue() == list.get(j)
//							 .getId().intValue()) {
//						 itemDetail.setItemCategoryName(list.get(j).getItemCategoryName());
//						 itemDetail.setTag(String.valueOf(i));
//						 itemDetailandCate.add(itemDetail);
//					 }
//				 }
//			 }
//		 }
//
//
////		else {
////			ItemCategory=CoreData.getInstance()
////					.getItemCategories(itemMainCategory);
////		}
//		return itemDetailandCate;
//	}
    private List<ItemDetail> getItemDetail(
    ) {
        List<ItemDetail> itemDetaillist = new ArrayList<ItemDetail>();
        //itemDetaillist=CoreData.getInstance().getItemDetails();
        List<ItemDetail> itemDetailandCate = new ArrayList<ItemDetail>();
//		itemDetaillist=CoreData.getInstance().getItemDetails();
//		List<ItemCategory> list=new ArrayList<ItemCategory>();
        List<ItemCategory> itemCategorylist = CoreData.getInstance().getItemCategoriesorDetail();
        List<ItemMainCategory> itemMainCategorielist = CoreData.getInstance().getItemMainCategories();

        for (int i = 0; i < itemMainCategorielist.size(); i++) {

            ItemDetail detail = new ItemDetail();
            detail.setItemCategoryName(itemMainCategorielist.get(i).getMainCategoryName());
            // detail.setId(list.get(j).getId());
            detail.setTag(String.valueOf(i));
            detail.setViewType(1);
            itemDetailandCate.add(detail);

            for (int j = 0; j < itemCategorylist.size(); j++) {
                int id, cid;

                id = itemMainCategorielist.get(i).getId();
                ItemCategory itemCategory = itemCategorylist.get(j);
                cid = itemCategory.getItemMainCategoryId();
                if (id == cid) {

                    ItemDetail itemCateDetail = new ItemDetail();
                    itemCateDetail.setItemCategoryName(itemMainCategorielist.get(i).getMainCategoryName());
                    // detail.setId(list.get(j).getId());
                    itemCateDetail.setItemName(itemCategory.getItemCategoryName());
                    itemCateDetail.setItemCategoryId(itemCategory.getId());
                    itemCateDetail.setTag(String.valueOf(i));
                    itemCateDetail.setViewType(2);
                    itemDetailandCate.add(itemCateDetail);
                    itemDetaillist.clear();
                    itemDetaillist = CoreData.getInstance().getItemDetails(itemCategory);
                    for (int d = 0; d < itemDetaillist.size(); d++) {
                        itemDetaillist.get(d).setItemCategoryName(itemMainCategorielist.get(i).getMainCategoryName());
                        itemDetaillist.get(d).setTag(String.valueOf(i));
                        itemDetaillist.get(d).setViewType(3);
                        itemDetailandCate.add(itemDetaillist.get(d));

                    }


                }

            }

        }


//		else {
//			ItemCategory=CoreData.getInstance()
//					.getItemCategories(itemMainCategory);
//		}
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
                UIHelp.startKOTNotification(context);
                break;
            }
            case R.id.ll_dish_total: {
                OpenOrderDetailsTotal();
                break;
            }
            case R.id.iv_person_index: {
                int maxGroupId = OrderDetailSQL.getMaxGroupId(currentOrder);

                dialog.show(currentOrder.getPersons() > maxGroupId ? currentOrder
                        .getPersons() : maxGroupId);
                break;
            }
            case R.id.iv_refresh:
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("visibilityMap", View.VISIBLE);
                UIHelp.startSetting(context, map, currentOrder);
                break;
            case R.id.tv_more_detail: {
                OrderDetail orderDetail = (OrderDetail) v.getTag();
                ItemDetail itemDetail = CoreData.getInstance().getItemDetailById(orderDetail.getItemId().intValue(), orderDetail.getItemName());
                List<Integer> modifierIds = OrderModifierSQL.getOrderModifierIdsByOrderDetailId(orderDetail.getId());
                modifierWindow.show(itemDetail, modifierIds, currentOrder, orderDetail);
            }
            break;
            default:
                break;
        }
    }

    private void OpenOrderDetailsTotal() {
        for (OrderDetail orderDetail : orderDetails) {
            OrderDetailSQL.updateOrderDetailStatusById(ParamConst.ORDERDETAIL_STATUS_WAITER_CREATE, orderDetail.getId().intValue());
        }

        //       RemainingStockHelper.updateRemainingStockNumByOrder(currentOrder);
        UIHelp.startOrderDetailsTotal(context, currentOrder);
//		this.finish();
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
        textTypeFace.setTrajanProBlod((TextView) findViewById(R.id.tv_person_index));
        textTypeFace.setTrajanProBlod((TextView) findViewById(R.id.tv_table_name));
    }


    //	private void setChecked(int position, boolean isLeft) {
//		LogUtil.d("p-------->", String.valueOf(position));
//		if (isLeft) {
//			itemCateAdapter.setCheckedPosition(position);
//			//此处的位置需要根据每个分类的集合来进行计算
//			int count = 0;
//			for (int i = 0; i < position; i++) {
//				count += mSortBean.getCategoryOneArray().get(i).getCategoryTwoArray().size();
//			}
//			count += position;
//			itemDetailFragment.setData(count);
//			ItemHeaderDetailDecoration.setCurrentTag(String.valueOf(targetPosition));//凡是点击左边，将左边点击的位置作为当前的tag
//		} else {
//			if (isMoved) {
//				isMoved = false;
//			} else
//				mSortAdapter.setCheckedPosition(position);
//			ItemHeaderDecoration.setCurrentTag(String.valueOf(position));//如果是滑动右边联动左边，则按照右边传过来的位置作为tag
//
//		}
//		moveToCenter(position);
//
//	}
//
//	//将当前选中的item居中
//	private void moveToCenter(int position) {
//		//将点击的position转换为当前屏幕上可见的item的位置以便于计算距离顶部的高度，从而进行移动居中
//		View childAt = rvSort.getChildAt(position - mLinearLayoutManager.findFirstVisibleItemPosition());
//		if (childAt != null) {
//			int y = (childAt.getTop() - rvSort.getHeight() / 2);
//			rvSort.smoothScrollBy(0, y);
//		}
//
//	}
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
        if (subCategoryWindow != null) {
            subCategoryWindow.dismiss();
            return;
        }
        super.onBackPressed();
    }
}
