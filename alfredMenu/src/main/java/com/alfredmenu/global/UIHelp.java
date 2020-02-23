package com.alfredmenu.global;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alfredbase.BaseActivity;
import com.alfredbase.R;
import com.alfredbase.javabean.ItemCategory;
import com.alfredbase.javabean.ItemDetail;

import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.utils.TextTypeFace;

import com.alfredmenu.activity.ConnectPOS;
import com.alfredmenu.activity.DevicesActivity;
import com.alfredmenu.activity.EmployeeID;
import com.alfredmenu.activity.InstructionDetail;
import com.alfredmenu.activity.KOTNotification;
import com.alfredmenu.activity.Login;
import com.alfredmenu.activity.MainPage;
import com.alfredmenu.activity.MenuCategoryPage;
import com.alfredmenu.activity.MenuListPage;
import com.alfredmenu.activity.MenuMainCategoryPage;

import com.alfredmenu.activity.MenuOrderPage;
import com.alfredmenu.activity.MenuPage;
import com.alfredmenu.activity.ModifierDetail;
import com.alfredmenu.activity.OrderDetailsTotal;

import com.alfredmenu.activity.OrderReceiptDetails;
import com.alfredmenu.activity.SelectRevenue;
import com.alfredmenu.activity.Setting;
import com.alfredmenu.activity.TablesPage;
import com.alfredmenu.activity.OrderDetailPage;
import com.alfredmenu.adapter.MainCategoryAdapter;

public class UIHelp {
    public static void startConnectPOS(BaseActivity context) {
        Intent intent = new Intent(context, ConnectPOS.class);
        context.startActivity(intent);
    }

    public static void startEmployeeID(BaseActivity context) {
        Intent intent = new Intent(context, EmployeeID.class);
        context.startActivity(intent);
        // context.overridePendingTransition(R.anim.slide_bottom_in,
        // R.anim.slide_top_out);
    }

    public static void startLogin(BaseActivity context) {
        Intent intent = new Intent(context, Login.class);
        context.startActivity(intent);
        // context.overridePendingTransition(R.anim.slide_bottom_in,
        // R.anim.slide_top_out);
    }

    public static void startSelectRevenue(BaseActivity context) {
        Intent intent = new Intent(context, SelectRevenue.class);
        context.startActivity(intent);
        // context.overridePendingTransition(R.anim.slide_bottom_in,
        // R.anim.slide_top_out);
    }

    public static void startSelectRevenue(BaseActivity context, HashMap<String, Object> map) {
        Intent intent = new Intent(context, SelectRevenue.class);
        intent.putExtra("revenueMap", map);
        context.startActivity(intent);
        // context.overridePendingTransition(R.anim.slide_bottom_in,
        // R.anim.slide_top_out);
    }

    public static void startSetting(BaseActivity context, HashMap<String, Object> map, Order currentOrder) {
        Intent intent = new Intent(context, Setting.class);
        intent.putExtra("attrMap", map);
        intent.putExtra("order", currentOrder);
        context.startActivity(intent);
//		context.overridePendingTransition(R.anim.slide_bottom_in,
//				 R.anim.slide_top_out);
    }

    public static void startTables(BaseActivity context) {
        Intent intent = new Intent(context, TablesPage.class);
        context.startActivity(intent);
        // context.overridePendingTransition(R.anim.slide_bottom_in,
        // R.anim.slide_top_out);
    }

    public static void startMenuPage(BaseActivity context) {
        Intent intent = new Intent(context, MenuPage.class);
        context.startActivity(intent);
        // context.overridePendingTransition(R.anim.slide_bottom_in,
        // R.anim.slide_top_out);
    }

    public static void startKOTNotification(BaseActivity context) {
        Intent intent = new Intent(context, KOTNotification.class);
//		intent.putExtra("notifications", notifications);
        context.startActivity(intent);
        // context.overridePendingTransition(R.anim.slide_bottom_in,
        // R.anim.slide_top_out);
    }

    public static void startOrderDetail(BaseActivity context, Order order, ItemDetail itemDetail, OrderDetail orderDetail, int currentGroupId) {
        Intent intent = new Intent(context, OrderDetailPage.class);
        intent.putExtra("order", order);
        intent.putExtra("itemDetail", (Parcelable) itemDetail);
        intent.putExtra("orderDetail", orderDetail);
        intent.putExtra("currentGroupId", currentGroupId);
        context.startActivity(intent);
    }

    public static void startMainPage(BaseActivity context, Order order,String id, String mainCategoryId) {
        Intent intent = new Intent(context, MainPage.class);
        intent.putExtra("order", order);
        intent.putExtra("itemId", id);
        intent.putExtra("itemMainCategoryId", mainCategoryId);
        context.startActivity(intent);
    }

    public static void startMenuCategoryPage(BaseActivity context, Order order, int mainCategoryId) {
        Intent intent = new Intent(context, MenuCategoryPage.class);
        intent.putExtra("order", order);
        intent.putExtra("mainCategoryId", mainCategoryId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    public static void startMenuMainCategoryPage(BaseActivity context, Order order) {
        Intent intent = new Intent(context, MenuMainCategoryPage.class);
        intent.putExtra("order", order);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }


    public static void startMenuCategoryPage1(BaseActivity context, Order order) {
        Intent intent = new Intent(context, MenuCategoryPage.class);
        intent.putExtra("order", order);

    }

    public static void startMenuListPage(BaseActivity context, String id,String mainCategoryId){
        Intent intent = new Intent(context, MenuListPage.class);
        intent.putExtra("itemId", id);
        intent.putExtra("itemMainCategoryId",mainCategoryId);
        context.startActivity(intent);
    }

    public static void startMenuOrderPage(Context context,String id, String mainCategoryId){
        Intent intent = new Intent(context, MenuOrderPage.class);
        intent.putExtra("itemId", id);
        intent.putExtra("itemMainCategoryId", mainCategoryId);
        context.startActivity(intent);
    }


    public static void startDevices(BaseActivity context){
        Intent intent = new Intent(context, DevicesActivity.class);
        context.startActivity(intent);
    }

    public static void startModifierDetail(BaseActivity context, ItemDetail itemDetail, Order order, OrderDetail orderDetail) {
        Intent intent = new Intent(context, ModifierDetail.class);
        intent.putExtra("itemDetail", (Parcelable) itemDetail);
        intent.putExtra("order", order);
        intent.putExtra("orderDetail", orderDetail);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.slide_bottom_in,
                R.anim.anim_null);
    }

    public static void startInstructionDetail(BaseActivity context, OrderDetail orderDetail, int requestCode) {
        Intent intent = new Intent(context, InstructionDetail.class);
        intent.putExtra("orderDetail", orderDetail);
        context.startActivityForResult(intent, requestCode);
        context.overridePendingTransition(R.anim.slide_bottom_in,
                R.anim.slide_top_out);
    }

    public static void startOrderDetailsTotal(BaseActivity context, Order order) {
        Intent intent = new Intent(context, OrderDetailsTotal.class);
        intent.putExtra("order", order);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.slide_bottom_in,
                R.anim.anim_null);
    }

    public static void startOrderReceiptDetails(BaseActivity context, Order order) {
        Intent intent = new Intent(context, OrderReceiptDetails.class);
        intent.putExtra("order", order);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.slide_bottom_in,
                R.anim.anim_null);
    }

//	public static void showToast(BaseActivity context, String text) {
//		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
//	}

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

}
