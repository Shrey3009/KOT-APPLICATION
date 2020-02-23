package com.alfredposclient.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.alfredbase.global.BugseeHelper;
import com.alfredposclient.R;

public class MoneyKeyboardForClock extends LinearLayout implements OnClickListener {
    private KeyBoardClickListener keyBoardClickListener;
    private LinearLayout ll_money;
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
    private Button btn_Delete;
    private Button btn_ClockIn;
    private Button btn_ClockOut;

    public MoneyKeyboardForClock(Context context) {
        super(context);
        init(context);
    }

    public MoneyKeyboardForClock(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        View.inflate(context, R.layout.money_keyboard_for_clock, this);
        ll_money = (LinearLayout) findViewById(R.id.ll_money);
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
        btn_Delete = (Button) findViewById(R.id.btn_Delete);
        btn_ClockIn = (Button) findViewById(R.id.btn_ClockIn);
        btn_ClockOut = (Button) findViewById(R.id.btn_ClockOut);
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
        btn_Delete.setTag("Delete");
        btn_Delete.setOnClickListener(this);
        btn_ClockIn.setTag("clock In");
        btn_ClockIn.setOnClickListener(this);
        btn_ClockOut.setTag("clock Out");
        btn_ClockOut.setOnClickListener(this);
    }

    public void setKeyBoardClickListener(
            KeyBoardClickListener keyBoardClickListener) {
        this.keyBoardClickListener = keyBoardClickListener;
    }

    public void setMoneyPanel(int visibility) {
        ll_money.setVisibility(visibility);
    }

    public interface KeyBoardClickListener {
        void onKeyBoardClick(String key);
    }

    @Override
    public void onClick(View v) {
        Button button = (Button) v;
        BugseeHelper.buttonClicked((String) button.getText());
        switch (v.getId()) {
            // case R.id.btn_ClockOut: {
            // if (keyBoardClickListener != null)
            // keyBoardClickListener.onKeyBoardClick("Cancel");
            // break;
            // }
            // case R.id.btn_ClockIn: {
            // if (keyBoardClickListener != null)
            // keyBoardClickListener.onKeyBoardClick("Enter");
            // break;
            // }
            // case R.id.btn_Delete: {
            // if (keyBoardClickListener != null)
            // keyBoardClickListener.onKeyBoardClick("Clear");
            // break;
            // }
            default:
                if (keyBoardClickListener != null)
                    keyBoardClickListener.onKeyBoardClick((String) button.getTag());
                break;
        }

    }

}
