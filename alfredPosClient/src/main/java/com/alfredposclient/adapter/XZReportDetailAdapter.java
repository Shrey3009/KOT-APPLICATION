package com.alfredposclient.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.utils.BH;
import com.alfredposclient.R;
import com.alfredposclient.javabean.ReportDetailAnalysisItem;

import java.util.List;

/**
 * Created by Zun on 2017/6/12 0012.
 */

public class XZReportDetailAdapter extends BaseAdapter {

    private List<ReportDetailAnalysisItem> reportDetailAnalysisItems;
    private BaseActivity activity;

    public XZReportDetailAdapter(List<ReportDetailAnalysisItem> reportDetailAnalysisItems, BaseActivity activity) {
        this.reportDetailAnalysisItems = reportDetailAnalysisItems;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return reportDetailAnalysisItems.size();
    }

    @Override
    public Object getItem(int position) {
        return reportDetailAnalysisItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(activity).inflate(R.layout.xzreport_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.xzreport_item_name = (TextView) convertView.findViewById(R.id.xzreport_item_name);
            viewHolder.xzreport_item_num = (TextView) convertView.findViewById(R.id.xzreport_item_num);
            viewHolder.xzreport_item_total = (TextView) convertView.findViewById(R.id.xzreport_item_total);
            viewHolder.xarepurt_item = (LinearLayout) convertView.findViewById(R.id.xarepurt_item);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ReportDetailAnalysisItem reportDetailAnalysisItem = reportDetailAnalysisItems.get(position);
//        viewHolder.xarepurt_item.setBackgroundColor(Color.parseColor("#B4EEB4"));
        viewHolder.xzreport_item_name.setText(reportDetailAnalysisItem.getName());
        if(reportDetailAnalysisItem.isShowOther()){
            viewHolder.xzreport_item_num.setVisibility(View.VISIBLE);
            viewHolder.xzreport_item_total.setVisibility(View.VISIBLE);
            viewHolder.xzreport_item_num.setText(reportDetailAnalysisItem.getQty() + "");
            viewHolder.xzreport_item_total.setText(BH.formatMoney(reportDetailAnalysisItem.getAmount().toString()).toString());
        }else{
            viewHolder.xzreport_item_num.setVisibility(View.INVISIBLE);
            viewHolder.xzreport_item_total.setVisibility(View.INVISIBLE);
        }



        return convertView;
    }

    class ViewHolder{
        TextView xzreport_item_name;
        TextView xzreport_item_num;
        TextView xzreport_item_total;
        LinearLayout xarepurt_item;
    }
}
