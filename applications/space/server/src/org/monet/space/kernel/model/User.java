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
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.library.LibraryDate;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class User extends BaseObject {
	private UserInfo info;
	private String codeLanguage;
	private Date registration;

	public User() {
		super();
		this.info = new UserInfo();
		this.codeLanguage = "es";
		this.registration = new Date();
	}

	public UserInfo getInfo() {
		return this.info;
	}

	public void setInfo(UserInfo info) {
		this.info = info;
	}

	public String getLanguage() {
		return this.codeLanguage;
	}

	public void setLanguage(String code) {
		this.codeLanguage = code;
	}

	public String getRegistrationDate(String format, String codeLanguage, TimeZone zone) {
		if (this.registration == null)
			return null;
		return LibraryDate.getDateAndTimeString(this.registration, codeLanguage, zone, format, false, Strings.BAR45);
	}

	public String getRegistrationDate(String format) {
		return this.getRegistrationDate(format, Language.getCurrent(), Language.getCurrentTimeZone());
	}

	public String getRegistrationDate() {
		return this.getRegistrationDate(LibraryDate.Format.DEFAULT, Language.getCurrent(), Language.getCurrentTimeZone());
	}

	public Date getInternalRegistrationDate() {
		return this.registration;
	}

	public void setRegistrationDate(Date registration) {
		this.registration = registration;
	}

	public JSONObject toJson() {
		JSONObject result = new JSONObject();

		result.put("id", this.getId());
		result.put("name", this.getName());
		result.put("language", this.getLanguage());
		result.put("info", this.getInfo().toJson());

		return result;
	}

	public void fromJson(String data) throws ParseException {
		JSONObject jsonData = (JSONObject) new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(data);

		this.id = (String) jsonData.get("id");
		this.codeLanguage = (String) jsonData.get("language");
		this.info.fromJson(jsonData.get("info").toString());

	}

	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {
		DateFormat dateFormat = new SimpleDateFormat("yyyyy-MM-dd HH:mm:ss.S z");

		serializer.startTag("", "user");

		serializer.attribute("", "id", this.id);
		serializer.attribute("", "name", this.getName());
		serializer.attribute("", "codeLanguage", this.codeLanguage);
		serializer.attribute("", "registration", dateFormat.format(this.getInternalRegistrationDate()));

		this.info.serializeToXML(serializer, depth);

		serializer.endTag("", "user");
	}

}
