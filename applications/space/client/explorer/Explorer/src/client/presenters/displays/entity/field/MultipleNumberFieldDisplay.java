package client.presenters.displays.entity.field;

import client.core.model.Node;
import client.core.model.definition.entity.field.NumberFieldDefinition;
import client.core.model.fields.MultipleNumberField;
import client.core.model.fields.NumberField;
import client.core.model.types.Number;
import client.presenters.displays.entity.FieldDisplay;
import client.presenters.displays.entity.MultipleFieldDisplay;
import cosmos.utils.StringUtils;

import static cosmos.utils.StringUtils.isLong;

public class MultipleNumberFieldDisplay extends MultipleFieldDisplay<MultipleNumberField, NumberFieldDefinition, client.core.model.types.Number> implements IsMultipleNumberFieldDisplay {

    public static final Type TYPE = new Type("MultipleNumberFieldDisplay", MultipleFieldDisplay.TYPE);
    private static final String formatChars = "[\\.,#0 ]";

    public MultipleNumberFieldDisplay(Node node, MultipleNumberField field) {
        super(node, field);
    }

    @Override
    public String getValueAsString() {
        return "";
    }

    @Override
    public Type getType() {
        return TYPE;
    }

    @Override
    public <T extends java.lang.Number> Number createNumber(T value) {
        return getTypeFactory().createNumber(value);
    }

    @Override
    public String getFormat() {
        return getDefinition().getFormat();
    }

    @Override
    public String getFormatNumber() {
        return getFormat().replaceAll(getUnit(), "").trim();
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
        return getDefinition().getEdition().toString();
    }

    @Override
    protected boolean check(client.core.model.types.Number value) {
        return value == null || isValid(value);
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
    public Number increment() {
        return null;
    }

    @Override
    public Number increment(String value) {
        Number number = valueWithoutUnit(value);
        number.increment(getMinStep());
        if (!isValid(number))
            number.decrement(getMinStep());
        return number;
    }

    @Override
    public Number decrement() {
        return null;
    }

    @Override
    public Number decrement(String value) {
        Number number = valueWithoutUnit(value);
        number.decrement(getMinStep());
        if (!isValid(number))
            number.decrement(getMinStep());
        return number;
    }

    @Override
    public boolean isValidNumber(String number) {
        if (isDouble())
            return StringUtils.isDouble(number);
        return isLong(number);
    }

    @Override
    public void add(Number value, String formatted) {
        final NumberField field = getEntity().createField(value);
        field.setFormattedValue(formatted);
        addField(field);
    }

    @Override
    public Number valueWithoutUnit(String value) {
        return valueOfString(value.replace(getUnit(), "").trim());
    }

    @Override
    public void setFormattedValue(String formattedValue) {
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
    public boolean isDouble() {
        return getFormat().contains(".") || getFormat().contains(",");
    }

    @Override
    public boolean hasRange() {
        return getDefinition().getRange() != null;
    }

    @Override
    public void addHook(FieldDisplay.Hook hook) {
        super.addHook(hook);
    }

    @Override
    public void setValue(Number number) {
    }

    private boolean isInsideRange(Number number) {
        if (getDefinition().getRange() == null) return true;
        if (isDouble())
            return getRange().getMin() <= number.getValue().doubleValue()  && number.getValue().doubleValue() <= getRange().getMax();
        return getRange().getMin() <= number.getValue().longValue()  && number.getValue().longValue() <= getRange().getMax();
    }

    private int numberOfZeros() {
        String separator = getFormat().contains(".") ? "." : ",";
        String format = getFormat().replaceAll("[^\\.,#0]", "");
        return format.length() - format.indexOf(separator) - 2;
    }

    @Override
    public Number valueOfString(String number) {
        number = number == null || number.isEmpty() ? String.valueOf(getRange().getMin()) : number;
        if (isDouble())
            return getTypeFactory().createNumber(Double.valueOf(number));
        return getTypeFactory().createNumber(Long.valueOf(number));
    }
}
