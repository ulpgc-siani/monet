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

package org.monet.space.kernel.components;

import org.monet.space.kernel.model.Account;
import org.monet.space.kernel.model.ISecurable;

public abstract class ComponentSecurity extends Component {
	protected static ComponentSecurity instance;

	protected ComponentSecurity() {
		super();
	}

	public synchronized static ComponentSecurity getInstance() {
		return instance;
	}

	public static Boolean started() {
		if (instance == null) return false;
		return instance.isRunning;
	}

	public abstract Boolean canCreate(ISecurable oSecurable, Account oAccount);

	public abstract Boolean canDelete(ISecurable oSecurable, Account oAccount);

	public abstract Boolean canRead(ISecurable oSecurable, Account oAccount);

	public abstract Boolean canWrite(ISecurable oSecurable, Account oAccount);

}