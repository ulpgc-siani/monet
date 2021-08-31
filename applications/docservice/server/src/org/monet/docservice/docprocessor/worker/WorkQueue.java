package org.monet.docservice.docprocessor.worker;


import org.monet.docservice.core.Key;

public interface WorkQueue {

	long queueNewWorkItem(WorkQueueItem item);
	boolean documentHasPendingOperations(Key documentKey);
	boolean documentHasPendingOperationsOfType(Key documentKey, int operationType);
	long calculateEstimatedTimeToFinish(long workQueueItemId);

}
