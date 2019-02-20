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
import org.monet.metamodel.SourceDefinition;
import org.monet.space.office.core.model.Language;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.SourceLayer;
import org.monet.space.kernel.constants.LabelCode;
import org.monet.space.kernel.model.Source;
import org.monet.space.kernel.model.SourceList;
import org.monet.space.office.core.constants.ErrorCode;

public class ActionLoadSourceList extends Action {
	private SourceLayer sourceLayer;

	public ActionLoadSourceList() {
		super();
		this.sourceLayer = ComponentPersistence.getInstance().getSourceLayer();
	}

	public String execute() {
		SourceList sourceList;
		JSONObject result = new JSONObject();
		JSONArray rows = new JSONArray();

		if (!this.getFederationLayer().isLogged()) {
			return ErrorCode.USER_NOT_LOGGED;
		}

		sourceList = this.sourceLayer.loadSourceList();

		for (Source<SourceDefinition> source : sourceList.get().values()) {
			JSONObject row = new JSONObject();
			String label = source.getLabel();

			row.put("id", source.getId());
			row.put("codeSource", source.getCode());
			row.put("typeSource", source.getType());
			row.put("isEditable", source.isEditable());
			row.put("isEditableSource", source.isEditable());

			if (label == null || label.trim().isEmpty())
				label = Language.getInstance().getLabel(LabelCode.NO_LABEL);

			row.put("label", label);
			rows.add(row);
		}

		result.put("nrows", sourceList.getCount());
		result.put("rows", rows);

		return result.toString();
	}

}