package org.monet.space.office.presentation.user.renders;

import org.monet.metamodel.CompositeFieldProperty;
import org.monet.metamodel.FieldProperty;
import org.monet.metamodel.FormDefinition;

import java.util.HashMap;

public class FormDefinitionRender extends NodeDefinitionRender {
	protected FormDefinition definition;

	public FormDefinitionRender() {
		super();
	}

	private String initFormField(FieldProperty fieldDefinition) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String fieldsNames = "";

		map.put("name", fieldDefinition.getName());
		map.put("code", fieldDefinition.getCode());
		fieldsNames += block("form.field", map);

		if (fieldDefinition.isComposite()) {
			CompositeFieldProperty compositeDefinition = (CompositeFieldProperty) fieldDefinition;
			for (FieldProperty childDefinition : compositeDefinition.getAllFieldPropertyList())
				fieldsNames += this.initFormField(childDefinition);
		}

		return fieldsNames;
	}


	@Override
	public void setTarget(Object target) {
		super.setTarget(target);
		this.definition = (FormDefinition) target;
	}

	@Override
	protected void initForm() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String fieldsNames = "";

		for (FieldProperty fieldDefinition : this.definition.getAllFieldPropertyList()) {
			fieldsNames += this.initFormField(fieldDefinition);
		}

		map.put("fieldsNames", fieldsNames);

		addMark("form", block("form", map));
	}

}
