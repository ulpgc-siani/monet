package org.monet.docservice.servlet;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.monet.docservice.core.exceptions.DocServiceException;
import org.monet.docservice.core.log.Logger;
import org.monet.docservice.servlet.factory.ActionFactory;
import org.monet.docservice.servlet.factory.impl.Action;
import org.monet.http.HttpResponse;
import org.monet.http.LibraryRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Singleton
public class Controller extends HttpServlet {

  private static final long serialVersionUID = 1853898575068451181L;
  private Logger logger;
  private ActionFactory actionsFactory;
  
  @Inject
  public void injectLogger(Logger logger){
    this.logger = logger;
  }
  
  @Inject 
  public void injectActionsFactory(ActionFactory actionsFactory) {
    this.actionsFactory = actionsFactory;
  }
  
  protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    try {
      Map<String, Object> params = LibraryRequest.parseParameters(request);
      String actionName = (String)params.get(RequestParams.REQUEST_PARAM_ACTION);
      this.logger.info("Controller.doProcess(%s)", actionName);
      Action action = this.actionsFactory.create(actionName);
      if(action != null) action.execute(params, new HttpResponse(response));
      else 
        this.logger.info("Action(%s) not found", actionName);
    } catch (DocServiceException e) {
      this.logger.error(e.getMessage(),e);
      response.sendError(500, e.getMessage());
    } catch (Exception e) {
      this.logger.error(e.getMessage(),e);
      response.setStatus(500);
    }
  }
  
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doProcess(request,response);
  }
  
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doProcess(request,response);
  }
}
