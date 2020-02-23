package com.alfredposclient.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.ParamConst;
import com.alfredbase.global.CoreData;
import com.alfredbase.javabean.ItemDetail;
import com.alfredbase.javabean.ItemModifier;
import com.alfredbase.javabean.Modifier;
import com.alfredbase.javabean.ModifierCheck;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.javabean.OrderModifier;
import com.alfredbase.javabean.Printer;
import com.alfredbase.store.sql.OrderDetailSQL;
import com.alfredbase.store.sql.OrderModifierSQL;
import com.alfredbase.store.sql.temporaryforapp.ModifierCheckSql;
import com.alfredbase.utils.ButtonClickTimer;
import com.alfredbase.utils.LogUtil;
import com.alfredbase.utils.ObjectFactory;
import com.alfredbase.utils.ScreenSizeUtil;
import com.alfredbase.utils.TextTypeFace;
import com.alfredposclient.R;
import com.alfredposclient.activity.MainPage;
import com.alfredposclient.global.UIHelp;

import java.util.ArrayList;
import java.util.List;

public class ModifierView extends LinearLayout implements OnClickListener {
    //	private static final int ROW_COUNT = 4;
    public static final int ITEM_SIZE = 100;
    public static final int MARGIN_SIZE = 10;
    private Context context;
    private BaseActivity parent;
    private LinearLayout ll_detail;
    private TextView tv_type,tv_type_min;
    private Handler handler;
    private TextTypeFace textTypeFace;
    private List<OrderModifier> orderModifiers;
    private int num;

    public ModifierView(Context context) {
        super(context);
        this.parent = (BaseActivity) context;
        init(context);
    }

    public ModifierView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        View.inflate(context, R.layout.dish_supplement_view, this);
        ll_detail = (LinearLayout) findViewById(R.id.ll_detail);
        tv_type = (TextView) findViewById(R.id.tv_type);
        tv_type_min=(TextView)findViewById(R.id.tv_type_min) ;
        initTextTypeFace();
    }


    public void setParams(final Order order, final OrderDetail orderDetail,
                          final ItemModifier itemModifier, final Handler mHandler, final int height) {
        num = 0;
//		int count = (int)Math.floor(height-ScreenSizeUtil.dip2px(parent,70))/
//				(ScreenSizeUtil.dip2px(parent,ModifierView.MARGIN_SIZE * 2)+(ScreenSizeUtil.dip2px(parent,ModifierView.ITEM_SIZE)));
        int count = (int) Math.floor(height - ScreenSizeUtil.dip2px(parent, 120)) /
                ((ScreenSizeUtil.dip2px(parent, 2 * MARGIN_SIZE)) + (ScreenSizeUtil.dip2px(parent, ModifierView.ITEM_SIZE)));
        this.handler = mHandler;
        orderModifiers = OrderModifierSQL
                .getOrderModifiers(order, orderDetail);
        final Modifier modifier_type = CoreData.getInstance().getModifier(
                itemModifier);
        tv_type.setText(modifier_type.getCategoryName());
        if(modifier_type.getMinNumber()>0)
        {
            tv_type_min.setVisibility(VISIBLE);
        }else {
            tv_type_min.setVisibility(GONE);
        }

        tv_type_min.setText(context.getResources().getString(R.string.at_least)+" "+modifier_type.getMinNumber()+" "+context.getResources().getString(R.string.items));
        List<Modifier> modifiers = CoreData.getInstance().getModifiers(
                modifier_type);
        int childCount = modifiers.size();
        int row_count = childCount / count;
        if (childCount % count != 0)
            row_count++;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.MATCH_PARENT);
        params.weight = 1;
        int addCount = 0;
        for (int i = 0; i < row_count; i++) {
            LinearLayout layout = new LinearLayout(context);
            layout.setLayoutParams(params);
            layout.setOrientation(LinearLayout.VERTICAL);
            for (int j = 0; j < count; j++) {
                if (addCount >= childCount)
                    break;
                final Modifier modifier = modifiers.get(i * count + j);
                TextView textView = new TextView(context);
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        ScreenSizeUtil.dip2px(parent, ITEM_SIZE), ScreenSizeUtil.dip2px(parent, ITEM_SIZE));
                param.bottomMargin = ScreenSizeUtil.dip2px(parent, MARGIN_SIZE);
                param.topMargin = ScreenSizeUtil.dip2px(parent, MARGIN_SIZE);
                param.leftMargin = ScreenSizeUtil.dip2px(parent, MARGIN_SIZE);
                param.rightMargin = ScreenSizeUtil.dip2px(parent, MARGIN_SIZE);
                textView.setLayoutParams(param);
                textView.setGravity(Gravity.CENTER);
                OrderModifier orderModifier = CoreData.getInstance().getOrderModifier(orderModifiers,
                        modifier);
                if (orderModifier != null
                        && orderModifier.getStatus().intValue() == ParamConst.ORDER_MODIFIER_STATUS_NORMAL) {
                    textView.setBackgroundResource(R.drawable.box_modifier_click);
                    textView.setTextColor(Color.WHITE);
                    num++;
                } else {
                    textView.setBackgroundResource(R.drawable.box_modifier);
                    textView.setTextColor(Color.BLACK);
                }

//				LogUtil.e("ModifierView", "==å®½===" +modifier_type.getCategoryName() + "é«====" + childCount+"=="+modifier.getModifierName());
                textView.setText(modifier.getModifierName());
                textTypeFace.setTrajanProRegular(textView);
                textView.setTag(modifier);
                textView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!ButtonClickTimer.canClick(v)) {
                            return;
                        }
                        int ods = orderDetail.getOrderDetailStatus();

                        //: 零时阻止编辑已经发送到厨房的modifier
                        if (ods == ParamConst.ORDERDETAIL_STATUS_KOTPRINTERD) {
                            return;
                        }

                        //
                        if (ods != ParamConst.ORDERDETAIL_STATUS_ADDED) {
                            orderDetail.setOrderDetailStatus(ParamConst.ORDERDETAIL_STATUS_ADDED);
                            OrderDetailSQL.updateOrderDetail(orderDetail);
                        }
                        Modifier tag = (Modifier) v.getTag();

                        OrderModifier orderModifier = CoreData.getInstance().getOrderModifier(
                                orderModifiers, tag);
                        if (orderModifier != null) {
                            if (orderModifier.getStatus().intValue() == ParamConst.ORDER_MODIFIER_STATUS_NORMAL) {
                                orderModifier
                                        .setStatus(ParamConst.ORDER_MODIFIER_STATUS_DELETE);
                                orderModifier.setUpdateTime(System
                                        .currentTimeMillis());
                                OrderModifierSQL
                                        .updateOrderModifier(orderModifier);
                                num--;
                            } else {
                                System.out.println("===" + (orderDetail.getIsSet() == ParamConst.IS_SET_COMBO));
                                System.out.println("===" + (modifier_type.getMustDefault() >= ParamConst.MODIFIER_MUST_DEFAULT_SELECT));
                                System.out.println("===" + (num >= modifier_type.getOptionQty()));
                                if (orderDetail.getIsSet() == ParamConst.IS_SET_COMBO
                                        && modifier_type.getMustDefault() >= ParamConst.MODIFIER_MUST_DEFAULT_SELECT
                                        && num >= modifier_type.getOptionQty()) {
                                    UIHelp.showShortToast(parent, String.format(parent.getResources().getString(R.string.the_most_selections), modifier_type.getOptionQty()));
                                    return;
                                }
                                orderModifier.setUpdateTime(System
                                        .currentTimeMillis());
                                orderModifier
                                        .setStatus(ParamConst.ORDER_MODIFIER_STATUS_NORMAL);
                                OrderModifierSQL
                                        .updateOrderModifier(orderModifier);
                                num++;
                            }
                        } else {
                            System.out.println(orderDetail.getIsSet() == ParamConst.IS_SET_COMBO);
                            System.out.println(modifier_type.getMustDefault() >= ParamConst.MODIFIER_MUST_DEFAULT_SELECT);
                            System.out.println(num >= modifier_type.getOptionQty());
                            if (orderDetail.getIsSet() == ParamConst.IS_SET_COMBO
                                    && modifier_type.getMustDefault() >= ParamConst.MODIFIER_MUST_DEFAULT_SELECT
                                    && num >= modifier_type.getOptionQty()) {
                                UIHelp.showShortToast(parent, String.format(parent.getResources().getString(R.string.the_most_selections), modifier_type.getOptionQty()));
                                return;
                            }
                            ItemDetail itemDetail = CoreData.getInstance().getItemDetailByTemplateId(tag.getItemId());
                            int printId = 0;
                            if (itemDetail != null) {
                                ArrayList<Printer> prints = CoreData.getInstance().getPrintersInGroup(itemDetail.getPrinterId().intValue());
                                if (prints.size() == 0) {
                                    printId = 0;
                                } else {
                                    printId = prints.get(0).getId().intValue();
                                }

                                //TODO: assign printerGroupId not printerId
                                printId = itemDetail.getPrinterId();
                            }

                            int max = modifier_type.getMaxNumber();
                            orderModifier = ObjectFactory.getInstance().getOrderModifier(order, orderDetail, tag, printId);
                            if (max > 0 && num <= max - 1) {

                                OrderModifierSQL.addOrderModifier(orderModifier);
                                num++;
                            } else if (max == 0) {

                                OrderModifierSQL.addOrderModifier(orderModifier);
                                num++;

                            } else {

                                UIHelp.showShortToast(parent, context.getString(R.string.at_only)+" " + max +" "+ context.getString(R.string.items));
                            }


                        }

                        int min = modifier_type.getMinNumber();
                        if (min > 0) {
                            int checkNim = 0;
                            checkNim = min - num;
                            ModifierCheckSql.update(checkNim, tag.getCategoryId(), orderModifier.getOrderDetailId(),order.getId());
                        }

                        //	LogUtil.e("ModifierView", "==å®½===" +num);
                        List<ModifierCheck> mc = ModifierCheckSql.getAllModifierCheck(order.getId());
                        LogUtil.e("ModifierView", mc.size() + "==å®½===" + num);
                        setParams(order, orderDetail, itemModifier, mHandler, height);// 回调，刷新数据
//						refreshView((TextView)v, orderModifier);
//						orderModifiers= OrderModifierSQL
//								.getOrderModifiers(order, orderDetail);
                        Message msg = handler.obtainMessage();
                        msg.what = MainPage.VIEW_EVENT_SET_DATA;
                        handler.sendMessage(msg);
                    }


                });
                layout.addView(textView);
                addCount++;
            }
            if (i == 0) {
                ll_detail.removeAllViews();
            }
            ll_detail.addView(layout);
        }
    }

    private void refreshView(TextView textView, OrderModifier orderModifier) {
        if (orderModifier != null
                && orderModifier.getStatus().intValue() == ParamConst.ORDER_MODIFIER_STATUS_NORMAL) {
            textView.setBackgroundResource(R.drawable.box_modifier_click);
            textView.setTextColor(parent.getResources().getColor(R.color.white));
        } else {
            textView.setBackgroundResource(R.drawable.box_modifier);
            textView.setTextColor(parent.getResources().getColor(R.color.black));
        }
        postInvalidate();
    }

    @Override
    public void onClick(View v) {

    }

    private void initTextTypeFace() {
        textTypeFace = TextTypeFace.getInstance();
        textTypeFace.setTrajanProBlod(tv_type);
    }
}
