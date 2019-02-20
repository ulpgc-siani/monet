package org.monet.space.explorer.control.displays;

import org.monet.space.explorer.control.displays.serializers.Serializer;
import org.monet.space.explorer.control.displays.serializers.TermSerializer;
import org.monet.space.kernel.model.TermList;

public class HistoryDisplay extends HttpDisplay<TermList> {

	@Override
	protected Serializer getSerializer(TermList object) {
		return new TermSerializer(createSerializerHelper());
	}

}
