package cosmos.types;

import java.util.ArrayList;
import java.util.List;

public class Century {

    private final int firstYearOfCentury;

    public Century(int year) {
        this.firstYearOfCentury = year - (year % 100);
    }

    public List<Decade> getDecades() {
        List<Decade> decades = new ArrayList<>();
        for (int decade = firstYearOfCentury; decade < firstYearOfCentury + 100; decade += 10)
            decades.add(new Decade(decade));
        return decades;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || !(object instanceof Century)) return false;
        return firstYearOfCentury == ((Century) object).firstYearOfCentury;
    }

    @Override
    public String toString() {
        return firstYearOfCentury + " - " + (firstYearOfCentury + 99);
    }
}
