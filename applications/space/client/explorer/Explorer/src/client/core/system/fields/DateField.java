package client.core.system.fields;

import client.core.model.definition.entity.field.DateFieldDefinition;
import cosmos.types.Date;
import cosmos.types.DatePrecision;
import cosmos.utils.date.DateFormatter;

import java.util.HashMap;
import java.util.Map;

public class DateField extends Field<DateFieldDefinition, Date> implements client.core.model.fields.DateField {
	private static final Map<DatePrecision, String> formats = new HashMap<DatePrecision, String>(){{
		put(DatePrecision.YEAR, DateFormatter.Format.YEARS);
		put(DatePrecision.MONTH, DateFormatter.Format.MONTHS);
		put(DatePrecision.DAY, DateFormatter.Format.DAYS);
		put(DatePrecision.HOUR, DateFormatter.Format.HOURS);
		put(DatePrecision.MINUTE, DateFormatter.Format.MINUTES);
		put(DatePrecision.SECOND, DateFormatter.Format.SECONDS);
	}};
	private Date value;
    private String formattedValue;

    public DateField() {
		super(Type.DATE);
	}

	public DateField(String code, String label) {
		super(code, label, Type.DATE);
	}

	@Override
	public final ClassName getClassName() {
		return client.core.model.fields.DateField.CLASS_NAME;
	}

	@Override
	public Date getValue() {
		return value;
	}

	@Override
	public String getValueAsString() {
        if (getValue() == null)
            return formattedValue;
		return DateFormatter.format(getValue(), formats.get(getPrecision()));
	}

    @Override
    public void setFormattedValue(String formattedValue) {
        this.formattedValue = formattedValue;
    }

    @Override
	public void setValue(Date value) {
		this.value = value;
	}

    @Override
	public final boolean isMultiple() {
		return false;
	}

	@Override
	public final boolean isNullOrEmpty() {
        return getValue() == null;
	}


	private DatePrecision getPrecision() {
		final DateFieldDefinition.Precision precision = getDefinition().getPrecision();
		if (precision == DateFieldDefinition.Precision.YEARS) return DatePrecision.YEAR;
		if (precision == DateFieldDefinition.Precision.MONTHS) return DatePrecision.MONTH;
		if (precision == DateFieldDefinition.Precision.DAYS) return DatePrecision.DAY;
		if (precision == DateFieldDefinition.Precision.HOURS) return DatePrecision.HOUR;
		if (precision == DateFieldDefinition.Precision.MINUTES) return DatePrecision.MINUTE;
		return DatePrecision.SECOND;
	}

}
