package org.monet.mobile.service.requests;

import org.monet.mobile.service.ActionCode;
import org.monet.mobile.service.Request;

public class LoadNewGlossariesRequest extends Request {

  public long SyncMark;
  
  public LoadNewGlossariesRequest() {
    super(ActionCode.LoadNewGlossaries);
  }
  
  public LoadNewGlossariesRequest(long syncMark) {
    super(ActionCode.LoadNewGlossaries);
    this.SyncMark = syncMark;
  }

}
