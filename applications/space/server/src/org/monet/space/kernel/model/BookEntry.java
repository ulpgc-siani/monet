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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class BookEntry extends BaseObject {
	private String idObject;
	private String idUser;
	private String author;
	private String data;
	private Date createDate;
	private Integer type;

	public BookEntry(String idObject, String sAuthor, Integer iType) {
		super();
		this.idUser = sAuthor;
		this.author = sAuthor;
		this.idObject = idObject;
		this.data = "";
		this.type = iType;
		this.createDate = new Date();
	}

	public BookEntry() {
		this(null, null, null);
	}

	public String getIdUser() {
		return this.idUser;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	public String getAuthor() {
		return this.author;
	}

	public void setAuthor(String sAuthor) {
		this.author = sAuthor;
	}

	public String getIdObject() {
		return this.idObject;
	}

	public void setIdObject(String idNode) {
		this.idObject = idNode;
	}

	public String getData() {
		return this.data;
	}

	public void setData(String sData) {
		this.data = sData;
	}

	public Integer getType() {
		return this.type;
	}

	public void setType(Integer iType) {
		this.type = iType;
	}

	public String getCreateDate(String sFormat, String codeLanguage, TimeZone zone) {
		if (this.createDate == null) return null;
		return LibraryDate.getDateAndTimeString(this.createDate, codeLanguage, zone, sFormat, true, Strings.BAR45);
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

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {
		DateFormat dateFormat = new SimpleDateFormat("yyyyy-MM-dd HH:mm:ss.S z");
		serializer.startTag("", "entry");
		serializer.attribute("", "idObject", this.idObject);
		serializer.attribute("", "type", String.valueOf(this.type));
		serializer.attribute("", "date", dateFormat.format(this.createDate));
		serializer.endTag("", "entry");
	}

}
