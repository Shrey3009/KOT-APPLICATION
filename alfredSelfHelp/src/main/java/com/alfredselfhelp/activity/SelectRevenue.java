package com.alfredselfhelp.activity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.BaseApplication;
import com.alfredbase.LoadingDialog;
import com.alfredbase.javabean.RevenueCenter;
import com.alfredbase.utils.RxBus;
import com.alfredbase.utils.TextTypeFace;

import com.alfredselfhelp.R;
import com.alfredselfhelp.global.App;
import com.alfredselfhelp.utils.KpmTextTypeFace;
import com.alfredselfhelp.utils.UIHelp;
import com.moonearly.model.UdpMsg;
import com.moonearly.utils.service.UdpServiceCallBack;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class SelectRevenue extends BaseActivity {
    //	public static final int HANDLER_PAIRING_COMPLETE = 1;
    public static final String CAN_SELECT_REVENUEID = "CAN_SELECT_REVENUEID";
    // public static final int HANDLER_CONN_ERROR = 4;
    // public static final int HANDLER_CONN_REFUSED = 5;
    // public static final String UNKNOW_ERROR =
    // "Network error: the main POS may be down";
    // public static final String CONN_TIMEOUT = "Network timeout";
    private ListView lv_revenue_centre;
    private RevenueListAdapter revenueListAdapter;
    private List<RevenueCenter> kitchens = new ArrayList<RevenueCenter>();
    private RevenueCenter revenue;
    private boolean doubleBackToExitPressedOnce = false;
    private TextTypeFace textTypeFace;
    private Observable<UdpMsg> observable;
    private TextView tv_re_login, tv_progress;
    private String posIp;
//	public static final int SYNC_DATA_TAG = 2015;

    private int syncDataCount = 0;

    @Override
    protected void initView() {
        super.initView();
//		loadingDialog = new LoadingDialog(context);
        setContentView(R.layout.activity_kpm_revenue);
        lv_revenue_centre = (ListView) findViewById(R.id.lv_revenue_centre);
        tv_re_login = (TextView) findViewById(R.id.tv_re_login);
        tv_progress = (TextView) findViewById(R.id.tv_progress);
        tv_re_login.setOnClickListener(this);
//		Intent intent = getIntent();
//		HashMap<String, Object> map = (HashMap<String, Object>) intent
//				.getSerializableExtra("revenueMap");
//		kitchens = (List<RevenueCenter>) map.get("revenueCenters");
//		revenue = (RevenueCenter) map.get("revenue");
        syncDataCount = 0;
        lv_revenue_centre.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                UdpMsg udpMsg = (UdpMsg) parent.getItemAtPosition(position);
                revenueListAdapter.setCheckedPosition(position);
                posIp = udpMsg.getIp();
                tv_re_login.setBackgroundResource(R.drawable.btn_view_cart);
                tv_re_login.setTextColor(getResources().getColor(R.color.white));
//
            }
        });
        findViewById(R.id.btn_manually).setOnClickListener(this);
        findViewById(R.id.iv_refresh).setOnClickListener(this);


        ((TextView) findViewById(R.id.tv_app_version)).setText(context.getResources().getString(R.string.version) + App.instance.VERSION);
        initTextTypeFace();
//        loadingDialog = new LoadingDialog(this);
//        loadingDialog.setTitle("Search Revenue ...");
//        loadingDialog.showByTime(5000);
        tv_progress.setVisibility(View.VISIBLE);
        lv_revenue_centre.setVisibility(View.GONE);
        new Thread(new MyThread()).start();
        observable = RxBus.getInstance().register(RxBus.RECEIVE_IP_ACTION);
        observable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<UdpMsg>() {
            @Override
            public void call(UdpMsg udpMsg) {
                if (revenueListAdapter == null) {
                    List<UdpMsg> udpMsgList = new ArrayList<>();
                    udpMsgList.add(udpMsg);
                    revenueListAdapter = new RevenueListAdapter(udpMsgList, context);
                    lv_revenue_centre.setAdapter(revenueListAdapter);
                } else {
                    revenueListAdapter.notifyData(udpMsg);
                }
            }
        });
        App.instance.startUDPService(App.UDP_INDEX_SELF_HELP, "Selfhelp", new UdpServiceCallBack() {
            @Override
            public void callBack(UdpMsg udpMsg) {
                RxBus.getInstance().post("RECEIVE_IP_ACTION", udpMsg);
            }
        });
        App.instance.searchRevenueIp();

    }


    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            // 要做的事情

            tv_progress.setVisibility(View.GONE);
            lv_revenue_centre.setVisibility(View.VISIBLE);
            super.handleMessage(msg);
        }
    };


    public class MyThread implements Runnable {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (true) {
                try {
                    Thread.sleep(3000);// 线程暂停10秒，单位毫秒
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);// 发送消息
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (observable != null)
            RxBus.getInstance().unregister(RxBus.RECEIVE_IP_ACTION, observable);
        super.onDestroy();
    }


    @Override
    public void handlerClickEvent(View v) {
        super.handlerClickEvent(v);
        switch (v.getId()) {
            case R.id.btn_manually:
                UIHelp.startConnectPOS(context);
                break;
            case R.id.iv_refresh:
                tv_progress.setVisibility(View.VISIBLE);
                lv_revenue_centre.setVisibility(View.GONE);
//                loadingDialog.setTitle("Search Revenue ...");
//                loadingDialog.showByTime(5000);
                App.instance.searchRevenueIp();
                break;

            case R.id.tv_re_login:

                if (!TextUtils.isEmpty(posIp)) {
                    App.instance.setPairingIp(posIp);
                    UIHelp.startEmployeeID(context);
                    finish();
                }
                break;
            default:
                break;
        }
    }

    class RevenueListAdapter extends BaseAdapter {
        List<UdpMsg> udpMsgList;
        private LayoutInflater inflater;
        KpmTextTypeFace textTypeFace = KpmTextTypeFace.getInstance();
        private int checkedPosition = -1;

        public RevenueListAdapter(List<UdpMsg> udpMsgList, Context context) {
            this.udpMsgList = new ArrayList<>();
            this.udpMsgList.addAll(udpMsgList);
            inflater = LayoutInflater.from(context);
        }

        public void setCheckedPosition(int checkedPosition) {
            this.checkedPosition = checkedPosition;
            notifyDataSetChanged();
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
                arg1 = inflater.inflate(R.layout.item_kitchen, null);
                holder = new ViewHolder();
                holder.tv_text = (TextView) arg1.findViewById(R.id.tv_text);
                holder.img_select = (ImageView) arg1.findViewById(R.id.img_select);
                arg1.setTag(holder);
            } else {
                holder = (ViewHolder) arg1.getTag();
            }
            UdpMsg udpMsg = udpMsgList.get(arg0);

            if (checkedPosition == arg0) {
                holder.img_select.setVisibility(View.VISIBLE);
            } else {
                holder.img_select.setVisibility(View.GONE);
            }
            holder.tv_text.setText(udpMsg.getIp());
            textTypeFace.setUbuntuMedium(holder.tv_text);
            return arg1;
        }

        private void addData(UdpMsg udpMsg) {
            for (UdpMsg udpMsg1 : this.udpMsgList) {
                if (udpMsg1.getIp().equals(udpMsg.getIp())) {
                    return;
                }
            }
            this.udpMsgList.add(udpMsg);
        }

        public void notifyData(UdpMsg udpMsg) {
            addData(udpMsg);
            notifyDataSetChanged();
        }

        class ViewHolder {
            public TextView tv_text;
            public ImageView img_select;
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            revenueListAdapter = null;
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        UIHelp.showToast(this, context.getResources().getString(R.string.exit_program));

        BaseApplication.postHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


    private void initTextTypeFace() {
        KpmTextTypeFace textTypeFace = KpmTextTypeFace.getInstance();
        textTypeFace.setUbuntuMedium((TextView) findViewById(R.id.tv_progress));
        textTypeFace.setUbuntuMedium((TextView) findViewById(R.id.tv_re_login));
        textTypeFace.setUbuntuMedium((TextView) findViewById(R.id.btn_manually));
        textTypeFace.setUbuntuMedium((TextView) findViewById(R.id.tv_version));


    }
}
