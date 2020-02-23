package com.alfredbase.utils;

import android.text.TextUtils;
import android.util.Log;

import com.alfredbase.BaseApplication;
import com.alfredbase.ParamConst;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class TimeUtil {
    public static final SimpleDateFormat FORMATTER = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss", Locale.US);

    public static final SimpleDateFormat YMDFORMATTER = new SimpleDateFormat(
            "yyyy-MM-dd", Locale.US);

    public static final SimpleDateFormat MDYFORMATTER = new SimpleDateFormat(
            "MM-dd-yyyy", Locale.US);

    public static final SimpleDateFormat FORMATTER_CARD_EXPIRY_DATE = new SimpleDateFormat(
            "MM/yy", Locale.US);

    public static String getTime() {
        return FORMATTER.format(new Date());
    }

    public static String getTime(long time) {
        return FORMATTER.format(time);
    }

    public static String getYMD(long time) {
        return YMDFORMATTER.format(time);
    }

    public static String getMDY(long time) {
        return MDYFORMATTER.format(time);
    }

    public static final SimpleDateFormat PRINTER_FORMAT_DATE = new SimpleDateFormat(
            "dd/MM/yyyy", Locale.US);

    public static final SimpleDateFormat PRINTER_FORMAT_DATE_TIME = new SimpleDateFormat(
            "dd/MM/yyyy HH:mm", Locale.US);
    public static final SimpleDateFormat PRINTING_FORMAT_DATE = new SimpleDateFormat(
            "yyyy-MM-dd", Locale.US);

    public static final SimpleDateFormat PRINTER_FORMAT_TIME = new SimpleDateFormat(
            "HH:mm", Locale.US);
    public static final SimpleDateFormat FORMATTER_TIME = new SimpleDateFormat(
            "HH:mm:ss", Locale.US);

    public static final SimpleDateFormat CLOSE_BILL_DATA_TIME = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);

    public static final SimpleDateFormat PRINTER_DELIVERY_DATE_TIME = new SimpleDateFormat(
            "dd/MM/yyyy, HH:mm", Locale.US);

    public static String getPrintDate(long time) {
        Calendar calendar = Calendar.getInstance(Locale.US);
        calendar.setTimeInMillis(time);
        if (BaseApplication.countryCode == ParamConst.CHINA) {
            return YMDFORMATTER.format(calendar.getTime());
        }
        else {
            return PRINTER_FORMAT_DATE.format(calendar.getTime());
        }
    }

    public static String getPrintDateTime(long time) {
        Calendar calendar = Calendar.getInstance(Locale.US);
        calendar.setTimeInMillis(time);
        if (BaseApplication.countryCode == ParamConst.CHINA)
            return YMDFORMATTER.format(calendar.getTime());
        else
            return PRINTER_FORMAT_DATE_TIME.format(calendar.getTime());
    }

    public static String getPrintingDate(long time) {
        Calendar calendar = Calendar.getInstance(Locale.US);
        calendar.setTimeInMillis(time);
        if (BaseApplication.countryCode == ParamConst.CHINA)
            return YMDFORMATTER.format(calendar.getTime());
        else
            return PRINTING_FORMAT_DATE.format(calendar.getTime());
    }

    public static long getPrintingLongDate(String time) {
        Date dt;
        try {
            if (BaseApplication.countryCode == ParamConst.CHINA)
                dt = YMDFORMATTER.parse(time);
            else
                dt = PRINTING_FORMAT_DATE.parse(time);
            return dt.getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0L;
    }

    public static String getPrintTime(long time) {
        Calendar calendar = Calendar.getInstance(Locale.US);
        calendar.setTimeInMillis(time);
        return PRINTER_FORMAT_TIME.format(calendar.getTime());
    }

    public static String getCloseBillDataTime(long time) {
        Calendar calendar = Calendar.getInstance(Locale.US);
        calendar.setTimeInMillis(time);
        return CLOSE_BILL_DATA_TIME.format(calendar.getTime());
    }

    public static String getDeliveryDataTime(long time) {
        Calendar calendar = Calendar.getInstance(Locale.US);
        calendar.setTimeInMillis(time);
        return PRINTER_DELIVERY_DATE_TIME.format(calendar.getTime());
    }


    public static String getTimeFormat(long time) {
        Calendar calendar = Calendar.getInstance(Locale.US);
        calendar.setTimeInMillis(time);
        return FORMATTER.format(calendar.getTime());
    }

    public static String getTimeByFormat(long time, SimpleDateFormat format) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return format.format(calendar.getTime());
    }

    public static long getTimeFromString(String time, SimpleDateFormat format) {
        try {
            Date date = format.parse(time);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static long getMillisOfTime(int hour, int minutes) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2019);
        cal.set(Calendar.MONTH, 3);
        cal.set(Calendar.DATE, 3);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minutes);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public static String getSubTimeFormat(long time) {
        long time1 = System.currentTimeMillis() - time;
        long result = 0;
        Calendar calendar = Calendar.getInstance(Locale.US);
        calendar.setTimeInMillis(getTimeInMillsByZero(0) + time1);
        try {
            result = (calendar.getTimeInMillis());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FORMATTER_TIME.format(result);
    }

    public static int getHourDifference(long start, long end) {
        long diff = start - end;
        int SECOND = 1000;
        int MINUTE = 60 * SECOND;
        int HOUR = 60 * MINUTE;
        return (int) (diff / HOUR);
    }

    public static int dayForWeek() {
        Calendar c = Calendar.getInstance(Locale.US);
        int dayForWeek = 0;
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            dayForWeek = 0;
        } else {
            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return dayForWeek;
    }

    public static int getWeek(long time) {
        Calendar cd = Calendar.getInstance(Locale.US);
        cd.setTime(new Date(time));
        int week = cd.get(Calendar.DAY_OF_WEEK);
        int mWeek = 0;
        if (1 == week) {
            mWeek = 1;
        } else if (2 == week) {
            mWeek = 2;
        } else if (3 == week) {
            mWeek = 3;
        } else if (4 == week) {
            mWeek = 4;
        } else if (5 == week) {
            mWeek = 5;
        } else if (6 == week) {
            mWeek = 6;
        } else if (7 == week) {
            mWeek = 7;
        }
        return mWeek;
    }

    public static long getTimeInMillis() {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.set(1970, 0, 01);
        return cal.getTimeInMillis();
    }


    /**
     * 获取0点时间
     */
    public static long getTimeInMillsByZero(int day) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        if (day != 0) {
            cal.set(Calendar.DAY_OF_MONTH, -day);
        }
        return cal.getTimeInMillis();
    }

    /**
     * 将输入的银行卡的过期转换成时间
     *
     * @param CardExpiryDateStr
     * @return
     */
    public static long getCardExpiryDate(String cardExpiryDateStr) {
        if (TextUtils.isEmpty(cardExpiryDateStr)) {
            return 0L;
        }
        Date date = null;
        try {
            date = FORMATTER_CARD_EXPIRY_DATE.parse(cardExpiryDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date == null) {
            return 0L;
        }
        return date.getTime();
    }

    /**
     * 将时间转换成银行卡的过期时间格式
     *
     * @param cardExpiryDate
     * @return
     */
    public static String getCardExpiryDateStr(long cardExpiryDate) {
        Calendar calendar = Calendar.getInstance(Locale.US);
        calendar.setTimeInMillis(cardExpiryDate);
        return FORMATTER_CARD_EXPIRY_DATE.format(calendar.getTime());
    }

    public static Calendar getCalendarByZero(int day) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        if (day != 0) {
            cal.set(Calendar.DAY_OF_MONTH, -day);
        }
        return cal;
    }

    /**
     * 获取当前时间的下一个整点
     *
     * @return
     */
    public static long getCalendarNextPoint(long time) {
        Calendar calendar = Calendar.getInstance(Locale.US);
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + 1);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static long getNewBusinessDate() {
        Calendar calendar = Calendar.getInstance(Locale.US);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static long getBusinessDateByDay(long businessDate, int day) {
        Calendar calendar = Calendar.getInstance(Locale.US);
        calendar.setTimeInMillis(businessDate);
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + day);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static long getBeforeYesterday(long businessDate) {
        Calendar calendar = Calendar.getInstance(Locale.US);
        calendar.setTimeInMillis(businessDate);
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 2);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();

    }

    public static int getTimeHour(long time) {
        Calendar calendar = Calendar.getInstance(Locale.US);
        calendar.setTimeInMillis(time);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static long getTimeBeforeTwoHour() {
        long time = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance(Locale.US);
        calendar.setTimeInMillis(time);
        calendar.add(Calendar.HOUR_OF_DAY, -2);
        return calendar.getTimeInMillis();
    }

    public static int getTotalDaysInMonth(int year, int month) {
        int iYear = year;

        int iMonth = month - 1; //Jan:0, Feb:1
        int iDay = 1;

        // Create a calendar object and set year and month
        Calendar mycal = new GregorianCalendar(iYear, iMonth, iDay);
        // Get the number of days in that month
        int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH); // 28
        return daysInMonth;
    }
}
