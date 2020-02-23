package com.alfredkds.activity;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.ParamConst;
import com.alfredbase.http.ResultCode;
import com.alfredbase.javabean.User;
import com.alfredbase.javabean.model.KDSDevice;
import com.alfredbase.javabean.model.MainPosInfo;
import com.alfredbase.store.Store;
import com.alfredbase.utils.DialogFactory;
import com.alfredbase.utils.KDSLogUtil;
import com.alfredbase.utils.LogUtil;
import com.alfredbase.utils.TimeUtil;
import com.alfredbase.utils.LanguageManager;
import com.alfredbase.utils.ToastUtils;
import com.alfredkds.R;
import com.alfredkds.global.App;
import com.alfredkds.global.SyncCentre;
import com.alfredkds.global.UIHelp;
import com.alfredkds.view.MyToggleButton;
import com.alfredkds.view.SystemSettings;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Setting extends BaseActivity implements MyToggleButton.OnToggleStateChangeListeren, LanguageManager.LanguageDialogListener {
    public static final String TAG = Setting.class.getSimpleName();
    public static final int HANDLER_LOGOUT_SUCCESS = 1;
    public static final int HANDLER_LOGOUT_FAILED = 2;

    private TextView tv_kot_history;
    private TextView tv_kot_reset;
    private TextView tv_switch_account;
    private EditText etStackCount;
    private TextView tvTimeStart;
    private TextView tvTimeEnd;
    private LinearLayout llStackCount;
    private LinearLayout llContTime;

    private MyToggleButton mt_kot_lan, mtPendingList, mtAllowPartial;
    private MyToggleButton mtKdsOnline, mtCrashReport;

    private ImageView iv_language;
    private LinearLayout ll_language_setting;
    private AlertDialog alertLanguage;


    private SystemSettings settings;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case HANDLER_LOGOUT_SUCCESS:
                    User user = App.instance.getUser();
                    if (user != null) {
                        Store.remove(context, Store.KDS_USER);
                    }
                    App.instance.popAllActivityExceptOne(EmployeeID.class);
                    UIHelp.startEmployeeID(context);
                    break;
                case HANDLER_LOGOUT_FAILED:
                    UIHelp.showToast(context, ResultCode.getErrorResultStr(context, (Throwable) msg.obj,
                            context.getResources().getString(R.string.revenue_center)));
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ((TextView) findViewById(R.id.tv_version)).setText(context.getResources().getString(R.string.version) + App.instance.VERSION);
        settings = App.instance.getSystemSettings();
        findView();
    }

    protected void findView() {
        super.initView();
        this.tv_kot_history = (TextView) findViewById(R.id.tv_history);
        this.tv_kot_reset = (TextView) findViewById(R.id.tv_reset);
        this.tvTimeStart = (TextView) findViewById(R.id.tvTimeStart);
        this.tvTimeEnd = (TextView) findViewById(R.id.tvTimeEnd);
        this.tv_switch_account = (TextView) findViewById(R.id.tv_switch_account);
        this.mtPendingList = (MyToggleButton) findViewById(R.id.mtPendingList);
        this.mt_kot_lan = (MyToggleButton) findViewById(R.id.mt_kot_lan);
        this.mtAllowPartial = (MyToggleButton) findViewById(R.id.mtAllowPartial);
        this.mtKdsOnline = (MyToggleButton) findViewById(R.id.mtKdsOnline);
        this.mtCrashReport = (MyToggleButton) findViewById(R.id.mtCrashReport);
        this.etStackCount = (EditText) findViewById(R.id.etStackCount);
        this.llStackCount = (LinearLayout) findViewById(R.id.llStackCount);
        this.llContTime = (LinearLayout) findViewById(R.id.contTime);
        this.mtPendingList.setOnStateChangeListeren(this);
        this.mtKdsOnline.setOnStateChangeListeren(this);
        this.mt_kot_lan.setOnStateChangeListeren(this);
        this.mtAllowPartial.setOnStateChangeListeren(this);
        this.mtCrashReport.setOnStateChangeListeren(this);
        this.tv_kot_history.setOnClickListener(this);
        this.tv_kot_reset.setOnClickListener(this);
        this.tv_switch_account.setOnClickListener(this);

        LinearLayout llNormal = (LinearLayout) findViewById(R.id.llNormal);
        LinearLayout llBalancer = (LinearLayout) findViewById(R.id.llBalancer);

        mtKdsOnline.setChecked(settings.isKdsOnline() == 0);
        mtAllowPartial.setChecked(settings.isAllowPartial());

        if (App.instance.isBalancer()) {
            llNormal.setVisibility(View.GONE);
            llBalancer.setVisibility(View.VISIBLE);
            llStackCount.setVisibility(View.GONE);

            RadioGroup radioGroup = (RadioGroup) findViewById(R.id.rgBalancerMode);
            RadioButton rbNormal = (RadioButton) findViewById(R.id.rbNormal);
            RadioButton rdBalance = (RadioButton) findViewById(R.id.rdBalance);
            RadioButton rdStack = (RadioButton) findViewById(R.id.rdStack);
            Button btnSave = (Button) findViewById(R.id.btSave);
            llContTime.setVisibility(View.GONE);

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                    if (checkedId == R.id.rdBalance) {
                        settings.setBalancerMode(SystemSettings.MODE_BALANCE);
                        llContTime.setVisibility(View.VISIBLE);
                        llStackCount.setVisibility(View.GONE);
                    } else if (checkedId == R.id.rdStack) {
                        settings.setBalancerMode(SystemSettings.MODE_STACK);
                        llContTime.setVisibility(View.VISIBLE);
                        llStackCount.setVisibility(View.VISIBLE);
                    } else {
                        settings.setBalancerMode(SystemSettings.MODE_NORMAL);
                        llContTime.setVisibility(View.GONE);
                        llStackCount.setVisibility(View.GONE);
                    }

                }
            });

            int mode = settings.getBalancerMode();

            if (mode == SystemSettings.MODE_BALANCE) {
                rdBalance.setChecked(true);
                llStackCount.setVisibility(View.GONE);
                llContTime.setVisibility(View.VISIBLE);
            } else if (mode == SystemSettings.MODE_STACK) {
                rdStack.setChecked(true);
                llStackCount.setVisibility(View.VISIBLE);
                llContTime.setVisibility(View.VISIBLE);

                if (settings.getStackCount() > 0)
                    etStackCount.setText(settings.getStackCount() + "");
                else {
                    etStackCount.setText("10");
                    settings.setStackCount(10);
                }
            } else {
                rbNormal.setChecked(true);
                llStackCount.setVisibility(View.GONE);
                llContTime.setVisibility(View.GONE);
            }

            if (mode > 0) {
                tvTimeStart.setText(TimeUtil.getTimeByFormat(settings.getBalancerTime(0), TimeUtil.PRINTER_FORMAT_TIME));
                tvTimeEnd.setText(TimeUtil.getTimeByFormat(settings.getBalancerTime(1), TimeUtil.PRINTER_FORMAT_TIME));
            }

            tvTimeStart.setOnClickListener(this);
            tvTimeEnd.setOnClickListener(this);
            btnSave.setOnClickListener(this);
            findViewById(R.id.tvReset).setOnClickListener(this);
        } else {
            llNormal.setVisibility(View.VISIBLE);
            llBalancer.setVisibility(View.GONE);
        }

        if (settings.isKdsLan()) {
            mt_kot_lan.setChecked(true);
        } else {
            mt_kot_lan.setChecked(false);
        }

        mtCrashReport.setChecked(settings.isCrashReportActive());

        mtPendingList.setChecked(settings.isPendingList());
        ll_language_setting = (LinearLayout) findViewById(R.id.ll_language_setting);
        ll_language_setting.setOnClickListener(this);

        iv_language = (ImageView) findViewById(R.id.iv_language);
        iv_language.setImageDrawable(LanguageManager.getLanguageFlag(this));


    }

    @Override
    public void handlerClickEvent(View v) {
        final Integer cid = (Integer) Store.getInt(context, Store.CURRENT_MAIN_POS_ID_CONNECTED);
        LogUtil.i(TAG, "" + cid);
        super.handlerClickEvent(v);
        switch (v.getId()) {
            case R.id.tv_history:
                UIHelp.startKotHistory(context);
                break;
            case R.id.tv_reset:
                DialogFactory.commonTwoBtnDialog(context, "", getString(R.string.reset),
                        getString(R.string.cancel), getString(R.string.ok), null,
                        new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                App.instance.finishAllActivityExceptOne(Setting.class);
                                App.instance.getTopActivity().finish();
                                User user = Store.getObject(context, Store.KDS_USER, User.class);
                                if (user != null) {
                                    Store.remove(context, Store.KDS_USER);
                                }
                                if (cid != null && cid != Store.DEFAULT_INT_TYPE) {
                                    Store.remove(context, Store.CURRENT_MAIN_POS_ID_CONNECTED);
                                    Store.remove(context, Store.CURRENT_MAIN_POS_IDS_CONNECTED);
                                }
                                UIHelp.startWelcome(context);
                            }
                        });
                break;
            case R.id.tv_switch_account:
                KDSDevice kdsDevice = App.instance.getKdsDevice();
                if (kdsDevice == null) {
                    return;
                }
                Map<String, Object> parameters = new HashMap<String, Object>();
                parameters.put("device", kdsDevice);
                parameters.put("deviceType", ParamConst.DEVICE_TYPE_KDS);
//                SyncCentre.getInstance().Logout(context, App.instance.getCurrentConnectedMainPos(), parameters, handler);
                for (MainPosInfo mainPos : App.instance.getCurrentConnectedMainPosList()) {
                    SyncCentre.getInstance().Logout(context, mainPos, parameters, handler);
                }
                break;
            case R.id.btSave:
                int stackCount = !TextUtils.isEmpty(etStackCount.getText()) ? Integer.parseInt(etStackCount.getText().toString()) : 0;
                settings.setStackCount(stackCount);
                ToastUtils.showToast(context, "Stack count updated to " + stackCount);
                break;
            case R.id.tvTimeStart:
                showTimePicker("Select Start Time", tvTimeStart, 0);
                break;
            case R.id.tvTimeEnd:
                showTimePicker("Select End Time", tvTimeEnd, 1);
                break;
            case R.id.tvReset:
                DialogFactory.commonTwoBtnDialog(context, "", getString(R.string.reset),
                        getString(R.string.cancel), getString(R.string.ok), null,
                        new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String kdsLLogs = KDSLogUtil.resetKdsLog(Store.getString(Setting.this, Store.KDS_LOGS));
                                Store.putString(Setting.this, Store.KDS_LOGS, kdsLLogs);
                                finish();
                            }
                        });

            case R.id.ll_language_setting:
                alertLanguage = LanguageManager.alertLanguage(this, this);
                break;
            default:
                break;
        }
    }

    private void showTimePicker(String title, final TextView tv, final int type) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String timeFormatted = selectedHour + ":" + selectedMinute;

                if (tv != null) {
                    tv.setText(timeFormatted);
                    settings.setBalancerTime(TimeUtil.getMillisOfTime(selectedHour, selectedMinute), type);
                }

            }
        }, hour, minute, true);
        timePickerDialog.setTitle(title);
        timePickerDialog.show();
    }

    @Override
    public void onToggleStateChangeListeren(MyToggleButton Mybutton, final Boolean checkState) {
        switch (Mybutton.getId()) {
            case R.id.mt_kot_lan:
                if (checkState) {
                    mt_kot_lan.setChecked(true);
                    settings.setKdsLan(ParamConst.DEFAULT_TRUE);
                } else {
                    mt_kot_lan.setChecked(false);
                    settings.setKdsLan(ParamConst.DEFAULT_FALSE);
                }

                break;
            case R.id.mtPendingList:
                if (checkState) {
                    mtPendingList.setChecked(true);
                    settings.setPendingList(true);
                } else {
                    mtPendingList.setChecked(false);
                    settings.setPendingList(false);
                }
                break;
            case R.id.mtKdsOnline:
                DialogFactory.commonTwoBtnDialog(context, "", "Shutdown KDS?",
                        getString(R.string.cancel), getString(R.string.ok), new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mtKdsOnline.setChecked(!checkState);
                            }
                        },
                        new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                updateStatus(checkState);

                                if (!checkState)
                                    UIHelp.startWelcome(context);
                            }
                        });

                break;
            case R.id.mtAllowPartial:
                if (checkState) {
                    settings.setAllowPartial(true);
                } else {
                    settings.setAllowPartial(false);
                }
                break;
            case R.id.mtCrashReport:
                String message;

                final boolean finalCheckedState = checkState;

                if (!finalCheckedState)
                    message = "Turn off crash reporting? \nsystem will be restart";
                else
                    message = "Turn on crash reporting? \nsystem will be restart";

                DialogFactory.commonTwoBtnDialog(context, "", message,
                        context.getResources().getString(R.string.cancel),
                        context.getResources().getString(R.string.ok),
                        new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mtCrashReport.setChecked(!finalCheckedState);
                            }
                        },
                        new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mtCrashReport.setChecked(finalCheckedState);
                                settings.setCrashReportStatus(finalCheckedState);
                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(App.instance, Welcome.class);
                                        @SuppressLint("WrongConstant")
                                        PendingIntent restartIntent = PendingIntent.getActivity(
                                                App.instance.getApplicationContext(), 0, intent,
                                                Intent.FLAG_ACTIVITY_NEW_TASK);
                                        AlarmManager mgr = (AlarmManager) App.instance
                                                .getSystemService(Context.ALARM_SERVICE);
                                        mgr.set(AlarmManager.RTC,
                                                System.currentTimeMillis() + 1000,
                                                restartIntent);
                                        ActivityManager am = (ActivityManager) App.instance
                                                .getSystemService(Context.ACTIVITY_SERVICE);
                                        if (am != null)
                                            am.killBackgroundProcesses(getPackageName());
                                        App.instance.finishAllActivity();
                                    }
                                });
                            }
                        });

                break;
        }
    }

    private void updateStatus(boolean checkState) {
        mtKdsOnline.setChecked(checkState);
        settings.setPendingList(checkState);

        int status;
        if (!checkState)
            status = -1;
        else
            status = 0;

        settings.setKdsOnline(status);
        KDSDevice kdsDevice = App.instance.getKdsDevice();
        kdsDevice.setKdsStatus(status);
        App.instance.setKdsDevice(kdsDevice);

        for (String ip : App.instance.getAllPairingIp()) {
            Map<String, Object> params = new HashMap<>();
            params.put("kds", kdsDevice);
            SyncCentre.getInstance().updateKdsStatus(this, ip, params, handler);
        }
    }

    @Override
    public void setLanguage(final String language) {
        if (alertLanguage != null) {
            alertLanguage.dismiss();
        }
        App.getTopActivity().changeLanguage(language);
    }
}
