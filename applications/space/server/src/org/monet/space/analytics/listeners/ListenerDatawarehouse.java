package org.monet.space.analytics.listeners;

import org.monet.space.analytics.model.Kernel;
import org.monet.space.analytics.model.ChartStore;
import org.monet.space.kernel.components.ComponentDatawareHouse;
import org.monet.space.kernel.components.layers.DashboardLayer;
import org.monet.space.kernel.listeners.Listener;
import org.monet.space.kernel.model.Datastore;
import org.monet.space.kernel.model.MonetEvent;
import org.monet.space.kernel.model.Session;

public class ListenerDatawarehouse extends Listener {

	@Override
	public void kernelReset(MonetEvent event) {
		Kernel.Instance().reset();
	}

	@Override
	public void datastoreMounted(MonetEvent event) {
		Datastore datastore = (Datastore) event.getSender();
		Kernel.Instance().resetDatastore(datastore.getCode());

		DashboardLayer dashboardLayer = ComponentDatawareHouse.getInstance().getDashboardLayer();
		dashboardLayer.reset();
	}

    @Override
    public void sessionDestroyed(MonetEvent event) {
        Session session = (Session)event.getSender();
        ChartStore.Instance().remove(session.getId());
    }
}
