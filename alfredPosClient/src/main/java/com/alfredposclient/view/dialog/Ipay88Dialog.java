package com.alfredposclient.view.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Window;

import com.alfredbase.ParamConst;
import com.alfredbase.global.CoreData;
import com.alfredbase.javabean.PaymentMethod;
import com.alfredbase.javabean.SettlementRestaurant;
import com.alfredbase.utils.TextTypeFace;
import com.alfredposclient.R;
import com.alfredposclient.adapter.Ipay88SettlementAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class Ipay88Dialog extends Dialog {

    private Context context;
    private AlertDialog dlg;
    private Ipay88SettlementAdapter.ClickListener clickListener;
    private ArrayList<PaymentMethod> paymentSettleRestaurant = new ArrayList<>();

    TextTypeFace textTypeFace = TextTypeFace.getInstance();

    public Ipay88Dialog(Context context, Ipay88SettlementAdapter.ClickListener clickListener) {
        super(context, com.alfredbase.R.style.Dialog_verify);
        this.context = context;
        this.clickListener = clickListener;
        init();
    }

    private void init() {
        dlg = new AlertDialog.Builder(context).create();
        dlg.show();
        dlg.setCancelable(true);
        dlg.setCanceledOnTouchOutside(false);

        Window window = dlg.getWindow();
        window.setContentView(R.layout.dialog_ipay88_layout);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        RecyclerView recyclerView = (RecyclerView) window.findViewById(R.id.rv_ipay88_settlement);


        List<SettlementRestaurant> otherPaymentSettle = CoreData.getInstance().getSettlementRestaurant();
        Ipay88SettlementAdapter settlementAdapter = new Ipay88SettlementAdapter(paymentSettleRestaurant, clickListener);
        int numberOfColumns = 3;
        recyclerView.setLayoutManager(new GridLayoutManager(context, numberOfColumns));
        recyclerView.setAdapter(settlementAdapter);

        if (otherPaymentSettle != null) {
            for (int x = 0; x < otherPaymentSettle.size(); x++) {
                if (otherPaymentSettle.get(x).getMediaId() == ParamConst.SETTLEMENT_TYPE_IPAY88) {
                    String[] strarray = otherPaymentSettle.get(x).getOtherPaymentId().toString().split("[|]");
                    for (int i = 0; i < strarray.length; i++) {
                        PaymentMethod pa = CoreData.getInstance().getPaymentMethod(Integer.valueOf(strarray[i]).intValue());
                        if (pa != null) {
                            paymentSettleRestaurant.add(pa);
                            settlementAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        }
        settlementAdapter.notifyDataSetChanged();


    }


    public void close() {
        dlg.dismiss();
    }


}
