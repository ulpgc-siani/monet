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

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.constants.LabelCode;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.library.LibraryDate;
import org.monet.space.kernel.model.DataRequest;
import org.monet.space.kernel.model.Revision;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.control.constants.Parameter;
import org.monet.space.office.core.constants.ErrorCode;
import org.monet.space.office.core.model.Language;
import org.monet.space.applications.library.LibraryRequest;

import java.util.HashMap;

public class ActionLoadNodeRevisionItems extends Action {
	private NodeLayer nodeLayer;

	public ActionLoadNodeRevisionItems() {
		super();
		this.nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
	}

	public String execute() {
		String idNode = LibraryRequest.getParameter(Parameter.ID, this.request);
		DataRequest dataRequest = new DataRequest();
		HashMap<String, String> parameters;
		int count;
		JSONObject result = new JSONObject();
		JSONArray rows = new JSONArray();

		if (!this.getFederationLayer().isLogged()) {
			return ErrorCode.USER_NOT_LOGGED;
		}

		if (idNode == null) {
			throw new DataException(ErrorCode.INCORRECT_PARAMETERS, Actions.LOAD_NODE_REVISION_ITEMS);
		}

		parameters = this.getRequestParameters();

		try {
			dataRequest.setCondition(new String(parameters.get(Parameter.CONDITION)));
		} catch (NullPointerException oException) {
			dataRequest.setCondition(Strings.EMPTY);
		} catch (ClassCastException oException) {
			dataRequest.setCondition(Strings.EMPTY);
		}

		try {
			dataRequest.setStartPos(Integer.valueOf(parameters.get(Parameter.START)));
		} catch (NumberFormatException oException) {
			dataRequest.setStartPos(0);
		} catch (ClassCastException oException) {
			dataRequest.setStartPos(0);
		}

		try {
			dataRequest.setLimit(Integer.valueOf(parameters.get(Parameter.LIMIT)));
		} catch (NumberFormatException oException) {
			dataRequest.setLimit(Strings.UNDEFINED_INT);
		} catch (ClassCastException oException) {
			dataRequest.setLimit(Strings.UNDEFINED_INT);
		}

		dataRequest.setSortsBy(this.getSortsBy(parameters.get(Parameter.SORTS_BY)));
		dataRequest.setGroupsBy(this.getGroupsBy(parameters.get(Parameter.GROUPS_BY)));
		dataRequest.setParameters(parameters);

		count = this.nodeLayer.requestRevisionListItemsCount(idNode, dataRequest);
		if (count > 0) {
			for (Revision revision : this.nodeLayer.requestRevisionListItems(idNode, dataRequest).values()) {
				JSONObject row = new JSONObject();
				String label = LibraryDate.getDateAndTimeString(revision.getRevisionDate(), Language.getCurrent(), Language.getCurrentTimeZone(), LibraryDate.Format.INTERNAL, true, "/");

				if (label == null || label.trim().isEmpty())
					label = Language.getInstance().getLabel(LabelCode.NO_LABEL);

				row.put("id", revision.getId());
				row.put("label", label);
				row.put("idNode", idNode);

				rows.add(row);
			}
		}

		result.put("nrows", count);
		result.put("rows", rows);

		return result.toString();
	}

}