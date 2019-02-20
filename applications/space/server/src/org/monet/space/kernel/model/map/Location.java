package org.monet.space.kernel.model.map;

import com.vividsolutions.jts.geom.*;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.library.LibraryDate;
import org.monet.space.kernel.model.BaseObject;
import org.monet.space.kernel.model.Language;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Location extends BaseObject {

	public static enum TYPE {
		POINT, LINESTRING, POLYGON, MULTIPOLYGON
	}

	private String label;
	private String description;
	private String color;
	private String nodeId;
	private String locationId;
	private String fieldCode;
	private String metadata;
	private Geometry geometry;
	private HashMap<String, String> attributes = new HashMap<String, String>();
	private Date createDate;

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public String getFieldCode() {
		return fieldCode;
	}

	public void setFieldCode(String fieldCode) {
		this.fieldCode = fieldCode;
	}

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	public String getCreateDate() {
		return LibraryDate.getDateAndTimeString(this.createDate, Language.getCurrent(), Language.getCurrentTimeZone(), LibraryDate.Format.DEFAULT, true, Strings.BAR45);
	}

	public Date getInternalCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Override
	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {
		DateFormat dateFormat = new SimpleDateFormat("yyyyy-MM-dd HH:mm:ss.S z");

		serializer.startTag("", "location");

		serializer.attribute("", "label", getLabel());
		serializer.attribute("", "wkt", geometry.toString());
		serializer.attribute("", "createDate", dateFormat.format(getInternalCreateDate()));

		serializer.endTag("", "location");
	}

	public JSONObject toJson() {
		JSONObject location = new JSONObject();
		TYPE type = null;
		JSONArray geometry = null;
		if (this.geometry instanceof Point) {
			type = TYPE.POINT;
			Point point = (Point) this.geometry;
			geometry = this.serializePoint(point);
		} else if (this.geometry instanceof LineString) {
			type = TYPE.LINESTRING;
			LineString lineString = (LineString) this.geometry;
			geometry = this.serializeLineString(lineString);
		} else if (this.geometry instanceof Polygon) {
			type = TYPE.POLYGON;

			Polygon polygon = (Polygon) this.geometry;
			geometry = this.serializePolygon(polygon);
		} else if (this.geometry instanceof MultiPolygon) {
			type = TYPE.MULTIPOLYGON;

			MultiPolygon polygon = (MultiPolygon) this.geometry;
			geometry = this.serializeMultiPolygon(polygon);
		}
		location.put("type", type.toString());
		location.put("geometry", geometry);
		location.put("zoom", 15);
		location.put("center", this.serializePoint(this.geometry.getCentroid()));
		return location;
	}

	private JSONArray serializePolygon(Polygon polygon) {
		JSONArray jsonPolygon = new JSONArray();
		LineString exteriorRing = polygon.getExteriorRing();
		jsonPolygon.add(this.serializeLineString(exteriorRing));

		int interiorRingLenght = polygon.getNumInteriorRing();
		for (int i = 0; i < interiorRingLenght; i++) {
			LineString ring = polygon.getInteriorRingN(i);
			jsonPolygon.add(this.serializeLineString(ring));
		}

		return jsonPolygon;
	}

	private JSONArray serializeMultiPolygon(MultiPolygon multiPolygon) {
		JSONArray jsonMultiPolygon = new JSONArray();

		int countPolygons = multiPolygon.getNumGeometries();
		for (int i = 0; i < countPolygons; i++) {
			Polygon polygon = (Polygon) multiPolygon.getGeometryN(i);
			jsonMultiPolygon.add(this.serializePolygon(polygon));
		}

		return jsonMultiPolygon;
	}

	private JSONArray serializeLineString(LineString lineString) {
		JSONArray jsonLineString = new JSONArray();
		int pointsLength = lineString.getNumPoints();
		for (int i = 0; i < pointsLength; i++) {
			Point point = lineString.getPointN(i);
			jsonLineString.add(this.serializePoint(point));
		}
		return jsonLineString;
	}

	private JSONArray serializePoint(Point point) {
		JSONArray jsonPoint = new JSONArray();
		Coordinate coord = point.getCoordinate();
		jsonPoint.add(coord.x);
		jsonPoint.add(coord.y);
		return jsonPoint;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getMetadata() {
		return metadata;
	}

	public void setMetadata(String metadata) {
		this.metadata = metadata;
	}

	public Map<String, String> getAttributes() {
		return this.attributes;
	}

	public void setAttributes(HashMap<String, String> attributes) {
		this.attributes = attributes;
	}

	public Location clone() {
		Location location = new Location();

		location.code = this.code;
		location.id = this.id;
		location.name = this.name;
		location.label = this.label;
		location.description = this.description;
		location.nodeId = this.nodeId;
		location.locationId = this.locationId;
		location.fieldCode = this.fieldCode;
		location.metadata = this.metadata;
		location.geometry = this.geometry;

		for (Map.Entry<String, String> entry : this.attributes.entrySet())
			location.attributes.put(entry.getKey(), entry.getValue());

		location.createDate = this.createDate;

		return location;
	}

}
