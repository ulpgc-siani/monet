package org.monet.space.kernel.model.map;

import com.vividsolutions.jts.geom.*;
import org.monet.space.kernel.agents.AgentLogger;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class KMLWriter implements MapLayerWriter {

	private AgentLogger logger = AgentLogger.getInstance();
	private XmlSerializer serializer;
	private StringWriter writer;
	private OutputStream output;

	private static final Map<String, String> colorPalette = new HashMap<String, String>() {{
		put("red", "FF0000"); put("green", "00FF00"); put("blue", "0000FF"); put("yellow", "FFFF00"); put("pink", "FFC0CB"); put("red", "FF0000");
		put("salmon", "FA8072"); put("orange", "FFA500"); put("gold", "FFD700"); put("violet", "EE82EE"); put("fuchsia", "FF00FF"); put("magenta", "FF00FF");
		put("purple", "800080"); put("indigo", "4B0082"); put("lime", "00FF00"); put("aqua", "00FFFF"); put("cyan", "00FFFF"); put("navy", "000080");
		put("chocolate", "D2691E"); put("brown", "A52A2A"); put("maroon", "800000");
	}};

	public void init(OutputStream output) {
		try {
			this.serializer = XmlPullParserFactory.newInstance().newSerializer();
			if (output != null) {
				this.output = output;
				this.serializer.setOutput(output, "UTF-8");
			} else {
				this.writer = new StringWriter();
				this.serializer.setOutput(this.writer);
			}

			this.serializer.startDocument("UTF-8", false);
			this.serializer.startTag("", "kml");
			this.serializer.attribute("", "xmlns", "http://www.opengis.net/kml/2.2");
			this.serializer.attribute("", "xmlns:m", "http://www.monetproject.com/schemas/kmlextendeddata");
			this.serializer.startTag("", "Document");

		} catch (Exception e) {
			this.logger.error(e);
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public void close() {
		try {
			this.serializer.endTag("", "Document");
			this.serializer.endTag("", "kml");
			this.serializer.endDocument();
		} catch (Exception e) {
			this.logger.error(e);
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public String getResultLayer() {
		if (this.writer != null)
			return this.writer.toString();
		return null;
	}

	public void addLocation(Location location) {
		try {
			this.serializer.startTag("", "Placemark");

			this.serializer.startTag("", "name");
			if (location.getLabel() != null)
				this.serializer.text(location.getLabel());
			this.serializer.endTag("", "name");

			this.serializer.startTag("", "description");
			if (location.getDescription() != null)
				this.serializer.text(location.getDescription());
			this.serializer.endTag("", "description");

			writeStyle(location);
			writeExtendedData(location);

			boolean addCentroid = false;
			Geometry geometry = location.getGeometry();
			if (geometry instanceof Point) {
				this.writePoint((Point) geometry);
			} else if (geometry instanceof LineString) {
				this.writeLineString((LineString) geometry);
				addCentroid = true;
			} else if (geometry instanceof Polygon) {
				this.writePolygon((Polygon) geometry);
				addCentroid = true;
			} else if (geometry instanceof MultiPolygon) {
				this.writeMultiPolygon((MultiPolygon) geometry);
				addCentroid = true;
			}

			this.serializer.endTag("", "Placemark");

			if (addCentroid) {
				Location centroid = location.clone();
				centroid.setGeometry(geometry.getCentroid());
				this.addLocation(centroid);
			}
		} catch (Exception e) {
			this.logger.error(e);
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	private void writeStyle(Location location) throws IOException {
		String color = location.getColor();

		if (color == null)
			return;

		this.serializer.startTag("", "styleUrl");
		this.serializer.text("#color" + color);
		this.serializer.endTag("", "styleUrl");

		this.serializer.flush();
	}

	private void writeExtendedData(Location location) throws IOException {
		this.serializer.startTag("", "ExtendedData");

		this.serializer.startTag("", "m:id");
		this.serializer.text(location.getNodeId());
		this.serializer.endTag("", "m:id");

		this.serializer.flush();

		String metadata = location.getMetadata();
		if (metadata != null) {
			if (this.output != null) {
				this.output.flush();
				this.output.write(metadata.getBytes("UTF-8"));
				this.output.flush();
			} else {
				this.writer.flush();
				this.writer.append(metadata);
				this.writer.flush();
			}
		}

		this.serializer.endTag("", "ExtendedData");
	}

	private void writePoint(Point point) throws IllegalArgumentException, IllegalStateException, IOException {
		this.serializer.startTag("", "Point");
		this.serializer.startTag("", "coordinates");

		Coordinate coordinate = point.getCoordinate();
		StringBuilder builder = new StringBuilder();
		builder.append(coordinate.y);
		builder.append(",");
		builder.append(coordinate.x);
		this.serializer.text(builder.toString());

		this.serializer.endTag("", "coordinates");
		this.serializer.endTag("", "Point");
	}

	private void writeLineString(LineString linestring) throws IllegalArgumentException, IllegalStateException, IOException {
		this.serializer.startTag("", "LineString");
		this.serializer.startTag("", "extrude");
		this.serializer.text("0");
		this.serializer.endTag("", "extrude");
		this.serializer.startTag("", "tessellate");
		this.serializer.text("1");
		this.serializer.endTag("", "tessellate");
		this.serializer.startTag("", "coordinates");

		StringBuilder builder = new StringBuilder();

		int numPoints = linestring.getNumPoints();
		for (int i = 0; i < numPoints; i++) {
			Coordinate coordinate = linestring.getPointN(i).getCoordinate();
			builder.append(coordinate.y);
			builder.append(",");
			builder.append(coordinate.x);
			builder.append(" ");
		}
		this.serializer.text(builder.toString());

		this.serializer.endTag("", "coordinates");
		this.serializer.endTag("", "LineString");
	}

	private void writeLinearRing(LineString linestring) throws IllegalArgumentException, IllegalStateException, IOException {
		this.serializer.startTag("", "LinearRing");
		this.serializer.startTag("", "coordinates");

		StringBuilder builder = new StringBuilder();

		int numPoints = linestring.getNumPoints();
		for (int i = 0; i < numPoints; i++) {
			Coordinate coordinate = linestring.getPointN(i).getCoordinate();
			builder.append(coordinate.y);
			builder.append(",");
			builder.append(coordinate.x);
			builder.append(" ");
		}
		this.serializer.text(builder.toString());

		this.serializer.endTag("", "coordinates");
		this.serializer.endTag("", "LinearRing");
	}

	private void writePolygon(Polygon polygon) throws IllegalArgumentException, IllegalStateException, IOException {
		this.serializer.startTag("", "Polygon");
		this.serializer.startTag("", "extrude");
		this.serializer.text("0");
		this.serializer.endTag("", "extrude");
		this.serializer.startTag("", "altitudeMode");
		this.serializer.text("clampToGround");
		this.serializer.endTag("", "altitudeMode");

		if (polygon.getExteriorRing() != null) {
			this.serializer.startTag("", "outerBoundaryIs");
			this.writeLinearRing(polygon.getExteriorRing());
			this.serializer.endTag("", "outerBoundaryIs");
		}

		int interiorRings = polygon.getNumInteriorRing();
		for (int i = 0; i < interiorRings; i++) {
			this.serializer.startTag("", "innerBoundaryIs");
			this.writeLinearRing(polygon.getInteriorRingN(i));
			this.serializer.endTag("", "innerBoundaryIs");
		}
		this.serializer.endTag("", "Polygon");
	}

	private void writeMultiPolygon(MultiPolygon multiPoligon) throws IllegalArgumentException, IllegalStateException, IOException {
		this.serializer.startTag("", "multigeometry");
		int count = multiPoligon.getNumGeometries();

		for (int i=0; i<count; i++)
			writePolygon((com.vividsolutions.jts.geom.Polygon) multiPoligon.getGeometryN(i));

		this.serializer.endTag("", "multigeometry");
	}

	private void addStyle(Location location) {

		try {
			String color = location.getColor();

			if (color == null)
				return;

			String hexColor = toHexColor(color);

			this.serializer.startTag("", "Style");
			this.serializer.attribute("", "id", "color" + color);

			this.serializer.startTag("", "IconStyle");

			this.serializer.startTag("", "Icon");
			this.serializer.startTag("", "href");
			this.serializer.text("http://chart.apis.google.com/chart?cht=mm&chs=30x30&chco=" + hexColor + "," + hexColor + ",333333&ext=.png");
			this.serializer.endTag("", "href");
			this.serializer.endTag("", "Icon");

			this.serializer.endTag("", "IconStyle");

			this.serializer.endTag("", "Style");

			this.serializer.flush();
		} catch (Exception e) {
			this.logger.error(e);
			throw new RuntimeException(e.getMessage(), e);
		}

	}

	private String toHexColor(String color) {
		String colorKey = color.replace("#", "").toLowerCase();

		if (colorPalette.containsKey(colorKey))
			return colorPalette.get(colorKey).toLowerCase();

		return colorKey;
	}

	public void addLocationList(LocationList locationList) {
		for (Location location : locationList)
			addStyle(location);

		for (Location location : locationList)
			this.addLocation(location);
	}

}