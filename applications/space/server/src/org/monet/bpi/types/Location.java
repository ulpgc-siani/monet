package org.monet.bpi.types;

import org.monet.bpi.types.location.Geometry;
import org.monet.bpi.types.location.GeometryFactory;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;
import org.simpleframework.xml.core.Commit;

public class Location {

	@Attribute(required = false)
	private java.util.Date timestamp;

	@Attribute(required = false)
	private String label;

	@Text
	private String wkt;

	private Geometry geometry;

	@Commit
	public void commit() {
		this.geometry = GeometryFactory.build(this.wkt);
	}

	public java.util.Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(java.util.Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getWkt() {
		return wkt;
	}

	public void setWkt(String wkt) {
		this.wkt = wkt;
		this.geometry = GeometryFactory.build(this.wkt);
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
		this.wkt = this.geometry.toString();
	}

	@Override
	public String toString() {
		return this.label;
	}

}
