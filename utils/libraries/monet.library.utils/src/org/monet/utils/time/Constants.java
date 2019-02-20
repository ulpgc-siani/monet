package org.monet.utils.time;

public class Constants {
  public static final int ONE_WEEK_IN_DAYS                        = 7;
  public static final int ONE_YEAR_IN_DAYS                        = 365;
  
  public static final int ONE_DAY_HOURS                           = 24;
  public static final int ONE_WEEK_IN_HOURS                       = ONE_WEEK_IN_DAYS * ONE_DAY_HOURS;
  public static final int ONE_YEAR_IN_HOURS                       = ONE_YEAR_IN_DAYS * ONE_DAY_HOURS;
  
  public static final int ONE_HOUR_IN_MINUTES                     = 60;
  public static final int ONE_DAY_IN_MINUTES                      = ONE_DAY_HOURS * ONE_HOUR_IN_MINUTES;
  public static final int ONE_WEEK_IN_MINUTES                     = ONE_WEEK_IN_HOURS * ONE_HOUR_IN_MINUTES;
  public static final int ONE_YEAR_IN_MINUTES                     = ONE_YEAR_IN_HOURS * ONE_HOUR_IN_MINUTES;
  
  public static final int ONE_MINUTE_IN_SECONDS                   = 60; 
  public static final int ONE_HOUR_IN_SECONDS                     = ONE_HOUR_IN_MINUTES * ONE_MINUTE_IN_SECONDS;
  public static final int ONE_DAY_IN_SECONDS                      = ONE_DAY_HOURS * ONE_HOUR_IN_SECONDS;
  public static final int ONE_WEEK_IN_SECONDS                     = ONE_WEEK_IN_MINUTES * ONE_MINUTE_IN_SECONDS;
  public static final int ONE_YEAR_IN_SECONDS                     = ONE_YEAR_IN_MINUTES * ONE_MINUTE_IN_SECONDS;
  
  public static final long ONE_YEAR_IN_MILISECONS                 = 31536000000L;
  public static final long THREE_YEAR_IN_MILISECONS               = ONE_YEAR_IN_MILISECONS * 3;
  public static final long FIVE_YEAR_IN_MILISECONS                = ONE_YEAR_IN_MILISECONS * 5;
  public static final long TEN_YEAR_IN_MILISECONS                 = ONE_YEAR_IN_MILISECONS * 10;
}
