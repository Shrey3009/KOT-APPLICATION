package com.alfredposclient.activity;

import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.LoadingDialog;
import com.alfredbase.javabean.posonly.TableSummary;
import com.alfredbase.store.sql.GeneralSQL;
import com.alfredbase.utils.BH;
import com.alfredposclient.R;
import com.alfredposclient.global.App;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Alex on 2018/3/14.
 */

public class TableSummaryActivity extends BaseActivity {

    private ListView lv_table_summary;
    private TextView tv_total_amount;
    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_table_summary);
        loadingDialog = new LoadingDialog(context);
        loadingDialog.setTitle(context.getString(R.string.loading));
        lv_table_summary = (ListView) findViewById(R.id.lv_table_summary);
        findViewById(R.id.btn_back).setOnClickListener(this);
        tv_total_amount = (TextView) findViewById(R.id.tv_total_amount);
        showSummary();
    }
    private void showSummary(){
        new AsyncTask<String, String, Map<String, Object>>(){

            @Override
            protected void onPreExecute() {
                loadingDialog.show();
            }

            @Override
            protected Map<String, Object> doInBackground(String... params) {
                Map<String, Object> map = GeneralSQL.getTableSummary(App.instance.getBusinessDate());
                return map;
            }

            @Override
            protected void onPostExecute(Map<String, Object> map) {
                dismissLoadingDialog();
                List<TableSummary> tableSummaryList = (List<TableSummary>) map.get("tableSummaryList");
                String total = (String) map.get("total");
                initValue(tableSummaryList, total);

            }
        }.execute();
    }


    private void initValue(List<TableSummary> tableSummaries, String total){
        if(tableSummaries == null || tableSummaries.size() == 0){
            return;
        }
        TableSummaryAdapter tableSummaryAdapter = new TableSummaryAdapter(tableSummaries);
        lv_table_summary.setAdapter(tableSummaryAdapter);
        tv_total_amount.setText(getString(R.string.amount)+" ("+App.instance.getLocalRestaurantConfig().getCurrencySymbol()+total+")");
    }



    @Override
    protected void handlerClickEvent(View v) {
        super.handlerClickEvent(v);
        switch (v.getId()){
            case R.id.btn_back:
                this.finish();
                break;
        }
    }

    class TableSummaryAdapter extends BaseAdapter{
        List<TableSummary> tableSummaryList;
        public TableSummaryAdapter(List<TableSummary> tableSummaryList){
            if(tableSummaryList == null){
                this.tableSummaryList = new ArrayList<>();
            }else{
                this.tableSummaryList = tableSummaryList;
            }
        }
        @Override
        public int getCount() {
            return tableSummaryList.size();
        }

        @Override
        public Object getItem(int position) {
            return tableSummaryList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if(convertView == null){
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.table_summary_item, null);
                viewHolder.tv_staff = (TextView) convertView.findViewById(R.id.tv_staff);
                viewHolder.tv_order_no = (TextView) convertView.findViewById(R.id.tv_order_no);
                viewHolder.tv_table_name = (TextView) convertView.findViewById(R.id.tv_table_name);
                viewHolder.tv_amount = (TextView) convertView.findViewById(R.id.tv_amount);
                viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            TableSummary tableSummary = tableSummaryList.get(position);
            viewHolder.tv_staff.setText(tableSummary.getFirstName() + " " + tableSummary.getLastName());
            viewHolder.tv_order_no.setText(tableSummary.getOrderNo());
            viewHolder.tv_table_name.setText(tableSummary.getTableName());
            viewHolder.tv_amount.setText(App.instance.getLocalRestaurantConfig().getCurrencySymbol()
                    + BH.getBD(tableSummary.getAmount()).toString());
            long hour = (System.currentTimeMillis() - tableSummary.getStartTime())/3600/1000;
            long min = (System.currentTimeMillis() - tableSummary.getStartTime())/60/1000 - hour*60;
            viewHolder.tv_time.setText( (hour > 9 ? hour : ("0" + hour)) + ":" + (min > 9 ? min : ("0" + min)));
            return convertView;
        }

        public class ViewHolder{
            TextView tv_staff;
            TextView tv_order_no;
            TextView tv_table_name;
            TextView tv_amount;
            TextView tv_time;
        }
    }
}
