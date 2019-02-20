package org.monet.api.federation.setupservice;

import org.monet.api.federation.setupservice.impl.model.*;

import java.io.InputStream;
import java.net.URI;

public interface FederationSetupApi {

    public String getVersion();

    public Status getStatus();

    public void run();

    public void stop();

    public boolean ping();

    public FederationSocketInfo getSocketInfo();

    public FederationInfo getInfo();

    public void putLabel(String label);

    public void putLogo(InputStream logo);

    public void registerPartner(String name, String label, URI uri, Federation federation, ServiceList serviceList, FeederList feederList);

    public void unregisterPartner(String name, Federation federation);

    public void registerTrustedFederation(Federation trustedFederation);

}
