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

import com.vividsolutions.jts.geom.Polygon;
import org.monet.metamodel.IndexDefinition;
import org.monet.metamodel.SetDefinition;
import org.monet.metamodel.SetDefinition.SetViewProperty;
import org.monet.metamodel.SetDefinitionBase.SetViewPropertyBase.ShowProperty.LocationsProperty.LayerEnumeration;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.library.LibraryMapLayer.MapLayerFormat;
import org.monet.space.kernel.model.Dictionary;
import org.monet.space.kernel.model.Node;
import org.monet.space.kernel.model.map.LocationList;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;
import org.monet.space.office.core.model.Language;
import org.monet.space.applications.library.LibraryRequest;

public class ActionLoadLocationLayer extends ActionLocation {

	public ActionLoadLocationLayer() {
		super();
	}

	public String execute() {
		String id = LibraryRequest.getParameter(Parameter.ID, this.request);
		String codeView = LibraryRequest.getParameter(Parameter.VIEW, this.request);
		LayerEnumeration layer = null;

		Polygon boundingBox = getBoundingBox();

		if (!this.getFederationLayer().isLogged()) {
			return ErrorCode.USER_NOT_LOGGED;
		}

		Node node = this.nodeLayer.loadNode(id);

		if (!this.componentSecurity.canRead(node, this.getAccount()))
			return ErrorCode.READ_NODE_PERMISSIONS + Strings.COLON + Strings.SPACE + Language.getInstance().getErrorMessage(ErrorCode.READ_NODE_PERMISSIONS);

		LocationList locationList = new LocationList();

		if (node.isSet()) {
			SetDefinition definition = (SetDefinition) node.getDefinition();
			SetViewProperty view = (SetViewProperty) definition.getNodeView(codeView);
			IndexDefinition indexDefinition = Dictionary.getInstance().getIndexDefinition(definition.getIndex().getValue());

			layer = view.getShow().getLocations().getLayer();

			if (view.getShow().getLocations() != null)
				locationList = this.nodeLayer.loadLocationsInNode(node, null, boundingBox, indexDefinition.getCode());
		}

		return printLayer(calculateFormat(layer), locationList);
	}

	private MapLayerFormat calculateFormat(LayerEnumeration layer) {
		if (layer != null && layer == LayerEnumeration.HEAT)
			return MapLayerFormat.GOOGLE_CHART_DATA;

		return MapLayerFormat.KML;
	}

}