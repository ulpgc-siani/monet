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

package org.monet.space.kernel.components.monet.layers;

import org.monet.api.federation.setupservice.impl.FederationSetupApiImpl;
import org.monet.api.federation.setupservice.impl.model.*;
import org.monet.space.kernel.components.layers.FederationSetupLayer;
import org.monet.space.kernel.components.monet.federation.ComponentFederationMonet;
import org.monet.space.kernel.configuration.Configuration;
import org.monet.space.kernel.model.Federation;

import java.net.URI;
import java.util.List;

public class FederationSetupLayerMonet extends LayerMonet implements FederationSetupLayer {

	private static final String SETUP_SERVICE_PATH = "/setupservice/";

	public FederationSetupLayerMonet() {
	}

	protected boolean isStarted() {
		return ComponentFederationMonet.started();
	}

    @Override
    public boolean ping(Federation federation) {
        String location = getFederationUrl(federation);
        Configuration configuration = Configuration.getInstance();
        FederationSetupApiImpl federationSetupApi = new FederationSetupApiImpl(location, configuration.getCertificateFilename(), configuration.getCertificatePassword());
        return federationSetupApi.ping();
    }

    @Override
    public FederationSocketInfo getSocketInfo(Federation federation) {
        String location = getFederationUrl(federation);
        Configuration configuration = Configuration.getInstance();
        FederationSetupApiImpl federationSetupApi = new FederationSetupApiImpl(location, configuration.getCertificateFilename(), configuration.getCertificatePassword());
        return federationSetupApi.getSocketInfo();
    }

	@Override
	public FederationInfo getInfo(Federation federation) {
		String location = getFederationUrl(federation);
		Configuration configuration = Configuration.getInstance();
		FederationSetupApiImpl federationSetupApi = new FederationSetupApiImpl(location, configuration.getCertificateFilename(), configuration.getCertificatePassword());
		return federationSetupApi.getInfo();
	}

	@Override
	public void registerPartner(Federation federation, String unit, String unitLabel, URI unitURI, Federation unitFederation, List<Federation.Service> services, List<Federation.Feeder> feeders) {
		String location = this.getFederationUrl(federation);
		Configuration configuration = Configuration.getInstance();
		FederationSetupApiImpl federationSetupApi = new FederationSetupApiImpl(location, configuration.getCertificateFilename(), configuration.getCertificatePassword());
		ServiceList serviceList = new ServiceList();
		FeederList feederList = new FeederList();
		org.monet.api.federation.setupservice.impl.model.Federation unitFederationStub = new org.monet.api.federation.setupservice.impl.model.Federation(unitFederation.getName(), unitFederation.getUri(), unitFederation.getLabel());

		for (Federation.Service federationService : services) {
			Service service = new Service(federationService.getName(), federationService.getLabel(), federationService.getOntology());
			serviceList.add(service);
		}

		for (Federation.Feeder federationFeeder : feeders) {
			Feeder feeder = new Feeder(federationFeeder.getId(), federationFeeder.getLabel(), federationFeeder.getOntology());
			feederList.add(feeder);
		}

		federationSetupApi.registerPartner(unit, unitLabel, unitURI, unitFederationStub, serviceList, feederList);
	}

	@Override
	public void unregisterPartner(Federation federation, String unit, Federation unitFederation) {
		String location = this.getFederationUrl(federation);
		Configuration configuration = Configuration.getInstance();
		FederationSetupApiImpl federationSetupApi = new FederationSetupApiImpl(location, configuration.getCertificateFilename(), configuration.getCertificatePassword());
		org.monet.api.federation.setupservice.impl.model.Federation unitFederationStub = new org.monet.api.federation.setupservice.impl.model.Federation(unitFederation.getName(), unitFederation.getUri(), unitFederation.getLabel());
		federationSetupApi.unregisterPartner(unit, unitFederationStub);
	}

	@Override
	public void registerTrustedFederation(Federation federation, Federation trustedFederation) {
		String location = this.getFederationUrl(federation);
		Configuration configuration = Configuration.getInstance();
		FederationSetupApiImpl federationSetupApi = new FederationSetupApiImpl(location, configuration.getCertificateFilename(), configuration.getCertificatePassword());
		org.monet.api.federation.setupservice.impl.model.Federation trustedFederationStub = new org.monet.api.federation.setupservice.impl.model.Federation(trustedFederation.getName(), trustedFederation.getUri(), trustedFederation.getLabel());
		federationSetupApi.registerTrustedFederation(trustedFederationStub);
	}

	private String getFederationUrl(Federation federation) {
		return federation.getUri() + SETUP_SERVICE_PATH;
	}

}