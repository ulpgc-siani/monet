package org.monet.space.explorer.control.displays;

import org.monet.space.explorer.control.displays.serializers.RoleSerializer;
import org.monet.space.explorer.control.displays.serializers.Serializer;
import org.monet.space.kernel.model.Role;

public class RoleDisplay extends HttpDisplay<Role> {

	@Override
	protected Serializer getSerializer(Role object) {
		return new RoleSerializer(createSerializerHelper());
	}

}
