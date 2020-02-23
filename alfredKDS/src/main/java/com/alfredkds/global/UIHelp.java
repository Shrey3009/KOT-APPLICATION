package com.alfredkds.global;

import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alfredbase.BaseActivity;
import com.alfredbase.BaseApplication;
import com.alfredbase.R;
import com.alfredbase.javabean.Printer;
import com.alfredbase.utils.TextTypeFace;
import com.alfredkds.activity.CallNumActivity;
import com.alfredkds.activity.ConnectPOS;
import com.alfredkds.activity.EmployeeID;
import com.alfredkds.activity.KitchenOrder;
import com.alfredkds.activity.KotHistory;
import com.alfredkds.activity.LogActivity;
import com.alfredkds.activity.Login;
import com.alfredkds.activity.SelectKitchen;
import com.alfredkds.activity.SelectRevenue;
import com.alfredkds.activity.Setting;
import com.alfredkds.activity.Summary;
import com.alfredkds.activity.Welcome;

import java.util.ArrayList;

public class UIHelp {

    public static void startWelcome(BaseActivity context) {
        Intent intent = new Intent(context, Welcome.class);
        context.startActivity(intent);
    }

    public static void startConnectPOS(BaseActivity context) {
        Intent intent = new Intent(context, ConnectPOS.class);
        context.startActivity(intent);
    }

    public static void startSelectRevenue(BaseActivity context) {
        Intent intent = new Intent(context, SelectRevenue.class);
        context.startActivity(intent);
    }

    public static void startEmployeeID(BaseActivity context) {
        Intent intent = new Intent(context, EmployeeID.class);
        context.startActivity(intent);
//		context.overridePendingTransition(R.anim.centre_open,
//				R.anim.slide_bottom_out);
    }

    public static void startLogin(BaseActivity context) {
        Intent intent = new Intent(context, Login.class);
        context.startActivity(intent);
//		context.overridePendingTransition(R.anim.centre_open,
//				R.anim.slide_bottom_out);
    }

    public static void startKitchenOrder(BaseActivity context) {
        Intent intent = new Intent(context, KitchenOrder.class);
        context.startActivity(intent);
//		context.overridePendingTransition(R.anim.slide_bottom_in,
//				R.anim.slide_top_out);
    }

    public static void startLogScreen(BaseActivity context) {
        Intent intent = new Intent(context, LogActivity.class);
        context.startActivity(intent);
    }

    public static void startSelectKitchen(BaseActivity context, ArrayList<Printer> printers) {
        Intent intent = new Intent(context, SelectKitchen.class);
        intent.putExtra("printers", printers);
        context.startActivity(intent);

//		context.overridePendingTransition(R.anim.slide_bottom_in,
//				R.anim.slide_top_out);
    }

    public static void startSetting(BaseActivity context) {
        Intent intent = new Intent(context, Setting.class);
        context.startActivity(intent);
//		context.overridePendingTransition(R.anim.slide_bottom_in,
//		R.anim.slide_top_out);
    }

    public static void startKotHistory(BaseActivity context) {
        Intent intent = new Intent(context, KotHistory.class);
        context.startActivity(intent);
//		context.overridePendingTransition(R.anim.slide_bottom_in,
//		R.anim.slide_top_out);
    }

    public static void startFilter(BaseActivity context) {
        Intent intent = new Intent(context, Summary.class);
        context.startActivity(intent);
//		context.overridePendingTransition(R.anim.slide_bottom_in,
//		R.anim.slide_top_out);
    }
//	public static void showToast(BaseActivity context, String text) {
//		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
//	}

    public static void startCallNum(BaseActivity context) {
        Intent intent = new Intent(context, CallNumActivity.class);
        context.startActivity(intent);
//		context.overridePendingTransition(R.anim.slide_bottom_in,
//		R.anim.slide_top_out);
    }

    public static void showToast(BaseActivity context, String text) {
        Toast toast = new Toast(context);
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.toast_view, null);
        TextView tv_toast_view = (TextView) view.findViewById(R.id.tv_toast_view);
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
        View view = inflater.inflate(R.layout.toast_view, null);
        TextView tv_toast_view = (TextView) view.findViewById(R.id.tv_toast_view);
        tv_toast_view.setText(text);
        TextTypeFace textTypeFace = TextTypeFace.getInstance();
        textTypeFace.setTrajanProRegular(tv_toast_view);
        toast.setGravity(Gravity.BOTTOM, 0, 10);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }


}
