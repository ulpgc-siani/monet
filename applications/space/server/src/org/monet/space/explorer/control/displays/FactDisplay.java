package org.monet.space.explorer.control.displays;

import org.monet.space.explorer.control.displays.serializers.FactSerializer;
import org.monet.space.explorer.control.displays.serializers.Serializer;
import org.monet.space.kernel.model.Fact;

public class FactDisplay extends HttpDisplay<Fact> {

	@Override
	protected Serializer getSerializer(Fact object) {
		return new FactSerializer(createSerializerHelper());
	}

}
