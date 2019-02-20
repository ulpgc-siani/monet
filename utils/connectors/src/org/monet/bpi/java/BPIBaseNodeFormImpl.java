package org.monet.bpi.java;

import org.monet.api.backservice.impl.model.Attribute;
import org.monet.bpi.BPIBaseNodeForm;
import org.monet.bpi.BPIBehaviorNodeForm;
import org.monet.bpi.BPIField;
import org.monet.bpi.BPISchema;
import org.monet.v2.metamodel.FieldDeclaration;
import org.monet.v2.metamodel.FormDefinition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class BPIBaseNodeFormImpl<Schema extends BPISchema,
	OperationEnum extends Enum<?>>
	extends BPIBaseNodeImpl<Schema>
	implements BPIBaseNodeForm<Schema>, BPIBehaviorNodeForm<OperationEnum> {

	BPIFieldFactory fieldFactory = BPIFieldFactory.getInstance();

	Attribute getNodeAttribute(String code) {
		HashMap<String, Attribute> hmResult = this.node.getAttributeList().searchByCode(code);
		return (hmResult.size() > 0) ? hmResult.values().iterator().next() : null;
	}

	List<Attribute> getNodeAttributes(String code) {
		return this.node.getAttributeList().searchAllByCode(code);
	}

	@SuppressWarnings("unchecked")
	protected <T, F extends BPIField<V>, V> T getField(String definition, String name) {
		Attribute attribute = null;
		BPIField<?> field = null;
		FormDefinition formDefinition = (FormDefinition)this.dictionary.getDefinition(this.node.getCode());
		FieldDeclaration fieldDeclaration = formDefinition.getFieldDeclaration(name);

		if (!name.substring(0, 1).equals("[")) name = "[" + name + "]";

		if (fieldDeclaration.isMultiple()) {
			List<Attribute> attributes = this.getNodeAttributes(fieldDeclaration.getCode());
			if (attributes.size() == 0) attributes = this.getNodeAttributes(name);
			if (attributes.size() > 0) {
				attribute = attributes.get(0);
				attributes.remove(0);
			}

			ArrayList<F> fields = new ArrayList<F>();
			for (Attribute currentAttribute : attributes) {
				fields.add((F) fieldFactory.get(definition, fieldDeclaration, currentAttribute, this.node));
			}

			BPIFieldMultipleImpl<F, V> fieldMultiple = new BPIFieldMultipleImpl<F, V>();
			fieldMultiple.injectDefinitionName(definition);
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
				this.node.getAttributeList().add(attribute);
			}
			field = fieldFactory.get(definition, fieldDeclaration, attribute, this.node);
		}

		return (T) field;
	}

}
