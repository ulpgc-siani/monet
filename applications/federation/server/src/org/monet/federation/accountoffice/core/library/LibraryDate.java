package org.monet.federation.accountoffice.core.library;

import java.util.Calendar;
import java.util.Date;

public abstract class LibraryDate {

  public static int getCurrentYear() {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date());
    return calendar.get(Calendar.YEAR);
  }

}
