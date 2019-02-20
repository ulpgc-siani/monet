/*
    Monet will assist business to process re-engineering. Monet separate the
    business logic from the underlying technology to allow Model-Driven
    Engineering (MDE). These models guide all the development process over a
    Service Oriented Architecture (SOA).

    Copyright (C) 2009  Grupo de Ingenieria del Sofware y Sistemas de la Universidad de Las Palmas de Gran Canaria

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see http://www.gnu.org/licenses/.
*/

package org.monet.editor.preview.model;

import java.util.ArrayList;
import java.util.Iterator;

import org.monet.metamodel.TermProperty;
import org.monet.metamodel.ThesaurusDefinition;

public class TermList extends BaseModelList<Term> {
  private String language;

  public TermList() {
    super();
    this.totalCount = 0;
  }
  
  public TermList(int totalCount) {
    this();
    this.totalCount = totalCount;
  }
  
  public TermList(ArrayList<TermProperty> termDefinitions) {
    for (TermProperty termDefinition : termDefinitions) {
      Term term = new Term();
      term.setCode(termDefinition.getKey());
      term.setLabel(termDefinition.getLabel());
      term.setTags(ThesaurusDefinition.getTags(termDefinition));
      this.add(term);
    }
    this.setTotalCount(this.items.size());
  }

  public String getLanguage() {
    return this.language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public MonetHashMap<Term> get() {
    return this.items;
  }

  public Term get(String code) {
    if (! this.items.containsKey(code)) return null;
    return this.items.get(code);
  }

  public void add(Term term) {
    if (this.items.containsKey(term.getCode())) return;
    this.items.put(term.getCode(), term);
  }

  public Boolean setTotalCount(Integer totalCount) {
    this.totalCount = totalCount;
    return true;
  }

  public Term first() {
    Iterator<Term> iter = this.items.values().iterator();
    if (!iter.hasNext()) return null;
    return iter.next();
  }

  public TermList subList(Integer startPos, Integer limit) {
    TermList result = new TermList(this.totalCount);
    Iterator<String> iter = this.items.keySet().iterator();
    Integer pos = 0;
    Integer count = 0;
    
    while ((count < limit) && (iter.hasNext())) {
      String codeTerm = iter.next();
      pos++;
      if (pos >= startPos) {
        result.add(this.items.get(codeTerm));
        count++;
      }
    }
    
    return result;
  }
  
  public TermList subList(Integer startPos) {
    return this.subList(startPos, this.items.size());
  }

}
