package com.alfredposclient.activity.kioskactivity.subpos;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.LoadingDialog;
import com.alfredbase.http.ResultCode;
import com.alfredbase.utils.RxBus;
import com.alfredbase.utils.TextTypeFace;
import com.alfredposclient.R;
import com.alfredposclient.global.App;
import com.alfredposclient.global.SubPosSyncCentre;
import com.alfredposclient.global.UIHelp;
import com.moonearly.model.UdpMsg;
import com.moonearly.utils.service.UdpServiceCallBack;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Alex on 2017/11/10.
 */

public class SelectRevenue extends BaseActivity {
    List<UdpMsg> udpMsgList = new ArrayList<>();
    private ListView listView;
    private RevenueListAdapter revenueListAdapter;
    private TextTypeFace textTypeFace;
    private Observable<UdpMsg> observable;
    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_select_revenue);
        listView = (ListView) findViewById(R.id.lv_revenue_centre);
        findViewById(R.id.btn_manually).setOnClickListener(this);
        findViewById(R.id.iv_refresh).setOnClickListener(this);
        revenueListAdapter = new RevenueListAdapter(this);
        listView.setAdapter(revenueListAdapter);
        ((TextView)findViewById(R.id.tv_app_version)).setText(context.getResources().getString(R.string.version) + App.instance.VERSION);
        initTextTypeFace();
        loadingDialog = new LoadingDialog(this);
        loadingDialog.setTitle(this.getString(R.string.search_revenue));
        loadingDialog.showByTime(5000);
        observable = RxBus.getInstance().register("RECEIVE_IP_ACTION");
        observable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<UdpMsg>() {
            @Override
            public void call(UdpMsg udpMsg) {
                for(UdpMsg udpMsg1 : udpMsgList){
                    if(udpMsg1.getIp().equals(udpMsg.getIp())){
                        return;
                    }
                }
                udpMsgList.add(udpMsg);
                revenueListAdapter.notifyDataSetChanged();
            }
        });
        App.instance.startUDPService(App.UDP_INDEX_SUB_POS, "KDS", new UdpServiceCallBack() {
            @Override
            public void callBack(final UdpMsg udpMsg) {
                RxBus.getInstance().post("RECEIVE_IP_ACTION", udpMsg);
            }
        });
        App.instance.searchRevenueIp();
    }

    @Override
    protected void onDestroy() {
        if(observable != null)
            RxBus.getInstance().unregister("RECEIVE_IP_ACTION", observable);
        super.onDestroy();
    }

    private void initTextTypeFace() {
        textTypeFace = TextTypeFace.getInstance();
        textTypeFace.setTrajanProRegular((TextView)findViewById(R.id.tv_app_version));
        textTypeFace.setTrajanProRegular((TextView)findViewById(R.id.tv_select_rev_tips));
    }

    @Override
    protected void handlerClickEvent(View v) {
        super.handlerClickEvent(v);
        switch (v.getId()) {
            case R.id.btn_manually:
                UIHelp.startConnectMainPOS(context);
                break;
            case R.id.iv_refresh:
                loadingDialog.setTitle(context.getString(R.string.search_revenue));
                loadingDialog.showByTime(5000);
                App.instance.searchRevenueIp();
                break;
            default:
                break;
        }
    }

    class RevenueListAdapter extends BaseAdapter {
        private Context context;


        private LayoutInflater inflater;
        public RevenueListAdapter(Context context) {
            inflater = LayoutInflater.from(context);
            this.context = context;
        }

        @Override
        public int getCount() {
            return udpMsgList.size();
        }

        @Override
        public Object getItem(int arg0) {
            return udpMsgList.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            ViewHolder holder = null;
            if (arg1 == null) {
                arg1 = inflater.inflate(R.layout.item_pos, null);
                holder = new ViewHolder();
                holder.tv_text = (TextView) arg1.findViewById(R.id.tv_text);
                arg1.setTag(holder);
            } else {
                holder = (ViewHolder) arg1.getTag();
            }
            final UdpMsg udpMsg = udpMsgList.get(arg0);
            textTypeFace.setTrajanProRegular(holder.tv_text);
            holder.tv_text.setText(udpMsg.getName() + "\n" + udpMsg.getIp());
            arg1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    App.instance.setPairingIp(udpMsg.getIp());
                    loadingDialog.setTitle(context.getString(R.string.loading));
                    loadingDialog.show();
                    SubPosSyncCentre.getInstance().chooseRevenue(context, handler);
                }
            });
            return arg1;
        }

        class ViewHolder {
            public TextView tv_text;
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case ResultCode.SUCCESS:
                    dismissLoadingDialog();
                    UIHelp.startSubPosLogin(SelectRevenue.this);
                    break;
                case ResultCode.CONNECTION_FAILED:
                    dismissLoadingDialog();
                    break;
            }

        }
    };
}
