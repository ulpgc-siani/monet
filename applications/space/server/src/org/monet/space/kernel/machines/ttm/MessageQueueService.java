package org.monet.space.kernel.machines.ttm;

import org.monet.space.kernel.machines.ttm.model.Message;

public interface MessageQueueService {

	public enum State {QUEUED, RETRYING, FAILED, SENT}

	long push(String orderId, Message message);

	State getState(long messageId);

}
