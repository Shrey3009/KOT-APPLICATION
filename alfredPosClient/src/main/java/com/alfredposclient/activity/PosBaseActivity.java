package com.alfredposclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.alfredbase.BaseActivity;
import com.alfredbase.http.ResultCode;
import com.alfredposclient.activity.kioskactivity.subpos.SubPosManagePage;
import com.alfredposclient.global.App;
import com.alfredposclient.service.HomeService;

public class PosBaseActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getDiff();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void getDiff() {
        if (App.getTopActivity() instanceof MainPage) {
           DifferentDislay.setVisibility(true);
        }else {
            DifferentDislay.setVisibility(false);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intentService = new Intent(App.instance,HomeService.class);
        startService(intentService);

    }
}
