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

package org.monet.space.kernel.library;

import net.minidev.json.JSONArray;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

import java.util.Map;

public class LibraryJSON {
	private static LibraryJSON oInstance;

	private LibraryJSON() {
	}

	public synchronized static LibraryJSON getInstance() {
		if (oInstance == null) oInstance = new LibraryJSON();
		return oInstance;
	}

	public static Boolean isArray(String data) {
		try {
			return new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(data) instanceof JSONArray;
		} catch (ParseException e) {
			return false;
		}
	}

	public static String serializeMap(Map<String, String> map) {
		String result = "";

		for (String key : map.keySet()) {
			String value = map.get(key);
			result += "\"" + key + "\":\"" + value + "\",";
		}
		if (result.length() > 0) result = result.substring(0, result.length() - 1);

		return "{" + result + "}";
	}
}
