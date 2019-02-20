package org.monet.bpi.types;

import org.joda.time.Interval;
import org.joda.time.PeriodType;
import org.monet.space.kernel.model.BusinessUnit;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class Date {

	public static final String INTERNAL = "internal";
	private static final String DEFAULT_DATE_FORMAT = "yyyy/MM/dd HH:mm:ss,SS";

	@Attribute(name = INTERNAL)
	private java.util.Date value;
	@Text
	private String formattedValue;
	private org.joda.time.DateTime jodaValue;

	public Date(Date date) {
		this.value = new java.util.Date(date.value.getTime());
		this.jodaValue = new org.joda.time.DateTime(date.value.getTime());
		this.formattedValue = date.formattedValue;
	}

	public Date() {
		this.value = new java.util.Date();
		this.jodaValue = new org.joda.time.DateTime(this.value);
		this.formattedValue = buildFormatter(DEFAULT_DATE_FORMAT).format(this.value);
	}

	public Date(java.util.Date date) {
		this.value = date;
		this.jodaValue = new org.joda.time.DateTime(this.value);
		if (date != null)
			this.formattedValue = buildFormatter(DEFAULT_DATE_FORMAT).format(this.value);
	}

	public Date(long time) {
		this(new java.util.Date(time));
	}

	public Date(String format) {
		this.value = new java.util.Date();
		this.jodaValue = new org.joda.time.DateTime(this.value);

		this.formattedValue = buildFormatter(format).format(this.value);
	}

	public Date(java.util.Date date, String format) {
		this.value = date;
		this.jodaValue = new org.joda.time.DateTime(this.value);
		if (date != null) {
			this.formattedValue = buildFormatter(format).format(this.value);
		}
	}

	public Date(String date, String format) throws ParseException {
		this.formattedValue = date;
		this.value = buildFormatter(format).parse(date);
		this.jodaValue = new org.joda.time.DateTime(this.value);
	}

	public void setValue(java.util.Date value) {
		this.value = value;
		this.formattedValue = buildFormatter(DEFAULT_DATE_FORMAT).format(this.value);
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
		SimpleDateFormat formatter = buildFormatter(format);
		return formatter.format(this.value);
	}

	public int millisecondsUntil(Date dateTime) {
		org.joda.time.DateTime dt_2 = new org.joda.time.DateTime(dateTime.value);
		Interval interval = new Interval(this.jodaValue, dt_2);
		return interval.toPeriod(PeriodType.millis()).getMillis();
	}

	public int secondsUntil(Date dateTime) {
		org.joda.time.DateTime dt_2 = new org.joda.time.DateTime(dateTime.value);
		Interval interval = new Interval(this.jodaValue, dt_2);
		return interval.toPeriod(PeriodType.seconds()).getSeconds();
	}

	public int minutesUntil(Date dateTime) {
		org.joda.time.DateTime dt_2 = new org.joda.time.DateTime(dateTime.value);
		Interval interval = new Interval(this.jodaValue, dt_2);
		return interval.toPeriod(PeriodType.minutes()).getMinutes();
	}

	public int hoursUntil(Date dateTime) {
		org.joda.time.DateTime dt_2 = new org.joda.time.DateTime(dateTime.value);
		Interval interval = new Interval(this.jodaValue, dt_2);
		return interval.toPeriod(PeriodType.hours()).getHours();
	}

	public int daysUntil(Date dateTime) {
		org.joda.time.DateTime dt_2 = new org.joda.time.DateTime(dateTime.value);
		Interval interval = new Interval(this.jodaValue, dt_2);
		return interval.toPeriod(PeriodType.days()).getDays();
	}

	public int weeksUntil(Date dateTime) {
		org.joda.time.DateTime dt_2 = new org.joda.time.DateTime(dateTime.value);
		Interval interval = new Interval(this.jodaValue, dt_2);
		return interval.toPeriod(PeriodType.weeks()).getWeeks();
	}

	public int monthsUntil(Date dateTime) {
		org.joda.time.DateTime dt_2 = new org.joda.time.DateTime(dateTime.value);
		Interval interval = new Interval(this.jodaValue, dt_2);
		return interval.toPeriod(PeriodType.months()).getMonths();
	}

	public int yearsUntil(Date dateTime) {
		org.joda.time.DateTime dt_2 = new org.joda.time.DateTime(dateTime.value);
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

	public Date plusHours(int hours) {
		this.jodaValue = this.jodaValue.plusHours(hours);
		this.setValue(this.jodaValue.toDate());
		return this;
	}

	public Date plusMinutes(int minutes) {
		this.jodaValue = this.jodaValue.plusMinutes(minutes);
		this.setValue(this.jodaValue.toDate());
		return this;
	}

	public Date plusSeconds(int seconds) {
		this.jodaValue = this.jodaValue.plusSeconds(seconds);
		this.setValue(this.jodaValue.toDate());
		return this;
	}

	public Date plusMillis(int milliseconds) {
		this.jodaValue = this.jodaValue.plusMillis(milliseconds);
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

	public Date minusHours(int hours) {
		this.jodaValue = this.jodaValue.minusHours(hours);
		this.setValue(this.jodaValue.toDate());
		return this;
	}

	public Date minusMinutes(int minutes) {
		this.jodaValue = this.jodaValue.minusMinutes(minutes);
		this.setValue(this.jodaValue.toDate());
		return this;
	}

	public Date minusSeconds(int seconds) {
		this.jodaValue = this.jodaValue.minusSeconds(seconds);
		this.setValue(this.jodaValue.toDate());
		return this;
	}

	public Date minusMillis(int milliseconds) {
		this.jodaValue = this.jodaValue.minusMillis(milliseconds);
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
		org.joda.time.DateTime dt = new org.joda.time.DateTime(from.value);
		org.joda.time.DateTime dt_2 = new org.joda.time.DateTime(to.value);
		Interval interval = new Interval(dt, dt_2);
		return interval.contains(new org.joda.time.DateTime(this.value));
	}

	public static Date parse(String pattern, String text) throws ParseException {
		if (text == null || text.trim().length() == 0)
			return null;
		java.util.Date date = buildFormatter(pattern).parse(text);
		Date datetime = new Date(date);
		return datetime;
	}

	public static SimpleDateFormat buildFormatter(String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);

		TimeZone timeZone = BusinessUnit.getTimeZone();
		if (timeZone != null)
			formatter.setTimeZone(timeZone);

		return formatter;
	}

}
