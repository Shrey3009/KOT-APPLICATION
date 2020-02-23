package com.alfredkds.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.LoadingDialog;
import com.alfredbase.ParamConst;
import com.alfredbase.global.CoreData;
import com.alfredbase.http.ResultCode;
import com.alfredbase.javabean.KotItemDetail;
import com.alfredbase.javabean.KotSummary;
import com.alfredbase.javabean.model.MainPosInfo;
import com.alfredbase.store.sql.KotItemDetailSQL;
import com.alfredbase.store.sql.KotSummarySQL;
import com.alfredbase.utils.DialogFactory;
import com.alfredbase.utils.IntegerUtils;
import com.alfredbase.utils.TimeUtil;
import com.alfredkds.R;
import com.alfredkds.global.App;
import com.alfredkds.global.SyncCentre;
import com.alfredkds.global.UIHelp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KotHistory extends BaseActivity {

    public static final int HANDLER_SUCCEED = 1;
    public static final int HANDLER_RECONNECT_POS = 10;
    public static final int HANDLER_SEND_FAILURE = 3;
    public static final int HANDLER_REFRESH = 2;
    boolean isKiosk = true;
    private ListView listView;
    public KotHistoryAdapter adapter;
    private TextView tv_table_name;

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_kot_history);
        loadingDialog = new LoadingDialog(context);
        loadingDialog.setTitle(context.getResources().getString(R.string.undoing));
        for (MainPosInfo mainPos : App.instance.getCurrentConnectedMainPosList()) {
            if (mainPos == null) continue;
            if (mainPos.getIsKiosk() != ParamConst.MAINPOSINFO_IS_KIOSK) {
                isKiosk = false;
            }
        }
        TextView tv_order_n = (TextView) findViewById(R.id.tv_order_n);
        tv_table_name = (TextView) findViewById(R.id.tv_table_name);
        if (isKiosk) {
            tv_table_name.setVisibility(View.GONE);
            tv_order_n.setText(getResources().getString(R.string.order_no));
        } else {
            tv_order_n.setText(getResources().getString(R.string.order_id));
        }
        initListView();
    }

    private void initListView() {
        listView = (ListView) this.findViewById(R.id.lv_kot_history);
        adapter = new KotHistoryAdapter(context);
        adapter.setKotHistory(App.instance.getKotHistoryData());
        listView.setAdapter(adapter);
    }

    public Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case HANDLER_SUCCEED:
                    refresh();
                    break;
                case HANDLER_RECONNECT_POS:
                    loadingDialog.dismiss();
                    DialogFactory.commonTwoBtnDialog(context, "", getString(R.string.reconnect_pos),
                            getString(R.string.cancel), getString(R.string.ok), null,
                            new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    UIHelp.startConnectPOS(context);
                                    finish();
                                }
                            });
                    break;
                case HANDLER_SEND_FAILURE:
                    refresh();
                    break;
                case HANDLER_REFRESH:
                    refresh();
                    break;
                case ResultCode.CONNECTION_FAILED:
                    UIHelp.showToast(context, ResultCode.getErrorResultStr(context, (Throwable) msg.obj,
                            context.getResources().getString(R.string.revenue_center)));
                    break;
                case App.HANDLER_KOTSUMMARY_IS_UNREAL:
                    loadingDialog.dismiss();
                    UIHelp.showToast(context, context.getResources().getString(R.string.order_discarded));
                    List<Object[]> kotHistory = App.instance.getKotHistoryData();
                    adapter.setKotHistory(kotHistory);
                    adapter.notifyDataSetChanged();
                    break;
                case App.HANDLER_KOT_COMPLETE_USER_FAILED:
                    App.instance.reload(context, handler);
                    break;
                case Login.HANDLER_LOGIN:
                    refresh();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    public void refresh() {
        if (loadingDialog != null && adapter != null) {
            loadingDialog.dismiss();
            adapter.setKotHistory(App.instance.getKotHistoryData());
            adapter.notifyDataSetChanged();
        }
    }

    public void httpRequestAction(int action, Object obj) {
        handler.sendMessage(handler.obtainMessage(HANDLER_REFRESH));
    }

    ;

    private class KotHistoryAdapter extends BaseAdapter {

        private List<Object[]> kotHistory = new ArrayList<Object[]>();
        private LayoutInflater inflater;

        public KotHistoryAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public List<Object[]> getKotHistory() {
            return kotHistory;
        }

        public void setKotHistory(List<Object[]> kotHistory) {
            this.kotHistory = kotHistory;
        }


        @Override
        public int getCount() {
            return kotHistory.size();
        }

        @Override
        public Object getItem(int position) {
            return kotHistory.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_kot_history, null);
                holder = new ViewHolder();
                holder.kotId = (TextView) convertView.findViewById(R.id.tv_kotId);
                holder.orderId = (TextView) convertView.findViewById(R.id.tv_orderId);
                holder.table = (TextView) convertView.findViewById(R.id.tv_table);
                holder.pos = (TextView) convertView.findViewById(R.id.tv_pos);
                holder.data = (TextView) convertView.findViewById(R.id.tv_data);
                holder.time = (TextView) convertView.findViewById(R.id.tv_time);
                holder.dish = (TextView) convertView.findViewById(R.id.tv_dish);
                holder.btn_reduction = (Button) convertView.findViewById(R.id.btn_reduction);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final KotSummary kotSummary = (KotSummary) kotHistory.get(position)[0];
            final KotItemDetail kotItemDetail = (KotItemDetail) kotHistory.get(position)[1];
            holder.kotId.setText(kotSummary.getId() + "");

            if (isKiosk) {
                holder.table.setVisibility(View.GONE);
                holder.orderId.setText(kotSummary.getNumTag() + IntegerUtils.formatLocale(kotSummary.getRevenueCenterIndex(), kotSummary.getOrderNoString()));
            } else {
                holder.orderId.setText(kotSummary.getOrderId() + "");
                holder.table.setText(kotSummary.getTableName() + "");
            }
            holder.pos.setText(kotSummary.getRevenueCenterName() + "");
            holder.data.setText(TimeUtil.getPrintDate(kotSummary.getCreateTime()) + "");
            holder.time.setText(TimeUtil.getPrintTime(kotSummary.getCreateTime()) + "");
            holder.dish.setText((String) kotHistory.get(position)[2]);
//			if (kotItemDetail.getKotStatus()==ParamConst.KOT_STATUS_DONE ||
//					(kotItemDetail.getKotStatus() == ParamConst.KOTS_STATUS_UNDONE &&
//					kotItemDetail.getFinishQty() != 0)) {
				holder.btn_reduction.setFocusable(true);
				holder.btn_reduction.setText(R.string.kot_revert);
				holder.btn_reduction.setBackgroundColor(Color.RED);
				holder.btn_reduction.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						loadingDialog.show();
						kotSummary.setStatus(ParamConst.KOTS_STATUS_UNDONE);
						KotSummarySQL.update(kotSummary);
						kotItemDetail.setKotStatus(ParamConst.KOT_STATUS_UNDONE);
						KotItemDetailSQL.update(kotItemDetail);
						Map<String, Object> parameters = new HashMap<String, Object>();
						parameters.put("kotSummary", kotSummary);
						parameters.put("kotItemDetail", kotItemDetail);
						parameters.put("userKey", CoreData.getInstance().getUserKey(kotSummary.getRevenueCenterId()));
						SyncCentre.getInstance().cancelComplete(context, 
								App.instance.getCurrentConnectedMainPos(kotSummary.getRevenueCenterId()), parameters, handler);
						
						adapter.setKotHistory(App.instance.getKotHistoryData());
						adapter.notifyDataSetChanged();
					}
				});
////			}else if (kotItemDetail.getKotStatus() == ParamConst.KOT_STATUS_VOID) {
//				holder.btn_reduction.setClickable(false);
//				holder.btn_reduction.setText("void");
//				holder.btn_reduction.setBackgroundColor(Color.GRAY);
//			}else {
//				holder.btn_reduction.setClickable(false);
//				holder.btn_reduction.setText(R.string.kot_in_progress);
//				holder.btn_reduction.setBackgroundColor(Color.GRAY);
//			}
            return convertView;
        }
    }

    class ViewHolder {
        public TextView kotId;
        public TextView orderId;
        public TextView table;
        public TextView pos;
        public TextView data;
        public TextView time;
        public TextView dish;
        public Button btn_reduction;
    }

}
