package org.monet.space.explorer.control.displays;

import org.monet.space.explorer.control.displays.serializers.FilterOptionSerializer;
import org.monet.space.explorer.control.displays.serializers.Serializer;
import org.monet.space.explorer.model.Filter;

public class FilterOptionDisplay extends HttpDisplay<Filter.Option> {
	@Override
	protected Serializer getSerializer(Filter.Option object) {
		return new FilterOptionSerializer(createSerializerHelper());
	}
}
