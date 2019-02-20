package org.monet.mobile.service.requests;

import java.util.ArrayList;

import org.monet.mobile.service.ActionCode;
import org.monet.mobile.service.Request;

public class LoadFinishedTasksToDeleteRequest extends Request {

  public ArrayList<String> Ids;
  
  public LoadFinishedTasksToDeleteRequest() {
    super(ActionCode.LoadFinishedTasksToDelete);
  }
  
  public LoadFinishedTasksToDeleteRequest(ArrayList<String> ids) {
    super(ActionCode.LoadFinishedTasksToDelete);
    this.Ids = ids;
  }

}
