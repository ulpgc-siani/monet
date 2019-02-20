package client.core.model;

import client.core.model.definition.entity.IndexDefinition;

public interface Index<Definition extends IndexDefinition> extends Entity<Definition> {

	ClassName CLASS_NAME = new ClassName("Index");
	Attribute TypeAttribute = new Attribute() {
		@Override
		public String getName() {
			return "code";
		}

		@Override
		public String getLabel() {
			return "Clase";
		}
	};

	List<Attribute> getAttributes();
	ClassName getClassName();

	interface Attribute {
		String getName();
		String getLabel();
	}

}