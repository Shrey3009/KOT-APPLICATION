package com.alfredbase.http;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;

import com.alfredbase.BaseActivity;
import com.alfredbase.R;

public class DownloadFactory {
    public static final int EVENT_DOWNLOAD_INSTALL = 110;

    public static long downloadApk(BaseActivity context, DownloadManager downManager, String url, long downloadId) {
        boolean canDonwload = true;
        if (downloadId != -1L) {
            DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
            Cursor c = downManager.query(query);
            if (c != null) {
                try {
                    if (c.moveToFirst()) {
                        int status = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS));
                        if (status == DownloadManager.STATUS_SUCCESSFUL) {
                            Uri uri = downManager.getUriForDownloadedFile(downloadId);
                            if (uri != null) {
                                PackageInfo packageInfo = getApkInfo(context, uri.getPath());
                                PackageInfo localPackageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                                if (localPackageInfo.packageName.equals(packageInfo.packageName) && localPackageInfo.versionCode < packageInfo.versionCode) {
                                    canDonwload = false;
                                    installApk(context, uri);
                                } else {
                                    downManager.remove(downloadId);
                                }
                            }

                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    c.close();
                }
            }
        }
        if (canDonwload) {
            downloadId = downManager.enqueue(DownloadFactory.downloadFile(context, url, context.getResources().getString(R.string.app_name)));
        }
        return downloadId;
    }

    public static DownloadManager.Request downloadFile(Context context, String url, String fileName) {
        DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse(url));
        // 设置在什么网络情况下进行下载
        request.setAllowedNetworkTypes(Request.NETWORK_WIFI);
        // 设置通知栏标题
        request.setNotificationVisibility(Request.VISIBILITY_VISIBLE);
        request.setTitle(context.getResources().getString(R.string.downloading));
        request.setDescription(fileName + context.getString(R.string.is_downloading));
        request.setAllowedOverRoaming(false);
        try {
            // 设置文件存放目录
            request.setDestinationInExternalFilesDir(context,
                    Environment.DIRECTORY_DOWNLOADS, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
//		request.setAllowedOverMetered()
        return request;
    }

    public static void installApk(BaseActivity activity, String apkName) {
//		File apkfile = new File(apkName);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.parse(apkName),
                "application/vnd.android.package-archive");
        activity.startActivity(i);
    }

    public static void installApk(BaseActivity activity, Uri uri) {
//		File apkfile = new File(apkName);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(uri,
                "application/vnd.android.package-archive");
        activity.startActivity(i);
    }

    /**
     * 获取apk程序信息[packageName,versionName...]
     *
     * @param context Context
     * @param path    apk path
     */
    public static PackageInfo getApkInfo(Context context, String path) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
        if (info != null) {
            return info;
        }
        return null;
    }

}
