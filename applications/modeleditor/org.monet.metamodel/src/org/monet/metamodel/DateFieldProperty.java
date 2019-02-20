package org.monet.metamodel;

public class DateFieldProperty extends DateFieldPropertyBase {

  private static final class Formats {
    private static final class English {
      private static final String YEARS   = "Y";
      private static final String MONTHS  = "Y/m";
      private static final String DAYS    = "Y/m/d";
      private static final String HOURS   = "Y/m/d H";
      private static final String MINUTES = "Y/m/d H:i";
      private static final String SECONDS = "Y/m/d H:i:s";
    }

    private static final class Spanish {
      private static final String YEARS   = "Y";
      private static final String MONTHS  = "m/Y";
      private static final String DAYS    = "d/m/Y";
      private static final String HOURS   = "d/m/Y H";
      private static final String MINUTES = "d/m/Y H:i";
      private static final String SECONDS = "d/m/Y H:i:s";
    }
  }

  public String getFormat(String language) {

    if (this._precision == null) {
      this._precision = PrecisionEnumeration.SECONDS;
    }

    switch (this._precision) {
      case YEARS:
        if (language.equals("es"))
          return DateFieldProperty.Formats.Spanish.YEARS;
        else
          return DateFieldProperty.Formats.English.YEARS;
      case MONTHS:
        if (language.equals("es"))
          return DateFieldProperty.Formats.Spanish.MONTHS;
        else
          return DateFieldProperty.Formats.English.MONTHS;
      case DAYS:
        if (language.equals("es"))
          return DateFieldProperty.Formats.Spanish.DAYS;
        else
          return DateFieldProperty.Formats.English.DAYS;
      case HOURS:
        if (language.equals("es"))
          return DateFieldProperty.Formats.Spanish.HOURS;
        else
          return DateFieldProperty.Formats.English.HOURS;
      case MINUTES:
        if (language.equals("es"))
          return DateFieldProperty.Formats.Spanish.MINUTES;
        else
          return DateFieldProperty.Formats.English.MINUTES;
      case SECONDS:
        if (language.equals("es"))
          return DateFieldProperty.Formats.Spanish.SECONDS;
        else
          return DateFieldProperty.Formats.English.SECONDS;
      default:
        if (language.equals("es"))
          return DateFieldProperty.Formats.Spanish.SECONDS;
        else
          return DateFieldProperty.Formats.English.SECONDS;
    }
  }

}