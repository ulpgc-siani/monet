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

import org.monet.space.kernel.constants.PermissionType;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.library.LibraryDate;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.Date;
import java.util.TimeZone;

public class Permission extends BaseObject {
	private String idObject;
	private String idUser;
	private Integer Type;
	private Date dtBegin;
	private Date dtExpire;

	public Permission() {
		super();
		this.idObject = new String(Strings.EMPTY);
		this.idUser = new String(Strings.EMPTY);
		this.Type = PermissionType.READ;
		this.dtBegin = new Date();
		this.dtExpire = new Date();
	}

	public String getIdObject() {
		return this.idObject;
	}

	public Boolean setIdObject(String idNode) {
		this.idObject = idNode;
		return true;
	}

	public String getIdUser() {
		return this.idUser;
	}

	public Boolean setIdUser(String idUser) {
		this.idUser = idUser;
		return true;
	}

	public Integer getType() {
		return this.Type;
	}

	public Boolean setType(Integer Type) {
		this.Type = Type;
		return true;
	}

	public Boolean canRead() {
		return this.Type.equals(PermissionType.READ);
	}

	public Boolean canWrite() {
		return this.Type.equals(PermissionType.READ_WRITE);
	}

	public Boolean canCreate() {
		return this.Type.equals(PermissionType.READ_WRITE_CREATE_DELETE);
	}

	public Boolean canDelete() {
		return this.Type.equals(PermissionType.READ_WRITE_CREATE_DELETE);
	}

	public String getBeginDate(String format, String codeLanguage, TimeZone zone) {
		if (this.dtBegin == null) return null;
		return LibraryDate.getDateAndTimeString(this.dtBegin, codeLanguage, zone, format, false, Strings.BAR45);
	}

	public String getBeginDate(String sFormat) {
		return this.getBeginDate(sFormat, Language.getCurrent(), Language.getCurrentTimeZone());
	}

	public String getBeginDate() {
		return this.getBeginDate(LibraryDate.Format.DEFAULT, Language.getCurrent(), Language.getCurrentTimeZone());
	}

	public Date getInternalBeginDate() {
		return this.dtBegin;
	}

	public Boolean setBeginDate(Date dtBegin) {
		this.dtBegin = dtBegin;
		return true;
	}

	public String getExpireDate(String format, String codeLanguage, TimeZone zone) {
		if (this.dtExpire == null) return null;
		return LibraryDate.getDateAndTimeString(this.dtExpire, codeLanguage, zone, format, false, Strings.BAR45);
	}

	public String getExpireDate(String sFormat) {
		return this.getExpireDate(sFormat, Language.getCurrent(), Language.getCurrentTimeZone());
	}

	public String getExpireDate() {
		return this.getExpireDate(LibraryDate.Format.DEFAULT, Language.getCurrent(), Language.getCurrentTimeZone());
	}

	public Date getInternalExpireDate() {
		return this.dtExpire;
	}

	public Boolean setExpireDate(Date dtExpire) {
		this.dtExpire = dtExpire;
		return true;
	}

	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {

	}

}
