package org.monet.mobile.service.requests;

import org.monet.mobile.service.ActionCode;
import org.monet.mobile.service.Request;

public class LoadNewAssignedTasksRequest extends Request {

  public long SyncMark;
  
  public LoadNewAssignedTasksRequest() {
    super(ActionCode.LoadNewAssignedTasks);
  }
  
  public LoadNewAssignedTasksRequest(long syncMark) {
    super(ActionCode.LoadNewAssignedTasks);
    this.SyncMark = syncMark;
  }

}
