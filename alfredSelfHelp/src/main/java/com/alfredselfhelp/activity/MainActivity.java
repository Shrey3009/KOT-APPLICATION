package com.alfredselfhelp.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.alfredbase.BaseActivity;
import com.alfredbase.BaseApplication;
import com.alfredbase.LoadingDialog;
import com.alfredbase.store.Store;
import com.alfredbase.utils.LanguageManager;
import com.alfredselfhelp.R;
import com.alfredselfhelp.global.App;
import com.alfredselfhelp.global.KpmDialogFactory;
import com.alfredselfhelp.global.RfidApiCentre;
import com.alfredselfhelp.global.SyncCentre;
import com.alfredselfhelp.utils.FileDialog;
import com.alfredselfhelp.utils.KpmTextTypeFace;
import com.alfredselfhelp.utils.PictureSwitch;
import com.alfredselfhelp.utils.TvPref;
import com.alfredselfhelp.utils.UIHelp;
import com.alfredselfhelp.utils.VideoResManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.nordicid.nurapi.NurApiUiThreadRunner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends BaseActivity implements LanguageManager.LanguageDialogListener {


    Handler mPeriodEventHdr = new Handler();
    String mstrPlayFile = null;
    long play_end_timestamp = 0;
    final int VIDEO_STATE_NONE = 0;
    final int VIDEO_STATE_PLAYING = 1;
    final int VIDEO_STATE_AD = 2;
    final int VIDEO_STATE_QR = 3;
    final int VIDEO_STATE_ONE_COMPLETE = 4;


    public static final int REMAINING_STOCK_SUCCESS = 100;
    public static final int REMAINING_STOCK_FAILURE = -100;
    private int mVideoState = VIDEO_STATE_NONE;
    int mnVideoWidth = 0;
    int mnVideoHeight = 0;
    private int counter = 0;

    private Button btn_video, btn_picture, btn_empty, btn_cc_ip, btn_time;
    private VideoView videoView;
    private boolean toPlayVideo = true;
    VideoResManager mVideoResManager;  //视频播放器
    private boolean isGameOpenNow = false;
    PictureSwitch picSwitch;
    View mQueClient;
    int queueing_view_mode = 0;// 不显示排队
    private int miLayoutType = 0;
    boolean mbUpdateVideo = false;
    private final static int LOGIN_STATUS_READY = 5;
    private int mVideoDefaultCurPos = 0;// in milisec
    private int mLoginStatus;
    private Button btn_start, btn_cc_ez;

    private LinearLayout li_select, ll_main;
    private KpmTextTypeFace textTypeFace;

    private RelativeLayout re_main_select;

    private ImageView img_mian_bg;
    private String imgUrl;
    private boolean doubleBackToExitPressedOnce = false;

    private ImageView iv_language;
    private LinearLayout ll_language_setting;
    private AlertDialog alertLanguage;

    protected void initView() {

        super.initView();
        setContentView(R.layout.activity_main);
        KpmTextTypeFace.getInstance().init(context);
        loadingDialog = new LoadingDialog(context);
        btn_start = (Button) findViewById(R.id.btn_start);
        btn_cc_ez = (Button) findViewById(R.id.btn_cc_ez);
        ll_main = (LinearLayout) findViewById(R.id.ll_main);
        int paymentType = Store.getInt(context, Store.KPMG_PAYMENT_TYPE, 1);
        if (paymentType == 1) {
            btn_cc_ez.setText("CC");
        } else {
            btn_cc_ez.setText("EZ-Link");
        }

        re_main_select = (RelativeLayout) findViewById(R.id.re_main_select);
        btn_start.setOnClickListener(this);

        ll_language_setting = (LinearLayout) findViewById(R.id.ll_language_setting);
        ll_language_setting.setOnClickListener(this);

        iv_language = (ImageView) findViewById(R.id.iv_language);
        iv_language.setImageDrawable(LanguageManager.getLanguageFlag(this));

        videoView = (VideoView) findViewById(R.id.videoView);
        picSwitch = (PictureSwitch) findViewById(R.id.slImages);
        picSwitch.setInAnimation(MainActivity.this, android.R.anim.fade_in);
        picSwitch.setOutAnimation(MainActivity.this, android.R.anim.fade_out);

        // btn_video = (Button) findViewById(R.id.btn_video);
        btn_picture = (Button) findViewById(R.id.btn_picture);
        btn_time = (Button) findViewById(R.id.btn_time);
        btn_cc_ip = (Button) findViewById(R.id.btn_cc_ip);
        btn_time.setOnClickListener(this);
        //  btn_empty = (Button) findViewById(R.id.btn_empty);
        li_select = (LinearLayout) findViewById(R.id.li_select);
        textTypeFace = KpmTextTypeFace.getInstance();
        textTypeFace.setUbuntuRegular((TextView) findViewById(R.id.tv_start));
        img_mian_bg = (ImageView) findViewById(R.id.img_main_bg);
        //  btn_empty.setOnClickListener(this);
        btn_picture.setOnClickListener(this);
        btn_cc_ip.setOnClickListener(this);
        //   btn_video.setOnClickListener(this);
        ll_main.setOnClickListener(this);
        btn_cc_ez.setOnClickListener(this);
        findViewById(R.id.btn_print_setting).setOnClickListener(this);
        ll_main.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                re_main_select.setVisibility(View.VISIBLE);

                return true;
            }
        });
        mVideoResManager = new VideoResManager(context);
//        TableInfo tables = TableInfoSQL.getKioskTable();
//        Order order = ObjectFactory.getInstance().getOrder(
//                ParamConst.ORDER_ORIGIN_POS, 0, tables,
//                App.instance.getRevenueCenter(), App.instance.getUser(),
//                App.instance.getSessionStatus(),
//                App.instance.getBusinessDate(),
//                App.instance.getIndexOfRevenueCenter(),
//                ParamConst.ORDER_STATUS_OPEN_IN_POS,
//                App.instance.getLocalRestaurantConfig()
//                        .getIncludedTax().getTax(), 0);


        imgUrl = Store.getString(context, Store.MAIN_URL);

        //
        if (TextUtils.isEmpty(imgUrl)) {

        } else {
            Glide.with(MainActivity.this)
                    .load(imgUrl)
                    .placeholder(R.drawable.globe_bg)
                    .error(R.drawable.globe_bg)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(img_mian_bg);

        }
        RfidApiCentre.getInstance().initApi(new NurApiUiThreadRunner() {
            public void runOnUiThread(Runnable r) {
                MainActivity.this.runOnUiThread(r);
            }
        });
//        VtintApiCentre.getInstance().init(context);
//        VtintApiCentre.getInstance().SearchUsb();

//        throw new NullPointerException("Test crash");
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
//                case ResultCode.SUCCESS: {
////					if (needSync) {
//                    dismissLoadingDialog();
//                    loadingDialog.setTitle(context.getString(R.string.update_all_data));
//                    loadingDialog.show();
//                    SyncCentre.getInstance().updateAllData(context, handler);
////					}else{
////						App.instance.setSessionStatus(Store.getObject(context, Store.SESSION_STATUS, SessionStatus.class));
////						//TODO startMainPage();
////					}
//                }
//                break;
                case REMAINING_STOCK_SUCCESS:
                    dismissLoadingDialog();
                    // TODO startMainPage();
                    UIHelp.startMenu(context);

                    break;
                case REMAINING_STOCK_FAILURE:
                    dismissLoadingDialog();
                    //  tv_error.setVisibility(View.VISIBLE);
                    break;


            }
        }
    };
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            counter++;
            //  prepare();

            mPeriodEventHdr.postDelayed(mRunnable, 4000);
        }

    };


    private Runnable mUpdateUiRunnable = new Runnable() {
        @Override
        public void run() {

            mPeriodEventHdr.postDelayed(mUpdateUiRunnable, 800);

            if (mbUpdateVideo) {
                adjustVideoView();
            }
//            if (mbCalling) {
//                flashingCall();// 叫号闪烁提醒
//            }
//            mUiCounter++;
//
//            if (mLoginStatus > LOGIN_STATUS_CHK_IP
//                    && (mUiCounter % CHECK_VER_PERIOD) == 20) {
//                // checkAppVer();
//            }

            // if (mstvShopAd.playState == 0) {
            // if (mShopAdHideCounter == 0) {
            // mShopAdHideCounter = SHOP_AD_HIDE_TIME;
            // } else {
            // mShopAdHideCounter--;
            // if (mShopAdHideCounter == 0) {
            // mstvShopAd.startScroll(); //广告字幕滚动条
            // }
            // }
            // }
            switch (mVideoState) {
                case VIDEO_STATE_PLAYING:
//                    if ((mUiCounter % ADVIDEO_SHOW_PERIOD) == 0) {
//                        // 进入广告时间
//                        boolean bPlayAd = false;
//                        String adfile = TvPref.readADFile();
//                        if (!adfile.isEmpty()) {
//                            File file = new File(adfile);
//                            if (file.exists() && !file.isDirectory()) {
//                                bPlayAd = true;
//                            }
//                        }
//
//                        if (bPlayAd || mQRCodeStatus == 1) {
//                            if (videoView.isPlaying()) {
//                                mVideoDefaultCurPos = videoView
//                                        .getCurrentPosition();
//                                videoView.stopPlayback();
//                            } else {
//                                mVideoDefaultCurPos = 0;
//                            }
//                            if (bPlayAd) {
//                                startPlayAd(adfile);
//                            } else {
//                                startPlayQR();
//                            }
//                        }
//
//                    }
                    break;
                case VIDEO_STATE_ONE_COMPLETE: {
                    if (System.currentTimeMillis() - play_end_timestamp > 1000) {
                        startPlay(false);// 播放下一个文件
                    }
                }
                break;

                case VIDEO_STATE_NONE:
                    break;
            }
//
//            if (mHeader.getVisibility() == View.VISIBLE
//                    && mUiCounter - stamp > 6) {
//                mHeader.setVisibility(View.GONE);// 自动隐藏标题栏
//            }
        }

    };

    @Override
    protected void handlerClickEvent(View v) {
        super.handlerClickEvent(v);
        Intent intent = new Intent(MainActivity.this, FileDialog.class);
        switch (v.getId()) {

            case R.id.ll_main:

                re_main_select.setVisibility(View.GONE);

                break;
            case R.id.btn_start:
                //  dismissLoadingDialog();
                loadingDialog.setTitle(getString(R.string.loading));
                loadingDialog.show();
                SyncCentre.getInstance().getRemainingStock(context, handler);
//
                break;

            case R.id.btn_picture:
                // 选择图片
                input();
                break;
            case R.id.btn_cc_ez:
                int paymentType = Store.getInt(context, Store.KPMG_PAYMENT_TYPE, 1);
                if (paymentType == 1) {
                    paymentType = 2;
                    Store.putInt(context, Store.KPMG_PAYMENT_TYPE, paymentType);
                    btn_cc_ez.setText("EZ-Link");
                } else {
                    paymentType = 1;
                    Store.putInt(context, Store.KPMG_PAYMENT_TYPE, paymentType);
                    btn_cc_ez.setText("CC");
                }
                break;
            case R.id.btn_time:
                diaTime();
                break;
            case R.id.btn_cc_ip:
                KpmDialogFactory.commonTwoBtnIPInputDialog(MainActivity.this,
                        "CC ip", "key in CC ip", this.getString(R.string.cancel), this.getString(R.string.ok),
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                re_main_select.setVisibility(View.GONE);
                            }
                        },
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                re_main_select.setVisibility(View.GONE);
                            }
                        });
                break;
            case R.id.btn_print_setting:
                UIHelp.startDevices(context);
                break;
            case R.id.ll_language_setting:
                alertLanguage = LanguageManager.alertLanguage(this, this);
            break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        doubleBackToExitPressedOnce = false;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == FileDialog.RESULT_SELECT_FILE) {
            String selFile = data
                    .getStringExtra(FileDialog.RESULT_SELECT_FILEPATH);
            if (requestCode == 1) {
                if (selFile != null) {
                    toPlayVideo = false;
                    TvPref.saveImageFilePath(selFile);
                    TvPref.saveVideoFile("");
                    if (videoView != null && videoView.isPlaying())// 视频结束
                    {
                        //  LogFile.v("正在播放视频  暂停视频");
                        videoView.stopPlayback();
                    }
//                    mtvVideoPath.setText(selFile);
                    videoView.setVisibility(View.GONE);
                    mVideoResManager.clear();
                    updateView();
                    this.playSlideImage();


                } else {
                    Toast.makeText(MainActivity.this, this.getString(R.string.no_image), Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == 2) {
                if (selFile != null) {
                    toPlayVideo = true;
                    TvPref.saveVideoFile(selFile);
                    TvPref.saveImageFilePath("");
                    if (picSwitch.isPlaying()) {
                        picSwitch.stopPlay();
                    }
                    picSwitch.setVisibility(View.GONE);
                    videoView.setVisibility(View.VISIBLE);
                    //mtvVideoPath.setText(selFile);
                    updateVideoView();// 重新开始，可以打断广告视频


                } else {
                    Toast.makeText(MainActivity.this, this.getString(R.string.no_video), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    void updateVideoView() {
        mVideoResManager.UpdateVideo();
        updateView();
        startPlay(false);
    }

    private void updateView() {
        String picpath = TvPref.readImageFilePath();
        Log.e("updateView", picpath);
        boolean hasPics = picpath != null && !picpath.isEmpty();

//        if (LOGIN_STATUS_READY == mLoginStatus){  // 在排队

        //   ViewGroup.LayoutParams params = mQueClient.getLayoutParams();
        if (mVideoResManager.hasVideo() || (hasPics && App.instance.getPlayIMGEn())
                || isGameOpenNow) {
            // 需要播放视频图片
            queueing_view_mode = 1;
            if (miLayoutType == 3) {
                //  params.width = TvPref.readQLayoutWidth();// 只显示右侧一部分
            } else {
                // params.height = TvPref.readQLayoutHeight();
            }
//				mQueAdapter.notifyDataSetChanged();
            // orderNoListAdapter.notifyDataSetChanged();
            // queview_all_screen_lyt.setVisibility(View.GONE);
            //queview_rigth_lyt.setVisibility(View.VISIBLE);
            //  falshCount = 0;
        } else {
            //TODO
            queueing_view_mode = 2;// 全屏
            // 全部是排队
            if (miLayoutType == 3) {
                //  params.width = display.getWidth();
            } else {
                // params.height = display.getHeight();
            }

//                queview_all_screen_lyt.setVisibility(View.VISIBLE);
//                queview_rigth_lyt.setVisibility(View.GONE);
//                falshCount = -1;
        }
//            mQueClient.setLayoutParams(params);
//            mQueClient.setVisibility(View.VISIBLE);
//        } else {
//            mQueClient.setVisibility(View.GONE);
//            mstvShopAd.setVisibility(View.INVISIBLE);// 广告
//        }

        // mVideoClient.setVisibility(View.GONE);
        //    picSwitch.setVisibility(View.GONE);
        //  mimLogo.setVisibility(View.GONE);

        if (mVideoResManager.hasVideo()) {
            //  mVideoClient.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.VISIBLE);
            mbUpdateVideo = true;
        } else if (hasPics && (queueing_view_mode == 0 || App.instance.getPlayIMGEn())) {
            picSwitch.setVisibility(View.VISIBLE);
        } else if (isGameOpenNow) {
            picSwitch.setVisibility(View.VISIBLE);
        } else {
            //    mimLogo.setVisibility(View.VISIBLE);
        }

        if (queueing_view_mode != 0) {
            //   calcFont();
        }
//
//        main_scan_barcode_edt.setFocusable(true);
//        main_scan_barcode_edt.requestFocus();

//        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        // 得到InputMethodManager的实例
//        if (imm != null) {
//            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//        }
    }

    /**
     * 播放视频
     * 1、从暂停状态恢复
     * 2、查找本地存储的视频地址
     * 1、有视频：播放
     * 2、无视频：有图片就播图片，没有就不显示
     *
     * @param resume 视频处于暂停？
     */
    private void startPlay(boolean resume) {
        /***
         * 将播放器关联上一个音频或者视频文件 videoView.setVideoURI(Uri uri)
         * videoView.setVideoPath(String path) 以上两个方法都可以。
         */

        videoView.stopPlayback();

        int pos = 0;
        if (resume) {
            // 从广告视频恢复
            pos = mVideoDefaultCurPos;
        } else {
            mstrPlayFile = mVideoResManager.getNextVideo();
            if (mstrPlayFile == null) {
                String s = TvPref.readImageFilePath();
                if (s.isEmpty()) {
                    mVideoState = VIDEO_STATE_NONE;
                } else {
                    playSlideImage();
                }
                //   LogFile.i("no video");
                return;// 不播放视频
            }
        }
        //   LogFile.i(String.format("playing video file:%s", mstrPlayFile));
        String dir = TvPref.readVideoFile();
        if (!mstrPlayFile.contains(dir)) {
            dir = TvPref.readExstoragePath();
        }
        //     mtvVideoPath.setText(dir);
        String filename = mstrPlayFile.substring(dir.length());
        //  mtvVideoplaying.setText(filename);

        videoView.setVideoPath(mstrPlayFile);

        /**
         * w为其提供一个控制器，控制其暂停、播放……等功能
         */
        // videoView.setMediaController(new MediaController(this));

        /**
         * 视频或者音频到结尾时触发的方法
         */
        videoView
                .setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
//                        LogFile.i(String.format("OnCompletion,file=%s",
//                                mstrPlayFile));
                        mVideoState = VIDEO_STATE_ONE_COMPLETE;
                        play_end_timestamp = System.currentTimeMillis();
                    }
                });

        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
//
//                LogFile.e(String.format("playfile=%s,OnError", mstrPlayFile));
//                LogFile.i(String.format("what=%d,ex=%d", what, extra));

                mVideoState = VIDEO_STATE_ONE_COMPLETE;// 跳过当前文件
                play_end_timestamp = System.currentTimeMillis();
                return true; // 不显示对话框
            }
        });
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                mnVideoWidth = mp.getVideoWidth();
                mnVideoHeight = mp.getVideoHeight();

                //    mp.setVolume(6f, 6f);

                adjustVideoView();
            }
        });

        videoView.start();
        if (pos > 0) {
            videoView.seekTo(pos);
        }
        mVideoState = VIDEO_STATE_PLAYING;
    }

    private void playSlideImage() {
        // 判断是否配置允许同时播放幻灯片和排队
        if (!App.instance.getPlayIMGEn() && LOGIN_STATUS_READY == mLoginStatus) {
            return;// 排队时不显示图片
        }

        if (!picSwitch.isPlaying()) {
            String esdpath = TvPref.readExstoragePath();
            String specifypath = TvPref.readImageFilePath();
            boolean bloss = TvPref.readSaveMem();
            if (specifypath.length() > 0 && esdpath.contains(specifypath)) {
                picSwitch.setImageDir(specifypath, bloss);
                // mtvVideoPath.setText(specifypath);
            } else {
                int num = 0;
                if (specifypath.length() > 0) {
                    num = picSwitch.setImageDir(esdpath, bloss);
                    if (num > 1) {
                        //  mtvVideoPath.setText(esdpath);
                    }
                }
                if (num < 2) {
                    if (specifypath.length() == 0) {
                        return;
                    }
                    picSwitch.setImageDir(specifypath, bloss);
                    // mtvVideoPath.setText(specifypath);
                }
            }

            picSwitch.setVisibility(View.VISIBLE);
            picSwitch.startPlay();
        }
    }

    private void adjustVideoView() {
        mbUpdateVideo = false;
        int client_w = videoView.getWidth();
        int client_h = videoView.getHeight();
        if (client_h == 0 || client_w == 0 || mnVideoWidth == 0
                || mnVideoHeight == 0) {
            return;
        }
        int ajust = TvPref.readVideoAdjust();

        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) videoView
                .getLayoutParams();
        if (client_h / mnVideoHeight > client_w / mnVideoWidth)//
        {
            // 等高模式
            int dstW = mnVideoWidth * client_h / mnVideoHeight;
            int margin_left = (client_w - dstW) * ajust / 20;
            mlp.leftMargin = margin_left;
            mlp.rightMargin = margin_left;
            // mlp.leftMargin=-80;
            // mlp.rightMargin=-80;
            videoView.setLayoutParams(mlp);
        } else {
            // 等宽
            int dstH = mnVideoHeight * client_w / mnVideoWidth;
            int margin_top = (client_h - dstH) * ajust / 20;
            mlp.topMargin = margin_top;
            mlp.bottomMargin = margin_top;
            videoView.setLayoutParams(mlp);
        }

    }


    private void input() {


//        final EditText inputServer = new EditText(this);
//        imgUrl = Store.getString(context, Store.MAIN_URL);
//        if (!TextUtils.isEmpty(imgUrl)) {
//            inputServer.setText(imgUrl);
//        }
        KpmDialogFactory.commonTwoBtnImageURLInputDialog(MainActivity.this,
                this.getString(R.string.background_image), this.getString(R.string.url_background_image), this.getString(R.string.cancel), this.getString(R.string.ok),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        re_main_select.setVisibility(View.GONE);
                    }
                },
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String input = ((EditText) v).getText().toString();
                        if (isCompleteUrl(input)) {
                            Store.putString(context, Store.MAIN_URL, input);
                            loadingDialog = new LoadingDialog(MainActivity.this);
                            loadingDialog.showByTime(2000);
                            Glide.with(MainActivity.this)
                                    .load(input)
                                    .placeholder(R.drawable.globe_bg)
                                    .error(R.drawable.globe_bg)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .into(img_mian_bg);

                        } else {
//                            Toast.makeText(MainActivity.this, "输入的地址有误", Toast.LENGTH_LONG).show();
                            UIHelp.showToast(MainActivity.this, MainActivity.this.getString(R.string.url_error));
                        }

                        re_main_select.setVisibility(View.GONE);
                    }
                });
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("请输入图片地址");
//        builder.setView(inputServer)
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        re_main_select.setVisibility(View.GONE);
//                    }
//                });
//        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//
//            public void onClick(DialogInterface dialog, int which) {
//                String input = inputServer.getText().toString();
//                // String input = "http://139.224.17.126/upload/img/item/c1cfbf26-7310-4a6e-b5a4-15e89a82a9c3.png";
//                if (isCompleteUrl(input)) {
//                    Store.putString(context, Store.MAIN_URL, input);
//                    loadingDialog = new LoadingDialog(MainActivity.this);
//                    loadingDialog.showByTime(2000);
//                    Glide.with(MainActivity.this)
//                            .load(input)
//                            .placeholder(R.drawable.globe_bg)
//                            .error(R.drawable.globe_bg)
//                            .diskCacheStrategy(DiskCacheStrategy.NONE)
//                            .into(img_mian_bg);
//
//                } else {
//                    Toast.makeText(MainActivity.this, "输入的地址有误", Toast.LENGTH_LONG).show();
//                }
//
//                re_main_select.setVisibility(View.GONE);
//            }
//        });
//        builder.show();

    }


    private void diaTime() {

//        String time;
//        final EditText timeServer = new EditText(this);
//        timeServer.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
//
//        timeServer.setKeyListener(new DigitsKeyListener(false, true));
//        timeServer.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                String editStr = s.toString().trim();
//
//                int posDot = editStr.indexOf(".");
//                //不允许输入3位小数,超过三位就删掉
//                if (posDot < 0) {
//                    return;
//                }
//                if (editStr.length() - posDot - 1 > 1) {
//                    s.delete(posDot + 2, posDot + 3);
//                } else {
//                    //TODO...这里写逻辑
//                }
//            }
//        });
//
//
//        time = Store.getString(context, Store.KPM_TIME);
//        if (!TextUtils.isEmpty(time)) {
//            timeServer.setText(time);
//            timeServer.setSelection(time.length());
//        }
        KpmDialogFactory.commonTwoBtnTimeInputDialog(MainActivity.this,
                MainActivity.this.getString(R.string.idle_time), MainActivity.this.getString(R.string.input_idle_time), this.getString(R.string.cancel), this.getString(R.string.ok),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        re_main_select.setVisibility(View.GONE);
                    }
                },
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String input = ((EditText) v).getText().toString();
                        // String input = "http://139.224.17.126/upload/img/item/c1cfbf26-7310-4a6e-b5a4-15e89a82a9c3.png";
                        Store.putString(context, Store.KPM_TIME, input);
                        re_main_select.setVisibility(View.GONE);
                    }
                });
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("请输入时间(Minute)");
//        builder.setView(timeServer)
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        re_main_select.setVisibility(View.GONE);
//                    }
//                });
//        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//
//            public void onClick(DialogInterface dialog, int which) {
//                String input = timeServer.getText().toString();
//                // String input = "http://139.224.17.126/upload/img/item/c1cfbf26-7310-4a6e-b5a4-15e89a82a9c3.png";
//                Store.putString(context, Store.KPM_TIME, input);
//                re_main_select.setVisibility(View.GONE);
//            }
//        });
//        builder.show();

    }

    private static final int DECIMAL_DIGITS = 1;

    InputFilter lengthfilter = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            // 删除等特殊字符，直接返回
            if ("".equals(source.toString())) {
                return null;
            }
            String dValue = dest.toString();
            String[] splitArray = dValue.split("//.");
            if (splitArray.length > 1) {
                String dotValue = splitArray[1];
                int diff = dotValue.length() + 1 - DECIMAL_DIGITS;
                if (diff > 0) {
                    return source.subSequence(start, end - diff);
                }
            }
            return null;
        }
    };

    public static boolean isCompleteUrl(String text) {
        Pattern p = Pattern.compile("((http|ftp|https)://)(([a-zA-Z0-9\\._-]+\\.[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,4})*(/[a-zA-Z0-9\\&%_\\./-~-]*)?", Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher(text);
        return matcher.find();
    }

    public void onDestroy() {
        //    App.instance.setSave();
        if (picSwitch.isPlaying()) {
            picSwitch.stopPlay();
        }
        if (videoView != null && videoView.isPlaying()) {
            videoView.stopPlayback();
        }
//        VtintApiCentre.getInstance().onDestory();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        UIHelp.showToast(this, context.getResources().getString(R.string.exit_program));

        BaseApplication.postHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public void setLanguage(final String language) {
        if (alertLanguage != null) {
            alertLanguage.dismiss();
        }
        App.getTopActivity().changeLanguage(language);
    }

}
