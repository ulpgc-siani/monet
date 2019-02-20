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

package org.monet.space.kernel.model;

import net.minidev.json.JSONArray;
import org.monet.metamodel.Definition;

import java.util.ArrayList;
import java.util.HashMap;

public class DefinitionList<T extends Definition> extends ArrayList<T> {

	private static final long serialVersionUID = 5319840392316633712L;

	public DefinitionList() {
	}

	public boolean addAll(HashMap<String, T> items) {
		this.clear();
		super.addAll(items.values());

		return true;
	}

	public JSONArray toJson() {
		JSONArray jsonArray = new JSONArray();

		for (T item : this)
			jsonArray.add(item.serializeToJSON());

		return jsonArray;
	}

}