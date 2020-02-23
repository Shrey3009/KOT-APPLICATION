package com.alfredmenu.popupwindow;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.global.CoreData;
import com.alfredbase.javabean.ItemDetail;
import com.alfredbase.javabean.ItemModifier;
import com.alfredbase.javabean.Modifier;
import com.alfredbase.javabean.ModifierCheck;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.store.TableNames;
import com.alfredbase.store.sql.CommonSQL;
import com.alfredbase.store.sql.temporaryforapp.ModifierCheckSql;
import com.alfredbase.utils.IntegerUtils;
import com.alfredbase.utils.RemainingStockHelper;
import com.alfredmenu.activity.MainPage;
import com.alfredmenu.global.UIHelp;
import com.alfredmenu.javabean.ModifierCPVariance;
import com.alfredmenu.javabean.ModifierVariance;
import com.alfredmenu.listener.RvItemClickListener;
import com.alfredmenu.adapter.ItemHeaderDecoration;
import com.alfredmenu.adapter.WaiterModifierAdapter;
import com.alfredmenu.R;
import com.alfredmenu.view.ModifierCountView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Alex on 2017/4/8.
 */

public class WaiterModifierCPWindow extends Dialog {
    private BaseActivity context;
    private Handler handler;
    private View parentView;
    private View contentView;
    private PopupWindow popupWindow;
    private RecyclerView lv_modifier;
    private WaiterModifierAdapter waiterModifierAdapter;
    private EditText remark_et;
    private Button btn_save_order_detail;
    private ItemDetail itemDetail;
    private List<ModifierCPVariance> modifierVariances = new ArrayList<ModifierCPVariance>();
    private List<Integer> modifierIds = new ArrayList<Integer>();
    private List<ModifierCPVariance> variances = new ArrayList<ModifierCPVariance>();
    private GridLayoutManager gridLayoutManager;
    private ModifierSetItemCountWindow setItemCountWindow;
    private List<Modifier> modifierTitels = new ArrayList<Modifier>();
    Map<Integer, Integer> seletMap;
    private Order order = new Order();
    private ModifierCountView modifierCountView;

    OrderDetail orderDetail=new OrderDetail();
    private TextView popModifierName;
    public int count = 1;

    /*public WaiterModifierCPWindow(@NonNull Context context) {
        super(context);
    }*/

    public WaiterModifierCPWindow(BaseActivity context, Handler handler, View parentView) {
        super(context);
        this.context = context;
        this.handler = handler;
        this.parentView = parentView;
        init();
    }

    /*public WaiterModifierCPWindow(BaseActivity context, Handler handler, View parentView) {
        this.context = context;
        this.handler = handler;
        this.parentView = parentView;
        init();
    }*/

    private void init() {
        contentView = View.inflate(context, R.layout.waiter_modifiercp_popwindow, null);
        seletMap = new HashMap<Integer, Integer>();
        popupWindow = new PopupWindow(parentView,
                RelativeLayout.LayoutParams.WRAP_CONTENT,
               RelativeLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setAnimationStyle(R.style.bottomInOutStyle);
        popupWindow.setContentView(contentView);
        lv_modifier = (RecyclerView) contentView.findViewById(R.id.lv_modifier);
        remark_et = (EditText) contentView.findViewById(R.id.remark_et);
        remark_et.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        remark_et.setGravity(Gravity.CENTER_VERTICAL);

        remark_et.setHorizontallyScrolling(false);


        modifierCountView = (ModifierCountView) contentView.findViewById(R.id.countView_item1);
        Log.d("orderdetail", ""+orderDetail.getItemNum());

        if( orderDetail.getItemNum()  == null){
            modifierCountView.setInitCount(1);
        }
        if(orderDetail.getItemNum() != null){
            modifierCountView.setInitCount(orderDetail.getItemNum());
        }


        modifierCountView.setIsCanClick(true);
        modifierCountView.setOnCountChange(new ModifierCountView.OnCountChange() {
            @Override
            public void onChange(ModifierVariance modifierVariance, int count, boolean isAdd) {
                Log.d("tes","tes");
                setCount(count);
            }
        });

        popModifierName = (TextView) contentView.findViewById(R.id.pop_modifier_name);
        popModifierName.setText(orderDetail.getItemName());

        btn_save_order_detail = (Button) contentView.findViewById(R.id.btn_save_order_detail);
        contentView.findViewById(R.id.pop_modifier_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCheck();
                popupWindow.dismiss();
                handler.sendEmptyMessage(MainPage.VIEW_ENVENT_GET_ORDERDETAILS);
            }
        });
        btn_save_order_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                StringBuffer showToast = new StringBuffer();
                for (Modifier modifier : modifierTitels) {
                    if (modifier.getMinNumber() > 0) {
                        if (seletMap.containsKey(modifier.getId())) {
                            if (seletMap.get(modifier.getId()) < modifier.getMinNumber()) {
                                showToast.append(modifier.getCategoryName() + " ");
                            }
                        } else {
                            showToast.append(modifier.getCategoryName() + " ");
                        }
                    }
                }
                if (showToast.length() > 0) {

                    UIHelp.showToast(context, showToast.toString());
                    return;
                }

//                if (checkMap.size() == 0) {

                ModifierCheckSql.deleteModifierCheck(orderDetail.getId(),order.getId());
                Map<String, Object> map = new HashMap<String, Object>();
                for (ModifierCPVariance modifierVariance : modifierVariances) {
                    if (modifierIds.size() > 0 && !modifierVariance.isTitle()) {
                        for (int id : modifierIds) {
                            if (id == modifierVariance.getModifierId()) {
                                variances.add(modifierVariance);
                            }
                        }
                    }
                }

                map.put("orderDetail", orderDetail);
                map.put("itemDetail", itemDetail);
                map.put("number", getCount());
                map.put("variances", variances);
                map.put("description", remark_et.getText().toString());
           //     RemainingStockHelper.updateRemainingStockNumByOrder(order);
                handler.sendMessage(handler.obtainMessage(MainPage.VIEW_EVENT_ADD_ORDER_DETAIL_AND_MODIFIER, map));
                popupWindow.dismiss();
//                } else {
//                    StringBuffer checkbuf = new StringBuffer();
//                    Iterator iter = checkMap.entrySet().iterator();
//                    while (iter.hasNext()) {
//                        Map.Entry entry = (Map.Entry) iter.next();
//                        String key = (String) entry.getKey();
//                        checkbuf.append(" " + key + ":");
//                        Map<Integer, String> val = (Map<Integer, String>) entry.getValue();
//                        Iterator iter2 = val.entrySet().iterator();
//                        while (iter2.hasNext()) {
//                            Map.Entry entry2 = (Map.Entry) iter2.next();
//                            String val2 = (String) entry2.getValue();
//                            checkbuf.append(val2 + " ");
////                      String val = (String) entry.getValue();
////                      checkbuf.append("不能少于"+val+"种 .");
//                        }
//                    }


                //  }


            }
        });
    }


    public void setCount(int count){
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    private void addCheck() {

        for (int i = 0; i < modifierTitels.size(); i++) {
             Modifier modifierTitle=modifierTitels.get(i);

            if (modifierTitle.getMinNumber() > 0) {
                ModifierCheck modifierCheck = null;
                modifierCheck = new ModifierCheck();
                modifierCheck.setId(CommonSQL.getNextSeq(TableNames.ModifierCheck));
                modifierCheck.setOrderDetailId(orderDetail.getId());
                modifierCheck.setOrderId(order.getId());
                modifierCheck.setModifierCategoryId(modifierTitle.getId());
                modifierCheck.setItemName(itemDetail.getItemName());

                modifierCheck.setModifierCategoryName(modifierTitle.getCategoryName());
                modifierCheck.setNum(modifierTitle.getMinNumber());
                modifierCheck.setMinNum(modifierTitle.getMinNumber());
                ModifierCheckSql.addModifierCheck(modifierCheck);

        }
    }
    }

    public void show(ItemDetail itemDetail, List<Integer> modifierIds, Order corder, OrderDetail orderDetail) {
        show(itemDetail, modifierIds, corder, "",orderDetail);
    }

    public void show(final ItemDetail itemDetail, List<Integer> modifierIdList, Order order, String description,OrderDetail orderDetail) {
        if (itemDetail == null)
            return;
        this.orderDetail=orderDetail;
        this.order = order;
        this.itemDetail = itemDetail;
        if (!popupWindow.isShowing()) {
            popupWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.update();
        }
        initData(modifierIdList);
        setItemCountWindow = new ModifierSetItemCountWindow(context, parentView, handler);
        if (modifierVariances != null && modifierVariances.size() > 0) {
            lv_modifier.setVisibility(View.VISIBLE);
            gridLayoutManager = new GridLayoutManager(context, 2);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

                @Override
                public int getSpanSize(int position) {
                    return modifierVariances.get(position).isTitle() ? 2 : 1;
                }
            });
            lv_modifier.setLayoutManager(gridLayoutManager);
            waiterModifierAdapter = new WaiterModifierAdapter(context, modifierVariances, this.modifierIds, new RvItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {

                    ModifierCPVariance modifierCPVariance = modifierVariances.get(position);

                    if (modifierIds.contains(modifierCPVariance.getModifierId())) {
                        if (seletMap.containsKey(modifierCPVariance.getCategoryId())) {
                            if (seletMap.get(modifierCPVariance.getCategoryId()) > 1) {
                                seletMap.put(modifierCPVariance.getCategoryId(),
                                        seletMap.get(modifierCPVariance.getCategoryId()) - 1);
                            } else {
                                seletMap.remove(modifierCPVariance.getCategoryId());
                            }
                        }
                        modifierIds.remove(Integer.valueOf(modifierCPVariance.getModifierId()));

                    } else {
                        if (seletMap.containsKey(modifierCPVariance.getCategoryId())) {

                            if (modifierCPVariance.getMax() > seletMap.get(modifierCPVariance.getCategoryId()).intValue()) {
                                seletMap.put(modifierCPVariance.getCategoryId(),
                                        seletMap.get(modifierCPVariance.getCategoryId()) + 1);
                                modifierIds.add(modifierCPVariance.getModifierId());
                            }
                            else if(modifierCPVariance.getMax()==0){
                                seletMap.put(modifierCPVariance.getCategoryId(),
                                        seletMap.get(modifierCPVariance.getCategoryId()) + 1);
                                modifierIds.add(modifierCPVariance.getModifierId());
                            }
                            else {
                                UIHelp.showToast(context, context.getString(R.string.at_only)+"  " + modifierCPVariance.getMax() + " "+context.getString(R.string.items));
                            }
                        } else {
                            seletMap.put(modifierCPVariance.getCategoryId(), 1);
                            modifierIds.add(modifierCPVariance.getModifierId());
                        }
                    }
                    waiterModifierAdapter.notifyDataSetChanged();
                }
            });
            ItemHeaderDecoration decoration = new ItemHeaderDecoration(context, modifierVariances, modifierTitels);
            decoration.setData(modifierVariances);
            lv_modifier.addItemDecoration(decoration);
            lv_modifier.setAdapter(waiterModifierAdapter);
        } else {
            lv_modifier.setVisibility(View.INVISIBLE);
        }

        Log.d("orderdetailitem",""+orderDetail.getItemNum());
        modifierCountView.setInitCount(orderDetail.getItemNum());
        remark_et.setText(description);
        popModifierName.setText(orderDetail.getItemName());
    }

//    public void setList(ModifierVariance modifierVariance){
////        for (ModifierVariance modifierVariance1 : modifierVariances){
////            if (modifierVariance1.getModifierId1() == modifierVariance.getModifierId1()){
////                modifierVariance1.setModQty(modifierVariance.getModQty());
////            }
////        }
////        modifierAdapter.notifyDataSetChanged();
//    }

    private void varianceModifiers() {

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
                if (modifier != null && modifier.getMinNumber() > 0) {
                    Modifier m = new Modifier();
                    m.setId(modifier.getId());
                    m.setRestaurantId(modifier.getRestaurantId());
                    m.setType(modifier.getType());
                    m.setCategoryId(modifier.getCategoryId());


                    m.setCategoryName(modifier.getCategoryName()
                            + " ("
                            + context.getString(R.string.At_least)
                            +" "+ modifier.getMinNumber()
                            +" "+ context.getString(R.string.items) + ")");
                    m.setPrice(modifier.getPrice());
                    m.setModifierName(modifier.getModifierName());
                    m.setIsActive(modifier.getIsActive());
                    m.setIsDefault(modifier.getIsDefault());
                    m.setItemId(modifier.getItemId());
                    m.setIsSet(modifier.getIsSet());
                    m.setQty(modifier.getQty());
                    m.setMustDefault(modifier.getMustDefault());
                    m.setOptionQty(modifier.getOptionQty());
                    m.setMinNumber(modifier.getMinNumber());
                    m.setMaxNumber(modifier.getMaxNumber());
                    modifierTitels.add(m);
                } else {

                    modifierTitels.add(modifier);
                }
            }
        }
        for (int i = 0; i < modifierTitels.size(); i++) {
            Modifier modifierTitle = modifierTitels.get(i);
            ModifierCPVariance modifierCPVarianceTitle = new ModifierCPVariance();
            modifierCPVarianceTitle.setTitle(true);
            modifierCPVarianceTitle.setModifierId(modifierTitle.getId());
            modifierCPVarianceTitle.setModifierName(modifierTitle.getCategoryName());
            modifierCPVarianceTitle.setMin(modifierTitle.getMinNumber());
            modifierCPVarianceTitle.setMax(modifierTitle.getMaxNumber());

//            if (modifierTitle.getMinNumber() > 0) {
//                ModifierCheck modifierCheck = null;
//                modifierCheck = new ModifierCheck();
//                modifierCheck.setId(CommonSQL.getNextSeq(TableNames.ModifierCheck));
//                modifierCheck.setOrderDetailId(itemDetail.getId());
//                modifierCheck.setOrderId(order.getId());
//                modifierCheck.setModifierCategoryId(modifierTitle.getId());
//                modifierCheck.setItemName(itemDetail.getItemName());
//
//                modifierCheck.setModifierCategoryName(modifierTitle.getCategoryName());
//                modifierCheck.setNum(modifierTitle.getMinNumber());
//                modifierCheck.setMinNum(modifierTitle.getMinNumber());
//                ModifierCheckSql.addModifierCheck(modifierCheck);
//            }
            List<Modifier> modifiers = CoreData.getInstance().getModifiers(modifierTitle);
            if (modifiers != null && modifiers.size() > 0) {
                modifierCPVarianceTitle.setTag(String.valueOf(i));
                modifierVariances.add(modifierCPVarianceTitle);
                for (Modifier modifier : modifiers) {
                    ModifierCPVariance modifierCPVariance = new ModifierCPVariance();
                    modifierCPVariance.setTitle(false);
                    modifierCPVariance.setModifierId(modifier.getId());
                    modifierCPVariance.setModifierName(modifier.getModifierName());
                    modifierCPVariance.setTag(String.valueOf(i));
                    modifierCPVariance.setCategoryId(modifier.getCategoryId());
                    modifierCPVariance.setMax(modifierTitle.getMaxNumber());
                    modifierVariances.add(modifierCPVariance);
//                    if (modifier.getIsDefault().intValue() == 1) {
//                        this.modifierIds.add(modifier.getId());
//
//                        seletMap.put(modifier.getId(), modifier.getCategoryId());
//                    }
                }
            }
        }
    }

    public boolean isShowing() {
        return popupWindow.isShowing();
    }

    private void backgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = alpha;
        context.getWindow().setAttributes(lp);
    }

    private void initData(List<Integer> modifierIdList) {
        modifierVariances.clear();
        variances.clear();
        this.modifierIds.clear();
        this.modifierTitels.clear();
        seletMap.clear();
        if (modifierIdList != null && modifierIdList.size() > 0) {
            this.modifierIds.addAll(modifierIdList);

        }
        for (Integer modifierId : modifierIds) {
            Modifier modifier = CoreData.getInstance().getModifier(modifierId);
            if (seletMap.containsKey(modifier.getCategoryId().intValue())) {
                seletMap.put(modifier.getCategoryId().intValue(),
                        seletMap.get(modifier.getCategoryId().intValue()) + modifier.getQty());
            } else {
                seletMap.put(modifier.getCategoryId().intValue(),
                        modifier.getQty());
            }
        }
        varianceModifiers();
    }


    /*protected  class ModifierAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return modifierVariances.size();
        }

        @Override
        public Object getItem(int position) {
            return modifierVariances.get(position);
        }

        @Override

        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            WaiterModifierCPWindow.ModifierAdapter.ViewHold viewHold = null;
            if(convertView == null){
                convertView = View.inflate(context, R.layout.modifier_item, null);
                viewHold = new WaiterModifierCPWindow.ModifierAdapter.ViewHold();
                viewHold.tv_item1 = (TextView) convertView.findViewById(R.id.tv_item1);
                viewHold.rl_item1 = (RelativeLayout) convertView.findViewById(R.id.rl_item1);
                viewHold.countView_item1 = (ModifierCountView) convertView.findViewById(R.id.countView_item1);
                convertView.setTag(viewHold);
            }else{
                viewHold = (WaiterModifierCPWindow.ModifierAdapter.ViewHold) convertView.getTag();
            }

            ModifierVariance modifierVariance = modifierVariances.get(position);
            if(modifierVariance.isModifier()){
                if (modifierVariance.getMustDefault() == 2) {
                    viewHold.countView_item1.setVisibility(View.VISIBLE);
                    viewHold.countView_item1.setIsCanClick(true);
                    viewHold.countView_item1.setInitCount(modifierVariance.getModQty());
                    viewHold.countView_item1.setParam(itemDetail, modifierVariance, setItemCountWindow);
                    viewHold.countView_item1.setOnCountChange(new ModifierCountView.OnCountChange() {
                        @Override
                        public void onChange(ModifierVariance modifierVariance, int count, boolean isAdd) {
                            if (count != 0) {
                                if (variances.size() != 0) {
                                    List<Integer> list = new ArrayList<Integer>();
                                    for (ModifierVariance variance : variances) {
                                        list.add(variance.getModifierId1());
                                    }
                                    if (list.contains(modifierVariance.getModifierId1())) {
                                        for (int i = 0; i < variances.size(); i++) {
                                            if (variances.get(i).getModifierId1() == modifierVariance.getModifierId1()) {
                                                variances.get(i).setModQty(count);
                                            }
                                        }
                                    } else {
                                        modifierVariance.setModQty(count);
                                        variances.add(modifierVariance);
                                    }
                                } else {
                                    modifierVariance.setModQty(count);
                                    variances.add(modifierVariance);
                                }
                            }
                        }
                    });
                    if (modifierVariance.getModQty() != 0){
                        if (variances.size() > 0) {
                            List<Integer> list = new ArrayList<Integer>();
                            for (ModifierVariance variance : variances) {
                                list.add(variance.getModifierId1());
                            }
                            if (list.contains(modifierVariance.getModifierId1())) {
                                for (int i = 0; i < variances.size(); i++) {
                                    if (variances.get(i).getModifierId1() == modifierVariance.getModifierId1()) {
                                        variances.get(i).setModQty(modifierVariance.getModQty());
                                    }
                                }
                            }else {
                                variances.add(modifierVariance);
                            }
                        }else {
                            variances.add(modifierVariance);
                        }
                    }
                } else if (modifierVariance.getMustDefault() == 1 || modifierVariance.getMustDefault() == 0 ){
                    viewHold.countView_item1.setInitCount(0);
                    viewHold.countView_item1.setVisibility(View.GONE);
                    viewHold.countView_item1.setIsCanClick(false);
                    viewHold.rl_item1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(modifierIds.contains(v.getTag())){
                                modifierIds.remove(v.getTag());
                            } else {
                                modifierIds.add((Integer)v.getTag());
                            }
                            notifyDataSetChanged();
                        }
                    });
                }
                if(!IntegerUtils.isEmptyOrZero(modifierVariance.getModifierId1()) && !TextUtils.isEmpty(modifierVariance.getModifierName1())){
                    viewHold.tv_item1.setText(modifierVariance.getModifierName1());
                    viewHold.rl_item1.setTag(modifierVariance.getModifierId1());
                } else {
                    viewHold.tv_item1.setText("");
                }
                if(modifierIds.contains(viewHold.rl_item1.getTag())){
                    viewHold.rl_item1.setBackground(context.getResources().getDrawable(R.drawable.modifier_bg_selected));
                    viewHold.tv_item1.setTextColor(context.getResources().getColor(R.color.white));
                } else {
                    viewHold.rl_item1.setBackground(context.getResources().getDrawable(R.drawable.modifier_bg));
                    viewHold.tv_item1.setTextColor(context.getResources().getColor(R.color.black));
                }

            } else {
                viewHold.countView_item1.setVisibility(View.GONE);
                viewHold.tv_item1.setText(modifierVariance.getModifierName1());
                viewHold.tv_item1.setTextColor(context.getResources().getColor(R.color.black));
                viewHold.rl_item1.setOnClickListener(null);
                viewHold.rl_item1.setBackground(null);
            }
            return convertView;
        }
        class ViewHold{
            TextView tv_item1;
            RelativeLayout rl_item1;
            ModifierCountView countView_item1;
        }
    }*/

}
