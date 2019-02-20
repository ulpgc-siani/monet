package org.monet.bpi.types;


public class Check {

  public static final String IS_CHECKED = "is-checked";
  public static final String CODE       = "code";
  
  private boolean isChecked;
  private String code;
  private String label;
  
  public Check() {}
  
  public Check(boolean isChecked, String code, String label) {
    this.isChecked = isChecked;
    this.code = code;
    this.label = label;
  }
  
  public void setChecked(boolean isChecked) {
    this.isChecked = isChecked;
  }
  
  public boolean isChecked() {
    return isChecked;
  }
  
  public void setCode(String code) {
    this.code = code;
  }

  public String getCode() {
    return this.code;
  }
  
  public void setLabel(String label) {
    this.label = label;
  }
  
  public String getLabel() {
    return label;
  }
  
  public Term toTerm() {
    Term term = new Term();
    term.setKey(this.code);
    term.setLabel(this.label);
    return term;
  }
  
  public boolean equals(Check check) {
    return this.isChecked() == check.isChecked() && this.getCode().equals(check.getCode()) && this.getLabel().equals(check.getLabel());
  }

}
