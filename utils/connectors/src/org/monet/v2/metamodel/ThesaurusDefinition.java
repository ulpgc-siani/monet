package org.monet.v2.metamodel;


import org.monet.bpi.types.Term;
import org.monet.bpi.types.TermList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;

// ThesaurusDefinition
// Declaración que se utiliza para modelar un tesauro

public class ThesaurusDefinition extends ThesaurusDefinitionBase {

  public HashMap<String, TermList> getTermListMap() {
    HashMap<String, TermList> termListMap = new HashMap<String, TermList>();
    for (TermIndexDeclaration termIndexDeclaration : this.getTermIndexDeclarationList()) {
      TermList termList = new TermList();
      for (TermDeclaration termDeclaration : termIndexDeclaration.getTermDeclarationList()) {
        Term term = new Term();
        term.setKey(termDeclaration.getCode());
        term.setLabel(termDeclaration.getLabel());
        LinkedHashSet<Term> childs = new LinkedHashSet<Term>();
        convertTermList(termDeclaration.getTermDeclarationList(), childs);
        //term.setTermList(childs);
        termList.add(term);
      }
      termListMap.put(termIndexDeclaration.getLanguage(), termList);
    }

    return termListMap;
  }

  private void convertTermList(ArrayList<TermDeclaration> termDeclarationList, HashSet<Term> termList) {
    for (TermDeclaration termDeclaration : termDeclarationList) {
      Term term = new Term();
      term.setKey(termDeclaration.getCode());
      term.setLabel(termDeclaration.getLabel());
      LinkedHashSet<Term> childs = new LinkedHashSet<Term>();
      convertTermList(termDeclaration.getTermDeclarationList(), termList);
      //term.setTermList(childs);
      termList.add(term);
    }
  }

  public boolean isExternal() {
    return (_external != null);
  }
}
