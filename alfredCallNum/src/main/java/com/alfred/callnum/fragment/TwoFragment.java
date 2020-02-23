package com.alfred.callnum.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.alfred.callnum.R;
import com.alfred.callnum.activity.MainActivity;
import com.alfred.callnum.adapter.CallBean;
import com.alfred.callnum.adapter.MycallAdapter;
import com.alfred.callnum.adapter.RvListener;
import com.alfred.callnum.global.App;
import com.alfred.callnum.utils.FileDialog;
import com.alfred.callnum.utils.TvPref;
import com.alfred.callnum.utils.VideoResManager;
import com.alfred.callnum.widget.PictureSwitch;
import com.alfredbase.global.BugseeHelper;
import com.alfredbase.utils.EditTextUtil;
import com.alfredbase.utils.LanguageManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TwoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TwoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TwoFragment extends Fragment implements View.OnClickListener, View.OnTouchListener, LanguageManager.LanguageDialogListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private int vid;
    private RecyclerView re_left, re_right;
    private List<CallBean> mDataLeft;
    private List<CallBean> mDatasRight;
    MycallAdapter mAdapterLeft;
    MycallAdapter mAdapterRight;
    private Button btn_video, btn_picture, btn_empty;
    private VideoView videoView;
    private boolean toPlayVideo = true;
    VideoResManager mVideoResManager;  //视频播放器
    private boolean isGameOpenNow = false;
    PictureSwitch picSwitch;
    View mQueClient;
    int queueing_view_mode = 0;// 不显示排队
    private int miLayoutType = 0;
    private int mLoginStatus;

    boolean mbUpdateVideo = false;
    private final static int LOGIN_STATUS_READY = 5;
    private int mVideoDefaultCurPos = 0;// in milisec
    private Boolean isOpen = true;
    private Boolean isVol = true;

    Handler mPeriodEventHdr = new Handler();
    String mstrPlayFile = null;
    long play_end_timestamp = 0;
    final int VIDEO_STATE_NONE = 0;
    final int VIDEO_STATE_PLAYING = 1;
    final int VIDEO_STATE_AD = 2;
    final int VIDEO_STATE_QR = 3;
    final int VIDEO_STATE_ONE_COMPLETE = 4;

    private int mVideoState = VIDEO_STATE_NONE;
    int mnVideoWidth = 0;
    int mnVideoHeight = 0;
    private int counter = 0;
    private LinearLayout li_select;
    RelativeLayout re_video_pic;
    Handler handler;
    Map<String, Object> callMap = new HashMap<String, Object>();
    private AnimationSet textAnimationSet;
    private Boolean type = true;
    private TextView call_big, line, lines;
    ScaleAnimation scaleAnimation;

    private Handler handlers;

    private ImageView iv_language;
    private LinearLayout ll_language_setting;
    private AlertDialog alertLanguage;

    public static TwoFragment newInstance(String param1, String param2) {
        TwoFragment fragment = new TwoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_two, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();

        //   mPeriodEventHdr.postDelayed(mUpdateUiRunnable, 3000);
    }

    private void initView() {
        mDataLeft = new ArrayList<>();
        mDatasRight = new ArrayList<>();
        re_left = (RecyclerView) getActivity().findViewById(R.id.review_left);
        re_right = (RecyclerView) getActivity().findViewById(R.id.review_right);
        btn_video = (Button) getActivity().findViewById(R.id.btn_video);
        btn_picture = (Button) getActivity().findViewById(R.id.btn_picture);
        btn_empty = (Button) getActivity().findViewById(R.id.btn_empty);
        videoView = (VideoView) getActivity().findViewById(R.id.videoView);
        picSwitch = (PictureSwitch) getActivity().findViewById(R.id.slImages);
        picSwitch.setInAnimation(getActivity(), android.R.anim.fade_in);
        picSwitch.setOutAnimation(getActivity(), android.R.anim.fade_out);
        li_select = (LinearLayout) getActivity().findViewById(R.id.li_select);
        re_video_pic = (RelativeLayout) getActivity().findViewById(R.id.re_video_pic);

        ll_language_setting = (LinearLayout) getActivity().findViewById(R.id.ll_language_setting);
        ll_language_setting.setOnClickListener(this);

        iv_language = (ImageView) getActivity().findViewById(R.id.iv_language);
        iv_language.setImageDrawable(LanguageManager.getLanguageFlag(getActivity()));

        call_big = (TextView) getActivity().findViewById(R.id.tv_call_big);
        line = (TextView) getActivity().findViewById(R.id.tv_line);
        lines = (TextView) getActivity().findViewById(R.id.tv_lines);
        re_video_pic.setOnTouchListener(this);
        li_select.setVisibility(View.GONE);
        btn_video.setOnClickListener(this);
        btn_picture.setOnClickListener(this);
        btn_empty.setOnClickListener(this);
        mVideoResManager = new VideoResManager(getActivity());

        if (vid == 1) {

            re_right.setVisibility(View.GONE);
            re_left.setVisibility(View.VISIBLE);
        } else {
            re_right.setVisibility(View.VISIBLE);
            re_left.setVisibility(View.VISIBLE);
        }
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity()); //设置布局管理器
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity()); //设置布局管理器


        re_left.setLayoutManager(layoutManager1); //设置为垂直布局，这也是默认的
        re_right.setLayoutManager(layoutManager2);

//

        //  layoutManager1.setOrientation(OrientationHelper.VERTICAL);

        mAdapterLeft = new MycallAdapter(getActivity(), mDataLeft, new RvListener() {
            @Override
            public void onItemClick(int id, int position) {


            }
        });

        mAdapterRight = new MycallAdapter(getActivity(), mDatasRight, new RvListener() {
            @Override
            public void onItemClick(int id, int position) {


            }
        });

        if (vid == 1) {
            re_left.setAdapter(mAdapterLeft);
        } else {
            re_left.setAdapter(mAdapterLeft);
            re_right.setAdapter(mAdapterRight);
        }

        //  initData();
    }


    private void initData() {
        List<CallBean> callList = App.instance.getCallList();

        if (callList != null) {
            if (vid == 1) {
                mDataLeft = callList;
                mAdapterLeft.notifyDataSetChanged();

            } else {


                for (int i = 0; i < callList.size(); i++) {

                    int tag = callList.get(i).getCallTag();
                    CallBean call = callList.get(i);
                    if (tag % 2 == 1) {
                        mDataLeft.add(call);
                    } else {
                        mDatasRight.add(call);
                    }
                }
                mAdapterLeft.notifyDataSetChanged();
                mAdapterRight.notifyDataSetChanged();

            }


//
//        for (int i = 0; i < 20; i++) {
//
//            CallBean call = new CallBean();
//            call.setId(i);
//            call.setName("name " + i);
//            mDatas.add(call);
//        }

//        Collections.reverse(mDatas);
//        mAdapter.notifyDataSetChanged();
        }
    }

    public void setViewId(int vid, Handler mhandler) {
        this.vid = vid;
        this.handlers = mhandler;
    }

    public void addData(int position, CallBean call) {


        if (vid == 1) {

            lines.setVisibility(View.VISIBLE);
            if (mDataLeft != null) {
                for (int i = 0; i < mDataLeft.size(); i++) {

                    if (mDataLeft.get(i).getCallNumber().equals(call.getCallNumber())) {
                        mDataLeft.remove(i);
                        mAdapterLeft.notifyDataSetChanged();
                    } else {
                        //  App.instance.setCall(call);
                    }
                }
            }
            mDataLeft.add(position, call);
            mAdapterLeft.notifyItemInserted(position);
            re_left.scrollToPosition(position);

        } else {
            line.setVisibility(View.VISIBLE);
            lines.setVisibility(View.VISIBLE);
            int v = call.getCallTag() % 2;
            switch (v) {
                case 0:
                    int size = mDataLeft.size();
                    for (int i = size - 1; i >= 0; i--) {

                        if (mDataLeft.get(i).getCallNumber().equals(call.getCallNumber())) {
                            mDataLeft.remove(i);
                            mAdapterLeft.notifyDataSetChanged();
                        } else {
                            App.instance.setCall(call);
                        }

                    }
                    mDataLeft.add(position, call);
                    mAdapterLeft.notifyItemInserted(position);
                    re_left.scrollToPosition(position);
                    break;

                case 1:
                    int rsize = mDatasRight.size();
                    for (int i = rsize - 1; i >= 0; i--) {

                        if (mDatasRight.get(i).getCallNumber().equals(call.getCallNumber())) {
                            mDatasRight.remove(i);
                            mAdapterLeft.notifyDataSetChanged();
                        } else {
                            App.instance.setCall(call);
                        }

                    }
                    mDatasRight.add(position, call);
                    mAdapterRight.notifyItemInserted(position);
                    re_right.scrollToPosition(position);
                    break;

            }
        }

//

    }


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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        BugseeHelper.buttonClicked(v);
        Intent intent = new Intent(getActivity(), FileDialog.class);
        switch (v.getId()) {
            case R.id.btn_picture:
                // 选择图片文件

                intent.putExtra(FileDialog.INP_FILE_FILTER, getResources()
                        .getStringArray(R.array.fileEndingImage));
                intent.putExtra(FileDialog.INP_CURRENT_PATH, TvPref.readVideoFile());
                startActivityForResult(intent, 1);
                break;
            case R.id.btn_video:

                intent.putExtra(FileDialog.INP_FILE_FILTER, getResources()
                        .getStringArray(R.array.fileEndingVideo));
                intent.putExtra(FileDialog.INP_CURRENT_PATH, TvPref.readVideoFile());
                startActivityForResult(intent, 2);
                break;

            case R.id.btn_empty:

                if (videoView.isPlaying()) {
                    videoView.stopPlayback();
                }
                if (picSwitch.isPlaying()) {
                    picSwitch.stopPlay();
                }
                TvPref.saveVideoFile("");
                TvPref.saveImageFilePath("");
//                VideoResManager.DelFilesExcept(ShopInfo.getVideoPath(),
////                        new String[0]);
                videoView.setVisibility(View.GONE);
                picSwitch.setVisibility(View.GONE);
                mVideoResManager.UpdateVideo();
                updateView();
                mDatasRight.clear();
                mDataLeft.clear();
                mAdapterLeft.notifyDataSetChanged();
                mAdapterRight.notifyDataSetChanged();
                call_big.setVisibility(View.GONE);


                break;
            case R.id.ll_language_setting: {
                alertLanguage = LanguageManager.alertLanguage(getActivity(), this);
            }
        }

    }

    public void dataClear() {
        call_big.setVisibility(View.GONE);
        mDatasRight.clear();
        mDataLeft.clear();
        mAdapterLeft.notifyDataSetChanged();
        mAdapterRight.notifyDataSetChanged();
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
                    call_big.setVisibility(View.GONE);

                } else {
                    Toast.makeText(getActivity(), getActivity().getString(R.string.image_file_not_selected), Toast.LENGTH_SHORT).show();
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
                    call_big.setVisibility(View.GONE);

                } else {
                    Toast.makeText(getActivity(), getActivity().getString(R.string.video_file_not_selected), Toast.LENGTH_SHORT).show();
                }
            }
        }
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
     * 配置幻灯片中的图片--播放幻灯片
     */
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

    void updateVideoView() {
        mVideoResManager.UpdateVideo();
        updateView();
        startPlay(false);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (videoView != null && videoView.isPlaying()) {
            videoView.stopPlayback();
        }
        mPeriodEventHdr.removeCallbacks(mUpdateUiRunnable);
        mPeriodEventHdr.removeCallbacks(mRunnable);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //    Toast.makeText(getActivity(), "down事件", Toast.LENGTH_SHORT).show();

                if (isOpen) {
                    isOpen = false;
                    li_select.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isOpen = true;
                            li_select.setVisibility(View.GONE);
                        }
                    }, 5000);

                } else {
//                    isOpen=true;
//                    li_select.setVisibility(View.VISIBLE);
                }
                break;
            case MotionEvent.ACTION_UP:
//                Toast.makeText(MainActivity.this, "up事件", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return true;
    }

    public void getVideoPause(String name) {
        //暂停播放
        if (videoView.getVisibility() == View.VISIBLE) {
            videoView.pause();
        }
        if (videoView.getVisibility() == View.GONE && picSwitch.getVisibility() == View.GONE) {
            call_big.setText(EditTextUtil.formatLocale(name));
            call_big.setVisibility(View.VISIBLE);
//            scaleAnimation = (ScaleAnimation) AnimationUtils.loadAnimation(getActivity(), R.anim.scale);
//            call_big.startAnimation(scaleAnimation);
            //  startScaleAnimation(call_big);

            ((MainActivity) getActivity()).sAnimation(call_big);
        } else {
            call_big.setVisibility(View.GONE);
        }
    }


    public void getVideoAgain() {
        //继续播放
        if (videoView.getVisibility() == View.VISIBLE) {
            videoView.start();
            call_big.setVisibility(View.GONE);
        }


        //    scaleAnimation.cancel();
        //  mAdapterLeft.setAnimation();

    }

    @Override
    public void onResume() {


        if (videoView.getVisibility() == View.GONE && picSwitch.getVisibility() == View.GONE) {

        } else {
            updateView();
            startPlay(false);
        }


        super.onResume();
    }


    public void onDestroy() {
        //    App.instance.setSave();
        if (picSwitch.isPlaying()) {
            picSwitch.stopPlay();
        }
        if (videoView != null && videoView.isPlaying()) {
            videoView.stopPlayback();
        }
        super.onDestroy();

    }

    @Override
    public void onPause() {
        super.onPause();
        // App.instance.setSave();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the com.alfredselfhelp.activity and potentially other fragments contained in that
     * com.alfredselfhelp.activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void setLanguage(final String language) {
        if (alertLanguage != null) {
            alertLanguage.dismiss();
        }
        App.getTopActivity().changeLanguage(language);
    }

}
