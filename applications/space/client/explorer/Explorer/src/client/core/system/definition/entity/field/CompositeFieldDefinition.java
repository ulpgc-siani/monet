package client.core.system.definition.entity.field;

import client.core.model.Instance;
import client.core.model.List;
import client.core.model.definition.entity.FieldDefinition;
import client.core.system.definition.entity.MultipleableFieldDefinition;

import java.util.HashMap;
import java.util.Map;

public class CompositeFieldDefinition extends MultipleableFieldDefinition implements client.core.model.definition.entity.field.CompositeFieldDefinition {
	private boolean extensible;
	private boolean conditional;
	private List<FieldDefinition> fields;
	private Map<String, FieldDefinition> fieldMap = new HashMap<>();
	private client.core.model.definition.entity.field.CompositeFieldDefinition.ViewDefinition view;

	@Override
	public Instance.ClassName getClassName() {
		return client.core.model.definition.entity.field.CompositeFieldDefinition.CLASS_NAME;
	}

	@Override
	public boolean isExtensible() {
		return extensible;
	}

	public void setExtensible(boolean extensible) {
		this.extensible = extensible;
	}

	@Override
	public boolean isConditional() {
		return conditional;
	}

	public void setConditional(boolean conditional) {
		this.conditional = conditional;
	}

	@Override
	public List<client.core.model.definition.entity.FieldDefinition> getFields() {
		return fields;
	}

	@Override
	public FieldDefinition getField(String key) {
		return fieldMap.get(key);
	}

	public void setFields(List<client.core.model.definition.entity.FieldDefinition> fields) {
		this.fields = fields;

		for (FieldDefinition field : fields) {
			fieldMap.put(field.getCode(), field);
			fieldMap.put(field.getName(), field);
		}
	}

	@Override
	public client.core.model.definition.entity.field.CompositeFieldDefinition.ViewDefinition getView() {
		return view;
	}

	public void setView(client.core.model.definition.entity.field.CompositeFieldDefinition.ViewDefinition view) {
		this.view = view;
	}

	public static class ViewDefinition implements client.core.model.definition.entity.field.CompositeFieldDefinition.ViewDefinition {
		private client.core.model.definition.entity.field.CompositeFieldDefinition.ViewDefinition.Summary summary;
		private client.core.model.definition.entity.field.CompositeFieldDefinition.ViewDefinition.Show show;

		@Override
		public client.core.model.definition.entity.field.CompositeFieldDefinition.ViewDefinition.Summary getSummary() {
			return summary;
		}

		public void setSummary(client.core.model.definition.entity.field.CompositeFieldDefinition.ViewDefinition.Summary summary) {
			this.summary = summary;
		}

		@Override
		public client.core.model.definition.entity.field.CompositeFieldDefinition.ViewDefinition.Show getShow() {
			return show;
		}

		public void setShow(client.core.model.definition.entity.field.CompositeFieldDefinition.ViewDefinition.Show show) {
			this.show = show;
		}

		public static class Summary implements client.core.model.definition.entity.field.CompositeFieldDefinition.ViewDefinition.Summary {
			private List<String> fields;

			@Override
			public List<String> getFields() {
				return fields;
			}

			public void setFields(List<String> fields) {
				this.fields = fields;
			}
		}

		public static class Show implements client.core.model.definition.entity.field.CompositeFieldDefinition.ViewDefinition.Show {
			private List<String> fields;
			private String layout;
			private String layoutExtended;

			@Override
			public List<String> getFields() {
				return fields;
			}

			public void setFields(List<String> fields) {
				this.fields = fields;
			}

			@Override
			public String getLayout() {
				return layout;
			}

			public void setLayout(String layout) {
				this.layout = layout;
			}

			@Override
			public String getLayoutExtended() {
				return layoutExtended;
			}

			public void setLayoutExtended(String layoutExtended) {
				this.layoutExtended = layoutExtended;
			}
		}
	}
}
