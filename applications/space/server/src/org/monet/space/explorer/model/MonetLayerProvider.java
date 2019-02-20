package org.monet.space.explorer.model;

import org.monet.space.kernel.components.ComponentDatawareHouse;
import org.monet.space.kernel.components.ComponentFederation;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.*;

public class MonetLayerProvider implements LayerProvider {

	@Override
	public NodeLayer getNodeLayer() {
		return ComponentPersistence.getInstance().getNodeLayer();
	}

	@Override
	public TaskLayer getTaskLayer() {
		return ComponentPersistence.getInstance().getTaskLayer();
	}

	@Override
	public NotificationLayer getNotificationLayer() {
		return ComponentPersistence.getInstance().getNotificationLayer();
	}

	@Override
	public DashboardLayer getDashboardLayer() {
		return ComponentDatawareHouse.getInstance().getDashboardLayer();
	}

	@Override
	public RoleLayer getRoleLayer() {
		return ComponentFederation.getInstance().getRoleLayer();
	}

	@Override
	public SourceLayer getSourceLayer() {
		return ComponentPersistence.getInstance().getSourceLayer();
	}

	@Override
	public HistoryStoreLayer getHistoryStoreLayer() {
		return ComponentPersistence.getInstance().getHistoryStoreLayer();
	}
}
