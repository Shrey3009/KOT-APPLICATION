package com.alfredposclient.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.alfredbase.BaseActivity;
import com.alfredbase.store.Store;
import com.alfredbase.utils.LogUtil;
import com.alfredposclient.R;
import com.alfredposclient.global.App;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

public class SunmiActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = SunmiActivity.class.getSimpleName();
    private RadioButton sunmi_select_picture_rbt, sunmi_select_text_rbt, sunmi_select_rbt, sunmi_select_video_text_rbt, sunmi_select_video_rbt;
    private Button style_url_btn, style_welcome_btn, style_video_btn;
    private RadioGroup sunmi_rg;
    private int styleType;
    private List<String> imgsPath = new ArrayList<String>();

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_sunmi);



        sunmi_rg = (RadioGroup) findViewById(R.id.sunmi_rg);
        sunmi_select_picture_rbt = (RadioButton) findViewById(R.id.sunmi_select_picture_rbt);
        sunmi_select_text_rbt = (RadioButton) findViewById(R.id.sunmi_select_text_rbt);
        sunmi_select_rbt = (RadioButton) findViewById(R.id.sunmi_select_rbt);
        sunmi_select_video_rbt = (RadioButton) findViewById(R.id.sunmi_select_video_rbt);
        sunmi_select_video_text_rbt = (RadioButton) findViewById(R.id.sunmi_select_video_text_rbt);

        style_url_btn = (Button) findViewById(R.id.style_url_btn);
        style_welcome_btn = (Button) findViewById(R.id.style_welcome_btn);
        style_video_btn = (Button) findViewById(R.id.style_video_btn);
        style_url_btn.setOnClickListener(this);
        style_welcome_btn.setOnClickListener(this);
        style_video_btn.setOnClickListener(this);

        styleType = Store.getInt(this, Store.SUNMI_STYLE);
        if (styleType == Store.SUNMI_TEXT){
            sunmi_select_text_rbt.setChecked(true);
        }else if (styleType == Store.SUNMI_IMG){
            sunmi_select_picture_rbt.setChecked(true);
        }else if (styleType == Store.SUNMI_IMG_TEXT){
            sunmi_select_rbt.setChecked(true);
        }else if (styleType == Store.SUNMI_VIDEO){
            sunmi_select_video_rbt.setChecked(true);
        }else if (styleType == Store.SUNMI_VIDEO_TEXT){
            sunmi_select_video_text_rbt.setChecked(true);
        }
        sunmi_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId()){
                    case R.id.sunmi_select_picture_rbt:
                        styleType = Store.SUNMI_IMG;
                        break;
                    case R.id.sunmi_select_text_rbt:
                        styleType = Store.SUNMI_TEXT;
                        break;
                    case R.id.sunmi_select_rbt:
                        styleType = Store.SUNMI_IMG_TEXT;
                        break;
                    case R.id.sunmi_select_video_rbt:
                        styleType = Store.SUNMI_VIDEO;
                        break;
                    case R.id.sunmi_select_video_text_rbt:
                        styleType = Store.SUNMI_VIDEO_TEXT;
                        break;
                    default:
                        Store.putInt(SunmiActivity.this, Store.SUNMI_STYLE, styleType);
                        break;
                }
                Store.putInt(SunmiActivity.this, Store.SUNMI_STYLE, styleType);
            }
        });
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.style_url_btn:
                image();
                break;
            case R.id.style_welcome_btn:
                welcome();
                break;
            case R.id.style_video_btn:
//                Intent intent = new Intent();
//                intent.setType("video/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
             //   startActivityForResult(intent, 1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK){
            Uri uri = data.getData();
            Log.d("***************" , uri.toString());
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            String videoPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
            String path = cursor.getString(1);
            String size = cursor.getString(2);
            String name = cursor.getString(3);
            Log.d("++++++++++++++", path);
            Log.d("++++++++++++++", size);
            Log.d("++++++++++++++", name);
            imgsPath.clear();
           // imgsPath.add(path);



            imgsPath.add(videoPath.toString());
            Store.putStrListValue(SunmiActivity.this, Store.SUNMI_DATA, imgsPath);
            Store.putInt(SunmiActivity.this, Store.SUNMI_STYLE, styleType);
        }
    }

    /**
     * 选择本地图片
     */
    private void image(){
        GalleryFinal.openGalleryMuti(1, 50,new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                LogUtil.d(TAG, resultList.toString());
                if (resultList.size() > 0) {
                    imgsPath.clear();
                    int i = resultList.size();
                    style_url_btn.setText(getString(R.string.category_select)+" " + i + "" + " "+getString(R.string.images));
                    for (PhotoInfo p : resultList) {
                        imgsPath.add(p.getPhotoPath());
                    }
                    Store.putStrListValue(SunmiActivity.this, Store.SUNMI_DATA, imgsPath);
                    Store.putInt(SunmiActivity.this, Store.SUNMI_STYLE, styleType);
                }
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {
                LogUtil.d(TAG, errorMsg);
                Toast.makeText(SunmiActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }



    /**
     * 修改欢迎页面
     */
    private void welcome(){
        GalleryFinal.openGallerySingle(1, new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                LogUtil.d(TAG, resultList.toString());
                if (resultList.size() > 0) {
                    String welcomeURL = resultList.get(0).getPhotoPath();
//                    Store.putString(SunmiActivity.this, Store.SUNMI_WELCOME, welcomeURL);
                    Store.putInt(SunmiActivity.this, Store.SUNMI_STYLE, styleType);
                    App.instance.setWelcomeToSecondScreen(welcomeURL);
                    Store.putString(App.instance, Store.WELCOME_PATH, welcomeURL);
                }
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {
                LogUtil.d(TAG, errorMsg);
                Toast.makeText(SunmiActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
