package org.monet.space.explorer.control.displays;

import org.monet.space.explorer.control.displays.serializers.BusinessUnitSerializer;
import org.monet.space.explorer.control.displays.serializers.Serializer;
import org.monet.space.explorer.model.BusinessUnit;

public class BusinessUnitDisplay extends HttpDisplay<BusinessUnit> {

	@Override
	protected Serializer getSerializer(BusinessUnit object) {
		return new BusinessUnitSerializer(createSerializerHelper());
	}

}
