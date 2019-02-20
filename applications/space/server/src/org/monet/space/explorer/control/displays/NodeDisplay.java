package org.monet.space.explorer.control.displays;

import org.monet.space.explorer.control.displays.serializers.NodeSerializer;
import org.monet.space.explorer.control.displays.serializers.Serializer;
import org.monet.space.kernel.model.Node;

public class NodeDisplay extends HttpDisplay<Node> {

	@Override
	protected Serializer getSerializer(Node object) {
		return new NodeSerializer(createSerializerHelper());
	}

}
