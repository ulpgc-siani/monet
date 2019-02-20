package org.monet.mobile.service.requests;

import org.monet.mobile.service.ActionCode;
import org.monet.mobile.service.Request;

public class DownloadTaskPackedRequest extends Request {

  public String ID;
  
  public DownloadTaskPackedRequest() {
    super(ActionCode.DownloadTaskPacked);
  }
  
  public DownloadTaskPackedRequest(String id) {
    super(ActionCode.DownloadTaskPacked);
    this.ID = id;
  }

}
