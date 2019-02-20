package org.monet.mobile.service.requests;

import org.monet.mobile.service.ActionCode;
import org.monet.mobile.service.Request;

public class LoadNewAvailableTasksRequest extends Request {

  public long SyncMark;
  
  public LoadNewAvailableTasksRequest() {
    super(ActionCode.LoadNewAvailableTasks);
  }
  
  public LoadNewAvailableTasksRequest(long syncMark) {
    super(ActionCode.LoadNewAvailableTasks);
    this.SyncMark = syncMark;
  }

}
