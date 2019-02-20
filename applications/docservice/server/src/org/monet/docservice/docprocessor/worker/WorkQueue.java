package org.monet.docservice.docprocessor.worker;


public interface WorkQueue {

	long queueNewWorkItem(WorkQueueItem item);
	boolean documentHasPendingOperations(String documentId);
	boolean documentHasPendingOperationsOfType(String documentId, int operationType);
	long calculateEstimatedTimeToFinish(long workQueueItemId);

}
