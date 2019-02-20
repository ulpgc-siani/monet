package org.monet.bpi;

import org.monet.bpi.types.TermList;


public interface Thesaurus {

  public TermList getTermList();
  public TermList getTermList(String parent);
  
}