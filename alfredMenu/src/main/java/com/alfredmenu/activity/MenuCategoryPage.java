package com.alfredmenu.activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.alfredbase.BaseActivity;
import com.alfredbase.BaseApplication;
import com.alfredbase.LoadingDialog;
import com.alfredbase.ParamConst;
import com.alfredbase.global.CoreData;
import com.alfredbase.http.ResultCode;
import com.alfredbase.javabean.ItemCategory;

import com.alfredbase.javabean.Order;

import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.javabean.PlaceInfo;
import com.alfredbase.javabean.TableInfo;
import com.alfredbase.store.Store;
import com.alfredbase.store.sql.ItemCategorySQL;
import com.alfredbase.store.sql.OrderDetailSQL;
import com.alfredbase.store.sql.OrderSQL;
import com.alfredbase.store.sql.PlaceInfoSQL;
import com.alfredbase.store.sql.TableInfoSQL;
import com.alfredbase.utils.RxBus;
import com.alfredmenu.adapter.RvListener;
import com.alfredmenu.adapter.RvMenuCategoryAdapter;
import com.alfredmenu.R;
import com.alfredmenu.adapter.TablesAdapter;
import com.alfredmenu.global.App;
import com.alfredmenu.global.SyncCentre;
import com.alfredmenu.global.UIHelp;
import com.alfredmenu.utils.WaiterUtils;
import com.viewpagerindicator.TabPageIndicator;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class MenuCategoryPage extends BaseActivity implements CheckListener,CallBackMove {

    private Order currentOrder;
    private Observable<String> observable;
    private List<PlaceInfo> placesList;
    private boolean doubleBackToExitPressedOnce = false;
    private TabPageIndicator indicator;
    private TableInfo currenTables;
    private TablesPage.Adapter adapter;
    RvMenuCategoryAdapter rcAdapter;
    int mainCategoryId;
    RecyclerView rView;
    int tableId;
    int table;
    List<ItemCategory> itemCategorylist;
    List<ItemCategory> selectedItemCategoryList;
    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_menu_category_page);

        loadingDialog = new LoadingDialog(context);
        placesList = PlaceInfoSQL.getAllPlaceInfo();
        selectedItemCategoryList = new ArrayList<>();

        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.iv_refresh).setOnClickListener(this);
        ((TextView) findViewById(R.id.tv_title_name)).setText(context.getResources().getString(R.string.menu));
         rView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager mManager = new GridLayoutManager(context, 2);
        rView.setLayoutManager(mManager);
        getIntentData();

        itemCategorylist = CoreData.getInstance().getItemCategoriesorDetail();
        for(int i  = 0; i<itemCategorylist.size(); i++){
            if(itemCategorylist.get(i).getItemMainCategoryId().equals(mainCategoryId)){
                ItemCategory itemCategory = itemCategorylist.get(i);
                selectedItemCategoryList.add(itemCategory);
            }
        }

         rcAdapter = new RvMenuCategoryAdapter(currentOrder, context, selectedItemCategoryList, this, new RvListener() {
            @Override
            public void onItemClick(View id, int position) {
                Toast.makeText(context, "pos" + position, Toast.LENGTH_LONG).show();
            }
        });
        rView.setAdapter(rcAdapter);


/*
        observable = RxBus.getInstance().register(RxBus.RX_REFRESH_TABLE);
        observable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
            @Override
            public void call(String data) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                     int tableId = jsonObject.getInt("tableId");

                    int status = jsonObject.getInt("status");

                    int table = Store.getInt(context, Store.TABLEID, 0);

                        table = Store.getInt(context, Store.TABLEID, tableId);
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
        });*/

    }

    private void loadOrder(){
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("tables", currenTables);
        loadingDialog.setTitle(context.getResources().getString(R.string.loading));
        loadingDialog.show();
        SyncCentre.getInstance().selectTables(context, parameters,
                handler);
    }



    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {

                case EmployeeID.SYNC_DATA_TAG : {
                    loadingDialog.dismiss();
                    placesList = PlaceInfoSQL.getAllPlaceInfo();
                    break;
                }

                case TablesPage.VIEW_EVENT_SET_PAX: {
                    currenTables.setStatus(ParamConst.TABLE_STATUS_DINING);
                    loadOrder();
                    break;
                }

                case TablesPage.VIEW_EVENT_SELECT_TABLES:
                    dismissLoadingDialog();
                    currentOrder = (Order) msg.obj;
                    OrderSQL.update(currentOrder);
                    ArrayList<OrderDetail> orderDetails = OrderDetailSQL.getCreatedOrderDetails(currentOrder.getId());
                    for (OrderDetail orderDetail : orderDetails) {
                        if (orderDetail.getOrderDetailStatus() == ParamConst.ORDERDETAIL_STATUS_WAITER_CREATE) {
                            currentOrder.setOrderStatus(ParamConst.ORDER_STATUS_OPEN_IN_WAITER);
                            OrderSQL.update(currentOrder);
                            break;
                        }
                    }
//				if (orderDetails.isEmpty()) {
                    OrderSQL.updateOrder(currentOrder);
//                    finish();
//                    UIHelp.startMenuCategoryPage(context, currentOrder);
                    finish();
                    break;

                default:
                    break;
            }

        };
    };



    private void getIntentData() {
        Intent intent = getIntent();
        currentOrder = (Order) intent.getExtras().get("order");

        mainCategoryId = (int) intent.getExtras().get("mainCategoryId");
    }

    @Override
    public void move(int n) {

    }

    @Override
    public void check(int position, boolean isScroll) {

    }

    @Override
    public void onBackPressed() {
        /*UIHelp.startTables(context);
        super.onBackPressed();*/
        if (doubleBackToExitPressedOnce) {
            /*finish();
            System.exit(1);
            android.os.Process.killProcess(android.os.Process.myPid());*/
            Store.putInt(context, Store.TABLEID, 0);
            super.onBackPressed();
            return;
        }


        this.doubleBackToExitPressedOnce = true;
        UIHelp.showToast(this, context.getResources().getString(R.string.exit_program));

        BaseApplication.postHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);


    }

    @Override
    public void handlerClickEvent(View v) {
        super.handlerClickEvent(v);
        switch (v.getId()) {
            case R.id.iv_back:

                UIHelp.startMenuMainCategoryPage(context,currentOrder);

                break;

            case R.id.iv_refresh:

                break;
            default:
                break;
        }

    }


    class Adapter extends PagerAdapter {
        private List<View> views;
        private List<TablesAdapter> tablesAdapters;
        public Adapter(Context context) {
            LayoutInflater inflater = LayoutInflater.from(context);
            views = new ArrayList<View>();
            tablesAdapters = new ArrayList<TablesAdapter>();
            View view = null;
            for (int i = 0; i < placesList.size(); i++) {
                PlaceInfo places = placesList.get(i);
                view = inflater.inflate(R.layout.item_table, null);
                GridView gv_tables = (GridView) view
                        .findViewById(R.id.gv_tables);
                /**
                 * CoreData
                 .getInstance().getTableList(
                 App.instance.getRevenueCenter().getId(),
                 places.getId())
                 */
                TablesAdapter tablesAdapter = new TablesAdapter(context, TableInfoSQL
                        .getTableInfosByPlaces(places), places);
                tablesAdapters.add(tablesAdapter);
                gv_tables.setAdapter(tablesAdapter);
                gv_tables.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int arg2, long arg3) {

                        currenTables = (TableInfo) arg0.getItemAtPosition(arg2);
						/*if (currenTables != null
								&& currenTables.getStatus() != ParamConst.TABLE_STATUS_IDLE) {
							handler.sendMessage(handler.obtainMessage(
									TablesPage.VIEW_EVENT_SET_PAX, currenTables.getPacks().toString()));
						} else {
							setPAXWindow.show();
						}*/
                        handler.sendMessage(handler.obtainMessage(
                                TablesPage.VIEW_EVENT_SET_PAX, currenTables.getPacks().toString()));

                    }
                });
                views.add(view);
            }
        }

        @Override
        public int getCount() {
            return placesList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = views.get(position);
            ((ViewPager) container).addView(view, 0);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return placesList.get(position).getPlaceName();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            if(tablesAdapters != null && !tablesAdapters.isEmpty()){
                for(int i = 0; i < tablesAdapters.size(); i++){
                    TablesAdapter tablesAdapter = tablesAdapters.get(i);
                    tablesAdapter.setTableList();
                    tablesAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Order mOrder = OrderSQL.getOrder(currentOrder.getId());
        if (mOrder == null) {
            WaiterUtils.showTransferTableDialog(context);
        }
    }

    @Override
    protected void onDestroy() {
        if(observable != null)
            RxBus.getInstance().unregister(RxBus.RX_REFRESH_TABLE, observable);
        super.onDestroy();
    }

    private void getPlaces() {
        loadingDialog.setTitle(context.getResources().getString(R.string.loading));
        loadingDialog.show();

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("revenueId", App.instance.getRevenueCenter().getId());
        SyncCentre.getInstance().getPlaceInfo(context, App.instance.getMainPosInfo().getIP(), parameters, handler);
//        loadOrder();

    }

    @Override
    public void httpRequestAction(int action, Object obj) {
        if (MainPage.TRANSFER_TABLE_NOTIFICATION == action) {
            Order mOrder = (Order) obj;
            if (mOrder.getId().intValue() == currentOrder.getId().intValue()) {
                handler.sendEmptyMessage(MainPage.TRANSFER_TABLE_NOTIFICATION);
            }
        }
    }
}
