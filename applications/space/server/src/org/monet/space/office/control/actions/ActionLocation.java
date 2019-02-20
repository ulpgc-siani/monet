/*
    Monet will assist business to process re-engineering. Monet separate the
    business logic from the underlying technology to allow Model-Driven
    Engineering (MDE). These models guide all the development process over a
    Service Oriented Architecture (SOA).

    Copyright (C) 2009  Grupo de Ingenieria del Sofware y Sistemas de la Universidad de Las Palmas de Gran Canaria

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see http://www.gnu.org/licenses/.
 */

package org.monet.space.office.control.actions;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;
import org.monet.space.applications.library.LibraryRequest;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.library.LibraryMapLayer;
import org.monet.space.kernel.library.LibraryMapLayer.MapLayerFormat;
import org.monet.space.kernel.model.map.GeometryHelper;
import org.monet.space.kernel.model.map.LocationList;
import org.monet.space.kernel.model.map.MapLayerWriter;

public abstract class ActionLocation extends Action {
	protected NodeLayer nodeLayer;

	public ActionLocation() {
		super();
		this.nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
	}

	protected int startPos() {
		try {
			return Integer.valueOf(LibraryRequest.getParameter("start", this.request));
		} catch (NumberFormatException oException) {
			return 0;
		}
	}

	protected int limit() {
		try {
			return Integer.valueOf(LibraryRequest.getParameter("limit", this.request));
		} catch (NumberFormatException oException) {
			return 0;
		}
	}

	protected Polygon getBoundingBox() {
		String nex = LibraryRequest.getParameter("nex", this.request);
		String ney = LibraryRequest.getParameter("ney", this.request);
		String swx = LibraryRequest.getParameter("swx", this.request);
		String swy = LibraryRequest.getParameter("swy", this.request);

		Polygon boundingBox = null;

		if (nex != null) {
			double dnex = Double.parseDouble(nex);
			double dney = Double.parseDouble(ney);
			double dswx = Double.parseDouble(swx);
			double dswy = Double.parseDouble(swy);

			Coordinate[] coordinates = new Coordinate[5];
			coordinates[0] = new Coordinate(dswx, dney);
			coordinates[1] = new Coordinate(dnex, dney);
			coordinates[2] = new Coordinate(dnex, dswy);
			coordinates[3] = new Coordinate(dswx, dswy);
			coordinates[4] = new Coordinate(dswx, dney);
			CoordinateArraySequence sequence = new CoordinateArraySequence(coordinates);
			LinearRing shell = new LinearRing(sequence, GeometryHelper.getFactory());
			boundingBox = new Polygon(shell, null, GeometryHelper.getFactory());
		}
		return boundingBox;
	}

	protected String printKmlLayer(LocationList locationList) {
		return printLayer(MapLayerFormat.KML, locationList);
	}

	protected String printGoogleChartDataLayer(LocationList locationList) {
		return printLayer(MapLayerFormat.GOOGLE_CHART_DATA, locationList);
	}

	protected String printLayer(MapLayerFormat format, LocationList locationList) {
		MapLayerWriter writer = LibraryMapLayer.getInstance().getWriter(format);
		writer.addLocationList(locationList);
		writer.close();

		return writer.getResultLayer();
	}
}