package client.presenters.displays.entity.field;

import static client.core.model.definition.entity.field.NumberFieldDefinition.RangeDefinition;

public interface IsNumberFieldDisplay extends IsFieldDisplay<client.core.model.types.Number> {

    <T extends java.lang.Number> client.core.model.types.Number createNumber(T value);

    String getValueAsString();

    String getFormat();
    String getFormatNumber();
    String getUnit();
    boolean unitIsOnLeft();
    boolean hasFormat();

    client.core.model.types.Number increment();
    client.core.model.types.Number decrement();
    void setFormattedValue(String formattedValue);

    RangeDefinition getRange();
    RangeDefinition getDefaultRange();
    double getMinStep();

    boolean isDouble();
    boolean hasRange();

	void addHook(NumberFieldDisplay.Hook hook);

}
