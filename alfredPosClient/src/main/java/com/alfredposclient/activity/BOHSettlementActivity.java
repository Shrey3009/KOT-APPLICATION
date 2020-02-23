package com.alfredposclient.activity;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.LoadingDialog;
import com.alfredbase.global.CoreData;
import com.alfredbase.http.ResultCode;
import com.alfredbase.javabean.BohHoldSettlement;
import com.alfredbase.javabean.User;
import com.alfredbase.store.Store;
import com.alfredbase.utils.BH;
import com.alfredbase.utils.DialogFactory;
import com.alfredbase.utils.TimeUtil;
import com.alfredposclient.R;
import com.alfredposclient.global.App;
import com.alfredposclient.global.SyncCentre;
import com.alfredposclient.global.UIHelp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Alex on 16/10/14.
 */

public class BOHSettlementActivity extends BaseActivity {
    private ListView lv_boh_list;
    private TextView tv_bill_no;
    private TextView tv_boh_amount;
    private TextView tv_name;
    private TextView tv_bill_date;
    private TextView tv_remarks;
    private TextView tv_payment_cash;
    private TextView tv_payment_card;
    private EditText et_amount;
    private LinearLayout ll_boh_detail;
    private Button btn_save;
    public static final int BOH_GETBOHHOLDUNPAID_INFO = 1;
    public static final int UPLOAD_BOH_PAID = 2;

    public static final int PAID_CASH = 0;
    public static final int PAID_CARD = 1;
    private int paid_type;
    private int selectPosition;
    private List<BohHoldSettlement> bohHoldSettlementList;
    private BohAdapter bohAdapter;

    @Override
    protected void initView() {
        super.initView();
        bohHoldSettlementList = new ArrayList<BohHoldSettlement>();
        setContentView(R.layout.activity_boh_settlement);
        lv_boh_list = (ListView) findViewById(R.id.lv_boh_list);
        tv_bill_no = (TextView) findViewById(R.id.tv_bill_no);
        tv_boh_amount = (TextView) findViewById(R.id.tv_boh_amount);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_bill_date = (TextView) findViewById(R.id.tv_bill_date);
        tv_remarks = (TextView) findViewById(R.id.tv_remarks);
        tv_payment_cash = (TextView) findViewById(R.id.tv_payment_cash);
        tv_payment_card = (TextView) findViewById(R.id.tv_payment_card);
        et_amount = (EditText) findViewById(R.id.et_amount);
        btn_save = (Button) findViewById(R.id.btn_save);
        tv_payment_cash.setOnClickListener(this);
        tv_payment_card.setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);
        btn_save.setOnClickListener(this);
        ((TextView)findViewById(R.id.tv_title_name)).setText(getResources().getString(R.string.boh_settlement));
        findViewById(R.id.btn_cancel).setOnClickListener(this);
        ll_boh_detail = (LinearLayout) findViewById(R.id.ll_boh_detail);
        loadingDialog = new LoadingDialog(context);
        loadingDialog.setTitle(getResources().getString(R.string.loading));
        SyncCentre.getInstance().getBOHSettlement(context, mHandler);
        selectPosition = -1;
        lv_boh_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectPosition = position;
                showBohDetail(bohHoldSettlementList.get(selectPosition));
            }
        });
    }

    private void showBohDetail(BohHoldSettlement bohHoldSettlement){
        ll_boh_detail.setVisibility(View.VISIBLE);
        tv_name.setText(bohHoldSettlement.getNameOfPerson());
        tv_bill_no.setText(bohHoldSettlement.getBillNo().toString());
        tv_boh_amount.setText(App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.getBD(bohHoldSettlement.getAmount()).toString());
        tv_bill_date.setText(TimeUtil.getTime(bohHoldSettlement.getDaysDue()));
        tv_remarks.setText(bohHoldSettlement.getRemarks());
        et_amount.setText(BH.getBD(bohHoldSettlement.getAmount()).toString());
        btn_save.setTag(bohHoldSettlement);
        selectPayType(PAID_CASH);
    }

    private void hideBohDetail(){
        ll_boh_detail.setVisibility(View.GONE);
        tv_name.setText("");
        tv_bill_no.setText("");
        tv_boh_amount.setText("");
        tv_bill_date.setText("");
        tv_remarks.setText("");
        et_amount.setText("");
        selectPosition = -1;
        if(bohAdapter == null){
            bohAdapter = new BohAdapter();
            lv_boh_list.setAdapter(bohAdapter);
        }else{
            bohAdapter.notifyDataSetChanged();
        }
    }

    private void selectPayType(int payType){
        paid_type = payType;
        if(PAID_CASH == payType){
            Drawable select= getResources().getDrawable(R.drawable.select);
            select.setBounds(0, 0, select.getMinimumWidth(), select.getMinimumHeight());
            tv_payment_cash.setCompoundDrawables(select,null,null,null);
            Drawable nomol= getResources().getDrawable(R.drawable.nomol);
            nomol.setBounds(0, 0, nomol.getMinimumWidth(), nomol.getMinimumHeight());
            tv_payment_card.setCompoundDrawables(nomol,null,null,null);
        }else{
            Drawable select= getResources().getDrawable(R.drawable.select);
            select.setBounds(0, 0, select.getMinimumWidth(), select.getMinimumHeight());
            tv_payment_card.setCompoundDrawables(select,null,null,null);
            Drawable nomol= getResources().getDrawable(R.drawable.nomol);
            nomol.setBounds(0, 0, nomol.getMinimumWidth(), nomol.getMinimumHeight());
            tv_payment_cash.setCompoundDrawables(nomol,null,null,null);
        }


    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case BOH_GETBOHHOLDUNPAID_INFO: {
                    dismissLoadingDialog();
                    bohHoldSettlementList = (List<BohHoldSettlement>) msg.obj;
                    if(bohAdapter == null){
                        bohAdapter = new BohAdapter();
                        lv_boh_list.setAdapter(bohAdapter);
                    }else{
                        bohAdapter.notifyDataSetChanged();
                    }

                }
                break;
                case ResultCode.CONNECTION_FAILED:
                    UIHelp.showToast(context, ResultCode.getErrorResultStr(context,
                            (Throwable) msg.obj, context.getResources().getString(R.string.server)));
                    break;
                case UPLOAD_BOH_PAID:
                    SyncCentre.getInstance().getBOHSettlement(context, mHandler);
                    break;
            }
        }
    };

    @Override
    protected void handlerClickEvent(View v) {
        super.handlerClickEvent(v);
        switch (v.getId()){
            case R.id.btn_save:
                String amount = et_amount.getText().toString();
                if(TextUtils.isEmpty(amount.trim())){
                    UIHelp.showShortToast(context, context.getString(R.string.please_input_amount));
                    return;
                }
                Map<String, Object> map = new HashMap<String, Object>();
                BohHoldSettlement bohHoldSettlement = (BohHoldSettlement) v.getTag();
                if(bohHoldSettlement == null){
                    return;
                }
                bohHoldSettlement.setPaymentType(paid_type);
                bohHoldSettlement.setStatus(1);
                loadingDialog.show();
                map.put("bohHoldSettlement", bohHoldSettlement);
                SyncCentre.getInstance().upDateBOHHoldPaid(context, map, mHandler);
                break;
            case R.id.btn_cancel:
                hideBohDetail();
                break;
            case R.id.tv_payment_cash:
                selectPayType(PAID_CASH);
                break;
            case R.id.tv_payment_card:
                selectPayType(PAID_CARD);
                break;
            case R.id.btn_back:
                this.finish();
                break;
        }
    }
    @Override
    public void httpRequestAction(int action, Object obj) {
        if (action == ResultCode.DEVICE_NO_PERMIT) {
            this.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    dismissLoadingDialog();
                    DialogFactory.showOneButtonCompelDialog(context, context.getResources().getString(R.string.warning),
                            ResultCode.getErrorResultStrByCode(context,
                                    ResultCode.DEVICE_NO_PERMIT, null),
                            new View.OnClickListener() {

                                @Override
                                public void onClick(View arg0) {
                                    Store.remove(context, Store.SYNC_DATA_TAG);
                                    App.instance.popAllActivityExceptOne(Welcome.class);
                                }
                            });
                }
            });
        }
    }

    protected class BohAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        public BohAdapter() {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return bohHoldSettlementList.size();
        }

        @Override
        public Object getItem(int position) {
            return bohHoldSettlementList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.boh_item_layout, null);
                viewHolder = new ViewHolder();
                viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                viewHolder.tv_bill_num = (TextView) convertView.findViewById(R.id.tv_bill_num);
                viewHolder.tv_amount_auth_by = (TextView) convertView.findViewById(R.id.tv_amount_auth_by);
                viewHolder.tv_due_days = (TextView) convertView.findViewById(R.id.tv_due_days);
                viewHolder.tv_bill_date = (TextView) convertView.findViewById(R.id.tv_bill_date);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            BohHoldSettlement bohHoldSettlement = bohHoldSettlementList.get(position);
            viewHolder.tv_name.setText(bohHoldSettlement.getNameOfPerson());
            viewHolder.tv_bill_num.setText(bohHoldSettlement.getBillNo().toString());
            User user = CoreData.getInstance().getUserById(bohHoldSettlement.getAuthorizedUserId().intValue());
            String userName;
            if(user != null){
                userName = user.getFirstName() + user.getLastName();
            }else{
                userName = bohHoldSettlement.getAuthorizedUserId().intValue() + "";
            }
            viewHolder.tv_amount_auth_by.setText(userName);
            int day = (int) ((System.currentTimeMillis()-bohHoldSettlement.getDaysDue())/(24*60*60*1000));
            viewHolder.tv_due_days.setText(day > 2 ? day + getString(R.string.days) : day + getString(R.string.day));
            viewHolder.tv_bill_date.setText(TimeUtil.getTime(bohHoldSettlement.getDaysDue()));
            if(selectPosition == position){
                convertView.setBackgroundColor(getResources().getColor(R.color.white));
            }else{
                convertView.setBackgroundColor(getResources().getColor(R.color.gray_bg));
            }
            return convertView;
        }

        class ViewHolder {
            TextView tv_name;
            TextView tv_bill_num;
            TextView tv_amount_auth_by;
            TextView tv_due_days;
            TextView tv_bill_date;
        }
    }
}
