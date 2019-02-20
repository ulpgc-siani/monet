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

package org.monet.federation.setupservice.control.actions;

import com.google.inject.Inject;
import org.monet.federation.accountoffice.core.components.templatecomponent.TemplateFactory;
import org.monet.federation.accountoffice.core.configuration.Configuration;
import org.monet.federation.accountoffice.core.database.DataRepository;
import org.monet.federation.accountoffice.core.logger.Logger;
import org.monet.federation.setupservice.core.model.FederationStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.HashMap;

public abstract class ActionSetupService {

    public static final String OK = "OK";
    public static final String TRUE = Boolean.TRUE.toString();
    public static final String FALSE = Boolean.FALSE.toString();

    protected Logger logger;
    protected Configuration configuration;
    protected FederationStatus federationStatus;
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected HashMap<String, Object> parameters;
    protected String sender;
    protected String idSession;
    protected String codeLanguage;
    protected DataRepository dataRepository;
    protected TemplateFactory templateFactory;

    @Inject
    public void injectDataRepository(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @Inject
    public void injectTemplateFactory(TemplateFactory templateFactory) {
        this.templateFactory = templateFactory;
    }

    public ActionSetupService() {
    }

    protected Boolean initLanguage() {
        this.codeLanguage = null;
        if (this.codeLanguage == null)
            this.codeLanguage = this.request.getLocale().getLanguage();
        return true;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
        this.idSession = request.getSession().getId();
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public void setParameters(HashMap<String, Object> parameters) {
        this.parameters = parameters;
    }

    public Boolean initialize(Logger logger, Configuration configuration, FederationStatus federationStatus) {
        this.logger = logger;
        this.configuration = configuration;
        this.federationStatus = federationStatus;
        this.response.setContentType("text/html;charset=UTF-8");
        this.initLanguage();
        return true;
    }

    protected String decode(String data) {
        try {
            if (data == null)
                return null;

            return URLDecoder.decode(data, "utf-8");
        } catch (Exception exception) {
            return data;
        }
    }

    public abstract String execute() throws Exception;

}