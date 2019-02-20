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
import org.json.simple.JSONObject;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.model.Language;
import org.monet.space.office.core.constants.ErrorCode;
import org.monet.space.office.presentation.user.renders.TaskListPrintRender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ActionLoadTaskListPrintAttributes extends Action {
	private NodeLayer nodeLayer;

	public ActionLoadTaskListPrintAttributes() {
		super();
		this.nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
	}

	public String execute() {

		if (!this.getFederationLayer().isLogged())
			return ErrorCode.USER_NOT_LOGGED;

		List<Attribute> attributeList = getAttributeList();
		return toJson(attributeList).toString();
	}

	private List<Attribute> getAttributeList() {
		List<Attribute> attributeList = new ArrayList<>();
		int pos = 0;
		for (final String attribute : TaskListPrintRender.attributes) {
			final int finalPos = pos;
			attributeList.add(new Attribute() {
				@Override
				public String getCode() {
					return attribute;
				}

				@Override
				public String getLabel() {
					return TaskListPrintRender.attributesLabels.get(Language.getCurrent()).get(finalPos);
				}

				@Override
				public String getType() {
					return TaskListPrintRender.attributesTypes.get(finalPos);
				}
			});

			pos++;
		}

		Collections.sort(attributeList, new Comparator<Attribute>() {
			@Override
			public int compare(Attribute o1, Attribute o2) {
				return o1.getLabel().compareTo(o2.getLabel());
			}
		});
		
		return attributeList;
	}

	private JSONArray toJson(List<Attribute> attributes) {
		JSONArray result = new JSONArray();

		for (Attribute attribute : attributes) {
			JSONObject item = new JSONObject();
			item.put("code", attribute.getCode());
			item.put("label", attribute.getLabel());
			item.put("type", attribute.getType());
			result.add(item);
		}

		return result;
	}

	public interface Attribute {
		String getCode();
		String getLabel();
		String getType();
	}

}