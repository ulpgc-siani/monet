package org.monet.mobile.service.requests;

import org.monet.mobile.service.ActionCode;
import org.monet.mobile.service.Request;

public class AssignTaskRequest extends Request {

  public String TaskId;
  
  public AssignTaskRequest() {
    super(ActionCode.AssignTask);
  }
  
  public AssignTaskRequest(String taskId) {
    super(ActionCode.AssignTask);
    this.TaskId = taskId;
  }

}
