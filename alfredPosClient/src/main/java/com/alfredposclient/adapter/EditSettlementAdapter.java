package com.alfredposclient.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.ParamConst;
import com.alfredbase.PrinterLoadingDialog;
import com.alfredbase.VerifyDialog;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.OrderBill;
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.javabean.OrderSplit;
import com.alfredbase.javabean.Payment;
import com.alfredbase.javabean.PaymentSettlement;
import com.alfredbase.javabean.PrinterTitle;
import com.alfredbase.javabean.RoundAmount;
import com.alfredbase.javabean.javabeanforhtml.EditSettlementInfo;
import com.alfredbase.javabean.model.PrintOrderItem;
import com.alfredbase.javabean.model.PrintOrderModifier;
import com.alfredbase.javabean.model.PrinterDevice;
import com.alfredbase.store.sql.OrderDetailSQL;
import com.alfredbase.store.sql.OrderDetailTaxSQL;
import com.alfredbase.store.sql.OrderSQL;
import com.alfredbase.store.sql.OrderSplitSQL;
import com.alfredbase.store.sql.PaymentSQL;
import com.alfredbase.store.sql.PaymentSettlementSQL;
import com.alfredbase.store.sql.RoundAmountSQL;
import com.alfredbase.store.sql.TableInfoSQL;
import com.alfredbase.utils.BH;
import com.alfredbase.utils.ObjectFactory;
import com.alfredposclient.R;
import com.alfredposclient.global.App;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.alfredposclient.activity.EditSettlementPage.EDIT_ITEM_ACTION;


public class EditSettlementAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private BaseActivity context;
	private List<EditSettlementInfo> editSettlementInfos;
	private int selectorPosition = 0;
	private VerifyDialog verifyDialog;

	public EditSettlementAdapter(BaseActivity context, List<EditSettlementInfo> editSettlementInfos, VerifyDialog verifyDialog) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		if (editSettlementInfos == null)
			editSettlementInfos = Collections.emptyList();
		this.editSettlementInfos = editSettlementInfos;
		this.verifyDialog = verifyDialog;
	}

	public void setEditSettlementInfos(List<EditSettlementInfo> editSettlementInfos){
		if (editSettlementInfos == null)
			this.editSettlementInfos = Collections.emptyList();
		else
			this.editSettlementInfos = editSettlementInfos;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return editSettlementInfos.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return editSettlementInfos.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View currentView, ViewGroup arg2) {

		ViewHolder holder = null;
		if (currentView == null) {
			currentView = inflater.inflate(R.layout.payment_item, null);
			holder = new ViewHolder();
			holder.tv_bill_no = (TextView) currentView.findViewById(R.id.tv_bill_no);
			holder.tv_place_name = (TextView) currentView.findViewById(R.id.tv_place_name);
			holder.tv_table_name = (TextView) currentView.findViewById(R.id.tv_table_name);
			holder.tv_total = (TextView) currentView.findViewById(R.id.tv_total);
			holder.tv_poeple = (TextView) currentView.findViewById(R.id.tv_poeple);
			holder.tv_time = (TextView) currentView.findViewById(R.id.tv_time);
			holder.btn_edit_settlement = (Button) currentView.findViewById(R.id.btn_edit_settlement);
			holder.btn_reprint = (Button) currentView.findViewById(R.id.btn_reprint);
			currentView.setTag(holder);
		} else {
			holder = (ViewHolder) currentView.getTag();
		}
		final EditSettlementInfo editSettlementInfo = editSettlementInfos.get(position);
		String billNo = editSettlementInfo.getBillNo() + "";
		if(editSettlementInfo.getOrderSplitId() > 0){
			billNo = billNo + "(Split:" + editSettlementInfo.getSplitGroupId() + ")";
		}
		holder.tv_bill_no.setText(billNo);
		holder.tv_place_name.setText(editSettlementInfo.getPlaceName());
		holder.tv_table_name.setText(editSettlementInfo.getTableName());
		holder.tv_total.setText(BH.formatMoney(editSettlementInfo.getTotalAmount()).toString());
		holder.tv_poeple.setText(editSettlementInfo.getUserName());
		holder.tv_time.setText(editSettlementInfo.getPaymentCreateTime());
		if(App.instance.isRevenueKiosk()){
			holder.tv_place_name.setVisibility(View.GONE);
			holder.tv_table_name.setVisibility(View.GONE);
		}

		holder.btn_edit_settlement.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				verifyDialog.show(EDIT_ITEM_ACTION, editSettlementInfo);
			}
		});

		holder.btn_reprint.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				printBillAction(editSettlementInfo);
			}
		});

		return currentView;

	}

	class ViewHolder {
		public TextView tv_bill_no;
		public TextView tv_place_name;
		public TextView tv_table_name;
		public TextView tv_total;
		public TextView tv_poeple;
		public TextView tv_time;
		public Button btn_edit_settlement;
		public Button btn_reprint;
	}

	private void printBillAction(EditSettlementInfo editSettlementInfo) {
		PrinterLoadingDialog printerLoadingDialog = new PrinterLoadingDialog(context);
		printerLoadingDialog.setTitle(context.getResources().getString(R.string.bill_printing));
		printerLoadingDialog.showByTime(3000);
		PrinterDevice printer = App.instance.getCahierPrinter();
		if (editSettlementInfo.getOrderSplitId() > 0) {
			OrderSplit orderSplit = OrderSplitSQL.get(editSettlementInfo.getOrderSplitId());
			Order order = OrderSQL.getOrder(orderSplit.getOrderId().intValue());
			OrderBill orderBill = ObjectFactory.getInstance()
					.getOrderBillByOrderSplit(orderSplit,
							App.instance.getRevenueCenter());
			if (orderSplit.getSplitByPax() > 0) {
				ArrayList<OrderDetail> orderDetails = OrderDetailSQL.getOrderDetails(order
						.getId());
				if (orderDetails.isEmpty()) {
					return;
				}
				PrinterTitle title = ObjectFactory.getInstance()
						.getPrinterTitleByOrderSplit(
								App.instance.getRevenueCenter(),
								order,
								orderSplit,
								App.instance.getUser().getFirstName()
										+ App.instance.getUser().getLastName(),
								TableInfoSQL.getTableById(orderSplit.getTableId())
										.getName(), orderBill, order.getBusinessDate().toString(), 2);
                title.setSpliteByPax(orderSplit.getSplitByPax());
                ArrayList<OrderDetail> orderSplitDetails = OrderDetailSQL.getOrderDetails(order.getId());
				ArrayList<PrintOrderItem> orderItems = ObjectFactory.getInstance().getItemList(orderSplitDetails);
				List<Map<String, String>> taxMap = OrderDetailTaxSQL.getTaxPriceSUMForPrint(App.instance.getLocalRestaurantConfig().getIncludedTax().getTax(), order);
				ArrayList<PrintOrderModifier> orderModifiers = ObjectFactory.getInstance().getItemModifierList(order, orderSplitDetails);
				Order temporaryOrder = new Order();
				temporaryOrder.setId(orderSplit.getOrderId());
				temporaryOrder.setPersons(orderSplit.getPersons());
				temporaryOrder.setSubTotal(orderSplit.getSubTotal());
				temporaryOrder.setDiscountAmount(order.getDiscountAmount());
				temporaryOrder.setTotal(order.getTotal());
				temporaryOrder.setTaxAmount(orderSplit.getTaxAmount());
				temporaryOrder.setOrderNo(order.getOrderNo());
				temporaryOrder.setGrandTotal(orderSplit.getTotal());
				RoundAmount roundAmount = RoundAmountSQL.getRoundAmountByOrderSplitAndBill(orderSplit, orderBill);
				Payment payment = PaymentSQL.getPaymentByOrderSplitId(orderSplit.getId().intValue());
				List<PaymentSettlement> paymentSettlements = PaymentSettlementSQL.getAllPaymentSettlementByPaymentId(payment.getId().intValue());
				App.instance.remoteBillRePrint(printer, title, temporaryOrder,
						orderItems, orderModifiers, taxMap, paymentSettlements,
						roundAmount, false);
			} else {
				ArrayList<OrderDetail> orderDetails = (ArrayList<OrderDetail>) OrderDetailSQL
						.getOrderDetailsByOrderAndOrderSplit(orderSplit);
				if (orderDetails.isEmpty()) {
					return;
				}
				List<Map<String, String>> taxMap = OrderDetailTaxSQL
						.getOrderSplitTaxPriceSUMForPrint(App.instance
								.getLocalRestaurantConfig().getIncludedTax()
								.getTax(), orderSplit);
				ArrayList<PrintOrderItem> orderItems = ObjectFactory
						.getInstance().getItemList(orderDetails);

				PrinterTitle title = ObjectFactory.getInstance()
						.getPrinterTitleByOrderSplit(
								App.instance.getRevenueCenter(),
								order,
								orderSplit,
								App.instance.getUser().getFirstName()
										+ App.instance.getUser().getLastName(),
								TableInfoSQL.getTableById(orderSplit.getTableId())
										.getName(), orderBill, order.getBusinessDate().toString(), 2);
				ArrayList<PrintOrderModifier> orderModifiers = ObjectFactory
						.getInstance().getItemModifierListByOrderDetail(
								orderDetails);
				Order temporaryOrder = new Order();
				temporaryOrder.setId(orderSplit.getOrderId());
				temporaryOrder.setPersons(orderSplit.getPersons());
				temporaryOrder.setSubTotal(orderSplit.getSubTotal());
				temporaryOrder.setDiscountAmount(orderSplit.getDiscountAmount());
				temporaryOrder.setTotal(orderSplit.getTotal());
				temporaryOrder.setTaxAmount(orderSplit.getTaxAmount());
				temporaryOrder.setOrderNo(order.getOrderNo());
				RoundAmount roundAmount = RoundAmountSQL.getRoundAmountByOrderSplitAndBill(orderSplit, orderBill);
				Payment payment = PaymentSQL.getPaymentByOrderSplitId(orderSplit.getId().intValue());
				List<PaymentSettlement> paymentSettlements = PaymentSettlementSQL.getAllPaymentSettlementByPaymentId(payment.getId().intValue());
				App.instance.remoteBillRePrint(printer, title, temporaryOrder,
						orderItems, orderModifiers, taxMap, paymentSettlements,
						roundAmount, false);
			}


		} else {
			Order order = OrderSQL.getOrder(editSettlementInfo.getOrderId());
			if (order == null) {
				return;
			}
			int orderDetailCount = OrderDetailSQL.getOrderDetailCountByGroupId(
					ParamConst.ORDERDETAIL_DEFAULT_GROUP_ID, order.getId());
			ArrayList<PrintOrderModifier> orderModifiers = ObjectFactory
					.getInstance().getItemModifierList(order, OrderDetailSQL.getOrderDetails(order.getId()));
			OrderBill orderBill = ObjectFactory.getInstance().getOrderBill(
					order, App.instance.getRevenueCenter());
			RoundAmount roundAmount = RoundAmountSQL
					.getRoundAmountByOrderAndBill(order, orderBill);
			List<PaymentSettlement> paymentSettlements = PaymentSettlementSQL.getAllPaymentSettlementByPaymentId(editSettlementInfo.getPaymentId());
			App.instance.remoteBillRePrint(
					printer,
					ObjectFactory.getInstance().getPrinterTitle(
							App.instance.getRevenueCenter(),
							order,
							App.instance.getUser().getFirstName()
									+ App.instance.getUser().getLastName(),
							TableInfoSQL.getTableById(order.getTableId())
									.getName(), 2,App.instance.getSystemSettings().getTrainType()),
					order,
					ObjectFactory.getInstance().getItemList(
							OrderDetailSQL.getOrderDetails(order.getId())),
					orderModifiers, OrderDetailTaxSQL.getTaxPriceSUMForPrint(
							App.instance.getLocalRestaurantConfig()
									.getIncludedTax().getTax(), order), paymentSettlements,
					roundAmount, false);
		}
	}

}
