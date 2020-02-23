package com.alfredwaiter.activity;

import android.content.Context;
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
import com.alfredbase.javabean.RevenueCenter;
import com.alfredbase.utils.RxBus;
import com.alfredbase.utils.TextTypeFace;
import com.alfredwaiter.R;
import com.alfredwaiter.global.App;
import com.alfredwaiter.global.UIHelp;
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
//	public static final int SYNC_DATA_TAG = 2015;
	
	private int syncDataCount = 0;
	@Override
	protected void initView() {
		super.initView();
//		loadingDialog = new LoadingDialog(context);
		setContentView(R.layout.activity_select_revenue);
		lv_revenue_centre = (ListView) findViewById(R.id.lv_revenue_centre);
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
				App.instance.setPairingIp(udpMsg.getIp());
				UIHelp.startEmployeeID(context);
				finish();
			}
		});
		findViewById(R.id.btn_manually).setOnClickListener(this);
		findViewById(R.id.iv_refresh).setOnClickListener(this);

		((TextView)findViewById(R.id.tv_app_version)).setText(context.getResources().getString(R.string.version) + App.instance.VERSION);
		initTextTypeFace();
		loadingDialog = new LoadingDialog(this);
		loadingDialog.setTitle(this.getString(R.string.search_revenue));
		loadingDialog.showByTime(5000);
		observable = RxBus.getInstance().register(RxBus.RECEIVE_IP_ACTION);
		observable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<UdpMsg>() {
			@Override
			public void call(UdpMsg udpMsg) {
				if(revenueListAdapter == null) {
					List<UdpMsg> udpMsgList = new ArrayList<>();
					udpMsgList.add(udpMsg);
					revenueListAdapter = new RevenueListAdapter(udpMsgList, context);
					lv_revenue_centre.setAdapter(revenueListAdapter);
				}else {
					revenueListAdapter.notifyData(udpMsg);
				}
			}
		});

		App.instance.searchRevenueIp();

	}

	@Override
	protected void onDestroy() {
		if(observable != null)
			RxBus.getInstance().unregister(RxBus.RECEIVE_IP_ACTION, observable);
		super.onDestroy();
	}

	private void initTextTypeFace() {
		textTypeFace = TextTypeFace.getInstance();
		textTypeFace.setTrajanProRegular((TextView)findViewById(R.id.tv_app_version));
		textTypeFace.setTrajanProRegular((TextView)findViewById(R.id.tv_select_rev_tips));
	}

	@Override
	public void handlerClickEvent(View v) {
		super.handlerClickEvent(v);
		switch (v.getId()) {
			case R.id.btn_manually:
				UIHelp.startConnectPOS(context);
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
		List<UdpMsg> udpMsgList;
		private LayoutInflater inflater;

		public RevenueListAdapter(List<UdpMsg> udpMsgList, Context context) {
			this.udpMsgList = new ArrayList<>();
			this.udpMsgList.addAll(udpMsgList);
			inflater = LayoutInflater.from(context);
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
				arg1.setTag(holder);
			} else {
				holder = (ViewHolder) arg1.getTag();
			}
			UdpMsg udpMsg = udpMsgList.get(arg0);
			holder.tv_text.setText(udpMsg.getName() + "\n" + udpMsg.getIp());
			textTypeFace.setTrajanProRegular(holder.tv_text);
			return arg1;
		}

		private void  addData(UdpMsg udpMsg){
			for(UdpMsg udpMsg1 : this.udpMsgList){
				if(udpMsg1.getIp().equals(udpMsg.getIp())){
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
		            doubleBackToExitPressedOnce=false;
		        }
		    }, 2000);
	}
}
