package client.core.system;

import client.core.model.List;
import client.core.model.definition.entity.IndexDefinition;
import client.core.model.definition.entity.IndexDefinition.ReferenceDefinition.AttributeDefinition;

public class Index<Definition extends IndexDefinition> extends Entity<Definition> implements client.core.model.Index<Definition> {
	private List<client.core.model.Index.Attribute> attributes = null;

	public Index() {
	}

	public Index(String id) {
		super(id, null);
	}

	public Index(String id, client.core.model.Index.Attribute[] attributes) {
		super(id, null);
		this.attributes = new MonetList<>(attributes);
	}

	@Override
	public List<client.core.model.Index.Attribute> getAttributes() {

		if (attributes != null)
			return attributes;

		loadAttributes();

		return attributes;
	}

	@Override
	public final ClassName getClassName() {
		return client.core.model.Index.CLASS_NAME;
	}

	public static class Attribute implements client.core.model.Index.Attribute {

		private String code;
		private String label;
		public Attribute(String code, String label) {
			this.code = code;
			this.label = label;
		}

		@Override
		public String getName() {
			return code;
		}

		@Override
		public String getLabel() {
			return label;
		}

	}

	private void loadAttributes() {
		IndexDefinition definition = getDefinition();
		List<AttributeDefinition> attributeDefinitions = definition.getReference().getAttributes();

		attributes = new MonetList<>();
		for (final AttributeDefinition attributeDefinition : attributeDefinitions)
			attributes.add(new Attribute(attributeDefinition.getCode(), attributeDefinition.getLabel()));
	}

}