/*
    Monet will assist business to process re-engineering. Monet separate the
    business logic from the underlying technology to allow Model-Driven
    Engineering (MDE). These models guide all the development process over a
    Service Oriented Architecture (SOA).

    Copyright (C) 2009  Grupo de Ingenieria del Sofware y Sistemas de la Universidad de Las Palmas de Gran Canaria

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see http://www.gnu.org/licenses/.
 */

// TODO Manejar exepciones en los adaptadores

package org.monet.federation.setupservice.control;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.monet.federation.accountoffice.core.components.certificatecomponent.CertificateComponent;
import org.monet.federation.accountoffice.core.logger.Logger;
import org.monet.federation.setupservice.control.actions.ActionSetupService;
import org.monet.federation.setupservice.control.actions.ActionsFactorySetupService;
import org.monet.federation.setupservice.control.constants.Parameter;
import org.monet.http.LibraryRequest;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ControllerSetupService extends HttpServlet {
  private static final long serialVersionUID = -6606533421447261452L;
  
  private Logger                     logger;
  private ActionsFactorySetupService actionFactory;
  private CertificateComponent      verificationComponent;

  @Inject
  public void injectLogger(Logger logger) {
    this.logger = logger;
  }

  @Inject
  public void injecActionsFactorySetupService(ActionsFactorySetupService actionFactory) {
    this.actionFactory = actionFactory;
  }

  @Inject
  public void injecVerificationComponent(CertificateComponent verificationComponent) {
    this.verificationComponent = verificationComponent;
  }

  private Boolean doRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String result, operation;
    ActionSetupService action;
    HashMap<String, Object> parameters = LibraryRequest.parseParameters(request);
    String signature, timestamp, hash;

    try {

      if ((operation = (String) parameters.get(Parameter.OPERATION)) == null) {
        response.getWriter().print("invalid request");
        return false;
      }

      signature = (String)parameters.get(Parameter.SIGNATURE);
      timestamp = (String)parameters.get(Parameter.TIMESTAMP);
      hash = (String)parameters.get(Parameter.HASH);
      
      if (signature == null) {
        response.setStatus(403);
        response.getWriter().println("No signature on request");
      }
      
      if (timestamp == null) {
        response.setStatus(403);
        response.getWriter().println("No timestamp on request");
        return false;
      }
      
      if (hash == null) {
        response.setStatus(403);
        response.getWriter().println("No hash on request");
        return false;
      }

      if (!this.verificationComponent.checkRootCertificate(hash, signature, false)) {
        response.setStatus(403);
        response.getWriter().println("No valid certificate");
        return false;
      }

      action = actionFactory.get(operation, request, response, parameters);
      result = action.execute();
    } catch (Exception e) {
      response.setStatus(500);
      response.getWriter().print(e.getMessage());
      logger.error(e.getMessage(), e);
      return false;
    }

    if (result != null)
      response.getWriter().print(result);

    return true;
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    logger.info("SetupService.Controller.doPost()");
    this.doRequest(request, response);
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    logger.info("SetupService.Controller.doPost()");
    this.doRequest(request, response);
  }
}
