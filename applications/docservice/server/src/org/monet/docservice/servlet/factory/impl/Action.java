package org.monet.docservice.servlet.factory.impl;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

public abstract class Action {
  public abstract void execute(Map<String, Object> params, HttpServletResponse response) throws Exception;

  public String normalize(String idDocument, String space){
    return space + "_" + idDocument;
  }
}
