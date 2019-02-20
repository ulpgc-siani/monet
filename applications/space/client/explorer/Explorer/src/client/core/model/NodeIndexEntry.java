package client.core.model;

import client.core.model.types.Link;

import static client.core.model.definition.entity.IndexDefinition.ReferenceDefinition.AttributeDefinition;

public interface NodeIndexEntry extends IndexEntry<Node> {

	Instance.ClassName CLASS_NAME = new Instance.ClassName("NodeIndexEntry");

	boolean isGeoReferenced();
	void setGeoReferenced(boolean value);

	List<Attribute> getAttributes();

	Attribute getTitle();
	Attribute getPicture();
	Attribute getIcon();

	List<Attribute> getHighlights();
	List<Attribute> getLines();
	List<Attribute> getLinesBelow();
	List<Attribute> getFooters();

	Link toLink();

	interface Attribute {
		String getCode();
		String getValue();
		AttributeDefinition getDefinition();
		void setDefinition(AttributeDefinition definition);
		boolean is(AttributeDefinition.Type type);
		Operation getLink();
	}

	interface Operation {
	}

}
