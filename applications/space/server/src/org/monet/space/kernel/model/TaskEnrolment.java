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

import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.library.LibraryDate;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.Date;
import java.util.TimeZone;

public class TaskEnrolment extends BaseObject {
	private String idUser;
	private Date createDate;

	public TaskEnrolment(String idUser, Date createDate) {
		super();
		this.idUser = idUser;
		this.createDate = createDate;
	}

	public String getIdUser() {
		return this.idUser;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	public String getCreateDate(String format, String codeLanguage, TimeZone zone) {
		if (this.createDate == null) return null;
		return LibraryDate.getDateAndTimeString(this.createDate, codeLanguage, zone, format, true, Strings.BAR45);
	}

	public String getCreateDate(String sFormat) {
		return this.getCreateDate(sFormat, Language.getCurrent(), Language.getCurrentTimeZone());
	}

	public String getCreateDate() {
		return this.getCreateDate(LibraryDate.Format.DEFAULT, Language.getCurrent(), Language.getCurrentTimeZone());
	}

	public Date getInternalCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date dtCreateDate) {
		this.createDate = dtCreateDate;
	}

	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {

	}
}
