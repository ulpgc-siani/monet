package org.monet.docservice.docprocessor.worker;

import java.io.InputStream;
import java.util.List;

public interface WorkQueueRepository {

  void resetAllInProgress();
  List<WorkQueueItem> getNotStartedOperations();
  
  InputStream getWorkQueueItemExtraData(long id);
  void updateWorkQueueItemToPending(long id);
  void updateWorkQueueItemToInProgress(long id);
  void updateWorkQueueItemToFinished(long id);
  void updateWorkQueueItemToError(long id, String message);
  
}
