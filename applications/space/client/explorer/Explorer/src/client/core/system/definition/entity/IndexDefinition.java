package client.core.system.definition.entity;

import client.core.model.List;
import client.core.model.definition.Ref;
import client.core.model.definition.views.IndexViewDefinition;
import client.core.system.MonetList;

import java.util.HashMap;
import java.util.Map;

public class IndexDefinition extends EntityDefinition implements client.core.model.definition.entity.IndexDefinition {
	private client.core.model.definition.entity.IndexDefinition.ReferenceDefinition reference;
	private List<client.core.model.definition.views.IndexViewDefinition> views;

	@Override
	public client.core.model.definition.entity.IndexDefinition.ReferenceDefinition getReference() {
		return reference;
	}

	public void setReference(client.core.model.definition.entity.IndexDefinition.ReferenceDefinition reference) {
		this.reference = reference;
	}

	@Override
	public List<client.core.model.definition.views.IndexViewDefinition> getViews() {
		return views;
	}

	@Override
	public IndexViewDefinition getDefaultView() {
		for (IndexViewDefinition view : views) {
			if (view.isDefault()) return view;
		}
		return views.get(0);
	}

	@Override
	public IndexViewDefinition getView(String key) {
		return getViews().get(key);
	}

	public void setViews(List<client.core.model.definition.views.IndexViewDefinition> views) {
		this.views = views;
	}

	public static class ReferenceDefinition implements client.core.model.definition.entity.IndexDefinition.ReferenceDefinition {
		private List<client.core.model.definition.entity.IndexDefinition.ReferenceDefinition.AttributeDefinition> attributeList;
		private Map<String, client.core.model.definition.entity.IndexDefinition.ReferenceDefinition.AttributeDefinition> attributes = new HashMap<>();

		@Override
		public List<client.core.model.definition.entity.IndexDefinition.ReferenceDefinition.AttributeDefinition> getAttributes() {
			return new MonetList<>(attributes.values());
		}

		@Override
		public client.core.model.definition.entity.IndexDefinition.ReferenceDefinition.AttributeDefinition getAttribute(String key) {
			return getAttributes() == null ? null : attributes.get(key);
		}

		public void setAttributes(List<client.core.model.definition.entity.IndexDefinition.ReferenceDefinition.AttributeDefinition> attributes) {
			this.attributeList = attributes;
			this.attributes.clear();
			for (client.core.model.definition.entity.IndexDefinition.ReferenceDefinition.AttributeDefinition attribute : attributes) {
				this.attributes.put(attribute.getCode(), attribute);
				this.attributes.put(attribute.getName(), attribute);
			}
		}

		public static class AttributeDefinition implements client.core.model.definition.entity.IndexDefinition.ReferenceDefinition.AttributeDefinition {
			private String code;
			private String name;
			private String label;
			private String description;
			private Type type;
			private Precision precision;
			private Ref source;

			@Override
			public String getCode() {
				return code;
			}

			public void setCode(String code) {
				this.code = code;
			}

			@Override
			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

			@Override
			public String getLabel() {
				return label;
			}

			public void setLabel(String label) {
				this.label = label;
			}

			@Override
			public String getDescription() {
				return description;
			}

			public void setDescription(String description) {
				this.description = description;
			}

			@Override
			public Type getType() {
				return type;
			}

			@Override
			public boolean is(Type type) {
				return getType() == type;
			}

			public void setType(Type type) {
				this.type = type;
			}

			@Override
			public Precision getPrecision() {
				return precision;
			}

			public void setPrecision(Precision precision) {
				this.precision = precision;
			}

			@Override
			public Ref getSource() {
				return source;
			}

			public void setSource(Ref source) {
				this.source = source;
			}
		}

	}

}
