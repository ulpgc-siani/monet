package org.monet.mobile.service.requests;

import org.monet.mobile.service.ActionCode;
import org.monet.mobile.service.Request;

public class HeloRequest extends Request {

  public HeloRequest() {
    super(ActionCode.Helo);
  }

}
