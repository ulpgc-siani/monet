package org.monet.space.mobile.control.actions;

import org.monet.mobile.exceptions.ActionException;
import org.monet.mobile.service.errors.NotLoggedError;
import org.monet.mobile.service.requests.RegisterRequest;
import org.monet.mobile.service.results.AckResult;
import org.monet.space.kernel.components.ComponentFederation;
import org.monet.space.mobile.library.LibraryRequest;
import org.scribe.model.Token;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class ActionDoRegister extends AuthenticatedTypedAction<RegisterRequest, AckResult> {

	public ActionDoRegister() {
		super(RegisterRequest.class);
	}

	@Override
	public AckResult execute(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Exception {
		federationLayer = ComponentFederation.getInstance().getLayer(createConfiguration(httpRequest));
		Map<String, String> authElements = LibraryRequest.extractAuthElements(httpRequest);

		try {
			federationLayer.injectAccessToken(new Token(authElements.get(LibraryRequest.OAUTH_TOKEN), null));
		} catch (Exception ex) {
			throw new ActionException(new NotLoggedError());
		}

		if (!this.federationLayer.isLogged())
			throw new ActionException(new NotLoggedError());

		return onExecute(getRequest(httpRequest, RegisterRequest.class));
	}

	@Override
	protected AckResult onExecute(RegisterRequest request) throws ActionException {
		try {
			ComponentFederation.getInstance().getFederationService().registerMobileDevice(getAccount().getId(), request.registrationId);
		} catch (Exception e) {
			agentLogger.error(e);
		}
		return new AckResult();
	}

}
