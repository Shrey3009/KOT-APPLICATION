package com.alfredbase.view;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: Posss
 * @Package: com.alfredposclient.view
 * @ClassName: FloatViewHelper
 * @Description:
 * @Author:
 * @CreateDate: 2019/2/19 15:29
 * @User: chensong
 * @Date: 2019/2/19 15:29
 */
public class FloatViewHelper {
    private static final String TAG = "FloatViewHelper";
    private static FloatView mFloatView;
    private static List<String> notShowList = new ArrayList();
    static {
        //添加不需要显示的页面
      //  notShowList.add("MainActivity");
    }

    public static void showFloatView(final Context context){

        if(!isAppOnForeground(context)||isTargetRunningForeground(context,notShowList)){
            return;
        }
        if(mFloatView == null){
            mFloatView = new FloatView(context.getApplicationContext());
        }
        mFloatView.show();
    }

    public static void removeFloatView(Context context){
        if(isAppOnForeground(context)&&isTargetRunningForeground(context,notShowList)){
            return;
        }
        if(mFloatView ==null||mFloatView.getWindowToken()==null){
            return;
        }

        mFloatView.dismiss();
    }
    public static void addFilterActivities(List<String> activityNames){
        notShowList.addAll(activityNames);
    }


    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public static boolean isAppOnForeground(Context context) {
        // Returns a list of application processes that are running on the
        // device
        ActivityManager activityManager = (ActivityManager) context
                .getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = context.getApplicationContext().getPackageName();
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;

    }

    public static boolean isTargetRunningForeground(Context context,List<String> targetActivityNames) {
        String topActivityName =  ((Activity)context).getClass().getSimpleName();
        if (!TextUtils.isEmpty(topActivityName) && targetActivityNames.contains(topActivityName)) {
            return true;
        }

        return false;
    }

}

