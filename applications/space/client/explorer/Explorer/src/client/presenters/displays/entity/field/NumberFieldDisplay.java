package client.presenters.displays.entity.field;

import client.core.model.Node;
import client.core.model.definition.entity.field.NumberFieldDefinition;
import client.core.model.definition.entity.field.NumberFieldDefinition.Edition;
import client.core.model.fields.NumberField;
import client.core.model.types.Number;
import client.presenters.displays.entity.FieldDisplay;

public class NumberFieldDisplay extends FieldDisplay<NumberField, NumberFieldDefinition, client.core.model.types.Number> implements IsNumberFieldDisplay {

    public static final Type TYPE = new Type("NumberFieldDisplay", FieldDisplay.TYPE);
    private static final String formatChars = "[\\.,#0 ]";

    public NumberFieldDisplay(Node node, NumberField field) {
        super(node, field);
    }

    @Override
    public Type getType() {
        return TYPE;
    }

    @Override
    public String getValueAsString() {
        return getEntity().getValueAsString();
    }

    @Override
    protected boolean check(client.core.model.types.Number value) {
        return isValid(value);
    }

    @Override
    public boolean isValid(Number number) {
        return number != null && isInsideRange(number);
    }

    @Override
    protected boolean shouldUpdateValue(Number oldValue, Number number) {
        return true;
    }

    @Override
    protected Number format(Number number) {
        return number;
    }

    @Override
    public <T extends java.lang.Number> Number createNumber(T value) {
        return getTypeFactory().createNumber(value);
    }

    private Number createInitialNumber() {
        final Number number = createNumber();
        setValue(number);
        return number;
    }

    private Number createNumber() {
        if (isDouble())
            return getTypeFactory().createNumber((double) getRange().getMin());
        return getTypeFactory().createNumber(getRange().getMin());
    }

    @Override
    public String getFormat() {
        return getDefinition().getFormat();
    }

    @Override
    public String getFormatNumber() {
        return getFormat().replace(getUnit(), "").trim();
    }

    @Override
    public String getUnit() {
        return getFormat().replaceAll(formatChars, "");
    }

    @Override
    public boolean unitIsOnLeft() {
        return getFormat().indexOf(getUnit()) == 0;
    }

    @Override
    public boolean hasFormat() {
        return !getFormat().isEmpty() && getFormat().equals(".");
    }

    @Override
    public String getEdition() {
        return getDefinition().getEdition() == null ? Edition.BUTTON.toString() : getDefinition().getEdition().toString();
    }

    @Override
    public NumberFieldDefinition.RangeDefinition getRange() {
        if (!hasRange())
            return new NumberFieldDefinition.RangeDefinition() {
                @Override
                public long getMin() {
                    return 0;
                }

                @Override
                public long getMax() {
                    return 0;
                }
            };
        return getDefinition().getRange();
    }

    @Override
    public NumberFieldDefinition.RangeDefinition getDefaultRange() {
        return new NumberFieldDefinition.RangeDefinition() {
            @Override
            public long getMin() {
                return -1000;
            }

            @Override
            public long getMax() {
                return 1000;
            }
        };
    }

    @Override
    public double getMinStep() {
        if (!isDouble()) return 1;
        String step = "0.";
        for (int i = 0; i < numberOfZeros(); i++)
            step += "0";
        return Double.valueOf(step + "1");
    }

    @Override
    public Number increment() {
        if (getValue() == null)
            return createInitialNumber();
        client.core.model.types.Number number = getValue();
        number.increment();
        if (!isValid(number))
            number.decrement();
        return number;
    }

    @Override
    public Number decrement() {
        if (getValue() == null)
            return createInitialNumber();
        client.core.model.types.Number number = getValue();
        number.decrement();
        if (!isValid(number))
            number.increment();
        return number;
    }

    @Override
    public void setFormattedValue(String formattedValue) {
        getEntity().setFormattedValue(formattedValue);
    }

    @Override
    public void setValue(Number number) {
        if (!check(number))
            super.setValue(getEntityFactory().createNumber(getRange().getMin()));
        else
            super.setValue(number);
    }

    private boolean isInsideRange(Number number) {
        if (getDefinition().getRange() == null) return true;
        if (isDouble())
            return getRange().getMin() <= number.getValue().doubleValue()  && number.getValue().doubleValue() <= getRange().getMax();
        return getRange().getMin() <= number.getValue().longValue()  && number.getValue().longValue() <= getRange().getMax();
    }

    @Override
    public boolean isDouble() {
        return getFormat().contains(".") || getFormat().contains(",");
    }

    @Override
    public boolean hasRange() {
        return getDefinition().getRange() != null;
    }

    private int numberOfZeros() {
        String separator = getFormat().contains(".") ? "." : ",";
        String format = getFormat().replaceAll("[^\\.,#0]", "");
        return format.length() - format.indexOf(separator) - 2;
    }

    @Override
    public void addHook(Hook hook) {
        super.addHook(hook);
    }
}
