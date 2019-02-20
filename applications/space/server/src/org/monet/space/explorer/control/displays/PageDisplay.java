package org.monet.space.explorer.control.displays;

import org.monet.space.explorer.control.displays.serializers.PageSerializer;
import org.monet.space.explorer.control.displays.serializers.Serializer;
import org.monet.space.kernel.model.Page;

public class PageDisplay extends HttpDisplay<Page> {

	@Override
	protected Serializer getSerializer(Page object) {
		return new PageSerializer(createSerializerHelper());
	}

}
