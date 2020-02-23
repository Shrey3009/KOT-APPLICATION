package com.alfredselfhelp.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.utils.BH;
import com.alfredselfhelp.R;
import com.alfredselfhelp.adapter.NurDetailAdapter;
import com.alfredselfhelp.adapter.RvListener;
import com.alfredselfhelp.global.App;
import com.alfredselfhelp.utils.KpmTextTypeFace;

import java.util.ArrayList;
import java.util.List;

public class OrderSelfDialog extends Dialog{


    private TextView yes;//确定按钮
    private TextView no,tv_nur_name;//取消按钮
    private TextView titleTv;//消息标题文本
    private TextView messageTv;//消息提示文本
    private String titleStr,totalStr;//从外界设置的title文本
    private String messageStr;//从外界设置的消息文本
    private TextView name, total;
    //确定文本和取消文本的显示内容
    private String yesStr, noStr;

    private RecyclerView re_nur;
    List<OrderDetail> itemDetails= new ArrayList<>();
    LinearLayoutManager mLinearLayoutManager;

    private Context mContext;
    NurDetailAdapter adapter;
    int   width,height;
    private onNoOnclickListener noOnclickListener;//取消按钮被点击了的监听器
    private onYesOnclickListener yesOnclickListener;//确定按钮被点击了的监听器

    KpmTextTypeFace textTypeFace=KpmTextTypeFace.getInstance();

    /**
     * 设置取消按钮的显示内容和监听
     *
     * @param str
     * @param onNoOnclickListener
     */
    public void setNoOnclickListener(String str, onNoOnclickListener onNoOnclickListener) {
        if (str != null) {
        //    noStr = str;
        }
        this.noOnclickListener = onNoOnclickListener;
    }

    /**
     * 设置确定按钮的显示内容和监听
     *
     * @param str
     * @param onYesOnclickListener
     */
    public void setYesOnclickListener(String str, onYesOnclickListener onYesOnclickListener) {
        if (str != null) {
          //  yesStr = str;
        }
        this.yesOnclickListener = onYesOnclickListener;
    }

    public OrderSelfDialog(Context context) {
        super(context,R.style.dialog);
        this.mContext=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_orderself_detail);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);

        //初始化界面控件
        initView();
        //初始化界面数据
        initData();
        //初始化界面控件的事件
        initEvent();

    }

    /**
     * 初始化界面的确定和取消监听器
     */
    private void initEvent() {
        //设置确定按钮被点击后，向外界提供监听
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yesOnclickListener != null) {
                    yesOnclickListener.onYesClick();
                }
            }
        });
        //设置取消按钮被点击后，向外界提供监听
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noOnclickListener != null) {
                    noOnclickListener.onNoClick();
                }
            }
        });
    }
    public void show() {
        super.show();
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;

        getWindow().getDecorView().setPadding(0, 0, 0, 0);

        getWindow().setAttributes(layoutParams);
    }
    /**
     * 初始化界面控件的显示数据
     */
    private void initData() {


        mLinearLayoutManager = new LinearLayoutManager(mContext);
        mLinearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        re_nur.setLayoutManager(mLinearLayoutManager);
        if(adapter == null) {
            adapter = new NurDetailAdapter(mContext, itemDetails, new RvListener() {
                @Override
                public void onItemClick(int id, int position) {

                }
            });
            re_nur.setAdapter(adapter);
        }
//        //如果用户自定了title和message
        if (totalStr != null && total != null) {
            total.setText("S" + App.instance.getCurrencySymbol() + BH.formatMoney(totalStr));
        }
//        if (messageStr != null) {
//            messageTv.setText(messageStr);
//        }
        //如果设置按钮的文字
//        if (yesStr != null) {
//            yes.setText(yesStr);
//        }
//        if (noStr != null) {
//            no.setText(noStr);
//        }
        yes.setText(mContext.getResources().getString(R.string.yes));
        no.setText(mContext.getResources().getString(R.string.no));
    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        yes = (TextView) findViewById(R.id.tv_ok);
        no = (TextView) findViewById(R.id.tv_no);
        re_nur=(RecyclerView)findViewById(R.id.re_nur_order);

        tv_nur_name = (TextView) findViewById(R.id.tv_nur_name);
        name = (TextView) findViewById(R.id.tv_dialog_name);
        total = (TextView) findViewById(R.id.tv_dialog_total);

        textTypeFace.setUbuntuMedium(tv_nur_name);
        textTypeFace.setUbuntuMedium(yes);
        textTypeFace.setUbuntuMedium(no);
        textTypeFace.setUbuntuMedium(name);
        textTypeFace.setUbuntuMedium(total);
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        width = dm.widthPixels;
        height = dm.heightPixels;


        LinearLayout center = (LinearLayout) findViewById(R.id.ll_dialog_center);//获取当前控件的对象
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) center.getLayoutParams();
//获取当前控件的布局对象
        params.height = height / 3 * 2;//设置当前控件布局的高度
        center.setLayoutParams(params);


        //   titleTv = (TextView) findViewById(R.id.title);
        //   messageTv = (TextView) findViewById(R.id.message);
    }

    /**
     * 从外界Activity为Dialog设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        titleStr = title;
    }

    public void setTotal(String total) {
        totalStr = total;
    }

    public void setList(List<OrderDetail> itemDetail) {
        itemDetails.clear();
        itemDetails.addAll(itemDetail);
    }
    public List<OrderDetail> getList() {
        return itemDetails;
    }

    public void notifyAdapter(){
        if (totalStr != null && total != null) {
            total.setText("S" + App.instance.getCurrencySymbol() + BH.formatMoney(totalStr));
        }
        if(adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
    /**
     * 从外界Activity为Dialog设置dialog的message
     *
     * @param message
     */
    public void setMessage(String message) {
        messageStr = message;
    }

    /**
     * 设置确定按钮和取消被点击的接口
     */
    public interface onYesOnclickListener {
        void onYesClick();
    }

    public interface onNoOnclickListener {
        void onNoClick();
    }


}
