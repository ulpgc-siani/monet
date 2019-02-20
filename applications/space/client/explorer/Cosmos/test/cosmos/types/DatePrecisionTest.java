package cosmos.types;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DatePrecisionTest {

    @Test
    public void checkBetweenYears() {
        assertTrue(DatePrecision.YEAR.dateBetweenDates(new Date(2010), new Date(2010), new Date(2012)));
        assertFalse(DatePrecision.YEAR.dateBetweenDates(new Date(2008), new Date(2010), new Date(2012)));
        assertFalse(DatePrecision.YEAR.dateBetweenDates(new Date(2014), new Date(2010), new Date(2012)));
    }

    @Test
    public void checkBetweenMonthsWithSameYear() {
        assertTrue(DatePrecision.MONTH.dateBetweenDates(new Date(2010, 3), new Date(2010, 1), new Date(2010, 5)));
        assertFalse(DatePrecision.MONTH.dateBetweenDates(new Date(2010, 5), new Date(2010, 1), new Date(2010, 3)));
        assertFalse(DatePrecision.MONTH.dateBetweenDates(new Date(2010, 1), new Date(2010, 2), new Date(2010, 3)));
    }

    @Test
    public void checkBetweenMonthsWithDifferentYears() {
        assertTrue(DatePrecision.MONTH.dateBetweenDates(new Date(2011, 8), new Date(2010, 6), new Date(2012, 2)));
    }

    @Test
    public void checkBetweenDaysWithSameMonth() {
        assertTrue(DatePrecision.DAY.dateBetweenDates(new Date(2010, 3, 22), new Date(2010, 3, 20), new Date(2010, 3, 25)));
        assertFalse(DatePrecision.DAY.dateBetweenDates(new Date(2010, 3, 27), new Date(2010, 3, 20), new Date(2010, 3, 25)));
    }

    @Test
    public void checkBetweenDaysWithDifferentYearAndMonth() {
        assertTrue(DatePrecision.DAY.dateBetweenDates(new Date(2011, 3, 22), new Date(2010, 1, 23), new Date(2012, 1, 5)));
        assertTrue(DatePrecision.DAY.dateBetweenDates(new Date(2014, 10, 27), new Date(2014, 10, 27), new Date(2018, 10, 27)));
    }

    @Test
    public void checkBetweenHours() {
        assertFalse(DatePrecision.HOUR.dateBetweenDates(new Date(2014, 3, 22, 15), new Date(2014, 3, 22, 16), new Date(2014, 3, 22, 17)));
        assertTrue(DatePrecision.HOUR.dateBetweenDates(new Date(2014, 10, 27, 16), new Date(2014, 10, 27, 15), new Date(2014, 10, 27, 17)));
        assertTrue(DatePrecision.HOUR.dateBetweenDates(new Date(2016, 10, 15, 14), new Date(2014, 10, 1, 7), new Date(2030, 12, 15, 7)));
    }

    @Test
    public void checkBetweenMinutes() {
        assertTrue(DatePrecision.MINUTE.dateBetweenDates(new Date(2014, 11, 21, 8, 42, 41), new Date(2014, 11, 6, 8, 45, 27), new Date(2016, 12, 15, 0, 0, 0)));
    }

    @Test
    public void checkBetweenSeconds() {
        assertTrue(DatePrecision.SECOND.dateBetweenDates(new Date(2010, 11, 1, 8, 34, 0), new Date(2010, 11, 1, 8, 0, 0), new Date(2010, 11, 1, 10, 0, 0)));
    }

    @Test
    public void checkBetweenHoursWhenPrecisionInMinutesMakesComparisonFalse() {
        assertTrue(DatePrecision.HOUR.dateBetweenDates(new Date(2010, 11, 1, 8, 0, 0), new Date(2010, 11, 1, 8, 3, 0), new Date(2010, 11, 1, 10, 0, 0)));
    }

    @Test
    public void checkBetweenMinutesWhenPrecisionInSecondsMakesComparisonFalse() {
        assertTrue(DatePrecision.MINUTE.dateBetweenDates(new Date(2010, 11, 1, 8, 3, 0), new Date(2010, 11, 1, 8, 3, 30), new Date(2010, 11, 1, 8, 3, 50)));
    }

    @Test
    public void checkRangeWhenDateIsNull() {
        assertTrue(DatePrecision.HOUR.dateBetweenDates(new Date(2010, 11, 1, 8, 0, 0), null, new Date(2010, 11, 1, 10, 0, 0)));
        assertFalse(DatePrecision.HOUR.dateBetweenDates(new Date(2010, 11, 1, 7, 0, 0), new Date(2010, 11, 1, 8, 3, 0), null));
        assertTrue(DatePrecision.HOUR.dateBetweenDates(new Date(2010, 11, 1, 8, 0, 0), null, null));
    }
}
