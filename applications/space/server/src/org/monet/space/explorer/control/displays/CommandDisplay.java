package org.monet.space.explorer.control.displays;

import org.monet.space.explorer.control.displays.serializers.ClientOperationSerializer;
import org.monet.space.explorer.control.displays.serializers.Serializer;
import org.monet.space.kernel.model.ClientOperation;

public class CommandDisplay extends HttpDisplay<ClientOperation> {

	@Override
	protected Serializer getSerializer(ClientOperation object) {
		return new ClientOperationSerializer(createSerializerHelper());
	}

}
