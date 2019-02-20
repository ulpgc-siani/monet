package org.monet.mobile.service.requests;

import org.monet.mobile.service.ActionCode;
import org.monet.mobile.service.Request;

public class UnregisterRequest extends Request {

  public String registrationId;

  public UnregisterRequest() {
    super(ActionCode.Unregister);
  }

  public UnregisterRequest(String registrationId) {
    super(ActionCode.Unregister);
    
    this.registrationId = registrationId;
  }

}
