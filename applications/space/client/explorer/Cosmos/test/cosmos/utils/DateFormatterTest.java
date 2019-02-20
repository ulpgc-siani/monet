package cosmos.utils;

import cosmos.types.Date;
import org.junit.Test;

import static cosmos.utils.date.DateFormatter.format;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class DateFormatterTest {

    @Test
    public void formatDateYear() {
        assertThat("2014", is(format(new Date(2014), "yyyy")));
        assertThat("201", is(format(new Date(201), "yyyy")));
        assertThat("14", is(format(new Date(2014), "yy")));
    }

    @Test
    public void formatDateYearAndMonth() {
        assertThat("10/2014", is(format(new Date(2014, 10), "MM/yyyy")));
        assertThat("2014/09", is(format(new Date(2014, 9), "yyyy/MM")));
        assertThat("2014-09", is(format(new Date(2014, 9), "yyyy-MM")));
        assertThat("14-11", is(format(new Date(2014, 11), "yy-MM")));
    }

    @Test
    public void formatDateYearMonthAndDay() {
        assertThat("2014/11/03", is(format(new Date(2014, 11, 3), "yyyy/MM/dd")));
        assertThat("14/11/03", is(format(new Date(2014, 11, 3), "yy/MM/dd")));
    }

    @Test
    public void formatDateWhenAllDateComponentsAreNotSet() {
        assertThat("10/2015", is(format(new Date(2015, 10), "dd/MM/yyyy")));
        assertThat("2015/11", is(format(new Date(2015, 11), "yyyy/MM/dd")));
    }
}
