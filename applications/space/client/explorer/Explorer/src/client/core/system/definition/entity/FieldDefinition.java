package client.core.system.definition.entity;

import client.core.model.Instance;
import client.core.model.List;

public class FieldDefinition extends EntityDefinition implements client.core.model.definition.entity.FieldDefinition {
	private Template template = Template.MULTILINE;
	private FieldType fieldType = FieldType.NORMAL;
	private boolean collapsible;
	private boolean required;
	private boolean readonly;
	private boolean extended;
	private boolean superField;
	private boolean isStatic;
	private boolean univocal;
	private List<client.core.model.definition.entity.FieldDefinition.Display> displayList;

	@Override
	public boolean is(String key) {
		return key.equals(getCode()) || key.equals(getName());
	}


	@Override
	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	@Override
	public FieldType getFieldType() {
		return fieldType;
	}

	@Override
	public boolean isCollapsible() {
		return collapsible;
	}

	public void setCollapsible(boolean collapsible) {
		this.collapsible = collapsible;
	}

	public void setFieldType(FieldType fieldType) {
		this.fieldType = fieldType;
	}

	@Override
	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	@Override
	public boolean isReadonly() {
		return readonly;
	}

	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}

	@Override
	public boolean isExtended() {
		return extended;
	}

	public void setExtended(boolean extended) {
		this.extended = extended;
	}

	@Override
	public boolean isSuperField() {
		return superField;
	}

	public void setSuperField(boolean superField) {
		this.superField = superField;
	}

	@Override
	public boolean isStatic() {
		return isStatic;
	}

	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}

	@Override
	public boolean isUnivocal() {
		return univocal;
	}

	public void setUnivocal(boolean univocal) {
		this.univocal = univocal;
	}

	@Override
	public List<client.core.model.definition.entity.FieldDefinition.Display> getDisplays() {
		return displayList;
	}

	@Override
	public client.core.model.definition.entity.FieldDefinition.Display getDisplay(client.core.model.definition.entity.FieldDefinition.Display.When when) {

		for (client.core.model.definition.entity.FieldDefinition.Display display : displayList)
			if (display.getWhen() == when)
				return display;

		return null;
	}

	public void setDisplays(List<client.core.model.definition.entity.FieldDefinition.Display> displayList) {
		this.displayList = displayList;
	}

	@Override
	public Instance.ClassName getClassName() {
		return client.core.model.definition.entity.FieldDefinition.CLASS_NAME;
	}

	public static class Display implements client.core.model.definition.entity.FieldDefinition.Display {
		private String message;
		private When when;

		@Override
		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		@Override
		public When getWhen() {
			return when;
		}

		public void setWhen(When when) {
			this.when = when;
		}
	}

}
