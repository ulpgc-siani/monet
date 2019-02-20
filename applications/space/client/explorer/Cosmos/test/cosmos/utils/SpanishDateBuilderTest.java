package cosmos.utils;

import cosmos.types.Date;
import cosmos.utils.date.MalformedDateException;
import cosmos.utils.date.SpanishDateBuilder;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class SpanishDateBuilderTest {

    private static SpanishDateBuilder builder;
    private static List<Integer> components;

    @BeforeClass
    public static void setUpClass() {
        builder = new SpanishDateBuilder();
        components = new ArrayList<>();
    }

    @Before
    public void setUp() throws Exception {
        components.clear();
    }

    @Test(expected = MalformedDateException.class)
    public void shouldThrowMalformedDateExceptionWhenMonthValueIsInvalid() {
        components.add(11);
        components.add(22);
        components.add(2334);
        builder.build(components);
    }

    @Test(expected = MalformedDateException.class)
    public void shouldThrowMalformedDateExceptionWhenDayValueIsInvalid() {
        components.add(33);
        components.add(1);
        components.add(2007);
        builder.build(components);
    }

    @Test(expected = MalformedDateException.class)
    public void shouldThrowMalformedDateExceptionWhenDayValueIsInvalidInCertainMonth() {
        components.add(31);
        components.add(4);
        components.add(2008);
        builder.build(components);
    }

    @Test
    public void shouldBuildDateWithValidComponents() {
        components.add(29);
        components.add(2);
        components.add(2008);
        final Date date = builder.build(components);
        assertThat(date.getYear(), is(2008));
        assertThat(date.getMonth(), is(2));
        assertThat(date.getDay(), is(29));
    }

    @Test(expected = MalformedDateException.class)
    public void shouldThrowMalformedDateExceptionWhenDayIsInvalidInCertainMonthAndYear() {
        components.add(29);
        components.add(2);
        components.add(2007);
        builder.build(components);
    }

    @Test(expected = MalformedDateException.class)
    public void shouldThrowMalformedDateExceptionWhenHourIsInvalid() {
        components.add(1);
        components.add(2);
        components.add(2007);
        components.add(25);
        builder.build(components);
    }

    @Test(expected = MalformedDateException.class)
    public void shouldThrowMalformedDateExceptionWhenMinuteIsInvalid() {
        components.add(9);
        components.add(2);
        components.add(2007);
        components.add(21);
        components.add(225);
        builder.build(components);
    }

    @Test(expected = MalformedDateException.class)
    public void shouldThrowMalformedDateExceptionWhenSecondIsInvalid() {
        components.add(2);
        components.add(2);
        components.add(2007);
        components.add(21);
        components.add(21);
        components.add(225);
        builder.build(components);
    }
}
