package org.monet.bpi.java;

import org.monet.bpi.Field;
import org.monet.bpi.FieldComposite;
import org.monet.metamodel.CompositeFieldProperty;
import org.monet.metamodel.FieldProperty;
import org.monet.space.kernel.bpi.java.locator.BPIClassLocator;
import org.monet.space.kernel.model.Attribute;
import org.monet.space.kernel.model.Indicator;
import org.monet.space.kernel.model.Node;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class FieldCompositeImpl extends FieldImpl<List<Field<?>>> implements FieldComposite {

	public FieldCompositeImpl() {
	}

	Node _node;

	void injectNode(Node node) {
		this._node = node;
	}

	FieldFactory fieldFactory = FieldFactory.getInstance();
	BPIClassLocator bpiClassLocator = BPIClassLocator.getInstance();

	Attribute getNodeAttribute(String code) {
		LinkedHashSet<Attribute> result = this.attribute.getAttributeList().searchByCode(code);
		return (result.size() > 0) ? result.iterator().next() : null;
	}

	List<Attribute> getNodeAttributes(String code) {
		return this.attribute.getAttributeList().searchAllByCode(code);
	}

	public <T, F extends Field<V>, V> T getField(String key) {
		return getField(this._node.getCode(), key);
	}

	@SuppressWarnings("unchecked")
	protected <T, F extends Field<V>, V> T getField(String definitionName, String name) {
		Attribute attribute = null;
		Field<?> field = null;
		FieldProperty fieldDeclaration = ((CompositeFieldProperty) this.fieldDefinition).getField(name);
		Node node = FieldImpl.node(this._node.getId());

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
				fields.add((F) fieldFactory.get(definitionName, fieldDeclaration, currentAttribute, node));
			}

			FieldMultipleImpl<F, V> fieldMultiple = new FieldMultipleImpl<F, V>();
			fieldMultiple.injectDefinitionName(definitionName);
			fieldMultiple.injectFieldDeclaration(fieldDeclaration);
			fieldMultiple.injectNode(node);
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
			field = fieldFactory.get(definitionName, fieldDeclaration, attribute, node);
		}

		return (T) field;
	}

	@Override
	public List<Field<?>> get() {
		CompositeFieldProperty definition = (CompositeFieldProperty) this.fieldDefinition;
		List<Field<?>> result = new ArrayList<>();

		for (FieldProperty fieldProperty : definition.getAllFieldPropertyList())
			result.add((Field<?>) getField(this._node.getCode(), fieldProperty.getCode()));

		return result;
	}

	@Override
	public void set(List<Field<?>> value) {
	}

	@Override
	public boolean equals(Object value) {
		return false;
	}

	@Override
	public void clear() {
	}

	@Override
	public boolean isEnabled() {
		String value = this.getIndicatorValue(Indicator.CONDITIONED);
		return value != null && (value.toLowerCase().equals("conditioned") || Boolean.valueOf(value).equals(true));
	}

	@Override
	public void setEnabled(boolean value) {
		setIndicatorValue(Indicator.CONDITIONED, "conditioned");
	}

	@Override
	public boolean isExtended() {
		String value = this.getIndicatorValue(Indicator.EXTENDED);
		return value != null && (value.toLowerCase().equals("extended") || Boolean.valueOf(value).equals(true));
	}

	@Override
	public void setExtended(boolean value) {
		setIndicatorValue(Indicator.EXTENDED, "extended");
	}

}