package org.monet.metamodel;

import org.monet.metamodel.interfaces.IsInitiable;
import org.monet.space.kernel.model.Term;
import org.monet.space.mobile.model.Language;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// SelectFieldDeclaration
// Declaración que se utiliza para modelar un	campo de selección

public class SelectFieldProperty extends SelectFieldPropertyBase implements IsInitiable {

	private HashMap<String, Object> termValues = new HashMap<String, Object>();
	private HashMap<Object, String> termCodes = new HashMap<Object, String>();
	private boolean isInitialized = false;
	private TermsProperty termsProperty = null;

	public void init() {
		SelectProperty selectDefinition = this.getSelect();

		if (this._termsProperty != null)
			this.processTermList(this._termsProperty._termPropertyList);

		if (selectDefinition != null && selectDefinition.getDepth() == null)
			selectDefinition._depth = 1L;

		this.isInitialized = true;
	}

	public void setTermList(List<Term> termList) {
		this.termCodes.clear();
		this.termValues.clear();
		this.termsProperty = termsPropertyOf(termList);
		this.processTermList(termsProperty._termPropertyList);
	}

	@Override
	public TermsProperty getTerms() {
		return termsProperty != null ? termsProperty : super.getTerms();
	}

	public String getTermCode(String sValue) {
		if (!isInitialized)
			this.init();
		if (!this.termCodes.containsKey(sValue))
			return null;
		return this.termCodes.get(sValue);
	}

	public String getTermValue(String code) {
		if (!isInitialized)
			this.init();
		if (!this.termValues.containsKey(code))
			return null;
		return Language.getInstance().getModelResource(this.termValues.get(code));
	}

	private void processTermList(List<TermProperty> termList) {
		for (TermProperty term : termList) {
			this.termCodes.put(term._label, term._key);
			this.termValues.put(term._key, term._label);
			processTermList(term._termPropertyList);
		}
	}

	private TermsProperty termsPropertyOf(List<Term> termList) {
		TermsProperty result = new TermsProperty();
		result._termPropertyList = termPropertyListOf(termList);
		return result;
	}

	private ArrayList<TermProperty> termPropertyListOf(List<Term> termList) {
		ArrayList<TermProperty> result = new ArrayList<>();
		for (Term term : termList) result.add(termPropertyOf(term));
		return result;
	}

	private TermProperty termPropertyOf(Term term) {
		TermProperty result = new TermProperty();
		result._key = term.getCode();
		result._label = term.getLabel();
		result._termPropertyList = termPropertyListOf(new ArrayList<>(term.getTermList()));
		return result;
	}

}
