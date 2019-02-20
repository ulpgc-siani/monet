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

package org.monet.space.analytics.model;

import org.monet.space.kernel.components.ComponentDatawareHouse;
import org.monet.space.kernel.components.layers.DashboardLayer;
import org.monet.space.kernel.model.Dashboard;
import org.monet.space.kernel.model.DashboardList;
import org.sumus.asset.Asset;
import org.sumus.asset.AssetException;
import org.sumus.cube.Cube;
import org.sumus.dimension.Dimension;
import org.sumus.indicator.Indicator;
import org.sumus.taxonomy.Taxonomy;

public class Kernel {
	private DashboardLayer dashboardLayer;
	private static Kernel instance;

	private Kernel() {
		this.dashboardLayer = ComponentDatawareHouse.getInstance().getDashboardLayer();
	}

	public static Kernel Instance() {
		if (instance == null)
			instance = new Kernel();

		return instance;
	}

	public boolean isDashboardLoaded(String key) {
		return this.dashboardLayer.isLoaded(key);
	}

	public Dashboard getDashboard(String key) {
		return this.dashboardLayer.load(key);
	}

	public DashboardList getDashboardList() {
		return this.dashboardLayer.loadAll();
	}

	public Indicator getIndicator(Asset asset, String key) {
		Indicator indicator = asset.getIndicator(key);

		if (indicator != null)
			return indicator;

		for (Cube cube : asset.getCubes()) {
			indicator = cube.getIndicator(key);
			if (indicator != null)
				return indicator;
		}

		return indicator;
	}

	public Taxonomy getTaxonomy(String dashboardKey, String key) throws AssetException {
		Asset asset = (Asset) this.getDashboard(dashboardKey).getAsset();

		for (Dimension dimension : asset.getDimensions()) {
			Taxonomy taxonomy = dimension.getTaxonomy(key);
			if (taxonomy != null)
				return taxonomy;
		}

		return null;
	}

	public void reset() {
	}

	public void resetDatastore(String code) {
	}

}

