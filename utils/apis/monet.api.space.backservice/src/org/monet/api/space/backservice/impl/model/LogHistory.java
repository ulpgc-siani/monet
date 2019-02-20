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

public class LogHistory extends BaseObject {
	private BookEntryList entryList;

	public LogHistory() {
		super();
		this.entryList = new BookEntryList();
	}

	public BookEntryList getEntryList() {
		return this.entryList;
	}

	public Boolean setEntryList(BookEntryList logEntryList) {
		this.entryList = logEntryList;
		return true;
	}

	public void serializeToXML(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
		serializer.startTag("", "log-history");
		this.entryList.serializeToXML(serializer);
		serializer.endTag("", "log-history");
	}

	public void deserializeFromXML(Element node) throws ParseException {
		this.entryList.clear();
		this.entryList.deserializeFromXML(node);
	}

	public void deserializeFXML(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
	}

}