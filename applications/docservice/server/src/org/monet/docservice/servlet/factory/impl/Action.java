package org.monet.docservice.servlet.factory.impl;

import org.monet.docservice.core.Key;
import org.monet.docservice.servlet.RequestParams;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

public abstract class Action {
  public abstract void execute(Map<String, Object> params, HttpServletResponse response) throws Exception;

  public Key documentKey(Map<String, Object> params) {
    return new Key(space(params), (String) params.get(RequestParams.REQUEST_PARAM_DOCUMENT_CODE));
  }

  public Key documentKeyFromId(Map<String, Object> params) {
    return new Key(space(params), (String) params.get(RequestParams.REQUEST_PARAM_DOCUMENT_ID));
  }

  public Key templateKey(Map<String, Object> params) {
    return new Key(space(params), (String) params.get(RequestParams.REQUEST_PARAM_TEMPLATE_CODE));
  }

  public String space(Map<String, Object> params) {
    return (String) params.get(RequestParams.REQUEST_PARAM_SPACE);
  }
}
