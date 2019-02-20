/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Modified by Rayco Ara�a 16/11/2010
 * Now the behaviour of this class is equal to Ext-JS, the format used by Monet
 *
 */

package org.monet.space.kernel.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DateFormat {

	private static final SimpleDateFormat MONTHSHORT_FORMAT = new SimpleDateFormat(
		"MMM");
	private static final SimpleDateFormat MONTH_FORMAT = new SimpleDateFormat(
		"MMMM");
	private static final SimpleDateFormat DAYOFWEEKSHORT_FORMAT = new SimpleDateFormat(
		"EEE");
	private static final SimpleDateFormat DAYOFWEEK_FORMAT = new SimpleDateFormat(
		"EEEE");
	private static final SimpleDateFormat AMPM_FORMAT = new SimpleDateFormat("a");

	private static final String RD = "rd";
	private static final String ND = "nd";
	private static final String ST = "st";
	private static final String TH = "th";
	public static final char DAY_OF_MONTH = 'j'; // 5
	public static final char DAY_OF_MONTH_PADDED = 'd'; // 05
	public static final char DAY_OF_MONTH_ORDINAL = 'S'; // th

	public static final char DAY_OF_WEEK_TEXT = 'l'; // Wednesday
	public static final char DAY_OF_WEEK_AB_TEXT = 'D'; // Wed
	public static final char DAY_OF_WEEK_NUMERIC = 'w'; // 1-7 OR 0-6 0->Sunday


	public static final char DAY_OF_YEAR = 'z'; // 0-365
	public static final char WEEK_OF_YEAR = 'W'; // 0-52

	public static final char MONTH_TEXT = 'F'; // January
	public static final char MONTH_AB_TEXT = 'M'; // Jan
	public static final char MONTH_PADDED = 'm'; // 01
	public static final char MONTH = 'n'; // 1

	public static final char MONTH_NUMBER_OF_DAYS = 't'; // 31

	public static final char LEAP_YEAR = 'L'; // 0 (1 if it is a leap year, else
	// 0)
	public static final char YEAR = 'Y'; // 2007
	public static final char YEAR_AB = 'y'; // 07

	public static final char AM_PM = 'a'; // pm
	public static final char AM_PM_CAPITAL = 'A'; // PM

	public static final char HOUR_12_FORMAT = 'g'; // 3
	public static final char HOUR_24_FORMAT = 'G'; // 15
	public static final char HOUR_12_FORMAT_PADDED = 'h'; // 03
	public static final char HOUR_24_FORMAT_PADDED = 'H'; // 15

	public static final char MINUTES = 'i'; // 05
	public static final char SECONDS = 's'; // 01

	public static final char TIME_ZONE_GMT_DIFF = 'O'; // -0600 Difference to
	// Greenwich time (GMT) in
	// hours
	public static final char TIME_ZONE = 'T'; // CST
	public static final char TIME_ZONE_OFFSET = 'Z'; // -21600 Timezone offset in
	// seconds (negative if west
	// of UTC, positive if east)

	public static final String format(String inFormat, Date inDate, TimeZone timeZone) {
		if (inDate == null)
			return "";
		Calendar c = new GregorianCalendar();
		c.setTimeZone(timeZone);
		c.setTime(inDate);
		return format(inFormat, c, timeZone);
	}

	public static final String format(String inFormat, Calendar inDate, TimeZone timeZone) {
		StringBuilder s = new StringBuilder(inFormat);
		int c;
		int count;

		int len = inFormat.length();

		for (int i = 0; i < len; i += count) {
			int temp;

			count = 1;
			c = s.charAt(i);

			String replacement;

			switch (c) {
				case AM_PM:
					replacement = getAMPMString(inDate, timeZone);
					break;
				case AM_PM_CAPITAL:
					replacement = getAMPMString(inDate, timeZone).toUpperCase();
					break;
				case DAY_OF_MONTH:
					temp = inDate.get(Calendar.DAY_OF_MONTH);
					replacement = String.valueOf(temp);
					break;
				case DAY_OF_MONTH_PADDED:
					temp = inDate.get(Calendar.DAY_OF_MONTH);
					replacement = zeroPad(temp, 2);
					break;
				case DAY_OF_MONTH_ORDINAL:
					temp = inDate.get(Calendar.DAY_OF_MONTH);
					replacement = getOrdinalFor(temp);
					break;
				case DAY_OF_WEEK_TEXT:
					temp = inDate.get(Calendar.DAY_OF_WEEK);
					replacement = getDayOfWeekString(inDate, timeZone);
					break;
				case DAY_OF_WEEK_AB_TEXT:
					temp = inDate.get(Calendar.DAY_OF_WEEK);
					replacement = getDayOfWeekShortString(inDate, timeZone);
					break;
				case DAY_OF_WEEK_NUMERIC:
					temp = inDate.get(Calendar.DAY_OF_WEEK) - 1;
					replacement = String.valueOf(temp);
					break;
				case DAY_OF_YEAR:
					temp = inDate.get(Calendar.DAY_OF_YEAR) - 1;
					replacement = String.valueOf(temp);
					break;
				case WEEK_OF_YEAR:
					temp = inDate.get(Calendar.WEEK_OF_YEAR);
					replacement = String.valueOf(temp);
					break;
				case MONTH_TEXT:
					replacement = getMonthString(inDate, 4, timeZone);
					break;
				case MONTH_AB_TEXT:
					replacement = getMonthString(inDate, 3, timeZone);
					break;
				case MONTH_PADDED:
					replacement = getMonthString(inDate, 2, timeZone);
					break;
				case MONTH:
					replacement = getMonthString(inDate, 1, timeZone);
					break;
				case MONTH_NUMBER_OF_DAYS:
					temp = inDate.getActualMaximum(Calendar.DAY_OF_MONTH);
					replacement = String.valueOf(temp);
					break;
				case LEAP_YEAR:
					temp = inDate.get(Calendar.YEAR);
					if (((GregorianCalendar) GregorianCalendar.getInstance())
						.isLeapYear(temp))
						replacement = "1";
					else
						replacement = "0";
					break;
				case YEAR:
					replacement = getYearString(inDate, 4);
					break;
				case YEAR_AB:
					replacement = getYearString(inDate, 2);
					break;
				case HOUR_12_FORMAT:
					temp = inDate.get(Calendar.HOUR);
					if (temp == 0)
						temp = 12;
					replacement = zeroPad(temp, 1);
					break;
				case HOUR_24_FORMAT:
					replacement = zeroPad(inDate.get(Calendar.HOUR_OF_DAY), 1);
					break;
				case HOUR_12_FORMAT_PADDED:
					temp = inDate.get(Calendar.HOUR);
					if (temp == 0)
						temp = 12;
					replacement = zeroPad(temp, 2);
					break;
				case HOUR_24_FORMAT_PADDED:
					replacement = zeroPad(inDate.get(Calendar.HOUR_OF_DAY), 2);
					break;
				case MINUTES:
					replacement = zeroPad(inDate.get(Calendar.MINUTE), 2);
					break;
				case SECONDS:
					replacement = zeroPad(inDate.get(Calendar.SECOND), 2);
					break;
				case TIME_ZONE_GMT_DIFF:
					replacement = getTimeZoneString(inDate, 1);
					break;
				case TIME_ZONE:
					replacement = getTimeZoneString(inDate, 2);
					break;
				case TIME_ZONE_OFFSET:
					replacement = getTimeZoneString(inDate, 0);
					break;
				default:
					replacement = null;
					break;
			}

			if (replacement != null) {
				s.replace(i, i + count, replacement);
				count = replacement.length(); // CARE: count is used in the for loop
				// above
				len = s.length();
			}
		}

		return s.toString();
	}

	private static final String getAMPMString(Calendar inDate, TimeZone timeZone) {
		AMPM_FORMAT.setTimeZone(timeZone);
		return AMPM_FORMAT.format(inDate.getTime());
	}

	private static final String getDayOfWeekString(Calendar inDate, TimeZone timeZone) {
		DAYOFWEEK_FORMAT.setTimeZone(timeZone);
		return DAYOFWEEK_FORMAT.format(inDate.getTime());
	}

	private static final String getDayOfWeekShortString(Calendar inDate, TimeZone timeZone) {
		DAYOFWEEKSHORT_FORMAT.setTimeZone(timeZone);
		return DAYOFWEEKSHORT_FORMAT.format(inDate.getTime());
	}

	private static String getOrdinalFor(int value) {
		int hundredRemainder = value % 100;
		int tenRemainder = value % 10;
		if (hundredRemainder - tenRemainder == 10) {
			return TH;
		}

		switch (tenRemainder) {
			case 1:
				return ST;
			case 2:
				return ND;
			case 3:
				return RD;
			default:
				return TH;
		}
	}

	private static final String getMonthString(Calendar inDate, int count, TimeZone timeZone) {
		int month = inDate.get(Calendar.MONTH);

		MONTH_FORMAT.setTimeZone(timeZone);
		MONTHSHORT_FORMAT.setTimeZone(timeZone);

		if (count >= 4)
			return MONTH_FORMAT.format(inDate.getTime());
		else if (count == 3)
			return MONTHSHORT_FORMAT.format(inDate.getTime());
		else {
			// Calendar.JANUARY == 0, so add 1 to month.
			return zeroPad(month + 1, count);
		}
	}

	private static final String getTimeZoneString(Calendar inDate, int count) {
		TimeZone tz = inDate.getTimeZone();

		if (count < 1) {
			int offset = inDate.get(Calendar.DST_OFFSET)
				+ inDate.get(Calendar.ZONE_OFFSET);
			offset /= 1000;
			return String.valueOf(offset);
		} else {
			String timeZoneOffset = formatZoneOffset(inDate.get(Calendar.DST_OFFSET)
				+ inDate.get(Calendar.ZONE_OFFSET), count);
			if (count < 2)
				return timeZoneOffset;
			else {
				boolean dst = inDate.get(Calendar.DST_OFFSET) != 0;
				return tz.getDisplayName(dst, TimeZone.SHORT).replace(timeZoneOffset,
					"");
			}
		}
	}

	private static final String formatZoneOffset(int offset, int count) {
		offset /= 1000; // milliseconds to seconds
		StringBuilder tb = new StringBuilder();

		if (offset < 0) {
			tb.insert(0, "-");
			offset = -offset;
		} else {
			tb.insert(0, "+");
		}

		int hours = offset / 3600;
		int minutes = (offset % 3600) / 60;

		tb.append(zeroPad(hours, 2));
		if (count == 2)
			tb.append(":");
		tb.append(zeroPad(minutes, 2));
		return tb.toString();
	}

	private static final String getYearString(Calendar inDate, int count) {
		int year = inDate.get(Calendar.YEAR);
		return (count <= 2) ? zeroPad(year % 100, 2) : String.valueOf(year);
	}

	private static final String zeroPad(int inValue, int inMinDigits) {
		String val = String.valueOf(inValue);

		if (val.length() < inMinDigits) {
			char[] buf = new char[inMinDigits];

			for (int i = 0; i < inMinDigits; i++)
				buf[i] = '0';

			val.getChars(0, val.length(), buf, inMinDigits - val.length());
			val = new String(buf);
		}
		return val;
	}
}