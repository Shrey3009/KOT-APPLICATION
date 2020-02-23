package com.alfredkds.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.ParamConst;
import com.alfredbase.global.CoreData;
import com.alfredbase.javabean.ItemDetail;
import com.alfredbase.javabean.KDSTracking;
import com.alfredbase.javabean.KotItemDetail;
import com.alfredbase.javabean.KotItemModifier;
import com.alfredbase.javabean.KotSummaryLog;
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.javabean.Printer;
import com.alfredbase.javabean.model.MainPosInfo;
import com.alfredbase.store.Store;
import com.alfredbase.store.sql.ItemDetailSQL;
import com.alfredbase.store.sql.KotItemDetailSQL;
import com.alfredbase.store.sql.KotItemModifierSQL;
import com.alfredbase.store.sql.KotSummarySQL;
import com.alfredbase.store.sql.OrderDetailSQL;
import com.alfredbase.utils.AnimatorListenerImpl;
import com.alfredbase.utils.ButtonClickTimer;
import com.alfredbase.utils.IntegerUtils;
import com.alfredbase.utils.TextTypeFace;
import com.alfredbase.utils.TimeUtil;
import com.alfredbase.utils.ToastUtils;
import com.alfredkds.R;
import com.alfredkds.activity.KitchenOrder;
import com.alfredkds.global.App;
import com.alfredkds.global.SyncCentre;
import com.alfredkds.javabean.Kot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * KOT信息，每桌菜品展示ScrollView
 *
 * @author
 */
public class KOTView extends LinearLayout implements AnimationListener,
        OnClickListener {

    private KitchenOrder parent;

    private Animation animCenterOpen;
    private Animation animCenterClose;
    private TextView timer;
    private Kot kot;
    private Context context;
    private TextView kotId, tv_kds_delivery;
    private TextView orderId;
    private TextView tvType;
    private TextView table;
    private TextView posName;
    private TextView date;
    private TextView time;
    private TextView tv_kiosk_order_id, tv_kiosk_app_order_id, tv_order_type;
    private TextView tv_orderremark;
    private TextView tv_remark;
    private LinearLayout ll_orderRemark, ll_type;
    private View kotView;
    public Chronometer tv_progress;

    private TextView complete_all_tv;
    private TextView call_num_tv;

    private ListView lv_dishes;
    private KotItemDetailAdapter adapter;
    private List<KotItemDetail> kotItemDetails = new ArrayList<KotItemDetail>();
    private List<KotItemModifier> kotItemModifiers = new ArrayList<KotItemModifier>();
    private TextTypeFace textTypeFace;
    private Handler handler;
    private int hour;
    private MainPosInfo mainPosInfo;
    private TextView tvNext;
    private RelativeLayout ll_progress;
    private LinearLayout linear_progress;
    private LinearLayout llAction;
    private KotSummaryLog kotSummaryLogs;

    public KOTView(Context context) {
        super(context);
        this.parent = (KitchenOrder) context;
        this.context = context;
        init();
    }

    public KOTView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.parent = (KitchenOrder) context;
        this.context = context;
        init();

    }

//	public Long getTime(){
//		return tv_progress.getBase();
//	}

    public void setParams(Context context, Handler handler) {
        this.parent = (KitchenOrder) context;
        this.context = context;
        this.handler = handler;
    }

    public void init() {
        mainPosInfo = App.instance.getCurrentConnectedMainPos();
        kotView = View.inflate(context, R.layout.kot_view, this);
        /*---kotTop显示---*/
        kotId = (TextView) kotView.findViewById(R.id.tv_kotId);

        tvType = (TextView) kotView.findViewById(R.id.tvType);
        orderId = (TextView) kotView.findViewById(R.id.tv_order_id);
        table = (TextView) kotView.findViewById(R.id.tv_table);
        posName = (TextView) kotView.findViewById(R.id.tv_pos);
        date = (TextView) kotView.findViewById(R.id.tv_createDate);
        time = (TextView) kotView.findViewById(R.id.tv_createTime);
        tv_progress = (Chronometer) kotView.findViewById(R.id.tv_progress);
        lv_dishes = (ListView) kotView.findViewById(R.id.lv_dishes);
        tv_kiosk_order_id = (TextView) kotView.findViewById(R.id.tv_kiosk_order_id);
        tv_kiosk_app_order_id = (TextView) kotView.findViewById(R.id.tv_kiosk_app_order_id);
        tv_order_type = (TextView) kotView.findViewById(R.id.tv_order_type);
        tv_orderremark = (TextView) kotView.findViewById(R.id.tv_orderremark);
        ll_orderRemark = (LinearLayout) kotView.findViewById(R.id.ll_orderRemark);
        ll_type = (LinearLayout) kotView.findViewById(R.id.ll_type);
        call_num_tv = (TextView) kotView.findViewById(R.id.call_num_tv);
        complete_all_tv = (TextView) kotView.findViewById(R.id.complete_all_tv);
        tvNext = (TextView) kotView.findViewById(R.id.tvNext);
        llAction = (LinearLayout) kotView.findViewById(R.id.llAction);
        ll_progress = (RelativeLayout) kotView.findViewById(R.id.ll_progress);
        linear_progress = (LinearLayout) kotView.findViewById(R.id.linear_progress);

//        if ("0".equals(App.instance.getKdsDevice().getKdsType())) {
//            call_num_tv.setVisibility(GONE);
//            complete_all_tv.setVisibility(GONE);
//            tvNext.setVisibility(VISIBLE);
//        } else {
//            call_num_tv.setVisibility(VISIBLE);
//            complete_all_tv.setVisibility(VISIBLE);
//            tvNext.setVisibility(GONE);
//        }

        tv_orderremark.setMovementMethod(ScrollingMovementMethod.getInstance());

        adapter = new KotItemDetailAdapter();
//		initTextTypeFace();
        if (mainPosInfo.getIsKiosk() == ParamConst.MAINPOSINFO_IS_KIOSK) {
            tv_kiosk_order_id.setVisibility(View.VISIBLE);
            orderId.setVisibility(View.GONE);
            time.setVisibility(GONE);
        } else {
            tv_kiosk_order_id.setVisibility(View.GONE);
            orderId.setVisibility(View.VISIBLE);
            time.setVisibility(VISIBLE);
        }

    }


    public class KotItemDetailAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return kotItemDetails.size();
        }

        @Override
        public Object getItem(int position) {
            return kotItemDetails.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_in_kot, null);
                holder = new ViewHolder();
                holder.tv_order_num = (TextView) convertView.findViewById(R.id.tv_order_num);
                holder.tv_text = (TextView) convertView.findViewById(R.id.tv_text);
                holder.tv_dish_introduce = (TextView) convertView.findViewById(R.id.tv_dish_introduce);
                holder.ivChecklist = (ImageView) convertView.findViewById(R.id.ivChecklist);
                holder.cmItem = (Chronometer) convertView.findViewById(R.id.cmItem);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.cmItem.setVisibility(GONE);

            KotItemDetail kotItemDetail = kotItemDetails.get(position);

            if (kotItemDetail.isChecklist) {
                holder.ivChecklist.setVisibility(VISIBLE);
            } else {
                holder.ivChecklist.setVisibility(GONE);
            }

            holder.tv_order_num.setTextColor(ContextCompat.getColor(context, R.color.black));
            convertView.setBackgroundResource(R.color.white);

            if (kotItemDetail.getKotStatus() == ParamConst.KOT_STATUS_DONE) {
                convertView.setBackgroundResource(R.color.bg_complete_item);
            } else if (kotItemDetail.getFireStatus() == 1) {
                convertView.setBackgroundResource(R.color.viewfinder_laser);
            } else if (kotItemDetail.getKotStatus() == ParamConst.KOT_STATUS_VOID) {
                convertView.setBackgroundResource(R.color.possible_result_points);
                holder.tv_text.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.FAKE_BOLD_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            } else if (kotItemDetail.getKotStatus() == ParamConst.KOT_STATUS_UNSEND) {
                convertView.setBackgroundResource(R.color.white);
                holder.tv_text.setTextColor(ContextCompat.getColor(context, R.color.black));
            } else if (kotItemDetail.getKotStatus() == ParamConst.KOT_STATUS_TMP) {
                holder.tv_text.setTextColor(ContextCompat.getColor(context, R.color.gray));
                holder.tv_order_num.setTextColor(ContextCompat.getColor(context, R.color.white));
            } else {
                if (kotItemDetail.getKotStatus() == ParamConst.KOT_STATUS_UPDATE) {
                    convertView.setBackgroundResource(R.color.bg_update_item);
                } else {
                    convertView.setBackgroundResource(R.color.white);
                }
            }

            if (App.instance.getKdsDevice().getKdsType() == Printer.KDS_SUMMARY) {
                holder.tv_text.setTextColor(ContextCompat.getColor(context, R.color.black));
                holder.cmItem.setVisibility(VISIBLE);
//                long createTime = kot.getKotSummary().getUpdateTime();
//                holder.cmItem.setBase(SystemClock.elapsedRealtime() - (System.currentTimeMillis() - createTime));

                List<KDSTracking> kdsTrackingList = kotSummaryLogs.kdsTrackingList;
                boolean isCompleted = false;

                for (KDSTracking kdsTracking : kdsTrackingList) {
                    if (kdsTracking.kdsDevice.getKdsType() == Printer.KDS_EXPEDITER ||
                            kdsTracking.kdsDevice.getKdsType() == Printer.KDS_SUMMARY) {
                        List<KotItemDetail> kotItemDetails = kdsTracking.kotItemDetails;

                        for (KotItemDetail kid : kotItemDetails) {
                            if (kid.getId().equals(kotItemDetail.getId()) && kid.getEndTime() > 0) {
                                convertView.setBackgroundResource(R.color.bg_complete_item);
                                holder.cmItem.stop();
                                isCompleted = true;
                                break;
                            }
                        }

                        if (isCompleted)
                            break;

                    }
                }

                if (!isCompleted)
                    holder.cmItem.start();

            }

            StringBuffer sBuffer = new StringBuffer();
            for (int j = 0; j < kotItemModifiers.size(); j++) {
                KotItemModifier kotItemModifier = kotItemModifiers.get(j);
                if (kotItemModifier != null
                        && kotItemDetail.getUniqueId().equals(kotItemModifier.getKotItemDetailUniqueId())) {
                    sBuffer.append("--" + kotItemModifier.getModifierName() + "\n");
                }
            }
            if (!TextUtils.isEmpty(kotItemDetail.getSpecialInstractions())) {
                sBuffer.append("*" + kotItemDetail.getSpecialInstractions() + "*");
            }
            if (sBuffer.toString().endsWith("\n")) {
                sBuffer.deleteCharAt(sBuffer.length() - 1);
            }
            holder.tv_order_num.setText(kotItemDetail.getUnFinishQty() + "");
            holder.tv_text.setText(kotItemDetail.getItemName());
            holder.tv_dish_introduce.setText(sBuffer);
//			textTypeFace.setTrajanProBlod(holder.tv_text);
//			textTypeFace.setTrajanProRegular(holder.tv_order_num);
//			textTypeFace.setTrajanProRegular(holder.tv_dish_introduce);
            return convertView;
        }

        public void setCheckListPosition(int i) {
            kotItemDetails.get(i).isChecklist = !kotItemDetails.get(i).isChecklist;
            notifyDataSetChanged();
        }

        public boolean isChecklistExists() {
            boolean isCheckListExist = false;

            for (KotItemDetail kid : kotItemDetails) {
                if (kid.isChecklist) {
                    isCheckListExist = true;
                    break;
                }
            }

            return isCheckListExist;
        }
    }

    class ViewHolder {
        private TextView tv_order_num;
        private TextView tv_text;
        private TextView tv_dish_introduce;
        private Chronometer cmItem;
        private ImageView ivChecklist;
    }

    private ArrayList<KotItemDetail> getSelectedKotItemDetails() {
        ArrayList<KotItemDetail> selectedKotItemDetails = new ArrayList<>();
        for (KotItemDetail kid : kotItemDetails) {
            if (kid.isChecklist)
                selectedKotItemDetails.add(kid);
        }

        return selectedKotItemDetails;
    }

    private Map<Integer, ArrayList<KotItemModifier>> getComboModifiers(KotItemDetail kotItemDetail, List<KotItemModifier> modifiers,
                                                                       Map<Integer, ArrayList<KotItemModifier>> modCombo) {

        int kotItemDetailId = kotItemDetail.getId();

        for (KotItemModifier kotItemModifier : modifiers) {
            int printerGroupId = kotItemModifier.getPrinterId();

            if (printerGroupId <= 0) continue;

            if (kotItemModifier.getKotItemDetailId() == kotItemDetailId) {
                if (modCombo.containsKey(printerGroupId)) {
                    ArrayList<KotItemModifier> tmp = modCombo.get(printerGroupId);
                    tmp.add(kotItemModifier);
                } else {
                    ArrayList<KotItemModifier> tmp = new ArrayList<>();
                    tmp.add(kotItemModifier);
                    modCombo.put(printerGroupId, tmp);
                }
            }
        }

        return modCombo;

    }

    public void setData(Kot originKot) {
        this.kot = originKot;
        this.kotItemDetails.clear();
        this.kotItemDetails.addAll(kot.getKotItemDetails());
        this.kotItemModifiers.clear();
        this.kotItemModifiers.addAll(kot.getKotItemModifiers());
        this.kotSummaryLogs = new Gson().fromJson(kot.getKotSummary().getKotSummaryLog(), KotSummaryLog.class);

        complete_all_tv.setText("Complete All");

        if (!kot.isPlaceOrder()) {
            linear_progress.setVisibility(GONE);
        } else {
            linear_progress.setVisibility(VISIBLE);
            if (App.instance.getKdsDevice().getKdsType() == Printer.KDS_SUB) {
                llAction.setVisibility(VISIBLE);
                call_num_tv.setVisibility(GONE);
                complete_all_tv.setVisibility(GONE);
                tvNext.setVisibility(VISIBLE);
            } else if (App.instance.getKdsDevice().getKdsType() == Printer.KDS_NORMAL) {
                llAction.setVisibility(VISIBLE);
                complete_all_tv.setText("Complete");
            } else if (App.instance.getKdsDevice().getKdsType() == Printer.KDS_SUMMARY) {
                llAction.setVisibility(GONE);
            } else {//kds expediter
                tvNext.setVisibility(GONE);
                complete_all_tv.setVisibility(VISIBLE);
                call_num_tv.setVisibility(VISIBLE);

                if (App.instance.getSystemSettings().isAllowPartial()) {
                    llAction.setVisibility(VISIBLE);

                    if (kot.getKotSummary().isNext() == 1) {
                        call_num_tv.setVisibility(GONE);
                    }

                } else {
                    int itemCount = 0;

                    List<KotItemDetail> kotItemDetailsLocal = KotItemDetailSQL.getKotItemDetailBySummaryIdRvcId(kot.getKotSummary().getId(), kot.getKotSummary().getRevenueCenterId());

                    for (KotItemDetail kotItemDetail : kotItemDetailsLocal) {

//                        if (ParamConst.KOT_STATUS_VOID != kotItemDetail.getKotStatus() &&
//                                ParamConst.KOT_STATUS_DONE != kotItemDetail.getKotStatus()) {
                        if (ParamConst.KOT_STATUS_VOID != kotItemDetail.getKotStatus()) {
                            if (kotItemDetail.getItemType() == ParamConst.ITEMDETAIL_COMBO_ITEM) {//package item
                                itemCount += KotItemModifierSQL.getKotItemModifiersByKotItemDetail(kotItemDetail).size();
                            } else {
                                itemCount++;
                            }
                        }
                    }

                    if (itemCount >= kot.getKotSummary().getOrderDetailCount()) {
                        llAction.setVisibility(VISIBLE);

                        if (kot.getKotSummary().isNext() == 1) {
                            call_num_tv.setVisibility(GONE);
                        }
                    } else {
                        llAction.setVisibility(GONE);
                    }
                }
            }
        }

        kotId.setText(kot.getKotSummary().getId() + "");
        int trainType = Store.getInt(context, Store.TRAIN_TYPE);
        String orderNoStr = context.getResources().getString(R.string.order_no) + kot.getKotSummary().getNumTag() + kot.getKotSummary().getOrderNo();
        String type = "";

        if (!TextUtils.isEmpty(kot.getKotSummary().getEmpName())) {
            orderNoStr = orderNoStr + "(Emp:" + kot.getKotSummary().getEmpName() + ")";
        }

        String kioskOrderNoStr = context.getResources().getString(R.string.order_no) + kot.getKotSummary().getNumTag() + IntegerUtils.formatLocale(kot.getKotSummary().getRevenueCenterIndex(), kot.getKotSummary().getOrderNo() + "");
        if (kot.getKotSummary() != null && kot.getKotSummary().getIsTakeAway().intValue() == ParamConst.TAKE_AWAY) {
//            orderNoStr = orderNoStr + "(" + context.getResources().getString(R.string.take_away) + ")";
            type = "(" + context.getResources().getString(R.string.takeaway) + ")";
            kioskOrderNoStr = kioskOrderNoStr + "(" + context.getResources().getString(R.string.takeaway) + ")";
        }

        if (kot.getKotSummary().getIsTakeAway().intValue() == ParamConst.DINE_IN) {
//            orderNoStr = orderNoStr + "(" + context.getResources().getString(R.string.dine_in) + ")";
            type = "(" + context.getResources().getString(R.string.dine_in) + ")";
            kioskOrderNoStr = kioskOrderNoStr + "(" + context.getResources().getString(R.string.dine_in) + ")";
        } else if (kot.getKotSummary().getIsTakeAway().intValue() == ParamConst.APP_DELIVERY) {
//            orderNoStr = orderNoStr + "(" + context.getResources().getString(R.string.app_delivery) + ")";
            type = "(" + context.getResources().getString(R.string.delivery) + ")";
            kioskOrderNoStr = kioskOrderNoStr + "(" + context.getResources().getString(R.string.delivery) + ")";
        }
        String trainString = "";

        if (trainType == 1) {
            trainString = ".Training";
        }
        if (TextUtils.isEmpty(kot.getKotSummary().getTableName())) {
            table.setText(kioskOrderNoStr + trainString);
            if (kot.getKotSummary().getAppOrderId() > 0) {
                if (kot.getKotSummary().getEatType() == 3) {
                    orderId.setText(context.getResources().getString(R.string.online_app_no) + " : " + kot.getKotSummary().getAppOrderId() + "\n" + TimeUtil.getDeliveryDataTime(kot.getKotSummary().getDeliveryTime()));
                    tv_kiosk_order_id.setText(context.getResources().getString(R.string.online_app_no) + " : " + kot.getKotSummary().getAppOrderId() + "\n" + TimeUtil.getDeliveryDataTime(kot.getKotSummary().getDeliveryTime()));

                } else {
                    orderId.setText(context.getResources().getString(R.string.online_app_no) + " : " + kot.getKotSummary().getAppOrderId());
                    tv_kiosk_order_id.setText(context.getResources().getString(R.string.online_app_no) + " : " + kot.getKotSummary().getAppOrderId());

                }
            } else {
                orderId.setText("");
                tv_kiosk_order_id.setText("");
            }
            ll_type.setVisibility(GONE);
        } else {
//            table.setText(context.getResources().getString(R.string.table_) + kot.getKotSummary().getTableName());
            table.setText("T-" + kot.getKotSummary().getTableName());
            tvType.setText(type);
            orderId.setText(orderNoStr);
            tv_kiosk_order_id.setText(kioskOrderNoStr);
            if (kot.getKotSummary().getAppOrderId() > 0) {
                ll_type.setVisibility(VISIBLE);
                if (kot.getKotSummary().getEatType() == 3) {
                    tv_kiosk_app_order_id.setText(context.getResources().getString(R.string.online_app_no) + " : " + kot.getKotSummary().getAppOrderId() + "\n" + TimeUtil.getDeliveryDataTime(kot.getKotSummary().getDeliveryTime()));
                } else {
                    tv_kiosk_app_order_id.setText(context.getResources().getString(R.string.online_app_no) + " : " + kot.getKotSummary().getAppOrderId());
                }

            } else {
                ll_type.setVisibility(GONE);
            }
        }
        //	posName.setText(kot.getKotSummary().getRevenueCenterName() + "");


//		if(kot.getKotSummary().getEatType()==2)
//		{
//			ll_type.setVisibility(GONE);
//		}else {
//			ll_type.setVisibility(VISIBLE);
//			tv_kiosk_app_order_id.setText("Online App No:"+kot.getKotSummary().getAppOrderId());
//			if(kot.getKotSummary().getEatType()==3){
//				//tv_order_type.setText("delivery");
//			}else {
//				//tv_order_type.setText("dine in");
//			}
//		}
        String remark = kot.getKotSummary().getOrderRemark();
        if (TextUtils.isEmpty(remark)) {
            ll_orderRemark.setVisibility(GONE);
        } else {
            if (kot.getKotSummary().getEatType() == 3) {
                ll_orderRemark.setVisibility(GONE);
            } else {
                ll_orderRemark.setVisibility(VISIBLE);
                tv_orderremark.setText(context.getString(R.string.remarks) + " : " + " " + remark);
            }

        }

        date.setText(TimeUtil.getPrintDate(kot.getKotSummary().getCreateTime()));
        time.setText(TimeUtil.getPrintTime(kot.getKotSummary().getCreateTime()));

//		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
//		long currentTime = System.currentTimeMillis();
        long createTime = kot.getKotSummary().getUpdateTime();
//		final String str = sdf.format(new Date(currentTime - createTime));
        tv_progress.setBase(SystemClock.elapsedRealtime() - (System.currentTimeMillis() - createTime));
//		int hour = (int) ((System.currentTimeMillis() - createTime) / 1000 / 60/60);
//		tv_progress.setFormat("0"+String.valueOf(hour)+":%s");
//		tv_progress.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
//			@Override
//			public void onChronometerTick(Chronometer chronometer) {
//				chronometer.setText(str);
//
//			}
//		});
        tv_progress.start();
//		tv_progress.setText(str);


        if (lv_dishes.getAdapter() == null) {
            lv_dishes.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }

        if (App.instance.getKdsDevice().getKdsType() == Printer.KDS_SUB ||
                App.instance.getKdsDevice().getKdsType() == Printer.KDS_NORMAL) {
            lv_dishes.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parentView, View View, int position,
                                        long id) {
                    if (!ButtonClickTimer.canClick(View)) {
                        return;
                    }

                    if (kot.isPlaceOrder())
                        adapter.setCheckListPosition(position);
                }
            });

            lv_dishes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (!ButtonClickTimer.canClick(view)) {
                        return false;
                    }

                    if (kot.isPlaceOrder())
                        parent.showOrderItem(kot.getKotSummary());
                    return false;
                }
            });
        }

        if (isComplete()) {
            tv_progress.setText(context.getResources().getString(R.string.item_complete));
            tv_progress.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    kot.getKotSummary().setStatus(ParamConst.KOTS_STATUS_DONE);
                    KotSummarySQL.update(kot.getKotSummary());
                    KOTView.this.dismissKot();
                }
            });
        } else {
            tv_progress.setFocusable(false);
        }

        call_num_tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ButtonClickTimer.canClick()) return;

                Message message = new Message();
                message.obj = kot;
                message.arg2 = -1;
                message.what = App.HANDLER_KOT_CALL_NUM_OLD;
                handler.sendMessage(message);
            }
        });

        complete_all_tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ButtonClickTimer.canClick()) {
                    return;
                }

                if (App.instance.getKdsDevice().getKdsType() == Printer.KDS_NORMAL) {
                    List<KotItemDetail> kotItemDetails = getSelectedKotItemDetails();

                    if (kotItemDetails.size() <= 0) {
                        return;
                    }

                    for (KotItemDetail kotItemDetail : kotItemDetails) {
                        if (kotItemDetail.getKotStatus() < ParamConst.KOT_STATUS_DONE &&
                                kotItemDetail.getCategoryId() == ParamConst.KOTITEMDETAIL_CATEGORYID_MAIN) {

                            if (kotItemDetail.getUnFinishQty() > 1) {
                            }

                            kotItemDetail.setKotStatus(ParamConst.KOT_STATUS_DONE);
                            kotItemDetail.setUnFinishQty(0);
                            kotItemDetail.setFinishQty(1);
                            KotItemDetailSQL.update(kotItemDetail);
                        }
                    }

                    Map<String, Object> parameters = new HashMap<>();
                    parameters.put("kotSummary", kot.getKotSummary());
                    parameters.put("kotItemDetails", kotItemDetails);
                    parameters.put("kdsId", App.instance.getKdsDevice().getDevice_id());
                    parameters.put("userKey", CoreData.getInstance().getUserKey(kot.getKotSummary().getRevenueCenterId()));

                    SyncCentre.getInstance().kotComplete(context,
                            App.instance.getCurrentConnectedMainPos(kot.getKotSummary().getRevenueCenterId()), parameters, handler, -1);

                } else {
                    if (kot.getKotSummary().isNext() == 1) {
                        sendToNextKds(false);
                    } else {
                        Message message = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("kotSummary", kot.getKotSummary());
                        message.setData(bundle);
                        message.what = App.HANDLER_KOT_COMPLETE_ALL;
                        handler.sendMessage(message);
                    }
                }
            }
        });

        tvNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ButtonClickTimer.canClick()) return;
                sendToNextKds(true);
            }
        });

    }

    private void sendToNextKds(boolean isSelected) {
        List<KotItemDetail> kotItemDetails;
        List<KotItemModifier> kotItemModifiers = new ArrayList<>();

        if (isSelected) {
            kotItemDetails = getSelectedKotItemDetails();

            if (kotItemDetails.size() > 0) {

                for (KotItemDetail kotItemDetail : kotItemDetails) {
                    ArrayList<KotItemModifier> kims = KotItemModifierSQL.
                            getKotItemModifiersByKotItemDetail(kotItemDetail);

                    kotItemModifiers.addAll(kims);
                }

            } else {
                BaseActivity baseActivity = App.getTopActivity();
                if (baseActivity != null)
                    ToastUtils.showToast(baseActivity, "Please select at least one item to bump!");
                return;
            }
        } else {
            kotItemDetails = this.kotItemDetails;

            if (kotItemDetails.size() > 0) {

                for (KotItemDetail kotItemDetail : kotItemDetails) {

                    ArrayList<KotItemModifier> kims = KotItemModifierSQL.
                            getKotItemModifiersByKotItemDetail(kotItemDetail);

                    kotItemModifiers.addAll(kims);
                }

            }
        }

        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("kot", new Gson().toJson(kot));
        bundle.putString("kotItemDetails", new Gson().toJson(kotItemDetails));
        bundle.putString("kotItemModifiers", new Gson().toJson(kotItemModifiers));
        message.setData(bundle);
        message.what = App.HANDLER_KOT_NEXT;
        handler.sendMessage(message);
    }

    public void showNewKOT() {
        // this.startAnimation(this.animCenterOpen);
        kotView.post(new Runnable() {
            @Override
            public void run() {
                AnimatorSet set = new AnimatorSet();
                ObjectAnimator animator1 = ObjectAnimator.ofFloat(kotView,
                        "scaleY", 0.3f, 1f).setDuration(500);
                ObjectAnimator animator2 = ObjectAnimator.ofFloat(kotView,
                        "scaleX", 0.3f, 1f).setDuration(500);
                set.playTogether(animator1, animator2);
                set.setInterpolator(new DecelerateInterpolator());
                set.start();
            }
        });
    }

    public void dismissKot() {
        kotView.post(new Runnable() {
            @Override
            public void run() {
                if (AnimatorListenerImpl.isRunning) {
                    return;
                }
                AnimatorSet set = new AnimatorSet();
                ObjectAnimator animator1 = ObjectAnimator.ofFloat(kotView,
                        "scaleY", 1f, 0.05f).setDuration(500);
                ObjectAnimator animator2 = ObjectAnimator.ofFloat(kotView,
                        "scaleX", 1f, 0.05f).setDuration(500);
                set.playTogether(animator1, animator2);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerImpl() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        parent.adapter.setKots(App.instance.getRefreshKots());
                        parent.adapter.notifyDataSetChanged();
                    }
                });
                set.start();
            }
        });
    }

    public boolean isComplete() {
        for (int i = 0; i < kotItemDetails.size(); i++) {
            if (kotItemDetails.get(i).getKotStatus() < ParamConst.KOT_STATUS_DONE) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if (ButtonClickTimer.canClick(v)) {

        }
    }

    @Override
    public void onAnimationEnd(Animation arg0) {

    }

    @Override
    public void onAnimationRepeat(Animation arg0) {

    }

    @Override
    public void onAnimationStart(Animation arg0) {

    }

    private void initTextTypeFace() {
        textTypeFace = TextTypeFace.getInstance();
        textTypeFace.setTrajanProBlod(kotId);
        textTypeFace.setTrajanProRegular(orderId);
        textTypeFace.setTrajanProRegular(table);
        textTypeFace.setTrajanProRegular(posName);
        textTypeFace.setTrajanProRegular(date);
        textTypeFace.setTrajanProRegular(time);
        textTypeFace.setTrajanProRegular(tv_progress);
        textTypeFace.setTrajanProRegular(tv_kiosk_order_id);
        textTypeFace.setTrajanProRegular(tv_orderremark);
        textTypeFace.setTrajanProRegular(tv_remark);
    }
}
