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
import com.google.inject.Injector;
import org.monet.federation.accountoffice.core.configuration.Configuration;
import org.monet.federation.accountoffice.core.logger.Logger;
import org.monet.federation.setupservice.control.constants.Actions;
import org.monet.federation.setupservice.core.model.FederationStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public class ActionsFactorySetupService {
    private HashMap<String, Object> actions;

    protected Injector injector;
    protected Logger logger;
    protected Configuration configuration;
    protected FederationStatus federationStatus;

    @Inject
    public ActionsFactorySetupService(Injector injector, Logger logger, Configuration configuration, FederationStatus federationStatus) {
        this.injector = injector;
        this.actions = new HashMap<String, Object>();
        this.logger = logger;
        this.configuration = configuration;
        this.federationStatus = federationStatus;
        this.registerActions();
    }

    private void registerActions() {
        this.register(Actions.SHOW_API, ActionShowApi.class);
        this.register(Actions.PING, ActionPing.class);
        this.register(Actions.GET_SOCKET_INFO, ActionGetSocketInfo.class);
        this.register(Actions.GET_INFO, ActionGetInfo.class);
        this.register(Actions.GET_VERSION, ActionGetVersion.class);
        this.register(Actions.GET_STATUS, ActionGetStatus.class);
        this.register(Actions.RUN, ActionRun.class);
        this.register(Actions.STOP, ActionStop.class);
        this.register(Actions.PUT_LABEL, ActionPutLabel.class);
        this.register(Actions.PUT_LOGO, ActionPutLogo.class);
        this.register(Actions.REGISTER_PARTNER, ActionRegisterPartner.class);
        this.register(Actions.UNREGISTER_PARTNER, ActionUnregisterPartner.class);
        this.register(Actions.REGISTER_TRUSTED_FEDERATION, ActionRegisterTrustedFederation.class);
    }

    public String getOperation(String op) {
        if (this.actions.containsKey(op)) return op;
        return Actions.SHOW_API;
    }

    public ActionSetupService get(String type, HttpServletRequest request, HttpServletResponse response, HashMap<String, Object> parameters) throws InstantiationException, IllegalAccessException {
        Class<?> clazz;
        ActionSetupService action = null;

        clazz = (Class<?>) this.actions.get(type);
        action = (ActionSetupService) this.injector.getInstance(clazz);

        action.setRequest(request);
        action.setResponse(response);
        action.setParameters(parameters);
        action.initialize(this.logger, this.configuration, this.federationStatus);

        return action;
    }

    public Boolean register(String sType, Object cAction)
            throws IllegalArgumentException {

        if ((cAction == null) || (sType == null)) {
            return false;
        }
        this.actions.put(sType, cAction);

        return true;
    }

}
