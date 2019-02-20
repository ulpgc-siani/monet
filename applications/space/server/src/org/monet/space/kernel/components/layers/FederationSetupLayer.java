package org.monet.space.kernel.components.layers;

import org.monet.api.federation.setupservice.impl.model.FederationInfo;
import org.monet.api.federation.setupservice.impl.model.FederationSocketInfo;
import org.monet.space.kernel.model.Federation;

import java.net.URI;
import java.util.List;

public interface FederationSetupLayer extends Layer {

    public abstract boolean ping(Federation federation);

    public abstract FederationSocketInfo getSocketInfo(Federation federation);

	public abstract FederationInfo getInfo(Federation federation);

	public abstract void registerPartner(Federation federation, String unit, String unitLabel, URI unitUri, Federation unitFederation, List<Federation.Service> services, List<Federation.Feeder> feeders);

	public abstract void unregisterPartner(Federation federation, String unit, Federation unitFederation);

	public abstract void registerTrustedFederation(Federation federation, Federation trustedFederation);

}
