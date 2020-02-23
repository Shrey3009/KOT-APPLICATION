package com.alfredmenu.utils;

import android.view.View;
import android.view.View.OnClickListener;

import com.alfredbase.BaseActivity;
import com.alfredmenu.activity.TablesPage;
import com.alfredmenu.global.App;
import com.alfredmenu.R;


public class WaiterUtils {
	public static void showTransferTableDialog(BaseActivity baseActivity) {
		baseActivity.showOneButtonCompelDialog(
				baseActivity.getResources().getString(R.string.note),
				baseActivity.getResources().getString(R.string.table) + App.instance.getFromTableName()
						+ baseActivity.getResources().getString(R.string.transfer_)
						+ App.instance.getToTableName() + "!",
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						int indexOfMainPage = App.instance
								.getIndexOfActivity(TablesPage.class);
						int indexOfCurrentActivity = App.instance
								.getIndexOfActivity(App.instance
										.getTopActivity().getClass());
						if (indexOfMainPage != -1
								&& indexOfMainPage < indexOfCurrentActivity) {
							App.instance
									.popAllActivityExceptOne(TablesPage.class);
						}
					}
				});
	}
}
