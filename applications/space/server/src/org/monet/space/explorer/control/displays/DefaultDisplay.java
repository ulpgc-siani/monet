package org.monet.space.explorer.control.displays;

import org.monet.space.explorer.control.displays.serializers.Serializer;
import org.monet.space.explorer.control.displays.serializers.StringSerializer;

public class DefaultDisplay extends HttpDisplay<String> {
	@Override
	protected Serializer getSerializer(String object) {
		return new StringSerializer(createSerializerHelper());
	}
}
