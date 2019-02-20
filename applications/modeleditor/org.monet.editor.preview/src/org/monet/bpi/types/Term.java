package org.monet.bpi.types;


public class Term {

  public static final String CODE = "code";
  
  private String key;
  
  private String label;

  public Term() {}
  
  public Term(String key) {
    this.key = key;
  }
  
  public Term(String key, String label) {
    this.key = key;
    this.label = label;
  }

  public void setKey(String key) {
    this.key = key;
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

  public Check toCheck() {
    Check check = new Check();
    check.setChecked(false);
    check.setCode(this.key);
    check.setLabel(this.label);
    return check;
  }

}
