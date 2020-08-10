package org.monet.bpi.types;

import org.monet.metamodel.TermProperty;
import org.monet.space.kernel.model.Language;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class TermList {

	private HashMap<String, Term> terms = new HashMap<String, Term>();

	public TermList() {
	}

	public TermList(org.monet.space.kernel.model.TermList termList) {
		for (org.monet.space.kernel.model.Term value : termList.getAll()) add(termOf(value));
	}

	public TermList(ArrayList<TermProperty> termPropertyList) {
		for (TermProperty value : termPropertyList) add(termOf(value));
	}

	public void clear() {
		this.terms.clear();
	}

	public void add(Term term) {
		this.terms.put(term.getKey(), term);
	}

	public void add(String code, String label) {
		this.terms.put(code, new Term(code, label));
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

	@Override
	public String toString() {
		return toString(", ");
	}

	public String toString(String separator) {
		StringBuilder builder = new StringBuilder();
		for (Term value : getAll()) {
			builder.append(value.toString());
			builder.append(separator);
		}
		if (builder.length() > 0) {
			int length = builder.length();
			builder.delete(length - separator.length(), length);
		}
		return builder.toString();
	}

	private Term termOf(org.monet.space.kernel.model.Term term) {
		return new Term(term.getCode(), term.getLabel());
	}

	private Term termOf(TermProperty term) {
		return new Term(term.getKey(), Language.getInstance().getModelResource(term.getLabel()));
	}

}
