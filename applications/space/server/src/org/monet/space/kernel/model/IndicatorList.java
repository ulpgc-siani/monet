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
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.monet.space.kernel.constants.ErrorCode;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.DataException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

public class IndicatorList extends BaseModelList<Indicator> {

	public IndicatorList() {
		super();
	}

	public IndicatorList(IndicatorList indicatorList) {
		this();
		for (Indicator indicator : indicatorList)
			this.add(new Indicator(indicator));
	}

	public void merge(IndicatorList indicatorList) {
		this.merge(indicatorList, false);
	}

	@SuppressWarnings("unchecked")
	public void merge(IndicatorList indicatorList, boolean onlyNotEmpty) {
		MonetHashMap<Indicator> indicators = indicatorList.get();
		synchronized (indicators) {
			Iterator<Indicator> iter = indicators.values().iterator();

			if (!iter.hasNext() && onlyNotEmpty)
				return;

			MonetHashMap<Indicator> items = (MonetHashMap<Indicator>) this.items.clone();
			MonetHashMap<String> codes = (MonetHashMap<String>) this.codes.clone();

			this.clear();
			while (iter.hasNext()) {
				Indicator indicator = iter.next();
				if (indicator.getData() == null || indicator.getData().isEmpty()) {
					if (codes.get(indicator.getCode()) != null) {
						if (onlyNotEmpty)
							this.add(items.get(codes.get(indicator.getCode())));
						continue;
					}
				}
				Indicator newIndicator = new Indicator(indicator.getCode(), indicator.getOrder(), indicator.getData());
				this.add(newIndicator);
			}
		}
	}

	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {
		serializer.startTag("", "indicatorlist");
		for (Indicator indicator : this.items.values()) {
			indicator.serializeToXML(serializer, depth);
		}
		serializer.endTag("", "indicatorlist");
	}

	public void deserializeFromXML(String content) {
		SAXBuilder builder = new SAXBuilder();
		StringReader reader;
		org.jdom.Document document;
		Element node;

		if (content.equals(Strings.EMPTY)) return;

		reader = new StringReader(content);

		this.clear();

		try {
			document = builder.build(reader);
			node = document.getRootElement();
			this.unserializeFromXML(node);
		} catch (JDOMException exception) {
			throw new DataException(ErrorCode.UNSERIALIZE_DEFINITION_FROM_XML, content, exception);
		} catch (IOException exception) {
			throw new DataException(ErrorCode.UNSERIALIZE_DEFINITION_FROM_XML, content, exception);
		}

	}

	@SuppressWarnings("unchecked")
	public void unserializeFromXML(Element indicatorList) {
		List<Element> indicators;
		Iterator<Element> iterator;

		if (indicatorList == null) return;

		indicators = indicatorList.getChildren("indicator");
		iterator = indicators.iterator();

		this.clear();

		while (iterator.hasNext()) {
			Indicator indicator = new Indicator();
			indicator.unserializeFromXML(iterator.next());
			this.add(indicator);
		}

	}

	public void fromJson(String data) throws ParseException {
		JSONArray jsonData;
		Integer pos;

		this.clear();

		jsonData = (JSONArray) new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(content);
		pos = 0;

		while (pos < jsonData.size()) {
			JSONObject element = (JSONObject) jsonData.get(pos);
			Indicator indicator = new Indicator();
			indicator.fromJson(element.toString());
			this.add(indicator);
			pos++;
		}

	}

}
