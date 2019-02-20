package org.monet.space.kernel.components.layers;

import org.monet.metamodel.DashboardDefinition;
import org.monet.space.kernel.model.Dashboard;
import org.monet.space.kernel.model.DashboardList;

public interface DashboardLayer extends Layer {

	public abstract DashboardList loadAll();

	public abstract boolean exists(String key);

	public abstract Dashboard load(String key);

	public abstract boolean isLoaded(String key);

	public abstract Dashboard create(String key);

	public abstract void update(DashboardDefinition oldDefinition, DashboardDefinition definition);

	public abstract void remove(String key);
}
