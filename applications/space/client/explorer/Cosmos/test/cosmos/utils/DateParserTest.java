package cosmos.utils;

import cosmos.services.translators.DateTranslator;
import cosmos.types.Date;
import cosmos.types.DatePrecision;
import cosmos.types.DayOfWeek;
import cosmos.utils.date.DateBuilder;
import cosmos.utils.date.DateParser;
import cosmos.utils.date.MalformedDateException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cosmos.types.DateComponent.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class DateParserTest {

    private static DateParser parser;

    @BeforeClass
    public static void setUpClass() {
        parser = new DateParser(createTranslator(), new TestDateBuilder(), DatePrecision.DAY);
    }

    @Test
    public void checkParseEmptyString() {
        assertNull(parser.parse(""));
    }

    @Test
    public void checkParseYear() {
        assertThat(new Date(2010), is(parser.parse("2010")));
        assertThat(new Date(2014), is(parser.parse("2014")));
    }

    @Test
    public void checkParseYearAndMonth() {
        assertThat(new Date(2010, 1), is(parser.parse("1/2010")));
        assertThat(new Date(2014, 11), is(parser.parse("11/2014")));
    }

    @Test
    public void checkParseYearAndMonthWithMonthAsText() {
        assertThat(new Date(2010, 1), is(parser.parse("Enero de 2010")));
        assertThat(new Date(2014, 11), is(parser.parse("Noviembre de 2014")));
    }

    @Test
    public void checkParseYearMonthAndDay() {
        assertThat(new Date(2010, 1, 23), is(parser.parse("23/1/2010")));
        assertThat(new Date(2014, 11, 3), is(parser.parse("3/11/2014")));
        assertThat(new Date(2014, 11, 3), is(parser.parse("3 / 11 / 2014")));
    }

    @Test
    public void checkParseYearMonthAndDayWithMonthAsText() {
        assertThat(new Date(2010, 1, 23), is(parser.parse("23 de enero de 2010")));
        assertThat(new Date(2014, 11, 3), is(parser.parse("3 del 11 de 2014")));
    }

    @Test
    public void checkParseYearMonthDayAndHour() {
        assertThat(new Date(2010, 1, 23, 10), is(parser.parse("23 de enero de 2010 10")));
        assertThat(new Date(2014, 11, 3, 9), is(parser.parse("3 del 11 de 2014 9")));
    }

    @Test
    public void checkParseYearMonthDayHourAndMinutes() {
        assertThat(new Date(2010, 1, 23, 10, 15), is(parser.parse("23 de enero de 2010 10:15")));
        assertThat(new Date(2014, 11, 3, 9, 10), is(parser.parse("3 del 11 de 2014 9:10")));
    }

    @Test
    public void checkParseYearMonthDayHourMinutesAndSeconds() {
        assertThat(new Date(2010, 1, 23, 10, 15, 25), is(parser.parse("23 de enero de 2010 10:15:25")));
        assertThat(new Date(2014, 11, 3, 9, 10, 10), is(parser.parse("3 del 11 de 2014 9:10:10")));
        assertThat(new Date(2014, 10, 22, 10, 30, 14), is(parser.parse("22/10/2014/10:30:14")));
    }

    @Test
    public void checkParseWithContext() {
        DateParser parser = new DateParser(createTranslator(), new TestDateBuilder(), DatePrecision.DAY);
        assertThat(new Date(2010, 1, 23), is(parser.parse("23 de enero", new Date(2010))));

        parser = new DateParser(createTranslator(), new TestDateBuilder(), DatePrecision.HOUR);
        assertThat(new Date(2010, 1, 27, 10), is(parser.parse("27 a las 10", new Date(2010, 1))));

        parser = new DateParser(createTranslator(), new TestDateBuilder(), DatePrecision.MINUTE);
        assertThat(new Date(2010, 1, 23, 10, 30), is(parser.parse("a las 10:30", new Date(2010, 1, 23))));
        assertThat(new Date(2014, 5, 25, 12, 15), is(parser.parse("25 de mayo del 2014 a las 12:15", new Date(2012, 3, 17))));
    }

    @Test(expected = MalformedDateException.class)
    public void checkParserWithInvalidInput() {
        parser.parse("hola", new Date(2010));
        parser.parse("diciembre de diciembre del 2010", new Date(2010, 10, 10));
    }


    private static DateTranslator createTranslator() {
        return new DateTranslator() {
            @Override
            public int dayOfWeekToNumber(DayOfWeek dayOfWeek) {
                if (dayOfWeek == DayOfWeek.MONDAY) return 0;
                if (dayOfWeek == DayOfWeek.TUESDAY) return 1;
                if (dayOfWeek == DayOfWeek.WEDNESDAY) return 2;
                if (dayOfWeek == DayOfWeek.THURSDAY) return 3;
                if (dayOfWeek == DayOfWeek.FRIDAY) return 4;
                if (dayOfWeek == DayOfWeek.SATURDAY) return 5;
                if (dayOfWeek == DayOfWeek.SUNDAY) return 6;
                return -1;
            }

            @Override
            public int monthToNumber(String month) {
                if (month.equalsIgnoreCase("Enero") || month.equalsIgnoreCase("Ene")) return 1;
                if (month.equalsIgnoreCase("Febrero") || month.equalsIgnoreCase("Feb")) return 2;
                if (month.equalsIgnoreCase("Marzo") || month.equalsIgnoreCase("Mar")) return 3;
                if (month.equalsIgnoreCase("Abril") || month.equalsIgnoreCase("Abr")) return 4;
                if (month.equalsIgnoreCase("Mayo") || month.equalsIgnoreCase("May")) return 5;
                if (month.equalsIgnoreCase("Junio") || month.equalsIgnoreCase("Jun")) return 6;
                if (month.equalsIgnoreCase("Julio") || month.equalsIgnoreCase("Jul")) return 7;
                if (month.equalsIgnoreCase("Agosto") || month.equalsIgnoreCase("Ago")) return 8;
                if (month.equalsIgnoreCase("Septiembre") || month.equalsIgnoreCase("Sep")) return 9;
                if (month.equalsIgnoreCase("Octubre") || month.equalsIgnoreCase("Oct")) return 10;
                if (month.equalsIgnoreCase("Noviembre") || month.equalsIgnoreCase("Nov")) return 11;
                if (month.equalsIgnoreCase("Diciembre") || month.equalsIgnoreCase("Dic")) return 12;
                return -1;
            }

            @Override
            public String monthNumberToString(Integer month) {
                if (month == 1) return "Enero";
                if (month == 2) return "Febrero";
                if (month == 3) return "Marzo";
                if (month == 4) return "Abril";
                if (month == 5) return "Mayo";
                if (month == 6) return "Junio";
                if (month == 7) return "Julio";
                if (month == 8) return "Agosto";
                if (month == 9) return "Septiembre";
                if (month == 10) return "Octubre";
                if (month == 11) return "Noviembre";
                if (month == 12) return "Diciembre";
                return "";
            }

            @Override
            public String[] getDateSeparators() {
                return new String[]{"de", "del", "/", " ", ":", "a", "las"};
            }

            @Override
            public String translateDateWithPrecision(Date date, DatePrecision precision) {
                if (precision == DatePrecision.CENTURY)
                    return date.getCentury().toString();
                if (precision == DatePrecision.DECADE)
                    return date.getDecade().toString();
                if (precision == DatePrecision.YEAR)
                    return String.valueOf(date.getYear());
                if (precision == DatePrecision.MONTH)
                    return monthNumberToString(date.getMonth()) + " de " + date.getYear();
                return date.getDay() + " de " + monthNumberToString(date.getMonth()) + " de " + date.getYear();
            }

            @Override
            public String translateFullDate(Date date) {
                return "";
            }

            @Override
            public String translateFullDateByUser(Date date, String user) {
                return "";
            }
        };
    }


    private static class TestDateBuilder implements DateBuilder {

        private final Map<Integer, Builder> builders;
        
        public TestDateBuilder() {
            builders = new HashMap<>();
            builders.put(1, new Builder() {
                @Override
                public Date build(List<Integer> components) {
                    return new Date(components.get(0));
                }
            });
            builders.put(2, new Builder() {
                @Override
                public Date build(List<Integer> components) {
                    return new Date(components.get(1), components.get(0));
                }
            });
            builders.put(3, new Builder() {
                @Override
                public Date build(List<Integer> components) {
                    return new Date(components.get(2), components.get(1), components.get(0));
                }
            });
            builders.put(4, new Builder() {
                @Override
                public Date build(List<Integer> components) {
                    return new Date(components.get(2), components.get(1), components.get(0), components.get(3));
                }
            });
            builders.put(5, new Builder() {
                @Override
                public Date build(List<Integer> components) {
                    return new Date(components.get(2), components.get(1), components.get(0), components.get(3), components.get(4));
                }
            });
            builders.put(6, new Builder() {
                @Override
                public Date build(List<Integer> components) {
                    return new Date(components.get(2), components.get(1), components.get(0), components.get(3), components.get(4), components.get(5));
                }
            });
        }

        @Override
        public Date build(List<Integer> components) {
            if (!builders.containsKey(components.size())) return null;
            return builders.get(components.size()).build(components);
        }

        @Override
        public Date build(List<Integer> components, Date context) {
            if (components.size() == 1)
                return dateWithNewValue(context, components.get(0));
            return dateWithValues(context, components);
        }

        private Date dateWithNewValue(Date date, Integer value) {
            if (!date.isSet(YEAR)) return date.replaceYear(value);
            if (!date.isSet(MONTH)) return date.replaceMonth(value);
            if (!date.isSet(DAY)) return date.replaceDay(value);
            if (!date.isSet(HOUR)) return date.replaceHours(value);
            if (!date.isSet(MINUTE)) return date.replaceMinutes(value);
            if (!date.isSet(SECOND)) return date.replaceSeconds(value);
            return date;
        }

        private Date dateWithValues(Date date, List<Integer> values) {
            Date newDate = date;
            while (!values.isEmpty()) {
                if (!newDate.isSet(YEAR)) {
                    int index = values.size() == 1 ? 0 : values.size() == 2 ? 1 : 2;
                    newDate = newDate.replaceHours(values.remove(index));
                } else if (!newDate.isSet(MONTH)) {
                    int index = values.size() > 1 ? 1 : 0;
                    newDate = newDate.replaceMonth(values.remove(index));
                } else if (!newDate.isSet(DAY))
                    newDate = newDate.replaceDay(values.remove(0));
                else if (!newDate.isSet(HOUR))
                    newDate = newDate.replaceHours(values.remove(0));
                else if (!newDate.isSet(MINUTE))
                    newDate = newDate.replaceMinutes(values.remove(0));
                else if (!newDate.isSet(SECOND))
                    newDate = newDate.replaceSeconds(values.remove(0));
            }
            return newDate;
        }

        private interface Builder {
            Date build(List<Integer> components);
        }
    }
}
