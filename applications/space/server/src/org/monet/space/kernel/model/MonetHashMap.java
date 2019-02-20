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
import net.minidev.json.JSONObject;
import net.minidev.json.parser.ParseException;

import java.util.Iterator;
import java.util.LinkedHashMap;

public class MonetHashMap<T> extends LinkedHashMap<String, T> {
	private static final long serialVersionUID = 1L;
	private int totalCount = 0;

	public MonetHashMap() {
	}

	public MonetHashMap(MonetHashMap<T> map) {
		for (String key : map.keySet()) {
			this.put(key, map.get(key));
		}
	}

	public T getFirst() {
		Iterator<T> iterator = this.values().iterator();

		if (!iterator.hasNext())
			return null;

		return iterator.next();
	}

	public T getLast() {
		Iterator<T> iterator = this.values().iterator();
		T result = null;

		while (iterator.hasNext())
			result = iterator.next();

		return result;
	}

	public JSONObject toJson() {
		JSONObject result = new JSONObject();
		JSONArray items = new JSONArray();

		for (T item : this.values()) {
			if (item instanceof BaseObject)
				items.add(((BaseObject) item).toJson());
		}

		result.put("nrows", String.valueOf(this.getTotalCount()));
		result.put("rows", items);

		return result;
	}

	public void fromJson(String content) throws ParseException {
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
}
