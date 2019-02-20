package org.monet.bpi.types;

import org.monet.bpi.Source;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;

public class Term {

  public static final String CODE = "code";
  
  @Attribute(name=CODE)
  private String key;
  
  @Text(required = false)
  private String label;

  public Term() {}
  
  public Term(String key) {
    this.key = key;
  }
  
  public Term(String key, String label) {
    this.key = key;
    this.label = label;
  }

  public void setKey(String code) {
    this.key = code;
  }

  public String getKey() {
    return key;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }
  
  public boolean equals(Term obj) {
    return this.key.equals(obj.key) && this.label.equals(obj.label);
  }

  public boolean isTerm(Source source) {
    return false;
  }

  public boolean isSuperTerm(Source source) {
    return false;
  }

  public void setIsSuperTerm(Source source, boolean value) {
  }

  public boolean isCategory(Source source) {
    return false;
  }

  public void setIsCategory(Source source, boolean value) {
  }

  public boolean isEnabled(Source source) {
    return false;
  }

  public boolean isDisabled(Source source) {
    return false;
  }
  
  public Term addTerm(Term term) {
    return null;
  }

  public Check toCheck() {
    Check check = new Check();
    check.setChecked(false);
    check.setCode(this.key);
    check.setLabel(this.label);
    return check;
  }
  
  @Override
  public String toString() {
    return this.label;
  }
  
}
