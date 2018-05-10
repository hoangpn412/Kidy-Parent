package vn.com.kidy.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by admin on 1/22/18.
 */

public class Tools {
    public static String longtoDate(long time) {
        String dateStr;
        Date date = new Date(time);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int dayofWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String dayStr;
        if (dayofWeek == 1) {
            dayStr = "Chủ nhật";
        } else {
            dayStr = "Thứ " + dayofWeek;
        }
        dateStr = dayStr + " Ngày " + day + "-" + month + "-" + year;
        return dateStr;
    }

    public static String calendarDaytoDateWithDayofWeekString(CalendarDay cDay) {
        Calendar calendar = cDay.getCalendar();
        return calendartoDateWithDayofWeekString(calendar);
    }

    public static String longtoDateWithDayofWeekString(long curDate) {
        Date dat = new Date(curDate);
        Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(dat);

        return calendartoDateWithDayofWeekString(calendar);
    }

    private static String calendartoDateWithDayofWeekString(Calendar calendar) {
        String dayofweek;
        String dayofmonth;
        String month;
        String year;
        String date;
        if (calendar.get(Calendar.DAY_OF_WEEK) == 1) {
            dayofweek = "Chủ nhật";
        } else {
            dayofweek = "Thứ " + calendar.get(Calendar.DAY_OF_WEEK);
        }
        if (calendar.get(Calendar.DAY_OF_MONTH) < 10) {
            dayofmonth = "0" + calendar.get(Calendar.DAY_OF_MONTH);
        } else {
            dayofmonth = "" + calendar.get(Calendar.DAY_OF_MONTH);
        }
        if ((calendar.get(Calendar.MONTH) + 1) < 10) {
            month = "0" + (calendar.get(Calendar.MONTH) + 1);
        } else {
            month = "" + (calendar.get(Calendar.MONTH) + 1);
        }
        year = "" + calendar.get(Calendar.YEAR);
        date = "<b>" + dayofweek + "</b>" + " Ngày " + "<b>" + dayofmonth + "</b>"  + "-" + "<b>" + month + "</b>" + "-" + "<b>" + year + "</b>";
        return date;
    }

    private static String calendartoDate(Calendar calendar) {
        String dayofweek;
        String dayofmonth;
        String month;
        String year;
        String date;
        if (calendar.get(Calendar.DAY_OF_WEEK) == 1) {
            dayofweek = "Chủ nhật";
        } else {
            dayofweek = "Thứ " + calendar.get(Calendar.DAY_OF_WEEK);
        }
        if (calendar.get(Calendar.DAY_OF_MONTH) < 10) {
            dayofmonth = "0" + calendar.get(Calendar.DAY_OF_MONTH);
        } else {
            dayofmonth = "" + calendar.get(Calendar.DAY_OF_MONTH);
        }
        if ((calendar.get(Calendar.MONTH) + 1) < 10) {
            month = "0" + (calendar.get(Calendar.MONTH) + 1);
        } else {
            month = "" + (calendar.get(Calendar.MONTH) + 1);
        }
        year = "" + calendar.get(Calendar.YEAR);
        date = "<b>" + dayofmonth + "</b>"  + "-" + "<b>" + month + "</b>" + "-" + "<b>" + year + "</b>";
        return date;
    }

    public static String longtoDateWithString(long curDate) {
        Date dat = new Date(curDate);
        Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(dat);

        String dayofmonth;
        String month;
        String year;
        String date;
        if (calendar.get(Calendar.DAY_OF_MONTH) < 10) {
            dayofmonth = "0" + calendar.get(Calendar.DAY_OF_MONTH);
        } else {
            dayofmonth = "" + calendar.get(Calendar.DAY_OF_MONTH);
        }
        if ((calendar.get(Calendar.MONTH) + 1) < 10) {
            month = "0" + (calendar.get(Calendar.MONTH) + 1);
        } else {
            month = "" + (calendar.get(Calendar.MONTH) + 1);
        }
        year = "" + calendar.get(Calendar.YEAR);
        date = dayofmonth + "/" + month + "/" + year;
        return date;
    }

    public static String dateToStringDate(String strDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = null;
        int year, month, day;
        try {
            date = sdf.parse(strDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return calendartoDateWithDayofWeekString(cal);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String dateToStringDateWithoutDayofWeek(String strDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = null;
        int year, month, day;
        try {
            date = sdf.parse(strDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return calendartoDate(cal);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Calendar stringDatetoCalendar(String strDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date;
        try {
            date = sdf.parse(strDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int dptoInt(Context context, int dp) {
        Resources r = context.getResources();
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );
        return px;
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
