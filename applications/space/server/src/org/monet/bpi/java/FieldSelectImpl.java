package org.monet.bpi.java;

import org.monet.bpi.FieldSelect;
import org.monet.bpi.exceptions.TermNotFoundException;
import org.monet.bpi.types.Term;
import org.monet.metamodel.SelectFieldProperty;
import org.monet.metamodel.SourceDefinition;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.components.layers.SourceLayer;
import org.monet.space.kernel.model.*;

import java.util.ArrayList;
import java.util.List;

import static org.monet.bpi.java.SourceImpl.toMonetTerm;

public class FieldSelectImpl extends FieldImpl<Term> implements FieldSelect {

	@Override
	public Term get() {
		Attribute optionAttribute = this.getAttribute(Attribute.OPTION);

		// Compatibility mode
		if (optionAttribute == null) optionAttribute = this.attribute;

		String code = this.getIndicatorValue(optionAttribute, Indicator.CODE);
		String value = this.getIndicatorValue(optionAttribute, Indicator.VALUE);
		String other = this.getIndicatorValue(optionAttribute, Indicator.OTHER);
		String source = this.getIndicatorValue(optionAttribute, Indicator.SOURCE);

		if (code.isEmpty()) return null;
		if (value.isEmpty() && other.isEmpty()) return null;

		if (value.isEmpty()) {
			SelectFieldProperty selectDeclaration = (SelectFieldProperty) this.fieldDefinition;
			if (selectDeclaration.allowOther()) return new Term(code, other, source);
			return null;
		}

		return new Term(code, value, source);
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
			throw new TermNotFoundException(this);

		if (code == null) {
			code = label;
			label = null;
		}

		if (label == null) {
			SourceLayer layer = ComponentPersistence.getInstance().getSourceLayer();
			NodeLayer nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
			SelectFieldProperty fieldDeclaration = (SelectFieldProperty) this.fieldDefinition;

			if (fieldDeclaration.getSource() != null) {
				String sourceId = this.getIndicatorValue(Indicator.SOURCE);
				SourceList sourceList = null;

				if (sourceId.isEmpty()) {
					String sourceCode = Dictionary.getInstance().getDefinitionCode(fieldDeclaration.getSource().getValue());
					Node node = nodeLayer.loadNode(this.nodeId);
					sourceList = layer.loadSourceList(sourceCode, node.getPartnerContext());
				} else {
					sourceList = new SourceList();
					sourceList.add(layer.loadSource(sourceId));
				}

				for (Source<SourceDefinition> source : sourceList) {
					label = layer.locateSourceTermValue(source, code);
					if (label != null && !label.isEmpty())
						break;
				}

				if (label == null || label.isEmpty()) {
					label = code;
					for (Source<SourceDefinition> source : sourceList) {
						code = layer.locateSourceTermCode(source, label);
						if (code != null && !code.isEmpty())
							break;
					}
				}
			} else {
				label = fieldDeclaration.getTermValue(code);
				if (label == null || label.isEmpty()) {
					label = code;
					code = fieldDeclaration.getTermCode(label);
				}
			}

			if (code == null || code.isEmpty())
				throw new TermNotFoundException(this);
		}

		if (this.attribute != null) {
			Attribute optionAttribute = this.attribute.getAttributeList().get(Attribute.OPTION);
			if (optionAttribute == null) {
				optionAttribute = new Attribute();
				optionAttribute.setCode(Attribute.OPTION);
				this.attribute.getAttributeList().add(optionAttribute);
			}
			optionAttribute.addOrSetIndicatorValue(Indicator.CODE, 0, code);
			optionAttribute.addOrSetIndicatorValue(Indicator.VALUE, 1, label);
			optionAttribute.addOrSetIndicatorValue(Indicator.SOURCE, 1, term.getSource());
			this.attribute.getIndicatorList().delete(Indicator.CODE);
			this.attribute.getIndicatorList().delete(Indicator.VALUE);
			this.attribute.getIndicatorList().delete(Indicator.SOURCE);
		}
	}

	@Override
	public void setInlineTerms(List<Term> termList) {
		SelectFieldProperty fieldDeclaration = (SelectFieldProperty) this.fieldDefinition;
		List<org.monet.space.kernel.model.Term> monetTermList = new ArrayList<>();

		for (Term term : termList) {
			org.monet.space.kernel.model.Term monetTerm = toMonetTerm(term);
			monetTermList.add(monetTerm);
		}

		fieldDeclaration.setTermList(monetTermList);
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
	public boolean isValid() {
		Attribute optionAttribute = this.getAttribute(Attribute.OPTION);

		// Compatibility mode
		if (optionAttribute == null) optionAttribute = this.attribute;

		String code = this.getIndicatorValue(optionAttribute, Indicator.CODE);
		String value = this.getIndicatorValue(optionAttribute, Indicator.VALUE);
		String other = this.getIndicatorValue(optionAttribute, Indicator.OTHER);

		if (value.isEmpty() && other.isEmpty() && !code.isEmpty())
			return false;

		if ((code.isEmpty() && !value.isEmpty()) || (code.isEmpty() && !other.isEmpty()))
			return false;

		return true;
	}

	@Override
	public String getSourceDefinitionCode() {
		SelectFieldProperty fieldDefinition = (SelectFieldProperty) this.fieldDefinition;

		if (fieldDefinition.getSource() == null)
			return null;

		return fieldDefinition.getSource().getValue();
	}
}