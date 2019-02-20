package org.monet.federation.accountoffice.control.servlets.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Action {
  void execute(HttpServletRequest request, HttpServletResponse response, String path);
}
