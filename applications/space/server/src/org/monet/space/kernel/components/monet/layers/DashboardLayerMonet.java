package org.monet.space.kernel.components.monet.layers;

import org.monet.metamodel.DashboardDefinition;
import org.monet.metamodel.Definition;
import org.monet.space.kernel.components.layers.DashboardLayer;
import org.monet.space.kernel.machines.bim.Engine;
import org.monet.space.kernel.model.Dashboard;
import org.monet.space.kernel.model.DashboardList;
import org.monet.space.kernel.model.Dictionary;

import java.util.HashMap;
import java.util.List;

public class DashboardLayerMonet extends DatawareHouseLayerMonet implements DashboardLayer {
	private static HashMap<String, Dashboard> loadedDashboards = new HashMap<>();
	private static HashMap<String, Dashboard> loadingDashboards = new HashMap<>();

	public DashboardLayerMonet() {
		super();
	}

	@Override
	public DashboardList loadAll() {
		Dictionary dictionary = Dictionary.getInstance();
		List<DashboardDefinition> dashboardDefinitionList = dictionary.getDashboardDefinitionList();
		DashboardList result = new DashboardList();

		for (DashboardDefinition definition : dashboardDefinitionList) {
			Dashboard dashboard = new Dashboard(definition);
			result.add(dashboard);
		}

		return result;
	}

	@Override
	public boolean exists(String key) {
		String code = Dictionary.getInstance().getDefinitionCode(key);
		return this.getEngine().existsDashboard(code);
	}

	@Override
	public Dashboard load(String key) {

		if (loadedDashboards.containsKey(key))
			return loadedDashboards.get(key);

		if (loadingDashboards.containsKey(key))
			return loadingDashboards.get(key);

		String code = Dictionary.getInstance().getDefinitionCode(key);
		Dashboard dashboard = this.getEngine().getDashboard(code, new Engine.OnLoad() {
			@Override
			public void loaded(Dashboard d) {
				loadedDashboards.put(d.getCode(), d);
				loadedDashboards.put(d.getName(), d);
				loadingDashboards.remove(d.getCode());
				loadingDashboards.remove(d.getName());
			}
		});

		loadingDashboards.put(dashboard.getCode(), dashboard);
		loadingDashboards.put(dashboard.getName(), dashboard);

		return dashboard;
	}

	@Override
	public boolean isLoaded(String key) {
		return loadedDashboards.containsKey(key);
	}

	@Override
	public Dashboard create(String key) {
		String code = Dictionary.getInstance().getDefinitionCode(key);
		return this.getEngine().createDashboard(code, new Engine.OnCreate() {
			@Override
			public void created(Dashboard d) {
			loadedDashboards.put(d.getCode(), d);
			loadedDashboards.put(d.getName(), d);
			}
		});
	}

	@Override
	public void update(DashboardDefinition oldDefinition, DashboardDefinition definition) {
		this.getEngine().updateDashboard(oldDefinition, definition);
		loadedDashboards.remove(definition.getCode());
		loadedDashboards.remove(definition.getName());
		loadingDashboards.remove(definition.getCode());
		loadingDashboards.remove(definition.getName());
	}

	@Override
	public void remove(String key) {
		Definition definition = Dictionary.getInstance().getDefinition(key);
		this.getEngine().removeDashboard(definition.getCode());
		loadedDashboards.remove(definition.getCode());
		loadedDashboards.remove(definition.getName());
		loadingDashboards.remove(definition.getCode());
		loadingDashboards.remove(definition.getName());
	}

	@Override
	public void reset() {
		this.getEngine().reset();
		loadedDashboards.clear();
		loadingDashboards.clear();
	}

}