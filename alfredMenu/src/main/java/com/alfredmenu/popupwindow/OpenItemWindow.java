package com.alfredmenu.popupwindow;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.ParamConst;
import com.alfredbase.global.CoreData;
import com.alfredbase.javabean.ItemCategory;
import com.alfredbase.javabean.ItemDetail;
import com.alfredbase.javabean.ItemMainCategory;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.javabean.Printer;
import com.alfredbase.javabean.TaxCategory;
import com.alfredbase.store.TableNames;
import com.alfredbase.store.sql.CommonSQL;
import com.alfredbase.store.sql.ItemDetailSQL;
import com.alfredbase.store.sql.OrderDetailSQL;
import com.alfredbase.store.sql.TableInfoSQL;
import com.alfredbase.utils.AnimatorListenerImpl;
import com.alfredbase.utils.ButtonClickTimer;
import com.alfredbase.utils.ObjectFactory;
import com.alfredbase.utils.ScreenSizeUtil;
import com.alfredmenu.activity.Setting;
import com.alfredmenu.global.App;
import com.alfredmenu.global.SyncCentre;
import com.alfredmenu.global.UIHelp;
import com.alfredmenu.R;
import com.google.gson.Gson;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OpenItemWindow implements OnClickListener, OnItemSelectedListener {
    public static final int ITEM_MAIN_CATEGORY_TYPE = 1;
    public static final int ITEM_CATEGORY_TYPE = 2;
    public static final int TAX_CATEGORY_TYPE = 3;
    public static final int PRINTER_TYPE = 4;
    private BaseActivity parent;
    private View parentView;
    private Order order;
    private Handler handler;
    private View contentView;
    private PopupWindow popupWindow;
    private List<ItemDetail> itemDetails = new ArrayList<ItemDetail>();
    private LayoutInflater inflater;
    private ViewPager vp_open_item;
    private View view1, view2;
    private Spinner sp_main_category;
    private SpinnerAdapter mainCategoryAdapter;
    private Spinner sp_tax_group;
    private SpinnerAdapter taxCategoryAdapter;
    private Spinner sp_sub_category;
    private SpinnerAdapter subCategoryAdapter;
    private Spinner sp_kitchen;
    private SpinnerAdapter printerAdapter;

    private List<View> viewList = new ArrayList<View>();
    private EditText et_new_open_item_name;
    private EditText et_new_open_item_price;
    private CheckBox checkbox_takeaways;
    private CheckBox checkbox_delivery;
    private CheckBox checkbox_discoutable;

    private ItemMainCategory currentItemMainCategory;
    private ItemCategory currentItemCategory;
    private TaxCategory currentTaxCategory;
    private Printer currentPrinterGroupName;
    private ItemDetailAdapter adapter = new ItemDetailAdapter();
    private ItemDetail currentItemDetail;

    public OpenItemWindow() {
    }

    private void init() {
        contentView = LayoutInflater.from(parent).inflate(
                R.layout.popup_open_item, null);
        view1 = LayoutInflater.from(parent).inflate(R.layout.open_item_view1,
                null);
        view1.findViewById(R.id.btn_open_item_back).setOnClickListener(this);
        view1.findViewById(R.id.btn_open_item_new).setOnClickListener(this);
        ListView listView = ((ListView) view1.findViewById(R.id.lv_orders));
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ItemDetail itemDetail = (ItemDetail) parent
                        .getItemAtPosition(position);
                OrderDetail orderDetail = ObjectFactory.getInstance()
                        .getOrderDetail(order, itemDetail, 0);
                Message msg = handler.obtainMessage();
                msg.what = Setting.VIEW_EVENT_ADD_ORDER_DETAIL;
                msg.obj = itemDetail;
                handler.sendMessage(msg);
                dismiss();
            }
        });
        view2 = LayoutInflater.from(parent).inflate(R.layout.open_item_view2,
                null);
        view2.findViewById(R.id.btn_new_open_item_back)
                .setOnClickListener(this);
        view2.findViewById(R.id.btn_new_open_item_save)
                .setOnClickListener(this);
        et_new_open_item_name = (EditText) view2
                .findViewById(R.id.et_new_open_item_name);
        et_new_open_item_price = (EditText) view2
                .findViewById(R.id.et_new_open_item_price);

        sp_main_category = (Spinner) view2.findViewById(R.id.sp_main_category);
        mainCategoryAdapter = new SpinnerAdapter(parent, CoreData.getInstance()
                .getItemMainCategories(), ITEM_MAIN_CATEGORY_TYPE);
        sp_main_category.setAdapter(mainCategoryAdapter);

        sp_sub_category = (Spinner) view2.findViewById(R.id.sp_sub_category);
        subCategoryAdapter = new SpinnerAdapter(parent, CoreData.getInstance()
                .getItemCategory(
                        CoreData.getInstance().getItemMainCategories().get(0)),
                ITEM_CATEGORY_TYPE);
        sp_sub_category.setAdapter(subCategoryAdapter);

        sp_tax_group = (Spinner) view2.findViewById(R.id.sp_tax_group);
        List<TaxCategory> list = CoreData.getInstance().getTaxCategoryGroup();
        taxCategoryAdapter = new SpinnerAdapter(parent, CoreData.getInstance()
                .getTaxCategoryGroup(), TAX_CATEGORY_TYPE);
        sp_tax_group.setAdapter(taxCategoryAdapter);

        sp_kitchen = (Spinner) view2.findViewById(R.id.sp_kitchen);
        printerAdapter = new SpinnerAdapter(parent, CoreData.getInstance()
                .getNameOfPrintergroup(), PRINTER_TYPE);
        sp_kitchen.setAdapter(printerAdapter);

        sp_main_category.setOnItemSelectedListener(this);

        sp_sub_category.setOnItemSelectedListener(this);

        sp_tax_group.setOnItemSelectedListener(this);

        sp_kitchen.setOnItemSelectedListener(this);

        checkbox_takeaways = (CheckBox) view2
                .findViewById(R.id.checkbox_takeaways);
        checkbox_delivery = (CheckBox) view2
                .findViewById(R.id.checkbox_delivery);
        checkbox_discoutable = (CheckBox) view2
                .findViewById(R.id.checkbox_discoutable);

        viewList.clear();
        viewList.add(view1);
        viewList.add(view2);
        popupWindow = new PopupWindow(parentView,
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        vp_open_item = (ViewPager) contentView.findViewById(R.id.vp_open_item);
        vp_open_item.setAdapter(new OpenItemPagerAdapter());
        popupWindow.setContentView(contentView);
        popupWindow.setFocusable(true);
        initView1();
    }

    private void initView1() {
        itemDetails.clear();
        itemDetails.addAll(ItemDetailSQL.getAllTempItemDetail());
        adapter.notifyDataSetChanged();
    }

    private void initView2(final ItemDetail itemDetail) {
        currentItemDetail = itemDetail;
        if (currentItemDetail == null) {
            et_new_open_item_name.setText("");
            et_new_open_item_price.setText("");
            sp_main_category.setSelection(0);
            sp_tax_group.setSelection(0);
            sp_kitchen.setSelection(0);
            checkbox_takeaways.setChecked(false);
            checkbox_delivery.setChecked(false);
            checkbox_discoutable.setChecked(true);
        } else {
            et_new_open_item_name.setText(itemDetail.getItemName());
            et_new_open_item_price.setText(itemDetail.getPrice());
            for (int i = 0; i < CoreData.getInstance().getItemMainCategories()
                    .size(); i++) {
                ItemMainCategory itemMainCategory = CoreData.getInstance()
                        .getItemMainCategories().get(i);
                if (itemMainCategory.getId().intValue() == itemDetail
                        .getItemMainCategoryId().intValue()) {
                    sp_main_category.setSelection(i);
                    break;
                }
            }

            for (int i = 0; i < CoreData.getInstance().getTaxCategoryGroup()
                    .size(); i++) {
                TaxCategory taxCategory = CoreData.getInstance()
                        .getTaxCategoryGroup().get(i);
                if (taxCategory.getId().intValue() == itemDetail
                        .getTaxCategoryId().intValue()) {
                    sp_tax_group.setSelection(i);
                    break;
                }
            }

            for (int i = 0; i < CoreData.getInstance().getNameOfPrintergroup().size(); i++) {
                Printer printer = CoreData.getInstance().getNameOfPrintergroup().get(i);
                if (printer.getId().intValue() == itemDetail.getPrinterId()
                        .intValue()) {
                    sp_kitchen.setSelection(i);
                    break;
                }
            }
            if (itemDetail.getIsPack() == null) {
                checkbox_takeaways.setChecked(false);
            } else {
                if (itemDetail.getIsPack().intValue() == ParamConst.CAN_PACK) {
                    checkbox_takeaways.setChecked(true);
                } else {
                    checkbox_takeaways.setChecked(false);
                }
            }

            if (itemDetail.getIsTakeout() == null) {
                checkbox_delivery.setChecked(false);
            } else {
                if (itemDetail.getIsTakeout().intValue() == ParamConst.CAN_TAKEOUT) {
                    checkbox_delivery.setChecked(true);
                } else {
                    checkbox_delivery.setChecked(false);
                }
            }
            if (itemDetail.getIsDiscount() == ParamConst.ITEM_DISCOUNT) {
                checkbox_discoutable.setChecked(true);
            } else {
                checkbox_discoutable.setChecked(false);
            }
        }
    }

    public void show(final BaseActivity parent, View parentView,
                     Handler handler, Order order) {
        if (isShowing()) {
            return;
        }
        this.parent = parent;
        this.order = order;
        this.parentView = parentView;
        this.handler = handler;
        inflater = LayoutInflater.from(parent);
        init();
        popupWindow
                .showAtLocation(parentView, Gravity.LEFT | Gravity.TOP, 0, 0);
        vp_open_item.post(new Runnable() {
            @Override
            public void run() {
                ObjectAnimator animator = ObjectAnimator.ofFloat(vp_open_item,
                        "y", vp_open_item.getHeight(),
                        ScreenSizeUtil.getStatusBarHeight(parent));
                animator.setDuration(300);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.play(animator);
                animatorSet.start();
            }
        });

    }

    public void dismiss() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    public boolean isShowing() {
        if (popupWindow != null) {
            return popupWindow.isShowing();
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        if (ButtonClickTimer.canClick(view)) {
            switch (view.getId()) {
                case R.id.btn_open_item_back:
                    ObjectAnimator animator = ObjectAnimator.ofFloat(vp_open_item,
                            "translationY", ScreenSizeUtil.getStatusBarHeight(parent),
                            vp_open_item.getHeight());
                    animator.setDuration(300);
                    AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.play(animator);
                    animatorSet.addListener(new AnimatorListenerImpl() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            popupWindow.dismiss();
                        }
                    });
                    animatorSet.start();
                    break;
                case R.id.btn_open_item_new:
                    vp_open_item.setCurrentItem(1);
                    initView2(null);
                    break;
                case R.id.btn_new_open_item_back:
                    vp_open_item.setCurrentItem(0);
                    initView1();
                    break;
                case R.id.btn_new_open_item_save:
                    String name = et_new_open_item_name.getText().toString();
                    if (TextUtils.isEmpty(name)) {
                        UIHelp.showToast(parent, parent.getResources().getString(R.string.name_not_empty));
                        return;
                    }
                    String priceStr = et_new_open_item_price.getText().toString();
                    if (TextUtils.isEmpty(priceStr)) {
                        UIHelp.showToast(parent, parent.getResources().getString(R.string.price_not_empty));
                        return;
                    }
                    save();
                    Message msg = handler.obtainMessage();
                    msg.what = Setting.VIEW_EVENT_DISMISS_OPEN_ITEM_WINDOW;
                    handler.sendMessage(msg);
                    ObjectAnimator animator1 = ObjectAnimator.ofFloat(vp_open_item,
                            "translationY", ScreenSizeUtil.getStatusBarHeight(parent),
                            vp_open_item.getHeight());
                    animator1.setDuration(300);
                    AnimatorSet animatorSet1 = new AnimatorSet();
                    animatorSet1.play(animator1);
                    animatorSet1.addListener(new AnimatorListenerImpl() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            popupWindow.dismiss();
                            InputMethodManager imm = (InputMethodManager) parent.getSystemService(Context.INPUT_METHOD_SERVICE);
                            //得到InputMethodManager的实例
                            if (imm != null) {
//                                imm.hideSoftInputFromWindow(parent.getCurrentFocus().getWindowToken()
//                                        , InputMethodManager.HIDE_NOT_ALWAYS);
                            }
//					Message msg = handler.obtainMessage();
//					msg.what = MainPage.DISMISS_SOFT_INPUT;
//					handler.sendMessage(msg);
                        }
                    });
                    animatorSet1.start();
                    break;
                default:
                    break;
            }
        }
    }

    private void save() {
        if (currentItemDetail == null) {
            currentItemDetail = new ItemDetail();
            currentItemDetail
                    .setId(CommonSQL.getNextSeq(TableNames.ItemDetail));
            currentItemDetail.setRestaurantId(App.instance.getRestaurantId());
            currentItemDetail.setRevenueId(App.instance.getRevenueCenter()
                    .getId());
            currentItemDetail.setItemName(et_new_open_item_name.getText()
                    .toString());
            currentItemDetail.setPrice(et_new_open_item_price.getText()
                    .toString());
            currentItemDetail.setIsActive(1);
            currentItemDetail.setItemType(ParamConst.ITEMDETAIL_TEMP_ITEM);
            if (currentPrinterGroupName != null)
                currentItemDetail.setPrinterId(currentPrinterGroupName.getId());
            if (currentItemMainCategory != null)
                currentItemDetail.setItemMainCategoryId(currentItemMainCategory
                        .getId());
            if (currentItemCategory != null)
                currentItemDetail.setItemCategoryId(currentItemCategory.getId());
            if (currentTaxCategory != null)
                currentItemDetail.setTaxCategoryId(currentTaxCategory.getId());
            else
                currentItemDetail.setTaxCategoryId(0);
            if (checkbox_takeaways.isChecked()) {
                currentItemDetail.setIsPack(ParamConst.CAN_PACK);
            } else {
                currentItemDetail.setIsPack(ParamConst.CANNOT_PACK);
            }
            if (checkbox_delivery.isChecked()) {
                currentItemDetail.setIsTakeout(ParamConst.CAN_TAKEOUT);
            } else {
                currentItemDetail.setIsTakeout(ParamConst.CANNOT_TAKEOUT);
            }
            if (checkbox_discoutable.isChecked()) {
                currentItemDetail.setIsDiscount(ParamConst.ITEM_DISCOUNT);
            } else {
                currentItemDetail.setIsDiscount(ParamConst.ITEM_NO_DISCOUNT);
            }
            currentItemDetail.setUserId(order.getUserId());
            Long time = System.currentTimeMillis();
            currentItemDetail.setCreateTime(time);
            currentItemDetail.setUpdateTime(time);
            ItemDetailSQL.addItemDetailByLocal(currentItemDetail);
            CoreData.getInstance().setItemDetails(
                    ItemDetailSQL.getAllItemDetail());
//			OrderDetail orderDetail = ObjectFactory.getInstance()
//					.getOrderDetail(order, currentItemDetail, 0);

            Gson gson = new Gson();
            String temporaryDish = gson.toJson(currentItemDetail);
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("timeUpdate", 0);
            parameters.put("temporaryDish", temporaryDish);

            SyncCentre.getInstance().getTemporaryDish(parent, parameters, handler);
//            OrderDetail orderDetail = OrderDetailSQL.getUnFreeOrderDetail(
//                    order, currentItemDetail, 0,
//                    ParamConst.ORDERDETAIL_STATUS_WAITER_ADD);
//            OrderDetail orderDetail = ObjectFactory.getInstance()
//                    .getOrderDetail(order, currentItemDetail, 0);
            Message msg = handler.obtainMessage();
            msg.what = Setting.VIEW_EVENT_ADD_ORDER_DETAIL;
            msg.obj = currentItemDetail;
            handler.sendMessage(msg);

//			Message msg = handler.obtainMessage();
//		//	msg.what = MainPage.VIEW_EVENT_ADD_ORDER_DETAIL;
//			msg.obj = orderDetail;
//			handler.sendMessage(msg);
        } else {
            currentItemDetail.setRestaurantId(App.instance.getRestaurantId());
            currentItemDetail.setRevenueId(App.instance.getRevenueCenter()
                    .getId());
            currentItemDetail.setItemName(et_new_open_item_name.getText()
                    .toString());
            currentItemDetail.setPrice(et_new_open_item_price.getText()
                    .toString());
            currentItemDetail.setItemType(ParamConst.ITEMDETAIL_TEMP_ITEM);
            if (currentPrinterGroupName != null)
                currentItemDetail.setPrinterId(currentPrinterGroupName.getId());
            if (currentItemMainCategory != null)
                currentItemDetail.setItemMainCategoryId(currentItemMainCategory
                        .getId());
            if (currentItemCategory != null)
                currentItemDetail.setItemCategoryId(currentItemCategory.getId());
            if (currentTaxCategory != null)
                currentItemDetail.setTaxCategoryId(currentTaxCategory.getId());
            else
                currentItemDetail.setTaxCategoryId(0);
            if (checkbox_takeaways.isChecked()) {
                currentItemDetail.setIsPack(ParamConst.CAN_PACK);
            } else {
                currentItemDetail.setIsPack(ParamConst.CANNOT_PACK);
            }
            if (checkbox_delivery.isChecked()) {
                currentItemDetail.setIsTakeout(ParamConst.CAN_TAKEOUT);
            } else {
                currentItemDetail.setIsTakeout(ParamConst.CANNOT_TAKEOUT);
            }
            if (checkbox_discoutable.isChecked()) {
                currentItemDetail.setIsDiscount(ParamConst.ITEM_DISCOUNT);
            } else {
                currentItemDetail.setIsDiscount(ParamConst.ITEM_NO_DISCOUNT);
            }
            currentItemDetail.setUserId(order.getUserId());
            Long time = System.currentTimeMillis();
            currentItemDetail.setUpdateTime(time);
            ItemDetailSQL.updateItemDetail(currentItemDetail);
            CoreData.getInstance().setItemDetails(
                    ItemDetailSQL.getAllItemDetail());
            Gson gson = new Gson();
            String temporaryDish = gson.toJson(currentItemDetail);
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("timeUpdate", 1);
            parameters.put("temporaryDish", temporaryDish);
            SyncCentre.getInstance().getTemporaryDish(parent, parameters, handler);

//			Message msg = handler.obtainMessage();
//		//	msg.what = MainPage.VIEW_EVENT_SET_DATA;
//			handler.sendMessage(msg);
        }
        currentItemDetail = null;
    }

    class ItemDetailAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return itemDetails.size();
        }

        @Override
        public Object getItem(int arg0) {
            return itemDetails.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            ViewHolder holder = null;
            ItemDetail itemDetail = itemDetails.get(arg0);
            if (arg1 == null) {
                arg1 = inflater.inflate(R.layout.item_itemdetail, null);
                holder = new ViewHolder();
                holder.tv_name = (TextView) arg1.findViewById(R.id.tv_name);
                holder.tv_price = (TextView) arg1.findViewById(R.id.tv_price);
                holder.tv_kitchen = (TextView) arg1
                        .findViewById(R.id.tv_kitchen);
                holder.tv_edit = (TextView) arg1.findViewById(R.id.tv_edit);
                holder.tv_edit.setTag(itemDetail);
                arg1.setTag(holder);
            } else {
                holder = (ViewHolder) arg1.getTag();
            }
            holder.tv_name.setText(itemDetail.getItemName());
            holder.tv_price.setText(itemDetail.getPrice());
            holder.tv_kitchen.setText(CoreData.getInstance()
                    .getPrinter(itemDetail).getPrinterName());
            holder.tv_edit.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    initView2((ItemDetail) v.getTag());
                    vp_open_item.setCurrentItem(1);
                }
            });
            return arg1;
        }

        class ViewHolder {
            public TextView tv_name;
            public TextView tv_price;
            public TextView tv_kitchen;
            public TextView tv_edit;
        }

    }

    class OpenItemPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(viewList.get(position));
            return viewList.get(position);
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view,
                               int position, long id) {
        if (adapterView == null)
            return;
        switch (adapterView.getId()) {
            case R.id.sp_main_category: {
                currentItemMainCategory = (ItemMainCategory) view.findViewById(
                        R.id.tv_name).getTag();
                sp_main_category.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        sp_sub_category = (Spinner) view2
                                .findViewById(R.id.sp_sub_category);
                        ArrayList<ItemCategory> itemCategories = CoreData
                                .getInstance().getItemCategory(
                                        currentItemMainCategory);
                        subCategoryAdapter = new SpinnerAdapter(parent,
                                itemCategories, ITEM_CATEGORY_TYPE);
                        sp_sub_category.setAdapter(subCategoryAdapter);
                        if (currentItemDetail == null) {
                            sp_sub_category.setSelection(0);
                        } else {
                            for (int i = 0; i < itemCategories.size(); i++) {
                                if (itemCategories.get(i).getId().intValue() == currentItemDetail
                                        .getItemCategoryId().intValue()) {
                                    sp_sub_category.setSelection(i);
                                    break;
                                }
                            }
                        }
                    }
                }, 200);
                break;
            }
            case R.id.sp_sub_category: {
                currentItemCategory = (ItemCategory) view
                        .findViewById(R.id.tv_name).getTag();
                break;
            }
            case R.id.sp_tax_group: {
                currentTaxCategory = (TaxCategory) view.findViewById(R.id.tv_name)
                        .getTag();
                break;
            }
            case R.id.sp_kitchen: {
                currentPrinterGroupName = (Printer) view.findViewById(R.id.tv_name).getTag();
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    class SpinnerAdapter extends BaseAdapter {
        private List mList;
        private Context mContext;
        LayoutInflater _LayoutInflater;
        private int type;

        public SpinnerAdapter(Context mContext, List mList, int type) {
            this.mContext = mContext;
            this.mList = mList;
            this.type = type;
            _LayoutInflater = LayoutInflater.from(this.mContext);
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = _LayoutInflater.inflate(R.layout.item_spinner, null);
            TextView tv_name = (TextView) convertView
                    .findViewById(R.id.tv_name);
            Object object = mList.get(position);
            switch (type) {
                case ITEM_MAIN_CATEGORY_TYPE: {
                    ItemMainCategory itemMainCategory = (ItemMainCategory) object;
                    tv_name.setText(itemMainCategory.getMainCategoryName());
                    tv_name.setTag(itemMainCategory);
                    break;
                }
                case ITEM_CATEGORY_TYPE: {
                    ItemCategory itemCategory = (ItemCategory) object;
                    tv_name.setText(itemCategory.getItemCategoryName());
                    tv_name.setTag(itemCategory);
                    break;
                }
                case TAX_CATEGORY_TYPE: {
                    TaxCategory taxCategory = (TaxCategory) object;
                    if (TextUtils.isEmpty(taxCategory.getTaxCategoryName())) {
                        tv_name.setText(parent.getResources().getString(R.string.normal));
                        taxCategory = null;
                    } else {
                        tv_name.setText(taxCategory.getTaxCategoryName());
                    }
                    tv_name.setTag(taxCategory);
                    break;
                }
                case PRINTER_TYPE: {
                    Printer printer = (Printer) object;
                    tv_name.setText(printer.getPrinterGroupName());
                    tv_name.setTag(printer);
                    break;
                }
                default:
                    break;
            }

            return convertView;
        }
    }

}
