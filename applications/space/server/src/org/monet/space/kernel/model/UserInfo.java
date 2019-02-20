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
import org.monet.space.kernel.library.LibraryArray;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.LinkedHashMap;

public class UserInfo extends BaseObject {
	private String photo;
	private String fullname;
	private LinkedHashMap<String, String> preferencesMap;
	private String email;
	private String[] occupations;

	public UserInfo() {
		super();
		this.photo = "";
		this.fullname = "";
		this.preferencesMap = new LinkedHashMap<String, String>();
		this.occupations = new String[0];
		this.email = "";
	}

	public String getPhoto() {
		return this.photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getFullname() {
		return this.fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LinkedHashMap<String, String> getPreferences() {
		return this.preferencesMap;
	}

	public void setPreferences(LinkedHashMap<String, String> preferencesMap) {
		this.preferencesMap = preferencesMap;
	}

	public String[] getOccupations() {
		return this.occupations;
	}

	public void setOccupations(String[] occupations) {
		this.occupations = occupations;
	}

	public JSONObject toJson() {
		JSONObject result = new JSONObject();

		result.put("photo", this.getPhoto());
		result.put("fullname", this.getFullname());
		result.put("preferences", SerializerData.serialize(this.getPreferences()));
		result.put("email", this.getEmail());
		result.put("occupations", LibraryArray.implode(this.getOccupations(), Strings.COMMA));

		return result;
	}

	public void fromJson(String data) throws ParseException {
		JSONObject jsonData = (JSONObject) new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(data);
		String occupations = (String) jsonData.get("occupations");

		this.fullname = (String) jsonData.get("fullname");
		this.preferencesMap = SerializerData.deserialize((String) jsonData.get("preferences"));
		this.email = (String) jsonData.get("email");
		this.occupations = ((occupations != null) && (!occupations.equals(""))) ? occupations.split(Strings.COMMA) : new String[0];

	}

	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {

	}

}
