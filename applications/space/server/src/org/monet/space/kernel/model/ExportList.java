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

import com.google.inject.Injector;
import org.jdom.Element;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.constants.ErrorCode;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.guice.InjectorFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

public class ExportList {
	LinkedHashSet<ExportItem> items;

	public ExportList() {
		this.items = new LinkedHashSet<ExportItem>();
	}

	public void add(ExportItem item) {
		this.items.add(item);
	}

	public String serializeToXML() {
		StringWriter writer = new StringWriter();

		try {
			Injector injector = InjectorFactory.getInstance();
			XmlSerializer serializer = injector.getInstance(XmlSerializer.class);
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);

			this.serializeToXML(serializer);

			serializer.endDocument();
		} catch (Exception e) {
			AgentLogger.getInstance().error(e);
			throw new DataException(ErrorCode.SERIALIZE_TO_XML, null, e);
		}

		return writer.toString();
	}

	public void serializeToXML(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
		serializer.startTag("", "exportlist");
		for (ExportItem exportItem : this.items)
			exportItem.serializeToXML(serializer);
		serializer.endTag("", "exportlist");
	}

	@SuppressWarnings("unchecked")
	public void unserializeFromXML(Element exportList) {
		List<Element> items;
		Iterator<Element> iterator;

		if (exportList == null) return;

		items = exportList.getChildren("item");
		iterator = items.iterator();

		this.items.clear();

		while (iterator.hasNext()) {
			ExportItem exportItem = new ExportItem();
			exportItem.deserializeFromXML(iterator.next());
			this.items.add(exportItem);
		}

	}

}
