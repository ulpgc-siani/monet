package org.monet.space.mobile.control.actions;

import org.monet.mobile.exceptions.ActionException;
import org.monet.mobile.service.requests.HeloRequest;
import org.monet.mobile.service.results.HeloResult;
import org.monet.space.kernel.components.monet.federation.FederationApi;
import org.monet.space.kernel.model.BusinessUnit;

public class ActionDoHelo extends TypedAction<HeloRequest, HeloResult> {

	public ActionDoHelo() {
		super(HeloRequest.class);
	}

	@Override
	protected HeloResult onExecute(HeloRequest request) throws ActionException {
		HeloResult result = new HeloResult();
		BusinessUnit businessUnit = BusinessUnit.getInstance();
		FederationApi federationApi = this.getFederationApi();

		result.setFederation(businessUnit.getFederation().getLabel());
		result.setFederationUrl(federationApi.getUrl());

		result.setBusinessUnit(businessUnit.getName());
		result.setBusinessUnitLabel(businessUnit.getLabel());

		result.setTitle(businessUnit.getTitle());
		result.setSubtitle(businessUnit.getSubTitle());

		return result;
	}

}
