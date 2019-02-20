package org.monet.space.explorer.control.dialogs;

import org.monet.metamodel.IndexDefinition;
import org.monet.space.explorer.control.dialogs.constants.Parameter;
import org.monet.space.explorer.control.dialogs.deserializers.FilterDeserializer;
import org.monet.space.explorer.control.dialogs.deserializers.OrderDeserializer;
import org.monet.space.explorer.model.Filter;
import org.monet.space.explorer.model.Order;
import org.monet.space.kernel.model.Node;

import java.util.List;

public class LoadIndexEntryDialog extends LoadIndexDialog {

	public IndexDefinition getDefinition() {
		return dictionary.getIndexDefinition(getEntityId());
	}

	public Node getNode() {
		return layerProvider.getNodeLayer().loadNode(getString(Parameter.NODE));
	}

}
