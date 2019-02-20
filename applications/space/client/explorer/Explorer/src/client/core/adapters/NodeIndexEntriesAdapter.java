package client.core.adapters;

import client.core.model.List;
import client.core.model.NodeIndexEntry;

import static client.core.model.NodeIndexEntry.*;
import static client.core.model.definition.entity.IndexDefinition.ReferenceDefinition;

public class NodeIndexEntriesAdapter {

	public void adapt(List<NodeIndexEntry> indexEntries, ReferenceDefinition definition) {
		NodeIndexEntryAdapter adapter = new NodeIndexEntryAdapter();

		for (NodeIndexEntry indexEntry : indexEntries)
			adapter.adapt(indexEntry, definition);
	}

}
