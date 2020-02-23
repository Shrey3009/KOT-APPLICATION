package com.alfredkds.view;

import android.content.Context;

import com.alfredbase.store.Store;
import com.alfredbase.utils.TimeUtil;

import java.util.Calendar;

public class SystemSettings {
    private Context context;

    public static final int MODE_NORMAL = 0;
    public static final int MODE_BALANCE = 1;
    public static final int MODE_STACK = 2;
    private boolean islandscape = false;

    public SystemSettings(Context context) {
        super();
        this.context = context;
    }

    public void setKdsLan(Integer kdslan) {
        Store.putInt(this.context, Store.PRINT_LABLE,
                kdslan.intValue());
        if (kdslan.intValue() == 1)
            this.islandscape = true;
        else
            this.islandscape = false;
    }

    public boolean isKdsLan() {
        Integer value = Store.getInt(context,
                Store.PRINT_LABLE);
        if (value != null && value != Store.DEFAULT_INT_TYPE) {
            if (value.intValue() == 1) {
                this.islandscape = true;
            } else {
                this.islandscape = false;
            }
        }
        return islandscape;
    }

    public void setBalancerMode(Integer mode) {
        Store.putInt(this.context, Store.MODE_BALANCER, mode);
    }

    public int getBalancerMode() {
        return Store.getInt(this.context, Store.MODE_BALANCER);
    }

    public void setPendingList(boolean value) {
        Store.putBoolean(this.context, Store.PENDING_LIST, value);
    }

    public boolean isPendingList() {
        return Store.getBoolean(this.context, Store.PENDING_LIST, false);
    }

    public void setStackCount(int count) {
        Store.putInt(this.context, Store.STACK_COUNT, count);
    }

    public int getStackCount() {
        return Store.getInt(this.context, Store.STACK_COUNT, 0);
    }

    public void setBalancerTime(long time, int type) {
        if (type == 1)
            Store.putLong(this.context, Store.BALANCER_TIME_END, time);
        else
            Store.putLong(this.context, Store.BALANCER_TIME_START, time);
    }

    public long getBalancerTime(int type) {
        if (type == 1)
            return Store.getLong(this.context, Store.BALANCER_TIME_END);
        else
            return Store.getLong(this.context, Store.BALANCER_TIME_START);
    }

    public boolean isBalancerTimeHasCome() {
        Calendar cal = Calendar.getInstance();
        long timeMillis = TimeUtil.getMillisOfTime(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
        long balancerTimeStart = getBalancerTime(0);

        return timeMillis >= balancerTimeStart;
    }

    public boolean isBalancerTimeEnded() {
        Calendar cal = Calendar.getInstance();
        long timeMillis = TimeUtil.getMillisOfTime(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
        long balancerTimeEnd = getBalancerTime(1);

        return timeMillis >= balancerTimeEnd;
    }

    public void setKdsOnline(int value) {
        Store.putInt(this.context, Store.KDS_ONLINE, value);
    }

    public int isKdsOnline() {
        return Store.getInt(this.context, Store.KDS_ONLINE, 0);
    }

    public void setAllowPartial(boolean value) {
        Store.putBoolean(this.context, Store.ALLOW_PARTIAL, value);
    }

    public boolean isAllowPartial() {
        return Store.getBoolean(this.context, Store.ALLOW_PARTIAL, false);
    }

    public void setCrashReportStatus(boolean active) {
        Store.putBoolean(this.context, Store.BUGSEE_STATUS, active);
    }

    public boolean isCrashReportActive() {
        return Store.getBoolean(this.context, Store.BUGSEE_STATUS, true);
    }

}
