package org.monet.mobile.service.requests;

import org.monet.mobile.service.ActionCode;
import org.monet.mobile.service.Request;

import java.util.List;

public class LoadAssignedTasksToDeleteRequest extends Request {

  public List<String> Ids;
  
  public LoadAssignedTasksToDeleteRequest() {
    super(ActionCode.LoadAssignedTasksToDelete);
  }
  
  public LoadAssignedTasksToDeleteRequest(List<String> ids) {
    super(ActionCode.LoadAssignedTasksToDelete);
    this.Ids = ids;
  }

}
