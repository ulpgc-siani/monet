package org.monet.bpi.types.location;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import org.monet.v3.model.GeometryHelper;

public class GeometryFactory {

	private static com.vividsolutions.jts.geom.GeometryFactory factory = GeometryHelper.getFactory();

	public static Geometry build(String wkt) {
		return build(buildGeometry(wkt));
	}

	public static Geometry build(com.vividsolutions.jts.geom.Geometry g) {
		Geometry geometry = null;
		if (g instanceof com.vividsolutions.jts.geom.Point) {
			geometry = new org.monet.bpi.types.location.Point((com.vividsolutions.jts.geom.Point) g);
		} else if (g instanceof com.vividsolutions.jts.geom.LineString) {
			geometry = new org.monet.bpi.types.location.Line((LineString) g);
		} else if (g instanceof com.vividsolutions.jts.geom.Polygon) {
			geometry = new org.monet.bpi.types.location.Polygon((com.vividsolutions.jts.geom.Polygon) g);
		}
		return geometry;
	}

	public static com.vividsolutions.jts.geom.Point buildPoint(double x, double y) {
		return factory.createPoint(new Coordinate(x, y));
	}

	public static com.vividsolutions.jts.geom.LineString buildLineString(Point[] points) {
		Coordinate[] coordinates = new Coordinate[points.length];
		int i = 0;
		for (Point point : points) {
			coordinates[i] = point.geometry.getCoordinate();
			i++;
		}
		return factory.createLineString(coordinates);
	}

	public static com.vividsolutions.jts.geom.Polygon buildPolygon(Line border, Line[] holes) {
		LinearRing borderRing = (LinearRing) border.line;

		LinearRing[] holesRings = new LinearRing[holes.length];
		int i = 0;
		for (Line line : holes) {
			holesRings[i] = (LinearRing) line.line;
			i++;
		}
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

	public static com.vividsolutions.jts.geom.Geometry buildGeometry(String wkt) {
		WKTReader reader = new WKTReader();
		try {
			return reader.read(wkt);
		} catch (ParseException e) {
			return null;
		}
	}

	public static com.vividsolutions.jts.geom.Geometry extract(org.monet.bpi.types.location.Geometry geometry) {
		return geometry.geometry;
	}

}
