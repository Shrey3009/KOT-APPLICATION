package com.alfredmenu.popupwindow;

import android.os.Handler;
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
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.global.CoreData;
import com.alfredbase.javabean.ItemDetail;
import com.alfredbase.javabean.ItemModifier;
import com.alfredbase.javabean.Modifier;
import com.alfredbase.utils.IntegerUtils;
import com.alfredbase.utils.ScreenSizeUtil;
import com.alfredmenu.activity.MainPage;
import com.alfredmenu.javabean.ModifierVariance;
import com.alfredmenu.view.ModifierCountView;
import com.alfredmenu.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Alex on 2017/4/8.
 */

public class WaiterModifierWindow {
    private BaseActivity context;
    private Handler handler;
    private View parentView;
    private View contentView;
    private PopupWindow popupWindow;
    private GridView lv_modifier;
    private EditText remark_et;
    private Button btn_save_order_detail;
    private ModifierAdapter modifierAdapter;
    private ItemDetail itemDetail;
    private List<ModifierVariance> modifierVariances = new ArrayList<ModifierVariance>();
    private List<Integer> modifierIds = new ArrayList<Integer>();
    private List<ModifierVariance> variances = new ArrayList<ModifierVariance>();
    private ModifierSetItemCountWindow setItemCountWindow;
    private ModifierCountView modifierCountView;
    public int count;

    public WaiterModifierWindow(BaseActivity context, Handler handler, View parentView) {
        this.context = context;
        this.handler = handler;
        this.parentView = parentView;
        init();
    }

    private void init(){
        contentView = View.inflate(context, R.layout.waiter_modifier_popwindow, null);
        popupWindow = new PopupWindow(parentView, (int)ScreenSizeUtil.width, (int)ScreenSizeUtil.height/ 3 * 2);
        popupWindow.setAnimationStyle(R.style.bottomInOutStyle);
        popupWindow.setContentView(contentView);
        lv_modifier = (GridView) contentView.findViewById(R.id.lv_modifier);
        remark_et = (EditText) contentView.findViewById(R.id.remark_et);
        remark_et.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        remark_et.setGravity(Gravity.CENTER_VERTICAL);

        remark_et.setHorizontallyScrolling(false);

        btn_save_order_detail = (Button) contentView.findViewById(R.id.btn_save_order_detail);
        contentView.findViewById(R.id.pop_modifier_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                handler.sendEmptyMessage(MainPage.VIEW_ENVENT_GET_ORDERDETAILS);
            }
        });


        modifierCountView = (ModifierCountView) contentView.findViewById(R.id.countView_item1);
        modifierCountView.setInitCount(1);
        /*if(orderDetail.getItemNum() == 0){
            modifierCountView.setInitCount(0);
        }else{
            modifierCountView.setInitCount(orderDetail.getItemNum());
        }*/

        modifierCountView.setIsCanClick(true);
        modifierCountView.setOnCountChange(new ModifierCountView.OnCountChange() {
            @Override
            public void onChange(ModifierVariance modifierVariance, int count, boolean isAdd) {
                Log.d("tes","tes");
                setCount(count);
            }
        });

        btn_save_order_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> map = new HashMap<String, Object>();
                for (ModifierVariance modifierVariance : modifierVariances){
                    if (modifierIds.size() > 0) {
                        for (int id : modifierIds) {
                            if (id == modifierVariance.getModifierId1()){
//                                modifierVariance.setModQty(1);
                                variances.add(modifierVariance);
                            }
                        }
                    }
                }
//                map.put("itemDetail", itemDetail);
                map.put("itemDetail", itemDetail);
                map.put("number", getCount());
                map.put("variances", variances);
                map.put("description", remark_et.getText().toString());
                handler.sendMessage(handler.obtainMessage(MainPage.VIEW_EVENT_ADD_ORDER_DETAIL_AND_MODIFIER, map));
                popupWindow.dismiss();
            }
        });
    }

    public void setCount(int count){
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void show(ItemDetail itemDetail) {
        show(itemDetail, null, "");

    }
    public void show(ItemDetail itemDetail, List<Integer> modifierIds, String description){
        if(itemDetail == null)
            return;
        this.itemDetail = itemDetail;
        if(!popupWindow.isShowing()) {
            popupWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
//        backgroundAlpha(0.5f);//设置半透明
            popupWindow.update();
        }
        initData();
        setItemCountWindow = new ModifierSetItemCountWindow(context, parentView, handler);
        modifierAdapter = new ModifierAdapter();
        lv_modifier.setAdapter(modifierAdapter);
        this.modifierIds.clear();
        if(modifierIds != null && modifierIds.size() > 0){
            this.modifierIds.addAll(modifierIds);
        }
        modifierVariances.clear();
        variances.clear();
        remark_et.setText(description);
        varianceModifiers();
    }

    public void setList(ModifierVariance modifierVariance){
        for (ModifierVariance modifierVariance1 : modifierVariances){
            if (modifierVariance1.getModifierId1() == modifierVariance.getModifierId1()){
                modifierVariance1.setModQty(modifierVariance.getModQty());
            }
        }
        modifierAdapter.notifyDataSetChanged();
    }

    private void varianceModifiers(){

        List<ItemModifier> itemModifiers = CoreData.getInstance()
                .getItemModifiers(itemDetail);
        List<Modifier> modifierTitel = new ArrayList<Modifier>();
        if (!itemModifiers.isEmpty()) {
            for (ItemModifier itemModifier : itemModifiers) {
                modifierTitel.add(CoreData.getInstance().getModifier(
                        itemModifier));
            }
        }
        for (int i = 0; i < modifierTitel.size(); i++) {
            List<Modifier> modifiers = CoreData.getInstance().getModifiers(modifierTitel.get(i));
            if(modifiers.size() > 0){
                ModifierVariance modifierVariance = new ModifierVariance();
                modifierVariance.setModifierName1(modifierTitel.get(i).getCategoryName());
                modifierVariance.setModifier(false);
                modifierVariances.add(modifierVariance);

                ModifierVariance modifierVariance1 = new ModifierVariance();
                modifierVariance1.setModifierName1("");
                modifierVariance1.setModifier(false);
                modifierVariances.add(modifierVariance1);
            }

            ModifierVariance modifierVariance = null;
            for(int j = 0; j < modifiers.size(); j++){
                Modifier modifier = modifiers.get(j);
                modifierVariance = new ModifierVariance();
                modifierVariance.setModifierId1(modifier.getId());
                modifierVariance.setModifierName1(modifier.getModifierName());
                modifierVariance.setModifier(true);
                modifierVariance.setMustDefault(modifier.getMustDefault());
                modifierVariances.add(modifierVariance);
            }
        }
    }
    public boolean isShowing(){
        return popupWindow.isShowing();
    }
    private void backgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = alpha;
        context.getWindow().setAttributes(lp);
    }

    private void initData(){

    }

    protected  class ModifierAdapter extends BaseAdapter {

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
            ViewHold viewHold = null;
            if(convertView == null){
                convertView = View.inflate(context, R.layout.modifier_item, null);
                viewHold = new ViewHold();
                viewHold.tv_item1 = (TextView) convertView.findViewById(R.id.tv_item1);
                viewHold.rl_item1 = (RelativeLayout) convertView.findViewById(R.id.rl_item1);
                viewHold.countView_item1 = (ModifierCountView) convertView.findViewById(R.id.countView_item1);
                convertView.setTag(viewHold);
            }else{
                viewHold = (ViewHold) convertView.getTag();
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
    }
}
