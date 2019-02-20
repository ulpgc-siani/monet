package client.utils;

import cosmos.types.Century;
import cosmos.types.Date;
import cosmos.types.DatePrecision;
import cosmos.types.Decade;

import static client.core.model.definition.entity.field.DateFieldDefinition.RangeDefinition;

public class DateUtils {

    private final Date minDate;
    private final Date maxDate;
    private final DatePrecision precision;
    private final boolean nearDate;

    public DateUtils(RangeDefinition rangeDefinition, DatePrecision precision, boolean nearDate) {
        this.minDate = rangeDefinition == null ? null : new Date(rangeDefinition.getMin());
        this.maxDate = rangeDefinition == null ? null : new Date(rangeDefinition.getMax());
        this.precision = precision;
        this.nearDate = nearDate;
    }

    public boolean centuryInsideRange(Century century) {
        for (Decade decade : century.getDecades())
            if (decadeInsideRange(decade)) return true;
        return false;
    }

    public boolean decadeInsideRange(Decade decade) {
        for (Integer year : decade.getYears())
            if (yearInsideRange(new Date(year))) return true;
        return false;
    }

    public boolean yearInsideRange(Date date) {
        return DatePrecision.YEAR.dateBetweenDates(date, minDate, maxDate);
    }

    public boolean monthInsideRange(Date date) {
        return DatePrecision.MONTH.dateBetweenDates(date, minDate, maxDate);
    }

    public boolean dayInsideRange(Date date) {
        return DatePrecision.DAY.dateBetweenDates(date, minDate, maxDate);
    }

    public DatePrecision getPrecision() {
        return precision;
    }

    public boolean isNearDate() {
        return nearDate;
    }

    public boolean hasMonthPrecision() {
        return precision == DatePrecision.MONTH || hasDayPrecision();
    }

    public boolean hasDayPrecision() {
        return precision == DatePrecision.DAY || hasTimePrecision();
    }

    public boolean hasTimePrecision() {
        return precision == DatePrecision.HOUR || precision == DatePrecision.MINUTE || precision == DatePrecision.SECOND;
    }

    public boolean isValidDate(Date date) {
        return precision.dateIsValid(date);
    }

    public static native int getCurrentTimeZone() /*-{
		return new Date().getTimezoneOffset()/60 * (-1);
	}-*/;

}
