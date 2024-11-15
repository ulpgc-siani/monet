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

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.HashMap;

public class Session extends BaseObject {
	private Account oAccount;
	private HashMap<String, Object> hmAttributes;
	private boolean isObsolete;

	public Session() {
		super();
		this.oAccount = new Account();
		this.hmAttributes = new HashMap<String, Object>();
		this.isObsolete = false;
	}

	public Account getAccount() {
		return this.oAccount;
	}

	public Boolean setAccount(Account oAccount) {
		this.oAccount = oAccount;
		return true;
	}

	public Boolean existVariable(String sVariable) {
		return this.hmAttributes.containsKey(sVariable);
	}

	@SuppressWarnings("unchecked")
	public <T> T getVariable(String sVariable) {
		return (T) this.hmAttributes.get(sVariable);
	}

	public Boolean setVariable(String sVariable, Object oValue) {
		if (oValue == null) this.hmAttributes.remove(sVariable);
		else this.hmAttributes.put(sVariable, oValue);
		return true;
	}

	public void deleteVariable(String name) {
		this.hmAttributes.remove(name);
	}

	public boolean isObsolete() {
		return this.isObsolete;
	}

	public void setObsolete(boolean value) {
		this.isObsolete = value;
	}

	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {

	}

}