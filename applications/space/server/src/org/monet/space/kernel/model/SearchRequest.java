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

import java.util.Date;
import java.util.TimeZone;

public class SearchRequest extends DataRequest {
	private Date dtFrom;
	private Date dtTo;

	public SearchRequest() {
		super();
		this.dtFrom = null;
		this.dtTo = null;
	}

	public String getFromDate(String format, String codeLanguage, TimeZone zone) {
		if (this.dtFrom == null) return null;
		return LibraryDate.getDateAndTimeString(this.dtFrom, codeLanguage, zone, format, false, Strings.BAR45);
	}

	public String getFromDate(String sFormat) {
		return this.getFromDate(sFormat, Language.getCurrent(), Language.getCurrentTimeZone());
	}

	public Date getFromDate() {
		return this.dtFrom;
	}

	public Boolean setFromDate(Date dtFrom) {
		this.dtFrom = dtFrom;
		return true;
	}

	public String getToDate(String format, String codeLanguage, TimeZone zone) {
		if (this.dtTo == null) return null;
		return LibraryDate.getDateAndTimeString(this.dtTo, codeLanguage, zone, format, false, Strings.BAR45);
	}

	public String getToDate(String sFormat) {
		return this.getToDate(sFormat, Language.getCurrent(), Language.getCurrentTimeZone());
	}

	public Date getToDate() {
		return this.dtTo;
	}

	public Boolean setToDate(Date dtTo) {
		this.dtTo = dtTo;
		return true;
	}

}
