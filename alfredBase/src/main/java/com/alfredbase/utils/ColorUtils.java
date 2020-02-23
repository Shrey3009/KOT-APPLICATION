package com.alfredbase.utils;

import com.alfredbase.BaseApplication;
import com.alfredbase.R;

public class ColorUtils {
	
	public static enum ColorGroup {
		color1(1, R.color.split_group1),
		
		color2(2, R.color.split_group2),
		
		color3(3, R.color.split_group3),
		
		color4(4, R.color.split_group4),
		
		color5(5, R.color.split_group5),
		
		color6(6, R.color.split_group6),
		
		color7(7, R.color.split_group7),
		
		color8(8, R.color.split_group8),
		
		color9(9, R.color.split_group9),
		
		color10(10, R.color.split_group10),
		
		color11(11, R.color.split_group11),
		
		color12(12, R.color.split_group12),
		
		color13(13, R.color.split_group13),
		
		color14(14, R.color.split_group14),
		
		color15(15, R.color.split_group15),
		
		color16(16, R.color.split_group16),
		
		color17(17, R.color.split_group17),
		
		color18(18, R.color.split_group18),
		
		color19(19, R.color.split_group19),
		
		color20(20, R.color.split_group20),
		
		color21(21, R.color.split_group21);

		public static int getColor(int bigIndex) {
			int index = bigIndex % 21 == 0 ? 21 : bigIndex % 21;
			for (ColorGroup color : ColorGroup.values()) {
				if (color.index == index) {
					return color.colorId;
				}
			}
			return 0;
		}

		@Override
		public String toString() {
			return index + "-" + BaseApplication.getTopActivity().getResources().getColor(colorId);
		}

		public int index;
		public int colorId;

		private ColorGroup(int index, int colorId) {
			this.index = index;
			this.colorId = colorId;
		}
	}
}
