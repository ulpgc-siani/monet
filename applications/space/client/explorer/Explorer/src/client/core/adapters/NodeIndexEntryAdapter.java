package client.core.adapters;

import client.core.model.List;
import client.core.model.NodeIndexEntry;

import static client.core.model.NodeIndexEntry.Attribute;
import static client.core.model.definition.entity.IndexDefinition.ReferenceDefinition;

public class NodeIndexEntryAdapter {

	public void adapt(NodeIndexEntry indexEntry, ReferenceDefinition definition) {
		adaptAttribute(indexEntry.getTitle(), definition);
		adaptAttribute(indexEntry.getIcon(), definition);
		adaptAttribute(indexEntry.getPicture(), definition);
		adaptAttributeList(indexEntry.getLines(), definition);
		adaptAttributeList(indexEntry.getLinesBelow(), definition);
		adaptAttributeList(indexEntry.getHighlights(), definition);
		adaptAttributeList(indexEntry.getFooters(), definition);
	}

	private void adaptAttribute(Attribute attribute, ReferenceDefinition definition) {
		if (attribute == null)
			return;

		attribute.setDefinition(definition.getAttribute(attribute.getCode()));
	}

	private void adaptAttributeList(List<Attribute> attributes, ReferenceDefinition definition) {
		for (Attribute attribute : attributes)
			adaptAttribute(attribute, definition);
	}

}
