package org.monet.bpi.java;

import org.monet.bpi.BehaviorSource;
import org.monet.bpi.Source;
import org.monet.bpi.types.Term;
import org.monet.bpi.types.TermList;

public abstract class SourceImpl implements Source, BehaviorSource {

  @Override
  public String getPartnerName() {
    return null;
  }
  
  @Override
  public String getPartnerLabel() {
    return null;
  }

  @Override
  public TermList getTermList() {
    return null;
  }

  @Override
  public TermList getTermList(String parent) {
    return null;
  }

  @Override
  public Term getParentTerm(String code) {
    return null;
  }

  @Override
  public boolean existsTerm(String key) {
    return false;
  }

  @Override
  public Term getTerm(String key) {
    return null;
  }

  @Override
  public boolean isTerm(String code) {
    return false;
  }

  @Override
  public boolean isTermSuperTerm(String code) {
    return false;
  }

  @Override
  public void setIsTermSuperTerm(String code, boolean value) {
  }

  @Override
  public boolean isTermCategory(String code) {
    return false;
  }
  
  @Override
  public void setIsTermCategory(String code, boolean value) {
  }

  @Override
  public boolean isTermEnabled(String code) {
    return false;
  }

  @Override
  public boolean isTermDisabled(String code) {
    return false;
  }
  
  @Override
  public Term addTerm(Term term) {
    return null;
  }

  @Override
  public Term addTerm(Term term, String parentKey) {
    return null;
  }

  @Override
  public Term addTerm(Term term, Term parent) {
    return null;
  }

  @Override
  public void onPopulated() {
  }

  @Override
  public void onSynchronized() {
  }

  @Override
  public void onTermAdded(Term term) {
  }

  @Override
  public void onTermModified(Term term) {
  }

}
