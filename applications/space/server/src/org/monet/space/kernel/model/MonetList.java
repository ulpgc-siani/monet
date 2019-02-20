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
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.monet.space.kernel.constants.ErrorCode;
import org.monet.space.kernel.exceptions.DataException;

import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;

public class MonetList<T extends BaseObject> extends LinkedList<T> {
	private static final long serialVersionUID = 1L;
	private int totalCount = 0;

	public MonetList() {
		this.totalCount = 0;
	}

	public MonetList(MonetList<T> list) {
		for (T item : list)
			this.add(item);
	}

	public LinkedList<T> getAll() {
		return this;
	}

	public T getFirst() {
		if (this.size() <= 0)
			return null;

		return this.get(0);
	}

	public JSONObject toJson() {
		JSONObject result = new JSONObject();
		JSONArray items = new JSONArray();

		for (T item : this) {
			items.add(item.toJson());
		}

		result.put("nrows", String.valueOf(this.getTotalCount()));
		result.put("rows", items);

		return result;
	}

	public void fromJson(String content) throws ParseException {
	}

	public void deserializeFromXML(String content, boolean resolveModelChanges) {
		SAXBuilder oBuilder = new SAXBuilder();
		StringReader oReader;
		org.jdom.Document oDocument;
		Element node;
		try {
			oReader = new StringReader(content);
			oDocument = oBuilder.build(oReader);
			node = oDocument.getRootElement();
			this.deserializeFromXML(node);
		} catch (JDOMException oException) {
			throw new DataException(ErrorCode.UNSERIALIZE_NODE, content, oException);
		} catch (IOException oException) {
			throw new DataException(ErrorCode.UNSERIALIZE_NODE, content, oException);
		}
	}

	public void deserializeFromXML(Element object) {
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

}
