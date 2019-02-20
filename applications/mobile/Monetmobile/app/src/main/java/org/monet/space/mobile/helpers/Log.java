package org.monet.space.mobile.helpers;

public class Log {

    public static final String TAG = "MonetMobile";

    public static void debug(String msg) {
        android.util.Log.d(TAG, msg);
    }

    public static void debug(String msg, Object... vars) {
        debug(String.format(msg, vars));
    }

    public static void info(String msg) {
        android.util.Log.i(TAG, msg);
    }

    public static void info(String msg, Object... vars) {
        info(String.format(msg, vars));
    }

    public static void error(String msg) {
        android.util.Log.e(TAG, msg);
    }

    public static void error(String msg, Object... vars) {
        error(String.format(msg, vars));
    }

    public static void error(String msg, Exception e) {
        android.util.Log.e(TAG, msg, e);
    }

    public static void error(Exception e) {
        error(e.getMessage(), e);
    }
}
