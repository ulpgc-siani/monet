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

package org.monet.api.space.backservice.impl.model;

import org.jdom.Element;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.text.ParseException;
import java.util.Iterator;
import java.util.List;

public class IndicatorList extends BaseModelList<Indicator> {

	public IndicatorList() {
		super();
	}

	public void merge(IndicatorList indicatorList) {
		MonetHashMap<Indicator> indicators = indicatorList.get();
		Iterator<Indicator> iter = indicators.values().iterator();

		this.clear();
		while (iter.hasNext()) {
			Indicator indicator = iter.next();
			Indicator newIndicator = new Indicator(indicator.getCode(), indicator.getOrder(), indicator.getData());
			this.add(newIndicator);
		}
	}

	public void serializeToXML(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
		serializer.startTag("", "indicatorlist");
		for (Indicator indicator : this.items.values()) {
			indicator.serializeToXML(serializer);
		}
		serializer.endTag("", "indicatorlist");
	}

	@SuppressWarnings("unchecked")
	public void deserializeFromXML(Element indicatorList) throws ParseException {
		List<Element> indicators;
		Iterator<Element> iterator;

		if (indicatorList == null) return;

		indicators = indicatorList.getChildren("indicator");
		iterator = indicators.iterator();

		this.clear();
		while (iterator.hasNext()) {
			Indicator indicator = new Indicator();
			indicator.deserializeFromXML(iterator.next());
			this.add(indicator);
		}

	}

}
