package org.monet.bpi.types.location;

public class Point extends Geometry {

	protected com.vividsolutions.jts.geom.Point point;

	Point(com.vividsolutions.jts.geom.Point point) {
		this.geometry = point;
		this.point = point;
	}

	public Point(double x, double y) {
		this.point = GeometryFactory.buildPoint(x, y);
	}

	public Point(String wkt) {
		this.point = GeometryFactory.buildPoint(wkt);
	}

	public double getX() {
		return this.point.getX();
	}

	public double getY() {
		return this.point.getY();
	}

}
