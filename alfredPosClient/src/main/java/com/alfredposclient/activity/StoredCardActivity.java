package com.alfredposclient.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.LoadingDialog;
import com.alfredbase.global.BugseeHelper;
import com.alfredbase.http.ResultCode;
import com.alfredbase.utils.BH;
import com.alfredbase.utils.DialogFactory;
import com.alfredbase.utils.IntegerUtils;
import com.alfredbase.utils.RxBus;
import com.alfredbase.utils.ScreenSizeUtil;
import com.alfredbase.utils.TextTypeFace;
import com.alfredbase.view.NumerickeyboardForStoredCard;
import com.alfredposclient.R;
import com.alfredposclient.global.App;
import com.alfredposclient.global.SyncCentre;
import com.alfredposclient.global.UIHelp;
import com.alfredposclient.zxing.IntentSource;
import com.alfredposclient.zxing.camera.CameraManager;
import com.alfredposclient.zxing.control.BeepManager;
import com.alfredposclient.zxing.decode.CaptureActivityHandler;
import com.alfredposclient.zxing.decode.InactivityTimer;
import com.alfredposclient.zxing.view.ViewfinderView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Alex on 16/9/28.
 */

public class StoredCardActivity extends BaseActivity implements SurfaceHolder.Callback, NumerickeyboardForStoredCard.KeyBoardClickListener {

    private static final String TAG = StoredCardActivity.class.getSimpleName();

    public static final int REGIST_STOREDCARD_SUCCEED = 101;
    //    public static final int REGIST_STOREDCARD_FAILURE = -100;
    public static final int REPLACEMENT_STOREDCARD_SUCCEED = 102;
    public static final int PAID_STOREDCARD_SUCCEED = 155;
    public static final int CHANGE_STOREDCARD_SUCCEED = 104;
    public static final int QUERYBALANCE_STOREDCARD_SUCCEED = 105;
    //    public static final int REPLACEMENT_STOREDCARD_FAILURE = -101;
    public static final int HTTP_FAILURE = -156;


    //    public static final int VIEW_EVENT_STORED_CARD_REFUND = 150;
//    public static final int VIEW_EVENT_STORED_CARD_LOSS = 151;
//    public static final int VIEW_EVENT_STORED_CARD_REPLACEMEN = 152;
    public static final int VIEW_EVENT_STORED_CARD_PAY = 153;
//    public static final int VIEW_EVENT_STORED_CARD_PAY_RESULT = 154;

    private LoadingDialog loadingDialog;
    private BaseActivity mainPage;
    private LinearLayout ll_stored_card_action;
    private RelativeLayout rl_stored_card_title;
    private RelativeLayout rl_stored_cart_root;
    private LinearLayout ll_stored_card_scan;
    private CameraManager cameraManager;
    // 声音、震动控制
    private BeepManager beepManager;
    // 电量控制
    private InactivityTimer inactivityTimer;
    private CaptureActivityHandler captureActivityHandler;
    private Collection<BarcodeFormat> decodeFormats;
    private Map<DecodeHintType, ?> decodeHints;
    private String characterSet;
    private boolean hasSurface;
    private ViewfinderView viewfinderView;
    private IntentSource source;
    private SurfaceView surfaceView;
    private boolean isShowedScaner = false;
    private boolean isFontShow = true;
    private int selectActionViewId;
    private String QRcodeString;
    private TextView tv_stored_card_no;
    private NumerickeyboardForStoredCard numerickeyboard;

    private EditText et_first_name;
    private EditText et_phone;
    private EditText et_last_name;
    private EditText et_email;
    private EditText et_address;

    private EditText et_replacement_card_no;
    private EditText et_replacement_phone;

    //    private EditText et_query_card_no;
    private EditText et_query_phone;

    private TextView tv_store_card_value;
//    private Handler mainPageHandler;

    private boolean isPayAction = false;
    private String amount;
    private Button btn_paid_refund;
    private Button btn_binding;
    private Button btn_clear;
    private Button btn_rebinding;
    private TextTypeFace textTypeFace;

    private boolean useUSBScanner = false;
    private Observable<String> observable;

    @Override
    protected void initView() {
        mainPage = context;
        setContentView(R.layout.activity_stored_card);
        textTypeFace = TextTypeFace.getInstance();
        textTypeFace.init(context);
        loadingDialog = new LoadingDialog(mainPage);
        loadingDialog.setTitle(context.getString(R.string.loading));
        hasSurface = false;
        ll_stored_card_action = (LinearLayout) findViewById(R.id.ll_stored_card_action);
        rl_stored_card_title = (RelativeLayout) findViewById(R.id.rl_stored_card_title);
        rl_stored_cart_root = (RelativeLayout) findViewById(R.id.rl_stored_cart_root);
        ll_stored_card_scan = (LinearLayout) findViewById(R.id.ll_stored_card_scan);
        surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        tv_stored_card_no = (TextView) findViewById(R.id.tv_stored_card_no);
        et_first_name = (EditText) findViewById(R.id.et_first_name);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_last_name = (EditText) findViewById(R.id.et_last_name);
        et_email = (EditText) findViewById(R.id.et_email);
        et_address = (EditText) findViewById(R.id.et_address);
        et_replacement_card_no = (EditText) findViewById(R.id.et_replacement_card_no);
        et_replacement_phone = (EditText) findViewById(R.id.et_replacement_phone);
//        et_query_card_no = (EditText) findViewById(R.id.et_query_card_no);
        et_query_phone = (EditText) findViewById(R.id.et_query_phone);
        tv_store_card_value = (TextView) findViewById(R.id.tv_store_card_value);
        numerickeyboard = (NumerickeyboardForStoredCard) findViewById(R.id.numerickeyboard);
        numerickeyboard.setKeyBoardClickListener(this);

        btn_paid_refund = (Button) findViewById(R.id.btn_paid_refund);
        btn_binding = (Button) findViewById(R.id.btn_binding);
        btn_clear = (Button) findViewById(R.id.btn_clear);
        btn_rebinding = (Button) findViewById(R.id.btn_rebinding);
        btn_binding.setOnClickListener(this);
        btn_paid_refund.setOnClickListener(this);
        btn_clear.setOnClickListener(this);
        btn_rebinding.setOnClickListener(this);
        TextView tv_refund = (TextView) findViewById(R.id.tv_refund);
        TextView tv_paid = (TextView) findViewById(R.id.tv_paid);
        textTypeFace.setTrajanProRegular(tv_refund);
        textTypeFace.setTrajanProRegular(tv_paid);
        findViewById(R.id.iv_stored_card_scan).setOnClickListener(this);
        findViewById(R.id.btn_cancel_scan).setOnClickListener(this);
        findViewById(R.id.btn_change).setOnClickListener(this);
        findViewById(R.id.btn_binding_ok).setOnClickListener(this);
        findViewById(R.id.btn_clear_info).setOnClickListener(this);
        tv_refund.setOnClickListener(this);
        tv_paid.setOnClickListener(this);
        findViewById(R.id.btn_replacement_ok).setOnClickListener(this);
        findViewById(R.id.rl_query_ok).setOnClickListener(this);
        selectActionViewId = R.id.btn_paid_refund;
        refreshActionView();
        isPayAction = getIntent().getBooleanExtra("isPayAction", false);
        if (App.instance.isSUNMIShow()) {
            observable = RxBus.getInstance().register(RxBus.RX_MSG_2);
            observable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
                @Override
                public void call(String object) {
                    if (useUSBScanner && object != null) {
                        useUSBScanner = false;
                        handleDecode(new Result(object, null, null, null));
                    }
                }
            });
        } else {
            beepManager = new BeepManager(mainPage);
        }
        if (!isPayAction) {
            ll_stored_card_action.setVisibility(View.VISIBLE);
            hiddenScanView();
            isFontShow = true;
            showActionView();
        } else {
            isFontShow = true;
            amount = getIntent().getStringExtra("amount");
            findViewById(R.id.ll_stored_card_scan).setVisibility(View.VISIBLE);
            if (App.instance.isSUNMIShow()) {
                findViewById(R.id.rl_camera_scanner).setVisibility(View.GONE);
                findViewById(R.id.tv_usb_scanner).setVisibility(View.VISIBLE);
                if (App.instance.isUsbScannerLink()) {
                    useUSBScanner = true;
//                    UIHelp.showShortToast(context, "Please Scan the QR Coder");
                } else {
                    UIHelp.showShortToast(context, getString(R.string.scan_qrcode));
                }
            } else {
                findViewById(R.id.rl_camera_scanner).setVisibility(View.VISIBLE);
                findViewById(R.id.tv_usb_scanner).setVisibility(View.GONE);
                showScanner(true);
            }
        }
        tv_store_card_value.setText("");

        RelativeLayout.LayoutParams ps = new RelativeLayout.LayoutParams((int) (ScreenSizeUtil.width * 2 / 3),
                (int) (ScreenSizeUtil.height * 2 / 3 + ScreenSizeUtil.dip2px(this, 60)));
        ps.addRule(RelativeLayout.CENTER_IN_PARENT);
        rl_stored_cart_root.setLayoutParams(ps);

    }


    private void refreshActionView() {
        btn_paid_refund.setBackgroundColor(getResources().getColor(R.color.white));
        btn_binding.setBackgroundColor(getResources().getColor(R.color.white));
        btn_clear.setBackgroundColor(getResources().getColor(R.color.white));
        btn_rebinding.setBackgroundColor(getResources().getColor(R.color.white));
        switch (selectActionViewId) {
            case R.id.btn_paid_refund:
                btn_paid_refund.setBackgroundColor(getResources().getColor(R.color.light_gray));
                break;
            case R.id.btn_binding:
                btn_binding.setBackgroundColor(getResources().getColor(R.color.light_gray));
                break;
            case R.id.btn_clear:
                btn_clear.setBackgroundColor(getResources().getColor(R.color.light_gray));
                break;
            case R.id.btn_rebinding:
                btn_rebinding.setBackgroundColor(getResources().getColor(R.color.light_gray));
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public CaptureActivityHandler getHandler() {
        return captureActivityHandler;
    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

//    public void setMainPageHandler(Handler mainPageHandler) {
//        this.mainPageHandler = mainPageHandler;
//    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    private void showScanner(boolean flag) {

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

        } else {

        }
        if (cameraManager != null)
            cameraManager.closeDriver();
        cameraManager = new CameraManager(App.instance);
        viewfinderView.setCameraManager(cameraManager);
        captureActivityHandler = null;
        final SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            // activity在paused时但不会stopped,因此surface仍旧存在；
            // surfaceCreated()不会调用，因此在这里初始化camera
            initCamera(surfaceHolder, flag);
        } else {
            // 重置callback，等待surfaceCreated()来初始化camera
            surfaceHolder.addCallback(this);
        }
        beepManager.updatePrefs();
        inactivityTimer = new InactivityTimer(mainPage);
        inactivityTimer.onResume();
        source = IntentSource.NONE;
        decodeFormats = null;
        characterSet = null;
        isShowedScaner = true;
//        initCamera(surfaceHolder, flag);
        isFontShow = !flag;
    }


    private void hiddenScanView() {
        ll_stored_card_scan.setVisibility(View.GONE);
        if (isShowedScaner) {
            if (captureActivityHandler != null) {
                captureActivityHandler.quitSynchronously();
                captureActivityHandler = null;
            }
            if (beepManager != null)
                beepManager.close();
            if (inactivityTimer != null) {
                inactivityTimer.onPause();
//                inactivityTimer.shutdown();
                inactivityTimer = null;
            }
            if (cameraManager != null) {
                cameraManager.closeDriver();
            }
            if (!hasSurface) {
                SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
                SurfaceHolder surfaceHolder = surfaceView.getHolder();
                surfaceHolder.removeCallback(this);
            }
            isShowedScaner = false;
        }
    }

//    public void setPayAction(boolean payAction, String amount) {
//        this.isPayAction = payAction;
//        this.amount = amount;
//    }

//    @Override
//    public void onHiddenChanged(boolean hidden) {
//
//        if(!hidden){
//            if(!isPayAction) {
//                ll_stored_card_action.setVisibility(View.VISIBLE);
//                hiddenScanView();
////            Window window = getActivity().getWindow();
////            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//                isFontShow = true;
//                showActionView();
//            }else{
//                isFontShow = true;
//                findViewById(R.id.ll_stored_card_scan).setVisibility(View.VISIBLE);
//                showScanner(true);
//            }
//            tv_store_card_value.setText("");
//        }else{
//            isPayAction = false;
//            if(isShowedScaner) {
//                if (captureActivityHandler != null) {
//                    captureActivityHandler.quitSynchronously();
//                    captureActivityHandler = null;
//                }
//                if (beepManager != null)
//                    beepManager.close();
//                if (inactivityTimer != null) {
//                    inactivityTimer.onPause();
//                    inactivityTimer.shutdown();
//                    inactivityTimer = null;
//                }
//                if (cameraManager != null) {
//                    cameraManager.closeDriver();
//                }
//                if (!hasSurface) {
//                    SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
//                    SurfaceHolder surfaceHolder = surfaceView.getHolder();
//                    surfaceHolder.removeCallback(this);
//                }
//            }
//            selectActionViewId = R.id.btn_paid_refund;
//        }
//        super.onHiddenChanged(hidden);
//    }


    private void showActionView() {
        QRcodeString = "";
        View ll_stored_card_user = findViewById(R.id.ll_stored_card_user);
        View rl_stored_card_paid = findViewById(R.id.rl_stored_card_paid);
        View rl_stored_card_query = findViewById(R.id.rl_stored_card_query);
        View rl_stored_card_replacement = findViewById(R.id.rl_stored_card_replacement);
        View ll_stored_card_info = findViewById(R.id.ll_stored_card_info);
        ll_stored_card_user.setVisibility(View.GONE);
        rl_stored_card_paid.setVisibility(View.GONE);
        rl_stored_card_query.setVisibility(View.GONE);
        rl_stored_card_replacement.setVisibility(View.GONE);
        ll_stored_card_info.setVisibility(View.VISIBLE);
        tv_stored_card_no.setText(QRcodeString);
        switch (selectActionViewId) {
            case R.id.btn_binding:
                ll_stored_card_user.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_paid_refund:
                rl_stored_card_paid.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_clear:
                rl_stored_card_query.setVisibility(View.VISIBLE);
//                ll_stored_card_info.setVisibility(View.INVISIBLE);
//              et_query_card_no
                break;
            case R.id.btn_rebinding:
                rl_stored_card_replacement.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (inactivityTimer != null) {
            inactivityTimer.onResume();
        }
    }

    @Override
    public void onPause() {
        useUSBScanner = false;
        if (isShowedScaner) {
            if (captureActivityHandler != null) {
                captureActivityHandler.quitSynchronously();
                captureActivityHandler = null;
            }
            if (beepManager != null)
                beepManager.close();
            if (inactivityTimer != null) {
                inactivityTimer.onPause();

            }
            if (cameraManager != null)
                cameraManager.closeDriver();
            if (!hasSurface) {
                SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
                SurfaceHolder surfaceHolder = surfaceView.getHolder();
                surfaceHolder.removeCallback(this);
            }
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (inactivityTimer != null) {
//            inactivityTimer.onPause();
            inactivityTimer.shutdown();
            inactivityTimer = null;
        }

        if (observable != null) {
            RxBus.getInstance().unregister(RxBus.RX_MSG_2, observable);
        }
        super.onDestroy();
    }

    private void initCamera(SurfaceHolder surfaceHolder, boolean b) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            return;
        }
        try {
            // 打开Camera硬件设备

            if (b) {
                cameraManager.openFrontCamera(surfaceHolder);
            } else {
                cameraManager.openBackCamera(surfaceHolder);
            }
//			cameraManager.openDriver(surfaceHolder);
            // 创建一个captureActivityHandler来打开预览，并抛出一个运行时异常
            if (captureActivityHandler == null) {
                captureActivityHandler = new CaptureActivityHandler(this, decodeFormats,
                        decodeHints, characterSet, cameraManager);
            }
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            Log.w(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }

    }


    /**
     * 显示底层错误信息并退出应用
     */
    private void displayFrameworkBugMessageAndExit() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle("警告");
//        builder.setMessage("抱歉，相机出现问题，您可能需要重启设备");
//        builder.setPositiveButton("确定", new FinishListener(getActivity()));
//        builder.setOnCancelListener(new FinishListener(getActivity()));
//        builder.show();
        DialogFactory.commonTwoBtnDialog(mainPage, mainPage.getResources().getString(R.string.warning),
                context.getString(R.string.camera_error),
                mainPage.getResources().getString(R.string.cancel),
                mainPage.getResources().getString(R.string.ok),
                null,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mainPage.finish();
                    }
                });
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (loadingDialog != null && loadingDialog.isShowing()) {
                loadingDialog.dismiss();
            }
            switch (msg.what) {
                case ResultCode.CONNECTION_FAILED:
                    loadingDialog.dismiss();
                    UIHelp.showToast(mainPage, ResultCode.getErrorResultStr(mainPage,
                            (Throwable) msg.obj, mainPage.getResources().getString(R.string.server)));
                    break;
                case REGIST_STOREDCARD_SUCCEED:
                    clearRegistInfo();
                    UIHelp.showShortToast(mainPage, context.getString(R.string.register_success));
                    break;
                case PAID_STOREDCARD_SUCCEED:
                    UIHelp.showShortToast(mainPage, context.getString(R.string.payment_success));
                    tv_store_card_value.setText("");
                    break;
                case CHANGE_STOREDCARD_SUCCEED:
                    UIHelp.showShortToast(mainPage, context.getString(R.string.card_change_success));
                    break;
                case QUERYBALANCE_STOREDCARD_SUCCEED:
                    DialogFactory.showOneButtonCompelDialog(context, "", context.getString(R.string.query_balance) + " : " + App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.getBD((String) msg.obj).toString(), null);
                    et_query_phone.setText("");
                    clearRegistInfo();
                    break;
                case HTTP_FAILURE:
                    UIHelp.showShortToast(mainPage, ResultCode.getErrorResultStrByCode(mainPage,
                            (Integer) msg.obj, mainPage.getResources().getString(R.string.server)));
                    break;
            }
        }
    };

    private void clearRegistInfo() {
        et_first_name.setText("");
        et_phone.setText("");
        et_last_name.setText("");
        et_email.setText("");
        et_address.setText("");
        tv_stored_card_no.setText("");
        QRcodeString = null;
    }

    public void storedCardRefund() {
        String value = tv_store_card_value.getText().toString();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("qrCode", QRcodeString);
        map.put("revenueId", App.instance.getRevenueCenter().getId().intValue());
        map.put("consumeAmount", value);
        map.put("operateType", 3);
        loadingDialog.show();
        SyncCentre.getInstance().updateStoredCardValue(mainPage, map, handler);
    }

    public void storedCardQueryBalance() {
        String query_phone = et_query_phone.getText().toString().trim();
//        String query_card_no = et_query_card_no.getText().toString().trim();
        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("cardId", query_card_no);
        map.put("phone", query_phone);
        map.put("revenueId", App.instance.getRevenueCenter().getId().intValue());
        map.put("qrCode", QRcodeString);
        loadingDialog.show();
        SyncCentre.getInstance().queryStoredCardBalance(mainPage, map, handler);
    }

    public void storedCardReplacement() {
        String replacement_phone = et_replacement_phone.getText().toString().trim();
        String replacement_card_no = et_replacement_card_no.getText().toString().trim();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cardId", replacement_card_no);
        map.put("revenueId", App.instance.getRevenueCenter().getId().intValue());
        map.put("qrCode", QRcodeString);
        map.put("phone", replacement_phone);
        loadingDialog.show();
        SyncCentre.getInstance().changeStoredCard(mainPage, map, handler);
    }

    @Override
    protected void handlerClickEvent(View v) {
        switch (v.getId()) {
            case R.id.btn_binding:
            case R.id.btn_paid_refund:
            case R.id.btn_clear:
            case R.id.btn_rebinding:
                selectActionViewId = v.getId();
                refreshActionView();
                showActionView();
                break;
            case R.id.iv_stored_card_scan:
                isFontShow = true;

                if (App.instance.isSUNMIShow()) {
                    if (App.instance.isUsbScannerLink()) {
                        useUSBScanner = true;
                        UIHelp.showShortToast(context, context.getString(R.string.scan_qrcode));
                    } else {
                        UIHelp.showShortToast(context, context.getString(R.string.input_scanner));
                    }
                } else {
                    findViewById(R.id.ll_stored_card_scan).setVisibility(View.VISIBLE);
                    showScanner(isFontShow);
                }
                break;
            case R.id.btn_change:
                if (App.instance.isSUNMIShow()) {
                    if (App.instance.isUsbScannerLink()) {
                        useUSBScanner = true;
                        UIHelp.showShortToast(context, context.getString(R.string.scan_qrcode));
                    } else {
                        UIHelp.showShortToast(context, context.getString(R.string.input_scanner));
                    }
                } else {
                    showScanner(isFontShow);
                }
                break;
            case R.id.btn_cancel_scan:
                if (isPayAction) {
                    this.finish();
                } else {
                    hiddenScanView();
                }

                break;
            /*
            findViewById(R.id.btn_ok).setOnClickListener(this);
        findViewById(R.id.btn_clear_info).setOnClickListener(this);
        findViewById(R.id.tv_refund).setOnClickListener(this);
        findViewById(R.id.tv_paid).setOnClickListener(this);
        findViewById(R.id.btn_replacement_ok).setOnClickListener(this);
        findViewById(R.id.rl_query_ok).setOnClickListener(this);
             */
            case R.id.btn_binding_ok: {
                if (TextUtils.isEmpty(QRcodeString)) {
                    UIHelp.showShortToast(mainPage, mainPage.getString(R.string.scan_qrcode));
                    return;
                }
                String firstName = et_first_name.getText().toString();
                String phone = et_phone.getText().toString();
                String lastName = et_last_name.getText().toString();
                String email = et_email.getText().toString();
                String address = et_address.getText().toString();
                if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(phone)) {
                    UIHelp.showShortToast(mainPage, this.getString(R.string.name_phone_mandatory));
                    return;
                }
                loadingDialog.show();
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("qrCode", QRcodeString);
                map.put("firstName", firstName);
                map.put("phone", phone);
                map.put("email", email);
                map.put("middleName", "");
                map.put("lastName", lastName);
                map.put("address", address);
                map.put("revenueId", App.instance.getRevenueCenter().getId().intValue());
                map.put("name", "");
                map.put("nickName", "");
                SyncCentre.getInstance().registStoredCard(mainPage, map, handler);

            }
            break;
            case R.id.btn_clear_info: {
                clearRegistInfo();
            }
            break;
            case R.id.tv_refund:
                if (TextUtils.isEmpty(QRcodeString)) {
                    UIHelp.showShortToast(mainPage, this.getString(R.string.scan_user_qr));
                    return;
                }
//                mainPageHandler.sendEmptyMessage(VIEW_EVENT_STORED_CARD_REFUND);
                storedCardRefund();
                break;
            case R.id.tv_paid: {
                if (TextUtils.isEmpty(QRcodeString)) {
                    UIHelp.showShortToast(mainPage, this.getString(R.string.scan_user_qr));
                    return;
                }
                String value = tv_store_card_value.getText().toString();
                try {
                    if (Integer.parseInt(value) > 200) {
                        UIHelp.showToast(context, this.getString(R.string.top_up_limit_200));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                Map<String, Object> map = new HashMap<String, Object>();
                map.put("qrCode", QRcodeString);
                map.put("revenueId", App.instance.getRevenueCenter().getId().intValue());
                map.put("consumeAmount", value);
                map.put("operateType", 1);
                map.put("payTypeId", 0);
                loadingDialog.show();
                SyncCentre.getInstance().updateStoredCardValue(mainPage, map, handler);
            }
            break;
            case R.id.btn_replacement_ok: {
                if (TextUtils.isEmpty(QRcodeString)) {
                    UIHelp.showShortToast(mainPage, this.getString(R.string.scan_user_qr));
                    return;
                }
                String replacement_phone = et_replacement_phone.getText().toString().trim();
                String replacement_card_no = et_replacement_card_no.getText().toString().trim();
                if (TextUtils.isEmpty(replacement_phone) && TextUtils.isEmpty(replacement_card_no)) {
                    et_replacement_phone.setError(getString(R.string.mandatory));
                    et_replacement_card_no.setError(getString(R.string.mandatory));
//                    UIHelp.showShortToast(mainPage, "phone and cardNo can not all empty");
                    return;
                }
//                mainPageHandler.sendEmptyMessage(VIEW_EVENT_STORED_CARD_REPLACEMEN);
                storedCardReplacement();
            }
            break;
            case R.id.rl_query_ok: {
                String query_phone = et_query_phone.getText().toString().trim();
//                String query_card_no = et_query_card_no.getText().toString().trim();
                if (TextUtils.isEmpty(query_phone)) {
                    et_query_phone.setError(getString(R.string.mandatory));
                    return;
                }
                if (TextUtils.isEmpty(QRcodeString)) {
                    UIHelp.showShortToast(mainPage, this.getString(R.string.scan_user_qr));
                    return;
                }
//                mainPageHandler.sendEmptyMessage(VIEW_EVENT_STORED_CARD_LOSS);
                storedCardQueryBalance();
            }

            break;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder, true);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }


    /**
     * 扫描成功，处理反馈信息
     *
     * @param rawResult
     */
    public void handleDecode(Result rawResult) {
        if (!App.instance.isSUNMIShow()) {
            inactivityTimer.onActivity();
            hiddenScanView();
        }
        //这里处理解码完成后的结果，此处将参数回传到Activity处理
        if (rawResult != null && !TextUtils.isEmpty(rawResult.getText())) {
            if (!App.instance.isSUNMIShow()) {
                beepManager.playBeepSoundAndVibrate();
            }
//            Toast.makeText(this, "扫描成功", Toast.LENGTH_SHORT).show();

//            Intent intent = getIntent();
//            intent.putExtra("codedContent", );
            String code = rawResult.getText();
            if (isPayAction) {
//                Map<String, Object> map = new HashMap<String, Object>();
//                map.put("qrCode", code);
//                map.put("revenueId", App.instance.getRevenueCenter().getId().intValue());
//                map.put("consumeAmount", amount);
//                map.put("operateType", 2);
//                mainPageHandler.sendMessage(mainPageHandler.obtainMessage(VIEW_EVENT_STORED_CARD_PAY_RESULT, map));
                Intent intent = getIntent();
                intent.putExtra("qrCode", code);
                intent.putExtra("revenueId", App.instance.getRevenueCenter().getId().intValue());
                intent.putExtra("consumeAmount", amount);
                intent.putExtra("operateType", 2);
                setResult(RESULT_OK, intent);
                finish();
                return;
            }
            if (code.startsWith("1_") || code.startsWith("2_")) {
                String[] card = code.split("_");
                if (card.length == 3 && IntegerUtils.isInteger(card[2])) {
                    QRcodeString = code;
                    tv_stored_card_no.setText(card[2]);
                } else {
                    UIHelp.showShortToast(mainPage, this.getString(R.string.please_scan_stored_qrcode));
                }

            } else {
                UIHelp.showShortToast(mainPage, this.getString(R.string.please_scan_stored_qrcode));
            }
            if (isPayAction) {

            }

//            intent.putExtra("codedBitmap", barcode);

//            setResult(RESULT_OK, intent);
//            finish();
        }

    }

    @Override
    public void onKeyBoardClick(String key) {
        BugseeHelper.buttonClicked(key);
        if (key.equals(mainPage.getResources().getString(com.alfredbase.R.string.delete))) {
            tv_store_card_value.setText("");
        } else {
            tv_store_card_value.setText(tv_store_card_value.getText().toString() + key);
        }
    }
}
