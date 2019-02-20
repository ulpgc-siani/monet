package cosmos.utils.date;

import cosmos.types.Date;
import cosmos.types.DateComponent;
import cosmos.utils.StringUtils;

import java.util.*;

public abstract class DateFormatter {

    public interface Format {
        String YEARS = "yyyy";
        String MONTHS = "MM/yyyy";
        String DAYS = "dd/MM/yyyy";
        String HOURS = "dd/MM/yyyy HH";
        String MINUTES = "dd/MM/yyyy HH:mm";
        String SECONDS = "dd/MM/yyyy HH:mm:ss";
    }

    public static class Component {
        public static final Character YEAR = 'y';
        public static final Character MONTH = 'M';
        public static final Character DAY = 'd';
        public static final Character HOUR = 'H';
        public static final Character MINUTE = 'm';
        public static final Character SECOND = 's';
        public static final String SYMBOLS = "" + YEAR + MONTH + DAY + HOUR + MINUTE + SECOND;

        public static boolean isComponentSymbol(Character component) {
            return SYMBOLS.indexOf(component) >= 0;
        }
    }

    private static Map<Character, String> formats;

    public static String format(Date date, String format) {
        formats = createFormat(getSymbolsInFormat(format));
        String formattedDate = replaceFormattingCharsWithValues(date, format);
        return formattedDate.substring(findFirstNumberPosition(formattedDate), findLastNumberPosition(formattedDate));
    }

    private static String replaceFormattingCharsWithValues(Date date, String formattedDate) {
        formattedDate = date.isSet(DateComponent.YEAR) ? format(date.getYear(), Component.YEAR, formattedDate) : formattedDate;
        formattedDate = date.isSet(DateComponent.MONTH) ? format(date.getMonth(), Component.MONTH, formattedDate) : formattedDate;
        formattedDate = date.isSet(DateComponent.DAY) ? format(date.getDay(), Component.DAY, formattedDate) : formattedDate;
        formattedDate = date.isSet(DateComponent.HOUR) ? format(date.getHours(), Component.HOUR, formattedDate) : formattedDate;
        formattedDate = date.isSet(DateComponent.MINUTE) ? format(date.getMinutes(), Component.MINUTE, formattedDate) : formattedDate;
        formattedDate = date.isSet(DateComponent.SECOND) ? format(date.getSeconds(), Component.SECOND, formattedDate) : formattedDate;
        return formattedDate;
    }

    private static String format(int value, Character component, String format) {
        if (!formats.containsKey(component)) return format;
        return format.replace(formats.get(component), formatNumber(value, formats.get(component).length()));
    }

    private static int findFirstNumberPosition(String date) {
        for (int i = 0; i < date.length(); i++)
            if (StringUtils.isNumber(String.valueOf(date.charAt(i)))) return i;
        return date.length();
    }

    private static int findLastNumberPosition(String date) {
        for (int i = date.length() - 1; i >= 0; i--) {
            if (StringUtils.isNumber(String.valueOf(date.charAt(i)))) return i + 1;
        }
        return 0;
    }

    private static List<String> getSymbolsInFormat(String format) {
        List<String> symbols = new ArrayList<>();
        String component = "";
        for (char symbol : (format + " ").toCharArray()) {
            if (!component.isEmpty() && currentSymbolEnds(component, symbol)) {
                symbols.add(component);
                component = "";
            } else if (component.isEmpty() || component.indexOf(symbol) >= 0)
                component += symbol;
        }
        return symbols;
    }

    private static Map<Character, String> createFormat(List<String> formats) {
        Map<Character, String> formatsMap = new LinkedHashMap<>();
        for (String format : formats)
            formatsMap.put(format.charAt(0), format);
        return formatsMap;
    }

    private static boolean currentSymbolEnds(String symbols, char symbol) {
        return symbols.indexOf(symbol) < 0 || !Component.isComponentSymbol(symbol);
    }

    private static String formatNumber(int number, int length) {
        String formattedNumber = "";
        String numberString = (number < 10 ? "0" : "") + String.valueOf(number);
        if (length >= numberString.length()) return numberString;
        int index = 1;
        while (index <= length)
            formattedNumber = numberString.charAt(numberString.length() - index++) + formattedNumber;
        return formattedNumber;
    }
}
