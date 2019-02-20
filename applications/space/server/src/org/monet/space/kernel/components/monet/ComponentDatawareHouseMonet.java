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

package org.monet.space.kernel.components.monet;

import org.monet.space.kernel.components.ComponentDatawareHouse;
import org.monet.space.kernel.components.layers.DashboardLayer;
import org.monet.space.kernel.components.layers.DatastoreLayer;
import org.monet.space.kernel.components.monet.layers.DashboardLayerMonet;
import org.monet.space.kernel.components.monet.layers.DatastoreLayerMonet;
import org.monet.space.kernel.exceptions.SystemException;

import java.util.HashMap;

public class ComponentDatawareHouseMonet extends ComponentDatawareHouse {
	private final DatastoreLayer datastoreLayer;
	private final DashboardLayer dashboardLayer;

	private ComponentDatawareHouseMonet() {
		super();
		this.dashboardLayer = new DashboardLayerMonet();
		this.dashboardLayer.create();

		this.datastoreLayer = new DatastoreLayerMonet();
		this.datastoreLayer.create();
	}

	public synchronized static ComponentDatawareHouse getInstance() {
		if (instance == null) instance = new ComponentDatawareHouseMonet();
		return instance;
	}

	@Override
	public HashMap<Integer, Boolean> getSupportedFeatures() {
		return new HashMap<Integer, Boolean>();
	}

	@Override
	public void run() throws SystemException {
		if (this.isRunning) return;
		this.isRunning = true;
	}

	@Override
	public void stop() throws SystemException {
		if (!this.isRunning) return;
		instance = null;
		this.isRunning = false;
	}

	@Override
	public DashboardLayer getDashboardLayer() {
		return this.dashboardLayer;
	}

	@Override
	public DatastoreLayer getDatastoreLayer() {
		return this.datastoreLayer;
	}

	@Override
	public void reset() {
		this.dashboardLayer.reset();
		this.datastoreLayer.reset();
	}

	@Override
	public void create() {
		this.dashboardLayer.create();
		this.datastoreLayer.create();
	}

	@Override
	public void destroy() {
		this.dashboardLayer.destroy();
		this.datastoreLayer.destroy();
	}

}