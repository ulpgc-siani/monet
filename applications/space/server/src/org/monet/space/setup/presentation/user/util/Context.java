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

package org.monet.space.setup.presentation.user.util;

import org.monet.space.kernel.constants.Strings;

import java.util.HashMap;
import java.util.Set;

public class Context {
	private String sValueForNull;
	private HashMap<String, Object> hmContext;

	public Context() {
		this.sValueForNull = Strings.EMPTY;
		this.hmContext = new HashMap<String, Object>();
	}

	public void setValueForNull(String sValue) {
		this.sValueForNull = sValue;
	}

	public HashMap<String, Object> get() {
		return this.hmContext;
	}

	public Set<String> getKeys() {
		return this.hmContext.keySet();
	}

	public Object get(String sKey) {
		return this.hmContext.get(sKey);
	}

	public boolean containsKey(String sKey) {
		return this.hmContext.containsKey(sKey);
	}

	public Boolean put(String sKey, Object oObject) {
		if (oObject == null) oObject = this.sValueForNull;
		this.hmContext.put(sKey, oObject);
		return true;
	}

	public Boolean remove(String sKey) {
		this.hmContext.remove(sKey);
		return true;
	}

	public Boolean clear() {
		this.hmContext.clear();
		return true;
	}

}