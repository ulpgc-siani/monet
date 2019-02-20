package org.monet.mobile.service.requests;

import org.monet.mobile.service.ActionCode;
import org.monet.mobile.service.Request;

public class UnassignTaskRequest extends Request {

  public String TaskId;
  
  public UnassignTaskRequest() {
    super(ActionCode.UnassignTask);
  }
  
  public UnassignTaskRequest(String taskId) {
    super(ActionCode.UnassignTask);
    this.TaskId = taskId;
  }

}
