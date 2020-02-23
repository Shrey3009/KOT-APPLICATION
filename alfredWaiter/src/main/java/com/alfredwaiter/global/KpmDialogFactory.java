package com.alfredwaiter.global;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.VideoView;

import com.alfredbase.BaseActivity;
import com.alfredbase.BaseApplication;
import com.alfredbase.javabean.model.PrinterDevice;
import com.alfredbase.javabean.system.VersionUpdate;
import com.alfredbase.javabean.temporaryforapp.AppOrder;
import com.alfredbase.store.Store;
import com.alfredbase.utils.BH;
import com.alfredbase.utils.BitmapUtil;
import com.alfredbase.utils.ButtonClickTimer;
import com.alfredbase.utils.CommonUtil;
import com.alfredbase.utils.ScreenSizeUtil;
import com.alfredbase.utils.TextTypeFace;
import com.alfredwaiter.R;
import com.alfredwaiter.utils.KpmTextTypeFace;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KpmDialogFactory {

    private static KpmTextTypeFace textTypeFace = KpmTextTypeFace.getInstance();

    public static void commonTwoBtnDialog(BaseActivity activity,
                                          String title, String content,
                                          String leftText, String rightText,
                                          OnClickListener leftListener,
                                          OnClickListener rghtListener) {
        commonTwoBtnDialog(activity, title, content, leftText, rightText, leftListener, rghtListener, false);
    }

    // 两个按钮带 input的 只给ip用
    public static void commonTwoBtnIPInputDialog(final BaseActivity activity, final String title, final String content,
                                                 final String leftText, final String rightText,
                                                 final OnClickListener leftListener,
                                                 final OnClickListener rghtListener) {
        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                final Dialog dialog = new Dialog(activity, com.alfredbase.R.style.base_dialog);
                Window window = dialog.getWindow();
                if (dialog != null && window != null) {
                    WindowManager.LayoutParams attr = window.getAttributes();
                    if (attr != null) {
                        attr.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        attr.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                        attr.gravity = Gravity.CENTER;//设置dialog 在布局中的位置
                    }
                }
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
                        new OnClickListener() {

                            @Override
                            public void onClick(final View v) {
                                dialog.dismiss();
//                                CommonUtil.hideSoftkeyBoard(activity);
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
                        new OnClickListener() {

                            @Override
                            public void onClick(View v) {

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
                                        return;
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


    // 两个按钮带 input的 只给时间用
    public static void commonTwoBtnTimeInputDialog(final BaseActivity activity, final String title, final String content,
                                                   final String leftText, final String rightText,
                                                   final OnClickListener leftListener,
                                                   final OnClickListener rghtListener) {
        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                final Dialog dialog = new Dialog(activity, com.alfredbase.R.style.base_dialog);
                Window window = dialog.getWindow();
                if (dialog != null && window != null) {
                    WindowManager.LayoutParams attr = window.getAttributes();
                    if (attr != null) {
                        attr.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        attr.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                        attr.gravity = Gravity.CENTER;//设置dialog 在布局中的位置
                    }
                }
                final View view = LayoutInflater.from(activity).inflate(
                        R.layout.self_dialog_input, null);
                ((TextView) view.findViewById(R.id.tv_title)).setText(title);
                ((TextView) view.findViewById(R.id.tv_content)).setText(content);
                ((TextView) view.findViewById(R.id.tv_left)).setText(leftText);
                ((TextView) view.findViewById(R.id.tv_right)).setText(rightText);
                final EditText editText = (EditText) view.findViewById(R.id.et_input);
                editText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);

                editText.setKeyListener(new DigitsKeyListener(false, true));
                editText.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }


                    @Override
                    public void afterTextChanged(Editable s) {
                        String editStr = s.toString().trim();

                        int posDot = editStr.indexOf(".");
                        //不允许输入3位小数,超过三位就删掉
                        if (posDot < 0) {
                            return;
                        }
                        if (editStr.length() - posDot - 1 > 1) {
                            s.delete(posDot + 2, posDot + 3);
                        } else {
                            //TODO...这里写逻辑
                        }
                    }
                });
                editText.setMaxLines(Integer.MAX_VALUE);
                String time = Store.getString(activity, Store.KPM_TIME);
                if (!TextUtils.isEmpty(time)) {
                    editText.setText(time);
                    editText.setSelection(time.length());
                }
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setContentView(view);
                view.findViewById(R.id.tv_left).setOnClickListener(
                        new OnClickListener() {

                            @Override
                            public void onClick(final View v) {
                                dialog.dismiss();
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
                        new OnClickListener() {

                            @Override
                            public void onClick(View v) {
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


    // 两个按钮带 input的 只给ImageURL用
    public static void commonTwoBtnImageURLInputDialog(final BaseActivity activity, final String title, final String content,
                                                       final String leftText, final String rightText,
                                                       final OnClickListener leftListener,
                                                       final OnClickListener rghtListener) {
        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                final Dialog dialog = new Dialog(activity, com.alfredbase.R.style.base_dialog);
                Window window = dialog.getWindow();
                if (dialog != null && window != null) {
                    WindowManager.LayoutParams attr = window.getAttributes();
                    if (attr != null) {
                        attr.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        attr.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                        attr.gravity = Gravity.CENTER;//设置dialog 在布局中的位置
                    }
                }
                final View view = LayoutInflater.from(activity).inflate(
                        R.layout.self_dialog_input, null);
                ((TextView) view.findViewById(R.id.tv_title)).setText(title);
                ((TextView) view.findViewById(R.id.tv_content)).setText(content);
                ((TextView) view.findViewById(R.id.tv_left)).setText(leftText);
                ((TextView) view.findViewById(R.id.tv_right)).setText(rightText);
                final EditText editText = (EditText) view.findViewById(R.id.et_input);
                String imgUrl = Store.getString(activity, Store.MAIN_URL);
                if (!TextUtils.isEmpty(imgUrl)) {
                    editText.setText(imgUrl);
                }
                editText.setMaxLines(Integer.MAX_VALUE);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setContentView(view);
                view.findViewById(R.id.tv_left).setOnClickListener(
                        new OnClickListener() {

                            @Override
                            public void onClick(final View v) {
                                dialog.dismiss();
//                                CommonUtil.hideSoftkeyBoard(activity);
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
                        new OnClickListener() {

                            @Override
                            public void onClick(View v) {

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

    /**
     * 通用的，两按钮Dialog
     *
     * @param activity
     * @param title
     * @param content
     * @param leftText
     * @param rightText
     * @param leftListener
     * @param rghtListener
     */
    public static void commonTwoBtnDialog(final BaseActivity activity,
                                          final String title, final String content, final String leftText, final String rightText,
                                          final OnClickListener leftListener,
                                          final OnClickListener rghtListener, final boolean canBack) {
        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                final Dialog dialog = new Dialog(activity, R.style.base_dialog);
                Window window = dialog.getWindow();
                if (dialog != null && window != null) {
                    WindowManager.LayoutParams attr = window.getAttributes();
                    if (attr != null) {
                        attr.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        attr.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                        attr.gravity = Gravity.CENTER;//设置dialog 在布局中的位置
                    }
                }
                View view = LayoutInflater.from(activity).inflate(
                        R.layout.dialog_item_common_two_btn, null);
                ((TextView) view.findViewById(R.id.tv_title)).setText(title);
                ((TextView) view.findViewById(R.id.tv_content)).setText(content);
                ((TextView) view.findViewById(R.id.tv_left)).setText(leftText);
                ((TextView) view.findViewById(R.id.tv_right)).setText(rightText);
                dialog.setCancelable(canBack);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setContentView(view);
                view.findViewById(R.id.tv_left).setOnClickListener(
                        new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                App.instance.startADKpm();
                                if (leftListener != null)
                                    leftListener.onClick(v);
                            }
                        });
                view.findViewById(R.id.tv_right).setOnClickListener(
                        new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                App.instance.startADKpm();
                                if (rghtListener != null)
                                    rghtListener.onClick(v);
                            }
                        });
                if (activity == null || activity.isFinishing())
                    return;
                dialog.show();
            }
        });

    }

    // 扫码弹框
    public static Dialog qcDialog(final BaseActivity activity,
                                  final String title, final String content, int drawableId, final Boolean isqc,

                                  final OnClickListener backListener, final boolean canBack) {
        final Dialog dialog = new Dialog(activity, R.style.kpm_dialog);
        Window window = dialog.getWindow();
        if (dialog != null && window != null) {
            WindowManager.LayoutParams attr = window.getAttributes();
            if (attr != null) {
                attr.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                attr.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                attr.gravity = Gravity.CENTER;//设置dialog 在布局中的位置
            }
        }
        View view = LayoutInflater.from(activity).inflate(
                R.layout.dialog_item_kpm_qr_btns, null);
        ((TextView) view.findViewById(R.id.tv_title)).setText(title);
        ((TextView) view.findViewById(R.id.tv_content)).setText(content);
        ImageView img = (ImageView) view.findViewById(R.id.img_center);
        TextView tv_qc_de = (TextView) view.findViewById(R.id.tv_qc_de);
        TextView tv_backs = (TextView) view.findViewById(R.id.tv_backs);
        textTypeFace.setUbuntuMedium((TextView) view.findViewById(R.id.tv_title));
        textTypeFace.setUbuntuMedium((TextView) view.findViewById(R.id.tv_content));
        textTypeFace.setUbuntuMedium(tv_backs);
        textTypeFace.setUbuntuMedium(tv_qc_de);
        if (canBack) {
            tv_backs.setVisibility(View.VISIBLE);
        } else {
            tv_backs.setVisibility(View.GONE);
        }
        img.setImageResource(drawableId);
        if (isqc) {
            tv_qc_de.setVisibility(View.VISIBLE);
        } else {
            tv_qc_de.setVisibility(View.INVISIBLE);
        }
        dialog.show();
        dialog.setCancelable(canBack);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(view);

        view.findViewById(R.id.tv_backs).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        App.instance.startADKpm();
                        if (backListener != null)
                            backListener.onClick(v);
                    }
                });
//        if (activity == null || activity.isFinishing())
//            return;

//			}
//		});
        return dialog;
    }


    public static Dialog kpmFDialog(final BaseActivity activity,


                                    final OnClickListener backListener, final boolean canBack) {

        final Dialog dialog = new Dialog(activity, R.style.kpm_dialog);


        Window window = dialog.getWindow();
        if (dialog != null && window != null) {
            WindowManager.LayoutParams attr = window.getAttributes();
            if (attr != null) {
                attr.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                attr.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                attr.gravity = Gravity.CENTER;//设置dialog 在布局中的位置
            }
        }
        View view = LayoutInflater.from(activity).inflate(
                R.layout.dialog_item_kpm_f_btns, null);

//        ((TextView) view.findViewById(R.id.tv_content)).setText(content);
//        ImageView img = (ImageView) view.findViewById(R.id.img_center);
//
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams()
        //  img.setImageResource(drawableId);

//				((TextView) view.findViewById(R.id.tv_left)).setText(leftText);
//				((TextView) view.findViewById(R.id.tv_right)).setText(rightText);
        dialog.show();
        dialog.setCancelable(canBack);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(view);

        textTypeFace.setUbuntuMedium((TextView) view.findViewById(R.id.tv_backs));
        view.findViewById(R.id.tv_backs).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        App.instance.startADKpm();
                        if (backListener != null)
                            backListener.onClick(v);
                    }
                });
//        if (activity == null || activity.isFinishing())
//            return;

//			}
//		});
        return dialog;
    }

// VideoView 视频播放

    public static Dialog kpmVideoViewDialog(final BaseActivity activity, int videoId,
                                            final OnClickListener backListener, final boolean canBack) {

        final Dialog dialog = new Dialog(activity, R.style.kpm_dialog);
        View view = LayoutInflater.from(activity).inflate(
                R.layout.dialog_item_kpm_video_btns, null);
        final VideoView mvideoView = (VideoView) view.findViewById(R.id.dia_videoViews);
        String uri = "android.resource://" + activity.getPackageName() + "/" + videoId;

        mvideoView.setVideoURI(Uri.parse(uri));
        mvideoView.setZOrderOnTop(true);
        mvideoView.start();
        //监听视频播放完的代码
        mvideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mPlayer) {
                // TODO Auto-generated method stub
                mPlayer.start();
                mPlayer.setLooping(true);
            }
        });
//        ((TextView) view.findViewById(R.id.tv_content)).setText(content);
//        ImageView img = (ImageView) view.findViewById(R.id.img_center);
//
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams()
        //  img.setImageResource(drawableId);
//				((TextView) view.findViewById(R.id.tv_left)).setText(leftText);
//				((TextView) view.findViewById(R.id.tv_right)).setText(rightText);
        dialog.show();
        dialog.setCancelable(canBack);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(view);

        textTypeFace.setUbuntuMedium((TextView) view.findViewById(R.id.tv_backs));
        view.findViewById(R.id.tv_backs).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        mvideoView.stopPlayback();
                        dialog.dismiss();

                        App.instance.startADKpm();
                        if (backListener != null)
                            backListener.onClick(v);
                    }
                });
//        if (activity == null || activity.isFinishing())
//            return;

//			}
//		});
        return dialog;
    }

    //错误提示消息弹框（back按钮）
    public static Dialog kpmTipsDialog(final BaseActivity activity,
                                       final String title, String content, int drawableId,

                                       final OnClickListener backListener, final boolean canBack) {
        final Dialog dialog = new Dialog(activity, R.style.kpm_dialog);
        Window window = dialog.getWindow();
        if (dialog != null && window != null) {
            WindowManager.LayoutParams attr = window.getAttributes();
            if (attr != null) {
                attr.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                attr.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                attr.gravity = Gravity.CENTER;//设置dialog 在布局中的位置
            }
        }
        View view = LayoutInflater.from(activity).inflate(
                R.layout.dialog_item_kpm_tip_btns, null);
        ((TextView) view.findViewById(R.id.tv_title)).setText(title);
        ((TextView) view.findViewById(R.id.tv_content)).setText(content);
        ImageView img = (ImageView) view.findViewById(R.id.img_center);
        TextView tv_qc_de = (TextView) view.findViewById(R.id.tv_qc_de);
        img.setImageResource(drawableId);

        textTypeFace.setUbuntuMedium((TextView) view.findViewById(R.id.tv_title));
        textTypeFace.setUbuntuRegular((TextView) view.findViewById(R.id.tv_content));

        textTypeFace.setUbuntuMedium((TextView) view.findViewById(R.id.tv_backs));
        view.findViewById(R.id.tv_backs).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        App.instance.startADKpm();
                        if (backListener != null) {
                            backListener.onClick(v);
                        }
                        dialog.dismiss();
                    }
                });
        dialog.show();
        dialog.setCancelable(canBack);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(view);
        return dialog;
    }

    //out of stock弹框（back按钮）
    public static Dialog kpmOutStockDialog(final BaseActivity activity,
                                           final String title, String content, int drawableId,

                                           final OnClickListener backListener, final boolean canBack) {
        final Dialog dialog = new Dialog(activity, R.style.kpm_dialog);
        Window window = dialog.getWindow();
        if (dialog != null && window != null) {
            WindowManager.LayoutParams attr = window.getAttributes();
            if (attr != null) {
                attr.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                attr.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                attr.gravity = Gravity.CENTER;//设置dialog 在布局中的位置
            }
        }
        View view = LayoutInflater.from(activity).inflate(
                R.layout.dialog_item_kpm_tip_btns, null);
        ((TextView) view.findViewById(R.id.tv_title)).setText(title);
        ((TextView) view.findViewById(R.id.tv_content)).setText(content);
        ImageView img = (ImageView) view.findViewById(R.id.img_center);
        TextView tv_qc_de = (TextView) view.findViewById(R.id.tv_qc_de);
        img.setImageResource(drawableId);

        textTypeFace.setUbuntuMedium((TextView) view.findViewById(R.id.tv_title));
        textTypeFace.setUbuntuRegular((TextView) view.findViewById(R.id.tv_content));

        textTypeFace.setUbuntuMedium((TextView) view.findViewById(R.id.tv_backs));
        view.findViewById(R.id.tv_backs).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        App.instance.startADKpm();
                        if (backListener != null) {
                            backListener.onClick(v);
                        }
                        dialog.dismiss();
                    }
                });
        dialog.show();
        dialog.setCancelable(canBack);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(view);
        return dialog;
    }

    //支付完成弹框(不带按钮的)
    public static Dialog kpmCompleteDialog(final BaseActivity activity,
                                           final String title, final String content, int drawableId,

                                           final boolean canBack) {
        final Dialog dialog = new Dialog(activity, R.style.kpm_dialog);
        Window window = dialog.getWindow();
        if (dialog != null && window != null) {
            WindowManager.LayoutParams attr = window.getAttributes();
            if (attr != null) {
                attr.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                attr.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                attr.gravity = Gravity.CENTER;//设置dialog 在布局中的位置
            }
        }

        View view = LayoutInflater.from(activity).inflate(
                R.layout.dialog_item_kpm_complete_btns, null);
        ((TextView) view.findViewById(R.id.tv_title)).setText(title);
        ((TextView) view.findViewById(R.id.tv_content)).setText(content);
//        TextView contentbottoms = (TextView) view.findViewById(R.id.tv_content_bottom);
//        contentbottoms.setText(contentbottom);
        ImageView img = (ImageView) view.findViewById(R.id.img_center);
        img.setImageResource(drawableId);
        ((TextView) view.findViewById(R.id.tv_content)).setText(content);

        TextView tv_qc_de = (TextView) view.findViewById(R.id.tv_qc_de);
        textTypeFace.setUbuntuMedium((TextView) view.findViewById(R.id.tv_title));
        textTypeFace.setUbuntuRegular((TextView) view.findViewById(R.id.tv_content));
        textTypeFace.setUbuntuRegular((TextView) view.findViewById(R.id.tv_content_bottom));
        dialog.show();
        dialog.setCancelable(canBack);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(view);
//
        return dialog;
    }


    /**
     * * 通用的，一个按钮Dialog
     *
     * @param activity
     * @param title
     * @param content
     * @param buttonListener
     */
    public static void showOneButtonCompelDialog(final BaseActivity activity, final String title, final String content, final OnClickListener buttonListener) {
        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                final Dialog oneButtonCompelDialog = new Dialog(activity, R.style.base_dialog);
                View view = LayoutInflater.from(activity).inflate(
                        R.layout.dialog_item_common_one_btn, null);
                ((TextView) view.findViewById(R.id.tv_title)).setText(title);
                ((TextView) view.findViewById(R.id.tv_content)).setText(content);
                ((TextView) view.findViewById(R.id.tv_ok)).setText(activity.getResources().getString(R.string.ok));
                oneButtonCompelDialog.setCancelable(false);
                oneButtonCompelDialog.setCanceledOnTouchOutside(false);
                oneButtonCompelDialog.setContentView(view);
                view.findViewById(R.id.tv_ok).setOnClickListener(
                        new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                oneButtonCompelDialog.dismiss();
                                if (buttonListener != null)
                                    buttonListener.onClick(v);
                            }
                        });
                if (activity == null || activity.isFinishing())
                    return;
                oneButtonCompelDialog.show();
            }
        });

    }

    private static int showCount = 0;

    public static void commonOneTimeUpdateDialog(final BaseActivity activity,
                                                 final String title, final String content, final String leftText, final String rightText,
                                                 final OnClickListener leftListener,
                                                 final OnClickListener rghtListener) {
        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (showCount >= 1) {
                    return;
                }

                final Dialog dialog = new Dialog(activity, R.style.base_dialog);
                View view = LayoutInflater.from(activity).inflate(
                        R.layout.dialog_item_common_two_btn, null);
                ((TextView) view.findViewById(R.id.tv_title)).setText(title);
                ((TextView) view.findViewById(R.id.tv_content)).setText(content);
                ((TextView) view.findViewById(R.id.tv_left)).setText(leftText);
                ((TextView) view.findViewById(R.id.tv_right)).setText(rightText);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setContentView(view);
                dialog.setOnDismissListener(new OnDismissListener() {

                    @Override
                    public void onDismiss(DialogInterface arg0) {
                        showCount--;
                    }
                });
                view.findViewById(R.id.tv_left).setOnClickListener(
                        new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                if (leftListener != null)
                                    leftListener.onClick(v);
                            }
                        });
                view.findViewById(R.id.tv_right).setOnClickListener(
                        new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                if (rghtListener != null)
                                    rghtListener.onClick(v);
                            }
                        });
                if (activity == null || activity.isFinishing())
                    return;
                dialog.show();
                showCount++;
            }
        });

    }

    /**
     * 输入框的dialog
     *
     * @param activity
     * @param title
     * @param content
     * @param leftText
     * @param rightText
     * @param leftListener
     * @param rghtListener
     */
    public static void commonTwoBtnInputDialog(final BaseActivity activity, final Boolean balance,
                                               final String title, final String content, final String leftText, final String rightText,
                                               final OnClickListener leftListener,
                                               final OnClickListener rghtListener) {
        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                final Dialog dialog = new Dialog(activity, R.style.base_dialog);
                final View view = LayoutInflater.from(activity).inflate(
                        R.layout.dialog_input, null);
                ((TextView) view.findViewById(R.id.tv_title)).setText(title);
                ((TextView) view.findViewById(R.id.tv_content)).setText(content);
                ((TextView) view.findViewById(R.id.tv_left)).setText(leftText);
                ((TextView) view.findViewById(R.id.tv_right)).setText(rightText);
                final EditText editText = (EditText) view.findViewById(R.id.et_input);
                String inputNum = Store.getString(activity, Store.OPEN_BALANCE);

                if (!TextUtils.isEmpty(inputNum) && BH.getBD(inputNum).compareTo(BH.getBD(100000)) < 0 && balance) {
                    editText.setText(inputNum);
                }

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setContentView(view);
                view.findViewById(R.id.tv_left).setOnClickListener(
                        new OnClickListener() {

                            @Override
                            public void onClick(final View v) {
                                dialog.dismiss();
                                CommonUtil.hideSoftkeyBoard(activity);
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
                        new OnClickListener() {

                            @Override
                            public void onClick(View v) {

                                String num = editText.getText().toString();

                                if (balance) {
                                    if (TextUtils.isEmpty(num)) {
                                        Store.remove(activity, Store.OPEN_BALANCE);
                                    } else {
                                        Store.putString(activity, Store.OPEN_BALANCE, num);
                                    }
                                }
                                try {
                                    if (BH.getBD(num).compareTo(BH.getBD(100000)) > 0) {
                                        ((EditText) view.findViewById(R.id.et_input)).setText("");
                                        Store.remove(activity, Store.OPEN_BALANCE);

                                        return;
                                    }
                                } catch (Exception e) {
                                    return;
                                }
                                dialog.dismiss();
                                CommonUtil.hideSoftkeyBoard(activity);
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

    public static void showQrCodeDialog(final BaseActivity activity, String qrCodeText, final String tableName, final OnClickListener printOnClickListener) {
        try {
            String content = URLEncoder.encode(qrCodeText, "UTF-8");
            QRCodeWriter writer = new QRCodeWriter();
            Map<EncodeHintType, ErrorCorrectionLevel> map = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
            map.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
            BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE, 400, 400, map);
            final Bitmap bitmap = BitmapUtil.bitMatrix2Bitmap(matrix);
            final Dialog dialog = new Dialog(activity, R.style.qrcode_dialog);
            View view = LayoutInflater.from(activity).inflate(
                    R.layout.dialog_qrcode, null);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(view);
            ((ImageView) view.findViewById(R.id.iv_qrcode)).setImageBitmap(bitmap);
            ((TextView) view.findViewById(R.id.tv_table_name)).setText(tableName);
            view.findViewById(R.id.btn_qrcode_print).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    printOnClickListener.onClick(v);
                    dialog.dismiss();
                }
            });
            view.findViewById(R.id.btn_qrcode_back).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            view.findViewById(R.id.btn_qrcode_save).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = tableName;
                    if (TextUtils.isEmpty(tableName)) {
                        name = "Kiosk";
                    }
                    BitmapUtil.saveImageToGallery(name, activity, bitmap);
                }
            });
            dialog.show();
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    public static void showUpdateVersionDialog(final BaseActivity activity, VersionUpdate versionUpdate, final OnClickListener onClickListener, final OnClickListener nextListener) {
        try {
            final Dialog dialog = new Dialog(activity, R.style.qrcode_dialog);
            View view = LayoutInflater.from(activity).inflate(
                    R.layout.dialog_update, null);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(view);
            TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
            StringBuffer title = new StringBuffer(activity.getString(R.string.has_new_version));
            ((TextView) view.findViewById(R.id.tv_version)).setText("New Version:" + versionUpdate.getVersionName());
            ((TextView) view.findViewById(R.id.tv_description)).setText(versionUpdate.getDescription());
            view.findViewById(R.id.btn_update_now).setOnClickListener(onClickListener);
            if (versionUpdate.getForceUpdate() == 1) {
                title.append("\nPlease update now");
                view.findViewById(R.id.btn_update_later).setVisibility(View.GONE);
            } else {
                view.findViewById(R.id.btn_update_later).setVisibility(View.VISIBLE);
            }
            tv_title.setText(title.toString());
            view.findViewById(R.id.btn_update_later).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (nextListener != null) {
                        nextListener.onClick(v);
                    }

                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int compulsoryCount = 0;

    public static void compulsoryUpdateDialog(final BaseActivity activity,
                                              final String title, final String content,
                                              final OnClickListener listener) {
        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (compulsoryCount >= 1) {
                    return;
                }

                final Dialog dialog = new Dialog(activity, R.style.base_dialog);
                View view = LayoutInflater.from(activity).inflate(
                        R.layout.dialog_item_common_one_btn, null);
                ((TextView) view.findViewById(R.id.tv_title)).setText(title);
                ((TextView) view.findViewById(R.id.tv_content)).setText(content);
                ((TextView) view.findViewById(R.id.tv_ok)).setText(activity.getResources().getString(R.string.ok));
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setContentView(view);
                dialog.setOnDismissListener(new OnDismissListener() {

                    @Override
                    public void onDismiss(DialogInterface arg0) {
                        compulsoryCount--;
                    }
                });
                view.findViewById(R.id.tv_ok).setOnClickListener(
                        new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                if (listener != null)
                                    listener.onClick(v);
                            }
                        });
                if (activity == null || activity.isFinishing())
                    return;
                dialog.show();
                compulsoryCount++;
            }
        });

    }


    public static void alertDialog(final Activity activity, final String title, final String content) {
        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
                builder1.setTitle(title);
                builder1.setMessage(content);
                builder1.setCancelable(true);
                builder1.setNeutralButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                if (activity == null || activity.isFinishing())
                    return;
                alert11.show();
            }
        });

    }


    public static void changeDialogOrder(final BaseActivity activity, final String changeNum, final OnClickListener exitListener) {
        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {

                final Dialog dialog = new Dialog(activity, R.style.change_dialog);
                View view = LayoutInflater.from(activity).inflate(
                        R.layout.dialog_item_change, null);
                Window dialogWindow = dialog.getWindow();
                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                dialogWindow.setGravity(Gravity.CENTER);
                lp.y = ScreenSizeUtil.dip2px(activity, 0); // 新位置Y坐标
                dialogWindow.setAttributes(lp);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setContentView(view);
                dialog.setOwnerActivity(activity);
                TextTypeFace textTypeFace = TextTypeFace.getInstance();
                TextView tv_num = (TextView) view.findViewById(R.id.tv_change_num);
                textTypeFace.setTrajanProBlod(tv_num);
                tv_num.setText(changeNum + "");

                textTypeFace.setTrajanProBlod((TextView) view
                        .findViewById(R.id.tv_dia_change));
                view.findViewById(R.id.ll_change).setOnClickListener(
                        new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                if (exitListener != null)
                                    exitListener.onClick(v);
                            }
                        });
                if (activity == null || activity.isFinishing())
                    return;
                dialog.show();
                BaseApplication.postHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if (dialog != null && dialog.isShowing() && activity != null && !activity.isFinishing())
                            dialog.dismiss();
                    }
                }, 20 * 1000);
            }
        });

    }


    public static void topDialogOrder(final BaseActivity activity, final AppOrder tempAppOrder, final OnClickListener leftListener,
                                      final OnClickListener rghtListener) {
        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (tempAppOrder == null)
                    return;
                final Dialog dialog = new Dialog(activity, R.style.top_dialog);
                View view = LayoutInflater.from(activity).inflate(
                        R.layout.dialog_item_top_show, null);
                Window dialogWindow = dialog.getWindow();
                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                dialogWindow.setGravity(Gravity.TOP);
                lp.y = ScreenSizeUtil.dip2px(activity, 25); // 新位置Y坐标
                dialogWindow.setAttributes(lp);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setContentView(view);
                dialog.setOwnerActivity(activity);
                TextView tv_msg_info = (TextView) view.findViewById(R.id.tv_msg_info);
                tv_msg_info.setText(activity.getResources().getString(R.string.num) + tempAppOrder.getOrderCount().intValue() + "，" + activity.getResources().getString(R.string.total) + "$" + BH.getBD(tempAppOrder.getTotal()).toString() + "");
                view.findViewById(R.id.tv_left).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.tv_left).setOnClickListener(
                        new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                if (leftListener != null)
                                    leftListener.onClick(v);
                            }
                        });
                view.findViewById(R.id.tv_right).setOnClickListener(
                        new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                if (rghtListener != null)
                                    rghtListener.onClick(v);
                            }
                        });
                if (activity == null || activity.isFinishing())
                    return;
                dialog.show();
                BaseApplication.postHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if (dialog != null && dialog.isShowing() && activity != null && !activity.isFinishing())
                            dialog.dismiss();
                    }
                }, 5 * 1000);
            }
        });

    }

    public static void showSelectPrinterDialog(final BaseActivity activity, final DialogCallBack dialogCallBack, Map<Integer, PrinterDevice> map) {
        try {
            final Dialog dialog = new Dialog(activity, R.style.qrcode_dialog);
            View view = LayoutInflater.from(activity).inflate(
                    R.layout.dialog_printer_list, null);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(view);

            Collection<PrinterDevice> valueCollection = map.values();
            final List<PrinterDevice> list = new ArrayList<PrinterDevice>(valueCollection);

            ListView listView = (ListView) view.findViewById(R.id.lv_printer);
            PrinterAdapter printerAdapter = new PrinterAdapter(activity, list);
            listView.setAdapter(printerAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (ButtonClickTimer.canClick()) {
                        PrinterDevice printerDevice = list.get(position);
                        dialogCallBack.callBack(printerDevice);
                        dialog.dismiss();
                    }
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface DialogCallBack {
        void callBack(PrinterDevice printerDevice);
    }

    private static class PrinterAdapter extends BaseAdapter {
        private List<PrinterDevice> list = new ArrayList<PrinterDevice>();
        private LayoutInflater inflater;

        public PrinterAdapter(Context context, List<PrinterDevice> list) {
            this.list = list;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.printer_item, null);
                holder = new ViewHolder();
                holder.tv_printer_name = (TextView) convertView.findViewById(R.id.tv_printer_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            PrinterDevice printerDevice = list.get(position);
            holder.tv_printer_name.setText(TextUtils.isEmpty(printerDevice.getPrinterName()) ? printerDevice.getName() : printerDevice.getPrinterName() + "\nIP:" + printerDevice.getIP());
            return convertView;
        }

        class ViewHolder {
            TextView tv_printer_name;
        }
    }

}
