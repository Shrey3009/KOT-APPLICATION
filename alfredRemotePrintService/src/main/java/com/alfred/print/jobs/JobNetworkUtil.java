package com.alfred.print.jobs;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.birbit.android.jobqueue.network.NetworkUtil;
import com.birbit.android.jobqueue.network.NetworkUtilImpl;

public class JobNetworkUtil extends NetworkUtilImpl{


    public JobNetworkUtil(Context context) {
        super(context);
    }

    @Override
    public int getNetworkStatus(Context context) {
//        if (isDozing(context)) {
//            return NetworkUtil.DISCONNECTED;
//        }
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter.isEnabled()) {
            return NetworkUtil.UNMETERED;
        }
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo == null) {
            return NetworkUtil.DISCONNECTED;
        }
        if (netInfo.getType() == ConnectivityManager.TYPE_WIFI ||
                netInfo.getType() == ConnectivityManager.TYPE_ETHERNET) {
            return NetworkUtil.UNMETERED;
        }
        return NetworkUtil.METERED;
    }
}
