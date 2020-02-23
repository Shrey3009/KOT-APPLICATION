package com.alfredposclient.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alfredbase.ParamConst;
import com.alfredbase.ParamHelper;
import com.alfredbase.SureDialog;
import com.alfredbase.global.BugseeHelper;
import com.alfredbase.global.CoreData;
import com.alfredbase.javabean.BarcodeDetail;
import com.alfredbase.javabean.ItemDetail;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.javabean.OrderSplit;
import com.alfredbase.store.sql.OrderDetailSQL;
import com.alfredbase.store.sql.OrderSQL;
import com.alfredbase.store.sql.OrderSplitSQL;
import com.alfredbase.store.sql.TableInfoSQL;
import com.alfredbase.store.sql.UserCustomSQL;
import com.alfredbase.utils.ButtonClickTimer;
import com.alfredbase.utils.CommonUtil;
import com.alfredbase.utils.DialogFactory;
import com.alfredbase.utils.IntegerUtils;
import com.alfredbase.utils.ObjectFactory;
import com.alfredbase.utils.TextTypeFace;
import com.alfredposclient.R;
import com.alfredposclient.activity.MainPage;
import com.alfredposclient.global.UIHelp;
import com.alfredposclient.popupwindow.DiscountWindow.ResultCall;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainPageOperatePanel extends LinearLayout implements
        OnClickListener {
    //	private BaseActivity parent;
    private Handler handler;
    private TextView tv_order_no;
    private TextView tv_pax;
    private TextView tv_grand_total;
    private Order order;
    private List<OrderDetail> orderDetails;
    private MainPage parent;
//	private TableInfo tables;

    private EditText et_bar_code;

    public MainPageOperatePanel(Context context) {
        super(context);
        init(context);
    }

    public MainPageOperatePanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void setParams(MainPage parent, Order order,
                          List<OrderDetail> orderDetails, Handler handler) {
        this.parent = parent;
//		this.tables = tables;
        this.handler = handler;
        this.order = order;
        this.orderDetails = orderDetails;
        setData();
    }

    private void init(Context context) {

        View.inflate(context, R.layout.operate_panel, this);
        findViewById(R.id.tv_close_bill).setOnClickListener(this);
        findViewById(R.id.tv_tables).setOnClickListener(this);
        findViewById(R.id.tv_discount).setOnClickListener(this);
        findViewById(R.id.tv_unseat).setOnClickListener(this);
        findViewById(R.id.tv_open_item).setOnClickListener(this);
        findViewById(R.id.tv_open_item_waiting_list).setOnClickListener(this);
        findViewById(R.id.tv_print_bill).setOnClickListener(this);
        findViewById(R.id.tv_transfer_table).setOnClickListener(this);
        findViewById(R.id.tv_kick_cashdrawer).setOnClickListener(this);
        findViewById(R.id.rl_pax).setOnClickListener(this);
        findViewById(R.id.tv_take_away).setOnClickListener(this);
        findViewById(R.id.tv_fire).setOnClickListener(this);
        findViewById(R.id.tv_split_by_pax).setOnClickListener(this);
        tv_order_no = (TextView) findViewById(R.id.tv_order_no);
        tv_pax = (TextView) findViewById(R.id.tv_pax);
    }

    private void initOrder() {
        findViewById(R.id.tv_bill_content).setVisibility(View.VISIBLE);
        findViewById(R.id.rl_print_colse_bill).setVisibility(View.VISIBLE);
        findViewById(R.id.tv_misc).setVisibility(View.VISIBLE);
        findViewById(R.id.rl_misc).setVisibility(View.VISIBLE);
        findViewById(R.id.rl_split_fire).setVisibility(View.VISIBLE);
        findViewById(R.id.tv_open_item_waiting_list).setVisibility(View.GONE);
        findViewById(R.id.tv_order_id).setVisibility(View.VISIBLE);
        TextView cancel = (TextView) findViewById(R.id.tv_cancel_waiting_list);
        cancel.setVisibility(View.GONE);
        ((TextView) findViewById(R.id.tv_transfer_table)).setText(this.getContext().getString(R.string.transfer_table));
        tv_order_no.setText(ParamHelper.getPrintOrderNO(order.getOrderNo()));
        et_bar_code = (EditText) findViewById(R.id.et_bar_code);
        et_bar_code.setFocusable(true);
        et_bar_code.requestFocus();
        et_bar_code.setOnKeyListener(new OnKeyListener()
        {
            @Override
            public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
        if (KeyEvent.KEYCODE_ENTER == arg1 && arg2.getAction() == KeyEvent.ACTION_DOWN)
        {
            SureDialog sureDialog = new SureDialog(parent);
            String barCode = et_bar_code.getText().toString();
            barCode = barCode.replaceAll("\\s+","");
            String itemBarCode = barCode;
            String priceBarCode = barCode;
            BigDecimal itemPrice = new BigDecimal(0);
            BigDecimal calculatedWeight;
            Boolean weightCalculator = false;
            int barCodeLength = 0;
            int currentBarcodeLength = barCode.length();
            for(BarcodeDetail barCodeDetail: UserCustomSQL.getAllBarCodeProperties())
            {
                weightCalculator = true;
                try
                {
                    switch(barCodeDetail.getBarCodeName())
                    {
                        case "item_detail_id":
                            itemBarCode = itemBarCode.substring(0, barCodeDetail.getBarCodeLength());
                        case "item_price":
                            priceBarCode = priceBarCode.substring(priceBarCode.length() - barCodeDetail.getBarCodeLength());
                            String frontPrice = priceBarCode.substring(0, barCodeDetail.getBarCodePriceFront());
                            String backPrice = priceBarCode.substring(priceBarCode.length() - (priceBarCode.length() - frontPrice.length()));
                            String ignoreLastDigit = backPrice.substring(0, backPrice.length() - 1);
                            itemPrice = new BigDecimal(frontPrice + "." + ignoreLastDigit);
                    }
                    barCodeLength = barCodeLength + barCodeDetail.getBarCodeLength();
                    if(barCodeLength != currentBarcodeLength)
                    {
                        weightCalculator = false;
                    }
                }
                catch (Exception e)
                {
                        weightCalculator = false;
                        sureDialog.show(false);
                }
            }

                if (TextUtils.isEmpty(barCode))
                {
                    UIHelp.showToast(parent, parent.getString(R.string.barcode_cannot_empty));
                    return false;
                }
                ItemDetail itemDetail = CoreData.getInstance().getItemDetailByBarCode(itemBarCode);
                OrderDetail orderDetail = null;
                et_bar_code.postDelayed(new Runnable()
                {
                    @Override
                    public void run() {
                        et_bar_code.setText("");
                        et_bar_code.requestFocus();
                    }
                }, 500);
                if (itemDetail != null)
                {
                    orderDetail = ObjectFactory.getInstance().getOrderDetail(order, itemDetail, 0);
                    if(weightCalculator)
                    {
                        try
                        {
                            calculatedWeight = itemPrice.divide(new BigDecimal(orderDetail.getItemPrice()), 4, RoundingMode.HALF_UP);
                            orderDetail.setWeight(String.valueOf(calculatedWeight));
                        }
                        catch (Exception e)
                        {
                            UIHelp.showToast(parent, "Weight is not changed, barcode format is invalid!");
                        }
                    }
                }
                if (orderDetail == null) {
                    sureDialog.show(false);
                    return false;
                }
                Message msg = handler.obtainMessage();
                msg.what = MainPage.VIEW_EVENT_ADD_ORDER_DETAIL;
                msg.obj = orderDetail;
                handler.sendMessage(msg);
                sureDialog.show(true);
                return true;
            }
            return false;
            }
        });
        initTextTypeFace();
    }

    private void initWaitingList() {
        findViewById(R.id.tv_bill_content).setVisibility(View.GONE);
        findViewById(R.id.rl_print_colse_bill).setVisibility(View.GONE);
        findViewById(R.id.tv_misc).setVisibility(View.GONE);
        findViewById(R.id.rl_misc).setVisibility(View.GONE);
        findViewById(R.id.rl_split_fire).setVisibility(View.GONE);
        TextView tvCancel = (TextView) findViewById(R.id.tv_cancel_waiting_list);
        tvCancel.setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.tv_transfer_table)).setText(this.getContext().getString(R.string.assign_table));
        findViewById(R.id.tv_order_id).setVisibility(View.GONE);
        findViewById(R.id.tv_open_item_waiting_list).setVisibility(View.VISIBLE);
        tv_order_no.setText(getContext().getString(R.string.waiting_list));
        tvCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                TableInfoSQL.deleteTableInfo(order.getTableId());
                OrderDetailSQL.deleteOrderDetailByOrder(order);
                OrderSQL.deleteOrder(order);

                handler.sendMessage(handler
                        .obtainMessage(
                                MainPage.REFRESH_TABLES_STATUS));
                handler.sendMessage(handler
                        .obtainMessage(
                                MainPage.VIEW_EVENT_SHOW_TABLES));
            }
        });

    }


    private void initTextTypeFace() {
        TextTypeFace textTypeFace = TextTypeFace.getInstance();
        textTypeFace.setTrajanProBlod((TextView) findViewById(R.id.tv_close_bill));
        textTypeFace.setTrajanProBlod((TextView) findViewById(R.id.tv_tables));
        textTypeFace.setTrajanProBlod((TextView) findViewById(R.id.tv_discount));
        textTypeFace.setTrajanProBlod((TextView) findViewById(R.id.tv_unseat));
        textTypeFace.setTrajanProBlod((TextView) findViewById(R.id.tv_open_item));
        textTypeFace.setTrajanProBlod((TextView) findViewById(R.id.tv_print_bill));
        textTypeFace.setTrajanProBlod((TextView) findViewById(R.id.tv_transfer_table));
        textTypeFace.setTrajanProBlod((TextView) findViewById(R.id.tv_kick_cashdrawer));
        textTypeFace.setTrajanProBlod((TextView) findViewById(R.id.tv_order_id));
        textTypeFace.setTrajanProBlod((TextView) findViewById(R.id.tv_order_no));
        textTypeFace.setTrajanProBlod((TextView) findViewById(R.id.tv_pax_title));
        textTypeFace.setTrajanProBlod((TextView) findViewById(R.id.tv_pax));
        textTypeFace.setTrajanProBlod((TextView) findViewById(R.id.tv_bill_content));
//		textTypeFace.setTrajanProBlod((TextView) findViewById(R.id.tv_split));
        textTypeFace.setTrajanProBlod((TextView) findViewById(R.id.tv_table_content));
        textTypeFace.setTrajanProBlod((TextView) findViewById(R.id.tv_misc));
//		textTypeFace.setTrajanProBlod((TextView) findViewById(R.id.tv_edit_kot));
        textTypeFace.setTrajanProBlod((TextView) findViewById(R.id.tv_take_away));
        textTypeFace.setTrajanProBlod((TextView) findViewById(R.id.tv_fire));
        textTypeFace.setTrajanProBlod((TextView) findViewById(R.id.tv_split_by_pax));
        textTypeFace.setTrajanProBlod((TextView) findViewById(R.id.tv_cancel_waiting_list));
        textTypeFace.setTrajanProBlod((TextView) findViewById(R.id.tv_open_item_waiting_list));

    }

    private void setData() {
        tv_pax.setText(order.getPersons() + "");
        if (order.getTableId() < 0) {
            initWaitingList();
        } else {
            initOrder();
        }
    }


    @Override
    public void onClick(View v) {
		if (ButtonClickTimer.canClick()) {
            switch (v.getId()) {
                case R.id.tv_close_bill: {
                    Message msg = handler.obtainMessage();
                    msg.what = MainPage.VIEW_EVENT_SHOW_CLOSE_ORDER_WINDOW;
                    handler.sendMessage(msg);
                    break;
                }
                case R.id.tv_tables: {
                    Message msg = handler.obtainMessage();
                    msg.what = MainPage.VIEW_EVENT_SHOW_TABLES;
                    handler.sendMessage(msg);
                    break;
                }
                case R.id.tv_discount: {
                    if (orderDetails.isEmpty() || OrderSplitSQL.getFinishedOrderSplits(order.getId()).size() > 0) {
                        UIHelp.showToast(parent, parent.getResources().getString(R.string.order_first));
                        return;
                    }
                    boolean canDiscount = true;
                    for (OrderDetail orderDetail : orderDetails) {
                        if (orderDetail.getDiscountType() != ParamConst.ORDERDETAIL_DISCOUNT_TYPE_RATE
                                && orderDetail.getDiscountType() != ParamConst.ORDERDETAIL_DISCOUNT_TYPE_SUB
                                && orderDetail.getIsItemDiscount() == ParamConst.ITEM_DISCOUNT
                                && orderDetail.getIsFree() == ParamConst.NOT_FREE) {
                            canDiscount = true;
                            break;
                        } else {
                            canDiscount = false;
                        }
                    }
                    if (canDiscount)
                        handler.sendMessage(handler.obtainMessage(MainPage.VIEW_EVENT_SHOW_DISCOUNT_WINDOW, discountShow()));
                    else {
                        UIHelp.showToast(parent, parent.getResources().getString(R.string.order_first));
                        return;
                    }
                    break;
                }
                case R.id.tv_unseat:
//				PaymentSettlement paymentSettlement = PaymentSettlementSQL.getPaymentSettlementsByOrderId(order.getId());
//				if (paymentSettlement != null) {
//					return;
//				}

                    int placeOrderCount = OrderDetailSQL.getOrderDetailPlaceOrderCountByOrder(order);
                    if (placeOrderCount > 0) {
                        DialogFactory.showOneButtonCompelDialog(parent, "", parent.getResources().getString(R.string.cannot_unseat), null);
                    } else {
                        DialogFactory.commonTwoBtnDialog(parent, parent.getResources().getString(R.string.warning),
                                parent.getResources().getString(R.string.unseat_table),
                                parent.getResources().getString(R.string.no),
                                parent.getResources().getString(R.string.yes), null, new OnClickListener() {

                                    @Override
                                    public void onClick(View arg0) {
                                        handler.sendEmptyMessage(MainPage.VIEW_EVENT_UNSEAT_ORDER);
                                    }
                                });
                    }
                    break;
                case R.id.tv_open_item:
                case R.id.tv_open_item_waiting_list:
                    Message msg = handler.obtainMessage();
                    msg.what = MainPage.VIEW_EVENT_SHOW_OPEN_ITEM_WINDOW;
                    handler.sendMessage(msg);
                    break;
                case R.id.tv_print_bill:
                    handler.sendEmptyMessage(MainPage.VIEW_EVENT_OPERATEPANEL);
                    break;
                case R.id.tv_transfer_table:
                    if (!IntegerUtils.isEmptyOrZero(order.getAppOrderId())) {
					UIHelp.showShortToast(parent, parent.getString(R.string.order_from_dinner_app_cannot_transfrerred));
                        return;
                    }
                    handler.sendEmptyMessage(MainPage.VIEW_EVENT_TANSFER_TABLE);
                    break;
                case R.id.tv_kick_cashdrawer:
                    handler.sendEmptyMessage(MainPage.VIEW_EVENT_KICK_CASHDRAWER);
                    break;
                case R.id.rl_pax:
                    BugseeHelper.buttonClicked("Pax");
                    handler.sendMessage(handler.obtainMessage(MainPage.VIEW_EVENT_TANSFER_PAX, tv_pax.getText().toString()));
                    break;
                case R.id.tv_take_away:
                    if (order.getIsTakeAway() == ParamConst.TAKE_AWAY)
                    {
                        order.setIsTakeAway(ParamConst.NOT_TAKE_AWAY);
                        for(OrderDetail orderDetail : orderDetails)
                        {
                            orderDetail.setSpecialInstractions("");
                            orderDetail.setIsTakeAway(ParamConst.NOT_TAKE_AWAY);
                            OrderDetailSQL.updateOrderDetail(orderDetail);
                        }
                    }
                    else
                    {
                        order.setIsTakeAway(ParamConst.TAKE_AWAY);
                        for(OrderDetail orderDetail : orderDetails)
                        {
                            orderDetail.setIsTakeAway(ParamConst.TAKE_AWAY);
                            OrderDetailSQL.updateOrderDetail(orderDetail);
                        }
                    }
                    OrderSQL.updateOrder(order);
                    handler.sendEmptyMessage(MainPage.VIEW_EVENT_SET_DATA);
                    break;
                case R.id.tv_fire: {
                    handler.sendEmptyMessage(MainPage.VIEW_EVENT_FIRE);
                }
                break;
                case R.id.tv_split_by_pax: {
                    handler.sendEmptyMessage(MainPage.VIEW_EVENT_SPLIT_BY_PAX);
                }
                break;
                default:
                    break;
            }

        }
    }

    private Map<String, Object> discountShow() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("order", order);
        map.put("orderDetail", null);
        map.put("resultCall", new ResultCall() {

            @Override
            public void call(final String discount_rate, final String discount_price, final String discountByCategory) {
//				parent.loadingDialog.show();
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        Message msg = handler.obtainMessage();
                        msg.what = MainPage.VIEW_EVENT_SET_DATA;
                        if (CommonUtil.isNull(discount_rate)
                                && CommonUtil.isNull(discount_price)) {
                            handler.sendMessage(msg);
                            return;
                        }

                        if (!CommonUtil.isNull(discount_rate)) {
                            order.setDiscountRate(discount_rate);
//							order.setDiscountPrice(BH.mul(
//									BH.getBD(order.getSubTotal()),
//									BH.getBDNoFormat(order.getDiscountRate()), true)
//									.toString());
                            if (!TextUtils.isEmpty(discountByCategory)) {
                                order.setDiscountType(ParamConst.ORDER_DISCOUNT_TYPE_RATE_BY_CATEGORY);
                                order.setDiscountCategoryId(discountByCategory);
                            } else {
                                order.setDiscountType(ParamConst.ORDER_DISCOUNT_TYPE_RATE_BY_ORDER);
                                order.setDiscountCategoryId("");
                            }
                        } else {
                            order.setDiscountPrice(discount_price);
//							order.setDiscountRate(BH.div(
//									BH.getBD(order.getDiscountAmount()),
//									BH.getBD(order.getSubTotal()), true)
//									.toString());
                            if (!TextUtils.isEmpty(discountByCategory)) {
                                order.setDiscountType(ParamConst.ORDER_DISCOUNT_TYPE_SUB_BY_CATEGORY);
                                order.setDiscountCategoryId(discountByCategory);
                            } else {
                                order.setDiscountType(ParamConst.ORDER_DISCOUNT_TYPE_SUB_BY_ORDER);
                                order.setDiscountCategoryId("");
                            }
                        }
                        OrderSQL.updateOrderAndOrderDetailByDiscount(order);
                        List<OrderSplit> orderSplits = OrderSplitSQL.getOrderSplits(order);
                        if (!orderSplits.isEmpty()) {
                            for (OrderSplit orderSplit : orderSplits) {
                                OrderSplitSQL.updateOrderSplitByOrder(order, orderSplit);
                            }
                        }
                        handler.sendMessage(msg);
                    }
                }).start();

            }
        });
        return map;
    }
}
