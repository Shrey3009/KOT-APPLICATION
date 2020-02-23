package com.alfredselfhelp.activity;

import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.InputType;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.global.BugseeHelper;
import com.alfredbase.utils.NetUtil;
import com.alfredbase.utils.TextTypeFace;

import com.alfredselfhelp.view.Numerickeyboard.KeyBoardClickListener;

import com.alfredselfhelp.R;
import com.alfredselfhelp.global.App;
import com.alfredselfhelp.global.KpmDialogFactory;
import com.alfredselfhelp.utils.KpmTextTypeFace;
import com.alfredselfhelp.utils.UIHelp;
import com.alfredselfhelp.view.Numerickeyboard;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 二维码参照：http://www.cnblogs.com/mythou/p/3280023.html
 * <p>
 * http://www.eoeandroid.com/thread-319592-1-1.html
 *
 * @author
 */
public class ConnectPOS extends BaseActivity implements Callback,
        KeyBoardClickListener, OnFocusChangeListener {
    private ViewPager viewPager;
    private ArrayList<View> views = new ArrayList<View>();

    private View QRConnectView;
    private View IPConnectView;
    private Numerickeyboard ipkeyboard;

    private SurfaceView surfaceView;
    //private ViewfinderView viewfinderView;
    private boolean hasSurface;
    //	private InactivityTimer inactivityTimer;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    private MediaPlayer mediaPlayer;
    //private ConnectPOSHandler handler;
    private EditText et_ip1;
    private EditText et_ip2;
    private EditText et_ip3;
    private EditText et_ip4;
    private TextView tv_cancel, tv_save;
    private boolean doubleBackToExitPressedOnce = false;
    private KpmTextTypeFace textTypeFace;
    private TextView tv_app_version;

    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_connect_pos);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tv_app_version = (TextView) findViewById(R.id.tv_app_version);
        tv_app_version.setText(context.getResources().getString(R.string.version) + App.instance.VERSION);
//		QRConnectView = getLayoutInflater().inflate(R.layout.qr_connect_view,
//				null);
        //	views.add(QRConnectView);
        IPConnectView = getLayoutInflater().inflate(R.layout.ip_connect_view,
                null);
        ipkeyboard = (Numerickeyboard) IPConnectView
                .findViewById(R.id.ipkeyboards);
        ipkeyboard.setKeyBoardClickListener(this);
        ipkeyboard.setParams(context);
//		tv_connect = (TextView) IPConnectView.findViewById(R.id.tv_connect);
//		tv_connect.setOnClickListener(this);
        tv_cancel = (TextView) IPConnectView.findViewById(R.id.tv_cancel);
        tv_save = (TextView) IPConnectView.findViewById(R.id.tv_save);
        tv_cancel.setOnClickListener(this);
        tv_save.setOnClickListener(this);
        et_ip1 = (EditText) IPConnectView.findViewById(R.id.et_ip1);
        et_ip2 = (EditText) IPConnectView.findViewById(R.id.et_ip2);
        et_ip3 = (EditText) IPConnectView.findViewById(R.id.et_ip3);
        et_ip4 = (EditText) IPConnectView.findViewById(R.id.et_ip4);
        et_ip1.setOnFocusChangeListener(this);
        et_ip2.setOnFocusChangeListener(this);
        et_ip3.setOnFocusChangeListener(this);
        et_ip4.setOnFocusChangeListener(this);
        et_ip1.setInputType(InputType.TYPE_NULL);
        et_ip2.setInputType(InputType.TYPE_NULL);
        et_ip3.setInputType(InputType.TYPE_NULL);
        et_ip4.setInputType(InputType.TYPE_NULL);
        et_ip1.requestFocus();
        views.add(IPConnectView);
//		CameraManager.init(getApplication());
//		viewfinderView = (ViewfinderView) QRConnectView
//				.findViewById(R.id.viewfinderView);
        hasSurface = false;
        //	inactivityTimer = new InactivityTimer(this);
        viewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                if (arg0 == 0) {
                    tv_app_version.setTextColor(getResources().getColor(R.color.white));
                    //	startQR();
                } else {
                    tv_app_version.setTextColor(getResources().getColor(R.color.black));
                    //closeQR();
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
        viewPager.setAdapter(new ViewPagerAdapter(views));
        viewPager.setCurrentItem(0);
//		//startQR();
        initTextTypeFace(QRConnectView, IPConnectView);

    }

    private void initTextTypeFace(View QRConnectView, View IPConnectView) {
        textTypeFace = KpmTextTypeFace.getInstance();
        ;
        textTypeFace.setUbuntuMedium(tv_app_version);
        //	textTypeFace.setTrajanProRegular((TextView)QRConnectView.findViewById(R.id.tv_scan_qr_remind));
        textTypeFace.setUbuntuMedium((TextView) IPConnectView.findViewById(R.id.tv_setup_ip));
        //	textTypeFace.setTrajanProRegular((TextView)IPConnectView.findViewById(R.id.tv_connect));
        textTypeFace.setUbuntuMedium(et_ip1);
        textTypeFace.setUbuntuMedium(et_ip2);
        textTypeFace.setUbuntuMedium(et_ip3);
        textTypeFace.setUbuntuMedium(et_ip4);
        textTypeFace.setUbuntuMedium(tv_cancel);
        textTypeFace.setUbuntuMedium(tv_save);

    }
//	private void startQR() {
//		surfaceView = (SurfaceView) QRConnectView
//				.findViewById(R.id.surfaceView);
//		if (hasSurface) {
//			initCamera(surfaceView.getHolder());
//		} else {
//			surfaceView.getHolder().addCallback(this);
//			surfaceView.getHolder().setType(
//					SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//		}
//		decodeFormats = null;
//		characterSet = null;
//
//		playBeep = true;
//		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
//		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
//			playBeep = false;
//		}
//		initBeepSound();
//		vibrate = true;
//	}
//
//	private void closeQR() {
//		if (handler != null) {
//			handler.quitSynchronously();
//			handler = null;
//		}
//		CameraManager.get().closeDriver();
//	}

//	private void initCamera(SurfaceHolder surfaceHolder) {
//		try {
//			CameraManager.get().openDriver(surfaceHolder);
//		} catch (IOException ioe) {
//			return;
//		} catch (RuntimeException e) {
//			return;
//		}
//		if (handler == null) {
//			handler = new ConnectPOSHandler(this, decodeFormats, characterSet);
//		}
//	}

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            //	initCamera(holder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    @Override
    protected void onDestroy() {
//		closeQR();
//		inactivityTimer.shutdown();
        super.onDestroy();
    }

//	public ViewfinderView getViewfinderView() {
//		return viewfinderView;
//	}
//
//	public Handler getHandler() {
//		return handler;
//	}
//
//	public void drawViewfinder() {
//		viewfinderView.drawViewfinder();
//	}
//
//	public void handleDecode(final Result obj, Bitmap barcode) {
//		inactivityTimer.onActivity();
//		String posIp = obj.getText();
//		if (NetUtil.ping(posIp)) {
//			App.instance.setPairingIp(posIp);
//			playBeepSoundAndVibrate();
//			UIHelp.startEmployeeID(context);
//			finish();
//			App.instance.finishTheActivity(SelectRevenue.class);
//		} else {
//			UIHelp.showToast(context, context.getResources().getString(R.string.invalid_ip));
//		}
//	}

    private static final long VIBRATE_DURATION = 200L;
//
//	private void playBeepSoundAndVibrate() {
//		if (playBeep && mediaPlayer != null) {
//			mediaPlayer.start();
//		}
//		if (vibrate) {
//			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
//			vibrator.vibrate(VIBRATE_DURATION);
//		}
////	}

    class ViewPagerAdapter extends PagerAdapter {
        private List<View> pageViews;

        public ViewPagerAdapter(List<View> pageViews) {
            if (pageViews == null)
                pageViews = Collections.emptyList();
            this.pageViews = pageViews;
        }

        @Override
        public int getCount() {
            return pageViews.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(pageViews.get(arg1));
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            View view = pageViews.get(arg1);
            try {
                if (view.getParent() != null) {
                    ((ViewPager) view.getParent()).removeView(view);
                }
                ((ViewPager) arg0).addView(view, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return view;

        }
    }

    @Override
    public void handlerClickEvent(View v) {
        super.handlerClickEvent(v);
        switch (v.getId()) {
            case R.id.tv_save:
                String ip = "(2[5][0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})";
                Pattern pattern = Pattern.compile(ip);
                Matcher matcher = pattern.matcher(getInputedIP());
                if (matcher.matches()) {
                    String posIp = getInputedIP();
                    if (NetUtil.ping(posIp)) {
                        App.instance.setPairingIp(posIp);
                        UIHelp.startEmployeeID(context);
                        finish();
                    } else {
                        UIHelp.showToast(context, context.getResources().getString(R.string.invalid_ip));
                    }
                } else {
                    UIHelp.showToast(context, context.getResources().getString(R.string.invalid_ip_));
                }
                break;

            case R.id.tv_cancel:
                finish();
                break;

            default:
                break;
        }
    }

    @Override
    public void onKeyBoardClick(String key) {
        if (TextUtils.isEmpty(key))
            return;

        BugseeHelper.buttonClicked(key);
        EditText tempEditText = getFocusView();
        if (tempEditText == null)
            return;
        if (key.equals("X")) {
            String content = tempEditText.getText().toString();
            if (content.length() > 0) {
                tempEditText
                        .setText(content.substring(0, content.length() - 1));
            } else {
                setBeforeFocusView();
            }
        } else {
            tempEditText.setText(tempEditText.getText().toString() + key);
            if (tempEditText.getText().toString().length() >= 3)
                setNextFocusView();
        }
    }

    private EditText getFocusView() {
        if (et_ip1.hasFocus())
            return et_ip1;
        if (et_ip2.hasFocus())
            return et_ip2;
        if (et_ip3.hasFocus())
            return et_ip3;
        if (et_ip4.hasFocus())
            return et_ip4;
        return null;
    }

    private void setNextFocusView() {
        if (et_ip1.hasFocus()) {
            et_ip2.requestFocus();
        } else if (et_ip2.hasFocus()) {
            et_ip3.requestFocus();
        } else if (et_ip3.hasFocus()) {
            et_ip4.requestFocus();
        }
    }

    private void setBeforeFocusView() {
        if (et_ip2.hasFocus()) {
            et_ip1.requestFocus();
        } else if (et_ip3.hasFocus()) {
            et_ip2.requestFocus();
        } else if (et_ip4.hasFocus()) {
            et_ip3.requestFocus();
        }
    }

    private String getInputedIP() {
        String ipAddress = et_ip1.getText().toString() + "."
                + et_ip2.getText().toString() + "."
                + et_ip3.getText().toString() + "."
                + et_ip4.getText().toString();
        return ipAddress;

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.et_ip1:
                if (hasFocus) {
                    et_ip1.setBackgroundColor(getResources().getColor(R.color.gray));
                } else {
                    et_ip1.setBackgroundColor(getResources()
                            .getColor(R.color.white));
                }
                break;
            case R.id.et_ip2:
                if (hasFocus) {
                    et_ip2.setBackgroundColor(getResources().getColor(R.color.gray));
                } else {
                    et_ip2.setBackgroundColor(getResources()
                            .getColor(R.color.white));
                }
                break;
            case R.id.et_ip3:
                if (hasFocus) {
                    et_ip3.setBackgroundColor(getResources().getColor(R.color.gray)
                    );
                } else {
                    et_ip3.setBackgroundColor(getResources()
                            .getColor(R.color.white));
                }
                break;
            case R.id.et_ip4:
                if (hasFocus) {
                    et_ip4.setBackgroundColor(getResources().getColor(R.color.gray));
                } else {
                    et_ip4.setBackgroundColor(getResources()
                            .getColor(R.color.white));
                }
                break;
            default:
                break;
        }

    }

//	@Override
//	public void onBackPressed() {
//		 if (doubleBackToExitPressedOnce) {
//		        super.onBackPressed();
//		        return;
//		    }
//
//		    this.doubleBackToExitPressedOnce = true;
//		    UIHelp.showToast(this, context.getResources().getString(R.string.exit_program));
//
//		BaseApplication.postHandler.postDelayed(new Runnable() {
//
//		        @Override
//		        public void run() {
//		            doubleBackToExitPressedOnce=false;
//		        }
//		    }, 2000);
//	}

}
