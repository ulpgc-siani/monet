package cosmos.utils.date;

import cosmos.services.translators.DateTranslator;
import cosmos.types.Date;
import cosmos.types.DatePrecision;

import java.util.*;

import static cosmos.utils.StringUtils.isLong;
import static cosmos.utils.StringUtils.isNumber;

public class DateParser {

    private final DateTranslator translator;
    private final DateBuilder builder;
    private final DatePrecision precision;
    private final List<String> separators;
    private boolean monthUsed;

    public DateParser(DateTranslator translator, DateBuilder builder, DatePrecision precision) {
        this.translator = translator;
        this.builder = builder;
        this.precision = precision;
        this.separators = separatorList(translator);
        orderSeparatorsBySize();
    }

    public Date parse(String text) {
        if (text == null || text.isEmpty())
            return null;
        monthUsed = false;
        List<Integer> components = getComponents(text);

        return builder.build(components.isEmpty() ? listFromText(text) : components);
    }

    public Date parse(String text, Date date) {
        monthUsed = false;
        if (date == null) return parse(text);
        List<Integer> components = getComponents(text);
        if (precision.numberOfComponents() > components.size())
            return partialDate(text, date, components);
        return builder.build(components.isEmpty() ? componentFromText(text) : components);
    }

    private Date partialDate(String text, Date date, List<Integer> components) {
        if (components.isEmpty())
            return builder.build(componentFromText(text), date.removeNumberOfComponents(1, precision.getMinComponent()));
        return builder.build(components, date.removeNumberOfComponents(components.size(), precision.getMinComponent()));
    }

    private List<Integer> componentFromText(String text) {
        if (isNumber(text))
            return listFromText(text);
        return listFromInteger(translator.monthToNumber(text));
    }

    private List<Integer> getComponents(String text) {
        if (isLong(text)) return listFromText(text);
        List<Integer> components = new ArrayList<>();
        for (String separator : separators) {
            if (!text.contains(separator)) continue;
            components.addAll(parse(text, separator));
            break;
        }
        return components;
    }

    private List<Integer> parse(String text, String separator) {
        List<Integer> components = new ArrayList<>();
        for (String component : text.split(separator))
            components.addAll(parseComponent(component.trim()));
        return components;
    }

    private List<Integer> parseComponent(String component) {
        if (componentIsSeparator(component)) return new ArrayList<>();
        if (componentHasSeparator(component))
            return parseComponentWithSeparator(component);
        if (isNumber(component))
            return listFromText(component);
        if (monthUsed || translator.monthToNumber(component) == 0) throw new MalformedDateException();
        monthUsed = true;
        return listFromInteger(translator.monthToNumber(component));
    }

    private boolean componentIsSeparator(String component) {
        for (String separator : separators)
            if (component.toLowerCase().equals(separator)) return true;
        return false;
    }

    private boolean componentHasSeparator(String component) {
        if (!componentHasNonAlphabeticSeparators(component)) return false;
        for (String separator : separators)
            if (component.contains(separator)) return true;
        return false;
    }

    private boolean componentHasNonAlphabeticSeparators(String component) {
        for (String separator : nonAlphabeticSeparators())
            if (component.contains(separator)) return true;
        return false;
    }

    private List<String> nonAlphabeticSeparators() {
        final List<String> separators = new ArrayList<>();
        for (String separator : this.separators)
            if (!separatorIsAlphabetic(separator)) separators.add(separator);

        return separators;
    }

    private boolean separatorIsAlphabetic(String separator) {
        String alphabet = "abcdefghijklmnñopqrstuvwxyz";
        for (char character : separator.toLowerCase().toCharArray())
            if (!(alphabet.indexOf(character) > -1)) return false;
        return true;
    }

    private List<Integer> parseComponentWithSeparator(String component) {
        return parse(component, getSeparator(component));
    }

    private String getSeparator(String component) {
        for (String separator : separators)
            if (component.contains(separator)) return separator;
        return "";
    }

    private List<Integer> listFromText(String text) {
        final List<Integer> list = new ArrayList<>();
        list.add(Integer.valueOf(text));
        return list;
    }

    private List<Integer> listFromInteger(Integer integer) {
        final List<Integer> list = new ArrayList<>();
        list.add(integer);
        return list;
    }

    private List<String> separatorList(DateTranslator translator) {
        final List<String> separators = new ArrayList<>();
        Collections.addAll(separators, translator.getDateSeparators());
        return separators;
    }

    private void orderSeparatorsBySize() {
        Collections.sort(separators, new Comparator<String>() {
            @Override
            public int compare(String string1, String string2) {
                return string2.length() - string1.length();
            }
        });
    }

}
