package com.alfredkds.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.alfredbase.javabean.KotItemDetail;
import com.alfredbase.javabean.model.MainPosInfo;
import com.alfredbase.store.sql.KotSummarySQL;
import com.alfredbase.utils.ButtonClickTimer;
import com.alfredbase.utils.IntegerUtils;
import com.alfredkds.R;
import com.alfredkds.global.App;
import com.alfredkds.javabean.KotItem;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class KOTArrayLanAdapter extends RecyclerView.Adapter<KOTArrayLanAdapter.ViewHolder> {

    private Context mContext;
    private List<KotItem> kots = new ArrayList<KotItem>();
    private boolean addFirstItem = false;
    private Handler handler;
    private LayoutInflater inflater;
    private MainPosInfo mainPosInfo;

//	private Map<Integer, Long> times = new HashMap<Integer, Long>();

    public KOTArrayLanAdapter(Context mContext, Handler handler) {
        super();
//		times.clear();
        this.mContext = mContext;
        this.handler = handler;
        inflater = LayoutInflater.from(mContext);
    }

    public void setAddFirstItem(boolean addFirstItem) {
        this.addFirstItem = addFirstItem;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = null;
        ViewHolder viewHolder = null;


        //	View convertView = View.inflate(mContext,R.layout.kot_array_view, null);


        //convertView = inflater.inflate(R.layout. kot_array_view,parent, false);
//        if (viewType == 0) {
//            convertView = inflater.inflate(R.layout.kot_array_landscape_title, parent, false);
//            //convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.kot_array_landscape_view, parent, false);
//            // convertView = View.inflate(mContext,R.layout.kot_array_landscape_view, null);
//            viewHolder = new ViewHolder(convertView);
//
//        } else {
        convertView = inflater.inflate(R.layout.kot_array_landscape_view, parent, false);

        viewHolder = new ViewHolder(convertView);

        viewHolder.table = (TextView) convertView.findViewById(R.id.tv_kot_item_table);

        viewHolder.detail = (TextView) convertView.findViewById(R.id.tv_kot_detail);
        viewHolder.mod = (TextView) convertView.findViewById(R.id.tv_kot_mod);

        viewHolder.status = (TextView) convertView.findViewById(R.id.tv_kot_item_status);
        viewHolder.orderNo = (TextView) convertView.findViewById(R.id.tv_kot_item_orderno);
        viewHolder.tv_lan_progress = (Chronometer) convertView.findViewById(R.id.tv_lan_progress);
        viewHolder.btn_complete = (Button) convertView.findViewById(R.id.btn_kot_complete);
        viewHolder.btn_call = (Button) convertView.findViewById(R.id.btn_kot_call);
        viewHolder.qty = (TextView) convertView.findViewById(R.id.tv_kot_item_qty);

        //    }


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//
//        int itemViewType = KOTArrayLanAdapter.this.getItemViewType(position);
//        if (itemViewType == 1) {
            final KotItem kotItem = kots.get(position);
            String orderNoStr = kotItem.getNumTag() + IntegerUtils.formatLocale(kotItem.getRevenueCenterIndex(), kotItem.getOrderNo() + "");
            holder.orderNo.setText(orderNoStr);
            holder.mod.setText(kotItem.getItemModName());
            holder.detail.setText(kotItem.getItemDetailName());
            holder.table.setText(kotItem.getTableName());

            //0未发送、1待完成、2更新、3已完成、4已退单、-1已删除

            if (kotItem.getKotStatus() == 1) {
                holder.status.setText(mContext.getString(R.string.kot_in_progress));
            } else if (kotItem.getKotStatus() == 0) {
                holder.status.setText(mContext.getString(R.string.kot_in_progress));
            } else if (kotItem.getKotStatus() == 2) {
                //holder.status.setText("更新");
            } else if (kotItem.getKotStatus() == 3) {
                //	holder.status.setText("已完成");
            } else if (kotItem.getKotStatus() == 4) {
                //	holder.status.setText("已退单");
            } else if (kotItem.getKotStatus() == -1) {
                //	holder.status.setText("已删除");
            }
            long createTime = kotItem.getUpdateTime();
            holder.tv_lan_progress.setBase(SystemClock.elapsedRealtime() - (System.currentTimeMillis() - createTime));
            holder.qty.setText(kotItem.getQty() + "");
            holder.tv_lan_progress.start();
            if (kotItem.getCallType() == 1) {
                //	holder.btn_call.setClickable(false);
                holder.btn_call.setText(mContext.getResources().getString(R.string.call_again));

            } else {
                holder.btn_call.setText(mContext.getResources().getString(R.string.call));
                //	holder.btn_call.setClickable(true);
                // holder.btn_call.setText("void");

            }


        holder.btn_call.setBackgroundColor(mContext.getResources().getColor(R.color.color_kotview));
        holder.btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ButtonClickTimer.canClick()) {
                    return;
                }
                mainPosInfo = App.instance.getCurrentConnectedMainPos();
                Message message = new Message();
                message.obj = kotItem;
                message.arg2 = kotItem.getItemDetailId();
                message.what = App.HANDLER_KOT_CALL_NUM;
                handler.sendMessage(message);
            }
        });

        holder.btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!ButtonClickTimer.canClick()) {
                    return;
                }

                Gson gson = new Gson();
                KotItemDetail kotItemDetail = gson.fromJson(kotItem.getItemDetail(), KotItemDetail.class);
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putSerializable("kotSummary", KotSummarySQL.getKotSummaryById(kotItem.getSummaryId()));

                bundle.putInt("kotItemDetailId", kotItem.getItemDetailId());
                bundle.putInt("id", 1);
                message.setData(bundle);
                message.what = App.HANDLER_KOT_COMPLETE;
                handler.sendMessage(message);
            }
        });
        //  }

    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return kots.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        Chronometer tv_lan_progress;
        TextView name, status, table, orderNo, detail, mod, qty;
        Button btn_complete, btn_call;
        Button complete, call;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     * @return the kots
     */
    public List<KotItem> getKots() {
        return kots;
    }

    /**
     * @param
     */
    public void setKots(List<KotItem> kotlist) {
        kots.clear();
        this.kots.addAll(kotlist);
    }

}