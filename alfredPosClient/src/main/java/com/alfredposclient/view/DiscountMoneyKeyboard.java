package com.alfredposclient.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.alfredbase.global.BugseeHelper;
import com.alfredbase.utils.TextTypeFace;
import com.alfredposclient.R;
import com.alfredposclient.adapter.DiscountOptionAdapter;
import com.alfredposclient.global.App;

public class DiscountMoneyKeyboard extends LinearLayout implements OnClickListener {
    private KeyBoardClickListener keyBoardClickListener;
    private Button btn_1;
    private Button btn_2;
    private Button btn_3;
    private Button btn_4;
    private Button btn_5;
    private Button btn_6;
    private Button btn_7;
    private Button btn_8;
    private Button btn_9;
    private Button btn_0;
    private Button btn_00;
    private Button btn_Clear;
    private Button btn_Enter;
    private Button btn_Cancel;
    private TextTypeFace textTypeFace;
    private DiscountOptionAdapter discountOptionAdapter;

    public DiscountMoneyKeyboard(Context context) {
        super(context);
        init(context);
    }

    public DiscountMoneyKeyboard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(final Context context) {
        View.inflate(context, R.layout.discount_money_keyboard, this);
        btn_1 = (Button) findViewById(R.id.btn_1);
        btn_2 = (Button) findViewById(R.id.btn_2);
        btn_3 = (Button) findViewById(R.id.btn_3);
        btn_4 = (Button) findViewById(R.id.btn_4);
        btn_5 = (Button) findViewById(R.id.btn_5);
        btn_6 = (Button) findViewById(R.id.btn_6);
        btn_7 = (Button) findViewById(R.id.btn_7);
        btn_8 = (Button) findViewById(R.id.btn_8);
        btn_9 = (Button) findViewById(R.id.btn_9);
        btn_0 = (Button) findViewById(R.id.btn_0);
        btn_00 = (Button) findViewById(R.id.btn_00);
        btn_Clear = (Button) findViewById(R.id.btn_Clear);
        btn_Enter = (Button) findViewById(R.id.btn_Enter);
        btn_Cancel = (Button) findViewById(R.id.btn_Cancel);
        btn_1.setTag("1");
        btn_1.setOnClickListener(this);
        btn_2.setTag("2");
        btn_2.setOnClickListener(this);
        btn_3.setTag("3");
        btn_3.setOnClickListener(this);
        btn_4.setTag("4");
        btn_4.setOnClickListener(this);
        btn_5.setTag("5");
        btn_5.setOnClickListener(this);
        btn_6.setTag("6");
        btn_6.setOnClickListener(this);
        btn_7.setTag("7");
        btn_7.setOnClickListener(this);
        btn_8.setTag("8");
        btn_8.setOnClickListener(this);
        btn_9.setTag("9");
        btn_9.setOnClickListener(this);
        btn_0.setTag("0");
        btn_0.setOnClickListener(this);
        btn_00.setTag("00");
        btn_00.setOnClickListener(this);
        btn_Clear.setTag("C");
        btn_Clear.setOnClickListener(this);
        btn_Enter.setTag("Enter");
        btn_Enter.setOnClickListener(this);
        btn_Cancel.setTag("X");
        btn_Cancel.setOnClickListener(this);
        initTextTypeFace();
        final GridView gv_discount = (GridView) findViewById(R.id.gv_discount);
        gv_discount.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {
                int optionLength = App.instance.getLocalRestaurantConfig().getDiscountOption().length;
                int numColumns = (int) Math.floor(optionLength % 4 == 0 ? optionLength / 4 : optionLength / 4 + 1);
                gv_discount.setNumColumns(numColumns);
                int width = context.getResources().getDimensionPixelOffset(R.dimen.dp80) + context.getResources().getDimensionPixelOffset(R.dimen.dp10);
                gv_discount.setLayoutParams(new LayoutParams(numColumns * width, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
            }
        });
        discountOptionAdapter = new DiscountOptionAdapter(context, App.instance.getLocalRestaurantConfig().getDiscountOption());
        gv_discount.setAdapter(discountOptionAdapter);

    }

    private void initTextTypeFace() {
        textTypeFace = TextTypeFace.getInstance();
        textTypeFace.setTrajanProRegular(btn_0);
        textTypeFace.setTrajanProRegular(btn_00);
        textTypeFace.setTrajanProRegular(btn_1);
        textTypeFace.setTrajanProRegular(btn_2);
        textTypeFace.setTrajanProRegular(btn_3);
        textTypeFace.setTrajanProRegular(btn_4);
        textTypeFace.setTrajanProRegular(btn_5);
        textTypeFace.setTrajanProRegular(btn_6);
        textTypeFace.setTrajanProRegular(btn_7);
        textTypeFace.setTrajanProRegular(btn_8);
        textTypeFace.setTrajanProRegular(btn_9);
        textTypeFace.setTrajanProRegular(btn_Enter);
        textTypeFace.setTrajanProRegular(btn_Clear);
        textTypeFace.setTrajanProRegular(btn_Cancel);
    }

    public void setKeyBoardClickListener(
            KeyBoardClickListener keyBoardClickListener) {
        this.keyBoardClickListener = keyBoardClickListener;
        discountOptionAdapter.setKeyBoardClickListener(keyBoardClickListener);
    }

    public interface KeyBoardClickListener {
        void onKeyBoardClick(String key);
    }

    @Override
    public void onClick(View v) {
        Button button = (Button) v;
        BugseeHelper.buttonClicked((String) button.getText());
        switch (v.getId()) {
            default:
                if (keyBoardClickListener != null)
                    keyBoardClickListener.onKeyBoardClick((String) button.getTag());
                break;
        }

    }


}
