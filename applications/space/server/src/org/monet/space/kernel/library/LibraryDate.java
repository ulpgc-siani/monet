/*
    Monet will assist business to process re-engineering. Monet separate the
    business logic from the underlying technology to allow Model-Driven
    Engineering (MDE). These models guide all the development process over a
    Service Oriented Architecture (SOA).

    Copyright (C) 2009  Grupo de Ingenieria del Sofware y Sistemas de la Universidad de Las Palmas de Gran Canaria

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see http://www.gnu.org/licenses/.
*/

package org.monet.space.kernel.library;

import org.monet.space.kernel.constants.Strings;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class LibraryDate {

	private static final long ONE_HOUR = 60 * 60 * 1000L;

	public class Format {
		public static final String TEXT = "text";
		public static final String NUMERIC = "numeric";
		public static final String INTERNAL = "internal";
		public static final String DEFAULT = LibraryDate.Format.INTERNAL;
	}

	public class Language {
		public static final String ENGLISH = "en";
		public static final String SPANISH = "es";
		public static final String DEUTSCH = "de";
		public static final String FRENCH = "fr";
		public static final String PORTUGUESE = "pt";
	}

	private static final String[] aLabelsAt = {"a las", "at"};
	private static final String[] aLabelsOf = {"de", "of"};

	private static Date parseInternalDate(String sDate) {
		DateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy/HH:mm:ss");
		try {
			return dfDate.parse(sDate);
		} catch (ParseException oExceptionDateTime) {
			dfDate = new SimpleDateFormat("dd/MM/yyyy");
			try {
				return dfDate.parse(sDate);
			} catch (ParseException oExceptionDate) {
				dfDate = new SimpleDateFormat("MM/yyyy");
				try {
					return dfDate.parse(sDate);
				} catch (ParseException oExceptionMonth) {
					if (sDate.length() > 4) return null;
					dfDate = new SimpleDateFormat("yyyy");
					try {
						return dfDate.parse(sDate);
					} catch (ParseException oExceptionDay) {
						return null;
					}
				}
			}
		}
	}

	public static String getDateAndTimeString(Date dtDate, String codeLanguage, TimeZone timeZone, String format, Boolean bShowTime, String separator) {
		SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE, dd");
		SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM");
		SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		String sAtLabel, sOfLabel, sDate = Strings.EMPTY;
		String time = Strings.EMPTY;
		String day, month, year = Strings.EMPTY;

		if (dtDate == null) return "";

		if (timeZone != null) {
			dayFormat.setTimeZone(timeZone);
			monthFormat.setTimeZone(timeZone);
			yearFormat.setTimeZone(timeZone);
			timeFormat.setTimeZone(timeZone);
		}

		sAtLabel = (Language.SPANISH.equals(codeLanguage)) ? aLabelsAt[0] : aLabelsAt[1];
		sOfLabel = (Language.SPANISH.equals(codeLanguage)) ? aLabelsOf[0] : aLabelsOf[1];

		if (!format.equals(LibraryDate.Format.TEXT)) {
			dayFormat.applyPattern("dd");
			monthFormat.applyPattern("MM");
			yearFormat.applyPattern("yyyy");
		}

		if (bShowTime) {
			if (LibraryDate.Format.TEXT.equals(format)) time = sAtLabel + Strings.SPACE + timeFormat.format(dtDate);
			else time = timeFormat.format(dtDate);
		}

		day = LibraryString.capitalize(dayFormat.format(dtDate));
		month = LibraryString.capitalize(monthFormat.format(dtDate));
		year = LibraryString.capitalize(yearFormat.format(dtDate));

		if (format.equals(LibraryDate.Format.INTERNAL))
			return day + Strings.BAR45 + month + Strings.BAR45 + year + Strings.BAR45 + time;

		if (Language.ENGLISH.equals(codeLanguage)) {
			if (LibraryDate.Format.NUMERIC.equals(format))
				sDate = month + separator + day + separator + year + Strings.SPACE + time;
			else sDate = day + Strings.SPACE + month + Strings.SPACE + year + Strings.SPACE + time;
		} else if (Language.DEUTSCH.equals(codeLanguage)) {
			if (LibraryDate.Format.NUMERIC.equals(format))
				sDate = day + separator + month + separator + year + Strings.SPACE + time;
			else sDate = day + Strings.SPACE + month + Strings.SPACE + year + Strings.SPACE + time;
		} else if (Language.SPANISH.equals(codeLanguage)) {
			if (LibraryDate.Format.NUMERIC.equals(format))
				sDate = day + separator + month + separator + year + Strings.SPACE + time;
			else
				sDate = day + Strings.SPACE + sOfLabel + Strings.SPACE + month + Strings.SPACE + sOfLabel + Strings.SPACE + year + Strings.SPACE + time;
		} else {
			if (LibraryDate.Format.NUMERIC.equals(format))
				sDate = month + separator + day + separator + year + Strings.SPACE + time;
			else sDate = day + Strings.SPACE + month + Strings.SPACE + year + Strings.SPACE + time;
		}

		return sDate;
	}

	public static String getCurrentYear(int digits) {
		SimpleDateFormat yearFormat;
		String yearPattern = "";

		if (digits < 0) return "";
		while (yearPattern.length() < digits) yearPattern += "y";

		yearFormat = new SimpleDateFormat(yearPattern);

		return yearFormat.format(new Date());
	}

	public static Date parseDate(String sDate) {
		return parseDate(sDate, null);
	}

	public static Date parseDate(String sDate, TimeZone timeZone) {
		DateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss");
		Date dtResult = LibraryDate.parseInternalDate(sDate);

		if (timeZone != null)
			dfDate.setTimeZone(timeZone);

		if (dtResult != null) return dtResult;

		try {
			return dfDate.parse(sDate);
		} catch (ParseException oExceptionDateTime) {
			dfDate = new SimpleDateFormat("yyyy-MM-dd");
			try {
				return dfDate.parse(sDate);
			} catch (ParseException oExceptionDate) {
				dfDate = new SimpleDateFormat("yyyy-MM");
				try {
					return dfDate.parse(sDate);
				} catch (ParseException oExceptionMonth) {
					dfDate = new SimpleDateFormat("yyyy");
					try {
						return dfDate.parse(sDate);
					} catch (ParseException oExceptionDay) {
						return null;
					}
				}
			}
		}
	}

	public static Date parsePartialDate(String sDate) {
		Pattern p = Pattern.compile("(\\d\\d)?[^\\d]*(\\d\\d\\d\\d)");
		Matcher m = p.matcher(sDate);

		if (m.find()) {
			String sMonth = m.group(1);
			String sYear = m.group(2);

			int day = 1;
			int month = sMonth != null ? Integer.parseInt(sMonth) - 1 : 0;
			int year = sYear != null ? Integer.parseInt(sYear) : 1970;

			Calendar cal = Calendar.getInstance();
			cal.set(year, month, day);
			return cal.getTime();
		} else {
			return null;
		}
	}

	public static int getCurrentYear() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		return calendar.get(Calendar.YEAR);
	}

	public static long yearsToMillis(int count) {
		return (long) count * 12 * monthsToMillis(1);
	}

	public static long monthsToMillis(int count) {
		return (long) count * 30 * daysToMillis(1);
	}

	public static long weeksToMillis(int count) {
		return (long) count * 7 * daysToMillis(1);
	}

	public static long daysToMillis(int count) {
		return (long) count * 24 * hoursToMillis(1);
	}

	public static long hoursToMillis(int count) {
		return (long) count * 60 * minutesToMillis(1);
	}

	public static long minutesToMillis(int count) {
		return (long) count * 60 * secondsToMillis(1);
	}

	public static long secondsToMillis(int count) {
		return (long) count * 1000;
	}

}
