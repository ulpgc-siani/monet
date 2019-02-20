package org.monet.bpi;

import org.monet.bpi.types.Term;
import org.monet.bpi.types.TermList;


public interface Source {

  public String getPartnerName();
  public String getPartnerLabel();
  public TermList getTermList();
  public TermList getTermList(String parent);
  public Term getParentTerm(String code);
  public boolean existsTerm(String key);
  public Term getTerm(String key);
  public boolean isTerm(String code);
  public boolean isTermSuperTerm(String code);
  public void setIsTermSuperTerm(String code, boolean value);
  public boolean isTermCategory(String code);
  public void setIsTermCategory(String code, boolean value);
  public boolean isTermEnabled(String code);
  public boolean isTermDisabled(String code);
  public Term addTerm(Term term);
  public Term addTerm(Term term, String parentKey);
  public Term addTerm(Term term, Term parent);
  
}