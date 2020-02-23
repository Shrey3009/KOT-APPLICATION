package com.alfred.callnum.utils;

import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alfred.callnum.activity.ConnectMainPos;
import com.alfred.callnum.activity.MainActivity;
import com.alfred.callnum.activity.SelectRevenue;
import com.alfredbase.BaseActivity;
import com.alfredbase.BaseApplication;
import com.alfredbase.utils.TextTypeFace;

public class UIHelp {
    public static void startConnectPOS(Activity context) {
        Intent intent = new Intent(context, ConnectMainPos.class);
        context.startActivity(intent);
    }

    public static void startMainActivity(Activity context, int viewId){
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("viewId", viewId);
        context.startActivity(intent);
    }

    public static void startSelectRevenue(Activity context){
        Intent intent = new Intent(context, SelectRevenue.class);
        context.startActivity(intent);
    }

    public static void showToast(BaseActivity context, String text) {
        Toast toast = new Toast(context);
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(com.alfredbase.R.layout.toast_view, null);
        TextView tv_toast_view = (TextView) view.findViewById(com.alfredbase.R.id.tv_toast_view);
        tv_toast_view.setText(text);
        TextTypeFace textTypeFace = TextTypeFace.getInstance();
        textTypeFace.setTrajanProRegular(tv_toast_view);
        toast.setGravity(Gravity.BOTTOM, 0, 10);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }

    public static void showToast(BaseApplication context, String text) {
        Toast toast = new Toast(context);
        LayoutInflater inflater = context.getTopActivity().getLayoutInflater();
        View view = inflater.inflate(com.alfredbase.R.layout.toast_view, null);
        TextView tv_toast_view = (TextView) view.findViewById(com.alfredbase.R.id.tv_toast_view);
        tv_toast_view.setText(text);
        TextTypeFace textTypeFace = TextTypeFace.getInstance();
        textTypeFace.setTrajanProRegular(tv_toast_view);
        toast.setGravity(Gravity.BOTTOM, 0, 10);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }
}
