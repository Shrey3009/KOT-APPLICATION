package com.alfredwaiter.activity;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.LoadingDialog;
import com.alfredbase.global.CoreData;
import com.alfredbase.http.ResultCode;
import com.alfredbase.javabean.ItemCategory;
import com.alfredbase.javabean.ItemMainCategory;
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.utils.TextTypeFace;
import com.alfredwaiter.R;
import com.alfredwaiter.adapter.OrderAdapter;
import com.alfredwaiter.global.App;
import com.alfredwaiter.global.UIHelp;
import com.alfredwaiter.javabean.ItemCategoryAndDetails;
import com.alfredwaiter.popupwindow.SearchMenuItemWindow;
import com.alfredwaiter.popupwindow.SetItemCountWindow;
import com.alfredwaiter.utils.WaiterUtils;
import com.alfredwaiter.view.SlidePanelView;

import java.util.ArrayList;
import java.util.List;

public class MenuPage extends BaseActivity {
    public static final int VIEW_EVENT_CLICK_MAIN_CATEGORY = 0;
    public static final int VIEW_EVENT_MODIFY_ITEM_COUNT = 1;
    public static final int VIEW_EVENT_SET_PERSON_INDEX = 2;
    public static final int VIEW_EVENT_SET_QTY = 3;
    public static final int VIEW_ENVENT_KOTNOTIFICATION_LIST = 4;
    public static final int VIEW_ENVENT_GET_ORDERDETAILS = 101;
    public static final int TRANSFER_TABLE_NOTIFICATION = 102;

    public static final int VIEW_EVENT_SHOW_SEARCH = 200;
    public static final int VIEW_EVENT_SEARCH = 201;
    public static final int VIEW_EVENT_DISMISS_SEARCH = 202;

    //	public static final int VIEW_EVENT_SET_ITEM_NUM = 103;
    private ExpandableListView expandableListView;
    private OrderAdapter adapter;
    private List<ItemCategoryAndDetails> itemCategoryAndDetailsList = new ArrayList<ItemCategoryAndDetails>();
    private SlidePanelView panel;
    private LinearLayout container;
    private SetItemCountWindow setItemCountWindow;

    private int currentGroupId = 0;

    private List<OrderDetail> orderDetails = new ArrayList<OrderDetail>();
    private TextView tv_notification_qty;

    private SearchMenuItemWindow searchPopUp;

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_menu_page);
        initTextTypeFace();
        searchPopUp = new SearchMenuItemWindow(context, handler, findViewById(R.id.rl_root));

        setItemCountWindow = new SetItemCountWindow(this, findViewById(R.id.rl_root),
                handler);
        getIntentData();
        expandableListView = (ExpandableListView) findViewById(R.id.expandedListViewEx);
        expandableListView.setDividerHeight(0);
        itemCategoryAndDetailsList.addAll(getItemCategoryAndDetails(null));
        adapter = new OrderAdapter(context, itemCategoryAndDetailsList, handler, setItemCountWindow, null);
        expandableListView.setGroupIndicator(null);
        expandableListView.setAdapter(adapter);
        for (int i = 0; i < itemCategoryAndDetailsList.size(); i++) {
            expandableListView.expandGroup(i);
        }
//		expandableListView.setOnChildClickListener(new OnChildClickListener() {
//
//			@Override
//			public boolean onChildClick(ExpandableListView parent, View v,
//					int groupPosition, int childPosition, long id) {
//				ItemDetail itemDetail = (ItemDetail) adapter.getChild(
//						groupPosition, childPosition);
//				OrderDetail orderDetail = OrderDetailSQL.getUnFreeOrderDetail(
//						currentOrder, itemDetail, currentGroupId,
//						ParamConst.ORDERDETAIL_STATUS_WAITER_CREATE);
//				UIHelp.startOrderDetail(context, currentOrder, itemDetail,
//						orderDetail, currentGroupId);
//				return false;
//			}
//		});
        // refreshList();
        findViewById(R.id.iv_kot_notification).setOnClickListener(this);
        container = (LinearLayout) findViewById(R.id.ll_container);
        panel = new SlidePanelView(this, expandableListView, 200,
                LayoutParams.MATCH_PARENT, handler);
        container.addView(panel, 0);// 加入Panel控件

        loadingDialog = new LoadingDialog(context);
        loadingDialog.setTitle(context.getString(R.string.loading));
        loadingDialog.show();
    }

    private void getIntentData() {
        Intent intent = getIntent();
    }

    public void initTitle() {
        findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView) findViewById(R.id.tv_title_name)).setText(getString(R.string.menu));
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
                    itemCategoryAndDetailsList.clear();
                    itemCategoryAndDetailsList
                            .addAll(getItemCategoryAndDetails(itemMainCategory));
                    for (int i = 0; i < itemCategoryAndDetailsList.size(); i++) {
                        expandableListView.expandGroup(i);
                    }
                    break;
                }
                case MenuPage.TRANSFER_TABLE_NOTIFICATION:
                    WaiterUtils.showTransferTableDialog(context);
                    break;
                case ResultCode.CONNECTION_FAILED:
                    loadingDialog.dismiss();
                    UIHelp.showToast(context, ResultCode.getErrorResultStr(context, (Throwable) msg.obj, getString(R.string.revenue_center)));
                    break;
//			case VIEW_EVENT_SET_ITEM_NUM:
//				Map<String, Object> map = (Map<String, Object>) msg.obj;
//				updateOrderDetail((ItemDetail) map.get("itemDetail"),
//						Integer.parseInt((String) map.get("count")));
//				refreshTotal();
//				refreshList();
//				break;
                case MenuPage.VIEW_EVENT_SHOW_SEARCH:
                    //searchPopUp.show();
                    break;
                case MenuPage.VIEW_EVENT_DISMISS_SEARCH:
                    searchPopUp.dismiss();
                default:
                    break;
            }
        }

        ;
    };

    protected void onResume() {
        super.onResume();
        httpRequestAction(App.VIEW_EVENT_SET_QTY, null);
    }

    ;

    private List<ItemCategoryAndDetails> getItemCategoryAndDetails(
            ItemMainCategory itemMainCategory) {
        List<ItemCategoryAndDetails> result = new ArrayList<ItemCategoryAndDetails>();
        ItemCategoryAndDetails itemCategoryAndDetails = null;
        if (itemMainCategory == null) {
            for (ItemCategory itemCategory : CoreData.getInstance()
                    .getItemCategories()) {
                itemCategoryAndDetails = new ItemCategoryAndDetails();
                itemCategoryAndDetails.setItemCategory(itemCategory);
                itemCategoryAndDetails.setItemDetails(CoreData.getInstance()
                        .getItemDetails(itemCategory));
                result.add(itemCategoryAndDetails);
            }

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
            case R.id.iv_kot_notification: {
                UIHelp.startKOTNotification(context);
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void httpRequestAction(int action, Object obj) {
        super.httpRequestAction(action, obj);
    }

    private void initTextTypeFace() {
        TextTypeFace textTypeFace = TextTypeFace.getInstance();
        textTypeFace.setTrajanProBlod((TextView) findViewById(R.id.tv_title_name));
        textTypeFace.setTrajanProBlod((TextView) findViewById(R.id.tv_notification_qty));
        textTypeFace.setTrajanProBlod((TextView) findViewById(R.id.tv_person_index));
        textTypeFace.setTrajanProBlod((TextView) findViewById(R.id.tv_table_name));
    }
}
