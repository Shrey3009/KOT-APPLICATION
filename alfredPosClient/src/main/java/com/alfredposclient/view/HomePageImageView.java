package com.alfredposclient.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;


import com.alfredbase.ParamConst;
import com.alfredbase.store.Store;
import com.alfredbase.utils.CommonUtil;
import com.alfredposclient.R;
import com.alfredposclient.global.App;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Zun on 2016/12/16.
 */

public class HomePageImageView extends LinearLayout {

    private ImageView home_page_img;
    private RelativeLayout home_page_sv;
    private SurfaceView sv;
    private List<String> list = new ArrayList<>();
    private Context context;
    private MediaPlayer mediaPlayer;
    private SurfaceHolder surfaceHolder;
    private int curPosition = 0;
    private boolean isSurfaceCreated = false; //surface是否已经创建好  
    private Bitmap qrBitmap;
    private String qrTitle;
    private String qrTotal;
    private TextView qrText;
    private TextView qrPrice;
 //   private File file;
 private String  file;
    private SurfaceHolder holder;
    private VideoView mVideoView;
    Uri uri = null;

    public HomePageImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public HomePageImageView(Context context, List<String> list) {
        super(context);
        this.list = list;
        this.context = context;
        initView(context);
    }

    public void refresh(List<String> list){
        this.list = list;
        refreshUI();
    }

    public void setQrBitmap(Bitmap qrBitmap, String qrTitle, String qrTotal)
    {
        this.qrBitmap = qrBitmap;
        this.qrTitle = qrTitle;
        this.qrTotal = qrTotal;
        refreshUI();
    }

    /**
     * 释放播放器资源
     */
    public void releasePlayer(){
        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

     //   stopPlaybackVideo();
    }

    public void restart(){
        if (!isSurfaceCreated){
            CreateSurface();
        }
    }

    /**
     * 视频暂停
     */
    public void pause(){
        if (mediaPlayer != null && mediaPlayer.isPlaying()){
            curPosition = mediaPlayer.getCurrentPosition();
            mediaPlayer.pause();
        }
    }

    public void resume(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(file)) {
                    playMedia(uri, curPosition);
                }
            }
        }, 10);
    }

    private void initView(Context context){
        View view = inflate(context, R.layout.home_page_img, this);
        home_page_img = (ImageView) view.findViewById(R.id.home_page_img);
        home_page_sv = (RelativeLayout) view.findViewById(R.id.home_page_sv);
        qrText = (TextView) view.findViewById(R.id.qrText);
        qrPrice = (TextView) view.findViewById(R.id.qrPrice);
       sv = (SurfaceView) findViewById(R.id.sv);
        mediaPlayer = new MediaPlayer();
        mVideoView=(VideoView)view.findViewById(R.id.vvideo);
        timeTask();
        refreshUI();
    }

    int index = 0;
    private void refreshUI(){
        boolean flag = Store.getBoolean(context, Store.VIDEO_IMAGE, true);
        int style= Store.getInt(App.getTopActivity(), Store.SUNMI_STYLE);

        if(qrBitmap != null)
        {
            home_page_sv.setVisibility(GONE);
            qrText.setVisibility(VISIBLE);
            qrPrice.setVisibility(VISIBLE);
            qrText.setText(qrTitle);
            qrPrice.setText(qrTotal);
            BitmapDrawable background = new BitmapDrawable(this.getResources(), qrBitmap);
            home_page_img.setBackgroundColor(Color.WHITE);
            home_page_img.getLayoutParams().height = 400;
            home_page_img.getLayoutParams().width = 400;
            home_page_img.setBackground(background);
        }
        else if (style!=Store.SUNMI_VIDEO_TEXT&&style!=Store.SUNMI_VIDEO)
        {
            home_page_img.setVisibility(VISIBLE);
            home_page_img.getLayoutParams().height = LayoutParams.MATCH_PARENT;
            home_page_img.getLayoutParams().width = LayoutParams.MATCH_PARENT;
            qrText.setVisibility(GONE);
            qrPrice.setVisibility(GONE);
            home_page_sv.setVisibility(GONE);
            if (list != null && list.size() > 0)
            {
                if (list.size() == 1) {
                   // ((BitmapDrawable)home_page_img.getDrawable()).getBitmap().recycle();
                    home_page_img.setImageURI(Uri.parse(list.get(0)));
                }
            } else {
                home_page_img.setBackgroundResource(R.drawable.picture);
            }
        }
        else
        {
            home_page_img.setVisibility(GONE);
            qrText.setVisibility(GONE);
            qrPrice.setVisibility(GONE);
            home_page_sv.setVisibility(GONE);
            home_page_sv.setVisibility(VISIBLE);
        }
           //  setupVideo();
        if(list.size()!=0){
            if (!CommonUtil.isNull(list.get(0))){
                file = list.get(0);
                uri=Uri.parse(file);
                if (!TextUtils.isEmpty(file)) {
                    CreateSurface();
                    playMedia(uri, curPosition);
                }

            }
        }

//        try {
//            String uri="http://video.dispatch.tc.qq.com/77613075/x0021o8d3g3.mp4?sdtfrom=v1001&type=mp4&vkey=23289E4B8D0F4B6CF18703222DFD0038845D8F56A75EEC20D5D4FDE678093D9AB211EFD7F4C99E5A612A96A04F46CEEB483628CFFBEA493D3AADBFCB81A540F7A92193874192FA0F70D1099DF330B2B419D45736554CB9BB3435019C985F530C5960E4B20FEBD5FAED17DC9F1FCE1C73&platform=10902&fmt=auto&sp=350&guid=1175defd049d3301e047ce50d93e9c7a";
//            mediaPlayer.setDataSource(context, Uri.parse(list.get(0)));
//            holder=sv.getHolder();
//            holder.addCallback(new MyCallBack());
//            mediaPlayer.prepare();
//            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                @Override
//                public void onPrepared(MediaPlayer mp) {
//
//                    mediaPlayer.start();
//                    mediaPlayer.setLooping(true);
//                }
//            });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }



    private void setupVideo() {

        try {
           //  uri = Uri.parse("android.resource://" + context.getPackageName() + "/raw/" + R.raw.aaa);
            uri = Uri.parse(list.get(0));
            mVideoView.setVideoURI(uri);
            mVideoView.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                mVideoView.start();
//            }
//        });





        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                mp.start();
                mp.setLooping(true);
              //  stopPlaybackVideo();
            }
        });

//        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                mp.start();
//                mp.setLooping(true);
//            }
//        });
//        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
//            @Override
//            public boolean onError(MediaPlayer mp, int what, int extra) {
//                stopPlaybackVideo();
//                return true;
//            }
//        });


    }

    private void stopPlaybackVideo() {
        try {
            mVideoView.stopPlayback();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class MyCallBack implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            mediaPlayer.setDisplay(holder);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    }

    /**
     * 创建视频展示页面
     */
    private void CreateSurface(){
        surfaceHolder = sv.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.addCallback(callback);
    }

    SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            isSurfaceCreated = true;
            mediaPlayer.setDisplay(surfaceHolder);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            isSurfaceCreated = false;
            if (mediaPlayer.isPlaying()){
                curPosition = mediaPlayer.getCurrentPosition();
                mediaPlayer.stop();
            }
        }
    };

    /**
     * mediaPlayer播放
     */
    private void playMedia(Uri file, final int currentPosition){
        try {
            mediaPlayer.reset();
           //   uri = Uri.parse("android.resource://" + context.getPackageName() + "/raw/" + R.raw.aaa);
            mediaPlayer.setDataSource(context,uri);
//            mediaPlayer.setDisplay(sv.getHolder());
          //  mediaPlayer.setLooping(true);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    int w = mediaPlayer.getVideoWidth();
                    int h = mediaPlayer.getVideoHeight();
                    Log.d("^^^^^^^^^^^^^^^^", w + "" + h + "");
                    if (w != 0 && h != 0){
                        surfaceHolder.setFixedSize(w / 2, h);
                    }
                    mediaPlayer.seekTo(currentPosition);
                    mediaPlayer.start();
                 //   mediaPlayer.setLooping(true);
                }
            });


//            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer arg0) {
////                    if (mediaPlayer != null) {
////                        mediaPlayer.stop();
////                        mediaPlayer.release();
////                        mediaPlayer = null;
////                    }
////                    if (!isSurfaceCreated){
////                        CreateSurface();
////                    }
////                //    CreateSurface();
////                    playMedia(uri, curPosition);
//                    mediaPlayer.start();
//                    mediaPlayer.setLooping(true);
//                }
//            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    Observable<Long> observeOn;
    private void timeTask(){
        observeOn = Observable.timer(3, TimeUnit.SECONDS);
        observeOn.observeOn(AndroidSchedulers.mainThread())
                .subscribe(action);
    }

    Action1 action = new Action1<Long>() {
        @Override
        public void call(Long aLong) {
            if(list!=null && list.size() > 1) {
                home_page_img.setVisibility(VISIBLE);
                home_page_sv.setVisibility(GONE);
                index = (index + 1) % list.size();
                home_page_img.setImageURI(Uri.parse(list.get(index)));
            }
            timeTask();
        }
    };



}
