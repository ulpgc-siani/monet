package org.monet.space.explorer.control.displays;

import org.monet.space.explorer.control.displays.serializers.Serializer;
import org.monet.space.explorer.control.displays.serializers.SourceSerializer;
import org.monet.space.kernel.model.Source;

public class SourceDisplay extends HttpDisplay<Source> {

	@Override
	protected Serializer getSerializer(Source object) {
		return new SourceSerializer(createSerializerHelper());
	}

}
