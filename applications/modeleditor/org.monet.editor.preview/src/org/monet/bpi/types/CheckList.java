package org.monet.bpi.types;

import java.util.ArrayList;
import java.util.Collection;

public class CheckList {
  
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

}
