package org.monet.space.kernel.model;

public class FederationUnitFeeder extends AbstractService {

	public FeederUri getUri(FederationUnit partner) {
		return partner.getFeederUri(this.getName());
	}

}