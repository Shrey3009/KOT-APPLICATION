package com.alfredposclient.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alfredbase.javabean.PaymentMethod;
import com.alfredbase.utils.BH;
import com.alfredbase.utils.TextTypeFace;
import com.alfredposclient.R;
import com.alfredposclient.global.App;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class PamentMethodAdapter extends BaseAdapter {

    private Context context = null;
    private LayoutInflater inflater = null;

    private String t;
    private String pay;

    List<PaymentMethod> list;
    private DisplayImageOptions options;

    TextTypeFace textTypeFace = TextTypeFace.getInstance();

    public PamentMethodAdapter(Context context, List<PaymentMethod> list) {

        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);

        options = new DisplayImageOptions.Builder()
                .showImageOnFail(R.drawable.icon_settle_cash)
                .showImageForEmptyUri(R.drawable.icon_settle_cash)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int arg0) {
        return this.list.get(arg0);
    }

    public long getItemId(int arg0) {
        return arg0;
    }


    public View getView(int position, View view, ViewGroup arg2) {
        PamentMethodAdapter.ViewHolder holder = null;
        if (holder == null) {
            holder = new PamentMethodAdapter.ViewHolder();
            if (view == null) {
                view = this.inflater.inflate(R.layout.item_pament_method, (ViewGroup) null);
            }
            //   holder.img = (ImageView)view.findViewById(R.id.img_pament_meth);
            holder.tv = (TextView) view.findViewById(R.id.tv_payment_meth);
            holder.tax = (TextView) view.findViewById(R.id.tv_payment_tax);
            holder.admin = (TextView) view.findViewById(R.id.tv_payment_admin);
            holder.pay = (TextView) view.findViewById(R.id.tv_payment_pay);
            holder.symbol = (TextView) view.findViewById(R.id.tv_payment_symbol);
            holder.money = (TextView) view.findViewById(R.id.tv_payment_money);
            holder.img = (ImageView) view.findViewById(R.id.img_payment_left);

//            textTypeFace.setTrajanProBlod((TextView) window
//                    .findViewById(R.id.lv_media));


            initTextTypeFace(view);
            view.setTag(holder);
        } else {
            holder = (PamentMethodAdapter.ViewHolder) view.getTag();
        }

        PaymentMethod p = list.get(position);

        ImageLoader.getInstance().displayImage(p.getLogoSm(), holder.img, options);
        holder.tv.setText("" + p.getNameOt());
        holder.symbol.setText(App.instance.getLocalRestaurantConfig().getCurrencySymbol());
        if (p.getIsTax() == 0) {
            holder.tax.setText(App.instance.getString(R.string.without_tax) + ", ");
        } else {
            holder.tax.setText(App.instance.getString(R.string.taxes) + ", ");
        }

        if (p.getIsAdmin() == 0) {
            holder.admin.setText(context.getString(R.string.cashier) + ", ");
        } else {
            holder.admin.setText(context.getString(R.string.manager) + ", ");
        }

        if (p.getIsPart() == 1) {
            holder.pay.setText(context.getString(R.string.split_pay));


            if (p.getPartAcount() > 0) {
                holder.symbol.setVisibility(View.VISIBLE);
                holder.money.setVisibility(View.VISIBLE);
                holder.money.setText(BH.formatMoney(String.valueOf(p.getPartAcount())).toString());
            } else {
                holder.symbol.setVisibility(View.GONE);
                holder.money.setVisibility(View.GONE);
            }
        } else {
            holder.pay.setText(context.getString(R.string.all_pay));
            holder.symbol.setVisibility(View.GONE);
            holder.money.setVisibility(View.GONE);


        }
        return view;
    }

    public class ViewHolder {
        public ImageView img;
        public TextView tv, tax, pay, admin, symbol, money;

    }

    private void initTextTypeFace(View view) {
        TextTypeFace textTypeFace = TextTypeFace.getInstance();

        textTypeFace.setTrajanProBlod((TextView) view
                .findViewById(R.id.tv_payment_meth));
        textTypeFace.setTrajanProBlod((TextView) view
                .findViewById(R.id.tv_payment_tax));
        textTypeFace.setTrajanProBlod((TextView) view
                .findViewById(R.id.tv_payment_admin));
        textTypeFace.setTrajanProBlod((TextView) view
                .findViewById(R.id.tv_payment_pay));
        textTypeFace.setTrajanProBlod((TextView) view
                .findViewById(R.id.tv_payment_symbol));
        textTypeFace.setTrajanProBlod((TextView) view
                .findViewById(R.id.tv_payment_money));

    }
}
