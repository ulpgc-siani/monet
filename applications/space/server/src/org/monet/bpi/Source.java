package org.monet.bpi;

import org.monet.bpi.types.Term;
import org.monet.bpi.types.TermList;

public interface Source {

	String getPartnerName();

	String getPartnerLabel();

	TermList getTermList();

	TermList getTermList(String parent);

	Term getParentTerm(String code);

	boolean existsTerm(String code);

	Term getTerm(String code);

	boolean isTerm(String code);

	boolean isTermSuperTerm(String code);

	void setIsTermSuperTerm(String key, boolean value);

	boolean isTermCategory(String code);

	void setIsTermCategory(String key, boolean value);

	boolean isTermEnabled(String code);

	boolean isTermDisabled(String code);

	Term addTerm(Term term);

	Term addTerm(Term term, String parentCode);

	Term addTerm(Term term, Term parent);

}