package org.monet.space.kernel.components.layers;

import org.monet.space.kernel.model.*;
import org.scribe.model.Token;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Map.Entry;

public interface FederationLayer extends Layer {

	interface Configuration {
		String getSessionId();
		String getCallbackUrl();
		String getLogoUrl();
		HttpServletRequest getRequest();
	}

	String getAuthorizationUrl();

	String getUserLanguage();

	boolean isLogged();

	boolean existsAccount(String code);

	Banner loadBanner();

	Account loadAccount();

	Account loadAccount(String id);

	Account locateAccount(String data);

	User loadUser(String id);

	boolean existsUserByUsername(String username);

	User loadUserByUsername(String username);

	User loadUserLogged();

	User loadUserLinkedToNode(Node node);

	String validateRequest(String signature, String hash);

	ValidationResult validateRequest(String signature, Long timestamp, ArrayList<Entry<String, Object>> parameters);

	void loginAsSystem();

	void login(String verifier);

	void logout();

	void saveAccount(String data);

	void saveAccount(Account account);

	Account createAccount(String id, String userName, UserInfo info);

	void removeAccount(String userCode);

	void removeAccounts(String users);

	UserList searchUsers(DataRequest dataRequest);

	UserList searchUsersWithRoles(DataRequest dataRequest);

	UserList searchFederationUsers(DataRequest dataRequest);

	FederationUnitList loadMembers();

	FederationUnitList loadMembers(Account account);

	FederationUnitList loadPartners();

	FederationUnit locatePartner(String name);

	FederationUnit loadPartner(String id);

	boolean existsAccessToken(Token token);

	void injectRequestToken(Token token);

	void injectAccessToken(Token token);

	void createOrUpdateAccount(Account account);

}
