package client.presenters.displays.entity.field;

import client.core.model.Node;
import client.core.model.definition.entity.field.DateFieldDefinition;
import client.core.model.definition.entity.field.DateFieldDefinition.Precision;
import client.core.model.fields.DateField;
import client.presenters.displays.entity.FieldDisplay;
import cosmos.types.Date;
import cosmos.types.DatePrecision;
import cosmos.types.DateRange;
import cosmos.utils.date.DateBuilder;
import cosmos.utils.date.DateParser;
import cosmos.utils.date.SpanishDateBuilder;

import static client.core.model.definition.entity.field.DateFieldDefinition.RangeDefinition;

public class DateFieldDisplay extends FieldDisplay<DateField, DateFieldDefinition,Date> implements IsDateFieldDisplay {

    private DateRange range;

	public static final Type TYPE = new Type("DateFieldDisplay", FieldDisplay.TYPE);

    public DateFieldDisplay(Node node, DateField field) {
		super(node, field);
	}

    @Override
    protected void onInjectServices() {
        super.onInjectServices();
        range = new DateRange(getRangeMin(), getRangeMax(), getPrecision());
        getEntity().setValue(new DateParser(services.getTranslatorService(), getDateBuilder(), getPrecision()).parse(getEntity().getValueAsString()));
    }

    @Override
	public Type getType() {
		return TYPE;
	}

    @Override
    protected void refreshValue(Object date) {
        getEntity().setValue(new DateParser(services.getTranslatorService(), getDateBuilder(), getPrecision()).parse((String) date));
        refresh();
    }

    @Override
    public Date createDate() {
        return getPrecision().getDateWithPrecision();
    }

    @Override
    public String getValueAsString() {
        return getValue() == null ? "" : getEntity().getValueAsString();
    }

    @Override
    public DateBuilder getDateBuilder() {
        return new SpanishDateBuilder();
    }

    @Override
    public boolean hasTime() {
        return getPrecision() == DatePrecision.HOUR || getPrecision() == DatePrecision.MINUTE || getPrecision() == DatePrecision.SECOND;
    }

    @Override
    public boolean allowLessPrecision() {
        return getDefinition().allowLessPrecision();
    }

    @Override
	public boolean rangeWrong() {
		return !dateIsBetweenRange(getValue());
	}

    @Override
    public RangeDefinition getRange() {
        return getDefinition().getRange();
    }

    @Override
	protected boolean check(Date value) {
        return value == null || dateIsBetweenRange(value);
    }

    @Override
    protected Date format(Date value) {
        return value;
    }

    @Override
    public DatePrecision getPrecision() {
        final Precision precision = getDefinition().getPrecision();
        if (precision == Precision.YEARS) return DatePrecision.YEAR;
        if (precision == Precision.MONTHS) return DatePrecision.MONTH;
        if (precision == Precision.DAYS) return DatePrecision.DAY;
        if (precision == Precision.HOURS) return DatePrecision.HOUR;
        if (precision == Precision.MINUTES) return DatePrecision.MINUTE;
        return DatePrecision.SECOND;
    }

    @Override
    public boolean dateIsValid(Date date) {
        if (getDefinition().allowLessPrecision())
            return getPrecision().dateIsValidAllowingLessPrecision(date);
        return getPrecision().dateIsValid(date);
    }

    @Override
    public boolean isNearDate() {
        return getDefinition().getPurpose() == DateFieldDefinition.Purpose.NEAR_DATE;
    }

    protected boolean dateIsBetweenRange(Date date) {
        return range.dateBetween(date);
    }

    private Date getRangeMin() {
        if (getDefinition().getRange() == null || getDefinition().getRange().getMin() == -1) return null;
        return new Date(getDefinition().getRange().getMin());
    }

    private Date getRangeMax() {
        if (getDefinition().getRange() == null || getDefinition().getRange().getMax() == -1) return null;
        return new Date(getDefinition().getRange().getMax());
    }
}
