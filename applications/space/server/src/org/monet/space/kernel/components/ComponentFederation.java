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

import org.monet.federation.accountservice.client.FederationService;
import org.monet.space.kernel.components.layers.FederationLayer;
import org.monet.space.kernel.components.layers.FederationSetupLayer;
import org.monet.space.kernel.components.layers.MasterLayer;
import org.monet.space.kernel.components.layers.RoleLayer;
import org.monet.space.kernel.model.Federation;

public abstract class ComponentFederation extends Component {
	protected static ComponentFederation instance;
	protected static MasterLayer masterLayer;

	protected ComponentFederation() {
		super();
	}

	public synchronized static ComponentFederation getInstance() {
		return instance;
	}

	public static Boolean started() {
		if (instance == null) return false;
		return instance.isRunning;
	}

	public static MasterLayer getMasterLayer() {
		if (masterLayer == null) return null;
		return masterLayer;
	}

	public abstract Federation getFederation();

	public abstract FederationService getFederationService();

	public abstract void reset();

	public abstract FederationSetupLayer getSetupLayer();

    public abstract FederationLayer getDefaultLayer();

	public abstract FederationLayer getLayer(FederationLayer.Configuration configuration);

	public abstract RoleLayer getRoleLayer();

	public abstract void reset(boolean force);

}