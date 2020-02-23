package com.alfredwaiter.adapter;


import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alfredbase.javabean.model.PrinterDevice;
import com.alfredbase.utils.ButtonClickTimer;
import com.alfredbase.view.CustomAdapter;
import com.alfredwaiter.R;
import com.alfredwaiter.activity.DevicesActivity;
import com.alfredwaiter.global.App;
import com.alfredwaiter.global.KpmDialogFactory;

import java.util.ArrayList;
import java.util.List;

public class DevicesAdapter extends CustomAdapter {

    //private List<PrinterDBModel> printerDBModelList;
    List<PrinterDevice> printerDBModelList = new ArrayList<PrinterDevice>();
    private Context con;
    private LayoutInflater inflater;
    private Handler handler;
    private boolean isShowUnbund = true;
    private int type = 1;

    public DevicesAdapter(Context context, List<PrinterDevice> printerDBModelList, Handler handler) {
        setPrinterDBModelList(printerDBModelList);
        this.con = context;
        this.handler = handler;
        inflater = LayoutInflater.from(con);
    }

    private void setPrinterDBModelList(List<PrinterDevice> printerDBModelList) {
        this.printerDBModelList.clear();
        if (printerDBModelList != null) {

            this.printerDBModelList.addAll(printerDBModelList);
        }
        if (this.type == 1 && (this.printerDBModelList.size() == 0
                || this.printerDBModelList.get(0).getDevice_id() == -1)) {

            PrinterDevice printerDevice = new PrinterDevice();
            printerDevice.setDevice_id(-100);
            this.printerDBModelList.add(printerDevice);
        }


    }

    public void setList(List<PrinterDevice> printerDBModelList, int type) {
        this.type = type;
        setPrinterDBModelList(printerDBModelList);


        notifyDataSetChanged();
    }

//	public void setShowUnbund(boolean isShowUnbund) {
//		isShowUnbund = false;
//		this.isShowUnbund = isShowUnbund;
//	}

    @Override
    public int getCount() {
        return printerDBModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return printerDBModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

//	public void refreshData(List<PrinterDevice> newData){
//		printerDBModelList.clear();
//		for (int i = 0; i < newData.size(); i++) {
//			printerDBModelList.add(newData.get(i));
//		}
//		notifyDataSetChanged();
//	}


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.divices_list_item, null);
            holder = new ViewHolder();
            holder.devices_ip_tv = (TextView) convertView.findViewById(R.id.devices_item_ip_tv);
            holder.tv_device_ip = (TextView) convertView.findViewById(R.id.tv_device_ip);
            holder.devices_unbund_tv = (TextView) convertView.findViewById(R.id.devices_item_unbund_tv);
            holder.devices_item_add_tv = (TextView) convertView.findViewById(R.id.devices_item_add_tv);
            holder.devices_item_add_img = (ImageView) convertView.findViewById(R.id.devices_item_add_img);
            holder.ll_auto_add = (LinearLayout) convertView.findViewById(R.id.ll_auto_add);
            holder.ll_manually_add = (LinearLayout) convertView.findViewById(R.id.ll_manually_add);
            holder.devices_item_type = (TextView) convertView.findViewById(R.id.devices_item_type);
            holder.img_delete = (ImageView) convertView.findViewById(R.id.img_delete);
//            holder.card_view_add = (CardView) convertView.findViewById(R.id.card_view_add);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PrinterDevice printerDevice = printerDBModelList.get(position);
        Log.d("Adapter", " ---显示---" + printerDevice.getIP() + "---" + printerDevice.getType() + "" + TextUtils.isEmpty(printerDevice.getType()));
        holder.devices_unbund_tv.setTag(printerDevice);
        holder.img_delete.setTag(printerDevice);
        holder.ll_auto_add.setTag(printerDevice);

        if (!TextUtils.isEmpty(printerDevice.getIP())) {

            if (printerDevice.getIP().contains(",")) {
                holder.devices_item_type.setText("USB");
            } else {

                if (printerDevice.getIP().indexOf(":") != -1) {
                    holder.devices_item_type.setText(con.getResources().getString(R.string.devices_bluetooth));
                } else {
                    holder.devices_item_type.setText(con.getResources().getString(R.string.devices_network));
                }
            }

        }

//		if(!TextUtils.isEmpty(printerDevice.getType()))
//		{
//			if(printerDevice.getType().equals("1"))
//			{
//				holder.devices_item_type.setText("网络");
//			}else {
//				holder.devices_item_type.setText("蓝牙");
//			}
//
//		}else {
//			holder.devices_item_type.setText("网络");
//		}


        if (printerDevice.getDevice_id() == -100) {
            holder.ll_auto_add.setVisibility(View.GONE);
            holder.img_delete.setVisibility(View.GONE);
            holder.ll_manually_add.setVisibility(View.VISIBLE);
//            holder.card_view_add.setVisibility(View.VISIBLE);
            holder.ll_manually_add.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    handler.sendEmptyMessage(DevicesActivity.MANUALLY_ADD_PRINTER);
                }
            });
        } else {
            holder.ll_auto_add.setVisibility(View.VISIBLE);
            holder.img_delete.setVisibility(View.VISIBLE);
            holder.ll_manually_add.setVisibility(View.GONE);
//            holder.card_view_add.setVisibility(View.GONE);
            holder.ll_auto_add.setOnClickListener(listener);
            if (printerDevice.getDevice_id() != -1) {
                //  holder.devices_unbund_tv.setOnClickListener(ocl);
                holder.devices_unbund_tv.setText("Printer Added");
                holder.img_delete.setOnClickListener(ocl);
                holder.devices_unbund_tv.setBackgroundColor(con.getResources().getColor(R.color.green));
                // holder.devices_unbund_tv.setBackgroundColor(R.color.green);
                holder.img_delete.setVisibility(View.VISIBLE);
                if (printerDevice.getIP().contains(",")) {
                    holder.devices_ip_tv.setText("");
                } else {
                    holder.devices_ip_tv.setText(printerDevice.getName());
                }
            } else {

                holder.devices_unbund_tv.setOnClickListener(listener);
                holder.ll_auto_add.setOnClickListener(listener);
                holder.devices_unbund_tv.setText("Add Printer");
                holder.devices_unbund_tv.setBackgroundColor(con.getResources().getColor(R.color.red1));
                holder.img_delete.setVisibility(View.GONE);
                if (printerDevice.getIP().contains(",")) {
                    holder.devices_ip_tv.setText("");
                } else {
                    holder.devices_ip_tv.setText(printerDevice.getName());
                }

            }
            if (!TextUtils.isEmpty(printerDevice.getName())) {
                holder.devices_ip_tv.setVisibility(View.VISIBLE);
                if (printerDevice.getIP().contains(",")) {
                    holder.devices_ip_tv.setText("");
                } else {
                    holder.devices_ip_tv.setText(printerDevice.getName());
                }
            } else {
                holder.devices_ip_tv.setVisibility(View.GONE);
            }

            if (type == 1) {
                holder.devices_unbund_tv.setVisibility(View.VISIBLE);
            } else if (type == 2) {
                holder.devices_unbund_tv.setVisibility(View.GONE);
            }

            if (printerDevice.getIP().contains(",")) {
                holder.tv_device_ip.setText("");
            } else {
                holder.tv_device_ip.setText(printerDevice.getIP());
            }

        }

        return convertView;
    }

    private OnClickListener ocl = new OnClickListener() {

        @Override
        public void onClick(final View view) {
            KpmDialogFactory.commonTwoBtnDialog(App.getTopActivity(), con.getResources().getString(R.string.warning), "Unassign will disconnect Printer!", con.getResources().getString(R.string.cancel), con.getResources().getString(R.string.ok), null, new OnClickListener() {

                @Override
                public void onClick(View v) {
                    PrinterDevice device = (PrinterDevice) view.getTag();
                    handler.sendMessage(handler.obtainMessage(DevicesActivity.UNASSIGN_PRINTER_DEVICE, device));
                }
            });

        }
    };

    private OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (ButtonClickTimer.canClick(view)) {
                PrinterDevice device = (PrinterDevice) view.getTag();
                if (device.getDevice_id() == -1) {
                    handler.sendMessage(handler.obtainMessage(DevicesActivity.ASSIGN_PRINTER_DEVICE, device));
                }
            }
        }
    };

    public class ViewHolder {
        public TextView devices_ip_tv;
        public TextView devices_unbund_tv;
        public TextView tv_device_ip;

        public TextView devices_item_type;
        TextView devices_item_add_tv;
        ImageView devices_item_add_img, img_delete;
        LinearLayout ll_auto_add;
        LinearLayout ll_manually_add, ll_add;

//        CardView card_view_add;
    }
}
