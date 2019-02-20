package org.monet.bpi.types;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;

public class Time {
  
  public static final String INTERNAL = "internal";
  
  @Attribute(name=INTERNAL)
  private long value;
  @Text
  private String formattedValue;

  public Time() {
    this.value = 0;
  }
  
  public Time(Date date) {
    this.setValue(date);
    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss,SS");
    this.formattedValue = formatter.format(date);
  }
  
  public Time(Date date, String formattedDate) {
    this.setValue(date);
    this.formattedValue = formattedDate;
  }
  
  public void setValue(Date value) {
    Calendar cal = new GregorianCalendar();
    cal.setTime(value);

    // Get the components of the time
    int hour24 = cal.get(Calendar.HOUR_OF_DAY);
    int min = cal.get(Calendar.MINUTE);
    int sec = cal.get(Calendar.SECOND);
    int ms = cal.get(Calendar.MILLISECOND);
    this.value = (((hour24*3600) + (min*60) + (sec)) * 1000) + ms;
  }
  
  public Date getValue() {
    Calendar cal = new GregorianCalendar();
    cal.setTimeInMillis(this.value);
    return cal.getTime();
  }
  
  public void setFormattedValue(String formattedValue) {
    this.formattedValue = formattedValue;
  }
  
  public String getFormattedValue() {
    return formattedValue;
  }
  
  public boolean equals(Time obj) {
    return this.value == obj.value && this.formattedValue.equals(obj.getFormattedValue());
  }
  
  public int millisecondsUntil(Time time) {
    return (int)(this.value - time.value);
  }
  
  public int secondsUntil(Time time) {
    return (int)(this.value - time.value)/1000;
  }
  
  public int minutesUntil(Time time) {
    return (int)(this.value - time.value)/60000;
  }
  
  public int hoursUntil(Time time) {
    return (int)(this.value - time.value)/3600000;
  }
  
}
