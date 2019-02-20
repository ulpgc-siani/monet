package org.monet.space.explorer.control.displays;

import org.monet.space.explorer.control.displays.serializers.Serializer;
import org.monet.space.explorer.control.displays.serializers.SpaceSerializer;
import org.monet.space.explorer.model.Space;

public class SpaceDisplay extends HttpDisplay<Space> {

	@Override
	protected Serializer getSerializer(Space object) {
		return new SpaceSerializer(createSerializerHelper());
	}

}
