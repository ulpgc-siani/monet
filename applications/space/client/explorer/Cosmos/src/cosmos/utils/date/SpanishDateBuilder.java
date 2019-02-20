package cosmos.utils.date;

import cosmos.types.Date;

import java.util.List;

import static cosmos.types.DateComponent.*;

public class SpanishDateBuilder implements DateBuilder {

    @Override
    public Date build(List<Integer> components) {
        if (components.size() < 1 || components.size() > 6) return null;
        return dateWithValues(Date.empty(), components);
    }

    @Override
    public Date build(List<Integer> components, Date context) {
        if (components.size() == 1) return dateWithNewValue(context, components.get(0));
        return dateWithValues(context, components);
    }

    private Date dateWithNewValue(Date date, Integer value) {
        if (!date.isSet(YEAR)) return dateWithUpdatedYear(date, value);
        if (!date.isSet(MONTH)) return dateWithUpdatedMonth(date, value);
        if (!date.isSet(DAY)) return dateWithUpdatedDay(date, value);
        if (!date.isSet(HOUR)) return dateWithUpdatedHours(date, value);
        if (!date.isSet(MINUTE)) return dateWithUpdatedMinutes(date, value);
        if (!date.isSet(SECOND)) return dateWithUpdatedSeconds(date, value);
        return date;
    }

    private Date dateWithUpdatedYear(Date date, int year) {
        if (!yearIsValid(year))
            throw new MalformedDateException("Year value (" + year + ") is not valid");
        return date.replaceYear(year);
    }

    private Date dateWithUpdatedMonth(Date date, int month) {
        if (!monthIsValid(month))
            throw new MalformedDateException("Month value (" + month + ") is not valid");
        return date.replaceMonth(month);
    }

    private Date dateWithUpdatedDay(Date date, int day) {
        if (!dayIsValidInMonthAndYear(date.getYear(), date.getMonth(), day))
            throw new MalformedDateException("Day value (" + day + ") is not valid with month (" + date.getMonth() + ") and year (" + date.getYear() + ")");
        return date.replaceDay(day);
    }

    private Date dateWithUpdatedHours(Date date, int hours) {
        if (!hoursAreValid(hours))
            throw new MalformedDateException("Hours value (" + hours + ") is not valid");
        return date.replaceHours(hours);
    }

    private Date dateWithUpdatedMinutes(Date date, int minutes) {
        if (!minutesAreValid(minutes))
            throw new MalformedDateException("Minutes value (" + minutes + ") is not valid");
        return date.replaceMinutes(minutes);
    }

    private Date dateWithUpdatedSeconds(Date date, int seconds) {
        if (!secondsAreValid(seconds))
            throw new MalformedDateException("Seconds value (" + seconds + ") is not valid");
        return date.replaceSeconds(seconds);
    }

    private Date dateWithValues(Date date, List<Integer> values) {
        Date newDate = date;
        while (!values.isEmpty())
            newDate = addValue(values, newDate);
        return newDate;
    }

    private Date addValue(List<Integer> values, Date newDate) {
        if (!newDate.isSet(YEAR))
            return dateWithNewValue(newDate, values.remove(values.size() <= 2 ? values.size() - 1 : 2));
        if (!newDate.isSet(MONTH))
            return dateWithNewValue(newDate, values.remove(values.size() > 1 ? 1 : 0));
        if (!newDate.isSet(DAY))
            return dateWithNewValue(newDate, values.remove(0));
        if (!values.isEmpty()) newDate = setHour(values, newDate);
        if (!values.isEmpty()) newDate = dateWithUpdatedMinutes(newDate, values.remove(0));
        if (!values.isEmpty()) newDate = dateWithUpdatedSeconds(newDate, values.remove(0));
        return newDate;
    }

    private Date setHour(List<Integer> values, Date date) {
        Date newDate = dateWithUpdatedHours(date, values.remove(0));
        if (!values.isEmpty()) return newDate;
        return newDate.replaceMinutes(0).replaceSeconds(0);
    }

    private boolean yearIsValid(int year) {
        return year >= 0;
    }

    private boolean monthIsValid(int month) {
        return month >= 1 && month <= 12;
    }

    private boolean dayIsValidInMonthAndYear(int year, int month, int day) {
        if (day < 1) return false;
        if (month == 2) return dayIsValidForFebruary(year, day);
        if (month == 4 || month == 6 || month == 9 ||month == 11)
            return day <= 30;
        return day <= 31;
    }

    private boolean dayIsValidForFebruary(int year, int day) {
        if (Date.isLeapYear(year))
            return day <= 29;
        return day <= 28;
    }

    private boolean hoursAreValid(int hours) {
        return hours >= 0 && hours <= 23;
    }

    private boolean minutesAreValid(int minutes) {
        return minutes >= 0 && minutes <= 59;
    }

    private boolean secondsAreValid(int seconds) {
        return seconds >= 0 && seconds <= 59;
    }
}
