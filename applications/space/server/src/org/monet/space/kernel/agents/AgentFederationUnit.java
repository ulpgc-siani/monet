package org.monet.space.kernel.agents;

import org.monet.space.kernel.components.ComponentFederation;
import org.monet.space.kernel.components.layers.FederationLayer;
import org.monet.space.kernel.model.*;
import org.monet.space.kernel.model.FeederUri;
import org.monet.space.kernel.model.Uri;

public class AgentFederationUnit {

	protected AgentFederationUnit() {
	}

    public static FederationUnit locate(Uri uri) {
        FederationLayer federationLayer = ComponentFederation.getInstance().getDefaultLayer();

        FederationUnit partner = federationLayer.locatePartner(uri.getPartner());

        if (partner == null)
            return null;

        return partner;
    }

    public static String getServiceUrl(FeederUri uri) {
        FederationUnit federationUnit = locate(uri);
        return federationUnit.getServiceUrl(uri.getId());
    }

    public static String getFeederUrl(FeederUri uri) {
        FederationUnit federationUnit = locate(uri);
        return federationUnit.getFeederUrl(uri.getId());
    }

    public static String getMailBoxUrl(FeederUri uri) {
        FederationUnit federationUnit = locate(uri);
        return federationUnit.getMailBoxUrl(uri.getId());
    }

    public static String getUrl(String uri) {

        if (!Uri.isMonetUri(uri))
            return uri;

        return getUrl(Uri.build(uri));
    }

    public static String getUrl(Uri uri) {

        if (uri == null)
            return null;

        FederationUnit federationUnit = locate(uri);
        if (federationUnit == null)
            return null;

        return federationUnit.getUrl(uri);
    }

}
