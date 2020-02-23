package com.alfredbase.javabean;

import android.bluetooth.BluetoothDevice;
import android.text.TextUtils;
import android.view.View;

import com.alfredbase.R;

public class PrintBean {

    public static final int PRINT_TYPE = 1664;
    //蓝牙-名称
    public String name;
    //蓝牙-地址
    public String address;
    //蓝牙-设备类型
    public int type;
    //蓝牙-是否已经匹配
    public boolean isConnect;

    BluetoothDevice device;

    /**
     * @param devicee 蓝牙设备对象
     */
    public PrintBean(BluetoothDevice devicee) {
        this.name = TextUtils.isEmpty(devicee.getName()) ? "Unknown" : devicee.getName();
        this.address = devicee.getAddress();
        this.isConnect = devicee.getBondState() == BluetoothDevice.BOND_BONDED;
        this.type = devicee.getBluetoothClass().getDeviceClass();
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    /**
     * 260-电脑
     * 1664-打印机
     * 524-智能手机
     *
     * @return
     */
    public int getTypeIcon() {
        if (type == 260) {
            return 0;
        } else if (type == PRINT_TYPE) {
            return 0;
        } else if (type == 524) {
            return 0;
        } else {
            return 0;
        }
    }

    public String getDeviceType(View view) {
        if (type == PRINT_TYPE) {

            return isConnect ? view.getContext().getString(R.string.select_printer) :  view.getContext().getString(R.string.click_to_connect);
        } else {

            return view.getContext().getString(R.string.not_found);
        }
    }

    public int getType() {
        return type;
    }

    public boolean isConnect() {
        return isConnect;
    }

    public BluetoothDevice getDevice() {

        return device;
    }

    public void setConnect(boolean connect) {
        isConnect = connect;
    }
}
