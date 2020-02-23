package com.alfredposclient.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.javabean.ReportHourly;
import com.alfredbase.utils.BH;
import com.alfredposclient.R;

import java.util.List;

/**
 * Created by Zun on 2017/6/12 0012.
 */

public class XZReportHourlyAdapter extends BaseAdapter {

    private List<ReportHourly> list;
    private BaseActivity activity;

    public XZReportHourlyAdapter(List<ReportHourly> list, BaseActivity activity) {
        this.list = list;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
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
        ReportHourly reportHourly = list.get(position);
        viewHolder.xarepurt_item.setBackgroundColor(Color.parseColor("#B4EEB4"));
        viewHolder.xzreport_item_name.setText(reportHourly.getHour() + "");
        viewHolder.xzreport_item_num.setText(reportHourly.getAmountQty() + "");
        viewHolder.xzreport_item_total.setText(BH.formatMoney(reportHourly.getAmountPrice()).toString());

        return convertView;
    }

    class ViewHolder{
        TextView xzreport_item_name;
        TextView xzreport_item_num;
        TextView xzreport_item_total;
        LinearLayout xarepurt_item;
    }
}
