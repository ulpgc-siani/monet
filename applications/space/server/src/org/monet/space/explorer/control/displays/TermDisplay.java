package org.monet.space.explorer.control.displays;

import org.monet.space.explorer.control.displays.serializers.Serializer;
import org.monet.space.explorer.control.displays.serializers.TermSerializer;
import org.monet.space.explorer.model.IndexEntry;

public class TermDisplay extends HttpDisplay<IndexEntry> {

	@Override
	protected Serializer getSerializer(IndexEntry object) {
		return new TermSerializer(createSerializerHelper());
	}

}
