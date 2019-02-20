package org.monet.docservice.servlet.factory.impl;

import org.monet.docservice.servlet.factory.MessageResponse;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class Ping extends Action {

  @Override
  public void execute(Map<String, Object> params, HttpServletResponse response) throws Exception {
    response.getWriter().write(MessageResponse.PING_SUCCESSFULLY);
  }

}
