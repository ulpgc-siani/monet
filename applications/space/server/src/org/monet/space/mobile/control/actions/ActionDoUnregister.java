package org.monet.space.mobile.control.actions;

import org.monet.mobile.exceptions.ActionException;
import org.monet.mobile.service.requests.UnregisterRequest;
import org.monet.mobile.service.results.AckResult;
import org.monet.space.kernel.components.ComponentFederation;
import org.monet.space.kernel.model.Account;

public class ActionDoUnregister extends AuthenticatedTypedAction<UnregisterRequest, AckResult> {

	public ActionDoUnregister() {
		super(UnregisterRequest.class);
	}

	@Override
	protected AckResult onExecute(UnregisterRequest request) throws ActionException {
		Account account = this.getAccount();

		ComponentFederation componentFederation = ComponentFederation.getInstance();
		try {
			componentFederation.getFederationService().unRegisterMobileDevice(account.getId(), request.registrationId);
		} catch (Exception e) {
			agentLogger.error(e);
		}

		return new AckResult();
	}

}
