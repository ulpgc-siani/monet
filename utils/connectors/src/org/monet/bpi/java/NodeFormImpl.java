package org.monet.bpi.java;

import org.monet.api.space.backservice.BackserviceApi;
import org.monet.api.space.backservice.impl.model.Attribute;
import org.monet.bpi.BehaviorNodeForm;
import org.monet.bpi.Field;
import org.monet.bpi.NodeForm;
import org.monet.bpi.types.Location;
import org.monet.metamodel.Dictionary;
import org.monet.metamodel.FieldProperty;
import org.monet.metamodel.FormDefinition;
import org.monet.v3.BPIClassLocator;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public abstract class NodeFormImpl extends NodeImpl implements NodeForm, BehaviorNodeForm {

	FieldFactory fieldFactory = FieldFactory.getInstance();

	public void injectApi(BackserviceApi api) {
		this.api = api;
	}

	public void injectBPIClassLocator(BPIClassLocator bpiClassLocator) {
		this.bpiClassLocator = bpiClassLocator;
	}

	public void injectDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	Attribute getNodeAttribute(String code) {
		LinkedHashMap<String, Attribute> result = this.node.getAttributeList().searchByCode(code);
		return (result.size() > 0) ? result.values().iterator().next() : null;
	}

	List<Attribute> getNodeAttributes(String code) {
		return this.node.getAttributeList().searchAllByCode(code);
	}

	@SuppressWarnings("unchecked")
	protected <T, F extends Field<V>, V> T getField(String definition, String name) {
		Attribute attribute = null;
		Field<?> field = null;
		FormDefinition formDefinition = (FormDefinition)this.dictionary.getDefinition(this.node.getCode());
		FieldProperty fieldDefinition = formDefinition.getField(name);

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

			ArrayList<F> fields = new ArrayList<F>();
			for (Attribute currentAttribute : attributes) {
				fields.add((F) fieldFactory.get(definition, fieldDefinition, currentAttribute, this.node));
			}

			FieldMultipleImpl<F, V> fieldMultiple = new FieldMultipleImpl<F, V>();
			fieldMultiple.injectDefinitionName(definition);
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
			field = fieldFactory.get(definition, fieldDefinition, attribute, this.node);
		}

		return (T) field;
	}

	@Override
	public List<Field<?>> getFields() {
		List<Field<?>> result = new ArrayList<>();
		FormDefinition formDefinition = (FormDefinition)this.dictionary.getDefinition(this.node.getCode());

		for (FieldProperty fieldProperty : formDefinition.getAllFieldPropertyList())
			result.add((Field<?>) getField(formDefinition.getCode(), fieldProperty.getCode()));

		return result;
	}

	@Override
	public void onFieldChanged(Field<?> field) {
	}

	@Override
	public Location getLocation() {
		GeoreferencedImpl.inject(api);
		return GeoreferencedImpl.getLocation(node);
	}

	@Override
	public void setLocation(Location bpilocation) {
		throw new NotImplementedException();
	}

	public void reset() {
		this.api.resetNodeForm(node.getId());
	}

}
