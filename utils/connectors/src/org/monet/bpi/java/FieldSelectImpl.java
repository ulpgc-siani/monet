package org.monet.bpi.java;

import org.monet.api.space.backservice.impl.model.Attribute;
import org.monet.api.space.backservice.impl.model.Indicator;
import org.monet.bpi.FieldSelect;
import org.monet.bpi.types.Term;
import org.monet.metamodel.Definition;
import org.monet.metamodel.SelectFieldProperty;

public class FieldSelectImpl extends FieldImpl<Term> implements FieldSelect {

	@Override
	public Term get() {
		Attribute optionAttribute = this.getAttribute(Attribute.OPTION);

		// Compatibility mode
		if (optionAttribute == null) optionAttribute = this.attribute;

		String code = this.getIndicatorValue(optionAttribute, Indicator.CODE);
		String value = this.getIndicatorValue(optionAttribute, Indicator.VALUE);
		String other = this.getIndicatorValue(optionAttribute, Indicator.OTHER);

		if (code.isEmpty()) return null;
		if (value.isEmpty() && other.isEmpty()) return null;

		if (value.isEmpty()) {
			SelectFieldProperty selectDeclaration = (SelectFieldProperty) this.fieldDefinition;
			if (selectDeclaration.allowOther()) return new Term(code, other);
			return null;
		}

		return new Term(code, value);
	}

	@Override
	public void set(Term term) {
		String code;
		String label;
		if (term == null) {
			this.attribute.getAttributeList().delete(Attribute.OPTION);
			return;
		}

		code = term.getKey();
		label = term.getLabel();
		if ((code == null && label == null))
			return;

		if (this.attribute != null) {
			Attribute optionAttribute = this.attribute.getAttributeList().get(Attribute.OPTION);
			if (optionAttribute == null) {
				optionAttribute = new Attribute();
				optionAttribute.setCode(Attribute.OPTION);
				this.attribute.getAttributeList().add(optionAttribute);
			}
			optionAttribute.addOrSetIndicatorValue(Indicator.CODE, 0, code);
			optionAttribute.addOrSetIndicatorValue(Indicator.VALUE, 1, label);
			this.attribute.getIndicatorList().delete(Indicator.CODE);
			this.attribute.getIndicatorList().delete(Indicator.VALUE);
		}
	}

	@Override
	public boolean equals(Object value) {
		if (value instanceof Term)
			return this.get().equals(value);
		else
			return false;
	}

	@Override
	public void clear() {
		this.set(new Term("", ""));
	}

	@Override
	public String getSourceId() {
		Attribute optionAttribute = this.getAttribute(Attribute.OPTION);

		// Compatibility mode
		if (optionAttribute == null) optionAttribute = this.attribute;

		return this.getIndicatorValue(optionAttribute, Indicator.SOURCE);
	}

	@Override
	public String getSourceCode() {
		SelectFieldProperty selectDeclaration = (SelectFieldProperty) this.fieldDefinition;

		if (selectDeclaration.getSource() == null)
			return null;

		Definition definition = this.dictionary.getDefinition(selectDeclaration.getSource().getValue());
		return definition.getCode();
	}

}