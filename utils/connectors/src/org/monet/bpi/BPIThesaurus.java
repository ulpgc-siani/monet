package org.monet.bpi;

import org.monet.bpi.types.TermList;

public interface BPIThesaurus {

  public TermList getTermList();
  public TermList getTermList(String parent);
  
}