package com.alfredposclient.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alfredbase.VerifyDialog;
import com.alfredbase.javabean.PaymentMethod;
import com.alfredbase.utils.TextTypeFace;
import com.alfredposclient.R;
import com.alfredposclient.adapter.PamentMethodAdapter;

import java.util.List;

public class MediaDialog extends Dialog {

    private Handler handler;
    private Context context;
    private ListView listView;
    private ListView listview;
    VerifyDialog verifyDialog;
    List<PaymentMethod> plist;

    PamentMethodAdapter adapters;

    private TextView title;

    private PaymentClickListener paymentClickListener;


    TextTypeFace textTypeFace = TextTypeFace.getInstance();

    public MediaDialog(Context context, Handler handler, List<PaymentMethod> pamentMethodslist) {
        super(context, com.alfredbase.R.style.Dialog_verify);
        this.handler = handler;
        this.context = context;
        plist = pamentMethodslist;
        init();
    }

    private void init() {
        final AlertDialog dlg = new AlertDialog.Builder(context).create();
        dlg.show();
        dlg.setCancelable(true);
        dlg.setCanceledOnTouchOutside(false);

        Window window = dlg.getWindow();
        window.setContentView(R.layout.dialog_meadia_layout);
        window.setBackgroundDrawableResource(android.R.color.transparent);

        listview = (ListView) window.findViewById(R.id.
                lv_media);

        title = (TextView) window.findViewById(R.id.tv_pay_title);


        textTypeFace.setTrajanProBlod((TextView) window
                .findViewById(R.id.tv_pay_title));
        adapters = new PamentMethodAdapter(context, plist);

        listview.setAdapter(adapters);


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				handler.sendMessage(handler
//						.obtainMessage(MainPage.VIEW_EVENT_SHOW_BILL_ON_HOLD));
                PamentMethodAdapter.ViewHolder holder = (PamentMethodAdapter.ViewHolder) view.getTag();

                if (plist.get(position).getIsverify() >= 0) {

                    if (paymentClickListener != null)
                        paymentClickListener.onPaymentClick(plist.get(position));
//					verifyDialog = new VerifyDialog(context, handler);
////					verifyDialog.show("111",null);

                    //	Toast.makeText()
                }
                //  pays = (String) list.get(position).get("id");
                System.out.println("======y优惠券fangs======" + position);

                dlg.cancel();
            }
        });
    }

    public void setPaymentClickListener(
            MediaDialog.PaymentClickListener otherClickListener) {
        this.paymentClickListener = otherClickListener;
    }

    public interface PaymentClickListener {
        void onPaymentClick(PaymentMethod pa);
    }
}
