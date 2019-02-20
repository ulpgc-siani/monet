package org.monet.bpi.types.location;

public abstract class Geometry {

	protected com.vividsolutions.jts.geom.Geometry geometry;

	public boolean contains(Geometry g) {
		return this.geometry.contains(g.geometry);
	}

	public boolean within(Geometry g) {
		return this.geometry.within(g.geometry);
	}

	public boolean covers(Geometry g) {
		return this.geometry.covers(g.geometry);
	}

	public boolean crosses(Geometry g) {
		return this.geometry.crosses(g.geometry);
	}

	public double distance(Geometry g) {
		return this.geometry.distance(g.geometry);
	}

	public boolean equals(Geometry g) {
		return this.geometry.equalsExact(g.geometry);
	}

	public boolean equals(Geometry g, double tolerance) {
		return this.geometry.equalsExact(g.geometry, tolerance);
	}

	public double getArea() {
		return this.geometry.getArea();
	}

	public Point getCentroid() {
		return (Point) GeometryFactory.build(this.geometry.getCentroid());
	}

	public Geometry getBoundingBox() {
		return GeometryFactory.build(this.geometry.getEnvelope());
	}

	public boolean isWithinDistance(Geometry geom, double distance) {
		return this.geometry.isWithinDistance(geom.geometry, distance);
	}

	public boolean touches(Geometry g) {
		return this.geometry.touches(g.geometry);
	}

	public Geometry union(Geometry g) {
		return GeometryFactory.build(this.geometry.union(g.geometry));
	}

	public String toString() {
		return this.geometry.toText();
	}

}
