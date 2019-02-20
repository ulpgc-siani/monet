package client.utils;

import cosmos.types.Date;
import cosmos.types.DatePrecision;
import cosmos.types.Decade;
import org.junit.BeforeClass;
import org.junit.Test;

import static client.core.model.definition.entity.field.DateFieldDefinition.RangeDefinition;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DateUtilsTest {

    private static DateUtils dateUtils;

    @BeforeClass
    public static void setUpClass() {
        dateUtils = new DateUtils(new RangeDefinition() {
            @Override
            public long getMin() {
                return new Date(2014, 11, 1).getMilliseconds();
            }

            @Override
            public long getMax() {
                return new Date(2030, 1, 1).getMilliseconds();
            }
        }, DatePrecision.DAY, true);
    }

    @Test
    public void checkDecadeInsideRange() {
        assertTrue(dateUtils.decadeInsideRange(new Decade(2013)));
        assertTrue(dateUtils.decadeInsideRange(new Decade(2018)));
        assertFalse(dateUtils.decadeInsideRange(new Decade(2040)));
    }

    @Test
    public void checkYearInsideRange() {
        assertTrue(dateUtils.yearInsideRange(new Date(2018)));
        assertTrue(dateUtils.yearInsideRange(new Date(2014)));
        assertTrue(dateUtils.yearInsideRange(new Date(2030)));
        assertFalse(dateUtils.yearInsideRange(new Date(2000)));
    }

    @Test
    public void checkMonthOfYearInsideRange() {
        assertTrue(dateUtils.monthInsideRange(new Date(2014, 11)));
        assertFalse(dateUtils.monthInsideRange(new Date(2030, 11)));
        assertFalse(dateUtils.monthInsideRange(new Date(2014, 5)));
    }

    @Test
    public void checkDayOfMonthOfYearInsideRange() {
        assertTrue(dateUtils.dayInsideRange(new Date(2014, 11, 1)));
        assertTrue(dateUtils.dayInsideRange(new Date(2016, 2, 23)));
        assertFalse(dateUtils.dayInsideRange(new Date(2014, 10, 31)));
        assertFalse(dateUtils.dayInsideRange(new Date(2030, 1, 2)));
    }
}
