package org.monet.space.kernel.workqueue;

import org.monet.space.kernel.constants.ErrorCode;
import org.monet.space.kernel.exceptions.SystemException;
import org.monet.space.kernel.model.WorkQueueType;

import java.util.HashMap;

public class WorkQueueActionFactory {

	private static WorkQueueActionFactory instance;
	private HashMap<WorkQueueType, Class<?>> actions = new HashMap<WorkQueueType, Class<?>>();

	public synchronized static WorkQueueActionFactory getInstance() {
		if (instance == null) {
			instance = new WorkQueueActionFactory();
		}
		return instance;
	}

	private WorkQueueActionFactory() {
	}

	public WorkQueueAction build(WorkQueueType type) {
		try {
			Class<?> clazz = this.actions.get(type);
			return (WorkQueueAction) clazz.newInstance();
		} catch (Exception oException) {
			throw new SystemException(ErrorCode.ACTIONS_FACTORY, type.toString(), oException);
		}
	}

}
