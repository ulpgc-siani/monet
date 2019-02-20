package org.monet.bpi.types.location;

import java.util.ArrayList;
import java.util.List;

public class MultiPolygon extends Geometry {

	protected com.vividsolutions.jts.geom.MultiPolygon polygon;

	MultiPolygon(com.vividsolutions.jts.geom.MultiPolygon polygon) {
		this.geometry = polygon;
		this.polygon = polygon;
	}

	public MultiPolygon(String wkt) {
		this(GeometryFactory.buildMultiPolygon(wkt));
	}

	public List<Polygon> getPolygons() {
		List<Polygon> result = new ArrayList<>();
		int count = this.polygon.getNumGeometries();
		for (int i=0; i<count; i++)
			result.add(new Polygon((com.vividsolutions.jts.geom.Polygon) this.polygon.getGeometryN(i)));
		return result;
	}
}
