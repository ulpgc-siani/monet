package org.monet.space.mobile.helpers;

import android.annotation.SuppressLint;
import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class DateUtils {

    public static Date parseDateTime(String timeToParse) {
        if (timeToParse == null || timeToParse.length() == 0) return null;

        SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            return iso8601Format.parse(timeToParse);
        } catch (ParseException ignored) {
        }
        return null;
    }

    public static String toString(Date date) {
        SimpleDateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return iso8601Format.format(date);
    }

    public static String formatAsDate(Context context, String timeToFormat) {
        return formatAsDate(context, parseDateTime(timeToFormat));
    }

    public static String formatAsDate(Context context, Date timeToFormat) {
        String finalDateTime = "";

        if (timeToFormat != null) {
            long when = timeToFormat.getTime();
            int flags = 0;
            flags |= android.text.format.DateUtils.FORMAT_SHOW_DATE;
            flags |= android.text.format.DateUtils.FORMAT_SHOW_YEAR;

            finalDateTime = android.text.format.DateUtils.formatDateTime(context, when, flags);
        }
        return finalDateTime;
    }

    public static String formatAsDateTime(Context context, Date timeToFormat) {
        String finalDateTime = "";

        if (timeToFormat != null) {
            long when = timeToFormat.getTime();
            int flags = 0;
            flags |= android.text.format.DateUtils.FORMAT_SHOW_TIME;
            flags |= android.text.format.DateUtils.FORMAT_SHOW_DATE;
            flags |= android.text.format.DateUtils.FORMAT_SHOW_YEAR;

            finalDateTime = android.text.format.DateUtils.formatDateTime(context, when, flags);
        }
        return finalDateTime;
    }

    public static String formatAsShortDate(Context context, String timeToFormat) {
        return formatAsShortDate(context, parseDateTime(timeToFormat));
    }

    public static String formatAsShortDate(Context context, Date timeToFormat) {
        String finalDateTime = "";

        if (timeToFormat != null) {
            long when = timeToFormat.getTime();
            int flags = 0;
            flags |= android.text.format.DateUtils.FORMAT_SHOW_DATE;
            flags |= android.text.format.DateUtils.FORMAT_ABBREV_MONTH;

            finalDateTime = android.text.format.DateUtils.formatDateTime(context, when, flags);
        }
        return finalDateTime;
    }

}
