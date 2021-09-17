package org.monet.space.kernel.deployer.stages;

import org.monet.api.federation.setupservice.impl.model.FederationInfo;
import org.monet.metamodel.AbstractManifestBase.FederationProperty;
import org.monet.metamodel.AbstractManifestBase.PublishProperty;
import org.monet.metamodel.AbstractManifestBase.UnpublishProperty;
import org.monet.metamodel.Distribution;
import org.monet.metamodel.Project;
import org.monet.metamodel.ServiceDefinition;
import org.monet.metamodel.SourceDefinition;
import org.monet.metamodel.internal.Ref;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.components.ComponentFederation;
import org.monet.space.kernel.components.layers.FederationSetupLayer;
import org.monet.space.kernel.configuration.Configuration;
import org.monet.space.kernel.deployer.Stage;
import org.monet.space.kernel.model.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;

public class SetupSpace extends Stage {

	@Override
	public void execute() {
		Language language = Language.getInstance();
		ComponentFederation componentFederation = ComponentFederation.getInstance();
		FederationSetupLayer federationSetupLayer = componentFederation.getSetupLayer();
		Federation oldFederation, newFederation;
		BusinessUnit businessUnit = BusinessUnit.getInstance();
		Distribution distribution = businessUnit.getDistribution();
		Project project = BusinessModel.getInstance().getProject();
		FederationProperty federationProperty = distribution.getFederation();
		boolean isSameFederation;

		if (federationProperty == null)
			throw new RuntimeException("Distribution file corrupt in model. Try clean eclipse project and upload again.");

		oldFederation = businessUnit.getFederation();
		newFederation = new Federation(federationProperty.getUri().toString(), federationProperty.getSecret());
		isSameFederation = oldFederation != null && oldFederation.equals(newFederation);

		if (!isSameFederation)
			businessUnit.setFederation(newFederation);

		businessUnit.setName(distribution.getSpace().getName());
		businessUnit.setLabel(BusinessUnit.getSubTitle(distribution, project));
		businessUnit.setUpdateInfo(new BusinessUnit.UpdateInfo(new Date()));
		businessUnit.save();

		ComponentFederation.getInstance().reset(true);

		try {
			this.unpublishFromFederations(distribution, businessUnit, federationSetupLayer);
			this.publishInFederations(distribution, businessUnit, federationSetupLayer);
		} catch (URISyntaxException e) {
			AgentLogger.getInstance().error(e);
		}

		if (!isSameFederation)
			componentFederation.reset();
	}

	private void unpublishFromFederations(Distribution distribution, BusinessUnit businessUnit, FederationSetupLayer federationSetupLayer) {
		for (UnpublishProperty unpublishDefinition : distribution.getUnpublishList()) {
			Federation federation = new Federation(unpublishDefinition.getFederation().getSetupUri().toString(), null);
			federationSetupLayer.unregisterPartner(federation, businessUnit.getName(), businessUnit.getFederation());
		}
	}

	private void publishInFederations(Distribution distribution, BusinessUnit businessUnit, FederationSetupLayer federationSetupLayer) throws URISyntaxException {
		Dictionary dictionary = Dictionary.getInstance();
		Federation localFederation = businessUnit.getFederation();
		String name = businessUnit.getName();
		String label = businessUnit.getLabel();
		URI uri = new URI(Configuration.getInstance().getServicesBaseUrl());

		for (PublishProperty publishDefinition : distribution.getPublishList()) {
			Federation federation = new Federation(publishDefinition.getFederation().getSetupUri().toString(), null);
			FederationInfo federationInfo = federationSetupLayer.getInfo(federation);

			federation.setName(federationInfo.getName());
			federation.setLabel(federationInfo.getLabel());

			ArrayList<Federation.Service> services = new ArrayList<Federation.Service>();
			for (Ref serviceRef : publishDefinition.getService()) {
				ServiceDefinition definition = (ServiceDefinition) dictionary.getTaskDefinition(serviceRef.getValue());
				Federation.Service service = new Federation.Service(definition.getName(), definition.getLabelString(), definition.getOntology());
				services.add(service);
			}

			ArrayList<Federation.Feeder> feeders = new ArrayList<Federation.Feeder>();
			for (Ref sourceRef : publishDefinition.getSource()) {
				SourceDefinition definition = (SourceDefinition) dictionary.getSourceDefinition(sourceRef.getValue());
				Federation.Feeder feeder = new Federation.Feeder(definition.getName(), definition.getLabelString(), definition.getOntology());
				feeders.add(feeder);
			}

			federationSetupLayer.registerPartner(federation, name, label, uri, businessUnit.getFederation(), services, feeders);
			federationSetupLayer.registerTrustedFederation(localFederation, federation);
		}
	}

	@Override
	public String getStepInfo() {
		return "Setting up space";
	}
}
