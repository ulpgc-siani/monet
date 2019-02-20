package org.monet.bpi;

import org.monet.bpi.types.Term;
import org.monet.bpi.types.TermList;

public interface Source {

	public String getId();

	public String getName();

	public String getCode();

	public String getPartnerName();

	public String getPartnerLabel();

	public TermList getTermList();

	public TermList getTermList(String parent);

	public Term getParentTerm(String code);

	public Term getTerm(String code);

	public boolean isTerm(String code);

	public boolean isTermSuperTerm(String code);

	public boolean isTermCategory(String code);

	public boolean isTermEnabled(String code);

	public boolean isTermDisabled(String code);

	public Term addTerm(Term term);

	public Term addTerm(Term term, String parent);

	public Term addTerm(Term term, Term parent);

}