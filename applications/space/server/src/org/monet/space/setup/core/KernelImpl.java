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

package org.monet.space.setup.core;

import org.monet.space.kernel.components.ComponentFederation;
import org.monet.space.kernel.components.layers.MasterLayer;
import org.monet.space.kernel.model.Context;
import org.monet.space.kernel.model.Master;
import org.monet.space.kernel.model.Session;

public class KernelImpl implements Kernel {

	public KernelImpl() {
	}

	public boolean login(String username, String certificateAuthority) {
		MasterLayer masterLayer = ComponentFederation.getMasterLayer();
		Session session = Context.getInstance().getSession(Thread.currentThread().getId());

		if (!masterLayer.exists(username, certificateAuthority))
			return false;

		Master master = masterLayer.load(username, certificateAuthority);

		session.setVariable("userId", master.getId());

		return true;
	}

	public boolean logout() {
		Session session = Context.getInstance().getSession(Thread.currentThread().getId());
		session.setVariable("userId", null);
		return false;
	}

	public boolean isLogged() {
		Session session = Context.getInstance().getSession(Thread.currentThread().getId());
		String userId = session.getVariable("userId");
		return userId != null && !userId.isEmpty();
	}

}