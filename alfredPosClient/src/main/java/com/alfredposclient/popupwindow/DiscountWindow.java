package com.alfredposclient.popupwindow;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.ParamConst;
import com.alfredbase.global.BugseeHelper;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.store.sql.OrderDetailSQL;
import com.alfredbase.utils.AnimatorListenerImpl;
import com.alfredbase.utils.BH;
import com.alfredbase.utils.ButtonClickTimer;
import com.alfredbase.utils.ScreenSizeUtil;
import com.alfredbase.utils.TextTypeFace;
import com.alfredposclient.R;
import com.alfredposclient.adapter.DiscountAdapter;
import com.alfredposclient.global.App;
import com.alfredposclient.view.DiscountMoneyKeyboard;
import com.alfredposclient.view.DiscountMoneyKeyboard.KeyBoardClickListener;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class DiscountWindow implements OnClickListener, KeyBoardClickListener {
	private static final int DURATION_1 = 300;
	private static final int DURATION_2 = 500;

	private ResultCall resultCall;

	private BaseActivity parent;
	private View parentView;

	private View contentView;
	private PopupWindow popupWindow;
	private RelativeLayout rl_discount;
	private RelativeLayout rl_discount_panel;
	private DiscountMoneyKeyboard discountKeyboard;
	private TextView tv_discount_percent;
	private TextView tv_percent_sign;
	private TextView tv_count_sign;
	private TextView tv_discount_count;
	private TextView inputView;
	private ListView discount_listview;
	private TextView discount_tv;

	private StringBuffer keyBuffer;
	private Order order;
	private OrderDetail orderDetail;
	private String sumRealPrice = "0.00";
//	private List<ItemMainCategory> categories = new ArrayList<ItemMainCategory>();

	private DiscountAdapter discountAdapter;
	String discountByCategory = "";
	public DiscountWindow(BaseActivity parent, View parentView) {
		this.parent = parent;
		this.parentView = parentView;
	}

	private void init()
	{
		contentView = LayoutInflater.from(parent).inflate(
				R.layout.popup_discount, null);

		rl_discount = (RelativeLayout) contentView
				.findViewById(R.id.rl_discount);
		tv_discount_percent = (TextView) contentView
				.findViewById(R.id.tv_discount_percent);
		tv_discount_percent.setOnClickListener(this);
		tv_discount_count = (TextView) contentView
				.findViewById(R.id.tv_discount_count);
		tv_discount_count.setOnClickListener(this);
		tv_discount_count.setText(BH.formatMoney("").toString());
		tv_percent_sign = (TextView) contentView.findViewById(R.id.tv_percent_sign);
		tv_count_sign = (TextView) contentView.findViewById(R.id.tv_count_sign);
		discount_listview = (ListView) contentView.findViewById(R.id.discount_listview);
		discount_tv = (TextView) contentView.findViewById(R.id.discount_tv);


		inputView = tv_discount_percent;
		if (order != null)
		{
			contentView.findViewById(R.id.ll_discount_layout).setVisibility(View.VISIBLE);
			discountAdapter = new DiscountAdapter(parent, order);
			discount_listview.setAdapter(discountAdapter);
			if(order.getDiscountType() == ParamConst.ORDER_DISCOUNT_TYPE_RATE_BY_CATEGORY)
			{
				tv_discount_percent.setText(BH.intFormat.format(BH.mul(
						BH.getBD(order.getDiscountRate()),
						BH.getBD(100), true)));
				tv_discount_count.setText(BH.formatMoney(""));
				inputView = tv_discount_percent;
				tv_count_sign.setBackgroundResource(R.color.white);
				tv_percent_sign.setBackgroundResource(R.color.brown);
				tv_percent_sign.setTextColor(parent.getResources().getColor(R.color.white));
			}
			else if(order.getDiscountType() == ParamConst.ORDER_DISCOUNT_TYPE_SUB_BY_CATEGORY)
			{
				tv_discount_percent.setText(ParamConst.INT_ZERO);
				tv_discount_count.setText(BH.formatMoney(order.getDiscountPrice()));
				inputView = tv_discount_count;
				tv_count_sign.setBackgroundResource(R.color.brown);
				tv_count_sign.setTextColor(parent.getResources().getColor(R.color.white));
				tv_percent_sign.setBackgroundResource(R.color.white);
			}
			else
			{
				sumRealPrice = OrderDetailSQL.getOrderDetailRealPriceWhenDiscountBySelf(order);
				if (order.getDiscountType() == ParamConst.ORDER_DISCOUNT_TYPE_NULL)
				{
					tv_discount_percent.setText(ParamConst.INT_ZERO);
					tv_discount_count.setText(BH.formatMoney("").toString());
				}
				else if (order.getDiscountType() == ParamConst.ORDER_DISCOUNT_TYPE_RATE_BY_ORDER)
				{
					tv_discount_percent
							.setText(BH.intFormat.format(BH.mul(
									BH.getBD(order.getDiscountRate()),
									BH.getBD(100), true)));

					tv_discount_count.setText(BH.formatMoney(BH.mul(
							BH.getBD(order.getDiscountRate()),
							BH.sub(BH.getBD(order.getSubTotal()), BH.getBD(sumRealPrice), false), true).toString()));
				}
				else
				{
					if (BH.compare(BH.getBD(order.getSubTotal()), BH.getBD(sumRealPrice)))
					{
						tv_discount_percent
								.setText(BH.intFormat.format(BH.mul(
										BH.div(BH.getBD(order.getDiscountPrice()), BH.sub(BH.getBD(order.getSubTotal()),
												BH.getBD(sumRealPrice), false), false),
										BH.getBD(100), true)));
					}
					else
					{
						tv_discount_percent.setText(ParamConst.INT_ZERO);
					}
					tv_discount_count.setText(BH.getBD(order.getDiscountPrice()).toString());
				}
			}
		}
		else
		{
			contentView.findViewById(R.id.ll_discount_layout).setVisibility(View.GONE);
			if (orderDetail.getDiscountType() == ParamConst.ORDERDETAIL_DISCOUNT_TYPE_RATE) {
				tv_discount_percent.setText(BH.intFormat.format(BH.mul(
						BH.getBD(orderDetail.getDiscountRate()), BH.getBD(100),
						true)));
				tv_discount_count.setText(BH.mul(
						BH.getBD(orderDetail.getRealPrice()),
						BH.getBD(orderDetail.getDiscountRate()), true)
						.toString());
			}
			else if (orderDetail.getDiscountPrice() != null)
			{
				tv_discount_percent.setText(BH.intFormat.format(BH.mul(
						BH.div(BH.getBD(orderDetail.getDiscountPrice()),
								BH.getBD(orderDetail.getRealPrice()), false),
						BH.getBD(100), true)));
				tv_discount_count.setText(BH.getBD(orderDetail.getDiscountPrice()).toString());
			}
			else
			{
				tv_discount_percent.setText(ParamConst.INT_ZERO);
				tv_discount_count.setText(ParamConst.DOUBLE_ZERO);
			}
		}
//		tv_discount_percent.setBackgroundColor(Color.parseColor("#FF0000"));
//		tv_discount_count.setBackgroundColor(Color.parseColor("#E9E9E9"));
		rl_discount_panel = (RelativeLayout) contentView
				.findViewById(R.id.rl_discount_panel);
		initTextTypeFace(contentView);

		discountKeyboard = (DiscountMoneyKeyboard) contentView
				.findViewById(R.id.discountKeyboard);
		discountKeyboard.setKeyBoardClickListener(this);
		popupWindow = new PopupWindow(parentView,
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);

		if(!App.instance.getLocalRestaurantConfig().getCurrencySymbol().equals("Rp"))
		{
			String textToSet = String.valueOf(new BigDecimal(String.valueOf(tv_discount_count.getText())).setScale(2, BigDecimal.ROUND_HALF_UP));
			tv_discount_count.setText(textToSet);
		}
		else
		{
			String convertedCount = String.valueOf(tv_discount_count.getText());
			tv_discount_count.setText(String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(new Double(convertedCount.replace(",", "")).intValue())));
		}

		popupWindow.setContentView(contentView);
		popupWindow.setFocusable(true);
	}

	private void initTextTypeFace(View view) {
		TextTypeFace textTypeFace = TextTypeFace.getInstance();
		textTypeFace.setTrajanProBlod((TextView)view.findViewById(R.id.tv_discount_tips));
		textTypeFace.setTrajanProRegular(tv_discount_percent);
		textTypeFace.setTrajanProRegular(tv_discount_count);
		textTypeFace.setTrajanProRegular(tv_count_sign);
		textTypeFace.setTrajanProRegular(tv_percent_sign);
		textTypeFace.setTrajanProRegular(discount_tv);
	}

	/**
	 * order不为空的时候，针对于整个订单打折，orderDetail不为空的时候，针对于每个菜打折
	 *
	 * 两个只能有一个为null，而且必须有一个为null
	 *
	 * @param order
	 * @param orderDetail
	 * @param resultCall
	 */
	public void show(Order order, OrderDetail orderDetail, ResultCall resultCall) {
		if(isShowing()){
			return;
		}
		this.order = order;
		this.orderDetail = orderDetail;
		this.resultCall = resultCall;
		this.discountByCategory = "";
		keyBuffer = new StringBuffer();
		init();
		popupWindow
				.showAtLocation(parentView, Gravity.LEFT | Gravity.TOP, 0, 0);
		rl_discount_panel.post(new Runnable() {
			@Override
			public void run() {
				AnimatorSet set = new AnimatorSet();
				ObjectAnimator animator1 = ObjectAnimator.ofFloat(
						rl_discount_panel,
						"y",
						rl_discount_panel.getY()
								+ rl_discount_panel.getHeight(),
						rl_discount_panel.getY()).setDuration(DURATION_2);

				ObjectAnimator animator2 = ObjectAnimator.ofFloat(
						rl_discount,
						"y",
						rl_discount.getY() + rl_discount.getHeight(),
						rl_discount.getY()
								+ ScreenSizeUtil.getStatusBarHeight(parent))
						.setDuration(DURATION_1);
				set.playTogether(animator1, animator2);
				set.setInterpolator(new DecelerateInterpolator());
				set.start();
			}
		});
	}

	public void dismiss() {
		if (popupWindow != null && popupWindow.isShowing()) {
			AnimatorSet set = new AnimatorSet();
			ObjectAnimator animator1 = ObjectAnimator.ofFloat(
					rl_discount_panel, "y", rl_discount_panel.getY(),
					rl_discount_panel.getY() + rl_discount_panel.getHeight())
					.setDuration(DURATION_2);

			ObjectAnimator animator2 = ObjectAnimator.ofFloat(
					rl_discount,
					"y",
					rl_discount.getY()
							+ ScreenSizeUtil.getStatusBarHeight(parent),
					rl_discount.getY() + rl_discount.getHeight()).setDuration(
					DURATION_1);
			set.playTogether(animator1, animator2);
			set.setInterpolator(new DecelerateInterpolator());
			set.addListener(new AnimatorListenerImpl() {
				public void onAnimationEnd(Animator animation) {
					super.onAnimationEnd(animation);
					popupWindow.dismiss();
				};
			});
			set.start();
		}
	}

	public boolean isShowing() {
		if (popupWindow != null) {
			return popupWindow.isShowing();
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		if (ButtonClickTimer.canClick()) {
			switch (v.getId()) {
			case R.id.tv_discount_percent: {
				if (inputView == tv_discount_percent) {
					return;
				} else {
					keyBuffer = new StringBuffer();
				}
				inputView = tv_discount_percent;
				tv_count_sign.setBackgroundResource(R.color.white);
				tv_percent_sign.setBackgroundResource(R.color.brown);
				tv_percent_sign.setTextColor(parent.getResources().getColor(R.color.white));
				break;
			}
			case R.id.tv_discount_count: {
				if (inputView == tv_discount_count) {
					return;
				} else {
					keyBuffer = new StringBuffer();
				}
				inputView = tv_discount_count;
				tv_count_sign.setBackgroundResource(R.color.brown);
				tv_count_sign.setTextColor(parent.getResources().getColor(R.color.white));
				tv_percent_sign.setBackgroundResource(R.color.white);
				break;
			}
			default:
				break;
			}
			if(!App.instance.getLocalRestaurantConfig().getCurrencySymbol().equals("Rp"))
			{
				String textToSet = String.valueOf(new BigDecimal(String.valueOf(tv_discount_count.getText())).setScale(2, BigDecimal.ROUND_HALF_UP));
				tv_discount_count.setText(textToSet);
			}
		}
	}

	private void setDiscountKey(String key){
		int percent = Integer.parseInt(key);
		inputView = tv_discount_percent;
		tv_count_sign.setBackgroundResource(R.color.white);
		tv_percent_sign.setBackgroundResource(R.color.brown);
		tv_percent_sign.setTextColor(parent.getResources().getColor(R.color.white));
		if (order != null) {
			if(TextUtils.isEmpty(discountByCategory))
			{
				tv_discount_count.setText(BH.mul(
						BH.sub(BH.getBD(order.getSubTotal()),
								BH.getBD(sumRealPrice), false),
						BH.div(BH.getBD(percent), BH.getBD(100), false),
						true).toString());
			}
		} else {
			tv_discount_count.setText(BH.mul(
					BH.getBD(orderDetail.getRealPrice()),
					BH.div(BH.getBD(percent), BH.getBD(100), false),
					true).toString());
		}
		if(!App.instance.getLocalRestaurantConfig().getCurrencySymbol().equals("Rp"))
		{
			String textToSet = String.valueOf(new BigDecimal(String.valueOf(tv_discount_count.getText())).setScale(2, BigDecimal.ROUND_HALF_UP));
			tv_discount_count.setText(textToSet);
		}
	}

	@Override
	public void onKeyBoardClick(String key)
	{
		BugseeHelper.buttonClicked(key);
		if ("X".equals(key)) {
			dismiss();
			if (resultCall != null)
				resultCall.call(null, null, "");
		} else if ("Enter".equals(key)) {
			dismiss();
			if (resultCall != null)
			{
				if (inputView == tv_discount_percent)
				{
					if(order != null)
					{
						discountByCategory = discountAdapter == null ? "" : discountAdapter.getSelectedItem(ParamConst.ORDERDETAIL_DISCOUNT_BYCATEGORY_TYPE_RATE);
					}

					resultCall.call(BH.div(BH.getBD(tv_discount_percent.getText()
									.toString()), BH.getBD(100), true)
									.toString(), null, discountByCategory);
				}
				else
				{
					if(order != null)
					{
						discountByCategory = discountAdapter == null ? "" : discountAdapter.getSelectedItem(ParamConst.ORDERDETAIL_DISCOUNT_BYCATEGORY_TYPE_SUB);
					}
					if(App.instance.getLocalRestaurantConfig().getCurrencySymbol().equals("Rp"))
					{
						String convertedValue = tv_discount_count.getText().toString().replaceAll(",", "");
						resultCall.call(null, convertedValue, discountByCategory);
					}
					else
					{
						resultCall.call(null, tv_discount_count.getText().toString(), discountByCategory);
					}
				}
			}
		} else if ("C".equals(key)) {
			keyBuffer = new StringBuffer();
			tv_discount_percent.setText("0");
			tv_discount_count.setText("0.00");
			if(App.instance.getLocalRestaurantConfig().getCurrencySymbol().equals("Rp"))
			{
				tv_discount_count.setText("0");
			}
		} else if (key.endsWith("%")) {
			String num = key.substring(0, key.length()-1);
			tv_discount_percent.setText(num);
			setDiscountKey(num);
		} else {
			if(order != null){
				discountByCategory = discountAdapter == null ? "" : discountAdapter.getSelectedItem(ParamConst.ORDERDETAIL_DISCOUNT_BYCATEGORY_TYPE_RATE);
			}

			if (inputView == tv_discount_percent) {
				String temp = keyBuffer.toString() + key;
				int percent = Integer.parseInt(temp);
				if (percent > 100)
					return;
				tv_discount_percent.setText(percent + "");
				keyBuffer.append(key);
				if (order != null)
				{
					if(TextUtils.isEmpty(discountByCategory))
					{
					tv_discount_count.setText(BH.mul(
							BH.sub(BH.getBD(order.getSubTotal()),
							BH.getBD(sumRealPrice), false),
							BH.div(BH.getBD(percent), BH.getBD(100), false),
							true).toString());
					}
				}
				else
				{
					tv_discount_count.setText(
							BH.mul(BH.getBD(orderDetail.getRealPrice()),
                                    BH.div(BH.getBD(percent), BH.getBD(100),false),
                                    true
                            )
							.toString());

				}
			} else {
				double count = BH.div(BH.getBD(keyBuffer.toString() + key), BH.getBD(100), true).doubleValue();
				if(App.instance.getLocalRestaurantConfig().getCurrencySymbol().equals("Rp"))
				{
					count = count * 100;
				}
				if (order != null) {
					if (count > Double.parseDouble(BH.sub(BH.getBD(order.getSubTotal()),
							BH.getBD(sumRealPrice), false).toString()))
						return;
				} else {
					if (count > Double.parseDouble(orderDetail.getRealPrice()))
						return;
				}
				tv_discount_count.setText(BH.getBD(count).toString());
				keyBuffer.append(key);

				if (order != null) {
					if(TextUtils.isEmpty(discountByCategory))
					tv_discount_percent.setText(BH.intFormat.format(BH.mul(
							BH.div(BH.getBD(count + ""),
									BH.sub(BH.getBD(order.getSubTotal()),
									BH.getBD(sumRealPrice), false), true),
							BH.getBD(100), true)));
				} else {
					tv_discount_percent
							.setText(BH.intFormat.format(BH.mul(
									BH.div(BH.getBD(count + ""), BH
											.getBD(orderDetail.getRealPrice()),
											true), BH.getBD(100), true)));
				}
			}
		}
		if(!App.instance.getLocalRestaurantConfig().getCurrencySymbol().equals("Rp"))
		{
			String textToSet = String.valueOf(new BigDecimal(String.valueOf(tv_discount_count.getText())).setScale(2, BigDecimal.ROUND_HALF_UP));
			tv_discount_count.setText(textToSet);
		}
		else
		{
			String convertedCount = String.valueOf(tv_discount_count.getText());
			tv_discount_count.setText(String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(new Double(convertedCount.replace(",", "")).intValue())));
		}
	}

	public interface ResultCall {
		public void call(String discount_rate, String discount_price, String discountByCategory);
	}
}
