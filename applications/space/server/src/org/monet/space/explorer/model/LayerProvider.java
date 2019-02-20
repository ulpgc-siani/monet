package org.monet.space.explorer.model;

import org.monet.space.kernel.components.layers.*;

public interface LayerProvider {
	NodeLayer getNodeLayer();
	TaskLayer getTaskLayer();
	NotificationLayer getNotificationLayer();
	DashboardLayer getDashboardLayer();
	RoleLayer getRoleLayer();
	SourceLayer getSourceLayer();
	HistoryStoreLayer getHistoryStoreLayer();
}
