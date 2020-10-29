package org.monet.docservice.servlet.factory.impl;

import org.monet.docservice.servlet.factory.MessageResponse;
import org.monet.http.Response;

import java.util.Map;

public class Ping extends Action {

  @Override
  public void execute(Map<String, Object> params, Response response) throws Exception {
    response.getWriter().write(MessageResponse.PING_SUCCESSFULLY);
  }

}
