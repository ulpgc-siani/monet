package org.monet.federation.accountoffice.control.sockets.impl;

import com.google.inject.Inject;
import com.google.inject.Injector;

import java.util.HashMap;
import java.util.Map;

public class AccountSocketFactory {

	private Map<String, ActionAccountSocket> actions;

	@Inject
	public AccountSocketFactory(Injector injector) {
		this.actions = new HashMap<String, ActionAccountSocket>();
		this.actions.put(ActionAccountSocket.ACTION_INIT, injector.getInstance(ActionInitCommunication.class));
		this.actions.put(ActionAccountSocket.ACTION_PING, injector.getInstance(ActionPing.class));
		this.actions.put(ActionAccountSocket.ACTION_IS_LOGGED, injector.getInstance(ActionIsLogged.class));
		this.actions.put(ActionAccountSocket.ACTION_LOGUT, injector.getInstance(ActionLogout.class));
		this.actions.put(ActionAccountSocket.ACTION_GENERATE_ACCOUNT_ID, injector.getInstance(ActionGenerateAccountId.class));
		this.actions.put(ActionAccountSocket.ACTION_GET_ACCOUNT, injector.getInstance(ActionGetAccount.class));
		this.actions.put(ActionAccountSocket.ACTION_EXISTS_ACCOUNT, injector.getInstance(ActionExistsAccount.class));
		this.actions.put(ActionAccountSocket.ACTION_EXISTS_PARTNER, injector.getInstance(ActionExistsPartner.class));
		this.actions.put(ActionAccountSocket.ACTION_VALIDATE_REQUEST, injector.getInstance(ActionValidateRequest.class));
		this.actions.put(ActionAccountSocket.ACTION_SIGN_CERTIFICATE, injector.getInstance(ActionSignCertificate.class));
		this.actions.put(ActionAccountSocket.ACTION_GET_INFO, injector.getInstance(ActionGetInfo.class));
		this.actions.put(ActionAccountSocket.ACTION_SEARCH_USERS, injector.getInstance(ActionSearchUsers.class));
		this.actions.put(ActionAccountSocket.ACTION_LOAD_MEMBERS, injector.getInstance(ActionLoadMembers.class));
		this.actions.put(ActionAccountSocket.ACTION_LOAD_PARTNERS, injector.getInstance(ActionLoadPartners.class));
		this.actions.put(ActionAccountSocket.ACTION_LOAD_PARTNER, injector.getInstance(ActionLoadPartner.class));
		this.actions.put(ActionAccountSocket.ACTION_LOCATE_PARTNER, injector.getInstance(ActionLocatePartner.class));
		this.actions.put(ActionAccountSocket.ACTION_LOAD_MOBILE_DEVICES, injector.getInstance(ActionLoadMobileDevices.class));
		this.actions.put(ActionAccountSocket.ACTION_LOAD_USER_MOBILE_DEVICES, injector.getInstance(ActionLoadUserMobileDevices.class));
		this.actions.put(ActionAccountSocket.ACTION_REGISTER_MOBILE_DEVICE, injector.getInstance(ActionRegisterMobileDevice.class));
		this.actions.put(ActionAccountSocket.ACTION_UNREGISTER_MOBILE_DEVICE, injector.getInstance(ActionUnRegisterMobileDevice.class));
	}

	public ActionAccountSocket getAction(String action) {
		return this.actions.get(action);
	}
}
