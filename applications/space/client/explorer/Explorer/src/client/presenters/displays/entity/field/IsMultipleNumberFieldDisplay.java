package client.presenters.displays.entity.field;

import client.core.model.types.Number;
import client.presenters.displays.IsMultipleDisplay;

public interface IsMultipleNumberFieldDisplay extends IsNumberFieldDisplay, IsMultipleDisplay<Number> {

    boolean isValidNumber(String number);
    void add(Number value, String formatted);
    Number increment(String value);
    Number decrement(String value);
    Number valueWithoutUnit(String value);
    Number valueOfString(String number);
}
