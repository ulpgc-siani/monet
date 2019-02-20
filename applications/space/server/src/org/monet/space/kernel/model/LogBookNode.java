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

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.Date;

public class LogBookNode extends LogBook {

	public LogBookNode() {
		super();
	}

	public void addEntry(BookEntry entry) {
	}

	public BookEntry[] getEntries(String idNode, Integer iStartPos, Integer iLimit) {
		DataRequest oDataRequest = new DataRequest();
		oDataRequest.setCode(idNode);
		oDataRequest.setStartPos(iStartPos);
		oDataRequest.setLimit(iLimit);
		return this.getBookLink().requestEntries(oDataRequest).get().values().toArray(new BookEntry[0]);
	}

	public BookEntry[] getEntries(Integer iStartPos, Integer iLimit) {
		return this.getEntries(null, iStartPos, iLimit);
	}

	public Integer getEntriesCount(String idNode) {
		return this.getBookLink().requestEntriesCount(idNode);
	}

	public Integer getEntriesCount() {
		return this.getEntriesCount(null);
	}

	public BookEntryList search(Integer iEventType, Date dtFrom, Date dtTo) {
		return this.getBookLink().searchEntries(iEventType, dtFrom, dtTo);
	}

	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {

	}
}