package org.monet.bpi.types;

import java.util.Collection;
import java.util.HashMap;

public class TermList {

  private HashMap<String, Term> terms = new HashMap<String, Term>();
  
  public TermList() {}
  
  public void clear() {
    this.terms.clear();
  }
  
  public void add(Term term) {
    this.terms.put(term.getKey(), term);
  }

  public void add(String key, String label) {
    this.terms.put(key, new Term(key, label));
  }
  
  public boolean exists(String code) {
    return this.terms.containsKey(code);
  }
  
  public Term get(String code) {
    return this.terms.get(code);
  }

  public Collection<Term> getAll() {
    return this.terms.values();
  }

  public CheckList toCheckList() {
    CheckList checkList = new CheckList();
    for (Term term : this.terms.values()) {
      checkList.add(term.toCheck());
    }
    return checkList;
  }

}
