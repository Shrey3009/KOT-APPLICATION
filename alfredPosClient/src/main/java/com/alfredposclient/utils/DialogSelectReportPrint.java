package com.alfredposclient.utils;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.alfredbase.BaseActivity;
import com.alfredposclient.R;

public class DialogSelectReportPrint {

	/**
	 * 通用的，两按钮Dialog
	 * 
	 * @param mContext
	 * @param title
	 * @param content
	 * @param leftText
	 * @param rightText
	 * @param leftListener
	 * @param rghtListener
	 */
	public static void show(final BaseActivity activity, final OnClickListener btnListener) {
		final Dialog dialog = new Dialog(activity, R.style.base_dialog);
		View view = LayoutInflater.from(activity).inflate(
				R.layout.select_print_report_type, null);
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setContentView(view);
		view.findViewById(R.id.btn_report_sales).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
						if (btnListener != null)
							btnListener.onClick(v);
					}
				});
		view.findViewById(R.id.btn_report_detail_analysis).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
						if (btnListener != null)
							btnListener.onClick(v);
					}
				});
		view.findViewById(R.id.btn_report_summary_analysis).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
						if (btnListener != null)
							btnListener.onClick(v);
					}
				});
		view.findViewById(R.id.btn_report_all).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
						if (btnListener != null)
							btnListener.onClick(v);
					}
				});
		if (activity == null || activity.isFinishing())
			return;
		dialog.show();
	}
}
