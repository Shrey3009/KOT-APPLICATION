
package com.alfredselfhelp.utils;


import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 对话框工具类
 *
 * @author LuCenly
 * @version 1.0
 */
public class ToolAlert {


    private static ListView listview;
    private ListView listView;
    private static Context context;

    private static ProgressDialog mProgressDialog;

    private static List<Map<String, Object>> plist;
    public static final int NO_ICON = -1;  //无图标

//    static PayDetailsAdapter adapters;
//    private static GridView gridView_check;
//
//    private static ImageAdapter ia_fast;
//
//    static List<TouchedUsersList.UserInfoBean> u=new ArrayList<TouchedUsersList.UserInfoBean>();


    //    /**
//     * login对话框
//     *
//     * @param context 上下文 必填
//     * @param title   标题 必填
//     *                //     * @param message  显示内容 必填
//     *                //     * @param btnName  按钮名称 必填
//     *                //     * @param listener 监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
//     * @return
//     */
    public static Dialog MyDialog(final Context context, String magess, String left, String right, View.OnClickListener okBtnListenner, View.OnClickListener caBtnListenner) {

        final AlertDialog dlg = new AlertDialog.Builder(context).create();
        dlg.show();
        dlg.setCancelable(false);
        dlg.setCanceledOnTouchOutside(false);
        Window window = dlg.getWindow();
        window.setContentView(com.alfredbase.R.layout.dialog_item_kpm_qr_btns);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        // window.setContentView(viewId);

        //	TextView msg = (TextView) window.findViewById(R.id.tv_dia_message);

//		if (magess.equals("")) {
//			msg.setVisibility(View.GONE);
//		} else {
//			msg.setText(magess);
//		}


        TextView back = (TextView) window.findViewById(com.alfredbase.R.id.tv_backs);
        //	back.setText(left);
        back.setOnClickListener(okBtnListenner);

//		Button cancel = (Button) window.findViewById(R.id.negative_Button);
//		cancel.setText(right);
//
//
//		cancel.setOnClickListener(caBtnListenner);

        return dlg;

    }




//    //快速心动
//    public static Dialog fastDialog(final Context context, String s, TouchedUsersList data, View.OnClickListener okBtnListenner, View.OnClickListener caListenner) {
//
//        final AlertDialog dlg = new Builder(context).create();
//        dlg.show();
//        dlg.setCancelable(false);
//        dlg.setCanceledOnTouchOutside(false);
//
//        Window window = dlg.getWindow();
//        window.setContentView(R.layout.dialog_fast_layout);
//        window.setBackgroundDrawableResource(android.R.color.transparent);
//
////        listview = (ListView) window.findViewById(R.id.
////                lv_buypay);
////
////
////        final List<Map<String, Object>> list = getData();
////        adapters = new PayDetailsAdapter(context, list, "-1");
////
////        listview.setAdapter(adapters);
//
//
//
//        u.clear();
//
//        u= data.getUserInfo().get(Integer.valueOf(s).intValue());
//
//
//
//        gridView_check = (GridView) window.findViewById(R.id.gridview_check);
//        ia_fast = new ImageAdapter(context, true,u);
//        gridView_check.setAdapter(ia_fast);
//
//        gridView_check.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
////                PayDetailsAdapter.ViewHolder holder = (PayDetailsAdapter.ViewHolder) view.getTag();
////                holder.cb.toggle();
////
////                for (int i = 0; i < list.size(); i++) {
////                    PayDetailsAdapter.isSelected.put(i, false);
////                }
////                adapters.notifyDataSetChanged();
////                PayDetailsAdapter.isSelected.put(position, true);
////                //  pays = (String) list.get(position).get("id");
////                System.out.println("======支付fangs======" + position);
////
////                Global.payment = position + "";
//
//                Global.touch=false;
//
//                ia_fast.changeState(position);
//
//            }
//        });
//
//        Button ok = (Button) window.findViewById(R.id.btn_fast);
//
//        ok.setOnClickListener(okBtnListenner);
//
//        ImageView cancel = (ImageView) window.findViewById(R.id.img_cancel);
//        if(s.equals("0"))
//        {
//            cancel.setVisibility(View.INVISIBLE);
//        }else {
//
//            cancel.setVisibility(View.VISIBLE);
//        }
//
//
//        cancel.setOnClickListener(caListenner);
//
//        return dlg;
//
//    }
//
//
//    //支付宝支付
//    public static Dialog payDialog(final Context context, int num, String money, View.OnClickListener okBtnListenner, View.OnClickListener caListenner) {
//
//        final AlertDialog dlg = new Builder(context).create();
//        dlg.show();
//        dlg.setCancelable(false);
//        dlg.setCanceledOnTouchOutside(false);
//
//        Window window = dlg.getWindow();
//        window.setContentView(R.layout.dialog_pay_layout);
//        window.setBackgroundDrawableResource(android.R.color.transparent);
//
//        listview = (ListView) window.findViewById(R.id.
//                lv_buypay);
//
//
//        final List<Map<String, Object>> list = getData();
//        adapters = new PayDetailsAdapter(context, list, "-1");
//
//        listview.setAdapter(adapters);
//
//
//        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                PayDetailsAdapter.ViewHolder holder = (PayDetailsAdapter.ViewHolder) view.getTag();
//                holder.cb.toggle();
//
//                for (int i = 0; i < list.size(); i++) {
//                    PayDetailsAdapter.isSelected.put(i, false);
//                }
//                adapters.notifyDataSetChanged();
//                PayDetailsAdapter.isSelected.put(position, true);
//                //  pays = (String) list.get(position).get("id");
//                System.out.println("======支付fangs======" + position);
//
//                Global.payment = position + "";
//
//            }
//        });
//
//
//        // window.setContentView(viewId);
//
////        TextView msg = (TextView) window.findViewById(R.id.tv_dia_message);
//
////        if (magess.equals("")) {
////            msg.setVisibility(View.GONE);
////        } else {
////            msg.setText(magess);
////        }
////        TextView nums = (TextView) window.findViewById(R.id.tv_tbNum);
////      //  nums.setText(num + "");
////
////        TextView total = (TextView) window.findViewById(R.id.tv_total_num);
//        //  total.setText("目前糖币为" + Global.getInfo(context).getBaseInfo().getCoin() + "个");
//        TextView title = (TextView) window.findViewById(R.id.tv_pay_title);
//        TextView pic = (TextView) window.findViewById(R.id.tv_pay_pic);
//
//
//        if (!TextUtils.isEmpty(money)) {
//
//
//            if (money.equals("0.01")||money.equals("98")) {
//
//                pic.setText("98");
//                title.setText("你将购买7天的会员服务");
//            } else if (money.equals("0.02")||money.equals("298")) {
//                pic.setText("298");
//                title.setText("你将购买1个月的会员服务");
//            } else {
//                pic.setText("2998");
//                title.setText("你将购买1年的会员服务");
//
//            }
//
//
//        } else {
//            pic.setText(money);
////            title.setText("您需要购买");
//
//        }
//
//
//        Button ok = (Button) window.findViewById(R.id.btn_pay);
//
//        ok.setOnClickListener(okBtnListenner);
//
//        ImageView cancel = (ImageView) window.findViewById(R.id.img_cancel);
//
//
//        cancel.setOnClickListener(caListenner);
//
//        return dlg;
//
//    }
//
//
//    //糖币支付
//    public static Dialog TbPayDialog(final Context context, String num, View.OnClickListener okBtnListenner, View.OnClickListener caListenner) {
//
//        final AlertDialog dlg = new Builder(context).create();
//        dlg.show();
//        dlg.setCancelable(false);
//        dlg.setCanceledOnTouchOutside(false);
//
//        Window window = dlg.getWindow();
//        window.setContentView(R.layout.dialog_tbpay_layout);
//        window.setBackgroundDrawableResource(android.R.color.transparent);
//        // window.setContentView(viewId);
//
////        TextView msg = (TextView) window.findViewById(R.id.tv_dia_message);
//
////        if (magess.equals("")) {
////            msg.setVisibility(View.GONE);
////        } else {
////            msg.setText(magess);
////        }
//        TextView nums = (TextView) window.findViewById(R.id.tv_tb_money);
//        nums.setText("目前糖币为" + num + "个");
//
//
//        Button ok = (Button) window.findViewById(R.id.btn_pay);
//
//        ok.setOnClickListener(okBtnListenner);
//
//        ImageView cancel = (ImageView) window.findViewById(R.id.img_cancel);
//
//
//        cancel.setOnClickListener(caListenner);
//
//
//        return dlg;
//
//    }
//
//
//    //系统审核信息
//    public static Dialog Review1Dialog(final Context context, String title, int img, String left, String right, String but, View.OnClickListener okBtnListenner) {
//
//        final AlertDialog dlg = new Builder(context).create();
//        dlg.show();
//        dlg.setCancelable(false);
//        dlg.setCanceledOnTouchOutside(false);
//
//        Window window = dlg.getWindow();
//        window.setContentView(R.layout.dialog_review1_layout);
//        window.setBackgroundDrawableResource(android.R.color.transparent);
//
//        TextView titles = (TextView) window.findViewById(R.id.tv_rev_title);
//
//        TextView lefts = (TextView) window.findViewById(R.id.tv_rev_left);
//
//        TextView rights = (TextView) window.findViewById(R.id.tv_rev_right);
//
//        ImageView imgs = (ImageView) window.findViewById(R.id.img_rev);
//        TextView ok = (TextView) window.findViewById(R.id.btn_rev_ok);
//        titles.setText(title);
//        lefts.setText(left);
//        rights.setText(right);
//        ok.setText(but);
//
//
//        imgs.setImageResource(img);
//        ok.setOnClickListener(okBtnListenner);
//
//
//        return dlg;
//
//    }
//
//
//    //头像系统审核信息
//    public static Dialog Review1Dialog2(final Context context, String title, String img, String type, String left, String right, String but, View.OnClickListener okBtnListenner) {
//
//        final AlertDialog dlg = new Builder(context).create();
//        dlg.show();
//        dlg.setCancelable(false);
//        dlg.setCanceledOnTouchOutside(false);
//
//        Window window = dlg.getWindow();
//        window.setContentView(R.layout.dialog_review2_layout);
//        window.setBackgroundDrawableResource(android.R.color.transparent);
//
//        TextView titles = (TextView) window.findViewById(R.id.tv_rev_title);
//
//        TextView lefts = (TextView) window.findViewById(R.id.tv_rev_left);
//
//        TextView rights = (TextView) window.findViewById(R.id.tv_rev_right);
//
//        ImageView imgs = (ImageView) window.findViewById(R.id.img_rev);
//
//        ImageView imgtype = (ImageView) window.findViewById(R.id.img_rev_type);
//        TextView ok = (TextView) window.findViewById(R.id.btn_rev_ok);
//        titles.setText(title);
//        lefts.setText(left);
//        rights.setText(right);
//        ok.setText(but);
//
//
//        RequestOptions options = new RequestOptions()
//                .centerCrop()
//                .placeholder(R.mipmap.ic_launcher)
//                .error(R.mipmap.ic_launcher)
//                //  .bitmapTransform(new GlideBlurformation(context))
//                .diskCacheStrategy(DiskCacheStrategy.ALL);
//
//        Glide.with(context)
//                .load(img)
////                            .bitmapTransform(new BlurTransformation(context,23,4))
//                .apply(options)
//                .into(imgs);
//
//
//        if (type.equals("2")) {
//            imgtype.setImageResource(R.mipmap.jg_icon);
//
//            ok.setVisibility(View.VISIBLE);
//        } else if (type.equals("6")) {
//            imgtype.setImageResource(R.mipmap.imgcheck_icon);
//
//            ok.setVisibility(View.GONE);
//        } else {
//
//            ok.setVisibility(View.VISIBLE);
//            imgtype.setImageResource(R.mipmap.imgcheck_icon);
//        }
////        imgs.setImageResource(img);
//        ok.setOnClickListener(okBtnListenner);
//
//
//        return dlg;
//
//    }
//
//    //新人有礼
//    public static Dialog FirstDialog(final Context context, View.OnClickListener okBtnListenner, View.OnClickListener caListenner) {
//
//        final AlertDialog dlg = new Builder(context).create();
//        dlg.show();
//        dlg.setCancelable(false);
//        dlg.setCanceledOnTouchOutside(false);
//
//        Window window = dlg.getWindow();
//        window.setContentView(R.layout.dialog_frist_layout);
//        window.setBackgroundDrawableResource(android.R.color.transparent);
//        // window.setContentView(viewId);
//
////        TextView msg = (TextView) window.findViewById(R.id.tv_dia_message);
//
////        if (magess.equals("")) {
////            msg.setVisibility(View.GONE);
////        } else {
////            msg.setText(magess);
////        }
////        TextView nums = (TextView) window.findViewById(R.id.tv_tb_money);
////        nums.setText("目前糖币为" + num + "个");
//
//
//        Button ok = (Button) window.findViewById(R.id.btn_first_ok);
//
//        ok.setOnClickListener(okBtnListenner);
//
//        ImageView cancel = (ImageView) window.findViewById(R.id.img_first_cancel);
//        cancel.setOnClickListener(caListenner);
//
//        return dlg;
//
//    }
//
//    //每日活动奖励
//    public static Dialog EveryDialog(final Context context, String confirm) {
//
//        final AlertDialog dlg = new Builder(context).create();
//        dlg.show();
//        dlg.setCancelable(false);
//        Window window = dlg.getWindow();
//        window.setContentView(R.layout.dialog_every_layout);
//        //  window.setContentView(viewId);
//        //透明度
////        WindowManager.LayoutParams lp = window.getAttributes();
////        lp.alpha = 0.0f;
////        window.setAttributes(lp);
//
//        window.setBackgroundDrawableResource(android.R.color.transparent);
////        TextView times=(TextView)window.findViewById(R.id.tv_confirm_time);
////
////        TextView message = (TextView) window.findViewById(R.id.tv_message_every);
////        message.setText(confirm);
////        moneys.setText("金额："+money+"元");
//
//        dlg.setCanceledOnTouchOutside(false);
////        TextView ok = (TextView) window.findViewById(R.id.tv_dia_dele);
////        ok.setText(confirm);
////        ok.setOnClickListener(okBtnListenner);
//        //dlg.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//        return dlg;
//
//    }
//
//    //状态
//    public static Dialog StateDialog(final Context context) {
//
//        final AlertDialog dlg = new Builder(context).create();
//        dlg.show();
//        dlg.setCancelable(false);
//        Window window = dlg.getWindow();
//        window.setContentView(R.layout.dialog_state_layout);
//        //  window.setContentView(viewId);
//        //透明度
////        WindowManager.LayoutParams lp = window.getAttributes();
////        lp.alpha = 0.0f;
////        window.setAttributes(lp);
//
//
//        window.setDimAmount(0);
//        window.setBackgroundDrawableResource(android.R.color.transparent);
////        TextView times=(TextView)window.findViewById(R.id.tv_confirm_time);
////
////        TextView message=(TextView)window.findViewById(R.id.tv_message_every);
////        message.setText(confirm);
//////        moneys.setText("金额："+money+"元");
//
//        dlg.setCanceledOnTouchOutside(true);
////        TextView ok = (TextView) window.findViewById(R.id.tv_dia_dele);
////        ok.setText(confirm);
////        ok.setOnClickListener(okBtnListenner);
//        //dlg.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//        return dlg;
//
//    }
//
//    //状态
//    public static Dialog StateDialog1(final Context context) {
//
//        final AlertDialog dlg = new Builder(context).create();
//        dlg.show();
//        dlg.setCancelable(false);
//        Window window = dlg.getWindow();
//        window.setContentView(R.layout.dialog_state1_layout);
//        //  window.setContentView(viewId);
//        //透明度
////        WindowManager.LayoutParams lp = window.getAttributes();
////        lp.alpha = 0.0f;
////        window.setAttributes(lp);
//
//
//        window.setDimAmount(0);
//        window.setBackgroundDrawableResource(android.R.color.transparent);
////        TextView times=(TextView)window.findViewById(R.id.tv_confirm_time);
////
////        TextView message=(TextView)window.findViewById(R.id.tv_message_every);
////        message.setText(confirm);
//////        moneys.setText("金额："+money+"元");
//
//        dlg.setCanceledOnTouchOutside(true);
////        TextView ok = (TextView) window.findViewById(R.id.tv_dia_dele);
////        ok.setText(confirm);
////        ok.setOnClickListener(okBtnListenner);
//        //dlg.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//        return dlg;
//
//    }
//
//
//    //
////    /**
//
//    //     * 认证对话框
////     *
////     * @param context 上下文 必填
////     *                <p>
////     *                //     * @param message  显示内容 必填Fri
////     *                //     * @param btnName  按钮名称 必填
////     *                //     * @param listener 监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
////     * @return
////     */
//    public static Dialog AuthDialog(final Context context, View.OnClickListener okBtnListenner) {
//
//        final AlertDialog dlg = new Builder(context).create();
//        dlg.show();
//        dlg.setCancelable(false);
//        dlg.setCanceledOnTouchOutside(false);
//        Window window = dlg.getWindow();
//        window.setContentView(R.layout.dialog_auth_layout);
//        window.setBackgroundDrawableResource(android.R.color.transparent);
//        // window.setContentView(viewId);
//
//
//        Button ok = (Button) window.findViewById(R.id.positiveButton_auth);
//
//        ok.setOnClickListener(okBtnListenner);
//
//
//        return dlg;
//
//    }
////
////
//
//
//    //app更新弹出框
//    public static Dialog UpdateDialog(final Context context, String version, String update, String forceUpdate, View.OnClickListener okBtnListenner, View.OnClickListener qxBtnListenner) {
//
//        final AlertDialog dlg = new Builder(context).create();
//        dlg.show();
//        dlg.setCancelable(false);
//        Window window = dlg.getWindow();
//        window.setContentView(R.layout.dialog_hint_update);
//        //  window.setContentView(viewId);
//        //透明度
////        WindowManager.LayoutParams lp = window.getAttributes();
////        lp.alpha = 0.0f;
////        window.setAttributes(lp);
//        window.setBackgroundDrawableResource(android.R.color.transparent);
//        TextView title = (TextView) window.findViewById(R.id.tv_updatetitle);
//        TextView updates = (TextView) window.findViewById(R.id.tv_update);
//        title.setText("升级到新版本" + version);
//
//        updates.setMovementMethod(ScrollingMovementMethod.getInstance());
//        updates.setText(update);
////
////        TextView moneys=(TextView)window.findViewById(R.id.tv_confirm_money);
////        times.setText("时长："+time+"分钟");
////        moneys.setText("金额："+money+"元");
//
//        dlg.setCanceledOnTouchOutside(false);
//        TextView ok = (TextView) window.findViewById(R.id.positiveButton);
//        Button qx = (Button) window.findViewById(R.id.qxButton);
//        ImageView close = (ImageView) window.findViewById(R.id.img_qu);
//
//        if (forceUpdate.equals("1")) {
//            qx.setVisibility(View.INVISIBLE);
//            close.setVisibility(View.INVISIBLE);
//        } else {
//            qx.setVisibility(View.VISIBLE);
//            close.setVisibility(View.VISIBLE);
//        }
//        close.setOnClickListener(qxBtnListenner);
//        qx.setOnClickListener(qxBtnListenner);
//        ok.setOnClickListener(okBtnListenner);
//        //dlg.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//
//        return dlg;
//
//    }
//
//
//    //糖币用完弹出框
//    public static Dialog NottbDialog(final Context context, String ntitle, String nback, View.OnClickListener okBtnListenner, View.OnClickListener qxBtnListenner) {
//
//        final AlertDialog dlg = new Builder(context).create();
//        dlg.show();
//        dlg.setCancelable(false);
//        Window window = dlg.getWindow();
//        window.setContentView(R.layout.dialog_not_tb);
//        //  window.setContentView(viewId);
//        //透明度
////        WindowManager.LayoutParams lp = window.getAttributes();
////        lp.alpha = 0.0f;
////        window.setAttributes(lp);
//        window.setBackgroundDrawableResource(android.R.color.transparent);
//        TextView title = (TextView) window.findViewById(R.id.tv_not_title);
//
//        title.setText(ntitle);
//
////
////
////        TextView moneys=(TextView)window.findViewById(R.id.tv_confirm_money);
////        times.setText("时长："+time+"分钟");
////        moneys.setText("金额："+money+"元");
//
//        dlg.setCanceledOnTouchOutside(false);
//        TextView ok = (TextView) window.findViewById(R.id.tv_recharge_vip);
//        Button qx = (Button) window.findViewById(R.id.qxButton);
//        ImageView close = (ImageView) window.findViewById(R.id.img_back);
//
//        qx.setText(nback);
//
//
//        close.setOnClickListener(qxBtnListenner);
//        qx.setOnClickListener(qxBtnListenner);
//        ok.setOnClickListener(okBtnListenner);
//        //dlg.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//
//        return dlg;
//
//    }
//
//    //发布糖圈弹出框
//    public static Dialog FindDialog(final Context context, String confirm, View.OnClickListener okBtnListenner, View.OnClickListener qxBtnListenner) {
//
//        final AlertDialog dlg = new Builder(context).create();
//        dlg.show();
//        dlg.setCancelable(false);
//        Window window = dlg.getWindow();
//        window.setContentView(R.layout.dialog_hint_find);
//        //  window.setContentView(viewId);
//        //透明度
////        WindowManager.LayoutParams lp = window.getAttributes();
////        lp.alpha = 0.0f;
////        window.setAttributes(lp);
//
//        window.setBackgroundDrawableResource(android.R.color.transparent);
////        TextView times=(TextView)window.findViewById(R.id.tv_confirm_time);
////
//        TextView title = (TextView) window.findViewById(R.id.tv_confirm_money);
//        if (confirm.equals("0")) {
//            title.setText("Hey ！欢迎来到糖圈，赶紧发布你的第一个" +
//                    "糖圈，让心仪的对象一眼看上美美/帅气多金" +
//                    "的你。");
//        } else {
//            title.setText("你还未发布过糖圈，赶紧来发布你的第一个" +
//                    "糖圈，让心仪的对象一眼看上美美/帅气多金" +
//                    "的你。");
//        }
////        moneys.setText("金额："+money+"元");
//
//        dlg.setCanceledOnTouchOutside(false);
//        Button ok = (Button) window.findViewById(R.id.positiveButton);
//        Button qx = (Button) window.findViewById(R.id.qxButton);
//        ImageView close = (ImageView) window.findViewById(R.id.img_qu);
//        close.setOnClickListener(qxBtnListenner);
//        qx.setOnClickListener(qxBtnListenner);
//        ok.setOnClickListener(okBtnListenner);
//        //dlg.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//
//        return dlg;
//
//    }
//
//
//    //发布糖圈弹出框
//    public static Dialog FindDialog2(final Context context, String confirm, View.OnClickListener okBtnListenner, View.OnClickListener qxBtnListenner) {
//
//        final AlertDialog dlg = new Builder(context).create();
//        dlg.show();
//        dlg.setCancelable(false);
//        Window window = dlg.getWindow();
//        window.setContentView(R.layout.dialog_hint_find);
//        //  window.setContentView(viewId);
//        //透明度
////        WindowManager.LayoutParams lp = window.getAttributes();
////        lp.alpha = 0.0f;
////        window.setAttributes(lp);
//
//        window.setBackgroundDrawableResource(android.R.color.transparent);
////        TextView times=(TextView)window.findViewById(R.id.tv_confirm_time);
////
//        TextView title = (TextView) window.findViewById(R.id.tv_confirm_money);
//        if (confirm.equals("0")) {
//            title.setText("Hey ！欢迎来到糖圈，赶紧发布你的第一个" +
//                    "糖圈，让心仪的对象一眼看上美美/帅气多金" +
//                    "的你。");
//        } else {
//            title.setText("你还未发布过糖圈，赶紧来发布你的第一个" +
//                    "糖圈，让心仪的对象一眼看上美美/帅气多金" +
//                    "的你。");
//        }
////        moneys.setText("金额："+money+"元");
//
//        dlg.setCanceledOnTouchOutside(false);
//        Button ok = (Button) window.findViewById(R.id.positiveButton);
//        Button qx = (Button) window.findViewById(R.id.qxButton);
//        ImageView close = (ImageView) window.findViewById(R.id.img_qu);
//        close.setOnClickListener(qxBtnListenner);
//        qx.setOnClickListener(qxBtnListenner);
//        ok.setOnClickListener(okBtnListenner);
//        //dlg.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//
//        return dlg;
//
//    }
//
//
//    //删除弹出框
//    public static Dialog DeleDialog(final Context context, String confirm, View.OnClickListener okBtnListenner) {
//
//        final AlertDialog dlg = new Builder(context).create();
//        dlg.show();
//        dlg.setCancelable(false);
//        Window window = dlg.getWindow();
//        window.setContentView(R.layout.dialog_dele);
//        //  window.setContentView(viewId);
//        //透明度
////        WindowManager.LayoutParams lp = window.getAttributes();
////        lp.alpha = 0.0f;
////        window.setAttributes(lp);
//
//        window.setBackgroundDrawableResource(android.R.color.transparent);
////        TextView times=(TextView)window.findViewById(R.id.tv_confirm_time);
////
////        TextView moneys=(TextView)window.findViewById(R.id.tv_confirm_money);
////        times.setText("时长："+time+"分钟");
////        moneys.setText("金额："+money+"元");
//
//        dlg.setCanceledOnTouchOutside(true);
//        TextView ok = (TextView) window.findViewById(R.id.tv_dia_dele);
//        ok.setText(confirm);
//        ok.setOnClickListener(okBtnListenner);
//        //dlg.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//        return dlg;
//
//    }
//
//
//    //账号互踢
//    public static Dialog AccountOutDialog(final Context context, String messge, View.OnClickListener okBtnListenner) {
//
//        final AlertDialog dlg = new Builder(context).create();
//        dlg.show();
//        dlg.setCancelable(false);
//        Window window = dlg.getWindow();
//        //  window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//        window.setContentView(R.layout.dialog_accoutout_layout);
//        //  window.setContentView(viewId);
//        //透明度
////        WindowManager.LayoutParams lp = window.getAttributes();
////        lp.alpha = 0.0f;
////        window.setAttributes(lp);
//
//        window.setBackgroundDrawableResource(android.R.color.transparent);
//        TextView mese = (TextView) window.findViewById(R.id.tv_listmessage);
//        mese.setText(messge);
////        TextView moneys=(TextView)window.findViewById(R.id.tv_confirm_money);
////        times.setText("时长："+time+"分钟");
////        moneys.setText("金额："+money+"元");
//        dlg.setCanceledOnTouchOutside(false);
//        TextView ok = (TextView) window.findViewById(R.id.positiveButton_out);
//        ok.setText("我知道了");
//        ok.setOnClickListener(okBtnListenner);
//        //  dlg.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//        return dlg;
//
//    }
//
//
//    /**
//     * 显示ProgressDialog
//     *
//     * @param context 上下文
//     * @param message 消息
//     */
//    public static void loading(Context context, String message) {
//
//        //loading(context,message,true);
//    }
//
//    /**
//     * 显示ProgressDialog
//     *
//     * @param context 上下文
//     * @param message 消息
//     */
//    public static void loading(Context context, String message, final ILoadingOnKeyListener listener) {
//
//        //	loading(context,message,true,listener);
//    }
//
//    /**
//     * 显示ProgressDialog
//     * @param context 上下文
//     * @param message 消息
//     * @param cancelable 是否可以取消
//    //	 */
////	public static void loading(Context context, String message,boolean cancelable){
////
////		if (mProgressDialog == null) {
////			mProgressDialog = new ProgressDialog(context,message);
////			mProgressDialog.setCancelable(cancelable);
////		}
////		if(mProgressDialog.isShowing()){mProgressDialog.cancel();mProgressDialog.dismiss();}
////		mProgressDialog.show();
////	}
////
////	/**
////	 * 显示ProgressDialog
////	 * @param context 上下文
////	 * @param message 消息
////	 */
////	public static void loading(Context context, String message,boolean cancelable ,final ILoadingOnKeyListener listener){
////
////		if(mProgressDialog == null){
////			mProgressDialog = new ProgressDialog(context,message);
////			mProgressDialog.setCancelable(cancelable);
////		}
////
////		if(mProgressDialog.isShowing()){mProgressDialog.cancel();mProgressDialog.dismiss();}
////
////		if(null != listener)
////		{
////			mProgressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
////	            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
////	            	listener.onKey(dialog, keyCode, event);
////	                return false;
////	            }
////	        });
////		}else{
////			mProgressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
////	            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
////	                if (keyCode == KeyEvent.KEYCODE_BACK) {
////	                	mProgressDialog.dismiss();
////	                }
////	                return false;
////	            }
////	        });
////		}
////
////		mProgressDialog.show();
////	}
//
//    /**
//     * 判断加载对话框是否正在加载
//     *
//     * @return 是否
//     */
//    public static boolean isLoading() {
//
//        if (null != mProgressDialog) {
//            return mProgressDialog.isShowing();
//        } else {
//            return false;
//        }
//    }
//
//    /**
//     * 关闭ProgressDialog
//     */
//    public static void closeLoading() {
//        if (mProgressDialog != null) {
//            mProgressDialog.dismiss();
//            mProgressDialog = null;
//        }
//    }
//
//    /**
//     * 更新ProgressDialog进度消息
//     *
//     * @param message 消息
//     */
//    public static void updateProgressText(String message) {
//        if (mProgressDialog == null) return;
//
//        if (mProgressDialog.isShowing()) {
//            mProgressDialog.setMessage(message);
//        }
//    }
//
//    /**
//     * 弹出对话框
//     * <p/>
//     * //     * @param title              对话框标题
//     * //     * @param okBtnListenner     确定按钮点击事件
//     * //     * @param cancelBtnListenner 取消按钮点击事件
//     *
//     * @param msg 对话框内容
//     */
////    public static AlertDialog dialog(String msg) {
////        return dialog(context, "", msg);
////    }
//
//    /**
//     * 弹出对话框
//     *
//     * @param title 对话框标题
//     * @param msg   对话框内容
//     *              //     * @param okBtnListenner     确定按钮点击事件
//     *              //     * @param cancelBtnListenner 取消按钮点击事件
//     */
////    public static AlertDialog dialog(Context context, String title, String msg) {
////        return dialog(context, title, msg, null);
////    }
//
//    /**
//     * 弹出对话框
//     *
//     * @param title          对话框标题
//     * @param msg            对话框内容
//     * @param okBtnListenner 确定按钮点击事件
//     *                       //     * @param cancelBtnListenner 取消按钮点击事件
//     */
////    public static AlertDialog dialog(Context context, String title, String msg, OnClickListener okBtnListenner) {
////        return dialog(context, title, msg, okBtnListenner, null);
////    }
//
//    /**
//     * 弹出对话框
//     *
//     * @param title              对话框标题
//     * @param msg                对话框内容
//     * @param okBtnListenner     确定按钮点击事件
//     * @param cancelBtnListenner 取消按钮点击事件
//     */
////    public static AlertDialog dialog(Context context, String title, String msg, OnClickListener okBtnListenner, OnClickListener cancelBtnListenner) {
////        return dialog(context, null, title, msg, okBtnListenner, cancelBtnListenner);
////    }
//
//    /**
//     * 弹出对话框
//     *
//     * @param title 对话框标题
//     * @param msg   对话框内容
//     *              //     * @param okBtnListenner     确定按钮点击事件
//     *              //     * @param cancelBtnListenner 取消按钮点击事件
//     */
////    public static AlertDialog dialog(Context context, Drawable icon, String title, String msg) {
////        return dialog(context, icon, title, msg, null);
////    }
//
//    /**
//     * 弹出对话框
//     *
//     * @param title          对话框标题
//     * @param msg            对话框内容
//     * @param okBtnListenner 确定按钮点击事件
//     *                       //     * @param cancelBtnListenner 取消按钮点击事件
//     */
////    public static AlertDialog dialog(Context context, Drawable icon, String title, String msg, OnClickListener okBtnListenner) {
////        return dialog(context, icon, title, msg, okBtnListenner, null);
////    }
//
//    /**
//     * 弹出对话框
//     *
//     * @param icon               标题图标
//     * @param title              对话框标题
//     * @param msg                对话框内容
//     * @param okBtnListenner     确定按钮点击事件
//     * @param cancelBtnListenner 取消按钮点击事件
//     */
////    public static AlertDialog dialog(Context context, Drawable icon, String title, String msg, OnClickListener okBtnListenner, OnClickListener cancelBtnListenner) {
////        Builder dialogBuilder = new Builder(context);
////
////        Window window = dialogBuilder.create().getWindow();
////        window.setContentView(R.layout.dialog_normal_layout);
////        if (null != icon) {
////            dialogBuilder.setIcon(icon);
////        }
////        if (ToolString.isNoBlankAndNoNull(title)) {
////            dialogBuilder.setTitle(title);
////        }
////        dialogBuilder.setMessage(msg);
////        if (null != okBtnListenner) {
////            dialogBuilder.setPositiveButton(android.R.string.ok, okBtnListenner);
////        }
////        if (null != cancelBtnListenner) {
////            dialogBuilder.setNegativeButton(android.R.string.cancel, cancelBtnListenner);
////        }
////
////
////        // dialogBuilder.create();
////
////
////        return dialogBuilder.show();
////    }
//
//    /**
//     * 弹出一个自定义布局对话框
//     *
//     * @param context 上下文
//     * @param view    自定义布局View
//     */
//    public static AlertDialog dialog(Context context, View view) {
//        final Builder builder = new Builder(context);
//        builder.setView(view);
//        return builder.show();
//    }
//
//    /**
//     * 弹出一个自定义布局对话框
//     *
//     * @param context 上下文
//     * @param resId   自定义布局View对应的layout id
//     */
//    public static AlertDialog dialog(Context context, int resId) {
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View view = inflater.inflate(resId, null);
//        Builder builder = new Builder(context);
//        builder.setView(view);
//        return builder.show();
//    }
//
//    /**
//     * 弹出较短的Toast消息
//     * @param msg 消息内容
//     */
////    public static void toastShort(String msg) {
////        Toast.makeText(CloudWashApplication.gainContext(), msg, Toast.LENGTH_SHORT).show();
////    }
////
//
//    /**
//     * 弹出较短的Toast消息
//     *
//     * @param msg 消息内容
//     */
//    public static void toastShort(Context context, String msg) {
//        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
//    }
//
//    /**
//     * 弹出较长的Toast消息
//     * @param msg 消息内容
//     */
////    public static void toastLong(String msg) {
////        Toast.makeText(CloudWashApplication.gainContext(), msg, Toast.LENGTH_LONG).show();
////    }
////
//
//    /**
//     * 弹出较长的Toast消息
//     *
//     * @param msg 消息内容
//     */
//    public static void toastLong(Context context, String msg) {
//        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
//    }
//
//    /**
//     * 弹出自定义时长的Toast消息
//     * @param msg 消息内容
//     */
////    public static void toast(String msg,int duration) {
////        Toast.makeText(CloudWashApplication.gainContext(), msg, duration).show();
////    }
////
//
//    /**
//     * 弹出自定义时长的Toast消息
//     *
//     * @param msg 消息内容
//     */
//    public static void toast(Context context, String msg, int duration) {
//        Toast.makeText(context, msg, duration).show();
//
//    }
//
//    /**
//     * 弹出Pop窗口
//     *
//     * @param context 依赖界面上下文
//     * @param anchor  触发pop界面的控件
//     * @param viewId  pop窗口界面layout
//     * @param xoff    窗口X偏移量
//     * @param yoff    窗口Y偏移量
//     */
//    public static PopupWindow popwindow(Context context, View anchor, int viewId, int xoff, int yoff) {
//        ViewGroup menuView = (ViewGroup) LayoutInflater.from(context).inflate(viewId, null);
//        PopupWindow pw = new PopupWindow(menuView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
//        pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        pw.setTouchable(true);
//        pw.setFocusable(true);
//        pw.setOutsideTouchable(true);
//        pw.showAsDropDown(anchor, xoff, yoff);
//        pw.update();
//        return pw;
//    }
//
//    /**
//     * 弹出Pop窗口
//     *
//     * @param anchor  触发pop界面的控件
//     * @param popView pop窗口界面
//     * @param xoff    窗口X偏移量
//     * @param yoff    窗口Y偏移量
//     */
//    public static PopupWindow popwindow(View anchor, View popView, int xoff, int yoff) {
//        PopupWindow pw = new PopupWindow(popView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
//        pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        pw.setOutsideTouchable(true);
//        pw.showAsDropDown(anchor, xoff, yoff);
//        pw.update();
//        return pw;
//    }
//
//    /**
//     * 弹出Pop窗口（可设置是否点击其他地方关闭窗口）
//     *
//     * @param context          依赖界面上下文
//     * @param anchor           触发pop界面的控件
//     * @param viewId           pop窗口界面layout
//     * @param xoff             窗口X偏移量
//     * @param yoff             窗口Y偏移量
//     * @param outSideTouchable 点击其他地方是否关闭窗口
//     */
//    public static PopupWindow popwindow(Context context, View anchor, int viewId, int xoff, int yoff, boolean outSideTouchable) {
//        ViewGroup menuView = (ViewGroup) LayoutInflater.from(context).inflate(viewId, null);
//        PopupWindow pw = new PopupWindow(menuView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
//        pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        pw.setTouchable(outSideTouchable);
//        pw.setFocusable(outSideTouchable);
//        pw.setOutsideTouchable(outSideTouchable);
//        pw.showAsDropDown(anchor, xoff, yoff);
//        pw.update();
//        return pw;
//    }
//
//    /**
//     * 弹出Pop窗口（可设置是否点击其他地方关闭窗口）
//     *
//     * @param anchor           触发pop界面的控件
//     * @param popView          pop窗口界面
//     * @param xoff             窗口X偏移量
//     * @param yoff             窗口Y偏移量
//     * @param outSideTouchable 点击其他地方是否关闭窗口
//     */
//    public static PopupWindow popwindow(View anchor, View popView, int xoff, int yoff, boolean outSideTouchable) {
//        PopupWindow pw = new PopupWindow(popView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
//        pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        pw.setOutsideTouchable(outSideTouchable);
//        pw.showAsDropDown(anchor, xoff, yoff);
//        pw.update();
//
//        return pw;
//    }
//
//    /**
//     * 指定坐标弹出Pop窗口
//     *
//     * @param pw      pop窗口对象
//     * @param anchor  触发pop界面的控件
//     * @param popView pop窗口界面
//     * @param x       窗口X
//     * @param y       窗口Y
//     *                //     * @param outSideTouchable 点击其他地方是否关闭窗口
//     */
//    public static PopupWindow popwindowLoction(PopupWindow pw, View anchor, View popView, int x, int y) {
//        if (pw == null) {
//            pw = new PopupWindow(popView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
//            pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            pw.setOutsideTouchable(false);
//        }
//
//        if (pw.isShowing()) {
//            pw.update(x, y, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//        } else {
//            pw.showAtLocation(anchor, Gravity.NO_GRAVITY, x, y);
//        }
//
//        return pw;
//    }
//
//
//    public void onClick(DialogInterface dialog, int which) {
//
//    }
//
//    /**
//     * 往状态栏发送一条通知消息
//     * @param mContext 上下文
//     * @param message 消息Bean
//    //     */
////    public static void notification(Context mContext,NotificationMessage message){
////
////    	//消息管理器
////    	NotificationManager mNoticeManager = (NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
////        //构造Notification
////        Notification notice = new Notification();
////        notice.icon = message.getIconResId();
////        notice.tickerText = message.getStatusBarText();
////        notice.when = System.currentTimeMillis();
////        /**
////         *  notification.defaults = Notification.DEFAULT_SOUND; // 调用系统自带声音
////			notification.defaults = Notification.DEFAULT_VIBRATE;// 设置默认震动
////			notification.defaults = Notification.DEFAULT_ALL; // 设置铃声震动
////			notification.defaults = Notification.DEFAULT_ALL; // 把所有的属性设置成默认
////         */
////        notice.defaults = Notification.DEFAULT_SOUND;//通知时发出的默认声音
////        /**
////         *  notification.flags = Notification.FLAG_NO_CLEAR; // 点击清除按钮时就会清除消息通知,但是点击通知栏的通知时不会消失
////			notification.flags = Notification.FLAG_ONGOING_EVENT; // 点击清除按钮不会清除消息通知,可以用来表示在正在运行
////			notification.flags |= Notification.FLAG_AUTO_CANCEL; // 点击清除按钮或点击通知后会自动消失
////			notification.flags |= Notification.FLAG_INSISTENT; // 一直进行，比如音乐一直播放，知道用户响应
////         */
////        notice.flags |= Notification.FLAG_AUTO_CANCEL; //通知点击清除
////
////        //设置通知显示的参数
////        Intent mIntent = new Intent(mContext, message.getForwardComponent());
////        mIntent.setAction(ToolString.gainUUID());
////        mIntent.putExtras(message.getExtras());
////        mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
////        //自动更新PendingIntent的Extra数据
////        PendingIntent pIntent = PendingIntent.getActivity(mContext,0,mIntent,PendingIntent.FLAG_UPDATE_CURRENT);
////        notice.setLatestEventInfo(mContext, message.getMsgTitle(), message.getMsgContent(),pIntent);
////
////        //发送通知
////        mNoticeManager.notify(1, notice);
////    }
////
//
//    /**
//     * 发送自定义布局通知消息
//     * @param mContext 上下文
//     * @param message  消息Bean
//    //     */
////    public static void notificationCustomView(Context mContext,NotificationMessage message){
////
////    	//消息管理器
////    	NotificationManager mNoticeManager = (NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
////        //构造Notification
////    	Notification mNotify = new Notification();
////        mNotify.icon = message.getIconResId();
////        mNotify.tickerText = message.getStatusBarText();
////        mNotify.when = System.currentTimeMillis();
////        mNotify.flags |= Notification.FLAG_AUTO_CANCEL; //通知点击清除
////        mNotify.contentView = message.getmRemoteViews();
////
////        //设置通知显示的参数
////        Intent mIntent = new Intent(mContext, message.getForwardComponent());
////        mIntent.setAction(ToolString.gainUUID());
////        mIntent.putExtras(message.getExtras());
////        mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
////        PendingIntent contentIntent = PendingIntent.getActivity(mContext,0,mIntent,PendingIntent.FLAG_UPDATE_CURRENT);
////        mNotify.contentIntent = contentIntent;
////
////        //发送通知
////        mNoticeManager.notify(1, mNotify);
////    }
//
//    /**
//     * Loading监听对话框
//     */
//    public interface ILoadingOnKeyListener {
//        boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event);
//    }
//
//
//    private static List<Map<String, Object>> getData() {
//        plist = new ArrayList<Map<String, Object>>();
//
//        Map<String, Object> map = new HashMap<String, Object>();
//
//
//        map = new HashMap<String, Object>();
//        map.put("title", "微信支付");
//        map.put("img", R.mipmap.wechat_icon);
//        map.put("id", "0");
//
//
//        plist.add(map);
//
//        map = new HashMap<String, Object>();
//        map.put("title", "支付宝支付");
//        map.put("img", R.mipmap.zfb_icon);
//        map.put("id", "1");
//
//        plist.add(map);
//
//        return plist;
//    }

}
