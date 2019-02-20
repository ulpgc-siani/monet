package org.monet.space.kernel.model.map;

import com.vividsolutions.jts.geom.*;
import org.monet.space.kernel.agents.AgentLogger;

import java.io.IOException;
import java.io.OutputStream;

public class GoogleChartDataWriter implements MapLayerWriter {

	private AgentLogger logger = AgentLogger.getInstance();
	private int coordinateCount = 0;
	private StringBuilder writer;
	private OutputStream output;

	public void init(OutputStream output) {
		this.writer = new StringBuilder();
		this.output = output;
	}

	public void close() {
		try {
			if (output != null)
				output.write(writer.toString().getBytes());
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
			boolean addCentroid = false;
			Geometry geometry = location.getGeometry();
			if (geometry instanceof Point) {
				this.writePoint((Point) geometry);
			} else if (geometry instanceof LineString) {
				addCentroid = true;
			} else if (geometry instanceof Polygon) {
				addCentroid = true;
			} else if (geometry instanceof MultiPolygon) {
				addCentroid = true;
			}

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

	private void writePoint(Point point) throws IllegalArgumentException, IllegalStateException, IOException {
		writeCoordinate(point.getCoordinate());
	}

	private void writeCoordinate(Coordinate coordinate) throws IllegalArgumentException, IllegalStateException, IOException {
		boolean addComma = coordinateCount > 0;
		writer.append(String.format("%s new google.maps.LatLng(%s,%s)", addComma ? "," : "", coordinate.x, coordinate.y));
		coordinateCount++;
	}

	private void writeLineString(LineString linestring) throws IllegalArgumentException, IllegalStateException, IOException {
		int numPoints = linestring.getNumPoints();
		for (int i = 0; i < numPoints; i++)
			writeCoordinate(linestring.getPointN(i).getCoordinate());
	}

	private void writeLinearRing(LineString linestring) throws IllegalArgumentException, IllegalStateException, IOException {
		int numPoints = linestring.getNumPoints();
		for (int i = 0; i < numPoints; i++)
			writeCoordinate(linestring.getPointN(i).getCoordinate());
	}

	private void writePolygon(Polygon polygon) throws IllegalArgumentException, IllegalStateException, IOException {

		if (polygon.getExteriorRing() != null)
			this.writeLinearRing(polygon.getExteriorRing());

		int interiorRings = polygon.getNumInteriorRing();
		for (int i = 0; i < interiorRings; i++)
			this.writeLinearRing(polygon.getInteriorRingN(i));
	}

	private void writeMultiPolygon(MultiPolygon polygon) throws IllegalArgumentException, IllegalStateException, IOException {
		int previousCoordinateCount = coordinateCount;
		boolean addComma = coordinateCount > 0;
		writer.append(String.format("%s [", addComma ? "," : ""));

		int numPolygons = polygon.getNumGeometries();
		coordinateCount = 0;
		for(int i=0; i<numPolygons; i++) {
			if (i!=0) writer.append(",");
			writePolygon((Polygon) polygon.getGeometryN(i));
		}
		coordinateCount = previousCoordinateCount;

		writer.append("]");
	}

	public void addLocationList(LocationList locationList) {
		writer.append("[");

		for (Location location : locationList)
			this.addLocation(location);

		writer.append("]");
	}
}