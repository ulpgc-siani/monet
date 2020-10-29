package org.monet.docservice.servlet.factory.impl;

import org.monet.http.Response;

import java.util.Map;

public abstract class Action {
  public abstract void execute(Map<String, Object> params, Response response) throws Exception;
}
