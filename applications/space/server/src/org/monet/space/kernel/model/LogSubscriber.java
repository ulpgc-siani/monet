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

public class LogSubscriber extends BaseObject {
	private ServerConfiguration oConfiguration;
	private Integer iType;
	private Date dtRegistration;

	public LogSubscriber() {
		super();
		this.oConfiguration = new ServerConfiguration();
		this.iType = Strings.UNDEFINED_INT;
		this.dtRegistration = new Date();
	}

	public ServerConfiguration getServerConfiguration() {
		return this.oConfiguration;
	}

	public Boolean setServerConfiguration(ServerConfiguration oConfiguration) {
		this.oConfiguration = oConfiguration;
		return true;
	}

	public Integer getType() {
		return this.iType;
	}

	public Boolean setType(Integer iType) {
		this.iType = iType;
		return true;
	}

	public String getRegistrationDate(String sFormat, String codeLanguage, TimeZone zone) {
		if (this.dtRegistration == null) return null;
		return LibraryDate.getDateAndTimeString(this.dtRegistration, codeLanguage, zone, sFormat, false, Strings.BAR45);
	}

	public String getRegistrationDate(String sFormat) {
		return this.getRegistrationDate(sFormat, Language.getCurrent(), Language.getCurrentTimeZone());
	}

	public String getRegistrationDate() {
		return this.getRegistrationDate(LibraryDate.Format.DEFAULT, Language.getCurrent(), Language.getCurrentTimeZone());
	}

	public Date getInternalRegistrationDate() {
		return this.dtRegistration;
	}

	public Boolean setRegistrationDate(Date dtRegistration) {
		this.dtRegistration = dtRegistration;
		return true;
	}

	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {

	}
}
