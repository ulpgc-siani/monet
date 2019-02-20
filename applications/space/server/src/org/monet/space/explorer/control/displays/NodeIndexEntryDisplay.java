package org.monet.space.explorer.control.displays;

import org.monet.space.explorer.control.displays.serializers.NodeIndexEntrySerializer;
import org.monet.space.explorer.control.displays.serializers.Serializer;
import org.monet.space.explorer.model.NodeIndexEntry;

public class NodeIndexEntryDisplay extends HttpDisplay<NodeIndexEntry> {

	@Override
	protected Serializer getSerializer(NodeIndexEntry object) {
		return new NodeIndexEntrySerializer(createSerializerHelper());
	}

}
