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

package org.monet.space.kernel.pool;

import java.sql.Connection;
import java.util.HashMap;

public class PoolDBConnection {
	HashMap<String, Connection> hmConnections;

	protected static PoolDBConnection oInstance;

	private PoolDBConnection() {
		this.hmConnections = new HashMap<String, Connection>();
	}

	public synchronized static PoolDBConnection getInstance() {
		if (oInstance == null) {
			oInstance = new PoolDBConnection();
		}
		return oInstance;
	}

	public Boolean register(String idConnection, Connection oConnection) {
		if (idConnection == null) return false;
		this.hmConnections.put(idConnection, oConnection);
		return true;
	}

	public Boolean unregister(String idConnection) {
		if (idConnection == null) return false;
		this.hmConnections.remove(idConnection);
		return true;
	}

	public Connection get(String idConnection) {
		if (idConnection == null) return null;
		if (!this.hmConnections.containsKey(idConnection)) {
			return null;
		}
		return this.hmConnections.get(idConnection);
	}

}