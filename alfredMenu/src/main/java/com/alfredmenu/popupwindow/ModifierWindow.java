package com.alfredmenu.popupwindow;

import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.global.BugseeHelper;
import com.alfredbase.global.CoreData;
import com.alfredbase.javabean.ItemDetail;
import com.alfredbase.javabean.ItemModifier;
import com.alfredbase.javabean.Modifier;
import com.alfredbase.utils.DialogFactory;
import com.alfredbase.utils.IntegerUtils;
import com.alfredbase.utils.ScreenSizeUtil;
import com.alfredmenu.activity.MainPage;
import com.alfredmenu.javabean.ModifierVariance;
import com.alfredmenu.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Alex on 2017/4/8.
 */

public class ModifierWindow {
    private BaseActivity context;
    private Handler handler;
    private View parentView;
    private View contentView;
    private PopupWindow popupWindow;
    private ListView lv_modifier;
    private Button btn_save_order_detail;
    private Button btn_description;
    private ModifierAdapter modifierAdapter;
    private ItemDetail itemDetail;
    private List<ModifierVariance> modifierVariances = new ArrayList<ModifierVariance>();
    private TextView tv_description_info;
    private List<Integer> modifierIds = new ArrayList<Integer>();

    public ModifierWindow(BaseActivity context, Handler handler, View parentView) {
        this.context = context;
        this.handler = handler;
        this.parentView = parentView;
        init();
    }

    private void init() {
        contentView = View.inflate(context, R.layout.modifier_popwindow, null);
        popupWindow = new PopupWindow(parentView, (int) ScreenSizeUtil.width, (int) ScreenSizeUtil.height / 3 * 2);
        popupWindow.setAnimationStyle(R.style.bottomInOutStyle);
//        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setContentView(contentView);
        lv_modifier = (ListView) contentView.findViewById(R.id.lv_modifier);
        btn_save_order_detail = (Button) contentView.findViewById(R.id.btn_save_order_detail);
        tv_description_info = (TextView) contentView.findViewById(R.id.tv_description_info);
        btn_description = (Button) contentView.findViewById(R.id.btn_description);
        modifierAdapter = new ModifierAdapter();
        contentView.findViewById(R.id.pop_modifier_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BugseeHelper.buttonClicked(v);
                popupWindow.dismiss();
                handler.sendEmptyMessage(MainPage.VIEW_ENVENT_GET_ORDERDETAILS);
            }
        });
        btn_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BugseeHelper.buttonClicked(v);
                DialogFactory.commonTwoBtnInputDialog(context, false, context.getString(R.string.description), context.getString(R.string.please_input_description), context.getString(R.string.cancel), context.getString(R.string.save), null, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BugseeHelper.buttonClicked(v);
                        EditText editText = (EditText) v;
                        tv_description_info.setText(editText.getText().toString());
                    }
                });
            }
        });
        btn_save_order_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BugseeHelper.buttonClicked(v);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("itemDetail", itemDetail);
                map.put("modifierIds", modifierIds);
                map.put("description", tv_description_info.getText().toString());
                handler.sendMessage(handler.obtainMessage(MainPage.VIEW_EVENT_ADD_ORDER_DETAIL_AND_MODIFIER, map));
                popupWindow.dismiss();
            }
        });
    }

    public void show(ItemDetail itemDetail) {

        if (itemDetail == null)
            return;
        this.itemDetail = itemDetail;
        tv_description_info.setText("");
        popupWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
//        backgroundAlpha(0.5f);//设置半透明
        popupWindow.update();
        initData();
        lv_modifier.setAdapter(modifierAdapter);
        modifierIds.clear();
        modifierVariances.clear();
        varianceModifiers();
    }

    private void varianceModifiers() {

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
            if (modifiers.size() > 0) {
                ModifierVariance modifierVariance = new ModifierVariance();
                modifierVariance.setModifierName1(modifierTitel.get(i).getCategoryName());
                modifierVariance.setModifier(false);
                modifierVariances.add(modifierVariance);
            }
            ModifierVariance modifierVariance = null;
            for (int j = 0; j < modifiers.size(); j++) {
                Modifier modifier = modifiers.get(j);
//                if(modifier.getIsDefault().intValue() == 1){
//                    modifierIds.add(modifier.getId());
//                }
                if (j % 2 == 0) {
                    modifierVariance = new ModifierVariance();
                    modifierVariance.setModifierId1(modifier.getId());
                    modifierVariance.setModifierName1(modifier.getModifierName());
                    modifierVariance.setModifier(true);
                    if (j + 1 == modifiers.size()) {
                        modifierVariances.add(modifierVariance);
                    }
                } else if (modifierVariance != null) {
                    modifierVariance.setModifierId2(modifier.getId());
                    modifierVariance.setModifierName2(modifier.getModifierName());
                    modifierVariances.add(modifierVariance);
                }

            }
        }
    }

    private void backgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = alpha;
        context.getWindow().setAttributes(lp);
    }

    private void initData() {

    }

    protected class ModifierAdapter extends BaseAdapter {

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
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.modifier_item, null);
                viewHold = new ViewHold();
                convertView.setTag(viewHold);
            } else {
                viewHold = (ViewHold) convertView.getTag();
            }

            ModifierVariance modifierVariance = modifierVariances.get(position);
            viewHold.tv_item1 = (TextView) convertView.findViewById(R.id.tv_item1);
//            viewHold.tv_item2 = (TextView) convertView.findViewById(R.id.tv_item2);

            if (modifierVariance.isModifier()) {
                if (!IntegerUtils.isEmptyOrZero(modifierVariance.getModifierId1()) && !TextUtils.isEmpty(modifierVariance.getModifierName1())) {
                    viewHold.tv_item1.setText(modifierVariance.getModifierName1());
                    viewHold.tv_item1.setTag(modifierVariance.getModifierId1());
                } else {
                    viewHold.tv_item1.setText("");
                }
//                if (modifierVariance.isModifier() && !IntegerUtils.isEmptyOrZero(modifierVariance.getModifierId2()) && !TextUtils.isEmpty(modifierVariance.getModifierName2())) {
//                    viewHold.tv_item2.setText(modifierVariance.getModifierName2());
//                    viewHold.tv_item2.setTag(modifierVariance.getModifierId2());
//                }else{
//                    viewHold.tv_item2.setText("");
//                }
                if (modifierIds.contains(viewHold.tv_item1.getTag())) {
                    viewHold.tv_item1.setBackground(context.getResources().getDrawable(R.drawable.modifier_bg_selected));
                    viewHold.tv_item1.setTextColor(context.getResources().getColor(R.color.white));
                } else {
                    viewHold.tv_item1.setBackground(context.getResources().getDrawable(R.drawable.modifier_bg));
                    viewHold.tv_item1.setTextColor(context.getResources().getColor(R.color.black));
                }
//                if(modifierIds.contains(viewHold.tv_item2.getTag())){
//                    viewHold.tv_item2.setBackground(context.getResources().getDrawable(R.drawable.modifier_bg_selected));
//                    viewHold.tv_item2.setTextColor(context.getResources().getColor(R.color.white));
//                }else{
//                    viewHold.tv_item2.setBackground(context.getResources().getDrawable(R.drawable.modifier_bg));
//                    viewHold.tv_item2.setTextColor(context.getResources().getColor(R.color.black));
//                }
                viewHold.tv_item1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BugseeHelper.buttonClicked(v);
                        if (modifierIds.contains(v.getTag())) {
                            modifierIds.remove(v.getTag());
                        } else {
                            modifierIds.add((Integer) v.getTag());
                        }
                        notifyDataSetChanged();
                    }
                });
//                viewHold.tv_item2.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if(modifierIds.contains(v.getTag())){
//                            modifierIds.remove(v.getTag());
//                        }else{
//                            modifierIds.add((Integer)v.getTag());
//                        }
//                        notifyDataSetChanged();
//                    }
//                });

            } else {
                viewHold.tv_item1.setText(modifierVariance.getModifierName1());
                viewHold.tv_item1.setTextColor(context.getResources().getColor(R.color.black));
                viewHold.tv_item1.setOnClickListener(null);
//                viewHold.tv_item2.setOnClickListener(null);
                viewHold.tv_item1.setBackground(null);
//                viewHold.tv_item2.setBackground(null);

            }
            return convertView;
        }

        class ViewHold {
            TextView tv_item1;
//            TextView tv_item2;
        }
    }
}
