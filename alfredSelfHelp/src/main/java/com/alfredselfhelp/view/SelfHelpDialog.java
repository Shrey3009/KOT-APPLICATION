package com.alfredselfhelp.view;

import android.app.Dialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.BaseApplication;
import com.alfredbase.global.BugseeHelper;
import com.alfredbase.store.Store;
import com.alfredbase.utils.DialogFactory;
import com.alfredselfhelp.R;
import com.alfredselfhelp.utils.UIHelp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SelfHelpDialog extends DialogFactory {

    public static void commonTwoBtnIPInputDialog(final BaseActivity activity, final String title, final String content,
                                                 final String leftText, final String rightText,
                                               final View.OnClickListener leftListener,
                                               final View.OnClickListener rghtListener) {
        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                final Dialog dialog = new Dialog(activity, com.alfredbase.R.style.base_dialog);

                final View view = LayoutInflater.from(activity).inflate(
                        R.layout.self_dialog_input, null);
                ((TextView) view.findViewById(R.id.tv_title)).setText(title);
                ((TextView) view.findViewById(R.id.tv_content)).setText(content);
                ((TextView) view.findViewById(R.id.tv_left)).setText(leftText);
                ((TextView) view.findViewById(R.id.tv_right)).setText(rightText);
                final EditText editText = (EditText) view.findViewById(R.id.et_input);
                String inputNum = Store.getString(activity, Store.KPM_CC_IP);

                if (!TextUtils.isEmpty(inputNum)) {
                    editText.setText(inputNum);
                }
                editText.setMaxLines(Integer.MAX_VALUE);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setContentView(view);
                view.findViewById(R.id.tv_left).setOnClickListener(
                        new View.OnClickListener() {

                            @Override
                            public void onClick(final View v) {
                                dialog.dismiss();
//                                CommonUtil.hideSoftkeyBoard(activity);
                                BugseeHelper.buttonClicked(v);
                                BaseApplication.postHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (leftListener != null)
                                            leftListener.onClick(v);
                                    }
                                }, 500);
                            }
                        });
                view.findViewById(R.id.tv_right).setOnClickListener(
                        new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                BugseeHelper.buttonClicked(v);
                                String num = editText.getText().toString();
                                if (TextUtils.isEmpty(num)) {
                                    Store.remove(activity, Store.KPM_CC_IP);
                                } else {
                                    String ip = "(2[5][0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})";
                                    Pattern pattern = Pattern.compile(ip);
                                    Matcher matcher = pattern.matcher(num);
                                    if (matcher.matches()) {
                                        Store.putString(activity, Store.KPM_CC_IP, num);
                                    } else {
                                        UIHelp.showToast(activity, activity.getResources().getString(R.string.invalid_ip_));
                                    }

                                }


                                dialog.dismiss();
//                                CommonUtil.hideSoftkeyBoard(activity);
                                BaseApplication.postHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (rghtListener != null)
                                            rghtListener.onClick(editText);
                                    }
                                }, 500);
                            }
                        });
                if (activity == null || activity.isFinishing())
                    return;
                dialog.show();
            }
        });

    }
}
