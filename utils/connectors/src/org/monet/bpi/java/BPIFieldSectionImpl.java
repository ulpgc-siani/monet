package org.monet.bpi.java;

import org.monet.api.backservice.impl.model.Attribute;
import org.monet.api.backservice.impl.model.Node;
import org.monet.bpi.BPIField;
import org.monet.bpi.BPIFieldSection;
import org.monet.v2.metamodel.FieldDeclaration;
import org.monet.v2.metamodel.FormDefinition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BPIFieldSectionImpl extends BPIFieldImpl<Void> implements BPIFieldSection {

	public BPIFieldSectionImpl() {
	}

	Node node;
	BPIFieldFactory fieldFactory = BPIFieldFactory.getInstance();

	void injectNode(Node node) {
		this.node = node;
	}

	Attribute getNodeAttribute(String code) {
		HashMap<String, Attribute> hmResult = this.attribute.getAttributeList().searchByCode(code);
		return (hmResult.size() > 0) ? hmResult.values().iterator().next() : null;
	}

	List<Attribute> getNodeAttributes(String code) {
		return this.attribute.getAttributeList().searchAllByCode(code);
	}

	@SuppressWarnings("unchecked")
	public <T, F extends BPIField<V>, V> T getField(String definitionName, String name) {
		Attribute attribute = null;
		BPIField<?> field = null;
		FormDefinition formDefinition = (FormDefinition) this.dictionary.getDefinition(this.node.getCode());
		FieldDeclaration fieldDeclaration = formDefinition.getFieldDeclaration(name);

		if (!name.substring(0, 1).equals("[")) name = "[" + name + "]";

		if (fieldDeclaration.isMultiple()) {
			List<Attribute> attributes = this.getNodeAttributes(fieldDeclaration.getCode());
			if (attributes.size() == 0) attributes = this.getNodeAttributes(name);
			if (attributes.size() > 0) {
				attribute = attributes.get(0);
				attributes.remove(0);
			} else {
				attribute = new Attribute();
				attribute.setCode(fieldDeclaration.getCode());
				this.setAttribute(fieldDeclaration.getCode(), attribute);
			}

			ArrayList<F> fields = new ArrayList<F>();
			for (Attribute currentAttribute : attributes) {
				fields.add((F) fieldFactory.get(definitionName, fieldDeclaration, currentAttribute, this.node));
			}

			BPIFieldMultipleImpl<F, V> fieldMultiple = new BPIFieldMultipleImpl<F, V>();
			fieldMultiple.injectDefinitionName(definitionName);
			fieldMultiple.injectFieldDeclaration(fieldDeclaration);
			fieldMultiple.injectNode(this.node);
			fieldMultiple.injectAttribute(attribute);
			fieldMultiple.injectFields(fields);
			fieldMultiple.injectFactory(this.fieldFactory);

			return (T) fieldMultiple;
		} else {
			attribute = this.getNodeAttribute(fieldDeclaration.getCode());
			if (attribute == null) attribute = this.getNodeAttribute(name);
			if (attribute == null) {
				attribute = new Attribute();
				attribute.setCode(fieldDeclaration.getCode());
				this.setAttribute(fieldDeclaration.getCode(), attribute);
			}
			field = fieldFactory.get(definitionName, fieldDeclaration, attribute, this.node);
		}

		return (T) field;
	}

	@Override
	public Void get() {
		return null;
	}

	@Override
	public void set(Void value) {
	}

	@Override
	public boolean equals(Object value) {
		return false;
	}

	@Override
	public void clear() {
	}

}