package org.monet.bpi.java;

import org.monet.api.backservice.impl.model.Attribute;
import org.monet.api.backservice.impl.model.AttributeList;
import org.monet.api.backservice.impl.model.Indicator;
import org.monet.bpi.BPIFieldCheck;
import org.monet.bpi.types.CheckList;
import org.monet.bpi.types.Check;
import org.monet.bpi.types.TermList;

public class BPIFieldCheckImpl extends BPIFieldImpl<CheckList> implements BPIFieldCheck {

  @Override
  public CheckList get() {
    CheckList checkList = new CheckList();
    
    if (this.attribute == null) return checkList;
    
    for (Attribute attribute : this.attribute.getAttributeList()) {
      String checked = this.getIndicatorValue(attribute, Indicator.CHECKED);
      String code = this.getIndicatorValue(attribute, Indicator.CODE);
      String value = this.getIndicatorValue(attribute, Indicator.VALUE);
      Boolean checkedValue;
      
      if (checked.equals("")) checkedValue = false;
      else checkedValue = Boolean.parseBoolean(checked);
      
      checkList.add(new Check(checkedValue, code, value));
    }
    
    return checkList; 
  }
  
  @Override
  public CheckList getChecked() {
    CheckList checkList = new CheckList();
    
    for (Check check : this.get().getAll()) {
      if (check.isChecked()) 
        checkList.add(check);
    }
    
    return checkList;
  }

  @Override
  public TermList getCheckedAsTermList() {
    TermList termList = new TermList();
    
    for (Check check : this.get().getAll()) {
      if (check.isChecked()) 
        termList.add(check.toTerm());
    }
    
    return termList;
  }
  
  @Override
  public void set(CheckList value) {
    AttributeList attributeList;
    int index = 0;

    if (this.attribute == null) return;
    
    attributeList = this.attribute.getAttributeList();
    attributeList.clear();
    for (Check check : value.getAll()) {
      Attribute attribute = new Attribute();
      attribute.setCode(Attribute.OPTION);
      attribute.setOrder(index);
      attribute.getIndicatorList().add(new Indicator(Indicator.CHECKED, 0, String.valueOf(check.isChecked())));
      attribute.getIndicatorList().add(new Indicator(Indicator.CODE, 1, check.getCode()));
      attribute.getIndicatorList().add(new Indicator(Indicator.VALUE, 2, check.getLabel()));
      attributeList.add(attribute);
      index++;
    }
  }

  @Override
  public void fillFromTermList(TermList termList) {
    this.set(termList.toCheckList());
  }

  @Override
  public boolean equals(Object value) {
    if (value instanceof CheckList)
      return this.get().equals((CheckList)value);
    else
      return false;
  }
  
  @Override
  public void clear() {
    this.set(new CheckList());
  }
  
}