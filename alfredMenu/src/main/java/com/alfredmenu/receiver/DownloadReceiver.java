package com.alfredmenu.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import com.alfredbase.http.DownloadFactory;
import com.alfredbase.store.Store;
import com.alfredbase.utils.LogUtil;
import com.alfredmenu.global.App;

/**
 * Created by Alex on 17/1/23.
 */

public class DownloadReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                long id1 = intent.getLongExtra(DownloadManager.INTENT_EXTRAS_SORT_BY_SIZE, -1);
                LogUtil.e("jidu", id1 + "");
                long downloadId = Store.getLong(context, "posUpdateId");
                if(id == downloadId){
                    DownloadManager.Query query = new DownloadManager.Query();
                    DownloadManager downloadManager = (DownloadManager) App.getTopActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                    Cursor cursor = downloadManager.query(query);
                    while(cursor.moveToNext()){
                        int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                        if(status == DownloadManager.STATUS_SUCCESSFUL){
                            String address = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                            if(!TextUtils.isEmpty(address)) {
                                if(context.getPackageName().equals(DownloadFactory.getApkInfo(context, Uri.parse(address).getPath()).packageName)) {
                                    DownloadFactory.installApk(App.getTopActivity(), address);
                                    return;
                                }
                            }
                        }
                    }
                    cursor.close();
                }
    }
}
