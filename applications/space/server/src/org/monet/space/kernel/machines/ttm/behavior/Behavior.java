package org.monet.space.kernel.machines.ttm.behavior;

import org.monet.space.kernel.machines.ttm.persistence.PersistenceHandler;
import org.monet.space.kernel.machines.ttm.persistence.PersistenceService;
import org.monet.space.kernel.model.MailBoxUri;
import org.monet.space.kernel.model.TaskOrder;

import java.util.LinkedHashMap;

public abstract class Behavior {
	protected PersistenceHandler persistenceHandler;
	protected PersistenceService persistenceService;
	private LinkedHashMap<String, String> flags;

	protected void markAsCompleted(String orderId) {
		TaskOrder taskOrder = this.persistenceService.loadTaskOrder(orderId);
		taskOrder.setClosed(true);
		this.persistenceService.saveTaskOrder(taskOrder);
	}

    protected String getMailBoxUrl(MailBoxUri uri) {
        return this.persistenceService.getUrl(uri);
    }
}
