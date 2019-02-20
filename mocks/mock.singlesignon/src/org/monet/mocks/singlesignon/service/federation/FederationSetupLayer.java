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

package org.monet.mocks.singlesignon.service.federation;

import org.monet.api.federation.setupservice.impl.FederationSetupApiImpl;
import org.monet.api.federation.setupservice.impl.model.FederationSocketInfo;
import org.monet.mocks.singlesignon.core.Configuration;

public class FederationSetupLayer {
	private Configuration configuration;

	private static final String SETUP_SERVICE_PATH = "/setupservice/";

	public FederationSetupLayer(Configuration configuration) {
		this.configuration = configuration;
	}

    public boolean ping(Federation federation) {
        String location = getFederationUrl(federation);
        FederationSetupApiImpl federationSetupApi = new FederationSetupApiImpl(location, configuration.getCertificateFilename(), configuration.getCertificatePassword());
        return federationSetupApi.ping();
    }

    public FederationSocketInfo getSocketInfo(Federation federation) {
        String location = getFederationUrl(federation);
        FederationSetupApiImpl federationSetupApi = new FederationSetupApiImpl(location, configuration.getCertificateFilename(), configuration.getCertificatePassword());
        return federationSetupApi.getSocketInfo();
    }

	private String getFederationUrl(Federation federation) {
		return federation.getUri() + SETUP_SERVICE_PATH;
	}

}