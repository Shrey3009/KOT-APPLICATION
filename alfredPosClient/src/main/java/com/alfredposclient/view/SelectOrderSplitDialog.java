package com.alfredposclient.view;

import android.app.Dialog;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.ParamConst;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.OrderSplit;
import com.alfredbase.store.sql.OrderSplitSQL;
import com.alfredbase.utils.ColorUtils;
import com.alfredbase.utils.DialogFactory;
import com.alfredbase.utils.ScreenSizeUtil;
import com.alfredbase.utils.TextTypeFace;
import com.alfredposclient.R;
import com.alfredposclient.activity.MainPage;
import com.alfredposclient.global.App;

import java.util.ArrayList;
import java.util.List;

public class SelectOrderSplitDialog extends Dialog {
    private LayoutInflater inflater;
    private GridView gv_person_index;
    private Adapter adapter;
    private Handler handler;
    private BaseActivity context;
    private TextTypeFace textTypeFace;
    private List<OrderSplit> orderSplits = new ArrayList<OrderSplit>();
    private Order order;
    private boolean canDelete = false;
	private View contentView;

    public SelectOrderSplitDialog(BaseActivity context, Handler handler) {
        super(context, R.style.Dialog_transparent);
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.handler = handler;
        init();
    }

    private void init() {
		contentView = View.inflate(getContext(),
                R.layout.select_groupid_dialog, null);
        setContentView(contentView);
        textTypeFace = TextTypeFace.getInstance();
        textTypeFace.setTrajanProRegular((TextView) contentView
                .findViewById(R.id.tv_clost_bill_title));
        gv_person_index = (GridView) contentView
                .findViewById(R.id.gv_person_index);
        adapter = new Adapter();
        gv_person_index.setAdapter(adapter);
		gv_person_index.setNumColumns(3);
        gv_person_index.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                OrderSplit orderSplit = orderSplits.get(arg2);
                if (orderSplit.getOrderStatus() != ParamConst.ORDER_STATUS_FINISHED) {
                    if (canDelete) {
                        handler.sendMessage(handler.obtainMessage(MainPage.VIEW_EVENT_SHOW_CLOSE_SPLIT_BY_PAX_BILL, arg1.getTag()));
                    } else {
                        handler.sendMessage(handler.obtainMessage(MainPage.VIEW_EVENT_SHOW_CLOSE_SPLIT_BILL, arg1.getTag()));
                    }
                    dismiss();
                }

            }
        });
//		setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    public void show(List<OrderSplit> orderSplits, Order order) {
        show(orderSplits, order, false);
    }

    public void show(List<OrderSplit> orderSplits, Order order, boolean canDelete) {
        this.orderSplits = orderSplits;
        this.order = order;
//		gv_person_index.setMaxHeight(contentView.getMeasuredHeightAndState());
		setGridViewHeight(gv_person_index);
        adapter.notifyDataSetChanged();
        super.show();
        App.instance.orderInPayment = order;
        this.canDelete = canDelete;

    }

	private void setGridViewHeight(GridView gridview) {
		// 获取gridview的adapter
		Adapter listAdapter = (Adapter) gridview.getAdapter();
		if (listAdapter == null) {
			return;
		}
		// 固定列宽，有多少列
		int numColumns= 3; //5
		int totalHeight = 0;
		// 计算每一列的高度之和
		for (int i = 0; i < listAdapter.getCount(); i += numColumns) {
			// 获取gridview的每一个item
			View listItem = listAdapter.getView(i, null, gridview);
			listItem.measure(0, 0);
			// 获取item的高度和
			totalHeight += listItem.getMeasuredHeight();
			if(i < listAdapter.getCount() - numColumns){
				totalHeight += gridview.getVerticalSpacing();
			}
		}
		// 获取gridview的布局参数
		ViewGroup.LayoutParams params = gridview.getLayoutParams();
		int gH = (int) (ScreenSizeUtil.height - ScreenSizeUtil.dip2px(context, 100));
		if(totalHeight > gH){
			totalHeight = gH;
		}
		params.height = totalHeight;
		gridview.setLayoutParams(params);
	}

    @Override
    public void dismiss() {
        super.dismiss();
        App.instance.orderInPayment = null;
    }

    private final class Adapter extends BaseAdapter {

        @Override
        public int getCount() {
            return orderSplits.size();
        }

        @Override
        public Object getItem(int arg0) {
            return orderSplits.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            arg1 = inflater.inflate(R.layout.item_person_index, null);
            OrderSplit orderSplit = orderSplits.get(arg0);
            RingTextView rtv_text = (RingTextView) arg1.findViewById(R.id.rtv_text);
            int groupId = orderSplit.getGroupId().intValue();
            arg1.setTag(orderSplit);
            if (orderSplit.getOrderStatus() == ParamConst.ORDER_STATUS_FINISHED) {
                rtv_text.setDoneCircleColor(groupId);
            } else {
				int colorId = ColorUtils.ColorGroup.getColor(groupId);
				if(colorId == 0){
					colorId = R.color.split_group1;
				}
				rtv_text.setCircleColor(context.getResources().getColor(colorId), groupId);
            }
            return arg1;
        }

    }

    @Override
    public void onBackPressed() {
        if (canDelete) {
            OrderSplitSQL.deleteOrderSplitPaxByOrderId(order);
            DialogFactory.showOneButtonCompelDialog(context, context.getString(R.string.warning), context.getString(R.string.cancel_split), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
        super.onBackPressed();
    }
}
