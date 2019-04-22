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

package org.monet.space.kernel.components.monet.federation;

import org.monet.api.federation.setupservice.impl.model.FederationSocketInfo;
import org.monet.federation.accountservice.accountactions.impl.messagemodel.FederationInfo;
import org.monet.federation.accountservice.client.FederationService;
import org.monet.http.Request;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.components.ComponentFederation;
import org.monet.space.kernel.components.layers.FederationLayer;
import org.monet.space.kernel.components.layers.FederationSetupLayer;
import org.monet.space.kernel.components.layers.RoleLayer;
import org.monet.space.kernel.components.monet.layers.FederationLayerMonet;
import org.monet.space.kernel.components.monet.layers.FederationSetupLayerMonet;
import org.monet.space.kernel.components.monet.layers.MasterLayerMonet;
import org.monet.space.kernel.components.monet.layers.RoleLayerMonet;
import org.monet.space.kernel.constants.ErrorCode;
import org.monet.space.kernel.exceptions.FederationConnectionException;
import org.monet.space.kernel.exceptions.SystemException;
import org.monet.space.kernel.model.AccountServiceProviderImpl;
import org.monet.space.kernel.model.BusinessUnit;
import org.monet.space.kernel.model.Federation;

import java.net.URI;
import java.util.HashMap;
import java.util.UUID;

public class ComponentFederationMonet extends ComponentFederation {
	private FederationService service;

	private ComponentFederationMonet() {
		super();
		masterLayer = new MasterLayerMonet(this);
	}

	public synchronized static ComponentFederation getInstance() {
		if (instance == null) instance = new ComponentFederationMonet();
		return instance;
	}

	@Override
	public HashMap<Integer, Boolean> getSupportedFeatures() {
		return new HashMap<Integer, Boolean>();
	}

	@Override
	public void run() throws SystemException {
		if (this.isRunning) return;
		this.isRunning = true;
	}

	@Override
	public void stop() throws SystemException {
		if (!this.isRunning) return;
		instance = null;
		this.isRunning = false;
	}

	private void initFederation(FederationLayerMonet.Configuration configuration) {

		if (this.isFederationInitialized()) return;

		BusinessUnit businessUnit = BusinessUnit.getInstance();
		Federation federation = businessUnit.getFederation();
		String spaceInfo = "";
		String label = businessUnit.getLabel();
		FederationInfo federationInfo;
		URI uri = null;

		if (!businessUnit.isInstalled())
			return;

		spaceInfo = "<info><label>" + label + "</label><logo-url>" + (configuration!=null && configuration.getLogoUrl()!=null?configuration.getLogoUrl().replaceAll("&", "&amp;"):"") + "</logo-url></info>";

		try {
			uri = new URI(federation.getUri());

            FederationSetupLayer federationSetupLayer = this.getSetupLayer();

            AgentLogger.getInstance().info("Connecting with federation...");
            if (!federationSetupLayer.ping(federation)) {
                AgentLogger.getInstance().info("Federation is down! Trying later...");
                return;
            }

			AgentLogger.getInstance().info("Connected with Federation!");

            FederationSocketInfo socketInfo = federationSetupLayer.getSocketInfo(federation);
            if (socketInfo == null) {
                AgentLogger.getInstance().error(String.format("Could not connect to federation %s", (uri != null) ? uri.toString() : ""), null);
                throw new FederationConnectionException(ErrorCode.FEDERATION_CONNECTION, String.format("URI: %s", (uri != null) ? uri.toString() : ""));
            }

			this.service = new FederationService(socketInfo.getHost(), socketInfo.getPort(), businessUnit.getName(), spaceInfo);
			this.service.init();

			AccountServiceProviderImpl.getInstance().setAccountService(this.service);

			federationInfo = this.service.getInfo();
			federation.setName(federationInfo.getName());
			federation.setLabel(federationInfo.getLabel());
            federation.setLogoPath(federationInfo.getLogoPath());

		} catch (Exception e) {
			AgentLogger.getInstance().error(e);
			throw new FederationConnectionException(ErrorCode.FEDERATION_CONNECTION, String.format("URI: %s", (uri != null) ? uri.toString() : ""));
		}

	}

	private boolean isFederationInitialized() {

		if (this.service == null)
			return false;

		return true;
	}

	@Override
	public FederationService getFederationService() {
		if (this.service == null)
			this.reset(true);
		return this.service;
	}

	@Override
	public Federation getFederation() {
		this.initFederation(null);
		return BusinessUnit.getInstance().getFederation();
	}

	@Override
	public FederationSetupLayer getSetupLayer() {
		return new FederationSetupLayerMonet();
	}

    @Override
    public FederationLayer getDefaultLayer() {
        return this.getLayer(new FederationLayer.Configuration() {
	        @Override
	        public String getSessionId() {
		        return UUID.randomUUID().toString();
	        }

	        @Override
	        public String getCallbackUrl() {
		        return "http://localhost";
	        }

	        @Override
	        public String getLogoUrl() {
		        return null;
	        }

	        @Override
	        public Request getRequest() {
		        return null;
	        }
        });
    }

	@Override
	public FederationLayer getLayer(FederationLayer.Configuration configuration) {
		this.initFederation(configuration);
		return new FederationLayerMonet(configuration, this);
	}

	@Override
	public RoleLayer getRoleLayer() {
		this.initFederation(null);
		return new RoleLayerMonet(this);
	}

	public void reset(boolean force) {

		if (!force && !this.isFederationInitialized())
			return;

		this.initFederation(null);
	}

	@Override
	public void reset() {
		// TODO create new certificate and send to federation service in order to sign the certificate
	}

	@Override
	public void create() {
	}

	@Override
	public void destroy() {
		try {
			if (this.service != null)
				this.service.close();
		} catch (Exception e) {
			AgentLogger.getInstance().error(e);
		}
	}

}