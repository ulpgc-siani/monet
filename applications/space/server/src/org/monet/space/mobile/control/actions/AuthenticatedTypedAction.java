package org.monet.space.mobile.control.actions;

import org.monet.mobile.exceptions.ActionException;
import org.monet.mobile.service.Request;
import org.monet.mobile.service.Result;
import org.monet.mobile.service.errors.NotLoggedError;
import org.monet.space.mobile.library.LibraryRequest;
import org.monet.space.mobile.model.MobileProfile;
import org.monet.space.kernel.components.ComponentFederation;
import org.monet.space.kernel.components.layers.FederationLayer;
import org.monet.space.kernel.model.Account;
import org.monet.space.kernel.model.Profile;
import org.scribe.model.Token;

import java.util.Map;

public abstract class AuthenticatedTypedAction<T extends Request, U extends Result> extends TypedAction<T, U> {

	protected FederationLayer federationLayer;

	public AuthenticatedTypedAction(Class<T> requestClass) {
		super(requestClass);
	}

	protected Boolean loadProfile(Account account) {
		MobileProfile profile;
		profile = new MobileProfile();
		account.setProfile(Profile.MOBILE, profile);
		return true;
	}

	public Account getAccount() {
		Account account = this.federationLayer.loadAccount();
		MobileProfile profile = (MobileProfile) account.getProfile(Profile.MOBILE);

		if (profile == null) {
			this.loadProfile(account);
		}

		return account;
	}

	@Override
	public U execute(org.monet.http.Request httpRequest, org.monet.http.Response httpResponse) throws Exception {
		this.federationLayer = ComponentFederation.getInstance().getLayer(createConfiguration(httpRequest));
		Map<String, String> authElements = LibraryRequest.extractAuthElements(httpRequest);

		try {
			String requestToken = authElements.get(LibraryRequest.OAUTH_TOKEN);

			if (!this.federationLayer.existsAccessToken(new Token(requestToken, null)))
				throw new ActionException(new NotLoggedError());

			this.federationLayer.injectAccessToken(new Token(requestToken, null));
		} catch (Exception ex) {
			throw new ActionException(new NotLoggedError());
		}

		if (!this.federationLayer.isLogged()) {
			throw new ActionException(new NotLoggedError());
		}

		return super.execute(httpRequest, httpResponse);
	}

}
