package org.monet.bpi.types.location;

import com.vividsolutions.jts.geom.LineString;

public class Line extends Geometry {

	protected LineString line;

	Line(LineString g) {
		this.geometry = g;
		this.line = g;
	}

	public Line(Point[] points) {
		this(GeometryFactory.buildLineString(points));
	}

	public Line(String wkt) {
		this(GeometryFactory.buildLineString(wkt));
	}

	public Point getPoint(int index) {
		return (Point) GeometryFactory.build(this.line.getPointN(index));
	}

	public int getPointsCount() {
		return this.line.getNumPoints();
	}

	public double length() {
		return this.line.getLength();
	}

	public boolean isClosed() {
		return this.line.isClosed();
	}

	public boolean isRing() {
		return this.line.isRing();
	}

	public Line reverse() {
		return (Line) GeometryFactory.build(this.line.reverse());
	}

}
