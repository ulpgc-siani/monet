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

package org.monet.v2.model;

import org.monet.v2.model.constants.Strings;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class LibraryDate {
  
  private static final long ONE_HOUR = 60 * 60 * 1000L;

  public class Format {
    public static final String TEXT     = "text";
    public static final String NUMERIC  = "numeric";
    public static final String INTERNAL = "internal";
    public static final String DEFAULT  = Format.INTERNAL;
  }
  
  public class Language {
    public static final String ENGLISH    = "en";
    public static final String SPANISH    = "es";
    public static final String DEUTSCH    = "de";
    public static final String FRENCH     = "fr";  
    public static final String PORTUGUESE = "pt";
  }

  private static final String[] aLabelsAt = { "a las", "at" };
  private static final String[] aLabelsOf = { "de", "of" };

  private static Date parseInternalDate(String sDate) {
    DateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy/HH:mm:ss");
    try { return dfDate.parse(sDate); }
    catch (ParseException oExceptionDateTime) {
      dfDate = new SimpleDateFormat("dd/MM/yyyy");
      try { return dfDate.parse(sDate); }
      catch (ParseException oExceptionDate) {
        dfDate = new SimpleDateFormat("MM/yyyy");
        try { return dfDate.parse(sDate); }
        catch (ParseException oExceptionMonth) {
          if (sDate.length() > 4) return null;
          dfDate = new SimpleDateFormat("yyyy");
          try { return dfDate.parse(sDate); }
          catch (ParseException oExceptionDay) { 
            return null; 
          }
        }
      }
    }
  }

  public static String getDateAndTimeString(Date dtDate, String codeLanguage, String sFormat, Boolean bShowTime, String sSeparator) {
    SimpleDateFormat dayFormat   = new SimpleDateFormat("EEEE, dd");
    SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM");
    SimpleDateFormat yearFormat  = new SimpleDateFormat("yyyy");
    SimpleDateFormat timeFormat  = new SimpleDateFormat("HH:mm:ss");
    String sAtLabel, sOfLabel, sDate = Strings.EMPTY;
    String sTime = Strings.EMPTY;
    String sDay, sMonth, sYear = Strings.EMPTY;
    
    if (dtDate == null) return "";
    
    sAtLabel = (codeLanguage == Language.SPANISH)?aLabelsAt[0]:aLabelsAt[1];
    sOfLabel = (codeLanguage == Language.SPANISH)?aLabelsOf[0]:aLabelsOf[1];

    if (! sFormat.equals(Format.TEXT)) {
      dayFormat.applyPattern("dd");
      monthFormat.applyPattern("MM");
      yearFormat.applyPattern("yyyy");
    }
    
    if (bShowTime) {
      if (sFormat == Format.TEXT) sTime = sAtLabel + Strings.SPACE + timeFormat.format(dtDate);
      else sTime = timeFormat.format(dtDate);
    }

    sDay   = LibraryString.capitalize(dayFormat.format(dtDate));
    sMonth = LibraryString.capitalize(monthFormat.format(dtDate));
    sYear  = LibraryString.capitalize(yearFormat.format(dtDate));

    if (sFormat.equals(Format.INTERNAL)) return sDay + Strings.BAR45 + sMonth + Strings.BAR45 + sYear + Strings.BAR45 + sTime;

    if (codeLanguage == Language.ENGLISH) {
      if (sFormat == Format.NUMERIC) sDate = sMonth + sSeparator + sDay + sSeparator + sYear + Strings.SPACE + sTime;
      else sDate = sDay + Strings.SPACE + sMonth + Strings.SPACE + sYear + Strings.SPACE + sTime;
    }
    else if (codeLanguage == Language.DEUTSCH) {
      if (sFormat == Format.NUMERIC) sDate = sDay + sSeparator + sMonth + sSeparator + sYear + Strings.SPACE + sTime;
      else sDate = sDay + Strings.SPACE + sMonth + Strings.SPACE + sYear + Strings.SPACE + sTime;
    }
    else if (codeLanguage == Language.SPANISH) {
      if (sFormat == Format.NUMERIC) sDate = sDay + sSeparator + sMonth + sSeparator + sYear + Strings.SPACE + sTime;
      else sDate = sDay + Strings.SPACE + sOfLabel + Strings.SPACE +  sMonth + Strings.SPACE + sOfLabel + Strings.SPACE + sYear + Strings.SPACE + sTime;
    }
    else {
      if (sFormat == Format.NUMERIC) sDate = sMonth + sSeparator + sDay + sSeparator + sYear + Strings.SPACE + sTime;
      else sDate = sDay + Strings.SPACE + sMonth + Strings.SPACE + sYear + Strings.SPACE + sTime;
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

  public static String formatTime(Date dtDate, String codeLanguage, String sFormat, String sSeparator) {
    SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
    SimpleDateFormat minuteFormat = new SimpleDateFormat("mm");
    return hourFormat.format(dtDate) + sSeparator + minuteFormat.format(dtDate);
  }
  
  public static Date parseDate(String sDate) {
    DateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss");
    Date dtResult = LibraryDate.parseInternalDate(sDate);
    
    if (dtResult != null) return dtResult;
    
    try { return dfDate.parse(sDate); }
    catch (ParseException oExceptionDateTime) {
      dfDate = new SimpleDateFormat("yyyy-MM-dd");
      try { return dfDate.parse(sDate); }
      catch (ParseException oExceptionDate) {
        dfDate = new SimpleDateFormat("yyyy-MM");
        try { return dfDate.parse(sDate); }
        catch (ParseException oExceptionMonth) {
          dfDate = new SimpleDateFormat("yyyy");
          try { return dfDate.parse(sDate); }
          catch (ParseException oExceptionDay) {
            return null; 
          }
        }
      }
    }
  }
  
  public static Date parsePartialDate(String sDate) {
    Pattern p = Pattern.compile("(\\d\\d)?[^\\d]*(\\d\\d\\d\\d)");
    Matcher m = p.matcher(sDate);
    if(m.find()) {
      String sMonth = m.group(1);
      String sYear = m.group(2);
      
      int day = 1;
      int month = sMonth != null ? Integer.parseInt(sMonth) - 1  : 0;
      int year = sYear != null ? Integer.parseInt(sYear) : 1970;
      
      Calendar cal = Calendar.getInstance();
      cal.set(year, month, day);
      return cal.getTime();
    } else {
      return null;
    }
  }

  public static long getDaysBetweenDates(Date startDate, Date endDate) {
    return ((endDate.getTime() - startDate.getTime() + ONE_HOUR) / (ONE_HOUR * 24));
  }

  public static String formatDate(Date date, String codeLanguage, String format, String separator) {
    SimpleDateFormat dateFormat;
    String yearFormat  = "yyyy";
    String monthFormat = "MMMM";
    String dayFormat = "dd";
    String hourFormat  = "HH";
    String minutesFormat  = "mm";
    String secondsFormat  = "ss";
    String formatValue = "";
    
    format = format.toLowerCase();
    
    if (format.equals("days")) {
      if (codeLanguage == Language.ENGLISH) formatValue = monthFormat + separator + dayFormat + separator + yearFormat;
      else formatValue = dayFormat + separator + monthFormat + separator + yearFormat;
    }
    else if (format.equals("months")) formatValue = monthFormat + separator + yearFormat;
    else if (format.equals("years")) formatValue = yearFormat;
    else if (format.equals("hours")) formatValue = hourFormat;
    else if (format.equals("minutes")) formatValue = hourFormat + ":" + minutesFormat;
    else if (format.equals("seconds")) formatValue = hourFormat + ":" + minutesFormat + ":" + secondsFormat;
    
    dateFormat = new SimpleDateFormat(formatValue);
    
    return LibraryString.capitalize(dateFormat.format(date));
  }

}
