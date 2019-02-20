package cosmos.types;

import java.util.HashMap;
import java.util.Map;

public class DayOfWeek {

    public static final DayOfWeek MONDAY = new DayOfWeek();
    public static final DayOfWeek TUESDAY = new DayOfWeek();
    public static final DayOfWeek WEDNESDAY = new DayOfWeek();
    public static final DayOfWeek THURSDAY = new DayOfWeek();
    public static final DayOfWeek FRIDAY = new DayOfWeek();
    public static final DayOfWeek SATURDAY = new DayOfWeek();
    public static final DayOfWeek SUNDAY = new DayOfWeek();

    private static final Map<Integer, DayOfWeek> daysOfWeek = new HashMap<>();

    static {
        daysOfWeek.put(0, DayOfWeek.SUNDAY);
        daysOfWeek.put(1, DayOfWeek.MONDAY);
        daysOfWeek.put(2, DayOfWeek.TUESDAY);
        daysOfWeek.put(3, DayOfWeek.WEDNESDAY);
        daysOfWeek.put(4, DayOfWeek.THURSDAY);
        daysOfWeek.put(5, DayOfWeek.FRIDAY);
        daysOfWeek.put(6, DayOfWeek.SATURDAY);
    }

    private DayOfWeek() {
    }

    public static DayOfWeek getDayOfWeek(int year, int month, int day) {
        return daysOfWeek.get(dayOfWeekNumber(year, month, day));
    }

    // http://en.wikipedia.org/wiki/Determination_of_the_day_of_the_week#Basic_method_for_mental_calculation
    private static int dayOfWeekNumber(int year, int month, int day) {
        return (day + calculateMonth(month, year) + year % 100 + ((year % 100) / 4) + calculateCenturyNumber(year)) % 7;
    }

    private static int calculateMonth(int month, int year) {
        if (month == 1) return isLeapYear(year) ? 6 : 0;
        if (month == 2) return isLeapYear(year) ? 2 : 3;
        if (month == 3) return 3;
        if (month == 4) return 6;
        if (month == 5) return 1;
        if (month == 6) return 4;
        if (month == 7) return 6;
        if (month == 8) return 2;
        if (month == 9) return 5;
        if (month == 10) return 0;
        if (month == 11) return 3;
        return 5;
    }

    private static int calculateCenturyNumber(int year) {
        int century = year / 100;
        if (century % 4 == 0) return 6;
        if ((century + 3) % 4 == 0) return 4;
        return (century + 2) % 4 == 0 ? 2 : 0;
    }

    private static boolean isLeapYear(int year) {
        return ((year % 4 == 0) && (year % 100 != 0)) || ((year % 100 == 0) && (year % 400 == 0));
    }
}
