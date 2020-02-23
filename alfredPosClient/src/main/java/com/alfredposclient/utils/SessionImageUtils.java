package com.alfredposclient.utils;

import android.content.Context;

import com.alfredbase.ParamConst;
import com.alfredbase.utils.SystemUtil;
import com.alfredposclient.R;
import com.alfredposclient.global.App;

public class SessionImageUtils {
	public static enum SessionImageIconGroup {
		image0(ParamConst.SESSION_STATUS_ALL_DAY, R.drawable.lunch_icon),
		
		image1(ParamConst.SESSION_STATUS_BREAKFAST, R.drawable.breakfast_icon),
		
		image2(ParamConst.SESSION_STATUS_LUNCH, R.drawable.lunch_icon),
		
		image3(ParamConst.SESSION_STATUS_DINNER, R.drawable.dinner_icon),
		
		image4(ParamConst.SESSION_STATUS_SUPPER, R.drawable.supper_icon);

		

		public static int getImageIconRes(int type) {
			for (SessionImageIconGroup image : SessionImageIconGroup.values()) {
				if (image.type == type) {
					return image.imageId;
				}
			}
			return 0;
		}

		@Override
		public String toString() {
			return type + "";
		}

		public int type;
		public int imageId;

		private SessionImageIconGroup(int type, int imageId) {
			this.type = type;
			this.imageId = imageId;
		}
	}
	
	public static enum SessionImageBgGroup {

		imageBg0(ParamConst.SESSION_STATUS_ALL_DAY, R.drawable.lunch_bg, R.drawable.lunch_bg_zh),
		
		imageBg1(ParamConst.SESSION_STATUS_BREAKFAST, R.drawable.breakfast_bg,R.drawable.breakfast_bg_zh),
		
		imageBg2(ParamConst.SESSION_STATUS_LUNCH, R.drawable.lunch_bg,R.drawable.lunch_bg_zh),
		
		imageBg3(ParamConst.SESSION_STATUS_DINNER, R.drawable.dinner_bg,R.drawable.dinner_bg_zh),
		
		imageBg4(ParamConst.SESSION_STATUS_SUPPER, R.drawable.supper_bg,R.drawable.supper_bg_zh);

		public static int getImageBgRes(int type) {
			for (SessionImageBgGroup image : SessionImageBgGroup.values()) {
				if (image.type == type) {
					if (App.instance.countryCode == ParamConst.CHINA && SystemUtil.isZh(App.instance)) {
						return image.imageZhId;
					}
					return image.imageId;
				}
			}
			return 0;
		}

		@Override
		public String toString() {
			return type + "";
		}

		public int type;
		public int imageId;
		public int imageZhId;
		public Context context;

		private SessionImageBgGroup(int type, int imageId, int imageZhId) {
			this.type = type;
			this.imageId = imageId;
			this.imageZhId = imageZhId;
		}
	}
}
