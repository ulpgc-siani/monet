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

import java.util.Iterator;
import java.util.LinkedHashMap;

public class NodeItemList {
	private LinkedHashMap<String, NodeItem> items;
	private Integer totalCount;

	public NodeItemList() {
		this(0);
	}

	public NodeItemList(Integer totalCount) {
		this.items = new LinkedHashMap<>();
		this.totalCount = totalCount;
	}

	public LinkedHashMap<String, NodeItem> get() {
		return this.items;
	}

	public NodeItem get(String code) {
		if (!this.items.containsKey(code)) return null;
		return this.items.get(code);
	}

	public Boolean add(NodeItem item) {
		if (this.items.containsKey(item.getCode())) return true;
		this.items.put(item.getCode(), item);
		return true;
	}

	public int getTotalCount() {
		return this.totalCount;
	}

	public Boolean setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
		return true;
	}

	public NodeItem first() {
		Iterator<NodeItem> iter = this.items.values().iterator();
		if (!iter.hasNext()) return null;
		return iter.next();
	}

	public NodeItemList subList(Integer startPos, Integer limit) {
		NodeItemList result = new NodeItemList(this.totalCount);
		Iterator<String> iter = this.items.keySet().iterator();
		Integer pos = 0;
		Integer count = 0;

		while ((count < limit) && (iter.hasNext())) {
			String codeItem = iter.next();
			pos++;
			if (pos >= startPos) {
				result.add(this.items.get(codeItem));
				count++;
			}
		}

		return result;
	}

	public NodeItemList subList(Integer startPos) {
		return this.subList(startPos, this.items.size());
	}

	public JSONObject toJson() {
		Iterator<String> iter = this.items.keySet().iterator();
		JSONObject result = new JSONObject();
		JSONArray jsonItems = new JSONArray();

		while (iter.hasNext()) {
			NodeItem item = this.items.get(iter.next());
			jsonItems.add(item.toJson());
		}

		result.put("nrows", String.valueOf(this.totalCount));
		result.put("rows", jsonItems);

		return result;
	}

}
