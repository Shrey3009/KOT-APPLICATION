package com.alfredmenu.activity;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.ParamConst;
import com.alfredbase.global.CoreData;
import com.alfredbase.javabean.ItemDetail;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.javabean.OrderModifier;
import com.alfredbase.store.sql.OrderDetailSQL;
import com.alfredbase.store.sql.OrderModifierSQL;
import com.alfredbase.store.sql.OrderSQL;
import com.alfredbase.utils.ObjectFactory;
import com.alfredbase.utils.TextTypeFace;
import com.alfredmenu.activity.MainPage;
import com.alfredmenu.global.App;
import com.alfredmenu.global.UIHelp;
import com.alfredmenu.popupwindow.ModifierWindow;
import com.alfredmenu.popupwindow.SetItemCountWindow;
import com.alfredmenu.popupwindow.WaiterModifierCPWindow;
import com.alfredmenu.utils.WaiterUtils;
import com.alfredmenu.view.CountView;
import com.alfredmenu.view.DeleteView;
import com.alfredmenu.view.ModifierCountView;
import com.alfredmenu.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class OrderDetailPage extends BaseActivity {
	public static final int REFRESH_ORDER_DETAIL = 1;
	private ListView lv_modifier;
	private ListView lv_instruction;
//	private TextView tv_instructions_text;
//	private TextView tv_brief_inst;
	private OrderDetail orderDetail;
	private Order order;
	private ItemDetail itemDetail;
	private int currentGroupId;
	private CountView count_view;
	private TextView tv_item_name;
	private TextView tv_item_price;
	private List<OrderModifier> orderModifiers = new ArrayList<OrderModifier>();
	private List<String> instructions = new ArrayList<String>();
	private MoidifierAdapter moidifierAdapter;
	private InstructionAdapter instructionAdapter;
	private SetItemCountWindow setItemCountWindow;
	private WaiterModifierCPWindow modifierWindow;
	private TextTypeFace textTypeFace;
	@Override
	protected void initView() {
		super.initView();
		setContentView(R.layout.activity_dish_detail);
		getIntentData();
		initTitle();
		lv_modifier = (ListView) findViewById(R.id.lv_modifier);
		lv_instruction = (ListView) findViewById(R.id.lv_instruction);
		tv_item_name = (TextView) findViewById(R.id.tv_item_name);
		tv_item_name.setText(itemDetail.getItemName());
		tv_item_price = (TextView) findViewById(R.id.tv_item_price);
		tv_item_price.setText(itemDetail.getPrice());
		
		moidifierAdapter = new MoidifierAdapter(orderModifiers);
		lv_modifier.setAdapter(moidifierAdapter);
		instructionAdapter = new InstructionAdapter(instructions);
		lv_instruction.setAdapter(instructionAdapter);
		setItemCountWindow = new SetItemCountWindow(this, findViewById(R.id.rl_root),
				handler);
		countViewAction();
		findViewById(R.id.ll_bottom_left).setOnClickListener(this);
		findViewById(R.id.ll_bottom_right).setOnClickListener(this);
//		findViewById(R.id.iv_back).setOnClickListener(this);
		initTextTypeFace();
	}
	
	private void countViewAction(){
		count_view = (CountView) findViewById(R.id.count_view);
		
		if(orderDetail == null){
			count_view.setIsCanClick(true);
			count_view.setVisibility(false);
			count_view.setInitCount(0);
		}else{
			if(orderDetail.getOrderDetailStatus() == ParamConst.ORDERDETAIL_STATUS_KOTPRINTERD){
				count_view.setIsCanClick(false);
			}else{
				count_view.setIsCanClick(true);
			}
			
			count_view.setInitCount(orderDetail.getItemNum());
		}
		count_view.setParam(itemDetail, setItemCountWindow);
		count_view.setNotes(itemDetail, modifierWindow);
		count_view.setOnCountChange(new CountView.OnCountChange() {
			@Override
			public void onChange(ItemDetail itemDetail, int count, boolean isAdd, boolean isDesc) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("count", count);
				map.put("itemDetail", itemDetail);
				map.put("isAdd", isAdd);
				map.put("isDesc", isDesc);
				handler.sendMessage(handler.obtainMessage(
						MainPage.VIEW_EVENT_MODIFY_ITEM_COUNT, map));
			}
		});
	}
	
	public void initTitle(){
		findViewById(R.id.iv_back).setOnClickListener(this);
		((TextView)findViewById(R.id.tv_title_name)).setText(context.getResources().getString(R.string.detail));
	}
	
	private void getIntentData() {
		Intent intent = getIntent();
		order = (Order) intent.getSerializableExtra("order");
		orderDetail = (OrderDetail) intent.getSerializableExtra("orderDetail");
		itemDetail = (ItemDetail) intent.getSerializableExtra("itemDetail");
		currentGroupId = intent.getIntExtra("currentGroupId", 0);
		orderModifiers = OrderModifierSQL.getAllOrderModifierByOrderDetailAndNormal(orderDetail);
//		String inst = orderDetail.getSpecialInstractions();
		ImageView iv_item_picture = (ImageView) findViewById(R.id.iv_item_picture);
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(itemDetail.getImgUrl(), iv_item_picture);
		if (orderDetail == null || orderDetail.getSpecialInstractions() == null || 
				orderDetail.getSpecialInstractions().equals("")) {
			return;
		}else {
			instructions = arrayToList(orderDetail.getSpecialInstractions().split("#"));
		}

	}
	
	private List<String> arrayToList(String[] arr){
		List<String> result = new ArrayList<String>();
		if (arr == null || (arr != null && arr.length == 0)) {
			return null;
		}else {
			for (int i = 0; i < arr.length; i++) {
				result.add(arr[i]);
			}
		}
		return result;
	}
	
	private void refreshModifierGrid(){
		orderModifiers.clear();
		orderModifiers.addAll(OrderModifierSQL.getAllOrderModifierByOrderDetailAndNormal(orderDetail));
		moidifierAdapter.notifyDataSetChanged();
	}
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MainPage.VIEW_EVENT_MODIFY_ITEM_COUNT: {
				Map<String, Object> map = (Map<String, Object>) msg.obj;
				updateOrderDetail(
						(Integer) map.get("count"));
				count_view.setInitCount((Integer) map.get("count"));
				break;
			}
			default:
				break;
			}
		};
	};
	
	private void updateOrderDetail( int count) {
		if (count <= 0) {// 删除
			OrderDetailSQL.deleteOrderDetail(orderDetail);
			OrderModifierSQL.deleteOrderModifierByOrderDetail(orderDetail);
		} else {// 添加
			order.setOrderStatus(ParamConst.ORDER_STATUS_OPEN_IN_WAITER);
			OrderSQL.update(order);
			if (orderDetail == null) {
				orderDetail = ObjectFactory.getInstance().createOrderDetailForWaiter(
						order, itemDetail, currentGroupId, App.instance.getUser());
				orderDetail.setItemNum(count);
				OrderDetailSQL.addOrderDetailETCForWaiter(orderDetail);
			} else {
				orderDetail.setItemNum(count);
				OrderDetailSQL.updateOrderDetailAndOrderForWaiter(orderDetail);
			}
		}
	}

	@Override
	public void handlerClickEvent(View v) {
		super.handlerClickEvent(v);
		switch (v.getId()) {
		case MainPage.TRANSFER_TABLE_NOTIFICATION:
			WaiterUtils.showTransferTableDialog(context);
			break;
		case R.id.ll_bottom_left:
			if (orderDetail != null) {
				UIHelp.startModifierDetail(context, itemDetail, order, orderDetail);
			} else {
				UIHelp.showToast(context, context.getResources().getString(R.string.add_item));
			}
			break;
		case R.id.ll_bottom_right:
			if (orderDetail != null) {
				UIHelp.startInstructionDetail(context, orderDetail, REFRESH_ORDER_DETAIL);
			} else {
				UIHelp.showToast(context, context.getResources().getString(R.string.add_item));
			}
			break;
		case R.id.iv_back:
			finish();
			break;
		default:
			break;
		}
	}
	
	@Override
	public void httpRequestAction(int action, Object obj) {
		super.httpRequestAction(action, obj);
		if(MainPage.TRANSFER_TABLE_NOTIFICATION == action){
			Order mOrder = (Order) obj;
			if(mOrder.getId().intValue() == order.getId().intValue()){
				handler.sendEmptyMessage(MainPage.TRANSFER_TABLE_NOTIFICATION);
			}
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
		refreshModifierGrid();
	}

	private final class MoidifierAdapter extends BaseAdapter {

		private List<OrderModifier> orderModifiers = new ArrayList<OrderModifier>();

		public MoidifierAdapter(List<OrderModifier> orderModifiers) {
			this.orderModifiers = orderModifiers;
		}

		@Override
		public int getCount() {
			return orderModifiers.size();
		}

		@Override
		public Object getItem(int arg0) {
			return orderModifiers.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			final DeleteView deleteView = new DeleteView(context);
			deleteView.setText(CoreData.getInstance().getModifier(orderModifiers.get(arg0).getModifierId()).getModifierName(),textTypeFace);
			deleteView.setTag(orderModifiers.get(arg0));
			deleteView.setDeleteListener(new DeleteView.DeleteListener() {
				@Override
				public void deleteClick() {
					orderModifiers.remove(deleteView.getTag());
					OrderModifierSQL.deleteOrderModifier((OrderModifier) deleteView.getTag());
					notifyDataSetChanged();
				}
			});
			return deleteView;
		}
	}
	private final class InstructionAdapter extends BaseAdapter {

		private List<String> instructions= new ArrayList<String>();

		public InstructionAdapter(List<String> instructions) {
			if (instructions.isEmpty()) {
				return;
			}
			this.instructions = instructions;
		}

		public void setData(List<String> instructions){
			if (instructions.isEmpty()) {
				return;
			}
			this.instructions = instructions;
			notifyDataSetChanged();
		}
		
		@Override
		public int getCount() {
			return instructions.size();
		}

		@Override
		public Object getItem(int arg0) {
			return instructions.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			final DeleteView deleteView = new DeleteView(context);
			deleteView.setText(instructions.get(arg0),textTypeFace);
			deleteView.setTag(instructions.get(arg0));
			deleteView.setDeleteListener(new DeleteView.DeleteListener() {
				@Override
				public void deleteClick() {
					instructions.remove(deleteView.getTag());
					StringBuffer sBuffer = new StringBuffer();
					for (String str : instructions) {
						sBuffer.append(str+"#");
					}
					if (sBuffer.equals("") || sBuffer == null || sBuffer.length() == 0) {
						orderDetail.setSpecialInstractions("");
					}else {
						sBuffer.substring(sBuffer.length()-1);
						orderDetail.setSpecialInstractions(sBuffer.toString());
					}
					
					// TODO
					OrderDetailSQL.updateOrderDetail(orderDetail);
					notifyDataSetChanged();
				}
			});
			return deleteView;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == REFRESH_ORDER_DETAIL && requestCode == REFRESH_ORDER_DETAIL){
			orderDetail = OrderDetailSQL.getOrderDetail(orderDetail.getId());
			String ins = orderDetail.getSpecialInstractions();
			if (ins == null) {
				return ;
			}else {
				instructions = arrayToList(orderDetail.getSpecialInstractions().split("#"));
				instructionAdapter.setData(removeDuplicate(instructions));
			}
		}
	}
	
	public List<String> removeDuplicate(List<String> list) {
		HashSet<String> hashSet = new HashSet<String>(list);
		list.clear();
		list.addAll(hashSet);
		return list;
	} 
	
	private void initTextTypeFace() {
		textTypeFace = TextTypeFace.getInstance();
		textTypeFace.setTrajanProBlod(tv_item_name);
		textTypeFace.setTrajanProRegular(tv_item_price);
		textTypeFace.setTrajanProRegular((TextView)findViewById(R.id.tv_modifier_title));
		textTypeFace.setTrajanProRegular((TextView)findViewById(R.id.tv_instruction_title));
		textTypeFace.setTrajanProRegular((TextView)findViewById(R.id.tv_title_name));
	}
}
