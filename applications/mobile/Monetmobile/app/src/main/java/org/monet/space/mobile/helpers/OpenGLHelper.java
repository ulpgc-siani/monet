package org.monet.space.mobile.helpers;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;

public class OpenGLHelper {

  public static boolean supportOpenGLES20(Context context) {
    final ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
    final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
    return configurationInfo.reqGlEsVersion >= 0x20000;
  }
  
}
