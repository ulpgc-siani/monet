package org.monet.bpi.types.location;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.model.map.GeometryHelper;

public class GeometryFactory {

	private static com.vividsolutions.jts.geom.GeometryFactory factory = GeometryHelper.getFactory();

	public static Geometry build(String wkt) {
		return build(buildGeometry(wkt));
	}

	public static Geometry build(com.vividsolutions.jts.geom.Geometry g) {
		if (g instanceof com.vividsolutions.jts.geom.Point)
			return new org.monet.bpi.types.location.Point((com.vividsolutions.jts.geom.Point) g);
		if (g instanceof com.vividsolutions.jts.geom.LineString)
			return new org.monet.bpi.types.location.Line((LineString) g);
		if (g instanceof com.vividsolutions.jts.geom.Polygon)
			return new org.monet.bpi.types.location.Polygon((com.vividsolutions.jts.geom.Polygon) g);
		if (g instanceof com.vividsolutions.jts.geom.MultiPolygon)
			return new org.monet.bpi.types.location.MultiPolygon((com.vividsolutions.jts.geom.MultiPolygon) g);
		return null;
	}

	public static com.vividsolutions.jts.geom.Point buildPoint(double x, double y) {
		return factory.createPoint(new Coordinate(x, y));
	}

	public static com.vividsolutions.jts.geom.LineString buildLineString(Point[] points) {
		Coordinate[] coordinates = new Coordinate[points.length];
		int i = 0;
		for (Point point : points)
			coordinates[i++] = point.geometry.getCoordinate();
		return factory.createLineString(coordinates);
	}

	public static com.vividsolutions.jts.geom.Polygon buildPolygon(Line border, Line[] holes) {
		LinearRing borderRing = (LinearRing) border.line;

		LinearRing[] holesRings = new LinearRing[holes.length];
		int i = 0;
		for (Line line : holes)
			holesRings[i++] = (LinearRing) line.line;
		return factory.createPolygon(borderRing, holesRings);
	}

	public static com.vividsolutions.jts.geom.Point buildPoint(String wkt) {
		return (com.vividsolutions.jts.geom.Point) buildGeometry(wkt);
	}

	public static com.vividsolutions.jts.geom.LineString buildLineString(String wkt) {
		return (com.vividsolutions.jts.geom.LineString) buildGeometry(wkt);
	}

	public static com.vividsolutions.jts.geom.Polygon buildPolygon(String wkt) {
		return (com.vividsolutions.jts.geom.Polygon) buildGeometry(wkt);
	}

	public static com.vividsolutions.jts.geom.MultiPolygon buildMultiPolygon(String wkt) {
		return (com.vividsolutions.jts.geom.MultiPolygon) buildGeometry(wkt);
	}

	public static com.vividsolutions.jts.geom.Geometry buildGeometry(String wkt) {
		try {
			return new WKTReader().read(wkt);
		} catch (ParseException e) {
			AgentLogger.getInstance().error(e);
			return null;
		}
	}

	public static com.vividsolutions.jts.geom.Geometry extract(org.monet.bpi.types.location.Geometry geometry) {
		return geometry.geometry;
	}
}
