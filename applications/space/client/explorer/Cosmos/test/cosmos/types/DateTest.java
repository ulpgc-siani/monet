package cosmos.types;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class DateTest {

    @Test
    public void checkGetNumberOfDayOfWeek() {
        assertThat(DayOfWeek.MONDAY, is(new Date(2014, 10, 20).getDayOfWeek()));
        assertThat(DayOfWeek.TUESDAY, is(new Date(2014, 10, 21).getDayOfWeek()));
        assertThat(DayOfWeek.WEDNESDAY, is(new Date(2014, 10, 22).getDayOfWeek()));
        assertThat(DayOfWeek.THURSDAY, is(new Date(2014, 10, 23).getDayOfWeek()));
        assertThat(DayOfWeek.FRIDAY, is(new Date(2014, 10, 24).getDayOfWeek()));
        assertThat(DayOfWeek.SATURDAY, is(new Date(2014, 10, 25).getDayOfWeek()));
        assertThat(DayOfWeek.SUNDAY, is(new Date(2014, 10, 26).getDayOfWeek()));
    }

    @Test
    @SuppressWarnings("deprecation")
    public void checkTranslateToJavaDate() {
        assertThat(new java.util.Date(114, 10, 3), is(new Date(2014, 11, 3).toJavaDate()));
        assertThat(new java.util.Date(114, 10, 3, 10, 29, 23), is(new Date(2014, 11, 3, 10, 29, 23).toJavaDate()));
    }

    @Test
    public void checkGetNumberOfDaysInMonth() {
        assertThat(31, is(new Date(2014, 1).getDaysInMonth()));
        assertThat(28, is(new Date(2014, 2).getDaysInMonth()));
        assertThat(29, is(new Date(2012, 2).getDaysInMonth()));
        assertThat(30, is(new Date(2014, 6).getDaysInMonth()));
    }

    @Test
    public void checkDateNextYear() {
        assertThat(new Date(2014), is(new Date(2013).nextYear()));
        assertThat(new Date(2013, 3, 1), is(new Date(2012, 2, 29).nextYear()));
        assertThat(new Date(2012, 11, 4), is(new Date(2011, 11, 4).nextYear()));
    }

    @Test
    public void checkDatePreviousYear() {
        assertThat(new Date(2013), is(new Date(2014).previousYear()));
        assertThat(new Date(2011, 2, 28), is(new Date(2012, 2, 29).previousYear()));
        assertThat(new Date(2013, 11, 4), is(new Date(2014, 11, 4).previousYear()));
    }

    @Test
    public void checkDateNextMonth() {
        assertThat(new Date(2012, 2), is(new Date(2012, 1).nextMonth()));
        assertThat(new Date(2013, 1), is(new Date(2012, 12).nextMonth()));
        assertThat(new Date(2012, 2, 29), is(new Date(2012, 1, 30).nextMonth()));
        assertThat(new Date(2011, 2, 28), is(new Date(2011, 1, 30).nextMonth()));
        assertThat(new Date(2011, 4, 30), is(new Date(2011, 3, 31).nextMonth()));
    }

    @Test
    public void checkDatePreviousMonth() {
        assertThat(new Date(2012, 4), is(new Date(2012, 5).previousMonth()));
        assertThat(new Date(2009, 12), is(new Date(2010, 1).previousMonth()));
        assertThat(new Date(2012, 2, 29), is(new Date(2012, 3, 31).previousMonth()));
        assertThat(new Date(2011, 2, 28), is(new Date(2011, 3, 30).previousMonth()));
        assertThat(new Date(2011, 4, 30), is(new Date(2011, 5, 31).previousMonth()));
    }

    @Test
    public void checkDateAtFirstDayOfMonth() {
        assertThat(new Date(2014, 3, 1), is(new Date(2014, 3, 23).atFirstDayOfMonth()));
        assertThat(new Date(2012, 8, 1), is(new Date(2012, 8, 12).atFirstDayOfMonth()));
    }

    @Test
    public void checkGetYearsInDecade() {
        assertThat(Arrays.asList(2000, 2001, 2002, 2003, 2004, 2005, 2006, 2007, 2008, 2009), is(new Date(2002).getYearsInDecade()));
    }

    @Test
    public void checkDateNextDecade() {
        assertThat(new Date(2022), is(new Date(2012).nextDecade()));
        assertThat(new Date(2018, 3, 1), is(new Date(2008, 2, 29).nextDecade()));
        assertThat(new Date(2020, 1, 23), is(new Date(2010, 1, 23).nextDecade()));
    }

    @Test
    public void checkDatePreviousDecade() {
        assertThat(new Date(2002), is(new Date(2012).previousDecade()));
        assertThat(new Date(2006, 2, 28), is(new Date(2016, 2, 29).previousDecade()));
        assertThat(new Date(2010, 1, 23), is(new Date(2020, 1, 23).previousDecade()));
    }

    @Test
    public void checkDateNextDay() {
        assertThat(new Date(2014, 10, 5), is(new Date(2014, 10, 4).nextDay()));
        assertThat(new Date(2014, 3, 1), is(new Date(2014, 2, 28).nextDay()));
        assertThat(new Date(2012, 2, 29), is(new Date(2012, 2, 28).nextDay()));
        assertThat(new Date(2014, 6, 1), is(new Date(2014, 5, 31).nextDay()));
        assertThat(new Date(2015, 1, 1), is(new Date(2014, 12, 31).nextDay()));
    }

    @Test
    public void checkDatePreviousDay() {
        assertThat(new Date(2014, 6, 22), is(new Date(2014, 6, 23).previousDay()));
        assertThat(new Date(2014, 2, 28), is(new Date(2014, 3, 1).previousDay()));
        assertThat(new Date(2012, 2, 29), is(new Date(2012, 3, 1).previousDay()));
        assertThat(new Date(2012, 7, 31), is(new Date(2012, 8, 1).previousDay()));
        assertThat(new Date(1992, 12, 31), is(new Date(1993, 1, 1).previousDay()));
    }

    @Test
    public void checkDateGetDecade() {
        assertThat(new Decade(2012), is(new Date(2012, 6, 22).getDecade()));
        assertThat(new Decade(2006), is(new Date(2002, 6, 22).getDecade()));
    }

    @Test
    public void checkDateGetCentury() {
        assertThat(new Century(2012), is(new Date(2012, 6, 22).getCentury()));
        assertThat(new Century(2006), is(new Date(2002, 6, 22).getCentury()));
        assertThat(new Century(1945), is(new Date(1992, 6, 22).getCentury()));
    }

    @Test
    public void checkDateGetDecadesFromCentury() {
        List<Decade> expectedList = new ArrayList<>();
        for (int decade = 1900; decade < 2000; decade+=10)
            expectedList.add(new Decade(decade));
        assertThat(expectedList, is(new Century(1992).getDecades()));
    }

    @Test
    public void checkDecadeToString() {
        assertThat("1990 - 1999", is(new Decade(1995).toString()));
        assertThat("2000 - 2009", is(new Decade(2006).toString()));
    }

    @Test
    public void checkCenturyToString() {
        assertThat("1900 - 1999", is(new Century(1914).toString()));
    }
}
