package com.alfredmenu.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alfredbase.javabean.ItemDetail;
import com.alfredbase.utils.ButtonClickTimer;
import com.alfredmenu.popupwindow.ModifierWindow;
import com.alfredmenu.popupwindow.SetItemCountWindow;
import com.alfredmenu.R;
import com.alfredmenu.popupwindow.WaiterModifierCPWindow;

public class CountView extends LinearLayout implements OnClickListener {
    private TextView tv_count;
    private ImageView iv_notes;
    private OnCountChange onCountChange;
    private OnNotesChange onNotesChange;
    private ItemDetail itemDetail;
    private SetItemCountWindow setItemCountWindow;
     private WaiterModifierCPWindow modifierWindow;
    private int count ;
    LinearLayout ll_minus;
    public CountView(Context context) {
        super(context);
        init(context);
    }

    public CountView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d("COUNT",""+count);
        init(context);
    }

    private void init(Context context) {
        View.inflate(context, R.layout.count_view, this);
        ll_minus = (LinearLayout) findViewById(R.id.ll_minus);
        tv_count = (TextView) findViewById(R.id.tv_count);
        iv_notes = (ImageView) findViewById(R.id.iv_notes);

        Log.d("COunt",""+count);
//		findViewById(R.id.tv_minus).setOnClickListener(this);
//		findViewById(R.id.tv_add).setOnClickListener(this);
    }

    public void setOnCountChange(OnCountChange onCountChange) {
        this.onCountChange = onCountChange;
    }

    public void setNotesChange(OnNotesChange onNotesChange){
        this.onNotesChange = onNotesChange;
    }

    public void setIsCanClick(boolean isCanClick) {
        LinearLayout ll_add = (LinearLayout) findViewById(R.id.ll_add);
        LinearLayout ll_minus = (LinearLayout) findViewById(R.id.ll_minus);
//		ImageView tv_minus = (ImageView) findViewById(R.id.iv_minus);
//		ImageView tv_add = (ImageView) findViewById(R.id.iv_add);
        LinearLayout ll_count = (LinearLayout) findViewById(R.id.ll_count);
        LinearLayout ll_notes = (LinearLayout) findViewById(R.id.ll_notes);
        if (isCanClick) {
            ll_minus.setOnClickListener(this);
            ll_add.setOnClickListener(this);
            ll_count.setOnClickListener(this);
            ll_notes.setOnClickListener(this);
        } else {
//			tv_minus.setTextColor(getResources().getColor(R.color.gray));
            ll_add.setOnClickListener(this);
            ll_count.setOnClickListener(this);
        }
    }

    public void setParam(ItemDetail itemDetail, SetItemCountWindow setItemCountWindow) {
        this.itemDetail = itemDetail;
        this.setItemCountWindow = setItemCountWindow;
    }

    public void setNotes(ItemDetail itemDetail, WaiterModifierCPWindow modifierWindow){
        this.itemDetail = itemDetail;
        this.modifierWindow = modifierWindow;
    }

    public void setInitCount(int count) {
        tv_count.setText(count + "");
        Log.d("count", ""+count);
        if(count > 0){
            iv_notes.setVisibility(View.VISIBLE);
        }else{
            iv_notes.setVisibility(View.GONE);
        }
    }

    public void setVisibility(boolean visibility){
        if(!visibility){
            ll_minus.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (!ButtonClickTimer.canClick(v)) {
            return;
        }
        switch (v.getId()) {
            case R.id.ll_minus: {
                 count = 0;
                try {
                    count = Integer.parseInt(tv_count.getText().toString());
                    count--;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (count < 0)
                    count = 0;
                tv_count.setText(count + "");

                if (onCountChange != null) {

                    onCountChange.onChange(itemDetail, count, false,false);
                }
                break;
            }
            case R.id.ll_add: {
//                int count = 0;
                Log.d("111111111--->", "111111111");
                try {
                    count = Integer.parseInt(tv_count.getText().toString());
                    count++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (count < 1)
                    count = 1;
                tv_count.setText(count + "");


                if (onCountChange != null) {
//                    iv_notes.setVisibility(View.VISIBLE);

                    onCountChange.onChange(itemDetail, count, true,true);
                    Log.d("2222222222--->", "22222222222");
                }
                break;
            }
            case R.id.ll_count:
                setItemCountWindow.show(Integer.parseInt(tv_count.getText().toString()), itemDetail);
                break;
            case R.id.ll_notes :

//                if(count > 0){
//                    iv_notes.setVisibility(View.VISIBLE);
//                }
                count  = Integer.parseInt(tv_count.getText().toString());

                if(onNotesChange !=null) {
                    onNotesChange.onChangeNotes(itemDetail, count,true);
                }

            default:
                break;
        }

    }

    public interface OnCountChange {
        void onChange(ItemDetail selectedItemDetail, int count, boolean isAdd, boolean desc);
    }

    public interface OnNotesChange {
        void onChangeNotes (ItemDetail selectedItemDetail,int count, boolean isAdd);
    }

}
