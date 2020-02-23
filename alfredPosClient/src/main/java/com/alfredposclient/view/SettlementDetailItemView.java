package com.alfredposclient.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alfredbase.ParamConst;
import com.alfredbase.global.CoreData;
import com.alfredbase.javabean.PaymentMethod;
import com.alfredbase.javabean.PaymentSettlement;
import com.alfredbase.utils.BH;
import com.alfredbase.utils.TextTypeFace;
import com.alfredposclient.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SettlementDetailItemView extends LinearLayout implements OnClickListener {
//	private static final int ROW_COUNT = 4;
	public static final int ITEM_SIZE = 100;
	public static final int MARGIN_SIZE = 10;
	public ImageView iv_settlement_icon;
	public TextView tv_settlement_num;
	public TextView tv_settlement_delicon;
	private TextTypeFace textTypeFace;
	private DisplayImageOptions options;

	public SettlementDetailItemView(Context context) {
		super(context);
		init(context);
	}

	public SettlementDetailItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	private void init(Context context) {
		View.inflate(context, R.layout.settlement_type_item, this);
		iv_settlement_icon = (ImageView) findViewById(R.id.iv_settlement_icon);
		tv_settlement_num = (TextView) findViewById(R.id.tv_settlement_num);
		tv_settlement_delicon = (TextView) findViewById(R.id.tv_settlement_delicon);
		initTextTypeFace();
	}

	
	public void setParams(final PaymentSettlement paymentSettlement, final ViewResultCall viewResultCall) {
		tv_settlement_num.setText(BH.formatMoney(paymentSettlement.getPaidAmount()).toString());
		int paymentTypeId = paymentSettlement.getPaymentTypeId()
				.intValue();
		PaymentMethod paymentMethod = CoreData.getInstance().getPamentMethodByPaymentTypeId(paymentSettlement.getId().intValue());
		if(paymentMethod != null && !TextUtils.isEmpty(paymentMethod.getLogoSm())){
			showImage(paymentMethod.getLogoSm());
		} else {
			iv_settlement_icon
					.setImageResource(getImageResourceBySettlementType(paymentSettlement
							.getPaymentTypeId().intValue()));
	       }

		if(paymentTypeId == ParamConst.SETTLEMENT_TYPE_PAYPAL){
			tv_settlement_delicon.setVisibility(View.INVISIBLE);
			tv_settlement_delicon.setOnClickListener(null);
		}else{
			tv_settlement_delicon.setVisibility(View.VISIBLE);
			tv_settlement_delicon.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					viewResultCall.call(paymentSettlement);
				}
			});
		}

	}
	
	private int getImageResourceBySettlementType(int paymentTypeId) {
		switch (paymentTypeId) {
		case ParamConst.SETTLEMENT_TYPE_PAYPAL:
			return R.drawable.icon_diner;
		case ParamConst.SETTLEMENT_TYPE_CASH:
			return R.drawable.icon_settle_cash;
		case ParamConst.SETTLEMENT_TYPE_STORED_CARD:
			return R.drawable.icon_settle_cash;

		case ParamConst.SETTLEMENT_TYPE_MASTERCARD:
			return R.drawable.icon_master;
		case ParamConst.SETTLEMENT_TYPE_UNIPAY:
			return R.drawable.icon_upay;
		case ParamConst.SETTLEMENT_TYPE_VISA:
			return R.drawable.icon_visa;
		case ParamConst.SETTLEMENT_TYPE_NETS:
			return R.drawable.icon_nets;
		case ParamConst.SETTLEMENT_TYPE_AMEX:
			return R.drawable.icon_amer;
		case ParamConst.SETTLEMENT_TYPE_JCB:
			return R.drawable.icon_jcb;
		case ParamConst.SETTLEMENT_TYPE_DINNER_INTERMATIONAL:
			return R.drawable.icon_d;
		case ParamConst.SETTLEMENT_TYPE_BILL_ON_HOLD:
			return R.drawable.settle_boh;
		case ParamConst.SETTLEMENT_TYPE_COMPANY:
			return R.drawable.settle_com;
		case ParamConst.SETTLEMENT_TYPE_HOURS_CHARGE:
			return R.drawable.settle_hc;
		case ParamConst.SETTLEMENT_TYPE_VOID:
		case ParamConst.SETTLEMENT_TYPE_REFUND:
			return R.drawable.settle_void;
		case ParamConst.SETTLEMENT_TYPE_ENTERTAINMENT:
			return R.drawable.settle_ent;
		case ParamConst.SETTLEMENT_TYPE_ALIPAY:
			return R.drawable.icon_alipay;
		case ParamConst.SETTLEMENT_TYPE_EZLINK:
			return R.drawable.icon_wxpay;		
		default:
			return R.drawable.icon_settle_cash;
		}
	}

	@Override
	public void onClick(View v) {

	}
	
	private void initTextTypeFace(){
		textTypeFace = TextTypeFace.getInstance();
		textTypeFace.setTrajanProBlod(tv_settlement_num);
	}

	public void showImage(String url) {
		options = new DisplayImageOptions.Builder()
				.showImageOnFail(R.drawable.icon_settle_cash)
				.showImageForEmptyUri(R.drawable.icon_settle_cash)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();
		ImageLoader.getInstance().displayImage(url	, iv_settlement_icon, options);
	}

	public interface ViewResultCall {
		public void call(PaymentSettlement paymentSettlement);
	}
}
