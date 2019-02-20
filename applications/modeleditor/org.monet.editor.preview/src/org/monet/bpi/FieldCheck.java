package org.monet.bpi;

import org.monet.bpi.types.CheckList;
import org.monet.bpi.types.TermList;

public interface FieldCheck extends Field<CheckList> {

  public CheckList getChecked();

  public TermList getCheckedAsTermList();
  
  public void fillFromTermList(TermList termList);
  
}