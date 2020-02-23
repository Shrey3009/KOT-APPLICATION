package com.alfredbase.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alfredbase.BaseActivity;
import com.alfredbase.BaseApplication;
import com.alfredbase.R;
import com.alfredbase.global.BugseeHelper;
import com.alfredbase.javabean.model.PrinterDevice;
import com.alfredbase.javabean.system.VersionUpdate;
import com.alfredbase.javabean.temporaryforapp.AppOrder;
import com.alfredbase.store.Store;
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

public class DialogFactory {


    public static void commonTwoBtnDialog(BaseActivity activity,
                                          String title, String content,
                                          String leftText, String rightText,
                                          OnClickListener leftListener,
                                          OnClickListener rghtListener) {
        commonTwoBtnDialog(activity, title, content, leftText, rightText, leftListener, rghtListener, false);
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
                                BugseeHelper.buttonClicked(leftText);
                                if (leftListener != null)
                                    leftListener.onClick(v);
                            }
                        });
                view.findViewById(R.id.tv_right).setOnClickListener(
                        new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                BugseeHelper.buttonClicked(rightText);
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
    public static void commonTwoBtnTimeDialog(final BaseActivity activity,
                                              final String title, final String time, final String content, final String leftText, final String rightText,
                                              final OnClickListener leftListener,
                                              final OnClickListener rghtListener, final boolean canBack) {
        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                final Dialog dialog = new Dialog(activity, R.style.base_dialog);
                View view = LayoutInflater.from(activity).inflate(
                        R.layout.dialog_item_common_two_time_btn, null);
                ((TextView) view.findViewById(R.id.tv_title)).setText(title);
                ((TextView) view.findViewById(R.id.tv_content)).setText(content);
                ((TextView) view.findViewById(R.id.tv_content_time)).setText(time);
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
                                BugseeHelper.buttonClicked(leftText);
                                if (leftListener != null)
                                    leftListener.onClick(v);
                            }
                        });
                view.findViewById(R.id.tv_right).setOnClickListener(
                        new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                BugseeHelper.buttonClicked(rightText);
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


    /**
     * 通用的，两按钮Dialog 显示二维码
     *
     * @param activity
     * @param leftText
     * @param rightText
     * @param leftListener
     * @param rghtListener
     */
    public static void commonTwoBtnQRDialog(final BaseActivity activity,
                                            final String url, final String leftText, final String rightText,
                                            final OnClickListener leftListener,
                                            final OnClickListener rghtListener) {
        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                final Dialog dialog = new Dialog(activity, R.style.base_dialog);
                View view = LayoutInflater.from(activity).inflate(
                        R.layout.dialog_item_common_qr_two_btn, null);
                QRCodeWriter writer = new QRCodeWriter();
                Map<EncodeHintType, Object> map = new HashMap<>();
                map.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
                map.put(EncodeHintType.MARGIN, 1);
                BitMatrix matrix = null;
                try {
                    matrix = writer.encode(url, BarcodeFormat.QR_CODE, 200, 200, map);
                    final Bitmap bitmap = BitmapUtil.bitMatrix2Bitmap(matrix);
                    ((ImageView) view.findViewById(R.id.iv_content)).setImageBitmap(bitmap);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                ((TextView) view.findViewById(R.id.tv_left)).setText(leftText);
                ((TextView) view.findViewById(R.id.tv_right)).setText(rightText);
                final EditText editText = (EditText) view.findViewById(R.id.et_reference_num);
                editText.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editText.setFocusable(true);
                        editText.setFocusableInTouchMode(true);
                        editText.requestFocus();
                        CommonUtil.inputMethodSet(activity);
                    }
                });

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setContentView(view);
                view.findViewById(R.id.tv_left).setOnClickListener(
                        new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                BugseeHelper.buttonClicked(leftText);
                                if (leftListener != null)
                                    leftListener.onClick(v);
                            }
                        });
                view.findViewById(R.id.tv_right).setOnClickListener(
                        new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                BugseeHelper.buttonClicked(rightText);
                                String num = editText.getText().toString();
                                if (TextUtils.isEmpty(num)) {
                                    Toast toast = new Toast(activity);
                                    LayoutInflater inflater = activity.getLayoutInflater();
                                    View view = inflater.inflate(R.layout.toast_view, null);
                                    TextView tv_toast_view = (TextView) view.findViewById(R.id.tv_toast_view);
                                    tv_toast_view.setText(activity.getResources().getString(R.string.please_key_in_reference_number));
                                    TextTypeFace textTypeFace = TextTypeFace.getInstance();
                                    textTypeFace.setTrajanProRegular(tv_toast_view);
                                    toast.setGravity(Gravity.CENTER, 0, 10);
                                    toast.setDuration(Toast.LENGTH_LONG);
                                    toast.setView(view);
                                    toast.show();
                                    return;
                                } else {
                                    v.setTag(num);
                                }
                                dialog.dismiss();
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


    public static Dialog qcDialog(final BaseActivity activity,
                                  final String title, final String content, int drawableId, final Boolean isqc,

                                  final OnClickListener backListener, final boolean canBack) {
//		activity.runOnUiThread(new Runnable() {
//
//			@Override
//			public void run() {
        final Dialog dialog = new Dialog(activity, R.style.kpm_dialog);
        View view = LayoutInflater.from(activity).inflate(
                R.layout.dialog_item_kpm_qr_btns, null);
        ((TextView) view.findViewById(R.id.tv_title)).setText(title);
//        ((TextView) view.findViewById(R.id.tv_content)).setText(content);
        ImageView img = (ImageView) view.findViewById(R.id.img_center);
        TextView tv_qc_de = (TextView) view.findViewById(R.id.tv_qc_de);
        img.setImageResource(drawableId);
        if (isqc) {
            tv_qc_de.setVisibility(View.VISIBLE);
        } else {
            tv_qc_de.setVisibility(View.INVISIBLE);
        }
//				((TextView) view.findViewById(R.id.tv_left)).setText(leftText);
//				((TextView) view.findViewById(R.id.tv_right)).setText(rightText);
        dialog.show();
        dialog.setCancelable(canBack);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(view);

        view.findViewById(R.id.tv_backs).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        BugseeHelper.buttonClicked("back");
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


//    public static Dialog kpmFDialog(final BaseActivity activity,
//                                       final String title, final String content, int drawableId,
//
//                                       final OnClickListener backListener, final boolean canBack) {
////		activity.runOnUiThread(new Runnable() {
////
////			@Override
////			public void run() {
//        final Dialog dialog = new Dialog(activity, R.style.kpm_dialog);
//        View view = LayoutInflater.from(activity).inflate(
//                R.layout.dialog_item_kpm_f_btns, null);
//
////        ((TextView) view.findViewById(R.id.tv_content)).setText(content);
//        ImageView img = (ImageView) view.findViewById(R.id.img_center);
//
//        img.setImageResource(drawableId);
//
////				((TextView) view.findViewById(R.id.tv_left)).setText(leftText);
////				((TextView) view.findViewById(R.id.tv_right)).setText(rightText);
//        dialog.show();
//        dialog.setCancelable(canBack);
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.setContentView(view);
//
//
//        view.findViewById(R.id.tv_backs).setOnClickListener(
//                new OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                        if (backListener != null)
//                            backListener.onClick(v);
//                    }
//                });
////        if (activity == null || activity.isFinishing())
////            return;
//
////			}
////		});
//        return dialog;
//    }
//


    public static Dialog kpmTipsDialog(final BaseActivity activity,
                                       final String title, final String content, int drawableId,

                                       final OnClickListener backListener, final boolean canBack) {
//		activity.runOnUiThread(new Runnable() {
//
//			@Override
//			public void run() {
        final Dialog dialog = new Dialog(activity, R.style.kpm_dialog);
        View view = LayoutInflater.from(activity).inflate(
                R.layout.dialog_item_kpm_tip_btns, null);
        ((TextView) view.findViewById(R.id.tv_title)).setText(title);
//        ((TextView) view.findViewById(R.id.tv_content)).setText(content);
        ImageView img = (ImageView) view.findViewById(R.id.img_center);
        TextView tv_qc_de = (TextView) view.findViewById(R.id.tv_qc_de);
        img.setImageResource(drawableId);

//				((TextView) view.findViewById(R.id.tv_left)).setText(leftText);
//				((TextView) view.findViewById(R.id.tv_right)).setText(rightText);
        dialog.show();
        dialog.setCancelable(canBack);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(view);


        view.findViewById(R.id.tv_backs).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
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


    public static Dialog kpmCompleteDialog(final BaseActivity activity,
                                           final String title, final String content, String contentbottom, int drawableId,

                                           final boolean canBack) {
//		activity.runOnUiThread(new Runnable() {
//
//			@Override
//			public void run() {
        final Dialog dialog = new Dialog(activity, R.style.kpm_dialog);
        View view = LayoutInflater.from(activity).inflate(
                R.layout.dialog_item_kpm_complete_btns, null);
        ((TextView) view.findViewById(R.id.tv_title)).setText(title);
        ((TextView) view.findViewById(R.id.tv_content)).setText(content);
        TextView contentbottoms = (TextView) view.findViewById(R.id.tv_content_bottom);
        contentbottoms.setText(contentbottom);
        ImageView img = (ImageView) view.findViewById(R.id.img_center);
        img.setImageResource(drawableId);
        ((TextView) view.findViewById(R.id.tv_content)).setText(content);

        TextView tv_qc_de = (TextView) view.findViewById(R.id.tv_qc_de);

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
                                BugseeHelper.buttonClicked("Ok");
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
                                BugseeHelper.buttonClicked(leftText);
                                dialog.dismiss();
                                if (leftListener != null)
                                    leftListener.onClick(v);
                            }
                        });
                view.findViewById(R.id.tv_right).setOnClickListener(
                        new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                BugseeHelper.buttonClicked(rightText);
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
     * 输入框的dialog(整数)
     *
     * @param activity
     * @param title
     * @param content
     * @param leftText
     * @param rightText
     * @param leftListener
     * @param rghtListener
     */
    public static void commonTwoBtnInputIntDialog(final BaseActivity activity, final Boolean balance,
                                                  final String title, final String content, final String leftText, final String rightText,
                                                  final OnClickListener leftListener,
                                                  final OnClickListener rghtListener) {
        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                final Dialog dialog = new Dialog(activity, R.style.base_dialog);
                final View view = LayoutInflater.from(activity).inflate(
                        R.layout.dialog_input_int, null);
                ((TextView) view.findViewById(R.id.tv_title)).setText(title);
                ((TextView) view.findViewById(R.id.tv_content)).setText(content);
                ((TextView) view.findViewById(R.id.tv_left)).setText(leftText);
                ((TextView) view.findViewById(R.id.tv_right)).setText(rightText);
                final EditText editText = (EditText) view.findViewById(R.id.et_input);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setContentView(view);
                view.findViewById(R.id.tv_left).setOnClickListener(
                        new OnClickListener() {

                            @Override
                            public void onClick(final View v) {
                                dialog.dismiss();
                                BugseeHelper.buttonClicked(leftText);
                                //  CommonUtil.hideSoftkeyBoard(activity);
                                //   hintKeyBoard(activity);
                                BaseApplication.postHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (leftListener != null)
                                            leftListener.onClick(v);
                                        CommonUtil.hideSoftkeyBoard(activity);
                                    }
                                }, 500);
                            }
                        });
                view.findViewById(R.id.tv_right).setOnClickListener(
                        new OnClickListener() {

                            @Override
                            public void onClick(View v) {

                                BugseeHelper.buttonClicked(rightText);
                                String num = editText.getText().toString();

                                if (balance) {
                                    if (TextUtils.isEmpty(num)) {
                                        Store.remove(activity, Store.OPEN_BALANCE);
                                    } else {
                                        Store.putString(activity, Store.OPEN_BALANCE, num);
                                    }
                                }
                                try {

                                } catch (Exception e) {
                                    return;
                                }
                                dialog.dismiss();
                                CommonUtil.hideSoftkeyBoard(activity);
                                //   hintKeyBoard(activity);

                                BaseApplication.postHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (rghtListener != null)
                                            rghtListener.onClick(editText);
                                        CommonUtil.hideSoftkeyBoard(activity);
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

                if (!TextUtils.isEmpty(inputNum) && BH.getBD(inputNum).compareTo(BH.getBD("100000000000")) < 0 && balance) {
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
                                BugseeHelper.buttonClicked(leftText);
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

                                BugseeHelper.buttonClicked(rightText);
                                String num = editText.getText().toString();
                                BugseeHelper.event("num : " + num);

                                if (balance) {
                                    if (TextUtils.isEmpty(num)) {
                                        Store.remove(activity, Store.OPEN_BALANCE);
                                    } else {
                                        Store.putString(activity, Store.OPEN_BALANCE, num);
                                    }
                                }
                                try {
                                    if (BH.getBD(num).compareTo(BH.getBD("100000000000")) > 0) {
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

    public static void showQrCodeDialog(final BaseActivity activity, String qrCodeText, final String tableName, boolean isEnCoding, final OnClickListener printOnClickListener) {
        try {
            String content = qrCodeText;
            if (isEnCoding) {
                content = URLEncoder.encode(qrCodeText, "UTF-8");
            }
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
                    BugseeHelper.buttonClicked(v);
                    printOnClickListener.onClick(v);
                    dialog.dismiss();
                }
            });
            view.findViewById(R.id.btn_qrcode_back).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    BugseeHelper.buttonClicked(v);
                    dialog.dismiss();
                }
            });
            view.findViewById(R.id.btn_qrcode_save).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    BugseeHelper.buttonClicked(v);
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
            ((TextView) view.findViewById(R.id.tv_version)).setText(activity.getString(R.string.new_version)+" : " + versionUpdate.getVersionName());
            ((TextView) view.findViewById(R.id.tv_description)).setText(versionUpdate.getDescription());
            view.findViewById(R.id.btn_update_now).setOnClickListener(onClickListener);
            if (versionUpdate.getForceUpdate() == 1) {
                title.append("\n"+activity.getResources().getString(R.string.please_update_now));
                view.findViewById(R.id.btn_update_later).setVisibility(View.GONE);
            } else {
                view.findViewById(R.id.btn_update_later).setVisibility(View.VISIBLE);
            }
            tv_title.setText(title.toString());
            view.findViewById(R.id.btn_update_later).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    BugseeHelper.buttonClicked(v);
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
                                BugseeHelper.buttonClicked(v);
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
                                BugseeHelper.buttonClicked(v);
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
                                BugseeHelper.buttonClicked(v);
                                if (leftListener != null)
                                    leftListener.onClick(v);
                            }
                        });
                view.findViewById(R.id.tv_right).setOnClickListener(
                        new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                BugseeHelper.buttonClicked(v);
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
                        BugseeHelper.buttonClicked(view);
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

    public static void hintKeyBoard(BaseActivity parent) {
        //拿到 InputMethodManager
        InputMethodManager imm = (InputMethodManager) parent.getSystemService(Context.INPUT_METHOD_SERVICE); //如果window上view获取焦点 && view不为空
        if (imm.isActive() && parent.getCurrentFocus() != null) {
            //拿到view的token 不为空
            if (parent.getCurrentFocus().getWindowToken() != null) { //表示软键盘窗口总是隐藏，除非开始时以SHOW_FORCED显示。
                imm.hideSoftInputFromWindow(parent.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
}
