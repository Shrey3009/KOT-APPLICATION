package com.alfredkds.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
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
import com.alfredbase.utils.DialogFactory;
import com.alfredbase.utils.IntegerUtils;
import com.alfredbase.utils.ScreenSizeUtil;
import com.alfredbase.utils.TimeUtil;
import com.alfredkds.R;
import com.alfredkds.global.App;
import com.alfredkds.global.SyncCentre;
import com.alfredkds.global.UIHelp;
import com.alfredkds.javabean.Kot;
import com.alfredkds.view.FinishQtyWindow;
import com.alfredkds.view.PopItemAdapter;
import com.alfredkds.view.PopItemListView;
import com.alfredkds.view.PopItemListView.RemoveDirection;
import com.alfredkds.view.PopItemListView.RemoveListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 有多少桌点同样的菜，筛选出来
 *
 * @author
 */
public class Summary extends BaseActivity {
    private static final int HANDLER_REFRESH = 30;

    private ListView dishNamesListView;
    private DishNamesAdapter dishNamesAdapter;
    private ListView kotDetailsListView;
    private KotDetailsAdapter kotdetailsAdapter;
    private FinishQtyWindow finishQtyPop;
    private TextView tv_table_name;
    private TextView tv_item_qyt;
    private boolean isKiosk = true;

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_kot_filter);
        loadingDialog = new LoadingDialog(context);
        loadingDialog.setTitle(context.getResources().getString(R.string.sending));
        for (MainPosInfo mainPos : App.instance.getCurrentConnectedMainPosList()) {
            if (mainPos.getIsKiosk() != ParamConst.MAINPOSINFO_IS_KIOSK) {
                isKiosk = false;
            }
        }
        tv_table_name = (TextView) findViewById(R.id.tv_table_name);
        if (isKiosk) {
            tv_table_name.setVisibility(View.GONE);
        }
        tv_item_qyt = (TextView) findViewById(R.id.tv_item_qyt);
        finishQtyPop = new FinishQtyWindow(context, findViewById(R.id.rl_root), handler);
        initDishNames();
        initKotDetailsListView();
    }

    public void initDishNames() {
        dishNamesListView = (ListView) this.findViewById(R.id.lv_dish_names);
        dishNamesAdapter = new DishNamesAdapter();
        dishNamesAdapter.setDishNames(App.instance.getDishNames());
        dishNamesListView.setAdapter(dishNamesAdapter);
        dishNamesListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                dishNamesAdapter.setSelectItem(position);
                dishNamesAdapter.notifyDataSetInvalidated();
                String dishName = (String) dishNamesAdapter.getItem(position);
                kotdetailsAdapter.setKotDishNames(App.instance.getKotDishDetail(dishName));
                kotdetailsAdapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        int unDoneNum = KotItemDetailSQL.getAllUnDoneKotItemDetailCount();
        tv_item_qyt.setText(unDoneNum + "");
    }

    private void initKotDetailsListView() {
        kotDetailsListView = (ListView) this.findViewById(R.id.lv_kot_details);
        kotdetailsAdapter = new KotDetailsAdapter(context);
        if (!App.instance.getDishNames().isEmpty()) {
            kotdetailsAdapter.setKotDishNames(App.instance.getKotDishDetail(App.instance.getDishNames().get(0)));
        }
        kotDetailsListView.setAdapter(kotdetailsAdapter);
    }

    public Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case HANDLER_REFRESH:
                    dishNamesAdapter.setSelectItem(getPosition());
                    dishNamesAdapter.setDishNames(App.instance.getDishNames());
                    dishNamesAdapter.notifyDataSetChanged();
                    if (dishName != null) {
                        kotdetailsAdapter.setKotDishNames(App.instance.getKotDishDetail(dishName));
                        kotdetailsAdapter.notifyDataSetChanged();
                        if (popupWindow != null && popupWindow.isShowing()) {
                            popItemAdapter.setKot(App.instance.getKot((KotSummary) App.instance.getKotDishDetail(dishName).get(index)[0]));
                            popItemAdapter.notifyDataSetChanged();
                        }
                    } else {
                        kotdetailsAdapter.setKotDishNames(App.instance.getKotDishDetail(App.instance.getDishNames().get(0)));
                        kotdetailsAdapter.notifyDataSetChanged();
                    }
                    break;
                case App.HANDLER_REFRESH_KOT:
                    loadingDialog.dismiss();
                    List<KotItemDetail> skotItemDetails = (List<KotItemDetail>) msg.obj;
                    kotdetailsAdapter.setKotDishNames(App.instance.getKotDishDetail(skotItemDetails.get(0).getItemName()));
                    kotdetailsAdapter.notifyDataSetChanged();
                    if (popItemAdapter != null)
                        popItemAdapter.notifyDataSetChanged();
                    break;
                case App.HANDLER_RECONNECT_POS:
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
                case App.HANDLER_SEND_FAILURE:
                    loadingDialog.dismiss();
                    List<KotItemDetail> fkotItemDetails = (List<KotItemDetail>) msg.obj;
                    kotdetailsAdapter.setKotDishNames(App.instance.getKotDishDetail(fkotItemDetails.get(0).getItemName()));
                    kotdetailsAdapter.notifyDataSetChanged();
                    break;
                case App.HANDLER_RETURN_ERROR:
                    loadingDialog.dismiss();
                    UIHelp.showToast(context, ResultCode.getErrorResultStrByCode(context, (Integer) msg.obj, null));
                    break;
                case App.HANDLER_RETURN_ERROR_SHOW:
                    loadingDialog.dismiss();
                    List<KotItemDetail> mkotItemDetails = (List<KotItemDetail>) msg.obj;
                    kotdetailsAdapter.setKotDishNames(App.instance.getKotDishDetail(mkotItemDetails.get(0).getItemName()));
                    kotdetailsAdapter.notifyDataSetChanged();
                    break;
                case App.HANDLER_SEND_FAILURE_SHOW:
                    UIHelp.showToast(context, context.getResources().getString(R.string.reconnect_pos_));
                    break;
                case ResultCode.CONNECTION_FAILED:
                    loadingDialog.dismiss();
                    UIHelp.showToast(context, ResultCode.getErrorResultStr(context, (Throwable) msg.obj,
                            context.getResources().getString(R.string.revenue_center)));
                    break;
                case App.HANDLER_KOTSUMMARY_IS_UNREAL:
                    loadingDialog.dismiss();
                    UIHelp.showToast(context, context.getResources().getString(R.string.order_discarded));
                    List<String> dishNames = App.instance.getDishNames();
                    if (dishNames.isEmpty()) {
                        dishNamesAdapter.setDishNames(dishNames);
                        dishNamesAdapter.notifyDataSetChanged();
                        kotdetailsAdapter.setKotDishNames(App.instance.getKotDishDetail(dishName));
                        kotdetailsAdapter.notifyDataSetChanged();
                        if (popupWindow != null && popupWindow.isShowing()) {
                            popupWindow.dismiss();
                        }
                        return;
                    }
                    if (dishNames.contains(dishName)) {
                        dishNamesAdapter.setSelectItem(getPosition());
                        dishNamesAdapter.setDishNames(dishNames);
                        dishNamesAdapter.notifyDataSetChanged();
                        kotdetailsAdapter.setKotDishNames(App.instance.getKotDishDetail(dishName));
                        kotdetailsAdapter.notifyDataSetChanged();
                        if (popupWindow != null && popupWindow.isShowing()) {
                            popupWindow.dismiss();
                        }
                    } else {
                        dishNamesAdapter.setSelectItem(0);
                        dishNamesAdapter.setDishNames(dishNames);
                        dishNamesAdapter.notifyDataSetChanged();
                        kotdetailsAdapter.setKotDishNames(App.instance.getKotDishDetail(App.instance.getDishNames().get(0)));
                        kotdetailsAdapter.notifyDataSetChanged();
                        if (popupWindow != null && popupWindow.isShowing()) {
                            popupWindow.dismiss();
                        }
                    }
                case App.HANDLER_KOT_COMPLETE_USER_FAILED:
                    App.instance.reload(context, handler);
                    break;
                case Login.HANDLER_LOGIN:
                    if (loadingDialog.isShowing()) {
                        loadingDialog.dismiss();
                    }
                    if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                    dishNamesAdapter.setSelectItem(0);
                    dishNamesAdapter.setDishNames(App.instance.getDishNames());
                    dishNamesAdapter.notifyDataSetChanged();
                    if (!App.instance.getDishNames().isEmpty()) {
                        kotdetailsAdapter.setKotDishNames(App.instance.getKotDishDetail(App.instance.getDishNames().get(0)));
                    }
                    kotdetailsAdapter.notifyDataSetChanged();
                    kotDetailsListView.setAdapter(kotdetailsAdapter);
                    break;
                default:
                    break;
            }
        }

        ;
    };

    public void httpRequestAction(int action, Object obj) {
        handler.sendMessage(handler.obtainMessage(HANDLER_REFRESH, null));
    }

    ;

    public class DishNamesAdapter extends BaseAdapter {

        public List<String> dishNames = Collections.emptyList();
        private LayoutInflater inflater;
        private int selectItem = 0;

        public void setSelectItem(int selectItem) {
            this.selectItem = selectItem;
        }

        public int getSelectItem() {
            return selectItem;
        }

        public DishNamesAdapter() {
            this.inflater = LayoutInflater.from(context);
        }

        public void setDishNames(List<String> dishNames) {
            this.dishNames = dishNames;
        }

        @Override
        public int getCount() {
            return dishNames.size();
        }

        @Override
        public Object getItem(int position) {
            return dishNames.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_dish_names, null);
                holder = new ViewHolder();
                holder.dishName = (TextView) convertView.findViewById(R.id.tv_dish_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.dishName.setText(dishNames.get(position));
//			if(selectItem >= getCount()){
//				selectItem = 0;
//				dishName = dishNames.get(selectItem);
//			}
			if (position == selectItem) {
				convertView.setBackgroundColor(Color.BLUE);
				dishName = dishNames.get(position);
			}else {
				convertView.setBackgroundColor(Color.WHITE);
			}
			return convertView;
		}
	}
	
	private class KotDetailsAdapter extends BaseAdapter{

		private List<Object[]> kotDishNames = new ArrayList<Object[]>();
		private LayoutInflater inflater;
		
		public KotDetailsAdapter(Context context){
			inflater = LayoutInflater.from(context);
		}
		
		public List<Object[]> getKotDishNames() {
			return kotDishNames;
		}

		public void setKotDishNames(List<Object[]> kotDishNames) {
			this.kotDishNames = kotDishNames;
		}

		@Override
		public int getCount() {
			return kotDishNames.size();
		}

		@Override
		public Object getItem(int position) {
			return kotDishNames.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder2 holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_kot_dish_name, null);
				holder = new ViewHolder2();
				holder.kotId = (TextView) convertView.findViewById(R.id.tv_kotId);
				holder.orderId = (TextView) convertView.findViewById(R.id.tv_orderId);
				holder.table = (TextView) convertView.findViewById(R.id.tv_table);
				holder.pos = (TextView) convertView.findViewById(R.id.tv_pos);
				holder.data = (TextView) convertView.findViewById(R.id.tv_data);
				holder.time = (TextView) convertView.findViewById(R.id.tv_time);
				holder.dish = (TextView) convertView.findViewById(R.id.tv_dish);
				holder.send = (Button) convertView.findViewById(R.id.btn_undone);
				convertView.setTag(holder);
			}else {
				holder = (ViewHolder2) convertView.getTag();
			}
			if (isKiosk) {
				holder.table.setVisibility(View.GONE);
			}
			final KotSummary kotSummary = (KotSummary) kotDishNames.get(position)[0];
			index = position;
			final KotItemDetail kotItemDetail = (KotItemDetail) kotDishNames.get(position)[1];
			holder.kotId.setText(kotSummary.getId()+"");
			holder.orderId.setText(kotSummary.getOrderId()+"");
			holder.table.setText(kotSummary.getTableName()+"");
			holder.orderId.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					showTablePopupWindow(App.instance.getKot(kotSummary));
				}
			});
			holder.pos.setText(kotSummary.getRevenueCenterName()+"");
			holder.data.setText(TimeUtil.getPrintDate(kotSummary.getCreateTime())+"");
			holder.time.setText(TimeUtil.getPrintTime(kotSummary.getCreateTime())+"");
			holder.dish.setText((String)kotDishNames.get(position)[2]);
			if (kotItemDetail.getKotStatus()==ParamConst.KOT_STATUS_DONE) {
				holder.send.setClickable(false);
				holder.send.setText(R.string.item_complete);
				holder.send.setBackgroundColor(Color.GRAY);
			}else if (kotItemDetail.getKotStatus()==ParamConst.KOT_STATUS_VOID) {
				holder.send.setClickable(false);
				holder.send.setText(context.getResources().getString(R.string.void_));
				holder.send.setBackgroundColor(Color.GRAY);
			}else {
				holder.send.setFocusable(true);
				holder.send.setText(R.string.kot_in_progress);
				holder.send.setBackgroundColor(Color.RED);
				holder.send.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if (kotItemDetail.getUnFinishQty() > 1 ) {
							finishQtyPop.show(kotItemDetail.getUnFinishQty()+"",kotSummary, kotItemDetail,loadingDialog);
							return;
						}
						loadingDialog.show();
						kotItemDetail.setKotStatus(ParamConst.KOT_STATUS_DONE);
						kotItemDetail.setUnFinishQty(0);
						kotItemDetail.setFinishQty(1);
						KotItemDetailSQL.update(kotItemDetail);
						List<KotItemDetail> itemDetails = new ArrayList<KotItemDetail>();
						itemDetails.add(kotItemDetail);
						Map<String, Object> parameters = new HashMap<String, Object>();
						parameters.put("kotSummary", kotSummary);
						parameters.put("kotItemDetails", itemDetails);
						parameters.put("userKey", CoreData.getInstance().getUserKey(kotSummary.getRevenueCenterId()));

						SyncCentre.getInstance().kotComplete(context, 
								App.instance.getCurrentConnectedMainPos(kotSummary.getRevenueCenterId()), parameters, handler,-1);
						
//						kotdetailsAdapter.setKotDishNames(App.instance.getKotDishDetail(kotItemDetail.getItemName()));
//						kotdetailsAdapter.notifyDataSetChanged();
					}
				});
			}
			return convertView;
		}
	}
	
	public class ViewHolder{
		public TextView dishName;
	}
	
	public class ViewHolder2{
		public TextView kotId;
		public TextView orderId;
		public TextView table;
		public TextView pos;
		public TextView data;
		public TextView time;
		public TextView dish;
		public Button send;
	}
	
	private PopupWindow popupWindow;
	private PopItemListView popItemListView;
	private PopItemAdapter popItemAdapter;
	private int index;
	private String dishName;
	public void showTablePopupWindow(final Kot kot){
		View view = LayoutInflater.from(context).inflate(R.layout.kitche_order_item_popupwindow, null);
		view.findViewById(R.id.iv_back).setOnClickListener(this);
		view.findViewById(R.id.iv_complete).setOnClickListener(this);
		TextView kotId = (TextView) view.findViewById(R.id.tv_kot_id);
		TextView orderId = (TextView) view.findViewById(R.id.tv_order_id);
		TextView table = (TextView) view.findViewById(R.id.tv_table);
		TextView posName = (TextView) view.findViewById(R.id.tv_pos);
		TextView date = (TextView) view.findViewById(R.id.tv_date);
		TextView time = (TextView) view.findViewById(R.id.tv_time);
		TextView tv_kiosk_order_id = (TextView) view.findViewById(R.id.tv_kiosk_order_id);
		if (isKiosk) {
			tv_kiosk_order_id.setVisibility(View.VISIBLE);
			table.setVisibility(View.GONE);
			orderId.setVisibility(View.GONE);
		}else {
			tv_kiosk_order_id.setVisibility(View.GONE);
			table.setVisibility(View.VISIBLE);
			orderId.setVisibility(View.VISIBLE);
		}
		kotId.setText(kot.getKotSummary().getId()+"");
		orderId.setText(context.getResources().getString(R.string.order_no)+kot.getKotSummary().getNumTag()+kot.getKotSummary().getOrderNo()+"");
		tv_kiosk_order_id.setText(context.getResources().getString(R.string.order_no) +kot.getKotSummary().getNumTag()+ IntegerUtils.formatLocale(kot.getKotSummary().getRevenueCenterIndex(), kot.getKotSummary().getOrderNo() + ""));
        table.setText(context.getResources().getString(R.string.table) + " - " + kot.getKotSummary().getTableName() + "");
		posName.setText(kot.getKotSummary().getRevenueCenterName()+"");
		date.setText(TimeUtil.getPrintDate(kot.getKotSummary().getCreateTime()));
		time.setText(TimeUtil.getPrintTime(kot.getKotSummary().getCreateTime()));
		
		popItemListView = (PopItemListView) view.findViewById(R.id.lv_kot);
		popItemAdapter = new PopItemAdapter(context);
		popItemAdapter.setKot(kot);
		popItemListView.setAdapter(popItemAdapter);
		popItemListView.setRemoveListener(new RemoveListener() {
			@Override
			public void removeItem(RemoveDirection direction, int position) {
				switch (direction) {
				case LEFT:
					if(kot.getKotItemDetails().get(position).getKotStatus()<ParamConst.KOT_STATUS_DONE){
						KotItemDetail kotItemDetail = kot.getKotItemDetails().get(position);
						if (kotItemDetail.getUnFinishQty() > 1 ) {
							finishQtyPop.show(kotItemDetail.getUnFinishQty()+"",kot.getKotSummary(), kotItemDetail,loadingDialog);
							return;
						}
						kotItemDetail.setKotStatus(ParamConst.KOT_STATUS_DONE);
						KotItemDetailSQL.update(kotItemDetail);
						List<KotItemDetail> itemDetails = new ArrayList<KotItemDetail>();
						itemDetails.add(kotItemDetail);
						Map<String, Object> parameters = new HashMap<String, Object>();
						parameters.put("kotSummary", kot.getKotSummary());
						parameters.put("kotItemDetails", itemDetails);
						parameters.put("userKey", CoreData.getInstance().getUserKey(kot.getKotSummary().getRevenueCenterId()));
						SyncCentre.getInstance().kotComplete(context, 
								App.instance.getCurrentConnectedMainPos(kot.getKotSummary().getRevenueCenterId()),parameters, handler,-1);
					}
					break;
				default:
					break;
				}
			}
		});
		popupWindow = new PopupWindow(view,LinearLayout.LayoutParams.MATCH_PARENT,
				(int) (ScreenSizeUtil.height - ScreenSizeUtil.getStatusBarHeight(context)));
		popupWindow.setFocusable(false);
		if (popupWindow != null && !popupWindow.isShowing())
			popupWindow.showAtLocation(findViewById(R.id.lv_kot_details),
					Gravity.TOP | Gravity.LEFT, 0,
					ScreenSizeUtil.getStatusBarHeight(context));
	}
	
	@Override
	public void handlerClickEvent(View v) {
		super.handlerClickEvent(v);
		switch (v.getId()) {
		case R.id.iv_complete:
			if(popupWindow != null && popupWindow.isShowing()){
				dishNamesAdapter.notifyDataSetInvalidated();  
				kotdetailsAdapter.notifyDataSetChanged();
				popupWindow.dismiss();
			}
			
			break;

		default:
			break;
		}
	}
	
	public int getPosition(){
		int position = 0;
		List<String> dishNames = App.instance.getDishNames();
		for (int i = 0; i < dishNames.size(); i++) {
			if (dishNames.get(i).equals(dishName)) {
				position = i;
			}
		}
		return position;
	}
}
