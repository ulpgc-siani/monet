package org.monet.bpi.types;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Date {

  private static final String           INTERNAL  = "internal";
  private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss,SS");

  private java.util.Date                value;
  private String                        formattedValue;

  public Date() {
    this.value = new java.util.Date();
    this.formattedValue = formatter.format(this.value);
  }

  public Date(java.util.Date date) {
    this.value = date;
    this.formattedValue = formatter.format(this.value);
  }

  public Date(String format) {
    SimpleDateFormat formatter = new SimpleDateFormat(format);
    this.value = new java.util.Date();
    this.formattedValue = formatter.format(this.value);
  }

  public Date(java.util.Date date, String format) {
    SimpleDateFormat formatter = new SimpleDateFormat(format);
    this.value = date;
    this.formattedValue = formatter.format(this.value);
  }

  public Date(String date, String format) throws ParseException {
    SimpleDateFormat formatter = new SimpleDateFormat(format);
    this.formattedValue = date;
    this.value = formatter.parse(date);
  }

  public void setValue(java.util.Date value) {
    this.value = value;
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

  @Override
  public String toString() {
    return this.formattedValue;
  }

  public String toString(String format) {
    SimpleDateFormat formatter = new SimpleDateFormat(format);
    return formatter.format(this.value);
  }

  public int millisecondsUntil(Date dateTime) {
    return -1;
  }

  public int secondsUntil(Date dateTime) {
    return -1;
  }

  public int minutesUntil(Date dateTime) {
    return -1;
  }

  public int hoursUntil(Date dateTime) {
    return -1;
  }

  public int daysUntil(Date dateTime) {
    return -1;
  }

  public int weeksUntil(Date dateTime) {
    return -1;
  }

  public int monthsUntil(Date dateTime) {
    return -1;
  }

  public int yearsUntil(Date dateTime) {
    return -1;
  }

  public Date plus(long l) {
    return this;
  }

  public Date plusYears(int years) {
    return this;
  }

  public Date plusMonths(int months) {
    return this;
  }

  public Date plusWeeks(int weeks) {
    return this;
  }

  public Date plusDays(int days) {
    return this;
  }

  public Date plusHours(int hours) {
    return this;
  }

  public Date plusMinutes(int minutes) {
    return this;
  }

  public Date plusSeconds(int seconds) {
    return this;
  }

  public Date plusMillis(int milliseconds) {
    return this;
  }

  public Date minus(long l) {
    return this;
  }

  public Date minusYears(int years) {
    return this;
  }

  public Date minusMonths(int months) {
    return this;
  }

  public Date minusWeeks(int weeks) {
    return this;
  }

  public Date minusDays(int days) {
    return this;
  }

  public Date minusHours(int hours) {
    return this;
  }

  public Date minusMinutes(int minutes) {
    return this;
  }

  public Date minusSeconds(int seconds) {
    return this;
  }

  public Date minusMillis(int milliseconds) {
    return this;
  }

  public boolean before(Date when) {
    return this.value.before(when.value);
  }

  public boolean after(Date when) {
    return this.value.after(when.value);
  }

  public boolean isInInterval(Date from, Date to) {
    return false;
  }

  public static Date parse(String pattern, String text) throws ParseException {
    if (text == null || text.trim().length() == 0)
      return null;
    SimpleDateFormat format = new SimpleDateFormat(pattern);

    java.util.Date date = format.parse(text);
    Date datetime = new Date(date);
    return datetime;
  }

}
