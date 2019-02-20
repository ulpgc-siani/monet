package org.monet.bpi.java;

import org.monet.api.backservice.impl.model.Indicator;
import org.monet.bpi.types.DateTime;
import org.monet.bpi.BPIFieldDate;
import org.monet.v2.metamodel.DateFieldDeclaration;
import org.monet.v2.model.DateFormat;
import org.monet.v2.model.LibraryDate;
import org.monet.v2.model.constants.Strings;

import java.util.Date;

public class BPIFieldDateImpl extends BPIFieldImpl<DateTime> implements BPIFieldDate {

  @Override
  public DateTime get() {
    String formattedValue = this.getIndicatorValue(Indicator.VALUE);
    String dateValue = this.getIndicatorValue(Indicator.INTERNAL);
    Date date = null;
    
    if (dateValue.trim().isEmpty()) {
      if(formattedValue.trim().isEmpty())
        return null;
      else
        date = LibraryDate.parsePartialDate(formattedValue);
    } else {
      date = LibraryDate.parseDate(dateValue);
    }
    if(date == null)
      return null;
    
    DateTime dateTime = new DateTime(date);
    dateTime.setFormattedValue(formattedValue);
    
    return dateTime;
  }

  public void set(Date value) {
    this.set(new DateTime(value));
  }
  
  @Override
  public void set(DateTime value) {
    String dateInternal = null;
    Date dateValue;
    if(value == null) {
    	dateValue = null;
    }else if(value.getValue() != null) {
      dateInternal = LibraryDate.getDateAndTimeString(value.getValue(), "en", LibraryDate.Format.INTERNAL, true, Strings.BAR45);
      dateValue = value.getValue();
    } else {
      dateInternal = (String)value.getFormattedValue();
      dateValue = LibraryDate.parseDate(dateInternal);
    }
    
    DateFieldDeclaration fieldDefinition = (DateFieldDeclaration)this.fieldDeclaration;
    
    this.setIndicatorValue(Indicator.INTERNAL, dateInternal);
    String format = fieldDefinition.getFormat() != null ? fieldDefinition.getFormat().getValue() : "d\\m\\Y";
    this.setIndicatorValue(Indicator.VALUE, DateFormat.format(format, dateValue));
  }

  @Override
  public boolean isValid() {
    String formattedValue = this.getIndicatorValue(Indicator.VALUE);
    String dateValue = this.getIndicatorValue(Indicator.INTERNAL);

    if (!formattedValue.isEmpty() && dateValue.isEmpty())
      return false;

    if (formattedValue.isEmpty() && !dateValue.isEmpty())
      return false;

    return true;
  }

  @Override
  public boolean equals(Object value) {
    if(value instanceof DateTime)
      return this.get().equals(value);
    else
      return false;
  }

  @Override
  public void clear() {
    this.set((Date)null);
  }

}