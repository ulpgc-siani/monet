package org.monet.bpi.java;

import org.monet.api.space.backservice.impl.model.Attribute;
import org.monet.api.space.backservice.impl.model.Indicator;
import org.monet.bpi.FieldDate;
import org.monet.bpi.types.Date;
import org.monet.v2.model.DateFormat;
import org.monet.v2.model.LibraryDate;
import org.monet.v2.model.constants.Strings;
import org.monet.metamodel.DateFieldProperty;

public class FieldDateImpl extends FieldImpl<Date> implements FieldDate {

	public static Date get(Attribute attribute) {
		FieldDateImpl fieldDate = new FieldDateImpl();
		fieldDate.attribute = attribute;
		return fieldDate.get();
	}

	public static void set(Attribute attribute, DateFieldProperty fieldDefinition, java.util.Date value) {
		FieldDateImpl fieldDate = new FieldDateImpl();
		fieldDate.attribute = attribute;
		fieldDate.fieldDefinition = fieldDefinition;
		fieldDate.set(value);
	}

	@Override
	public Date get() {
		String formattedValue = this.getIndicatorValue(Indicator.VALUE);
		String dateValue = this.getIndicatorValue(Indicator.INTERNAL);
		java.util.Date date = null;

		if (dateValue == null)
			return null;

		if (dateValue.trim().isEmpty()) {
			if (formattedValue.trim().isEmpty())
				return null;
			else
				date = LibraryDate.parsePartialDate(formattedValue);
		} else {
			date = LibraryDate.parseDate(dateValue);
		}
		if (date == null)
			return null;

		Date dateTime = new Date(date);
		dateTime.setFormattedValue(formattedValue);
		return dateTime;
	}

	public void set(java.util.Date value) {
		this.set(new Date(value));
	}

	@Override
	public void set(Date value) {
		DateFieldProperty fieldDefinition = (DateFieldProperty) this.fieldDefinition;
		String dateInternal = null;
		String dateValue;

		if (value == null) {
			dateValue = "";
		} else if (value.getValue() != null) {
			dateInternal = LibraryDate.getDateAndTimeString(value.getValue(), "es", LibraryDate.Format.INTERNAL, true, Strings.BAR45);
			dateValue = DateFormat.format(fieldDefinition.getFormat("es"), LibraryDate.parseDate(dateInternal));
		} else if (value.getFormattedValue() != null) {
			dateInternal = value.getFormattedValue();
			dateValue = DateFormat.format(fieldDefinition.getFormat("es"), LibraryDate.parseDate(dateInternal));
		} else {
			dateInternal = "";
			dateValue = "";
		}

		this.setIndicatorValue(Indicator.INTERNAL, dateInternal);
		this.setIndicatorValue(Indicator.VALUE, dateValue);
	}

	@Override
	public boolean equals(Object value) {
		if (value instanceof Date)
			return this.get().equals(value);
		else
			return false;
	}

	@Override
	public void clear() {
		this.set((java.util.Date) null);
	}

}