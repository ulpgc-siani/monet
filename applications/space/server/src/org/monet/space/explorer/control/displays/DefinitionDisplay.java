package org.monet.space.explorer.control.displays;

import org.monet.metamodel.Definition;
import org.monet.space.explorer.control.displays.serializers.Serializer;
import org.monet.space.explorer.control.displays.serializers.definition.DefinitionSerializer;

public class DefinitionDisplay extends HttpDisplay<Definition> {

	@Override
	protected Serializer getSerializer(Definition object) {
		return new DefinitionSerializer(createSerializerHelper());
	}

}
