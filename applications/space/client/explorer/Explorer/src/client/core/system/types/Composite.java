package client.core.system.types;

import client.core.model.Field;
import client.core.system.MonetList;

public class Composite extends MonetList<Field> implements client.core.model.types.Composite {

    public Composite() {
    }

    public Composite(Field[] elements) {
        addAll(elements);
    }

    @Override
    public final boolean valuesEmpty() {
        for (Field field : this)
            if (!field.isNullOrEmpty()) return false;
        return true;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Composite)) return false;
        if (((Composite) object).size() != size()) return false;
        for (int i = 0; i < size(); i++)
            if (!get(i).equals(((Composite) object).get(i))) return false;
        return true;
    }
}
