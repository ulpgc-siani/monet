package cosmos.types;

public abstract class DatePrecision {

    public static final DatePrecision CENTURY = new DatePrecision() {
        @Override
        public boolean dateBetweenDates(Date date, Date minDate, Date maxDate) {
            if (minDate == null && maxDate == null) return true;
            if (minDate == null) return date.getYear() <= maxDate.getYear();
            if (maxDate == null) return minDate.getYear()<= date.getYear();
            return minDate.getYear() <= date.getYear() && date.getYear() <= maxDate.getYear();
        }

        @Override
        public boolean dateIsValid(Date date) {
            return date.isSet(DateComponent.YEAR);
        }

        @Override
        public boolean dateIsValidAllowingLessPrecision(Date date) {
            return dateIsValid(date);
        }

        @Override
        public int numberOfComponents() {
            return 1;
        }

        @Override
        public Date getDateWithPrecision() {
            return new Date(new Date().getYear());
        }

        @Override
        public DateComponent getMinComponent() {
            return DateComponent.YEAR;
        }
    };

    public static final DatePrecision DECADE = new DatePrecision() {
        @Override
        public boolean dateBetweenDates(Date date, Date minDate, Date maxDate) {
            if (minDate == null && maxDate == null) return true;
            if (minDate == null) return date.getYear() <= maxDate.getYear();
            if (maxDate == null) return minDate.getYear()<= date.getYear();
            return minDate.getYear() <= date.getYear() && date.getYear() <= maxDate.getYear();
        }

        @Override
        public boolean dateIsValid(Date date) {
            return CENTURY.dateIsValid(date);
        }

        @Override
        public boolean dateIsValidAllowingLessPrecision(Date date) {
            return dateIsValid(date);
        }

        @Override
        public int numberOfComponents() {
            return 1;
        }

        @Override
        public Date getDateWithPrecision() {
            final Date date = new Date();
            return new Date(date.getYear());
        }

        @Override
        public DateComponent getMinComponent() {
            return DateComponent.YEAR;
        }
    };

    public static final DatePrecision YEAR = new DatePrecision() {
        @Override
        public boolean dateBetweenDates(Date date, Date minDate, Date maxDate) {
            if (minDate == null && maxDate == null) return true;
            if (minDate == null) return date.getYear() <= maxDate.getYear();
            if (maxDate == null) return minDate.getYear()<= date.getYear();
            return minDate.getYear() <= date.getYear() && date.getYear() <= maxDate.getYear();
        }

        @Override
        public boolean dateIsValid(Date date) {
            return DECADE.dateIsValid(date);
        }

        @Override
        public boolean dateIsValidAllowingLessPrecision(Date date) {
            return dateIsValid(date);
        }

        @Override
        public int numberOfComponents() {
            return 1;
        }

        @Override
        public Date getDateWithPrecision() {
            final Date date = new Date();
            return new Date(date.getYear());
        }

        @Override
        public DateComponent getMinComponent() {
            return DateComponent.YEAR;
        }
    };

    public static final DatePrecision MONTH = new DatePrecision() {
        @Override
        public boolean dateBetweenDates(Date date, Date minDate, Date maxDate) {
            if (minDate == null && maxDate == null) return true;
            if (minDate != null && date.getYear() == minDate.getYear() && (date.getMonth() < minDate.getMonth())) return false;
            if (maxDate != null && date.getYear() == maxDate.getYear() && (date.getMonth() > maxDate.getMonth())) return false;
            return YEAR.dateBetweenDates(date, minDate, maxDate);
        }

        @Override
        public boolean dateIsValid(Date date) {
            return YEAR.dateIsValid(date) && date.isSet(DateComponent.MONTH);
        }

        @Override
        public boolean dateIsValidAllowingLessPrecision(Date date) {
            return dateIsValid(date) || YEAR.dateIsValid(date);
        }

        @Override
        public int numberOfComponents() {
            return 2;
        }

        @Override
        public Date getDateWithPrecision() {
            final Date date = new Date();
            return new Date(date.getYear(), date.getMonth());
        }

        @Override
        public DateComponent getMinComponent() {
            return DateComponent.MONTH;
        }
    };

    public static final DatePrecision DAY = new DatePrecision() {
        @Override
        public boolean dateBetweenDates(Date date, Date minDate, Date maxDate) {
            if (minDate == null && maxDate == null) return true;
            if (minDate != null && equalsWithMonthPrecision(date, minDate) && (date.getDay() < minDate.getDay())) return false;
            if (maxDate != null && equalsWithMonthPrecision(date, maxDate) && (date.getDay() > maxDate.getDay())) return false;
            return MONTH.dateBetweenDates(date, minDate, maxDate);
        }

        @Override
        public boolean dateIsValid(Date date) {
            return MONTH.dateIsValid(date) && date.isSet(DateComponent.DAY);
        }

        @Override
        public boolean dateIsValidAllowingLessPrecision(Date date) {
            return dateIsValid(date) || MONTH.dateIsValidAllowingLessPrecision(date);
        }

        @Override
        public int numberOfComponents() {
            return 3;
        }

        @Override
        public Date getDateWithPrecision() {
            final Date date = new Date();
            return new Date(date.getYear(), date.getMonth(), date.getDay());
        }

        @Override
        public DateComponent getMinComponent() {
            return DateComponent.DAY;
        }
    };

    public static final DatePrecision HOUR = new DatePrecision() {
        @Override
        public boolean dateBetweenDates(Date date, Date minDate, Date maxDate) {
            if (minDate == null && maxDate == null) return true;
            if (minDate != null && equalsWithDayPrecision(date, minDate) && (date.getHours() < minDate.getHours())) return false;
            if (maxDate != null && equalsWithDayPrecision(date, maxDate) && (date.getHours() > maxDate.getHours())) return false;
            return DAY.dateBetweenDates(date, minDate, maxDate);
        }

        @Override
        public boolean dateIsValid(Date date) {
            return DAY.dateIsValid(date) && date.isSet(DateComponent.HOUR);
        }

        @Override
        public boolean dateIsValidAllowingLessPrecision(Date date) {
            return dateIsValid(date) || DAY.dateIsValidAllowingLessPrecision(date);
        }

        @Override
        public int numberOfComponents() {
            return 4;
        }

        @Override
        public Date getDateWithPrecision() {
            final Date date = new Date();
            return new Date(date.getYear(), date.getMonth(), date.getDay(), date.getHours());
        }

        @Override
        public DateComponent getMinComponent() {
            return DateComponent.HOUR;
        }
    };

    public static final DatePrecision MINUTE = new DatePrecision() {
        @Override
        public boolean dateBetweenDates(Date date, Date minDate, Date maxDate) {
            if (minDate == null && maxDate == null) return true;
            if (minDate != null && equalsWithHourPrecision(date, minDate) && (date.getMinutes() < minDate.getMinutes())) return false;
            if (maxDate != null && equalsWithHourPrecision(date, maxDate) && (date.getMinutes() > maxDate.getMinutes())) return false;
            return HOUR.dateBetweenDates(date, minDate, maxDate);
        }

        @Override
        public boolean dateIsValid(Date date) {
            return HOUR.dateIsValid(date) && date.isSet(DateComponent.MINUTE);
        }

        @Override
        public boolean dateIsValidAllowingLessPrecision(Date date) {
            return dateIsValid(date) || HOUR.dateIsValidAllowingLessPrecision(date);
        }

        @Override
        public int numberOfComponents() {
            return 5;
        }

        @Override
        public Date getDateWithPrecision() {
            final Date date = new Date();
            return new Date(date.getYear(), date.getMonth(), date.getDay(), date.getHours(), date.getMinutes());
        }

        @Override
        public DateComponent getMinComponent() {
            return DateComponent.MINUTE;
        }
    };

    public static final DatePrecision SECOND = new DatePrecision() {
        @Override
        public boolean dateBetweenDates(Date date, Date minDate, Date maxDate) {
            if (minDate == null && maxDate == null) return true;
            if (minDate != null && equalsWithMinutePrecision(date, minDate) && (date.getSeconds() < minDate.getSeconds())) return false;
            if (maxDate != null && equalsWithMinutePrecision(date, maxDate) && (date.getSeconds() > maxDate.getSeconds())) return false;
            return MINUTE.dateBetweenDates(date, minDate, maxDate);
        }

        @Override
        public boolean dateIsValid(Date date) {
            return MINUTE.dateIsValid(date) && date.isSet(DateComponent.SECOND);
        }

        @Override
        public boolean dateIsValidAllowingLessPrecision(Date date) {
            return dateIsValid(date) || MINUTE.dateIsValidAllowingLessPrecision(date);
        }

        @Override
        public int numberOfComponents() {
            return 6;
        }

        @Override
        public Date getDateWithPrecision() {
            final Date date = new Date();
            return new Date(date.getYear(), date.getMonth(), date.getDay(), date.getHours(), date.getMinutes(), date.getSeconds());
        }

        @Override
        public DateComponent getMinComponent() {
            return DateComponent.SECOND;
        }
    };

    public abstract boolean dateBetweenDates(Date date, Date minDate, Date maxDate);

    public abstract boolean dateIsValid(Date date);

    public abstract boolean dateIsValidAllowingLessPrecision(Date date);

    public abstract int numberOfComponents();

    public abstract Date getDateWithPrecision();

    public abstract DateComponent getMinComponent();

    private static boolean equalsWithYearPrecision(Date date, Date other) {
        return date.getYear() == other.getYear();
    }

    private static boolean equalsWithMonthPrecision(Date date, Date other) {
        return equalsWithYearPrecision(date, other) && date.getMonth() == other.getMonth();
    }

    private static boolean equalsWithDayPrecision(Date date, Date other) {
        return equalsWithMonthPrecision(date, other) && date.getDay() == other.getDay();
    }

    private static boolean equalsWithHourPrecision(Date date, Date other) {
        return equalsWithDayPrecision(date, other) && date.getHours() == other.getHours();
    }

    private static boolean equalsWithMinutePrecision(Date date, Date other) {
        return equalsWithHourPrecision(date, other) && date.getMinutes() == other.getMinutes();
    }
}
