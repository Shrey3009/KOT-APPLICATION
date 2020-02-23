package com.alfredkds.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.BaseApplication;
import com.alfredbase.LoadingDialog;
import com.alfredbase.ParamConst;
import com.alfredbase.http.ResultCode;
import com.alfredbase.javabean.Printer;
import com.alfredbase.javabean.model.KDSDevice;
import com.alfredbase.store.Store;
import com.alfredbase.utils.CommonUtil;
import com.alfredbase.utils.TextTypeFace;
import com.alfredkds.R;
import com.alfredkds.global.App;
import com.alfredkds.global.SyncCentre;
import com.alfredkds.global.UIHelp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectKitchen extends BaseActivity {
    private ListView lv_kitchen_centre;
    private List<Printer> printers = new ArrayList<Printer>();

    //	public static final int PAIRING_COMPLETE = 3;
    public static final int HANDLER_ERROR = 4;

    private boolean doubleBackToExitPressedOnce = false;
    private TextTypeFace textTypeFace;

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_select_kitchen);
        lv_kitchen_centre = (ListView) findViewById(R.id.lv_kitchen_centre);
        loadingDialog = new LoadingDialog(context);
        Intent intent = getIntent();
        printers = (List<Printer>) intent.getSerializableExtra("printers");
        if (printers == null || printers.isEmpty()) {
            UIHelp.showToast(context, context.getString(R.string.pos_not_connect_to_kitchen));
            finish();
        }
        lv_kitchen_centre.setAdapter(new KitchenListAdapter(context, printers));

        lv_kitchen_centre.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Printer printer = printers.get(position);
                KDSDevice kdsDevice = new KDSDevice();
                kdsDevice.setDevice_id(printer.getId());
                kdsDevice.setName(printer.getPrinterName());
                kdsDevice.setIP(CommonUtil.getLocalIpAddress());
                kdsDevice.setMac(CommonUtil.getLocalMacAddress(context));
                kdsDevice.setKdsType(printer.getPrinterUsageType());

                App.instance.setPrinter(printer);
                App.instance.setKdsDevice(kdsDevice);

                Map<String, Object> parameters = new HashMap<String, Object>();
                parameters.put("device", kdsDevice);
                parameters.put("deviceType", ParamConst.DEVICE_TYPE_KDS);

                List<String> ips = new ArrayList<>();
//                if (App.instance.isBalancer()) {
                    ips.addAll(App.instance.getAllPairingIp());
//                } else {
//                    ips.add(App.instance.getPairingIp());
//                }

                for (String ip : ips) {
                    SyncCentre.getInstance().pairingComplete(context, ip, parameters, handler);
                }
            }
        });

        ((TextView) findViewById(R.id.tv_version)).setText(context.getResources().getString(R.string.version) + App.instance.VERSION);
        initTextTypeFace();
    }

    private void initTextTypeFace() {
        textTypeFace = TextTypeFace.getInstance();
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.tv_app_version));
        textTypeFace.setTrajanProRegular((TextView) findViewById(R.id.tv_kitchen_tips));
    }

    @Override
    public void handlerClickEvent(View v) {
        super.handlerClickEvent(v);
    }

    private int pairingSize = App.instance.getAllPairingIp().size();
    private int pairingCount = 0;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                // case UIHelp.GET_PRINTER_OK:
                // load.dismiss();
                // printers = (List<Printer>) msg.obj;
                //
                // lv_kitchen_centre.setAdapter(new KitchenListAdapter(context,
                // kitchens));
                //
                // lv_kitchen_centre.setOnItemClickListener(new
                // OnItemClickListener() {
                //
                // @Override
                // public void onItemClick(AdapterView<?> parent, View view,
                // int position, long id) {
                // //UIHelp.startLogin(context);
                // //finish();
                // SyncCentre.getInstance().pairingComplete(context,
                // App.instance.getKds(), handler);
                // }
                // });
                // break;
                case ResultCode.SUCCESS:
                    // Store POS device Info once pairing success
                    pairingCount++;
                    if (pairingCount >= pairingSize) {
                        loadingDialog.dismiss();
                        UIHelp.startLogin(context);
                        finish();
                    }
                    break;
                case HANDLER_ERROR:
                    loadingDialog.dismiss();
                    pairingCount = 0;
                    //remove KDS info
                    Store.remove(context, Store.KDS_DEVICE);
                    break;
                case ResultCode.CONNECTION_FAILED:
                    loadingDialog.dismiss();
                    pairingCount = 0;
                    UIHelp.showToast(context, ResultCode.getErrorResultStr(context, (Throwable) msg.obj,
                            context.getResources().getString(R.string.revenue_center)));
                    break;
                default:
                    break;
            }
        }

        ;
    };

    class KitchenListAdapter extends BaseAdapter {
        private Context context;

        private List<Printer> kdsPrinters;

        private LayoutInflater inflater;

        public KitchenListAdapter() {

        }

        public KitchenListAdapter(Context context, List<Printer> printers) {
            this.context = context;
            if (printers == null)
                printers = Collections.emptyList();
            this.kdsPrinters = printers;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return kdsPrinters.size();
        }

        @Override
        public Object getItem(int arg0) {
            return kdsPrinters.get(arg0);
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
            holder.tv_text.setText(kdsPrinters.get(arg0).getPrinterName());
            textTypeFace.setTrajanProRegular(holder.tv_text);
            return arg1;
        }

        class ViewHolder {
            public TextView tv_text;
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
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
}
