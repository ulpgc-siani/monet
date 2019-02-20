package client.presenters.displays.entity.field;

import client.core.model.definition.entity.field.DateFieldDefinition;
import client.core.model.Node;
import client.core.model.fields.MultipleDateField;
import client.presenters.displays.entity.MultipleFieldDisplay;
import cosmos.presenters.Presenter;
import cosmos.types.Date;
import cosmos.types.DatePrecision;
import cosmos.types.DateRange;
import cosmos.utils.date.DateBuilder;
import cosmos.utils.date.DateFormatter;
import cosmos.utils.date.SpanishDateBuilder;

import java.util.HashMap;
import java.util.Map;

import static client.core.model.definition.entity.field.DateFieldDefinition.*;
import static cosmos.utils.date.DateFormatter.Format;

public class MultipleDateFieldDisplay extends MultipleFieldDisplay<MultipleDateField, DateFieldDefinition, Date> implements IsMultipleDateFieldDisplay {

    public static final Type TYPE = new Type("MultipleDateFieldDisplay", MultipleFieldDisplay.TYPE);
    private Map<DatePrecision, String> formats;
    private DateRange range;

    public MultipleDateFieldDisplay(Node node, MultipleDateField field) {
        super(node, field);
    }

    @Override
    protected void onInjectServices() {
        super.onInjectServices();
        formats = new HashMap<>();
        addFormats();
        range = new DateRange(getRangeMin(), getRangeMax(), getPrecision());
    }

    @Override
    public Type getType() {
        return TYPE;
    }

    @Override
    public Date createDate() {
        return getPrecision().getDateWithPrecision();
    }

    private boolean dateIsBetweenRange(Date date) {
        return range.dateBetween(date);
    }

    @Override
    public boolean dateIsValid(Date date) {
        if (getDefinition().allowLessPrecision())
            return getPrecision().dateIsValidAllowingLessPrecision(date);
        return getPrecision().dateIsValid(date);
    }

    @Override
    public boolean hasTime() {
        return getPrecision() == DatePrecision.HOUR || getPrecision() == DatePrecision.MINUTE || getPrecision() == DatePrecision.SECOND;
    }

    @Override
    protected boolean check(Date date) {
        return dateIsBetweenRange(date);
    }

    @Override
    public boolean rangeWrong() {
        return rangeWrong(getValue());
    }

    @Override
    public RangeDefinition getRange() {
        return getDefinition().getRange();
    }

    @Override
    public String getValueAsString() {
        return getValue() == null ? "" : DateFormatter.format(getValue(), formats.get(getPrecision()));
    }

    @Override
    public DateBuilder getDateBuilder() {
        return new SpanishDateBuilder();
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
    public boolean isNearDate() {
        return getDefinition().getPurpose() == Purpose.NEAR_DATE;
    }

    @Override
    public void addHook(Presenter.Hook hook) {
        super.addHook(hook);
    }

    @Override
    public String formatDate(Date date) {
        return date == null ? "" : DateFormatter.format(date, formats.get(getPrecision()));
    }

    @Override
    public boolean allowLessPrecision() {
        return getDefinition().allowLessPrecision();
    }

    private boolean rangeWrong(Date value) {
        return value != null && !dateIsBetweenRange(getValue());
    }

    private Date getRangeMin() {
        if (getDefinition().getRange() == null || getDefinition().getRange().getMin() == -1) return null;
        return new Date(getDefinition().getRange().getMin());
    }

    private Date getRangeMax() {
        if (getDefinition().getRange() == null || getDefinition().getRange().getMax() == -1) return null;
        return new Date(getDefinition().getRange().getMax());
    }

    private void addFormats() {
        formats.put(DatePrecision.YEAR, Format.YEARS);
        formats.put(DatePrecision.MONTH, Format.MONTHS);
        formats.put(DatePrecision.DAY, Format.DAYS);
        formats.put(DatePrecision.HOUR, Format.HOURS);
        formats.put(DatePrecision.MINUTE, Format.MINUTES);
        formats.put(DatePrecision.SECOND, Format.SECONDS);
    }
}
