package org.monet.bpi.java;

import org.monet.api.backservice.impl.model.Attribute;
import org.monet.api.backservice.impl.model.Indicator;
import org.monet.bpi.types.Term;
import org.monet.bpi.BPIFieldSelect;
import org.monet.v2.metamodel.SelectFieldDeclaration;

public class BPIFieldSelectImpl extends BPIFieldImpl<Term> implements BPIFieldSelect {

  @Override
  public Term get() {
    Attribute optionAttribute = this.getAttribute(Attribute.OPTION);
    
    // Compatibility mode
    if (optionAttribute == null) optionAttribute = this.attribute;
    
    String code = this.getIndicatorValue(optionAttribute, Indicator.CODE);
    String value = this.getIndicatorValue(optionAttribute, Indicator.VALUE);
    String other = this.getIndicatorValue(optionAttribute, Indicator.OTHER);
    
    if (code.isEmpty()) return null;
    if (value.isEmpty() && other.isEmpty()) return null;
    
    if (value.isEmpty()) {
      SelectFieldDeclaration selectDeclaration = (SelectFieldDeclaration)this.fieldDeclaration;
      if (selectDeclaration.allowOther()) return new Term(code, other);
      return null;
    }
    
    return new Term(code, value);
  }

  @Override
  public void set(Term term) {
    String code;
    String value;
    if(term == null) {
      this.attribute.getAttributeList().delete(Attribute.OPTION);
      return;
    }
    
    code = term.getKey();
    value = term.getLabel();
    if((code == null || value == null))
      return;

    if (this.attribute != null) {
      Attribute optionAttribute = this.attribute.getAttributeList().get(Attribute.OPTION);
      if (optionAttribute == null) {
        optionAttribute = new Attribute();
        optionAttribute.setCode(Attribute.OPTION);
        this.attribute.getAttributeList().add(optionAttribute);
      }
      optionAttribute.addOrSetIndicatorValue(Indicator.CODE, 0, code);
      optionAttribute.addOrSetIndicatorValue(Indicator.VALUE, 1, value);
      this.attribute.getIndicatorList().delete(Indicator.CODE);
      this.attribute.getIndicatorList().delete(Indicator.VALUE);
    }
  }

  @Override
  public boolean isValid() {
    Attribute optionAttribute = this.getAttribute(Attribute.OPTION);

    // Compatibility mode
    if (optionAttribute == null) optionAttribute = this.attribute;

    String code = this.getIndicatorValue(optionAttribute, Indicator.CODE);
    String value = this.getIndicatorValue(optionAttribute, Indicator.VALUE);
    String other = this.getIndicatorValue(optionAttribute, Indicator.OTHER);

    if (value.isEmpty() && other.isEmpty() && !code.isEmpty())
      return false;

    if ((code.isEmpty() && !value.isEmpty()) || (code.isEmpty() && !other.isEmpty()))
      return false;

    return true;
  }

  @Override
  public boolean equals(Object value) {
    if(value instanceof Term)
      return this.get().equals(value);
    else
      return false;
  }

  @Override
  public void clear() {
    this.set(new Term("", ""));
  }
  
}