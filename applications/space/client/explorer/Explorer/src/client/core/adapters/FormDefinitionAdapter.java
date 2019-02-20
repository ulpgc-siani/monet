package client.core.adapters;

import client.core.model.Field;
import client.core.model.Form;
import client.core.model.List;
import client.core.model.definition.entity.FieldDefinition;
import client.core.model.definition.entity.FormDefinition;
import client.core.model.definition.entity.field.CompositeFieldDefinition;
import client.core.model.fields.CompositeField;
import client.core.system.fields.MultipleField;
import client.services.TranslatorService;

public class FormDefinitionAdapter extends NodeDefinitionAdapter {

	public FormDefinitionAdapter(TranslatorService translatorService) {
		super(translatorService);
	}

	public void adapt(Form form, FormDefinition definition) {
		adaptFields(form, definition);
	}

	private void adaptFields(Form form, FormDefinition definition) {
		for (Field field : form.get())
			adaptField(field, definition.getField(field.getCode()));
	}

	private void adaptField(Field field, FieldDefinition definition) {
		field.setDefinition(definition);

		if (field.isMultiple())
			adaptChildren(((MultipleField) field).getAll(), definition);

		if (field instanceof CompositeField)
			adaptCompositeChildren(((CompositeField) field).getValue(), (CompositeFieldDefinition) definition);
	}

	private void adaptChildren(List children, FieldDefinition definition) {
		for (Field child : (List<Field>) children)
            adaptField(child, definition);
	}

	private void adaptCompositeChildren(List<Field> children, CompositeFieldDefinition definition) {
		for (Field child : children)
			adaptField(child, definition.getField(child.getCode()));
	}
}
