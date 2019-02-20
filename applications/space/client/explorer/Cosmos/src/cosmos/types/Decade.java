package cosmos.types;

import java.util.ArrayList;
import java.util.List;

public class Decade {

    private final int firstYearOfDecade;

    public Decade(int year) {
        this.firstYearOfDecade = year - (year % 10);
    }

    public List<Integer> getYears() {
        List<Integer> years = new ArrayList<>();
        for (int year = firstYearOfDecade; year < firstYearOfDecade + 10; year++)
            years.add(year);
        return years;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || !(object instanceof Decade)) return false;
        return firstYearOfDecade == ((Decade) object).firstYearOfDecade;
    }

    @Override
    public String toString() {
        return firstYearOfDecade + " - " + (firstYearOfDecade + 9);
    }
}
