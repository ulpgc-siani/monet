package org.monet.v3.model;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;

public class GeometryHelper {

	public static GeometryFactory getFactory() {
		return new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 8307);
	}

}
