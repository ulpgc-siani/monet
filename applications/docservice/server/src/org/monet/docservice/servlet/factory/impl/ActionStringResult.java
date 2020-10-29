package org.monet.docservice.servlet.factory.impl;

import org.monet.http.Response;

import java.util.Map;

public abstract class ActionStringResult extends Action {

  @Override
  public void execute(Map<String, Object> params, Response response)
      throws Exception {
    String result = onExecute(params, response);
    response.setCharacterEncoding("UTF-8");
   
    if(result != null)
      response.getOutputStream().write(result.getBytes("UTF-8"));
  }

  public abstract String onExecute(Map<String, Object> params, Response response) throws Exception;

}
