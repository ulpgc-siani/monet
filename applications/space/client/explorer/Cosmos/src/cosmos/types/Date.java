package cosmos.types;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cosmos.types.DateComponent.*;

public class Date {

    private static final long MILLIS_PER_SECOND = 1000;
    private static final long MILLIS_PER_MINUTE = 60 * MILLIS_PER_SECOND;
    private static final long MILLIS_PER_HOUR = 60 * MILLIS_PER_MINUTE;
    private static final long MILLIS_PER_DAY = 24 * MILLIS_PER_HOUR;
    private final Map<DateComponent, Integer> dateComponents = new HashMap<>(); 

    public Date() {
        this(System.currentTimeMillis());
    }
    
    public Date(long time) {
        final java.util.Date date = new java.util.Date(time);
        dateComponents.put(YEAR, year(date));
        dateComponents.put(MONTH, month(date));
        dateComponents.put(DAY, day(date));
        dateComponents.put(HOUR, hour(date));
        dateComponents.put(MINUTE, minute(date));
        dateComponents.put(SECOND, second(date));
    }
    
    public Date(int year) {
        this(year, null, null, null, null, null);
    }

    public Date(int year, int month) {
        this(year, month, null, null, null, null);
    }

    public Date(int year, int month, int day) {
        this(year, month, day, null, null, null);
    }

    public Date(int year, int month, int day, int hours) {
        this(year, month, day, hours, null, null);
    }

    public Date(int year, int month, int day, int hours, int minutes) {
        this(year, month, day, hours, minutes, null);
    }

    public Date(Integer year, Integer month, Integer day, Integer hours, Integer minutes, Integer seconds) {
        dateComponents.put(YEAR, year);
        dateComponents.put(MONTH, month);
        dateComponents.put(DAY, day);
        dateComponents.put(HOUR, hours);
        dateComponents.put(MINUTE, minutes);
        dateComponents.put(SECOND, seconds);
    }

    public static Date empty() {
        return new Date(2015).removeYear();
    }

    public Century getCentury() {
        return new Century(getYear());
    }

    public Decade getDecade() {
        return new Decade(getYear());
    }

    public int getYear() {
        if (dateComponents.get(YEAR) == null) return 1;
        return dateComponents.get(YEAR);
    }

    public int getMonth() {
        if (dateComponents.get(MONTH) == null) return 1;
        return dateComponents.get(MONTH);
    }

    public int getDay() {
        if (dateComponents.get(DAY) == null) return 1;
        return dateComponents.get(DAY);
    }

    public int getHours() {
        if (dateComponents.get(HOUR) == null) return 0;
        return dateComponents.get(HOUR);
    }

    public int getMinutes() {
        if (dateComponents.get(MINUTE) == null) return 0;
        return dateComponents.get(MINUTE);
    }

    public int getSeconds() {
        if (dateComponents.get(SECOND) == null) return 0;
        return dateComponents.get(SECOND);
    }

    public boolean isSet(DateComponent component) {
        return dateComponents.get(component) != null;
    }

    public Date removeComponentsFrom(DateComponent component) {
        if (component == DateComponent.YEAR) return empty();
        if (component == DateComponent.MONTH) return removeMonth().removeDay().removeHours().removeMinutes().removeSeconds();
        if (component == DateComponent.DAY) return removeDay().removeHours().removeMinutes().removeSeconds();
        if (component == DateComponent.HOUR) return removeHours().removeMinutes().removeSeconds();
        if (component == DateComponent.MINUTE) return removeMinutes().removeSeconds();
        if (component == DateComponent.SECOND) return removeSeconds();
        return this;
    }

    public Date removeYear() {
        return new Date(null, dateComponents.get(MONTH), dateComponents.get(DAY), dateComponents.get(HOUR), dateComponents.get(MINUTE), dateComponents.get(SECOND));
    }

    public Date removeMonth() {
        return new Date(dateComponents.get(YEAR), null, dateComponents.get(DAY), dateComponents.get(HOUR), dateComponents.get(MINUTE), dateComponents.get(SECOND));
    }

    public Date removeDay() {
        return new Date(dateComponents.get(YEAR), dateComponents.get(MONTH), null, dateComponents.get(HOUR), dateComponents.get(MINUTE), dateComponents.get(SECOND));
    }

    public Date removeHours() {
        return new Date(dateComponents.get(YEAR), dateComponents.get(MONTH), dateComponents.get(DAY), null, dateComponents.get(MINUTE), dateComponents.get(SECOND));
    }

    public Date removeMinutes() {
        return new Date(dateComponents.get(YEAR), dateComponents.get(MONTH), dateComponents.get(DAY), dateComponents.get(HOUR), null, dateComponents.get(SECOND));
    }

    public Date removeSeconds() {
        return new Date(dateComponents.get(YEAR), dateComponents.get(MONTH), dateComponents.get(DAY), dateComponents.get(HOUR), dateComponents.get(MINUTE), null);
    }

    public Date replaceYear(int year) {
        return new Date(year, dateComponents.get(MONTH), dateComponents.get(DAY), dateComponents.get(HOUR), dateComponents.get(MINUTE), dateComponents.get(SECOND));
    }

    public Date replaceMonth(int month) {
        return new Date(dateComponents.get(YEAR), month, dateComponents.get(DAY), dateComponents.get(HOUR), dateComponents.get(MINUTE), dateComponents.get(SECOND));
    }

    public Date replaceDay(int day) {
        return new Date(dateComponents.get(YEAR), dateComponents.get(MONTH), day, dateComponents.get(HOUR), dateComponents.get(MINUTE), dateComponents.get(SECOND));
    }

    public Date replaceHours(int hours) {
        return new Date(dateComponents.get(YEAR), dateComponents.get(MONTH), dateComponents.get(DAY), hours, dateComponents.get(MINUTE), dateComponents.get(SECOND));
    }

    public Date replaceMinutes(int minutes) {
        return new Date(dateComponents.get(YEAR), dateComponents.get(MONTH), dateComponents.get(DAY), dateComponents.get(HOUR), minutes, dateComponents.get(SECOND));
    }

    public Date replaceSeconds(int seconds) {
        return new Date(dateComponents.get(YEAR), dateComponents.get(MONTH), dateComponents.get(DAY), dateComponents.get(HOUR), dateComponents.get(MINUTE), seconds);
    }

    public java.util.Date toJavaDate() {
        return new java.util.Date(getMilliseconds());
    }

    public List<Decade> getDecadesInCentury() {
        return getCentury().getDecades();
    }

    public List<Integer> getYearsInDecade() {
        return getDecade().getYears();
    }

    public int getDaysInMonth() {
        return getDaysInMonth(dateComponents.get(MONTH));
    }

    private int getDaysInMonth(int month) {
        if (month == 2) return isLeapYear(getYear()) ? 29 : 28;
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) return 31;
        return 30;
    }

    public Date nextCentury() {
        return new Date(dateComponents.get(YEAR) + 100, dateComponents.get(MONTH), dateComponents.get(DAY), dateComponents.get(HOUR), dateComponents.get(MINUTE), dateComponents.get(SECOND));
    }

    public Date previousCentury() {
        return new Date(dateComponents.get(YEAR) - 100, dateComponents.get(MONTH), dateComponents.get(DAY), dateComponents.get(HOUR), dateComponents.get(MINUTE), dateComponents.get(SECOND));
    }

    public Date nextDecade() {
        if (isLeapYear(getYear()) && getMonth() == 2 && getDay() == 29)
            return new Date(dateComponents.get(YEAR) + 10, 3, 1, dateComponents.get(HOUR), dateComponents.get(MINUTE), dateComponents.get(SECOND));
        return new Date(dateComponents.get(YEAR) + 10, dateComponents.get(MONTH), dateComponents.get(DAY), dateComponents.get(HOUR), dateComponents.get(MINUTE), dateComponents.get(SECOND));
    }

    public Date previousDecade() {
        if (isLeapYear(getYear()) && getMonth() == 2 && getDay() == 29)
            return new Date(dateComponents.get(YEAR) - 10, dateComponents.get(MONTH), 28, dateComponents.get(HOUR), dateComponents.get(MINUTE), dateComponents.get(SECOND));
        return new Date(dateComponents.get(YEAR) - 10, dateComponents.get(MONTH), dateComponents.get(DAY), dateComponents.get(HOUR), dateComponents.get(MINUTE), dateComponents.get(SECOND));
    }

    public Date nextYear() {
        if (isLeapYear(getYear()) && getMonth() == 2 && getDay() == 29)
            return new Date(dateComponents.get(YEAR) + 1, dateComponents.get(MONTH) + 1, 1, dateComponents.get(HOUR), dateComponents.get(MINUTE), dateComponents.get(SECOND));
        return new Date(dateComponents.get(YEAR) + 1, dateComponents.get(MONTH), dateComponents.get(DAY), dateComponents.get(HOUR), dateComponents.get(MINUTE), dateComponents.get(SECOND));
    }

    public Date previousYear() {
        if (isLeapYear(getYear()) && getMonth() == 2 && getDay() == 29)
            return new Date(dateComponents.get(YEAR) - 1, dateComponents.get(MONTH), 28, dateComponents.get(HOUR), dateComponents.get(MINUTE), dateComponents.get(SECOND));
        return new Date(dateComponents.get(YEAR) - 1, dateComponents.get(MONTH), dateComponents.get(DAY), dateComponents.get(HOUR), dateComponents.get(MINUTE), dateComponents.get(SECOND));
    }

    public Date nextMonth() {
        if (getMonth() == 12)
            return new Date(dateComponents.get(YEAR) + 1, 1, dateComponents.get(DAY), dateComponents.get(HOUR), dateComponents.get(MINUTE), dateComponents.get(SECOND));
        if (getDay() > getDaysInMonth(getMonth() + 1))
            return new Date(dateComponents.get(YEAR), dateComponents.get(MONTH) + 1, getDaysInMonth(dateComponents.get(MONTH) + 1), dateComponents.get(HOUR), dateComponents.get(MINUTE), dateComponents.get(SECOND));
        return new Date(dateComponents.get(YEAR), dateComponents.get(MONTH) + 1, dateComponents.get(DAY), dateComponents.get(HOUR), dateComponents.get(MINUTE), dateComponents.get(SECOND));
    }

    public Date previousMonth() {
        if (getMonth() == 1)
            return new Date(dateComponents.get(YEAR) - 1, 12, dateComponents.get(DAY), dateComponents.get(HOUR), dateComponents.get(MINUTE), dateComponents.get(SECOND));
        if (getDay() > getDaysInMonth(getMonth() - 1))
            return new Date(dateComponents.get(YEAR), dateComponents.get(MONTH) - 1, getDaysInMonth(dateComponents.get(MONTH) - 1), dateComponents.get(HOUR), dateComponents.get(MINUTE), dateComponents.get(SECOND));
        return new Date(dateComponents.get(YEAR), dateComponents.get(MONTH) - 1, dateComponents.get(DAY), dateComponents.get(HOUR), dateComponents.get(MINUTE), dateComponents.get(SECOND));
    }

    public Date nextDay() {
        if (getMonth() == 12 && getDay() == 31)
            return new Date(dateComponents.get(YEAR) + 1, 1, 1, dateComponents.get(HOUR), dateComponents.get(MINUTE), dateComponents.get(SECOND));
        if ((getDay() + 1) > getDaysInMonth(getMonth()))
            return new Date(dateComponents.get(YEAR), dateComponents.get(MONTH) + 1, 1, dateComponents.get(HOUR), dateComponents.get(MINUTE), dateComponents.get(SECOND));
        return new Date(dateComponents.get(YEAR), dateComponents.get(MONTH), dateComponents.get(DAY) + 1, dateComponents.get(HOUR), dateComponents.get(MINUTE), dateComponents.get(SECOND));
    }

    public Date previousDay() {
        if (getMonth() == 1 && getDay() == 1)
            return new Date(dateComponents.get(YEAR) - 1, 12, 31, dateComponents.get(HOUR), dateComponents.get(MINUTE), dateComponents.get(SECOND));
        if (getDay() == 1)
            return new Date(dateComponents.get(YEAR), dateComponents.get(MONTH) - 1, getDaysInMonth(dateComponents.get(MONTH) - 1), dateComponents.get(HOUR), dateComponents.get(MINUTE), dateComponents.get(SECOND));
        return new Date(dateComponents.get(YEAR), dateComponents.get(MONTH), dateComponents.get(DAY) - 1, dateComponents.get(HOUR), dateComponents.get(MINUTE), dateComponents.get(SECOND));
    }

    public Date atFirstDayOfMonth() {
        return new Date(dateComponents.get(YEAR), dateComponents.get(MONTH), 1, dateComponents.get(HOUR), dateComponents.get(MINUTE), dateComponents.get(SECOND));
    }

    public static boolean isLeapYear(int year) {
        return ((year % 4 == 0) && (year % 100 != 0)) || ((year % 100 == 0) && (year % 400 == 0));
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || !(object instanceof Date)) return false;
        Date date = (Date) object;
        for (DateComponent component : dateComponents.keySet())
            if (!componentsAreEquals(dateComponents.get(component), date.dateComponents.get(component))) return false;
        return true;
    }

    private boolean componentsAreEquals(Integer component1, Integer component2) {
        if (component1 == null && component2 == null) return true;
        if (component1 == null || component2 == null) return false;
        return component1.equals(component2);
    }

    public DayOfWeek getDayOfWeek() {
        return DayOfWeek.getDayOfWeek(dateComponents.get(YEAR), dateComponents.get(MONTH), dateComponents.get(DAY));
    }

    public Long getMilliseconds() {
        Long numberOfDays = (long) (getDay() - 1);
        int months[] = new int[]{31, 28 + (isLeapYear(getYear()) ? 1 : 0), 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        for (int i = 0; i < getMonth() - 1; i++)
            numberOfDays += months[i];
        numberOfDays += ((getYear() - 1970 - leapYears(getYear())) * 365) + leapYears(getYear()) * 366;
        return numberOfDays * MILLIS_PER_DAY + timeInMillis();
    }

    private long timeInMillis() {
        long hoursInMillis = (getHours() == -1 ? 0 : getHours()) * MILLIS_PER_HOUR;
        long minutesInMillis = (getMinutes() == -1 ? 0 : getMinutes()) * MILLIS_PER_MINUTE;
        long secondsInMillis = (getSeconds() == -1 ? 0 : getSeconds()) * MILLIS_PER_SECOND;
        return hoursInMillis + minutesInMillis + secondsInMillis;
    }

    private static int year(java.util.Date date) {
        String dateArray[] = date.toString().split(" ");
        return Integer.valueOf(dateArray[dateArray.length - 1]);
    }

    private static int month(java.util.Date date) {
        String dateArray[] = date.toString().split(" ");
        if (dateArray[1].equals("Jan")) return 1;
        if (dateArray[1].equals("Feb")) return 2;
        if (dateArray[1].equals("Mar")) return 3;
        if (dateArray[1].equals("Apr")) return 4;
        if (dateArray[1].equals("May")) return 5;
        if (dateArray[1].equals("Jun")) return 6;
        if (dateArray[1].equals("Jul")) return 7;
        if (dateArray[1].equals("Aug")) return 8;
        if (dateArray[1].equals("Sep")) return 9;
        if (dateArray[1].equals("Oct")) return 10;
        if (dateArray[1].equals("Nov")) return 11;
        return 12;
    }

    private static int day(java.util.Date date) {
        String dateArray[] = date.toString().split(" ");
        return Integer.valueOf(dateArray[2]);
    }

    private static int hour(java.util.Date date) {
        String dateArray[] = date.toString().split(" ");
        return Integer.valueOf(dateArray[3].split(":")[0]);
    }

    private static int minute(java.util.Date date) {
        String dateArray[] = date.toString().split(" ");
        return Integer.valueOf(dateArray[3].split(":")[1]);
    }

    private static int second(java.util.Date date) {
        String dateArray[] = date.toString().split(" ");
        return Integer.valueOf(dateArray[3].split(":")[2]);
    }
    
    private int leapYears(int year) {
        int leapYears = 0;
        for (int i = 1970; i < year; i++)
            if (isLeapYear(i))
                leapYears++;
        return leapYears;
    }

    public Date removeNumberOfComponents(int componentsToRemove, DateComponent minComponent) {
        if (numberOfSetComponents() <= componentsToRemove) return this;
        List<DateComponent> components = DateComponent.valuesGreaterThan(minComponent);
        while (componentsToRemove != 0 && !components.isEmpty()) {
            dateComponents.put(components.remove(0), null);
            componentsToRemove--;
        }
        return this;
    }

    private int numberOfSetComponents() {
        int result = 0;
        for (Integer component : dateComponents.values())
            result += component == null ? 0 : 1;
        return result;
    }
}