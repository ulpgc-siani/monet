package org.monet.space.explorer.model;

import org.monet.metamodel.IndexDefinition;
import org.monet.space.kernel.model.Node;

public interface NodeIndexEntry extends IndexEntry<Node> {
	String getAttributeValue(String code);
	IndexDefinition getDefinition();
	IndexDefinition.IndexViewProperty getViewDefinition();
}
