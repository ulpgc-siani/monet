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

import net.minidev.json.JSONObject;
import org.monet.space.kernel.constants.Common;

import java.util.HashMap;
import java.util.Iterator;

public class NodeItem {
	private String definitionCode = null;
	private HashMap<String, String> attributes;

	public NodeItem() {
		this.attributes = new HashMap<>();
	}

	public Boolean addAttribute(String code, String value) {

		if (code.equals(Common.DataStoreField.CODE)) {
			this.definitionCode = value;
			return true;
		}

		if (code.equals(Common.DataStoreField.VALUE)) return true;
		if (code.equals(Common.DataStoreField.BODY)) return true;

		if (code.equals("id_node")) this.attributes.put(Common.DataStoreField.CODE, value);
		if (code.equals("label")) this.attributes.put(Common.DataStoreField.VALUE, value);
		if (code.equals("description")) this.attributes.put(Common.DataStoreField.BODY, value);

		this.attributes.put(code, value);

		return true;
	}

	public String getDefinitionCode() {
		return definitionCode;
	}

	public String getCode() {
		if (!this.attributes.containsKey(Common.DataStoreField.CODE)) return "";
		return this.attributes.get(Common.DataStoreField.CODE);
	}

	public String getAttribute(String code) {
		if (!this.attributes.containsKey(code)) return "";
		return this.attributes.get(code);
	}

	public JSONObject toJson() {
		JSONObject result = new JSONObject();
		Iterator<String> iter = this.attributes.keySet().iterator();

		while (iter.hasNext()) {
			String code = iter.next();
			result.put(code, this.attributes.get(code));
		}

		return result;
	}

}
