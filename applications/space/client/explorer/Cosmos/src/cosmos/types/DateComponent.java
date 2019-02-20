package cosmos.types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum DateComponent {

    CENTURY, DECADE, YEAR, MONTH, DAY, HOUR, MINUTE, SECOND;

    public static List<DateComponent> valuesGreaterThan(DateComponent component) {
        final List<DateComponent> result = new ArrayList<>();
        for (DateComponent value : DateComponent.reverseValues())
            if (component.compareTo(value) >= 0)
                result.add(value);
        return result;
    }

    private static List<DateComponent> reverseValues() {
        List<DateComponent> components = new ArrayList<>();
        components.addAll(Arrays.asList(DateComponent.values()));
        Collections.reverse(components);
        return components;
    }
}
