package client.core.system;

import client.core.model.Field;
import client.core.model.NodeView;
import client.core.model.definition.entity.FieldDefinition;
import client.core.model.definition.entity.FormDefinition;
import client.core.model.definition.views.FormViewDefinition;
import client.core.model.definition.views.NodeViewDefinition;

public class Form extends Node<FormDefinition> implements client.core.model.Form {
	private client.core.model.List<client.core.model.Field> fields;

	public Form() {
	}

	@Override
	protected NodeView loadView(NodeViewDefinition viewDefinition) {
		FormView view = new FormView(new Key(viewDefinition.getCode(), viewDefinition.getName()), viewDefinition.getLabel(), viewDefinition.isDefault(), this);
		view.setDefinition((FormViewDefinition)viewDefinition);
		return view;
	}

	public Form(String id, String label, boolean isComponent) {
		super(id, label, isComponent, Type.FORM);
	}

	@Override
	public ClassName getClassName() {
		return client.core.model.Form.CLASS_NAME;
	}

	@Override
	public client.core.model.List<client.core.model.Field> get() {
		return this.fields;
	}

	public client.core.model.Field get(String key) {
		String code = key;

		FieldDefinition fieldDefinition = getDefinition().getField(key);
		if (fieldDefinition != null)
			code = fieldDefinition.getCode();

		for (Field field : get())
			if (field.getCode().equals(code))
				return field;

		return null;
	}

	public void set(client.core.model.List<client.core.model.Field> fields) {
		this.fields = fields;
	}

	@Override
	public boolean isEnvironment() {
		return false;
	}
}
