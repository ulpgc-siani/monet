package org.monet.federation.accountoffice.control.sockets.impl;

import org.monet.federation.accountservice.accountactions.impl.messagemodel.SocketMessageModel;
import org.monet.federation.accountservice.accountactions.impl.messagemodel.SocketResponseMessage;

public interface ActionAccountSocket {

	public static final String ACTION_INIT = "InitRequest";
	public static final String ACTION_PING = "PingRequest";
	public static final String ACTION_IS_LOGGED = "IsLogged";
	public static final String ACTION_LOGUT = "Logout";
	public static final String ACTION_GENERATE_ACCOUNT_ID = "GenerateAccountId";
	public static final String ACTION_GET_ACCOUNT = "GetAccount";
	public static final String ACTION_EXISTS_ACCOUNT = "ExistsAccount";
	public static final String ACTION_EXISTS_PARTNER = "ExistsPartner";
	public static final String ACTION_VALIDATE_REQUEST = "ValidateRequest";
	public static final String ACTION_SIGN_CERTIFICATE = "SignCertificate";
	public static final String ACTION_CLOSE = "Close";
	public static final String ACTION_GET_INFO = "GetInfo";
	public static final String ACTION_SEARCH_USERS = "SearchUsers";
	public static final String ACTION_LOAD_MEMBERS = "LoadMembers";
	public static final String ACTION_LOAD_PARTNERS = "LoadPartners";
	public static final String ACTION_LOAD_PARTNER = "LoadPartner";
	public static final String ACTION_LOCATE_PARTNER = "LocatePartner";
	public static final String ACTION_LOAD_MOBILE_DEVICES = "LoadMobileDevices";
	public static final String ACTION_LOAD_USER_MOBILE_DEVICES = "LoadUserMobileDevices";
	public static final String ACTION_REGISTER_MOBILE_DEVICE = "RegisterMobileDevice";
	public static final String ACTION_UNREGISTER_MOBILE_DEVICE = "UnRegisterMobileDevice";

	SocketResponseMessage execute(SocketMessageModel socketMessage);

}
