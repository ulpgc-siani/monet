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
import org.monet.space.kernel.constants.Strings;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class BaseModelList<T extends BaseObject> extends BaseObject implements Iterable<T> {
	protected MonetHashMap<T> items;
	protected MonetHashMap<String> codes;
	protected String content;
	protected int totalCount;

	public BaseModelList() {
		this.items = new MonetHashMap<>();
		this.codes = new MonetHashMap<>();
		this.content = Strings.EMPTY;
		this.totalCount = -1;
	}

	private String generateId() {
		int id = this.items.size();
		while (this.items.containsKey(String.valueOf(id))) id++;
		return String.valueOf(id);
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean exist(String idCode) {
		return items.containsKey(idCode) || codes.containsKey(idCode);
	}

	@Override
	public Iterator<T> iterator() {
		return this.get().values().iterator();
	}

	public Iterator<Map.Entry<String, T>> entryIterator() {
		return this.get().entrySet().iterator();
	}

	public MonetHashMap<T> get() {
		return this.items;
	}

	public T get(String codeId) {
		MonetHashMap<T> items = this.get();
		if (items.containsKey(codeId)) return items.get(codeId);
		if (!this.codes.containsKey(codeId)) return null;
		return items.get(this.codes.get(codeId));
	}

	public T get(int index) {
		int currentIndex = 0;

		for (String key : items.keySet()) {
			if (currentIndex == index)
				return items.get(key);
			currentIndex++;
		}

		return null;
	}

	public void add(T baseModel) {
		String id, code;

		id = baseModel.getId();
		if ((id.equals(Strings.UNDEFINED_ID)) || (id.equals(Strings.EMPTY))) id = baseModel.getName();
		if (id.equals(Strings.UNDEFINED_KEY)) id = this.generateId();

		baseModel.setId(id);
		this.items.put(id, baseModel);

		code = baseModel.getCode();
		if ((code != null) && (!code.equals(Strings.UNDEFINED_ID))) this.codes.put(code, id);
	}

	public void insert(int index, T baseModel) {
		MonetHashMap<T> items = new MonetHashMap<>(this.get());
		int currentIndex = 0;
		boolean inserted = false;
		String id, code;

		id = baseModel.getId();
		if ((id.equals(Strings.UNDEFINED_ID)) || (id.equals(Strings.EMPTY))) id = baseModel.getName();
		if (id.equals(Strings.UNDEFINED_KEY)) id = this.generateId();

		baseModel.setId(id);
		this.clear();

		for (String key : items.keySet()) {
			if (currentIndex == index) {
				this.items.put(id, baseModel);
				inserted = true;
			}

			this.items.put(key, items.get(key));
		}

		if (!inserted)
			this.items.put(id, baseModel);

		code = baseModel.getCode();
		if ((code != null) && (!code.equals(Strings.UNDEFINED_ID))) this.codes.put(code, id);
	}

	public void delete(String codeId) {
		String id = null;

		if (this.items.containsKey(codeId)) id = codeId;
		if ((id == null) && (this.codes.containsKey(codeId))) id = (String) this.codes.get(codeId);
		if (id == null) return;

		this.items.remove(id);
		this.codes.remove(id);
	}

	public void delete(int index) {
		Set<String> keys = items.keySet();
		String keyToRemove = null;
		int currentIndex = 0;

		for (String key : keys) {
			if (currentIndex == index) {
				keyToRemove = key;
				break;
			}
			currentIndex++;
		}

		if (keyToRemove != null)
			this.items.remove(keyToRemove);
	}

	public void clear() {
		this.items.clear();
		this.codes.clear();
	}

	public int getCount() {
		return this.get().size();
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getTotalCount() {
		return this.totalCount;
	}

	@Override
	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {
	}

	public JSONObject toJson() {
		JSONObject result = new JSONObject();
		JSONArray itemsArray = new JSONArray();
		Boolean partialLoading = this.isPartialLoading();

		if (partialLoading) this.disablePartialLoading();

		result.put("content", this.getContent());

		for (T baseModel : this)
			itemsArray.add(baseModel.toJson());

		result.put("items", itemsArray);

		result.put("totalCount", this.totalCount);
		result.put("count", this.items.keySet().size());

		if (partialLoading) this.enablePartialLoading();

		return result;
	}

	public void fromJson(String data) throws ParseException {
	}

	@Override
	public void unserializeFromXML(Element element) {

	}


}
