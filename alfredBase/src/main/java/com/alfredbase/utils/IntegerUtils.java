package com.alfredbase.utils;

import java.util.Locale;

public class IntegerUtils {
    public static boolean isEmptyOrZero(Integer num) {
        if (num == null) {
            return true;
        } else if (num.intValue() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static String ordinal(int i) {
        String[] sufixes = new String[]{"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th"};
        switch (i % 100) {
            case 11:
            case 12:
            case 13:
                return i + "th";
            default:
                return i + sufixes[i % 10];

        }
    }

    /**
     * 组合叫号
     *
     * @param index
     * @param num
     * @return
     */
    public static String format(int index, String num) {
        return index + String.format(Locale.US, "%03d", Integer.parseInt(num));
    }

    public static String formatLocale(int index, String num) {
        String numFormat = String.format("%03d", Integer.parseInt(num));
        int formatSize = (index + "").length() + 3;
        String format = "%0" + formatSize + "d";
        return String.format(format, Integer.parseInt(index + numFormat));
    }


    /**
     * 组合叫号
     *
     * @param index
     * @param num
     * @return
     */
    public static int fromat(int index, int num) {
        return Integer.parseInt(index + String.format(Locale.US,"%03d", num));
    }

    public static String format24(String barcode) {
        return String.format(Locale.US,"%1$-24s", barcode).replace(' ', '0');
    }

    public static String format20(String barcode) {
        return String.format(Locale.US,"%1$-20s", barcode).replace(' ', '0');
    }
}
