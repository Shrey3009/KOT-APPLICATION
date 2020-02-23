package com.alfredselfhelp.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.alfredbase.BaseActivity;
import com.alfredbase.global.BugseeHelper;
import com.alfredbase.utils.TextTypeFace;
import com.alfredselfhelp.R;

public class Numerickeyboard extends LinearLayout {
    private LayoutInflater inflater;
    private GridView gv_keyboard;
    private KeyBoardClickListener keyBoardClickListener;
    private static final String[] NUMERIC = {"1", "2", "3", "4", "5", "6",
            "7", "8", "9", "", "0", "X"};
    private TextTypeFace textTypeFace;

    public Numerickeyboard(Context context) {
        super(context);
        init(context);
    }

    public Numerickeyboard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        View.inflate(context, R.layout.numeric_keyboard_kpm, this);
        inflater = LayoutInflater.from(context);
        gv_keyboard = (GridView) findViewById(R.id.gv_keyboards);
        gv_keyboard.setAdapter(new Adapter());
        gv_keyboard.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                BugseeHelper.buttonClicked(NUMERIC[arg2]);
                if (keyBoardClickListener != null) {
                    keyBoardClickListener.onKeyBoardClick(NUMERIC[arg2]);
                }

            }
        });
    }

    public void setKeyBoardClickListener(KeyBoardClickListener keyBoardClickListener) {
        this.keyBoardClickListener = keyBoardClickListener;
    }

    public void setParams(BaseActivity context) {
        textTypeFace = TextTypeFace.getInstance();
        textTypeFace.init(context);
    }

    private final class Adapter extends BaseAdapter {

        @Override
        public int getCount() {
            return NUMERIC.length;
        }

        @Override
        public Object getItem(int arg0) {
            return NUMERIC[arg0];
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            arg1 = inflater.inflate(R.layout.item_numeric_kpm_keyboard, null);
            TextView tv_text = (TextView) arg1.findViewById(R.id.tv_num_text);

            if (TextUtils.isEmpty(NUMERIC[arg0])) {
                tv_text.setVisibility(INVISIBLE);

            } else {
                //  tv_text.setText(NUMERIC[arg0]);
            }

            switch (NUMERIC[arg0]) {
                case "0":
                    tv_text.setBackground(getResources().getDrawable(R.drawable.numberpad10));
                    break;

                case "1":
                    tv_text.setBackground(getResources().getDrawable(R.drawable.numberpad1));
                    break;
                case "2":
                    tv_text.setBackground(getResources().getDrawable(R.drawable.numberpad2));
                    break;

                case "3":
                    tv_text.setBackground(getResources().getDrawable(R.drawable.numberpad3));
                    break;


                case "4":
                    tv_text.setBackground(getResources().getDrawable(R.drawable.numberpad4));
                    break;

                case "5":
                    tv_text.setBackground(getResources().getDrawable(R.drawable.numberpad5));
                    break;


                case "6":
                    tv_text.setBackground(getResources().getDrawable(R.drawable.numberpad6));
                    break;


                case "7":
                    tv_text.setBackground(getResources().getDrawable(R.drawable.numberpad7));
                    break;

                case "8":
                    tv_text.setBackground(getResources().getDrawable(R.drawable.numberpad8));
                    break;

                case "9":
                    tv_text.setBackground(getResources().getDrawable(R.drawable.numberpad9));
                    break;

                case "X":
                    tv_text.setBackground(getResources().getDrawable(R.drawable.numberpadx));
                    break;

            }

            textTypeFace.setTrajanProRegular(tv_text);
            return arg1;
        }

    }

    public interface KeyBoardClickListener {
        void onKeyBoardClick(String key);
    }

}
