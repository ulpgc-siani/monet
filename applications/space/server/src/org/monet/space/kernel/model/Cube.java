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
import org.monet.metamodel.DatastoreDefinition;
import org.monet.metamodel.DatastoreDefinitionBase.CubeProperty;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.library.LibraryDate;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.TimeZone;

public class Cube extends Entity<DatastoreDefinition.CubeProperty> implements ISecurable {
	private String label;
	private String description;
	private Date createDate;
	private Date updateDate;
	private String content;
	private StringBuffer data;
	private HashSet<String> grantedUsers;

	public Cube(String code) {
		super();
		this.setCode(code);
		this.label = Strings.EMPTY;
		this.description = Strings.EMPTY;
		this.createDate = new Date();
		this.updateDate = null;
		this.content = Strings.EMPTY;
		this.data = new StringBuffer();
		this.grantedUsers = new HashSet<String>();
	}

	public String getLabel() {
		return this.label;
	}

	public String getInstanceLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isPublic() {
		return true;
	}

	public boolean isLinked() {
		return false;
	}

	public boolean isDeletable() {
		return true;
	}

	public boolean isLocked() {
		return false;
	}

	public String getCreateDate(String format, String codeLanguage, TimeZone zone) {
		if (this.createDate == null)
			return null;
		return LibraryDate.getDateAndTimeString(this.createDate, codeLanguage, zone, format, true, Strings.BAR45);
	}

	public String getCreateDate(String format) {
		return this.getCreateDate(format, Language.getCurrent(), Language.getCurrentTimeZone());
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

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Date getInternalUpdateDate() {
		return this.updateDate;
	}

	public String getUpdateDate(String format, String codeLanguage, TimeZone zone) {
		if (this.updateDate == null)
			return null;
		return LibraryDate.getDateAndTimeString(this.updateDate, codeLanguage, zone, format, true, Strings.BAR45);
	}

	public String getUpdateDate(String format) {
		return this.getUpdateDate(format, Language.getCurrent(), Language.getCurrentTimeZone());
	}

	public String getUpdateDate() {
		return this.getUpdateDate(LibraryDate.Format.DEFAULT, Language.getCurrent(), Language.getCurrentTimeZone());
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public PermissionList getPermissionList() {
		return new PermissionList();
	}

	public HashSet<String> getGrantedUsers() {
		return this.grantedUsers;
	}

	public void setGrantedUsers(HashSet<String> grantedUsers) {
		this.grantedUsers = grantedUsers;
	}

	public HashSet<String> getRoles() {
		HashSet<String> result = new HashSet<String>();
		result.add(UserRole.HOLDER);
		return result;
	}

	public HashSet<String> getRules() {
		return new HashSet<String>();
	}

	public StringBuffer getData() {
		return this.data;
	}

	public void setData(StringBuffer oData) {
		this.data = oData;
	}

	public CubeProperty getDefinition() {
		return Dictionary.getInstance().getCubeDefinition(this.getCode());
	}

	public JSONObject toJson() {
		JSONObject result = new JSONObject();
		String content = this.getContent();
		Boolean partialLoading = this.isPartialLoading();

		if (partialLoading)
			this.disablePartialLoading();

		result.put("id", this.getId());
		result.put("code", this.getCode());
		result.put("name", this.getName());
		result.put("label", this.getLabel());
		result.put("description", this.getDescription());
		result.put("created", this.getCreateDate());
		result.put("updated", this.getUpdateDate());
		result.put("content", content);

		if (partialLoading)
			this.enablePartialLoading();

		return result;
	}

	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {
		DateFormat dateFormat = new SimpleDateFormat("yyyyy-MM-dd HH:mm:ss.S z");

		serializer.startTag("", "cube");

		serializer.attribute("", "code", this.code);
		serializer.attribute("", "name", this.name);
		if (this.createDate != null) serializer.attribute("", "createDate", dateFormat.format(this.createDate));
		if (this.updateDate != null) serializer.attribute("", "updateDate", dateFormat.format(this.updateDate));

		serializer.startTag("", "label");
		serializer.text(this.label);
		serializer.endTag("", "label");

		serializer.startTag("", "description");
		serializer.text(this.description);
		serializer.endTag("", "description");

		serializer.endTag("", "cube");
	}

	public void deserializeFromXML(String content) {
	}

}
