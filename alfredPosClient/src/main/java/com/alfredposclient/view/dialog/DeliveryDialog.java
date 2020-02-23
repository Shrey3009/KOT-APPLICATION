package com.alfredposclient.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alfredbase.VerifyDialog;
import com.alfredbase.javabean.temporaryforapp.AppOrder;
import com.alfredbase.utils.TextTypeFace;
import com.alfredposclient.R;
import com.alfredposclient.adapter.DeliveryApdater;

import java.util.ArrayList;
import java.util.List;

public class DeliveryDialog extends Dialog {

    private Handler handler;
    private Context context;
    private ListView listView;
    private ListView listview;
    VerifyDialog verifyDialog;
    List<AppOrder> plist;


    private TextView title;

    private PaymentClickListener paymentClickListener;

    DeliveryApdater adapter;
    private Button print;
    TextTypeFace textTypeFace = TextTypeFace.getInstance();

    public DeliveryDialog(Context context, List<AppOrder> deliverylist) {
        super(context, com.alfredbase.R.style.Dialog_verify);

        this.context = context;
        plist = deliverylist;
        init();
    }

    private void init() {
        final AlertDialog dlg = new AlertDialog.Builder(context).create();
        dlg.show();
        dlg.setCancelable(true);
        dlg.setCanceledOnTouchOutside(true);

        Window window = dlg.getWindow();
        window.setContentView(R.layout.dialog_delivery_layout);
        window.setBackgroundDrawableResource(android.R.color.transparent);

        listview = (ListView) window.findViewById(R.id.
                lv_delivery);

//		title=(TextView) window.findViewById(R.id.tv_pay_title);
        print = (Button) window.findViewById(R.id.btn_de_print);

//		textTypeFace.setTrajanProBlod((TextView) window
//				.findViewById(R.id.tv_pay_title));
        adapter = new DeliveryApdater(context, (ArrayList<AppOrder>) plist,
                new DeliveryApdater.OnSelectedItemChanged() {


                    public void getSelectedCount(int count) {
                        // tv_count.setText("总计：    " + count + " 次");
                    }


                    public void getSelectedItem(AppOrder appOrder) {
//                        Toast.makeText(context, appOrder.getOrderNo(),
//                                Toast.LENGTH_SHORT).show();
                    }
                });

        listview.setAdapter(adapter);
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plist = adapter.currentSelect();

                if (plist.size() > 0) {
                    if (paymentClickListener != null)
                        paymentClickListener.onPaymentClick(plist);
                    dlg.cancel();
                } else {
                    Toast.makeText(context, context.getString(R.string.please_select_order),
                            Toast.LENGTH_SHORT).show();
                }
//

            }
        });

//		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////				handler.sendMessage(handler
////						.obtainMessage(MainPage.VIEW_EVENT_SHOW_BILL_ON_HOLD));
//				PamentMethodAdapter.ViewHolder holder = (PamentMethodAdapter.ViewHolder) view.getTag();
//
//
//
//				if(plist.get(position).getIsverify()>=0)
//				{
//
//					if (paymentClickListener != null)
//						paymentClickListener.onPaymentClick(plist.get(position));
////					verifyDialog = new VerifyDialog(context, handler);
//////					verifyDialog.show("111",null);
//
//				//	Toast.makeText()
//				}
//				//  pays = (String) list.get(position).get("id");
//				System.out.println("======y优惠券fangs======" + position);
//
//				dlg.cancel();
//			}
//		});
    }

    public void setPaymentClickListener(
            DeliveryDialog.PaymentClickListener otherClickListener) {
        this.paymentClickListener = otherClickListener;
    }

    public interface PaymentClickListener {
        void onPaymentClick(List<AppOrder> order);
    }
}
