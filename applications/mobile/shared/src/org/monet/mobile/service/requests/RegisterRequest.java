package org.monet.mobile.service.requests;

import org.monet.mobile.service.ActionCode;
import org.monet.mobile.service.Request;

public class RegisterRequest extends Request {

  public String registrationId;

  public RegisterRequest() {
    super(ActionCode.Register);
  }

  public RegisterRequest(String registrationId) {
    super(ActionCode.Register);
    
    this.registrationId = registrationId;
  }

}
