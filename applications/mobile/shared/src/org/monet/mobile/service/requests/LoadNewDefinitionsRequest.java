package org.monet.mobile.service.requests;

import org.monet.mobile.service.ActionCode;
import org.monet.mobile.service.Request;

public class LoadNewDefinitionsRequest extends Request {

  public long SyncMark;
  
  public LoadNewDefinitionsRequest() {
    super(ActionCode.LoadNewDefinitions);
  }
  
  public LoadNewDefinitionsRequest(long syncMark) {
    super(ActionCode.LoadNewDefinitions);
    this.SyncMark = syncMark;
  }

}
