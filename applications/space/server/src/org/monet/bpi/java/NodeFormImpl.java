package org.monet.bpi.java;

import org.monet.bpi.BehaviorNodeForm;
import org.monet.bpi.Field;
import org.monet.bpi.NodeForm;
import org.monet.bpi.types.Location;
import org.monet.metamodel.FieldProperty;
import org.monet.metamodel.FormDefinition;
import org.monet.space.kernel.bpi.java.locator.BPIClassLocator;
import org.monet.space.kernel.model.Attribute;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public abstract class NodeFormImpl extends NodeImpl implements NodeForm, BehaviorNodeForm {

	FieldFactory fieldFactory = FieldFactory.getInstance();
	BPIClassLocator bpiClassLocator = BPIClassLocator.getInstance();

	Attribute getNodeAttribute(String code) {
		LinkedHashSet<Attribute> result = this.node.getAttributeList().searchByCode(code);
		return (result.size() > 0) ? result.iterator().next() : null;
	}

	List<Attribute> getNodeAttributes(String code) {
		return this.node.getAttributeList().searchAllByCode(code);
	}

	// Obsolete. Compatibility mode.
	protected <T, F extends Field<V>, V> T getField(String definition, String name) {
		return getField(name);
	}

	public <T, F extends Field<V>, V> T getField(String name) {
		Attribute attribute = null;
		Field<?> field = null;
		FormDefinition formDefinition = (FormDefinition) this.node.getDefinition();
		String formDefinitionCode = formDefinition.getCode();
		FieldProperty fieldDefinition = formDefinition.getField(name);

		if (fieldDefinition == null)
			return null;

		if (!name.substring(0, 1).equals("[")) name = "[" + name + "]";

		if (fieldDefinition.isMultiple()) {
			List<Attribute> attributes = this.getNodeAttributes(fieldDefinition.getCode());
			if (attributes.size() == 0) attributes = this.getNodeAttributes(name);
			if (attributes.size() > 0) {
				attribute = attributes.get(0);
				attributes.remove(0);
			} else {
				attribute = new Attribute();
				attribute.setCode(fieldDefinition.getCode());
				this.node.getAttributeList().add(attribute);
			}

			List<F> fields = new ArrayList<>();
			for (Attribute currentAttribute : attributes) {
				fields.add((F) fieldFactory.get(formDefinitionCode, fieldDefinition, currentAttribute, this.node));
			}

			FieldMultipleImpl<F, V> fieldMultiple = new FieldMultipleImpl<F, V>();
			fieldMultiple.injectDefinitionName(formDefinitionCode);
			fieldMultiple.injectFieldDeclaration(fieldDefinition);
			fieldMultiple.injectNode(this.node);
			fieldMultiple.injectAttribute(attribute);
			fieldMultiple.injectFields(fields);
			fieldMultiple.injectFactory(this.fieldFactory);

			return (T) fieldMultiple;
		} else {
			attribute = this.getNodeAttribute(fieldDefinition.getCode());
			if (attribute == null) attribute = this.getNodeAttribute(name);
			if (attribute == null) {
				attribute = new Attribute();
				attribute.setCode(fieldDefinition.getCode());
				this.node.getAttributeList().add(attribute);
			}
			field = fieldFactory.get(formDefinitionCode, fieldDefinition, attribute, this.node);
		}

		return (T) field;
	}

	@Override
	public List<Field<?>> getFields() {
		List<Field<?>> result = new ArrayList<>();
		FormDefinition formDefinition = (FormDefinition)this.node.getDefinition();

		for (FieldProperty fieldProperty : formDefinition.getAllFieldPropertyList())
			result.add((Field<?>) getField(fieldProperty.getCode()));

		return result;
	}

	@Override
	public void onFieldChanged(Field<?> field) {
	}

	public Location getLocation() {
		return GeoreferencedImpl.getLocation(this.node);
	}

	public void setLocation(Location bpiLocation) {
		GeoreferencedImpl.setLocation(this.node, bpiLocation);
		super.setLocation(bpiLocation);
	}

	public void reset() {
		nodeLayer.resetNodeForm(node);
	}

}
