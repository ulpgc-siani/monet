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

import org.monet.space.applications.library.LibraryRequest;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.DataRequest;
import org.monet.space.kernel.model.NodeDataRequest;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;

import static java.util.Collections.singletonList;

public class ActionLoadLinkNodeItems extends Action {
	private NodeLayer nodeLayer;

	public ActionLoadLinkNodeItems() {
		super();
		this.nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
	}

	public String execute() {
		String domainCode = LibraryRequest.getParameter(Parameter.DOMAIN, this.request);
		String codeNode = LibraryRequest.getParameter(Parameter.CODE, this.request);
		String sorting = LibraryRequest.getParameter(Parameter.SORTING, this.request);
		String sortingMode = LibraryRequest.getParameter(Parameter.SORTING_MODE, this.request);

		if (!this.getFederationLayer().isLogged())
			return ErrorCode.USER_NOT_LOGGED;

		if (codeNode == null)
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.LOAD_LINK_NODE_ITEMS);

		NodeDataRequest dataRequest = createNodeDataRequest(codeNode, domainCode);
		if (sorting != null && !sorting.isEmpty()) dataRequest.setSortsBy(singletonList(sortByOf(sorting, sortingMode)));
		return this.nodeLayer.searchLinkNodeItems(dataRequest).toJson().toString();
	}

	private DataRequest.SortBy sortByOf(final String sorting, final String sortingMode) {
		return new DataRequest.SortBy() {
			@Override
			public String attribute() {
				return sorting;
			}

			@Override
			public String mode() {
				return sortingMode;
			}
		};
	}

}