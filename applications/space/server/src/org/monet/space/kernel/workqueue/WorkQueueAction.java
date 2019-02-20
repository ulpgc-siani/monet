package org.monet.space.kernel.workqueue;

import org.monet.space.kernel.model.WorkQueueItem;

public abstract class WorkQueueAction {

	public abstract void execute(WorkQueueItem item) throws Exception;

}
