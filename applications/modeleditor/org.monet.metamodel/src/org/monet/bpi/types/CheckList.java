package org.monet.bpi.types;

import java.util.ArrayList;
import java.util.Collection;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="check-list")
public class CheckList {
  
  @ElementList
  private ArrayList<Check> checkList = new ArrayList<Check>();
  
  public CheckList() {}
  
  public void clear() {
    this.checkList.clear();
  }
  
  public void add(Check check) {
    this.checkList.add(check);
  }

  public void add(boolean isChecked, String code, String label) {
    this.checkList.add(new Check(isChecked, code, label));
  }
  
  public Check get(int pos) {
    return this.checkList.get(pos);
  }

  public Collection<Check> getAll() {
    return this.checkList;
  }

  public boolean isChecked(String code) {
    for (Check check : this.checkList) {
      if (check.getCode().equals(code))
        return check.isChecked();
    }
    return false;
  }

  public boolean equals(CheckList checkList) {
    boolean found;
    Collection<Check> checks = this.getAll();
    
    for (Check check : checkList.getAll()) {
      found = false;
      for (Check currentCheck : checks) {
        if (check.equals(currentCheck)) found = true;
      }
      if (!found) return false;
    }
    return true;
  }
  
  @Override
  public String toString() {
    return toString(", ");
  }
  
  public String toString(String separator) {
    StringBuilder builder = new StringBuilder();
    for(Check value : getAll()) {
      builder.append(value.toString());
      builder.append(separator);
    }
    if(builder.length() > 0) {
      int length = builder.length();
      builder.delete(length-separator.length(), length);
    }
    return builder.toString();
  }

}
