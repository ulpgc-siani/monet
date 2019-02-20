package org.monet.bpi.types;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.Interval;
import org.joda.time.PeriodType;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;

public class DateTime {

  public static final String           INTERNAL  = "internal";
  public static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss,SS");

  @Attribute(name = INTERNAL)
  private Date                         value;
  @Text
  private String                       formattedValue;
  private org.joda.time.DateTime       jodaValue;

  public DateTime() {
    this.value = new Date();
    this.jodaValue = new org.joda.time.DateTime(this.value);
    this.formattedValue = formatter.format(this.value);
  }

  public DateTime(Date date) {
    this.value = date;
    this.jodaValue = new org.joda.time.DateTime(this.value);
    if (date != null)
      this.formattedValue = formatter.format(this.value);
  }

  public DateTime(String format) {
    this.value = new Date();
    this.jodaValue = new org.joda.time.DateTime(this.value);

    SimpleDateFormat formatter = new SimpleDateFormat(format);
    this.formattedValue = formatter.format(this.value);
  }

  public DateTime(Date date, String format) {
    this.value = date;
    this.jodaValue = new org.joda.time.DateTime(this.value);
    if (date != null) {
      SimpleDateFormat formatter = new SimpleDateFormat(format);
      this.formattedValue = formatter.format(this.value);
    }
  }

  public DateTime(String date, String format) throws ParseException {
    SimpleDateFormat formatter = new SimpleDateFormat(format);
    this.formattedValue = date;
    this.value = formatter.parse(date);
    this.jodaValue = new org.joda.time.DateTime(this.value);
  }

  public void setValue(Date value) {
    this.value = value;
  }

  public Date getValue() {
    return value;
  }

  public void setFormattedValue(String formattedValue) {
    this.formattedValue = formattedValue;
  }

  public String getFormattedValue() {
    return formattedValue;
  }

  public boolean equals(DateTime obj) {
    return this.value.equals(obj.getValue()) && this.formattedValue.equals(obj.getFormattedValue());
  }

  @Override
  public String toString() {
    return this.formattedValue;
  }

  public String toString(String format) {
    SimpleDateFormat formatter = new SimpleDateFormat(format);
    return formatter.format(this.value);
  }

  public int millisecondsUntil(DateTime dateTime) {
    org.joda.time.DateTime dt_2 = new org.joda.time.DateTime(dateTime.value);
    Interval interval = new Interval(this.jodaValue, dt_2);
    return interval.toPeriod(PeriodType.millis()).getMillis();
  }

  public int secondsUntil(DateTime dateTime) {
    org.joda.time.DateTime dt_2 = new org.joda.time.DateTime(dateTime.value);
    Interval interval = new Interval(this.jodaValue, dt_2);
    return interval.toPeriod(PeriodType.seconds()).getSeconds();
  }

  public int minutesUntil(DateTime dateTime) {
    org.joda.time.DateTime dt_2 = new org.joda.time.DateTime(dateTime.value);
    Interval interval = new Interval(this.jodaValue, dt_2);
    return interval.toPeriod(PeriodType.minutes()).getMinutes();
  }

  public int hoursUntil(DateTime dateTime) {
    org.joda.time.DateTime dt_2 = new org.joda.time.DateTime(dateTime.value);
    Interval interval = new Interval(this.jodaValue, dt_2);
    return interval.toPeriod(PeriodType.hours()).getHours();
  }

  public int daysUntil(DateTime dateTime) {
    org.joda.time.DateTime dt_2 = new org.joda.time.DateTime(dateTime.value);
    Interval interval = new Interval(this.jodaValue, dt_2);
    return interval.toPeriod(PeriodType.days()).getDays();
  }

  public int weeksUntil(DateTime dateTime) {
    org.joda.time.DateTime dt_2 = new org.joda.time.DateTime(dateTime.value);
    Interval interval = new Interval(this.jodaValue, dt_2);
    return interval.toPeriod(PeriodType.weeks()).getWeeks();
  }

  public int monthsUntil(DateTime dateTime) {
    org.joda.time.DateTime dt_2 = new org.joda.time.DateTime(dateTime.value);
    Interval interval = new Interval(this.jodaValue, dt_2);
    return interval.toPeriod(PeriodType.months()).getMonths();
  }

  public int yearsUntil(DateTime dateTime) {
    org.joda.time.DateTime dt_2 = new org.joda.time.DateTime(dateTime.value);
    Interval interval = new Interval(this.jodaValue, dt_2);
    return interval.toPeriod(PeriodType.years()).getYears();
  }

  public DateTime plus(long l) {
    this.jodaValue = this.jodaValue.plus(l);
    this.setValue(this.jodaValue.toDate());
    return this;
  }

  public DateTime plusYears(int years) {
    this.jodaValue = this.jodaValue.plusYears(years);
    this.setValue(this.jodaValue.toDate());
    return this;
  }

  public DateTime plusMonths(int months) {
    this.jodaValue = this.jodaValue.plusMonths(months);
    this.setValue(this.jodaValue.toDate());
    return this;
  }

  public DateTime plusWeeks(int weeks) {
    this.jodaValue = this.jodaValue.plusWeeks(weeks);
    this.setValue(this.jodaValue.toDate());
    return this;
  }

  public DateTime plusDays(int days) {
    this.jodaValue = this.jodaValue.plusDays(days);
    this.setValue(this.jodaValue.toDate());
    return this;
  }

  public DateTime plusHours(int hours) {
    this.jodaValue = this.jodaValue.plusHours(hours);
    this.setValue(this.jodaValue.toDate());
    return this;
  }

  public DateTime plusMinutes(int minutes) {
    this.jodaValue = this.jodaValue.plusMinutes(minutes);
    this.setValue(this.jodaValue.toDate());
    return this;
  }

  public DateTime plusSeconds(int seconds) {
    this.jodaValue = this.jodaValue.plusSeconds(seconds);
    this.setValue(this.jodaValue.toDate());
    return this;
  }

  public DateTime plusMillis(int milliseconds) {
    this.jodaValue = this.jodaValue.plusMillis(milliseconds);
    this.setValue(this.jodaValue.toDate());
    return this;
  }

  public DateTime minus(long l) {
    this.jodaValue = this.jodaValue.minus(l);
    this.setValue(this.jodaValue.toDate());
    return this;
  }

  public DateTime minusYears(int years) {
    this.jodaValue = this.jodaValue.minusYears(years);
    this.setValue(this.jodaValue.toDate());
    return this;
  }

  public DateTime minusMonths(int months) {
    this.jodaValue = this.jodaValue.minusMonths(months);
    this.setValue(this.jodaValue.toDate());
    return this;
  }

  public DateTime minusWeeks(int weeks) {
    this.jodaValue = this.jodaValue.minusWeeks(weeks);
    this.setValue(this.jodaValue.toDate());
    return this;
  }

  public DateTime minusDays(int days) {
    this.jodaValue = this.jodaValue.minusDays(days);
    this.setValue(this.jodaValue.toDate());
    return this;
  }

  public DateTime minusHours(int hours) {
    this.jodaValue = this.jodaValue.minusHours(hours);
    this.setValue(this.jodaValue.toDate());
    return this;
  }

  public DateTime minusMinutes(int minutes) {
    this.jodaValue = this.jodaValue.minusMinutes(minutes);
    this.setValue(this.jodaValue.toDate());
    return this;
  }

  public DateTime minusSeconds(int seconds) {
    this.jodaValue = this.jodaValue.minusSeconds(seconds);
    this.setValue(this.jodaValue.toDate());
    return this;
  }

  public DateTime minusMillis(int milliseconds) {
    this.jodaValue = this.jodaValue.minusMillis(milliseconds);
    this.setValue(this.jodaValue.toDate());
    return this;
  }

  public boolean before(DateTime when) {
    return this.value.before(when.value);
  }

  public boolean after(DateTime when) {
    return this.value.after(when.value);
  }

  public boolean isInInterval(DateTime from, DateTime to) {
    org.joda.time.DateTime dt = new org.joda.time.DateTime(from.value);
    org.joda.time.DateTime dt_2 = new org.joda.time.DateTime(to.value);
    Interval interval = new Interval(dt, dt_2);
    return interval.contains(new org.joda.time.DateTime(this.value));
  }

}
