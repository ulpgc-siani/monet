package client.core.model.types;

import client.core.model.Field;
import client.core.model.List;

public interface Composite extends List<Field> {
    boolean valuesEmpty();
}
