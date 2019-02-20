package cosmos.types;

public class DateRange {

    private final Date minValue;
    private final Date maxValue;
    private final DatePrecision precision;

    public DateRange(Date minValue, Date maxValue, DatePrecision precision) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.precision = precision;
    }

    public boolean dateBetween(Date date) {
        return date != null && precision.dateBetweenDates(date, minValue, maxValue);
    }
}
