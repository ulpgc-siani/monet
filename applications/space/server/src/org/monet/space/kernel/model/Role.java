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

import net.minidev.json.JSONObject;
import org.monet.metamodel.RoleDefinition;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.library.LibraryDate;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.Date;
import java.util.TimeZone;

public abstract class Role extends BaseObject {
	private Type type;
	private Date dtBegin;
	private Date dtExpired;

	public enum Type {
		User,
		Service,
		Feeder
	}

	public enum Nature {
		Internal,
		External,
		Both
	}

	public static final String USER = "user";
	public static final String PARTNER = "partner";

	public Role() {
		super();
		this.dtBegin = null;
		this.dtExpired = null;
	}

	public abstract String getLabel();

	public RoleDefinition getDefinition() {
		Dictionary dictionary = Dictionary.getInstance();
		return dictionary.getRoleDefinition(this.code);
	}

	public Type getType() {
		return this.type;
	}

	public void setType(Type type) {
		this.type = type;
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
		if (this.dtExpired == null) return null;
		return LibraryDate.getDateAndTimeString(this.dtExpired, codeLanguage, zone, format, false, Strings.BAR45);
	}

	public String getExpireDate(String sFormat) {
		return this.getExpireDate(sFormat, Language.getCurrent(), Language.getCurrentTimeZone());
	}

	public String getExpireDate() {
		return this.getExpireDate(LibraryDate.Format.DEFAULT, Language.getCurrent(), Language.getCurrentTimeZone());
	}

	public Date getInternalExpireDate() {
		return this.dtExpired;
	}

	public Boolean setExpireDate(Date dtExpired) {
		this.dtExpired = dtExpired;
		return true;
	}

	public boolean isUserRole() {
		return this instanceof UserRole;
	}

	public boolean isServiceRole() {
		return this instanceof ServiceRole;
	}

	public boolean isFeederRole() {
		return this instanceof FeederRole;
	}

	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {
	}

	public abstract void addJsonAttributes(JSONObject result);

	public JSONObject toJson() {
		JSONObject result = new JSONObject();
		Date beginDate = this.getInternalBeginDate();
		Date expireDate = this.getInternalExpireDate();

		result.put("id", this.getId());
		result.put("code", this.getCode());
		result.put("label", this.getLabel());
		result.put("beginDate", LibraryDate.getDateAndTimeString(beginDate, Language.getCurrent(), Language.getCurrentTimeZone(), LibraryDate.Format.INTERNAL, true, "/"));
		result.put("expireDate", LibraryDate.getDateAndTimeString(expireDate, Language.getCurrent(), Language.getCurrentTimeZone(), LibraryDate.Format.INTERNAL, true, "/"));
		result.put("expires", expireDate != null);
		result.put("began", beginDate.getTime() <= (new Date()).getTime());

		this.addJsonAttributes(result);

		if (expireDate != null)
			result.put("expired", (new Date()).getTime() > expireDate.getTime());
		else
			result.put("expired", false);

		return result;
	}

}
