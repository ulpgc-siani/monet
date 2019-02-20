package org.monet.bpi.types;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.PeriodType;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;

public class Date {
  
  public static final String INTERNAL = "internal";
  public static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
  
  @Attribute(name=INTERNAL)
  private java.util.Date value;
  @Text
  private String formattedValue;
  private DateTime jodaValue;

  public Date() {
    this.setValue(new java.util.Date());
    this.formattedValue = formatter.format(this.value);
  }
  
  public Date(java.util.Date date) {
    this.setValue(date);
    this.formattedValue = formatter.format(this.value);
  }
  
  public Date(java.util.Date date, String formattedDate) {
    this.setValue(date);
    this.formattedValue = formattedDate;
  }
  
  public Date(String date, String format) throws ParseException {
    SimpleDateFormat formatter = new SimpleDateFormat(format);
    this.formattedValue = date;
    this.setValue(formatter.parse(date));
  }
  
  public void setValue(java.util.Date value) {
    Calendar cal = new GregorianCalendar();
    cal.setTime(value);
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    this.value = cal.getTime();
    this.jodaValue = new DateTime(this.value);
  }
  
  public java.util.Date getValue() {
    return value;
  }
  
  public void setFormattedValue(String formattedValue) {
    this.formattedValue = formattedValue;
  }
  
  public String getFormattedValue() {
    return formattedValue;
  }
  
  public boolean equals(Date obj) {
    return this.value.equals(obj.getValue()) && this.formattedValue.equals(obj.getFormattedValue());
  }
  
  public int millisecondsUntil(Date date) {
    DateTime dt_2 = new DateTime(date.value);
    Interval interval = new Interval(this.jodaValue, dt_2);
    return interval.toPeriod(PeriodType.millis()).getMillis();
  }
  
  public int secondsUntil(Date date) {
    DateTime dt_2 = new DateTime(date.value);
    Interval interval = new Interval(this.jodaValue, dt_2);
    return interval.toPeriod(PeriodType.seconds()).getSeconds();
  }
  
  public int minutesUntil(Date date) {
    DateTime dt_2 = new DateTime(date.value);
    Interval interval = new Interval(this.jodaValue, dt_2);
    return interval.toPeriod(PeriodType.minutes()).getMinutes();
  }
  
  public int hoursUntil(Date date) {
    DateTime dt_2 = new DateTime(date.value);
    Interval interval = new Interval(this.jodaValue, dt_2);
    return interval.toPeriod(PeriodType.hours()).getHours();
  }
  
  public int daysUntil(Date date) {
    DateTime dt_2 = new DateTime(date.value);
    Interval interval = new Interval(this.jodaValue, dt_2);
    return interval.toPeriod(PeriodType.days()).getDays();
  }

  public int weeksUntil(Date date) {
    DateTime dt_2 = new DateTime(date.value);
    Interval interval = new Interval(this.jodaValue, dt_2);
    return interval.toPeriod(PeriodType.weeks()).getWeeks();
  }
  
  public int monthsUntil(Date date) {
    DateTime dt_2 = new DateTime(date.value);
    Interval interval = new Interval(this.jodaValue, dt_2);
    return interval.toPeriod(PeriodType.months()).getMonths();
  }
  
  public int yearsUntil(Date date) {
    DateTime dt_2 = new DateTime(date.value);
    Interval interval = new Interval(this.jodaValue, dt_2);
    return interval.toPeriod(PeriodType.years()).getYears();
  }
  
  public Date plus(long l) {
    this.jodaValue = this.jodaValue.plus(l);
    this.setValue(this.jodaValue.toDate());
    return this;
  }

  public Date plusYears(int years) {
    this.jodaValue = this.jodaValue.plusYears(years);
    this.setValue(this.jodaValue.toDate());
    return this;
  }

  public Date plusMonths(int months) {
    this.jodaValue = this.jodaValue.plusMonths(months);
    this.setValue(this.jodaValue.toDate());
    return this;
  }

  public Date plusWeeks(int weeks) {
    this.jodaValue = this.jodaValue.plusWeeks(weeks);
    this.setValue(this.jodaValue.toDate());
    return this;
  }

  public Date plusDays(int days) {
    this.jodaValue = this.jodaValue.plusDays(days);
    this.setValue(this.jodaValue.toDate());
    return this;
  }

  public Date minus(long l) {
    this.jodaValue = this.jodaValue.minus(l);
    this.setValue(this.jodaValue.toDate());
    return this;
  }

  public Date minusYears(int years) {
    this.jodaValue = this.jodaValue.minusYears(years);
    this.setValue(this.jodaValue.toDate());
    return this;
  }

  public Date minusMonths(int months) {
    this.jodaValue = this.jodaValue.minusMonths(months);
    this.setValue(this.jodaValue.toDate());
    return this;
  }

  public Date minusWeeks(int weeks) {
    this.jodaValue = this.jodaValue.minusWeeks(weeks);
    this.setValue(this.jodaValue.toDate());
    return this;
  }

  public Date minusDays(int days) {
    this.jodaValue = this.jodaValue.minusDays(days);
    this.setValue(this.jodaValue.toDate());
    return this;
  }

  public boolean before(Date when) {
    return this.value.before(when.value);
  }
  
  public boolean after(Date when) {
    return this.value.after(when.value);
  }
  
  public boolean isInInterval(Date from, Date to) {
    DateTime dt = new DateTime(from.value);
    DateTime dt_2 = new DateTime(to.value);
    Interval interval = new Interval(dt, dt_2);
    return interval.contains(new DateTime(this.value));
  }
  
}
