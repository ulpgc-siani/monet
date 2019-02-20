package org.monet.bpi.types.location;

public class Polygon extends Geometry {

	protected com.vividsolutions.jts.geom.Polygon polygon;

	Polygon(com.vividsolutions.jts.geom.Polygon polygon) {
		this.geometry = polygon;
		this.polygon = polygon;
	}

	public Polygon(Line border, Line[] holes) {
		this(GeometryFactory.buildPolygon(border, holes));
	}

	public Polygon(String wkt) {
		this(GeometryFactory.buildPolygon(wkt));
	}

	public Line getBorder() {
		return (Line) GeometryFactory.build(this.polygon.getExteriorRing());
	}

	public int getHolesCount() {
		return this.polygon.getNumInteriorRing();
	}

	public Line getHole(int index) {
		return (Line) GeometryFactory.build(this.polygon.getInteriorRingN(index));
	}

}
