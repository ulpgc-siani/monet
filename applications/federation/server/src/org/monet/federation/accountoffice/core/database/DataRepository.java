package org.monet.federation.accountoffice.core.database;

import org.monet.federation.accountoffice.core.model.*;
import org.monet.federation.accountservice.accountactions.impl.messagemodel.DeviceList;

import java.util.Date;
import java.util.HashMap;

public interface DataRepository {
	public static final String LOAD_FEDERATIONS = "LOAD_FEDERATIONS";
	public static final String LOAD_TRUSTED_FEDERATIONS = "LOAD_TRUSTED_FEDERATIONS";
	public static final String LOAD_FEDERATION = "LOAD_FEDERATION";
	public static final String LOAD_FEDERATION_BY_ID = "LOAD_FEDERATION_BY_ID";
	public static final String EXISTS_FEDERATION = "EXISTS_FEDERATION";
	public static final String CREATE_FEDERATION = "CREATE_FEDERATION";
	public static final String SAVE_FEDERATION = "SAVE_FEDERATION";
	public static final String REMOVE_FEDERATION = "REMOVE_FEDERATION";

	public static final Object CREATE_FEDERATION_TRUST_REQUEST = "CREATE_FEDERATION_TRUST_REQUEST";
	public static final Object EXISTS_FEDERATION_TRUST_REQUEST = "EXISTS_FEDERATION_TRUST_REQUEST";
	public static final Object REMOVE_FEDERATION_TRUST_REQUEST = "REMOVE_FEDERATION_TRUST_REQUEST";

	public static final String LOAD_BUSINESS_UNITS = "LOAD_BUSINESS_UNITS";
	public static final String LOAD_MEMBER_BUSINESS_UNITS = "LOAD_MEMBER_BUSINESS_UNITS";
	public static final String LOAD_PARTNER_BUSINESS_UNITS = "LOAD_PARTNER_BUSINESS_UNITS";
	public static final String LOAD_MEMBER_BUSINESS_UNIT = "LOAD_MEMBER_BUSINESS_UNIT";
	public static final String LOAD_PARTNER_BUSINESS_UNIT = "LOAD_PARTNER_BUSINESS_UNIT";
	public static final String LOAD_BUSINESS_UNIT = "LOAD_BUSINESS_UNIT";
    public static final String EXISTS_BUSINESS_UNIT_BY_ID = "EXISTS_BUSINESS_UNIT_BY_ID";
	public static final String LOAD_BUSINESS_UNIT_BY_ID = "LOAD_BUSINESS_UNIT_BY_ID";
	public static final String EXISTS_BUSINESS_UNIT = "EXISTS_BUSINESS_UNIT";
	public static final String EXISTS_MEMBER_BUSINESS_UNIT = "EXISTS_MEMBER_BUSINESS_UNIT";
	public static final String CREATE_BUSINESS_UNIT = "CREATE_BUSINESS_UNIT";
	public static final String SAVE_BUSINESS_UNIT = "SAVE_BUSINESS_UNIT";
	public static final String REMOVE_BUSINESS_UNIT = "REMOVE_BUSINESS_UNIT";

	public static final Object CREATE_BUSINESS_UNIT_PARTNER_REQUEST = "CREATE_BUSINESS_UNIT_PARTNER_REQUEST";
	public static final Object EXISTS_BUSINESS_UNIT_PARTNER_REQUEST = "EXISTS_BUSINESS_UNIT_PARTNER_REQUEST";
	public static final Object REMOVE_BUSINESS_UNIT_PARTNER_REQUEST = "REMOVE_BUSINESS_UNIT_PARTNER_REQUEST";

	public static final String LOAD_BUSINESS_UNIT_SERVICES = "LOAD_BUSINESS_UNIT_SERVICES";
	public static final String CREATE_BUSINESS_UNIT_SERVICE = "CREATE_BUSINESS_UNIT_SERVICE";
	public static final String CLEAR_BUSINESS_UNIT_SERVICES = "CLEAR_BUSINESS_UNIT_SERVICES";

	public static final String LOAD_BUSINESS_UNIT_FEEDERS = "LOAD_BUSINESS_UNIT_FEEDERS";
	public static final String CREATE_BUSINESS_UNIT_FEEDER = "CREATE_BUSINESS_UNIT_FEEDER";
	public static final String CLEAR_BUSINESS_UNIT_FEEDERS = "CLEAR_BUSINESS_UNIT_FEEDERS";

	public static final String SEARCH_USERS = "SEARCH_USERS";
	public static final String SEARCH_USERS_COUNT = "SEARCH_USERS_COUNT";
	public static final Object EXISTS_USER = "EXISTS_USER";
	public static final String LOAD_USER = "LOAD_USER";
	public static final String LOAD_USER_FROM_USERNAME = "LOAD_USER_FROM_USERNAME";
	public static final String LOAD_USER_FROM_EMAIL = "LOAD_USER_FROM_EMAIL";
	public static final String CREATE_USER = "CREATE_USER";
	public static final String SAVE_USER = "SAVE_USER";

	public static final String SELECT_BANNED_IP = "SELECT_BANNED_IP";
	public static final String UPDATE_BANNED_IP = "UPDATE_BANNED_IP";
	public static final String DELETE_BANNED_IP = "DELETE_BANNED_IP";
	public static final String INSERT_BANNED_IP = "INSERT_BANNED_IP";

	public static final String ADD_PARTNER = "ADD_PARTNER";
	public static final String DELETE_PARTNER = "DELETE_PARTNER";

	public static final String LOAD_SESSIONS = "LOAD_SESSIONS";
	public static final String LOAD_SESSIONS_COUNT = "LOAD_SESSIONS_COUNT";
	public static final String LOAD_SESSION = "LOAD_SESSION";
	public static final String LOAD_SESSION_FROM_USERNAME = "LOAD_SESSION_FROM_USERNAME";
	public static final String REGISTER_SESSION = "REGISTER_SESSION";
	public static final String SAVE_SESSION = "SAVE_SESSION";
	public static final String UNREGISTER_SESSION = "UNREGISTER_SESSION";
	public static final String UNREGISTER_SESSIONS = "UNREGISTER_SESSIONS";

	public static final String LOAD_MOBILE_DEVICES = "LOAD_MOBILE_DEVICES";
	public static final String LOAD_USER_MOBILE_DEVICES = "LOAD_USER_MOBILE_DEVICES";
	public static final String EXISTS_MOBILE_DEVICE = "EXISTS_MOBILE_DEVICE";
	public static final String REGISTER_MOBILE_DEVICE = "REGISTER_MOBILE_DEVICE";
	public static final String UNREGISTER_MOBILE_DEVICE = "UNREGISTER_MOBILE_DEVICE";

	public static final String PARAM_ID_FEDERATION = "id_federation";
	public static final String PARAM_ID_BUSINESS_UNIT = "id_business_unit";
	public static final String PARAM_HOST = "host";
	public static final String PARAM_URL = "url";
	public static final String PARAM_URI = "uri";
	public static final String PARAM_SECRET = "secret";
	public static final String PARAM_IP = "ip";
	public static final String PARAM_EMAIL = "email";
	public static final String PARAM_LANG = "lang";
	public static final String PARAM_USERNAME = "username";
	public static final String PARAM_FULLNAME = "fullname";
	public static final String PARAM_ATTEMPTS = "attempts";
	public static final String PARAM_LAST_ATTEMPT = "last_attempt";
	public static final String PARAM_NAME = "name";
	public static final String PARAM_FEDERATION = "federation";
	public static final String PARAM_LABEL = "label";
	public static final String PARAM_ONTOLOGY = "ontology";
	public static final String PARAM_ID = "id";
	public static final String PARAM_ID_USER = "id_user";
	public static final String PARAM_IS_HUMAN = "human";
	public static final String PARAM_MODE = "mode";
	public static final String PARAM_TYPE = "type";
	public static final String PARAM_CODE = "code";
	public static final String PARAM_TAG = "tag";
	public static final String PARAM_ENABLE = "enable";
	public static final String PARAM_VISIBLE = "visible";
	public static final String PARAM_TRUSTED = "trusted";
	public static final String PARAM_VALIDATION_CODE = "validation_code";
	public static final String PARAM_CREATE_DATE = "create_date";
	public static final String PARAM_CONDITION = "condition";
	public static final String PARAM_CONDITION_LIKE = "condition_like";
	public static final String PARAM_START = "start";
	public static final String PARAM_LIMIT = "limit";
	public static final String PARAM_TOKEN = "token";
	public static final String PARAM_VERIFIER = "verifier";
	public static final String PARAM_LAST_USE = "last_use";
	public static final String PARAM_IS_MOBILE = "is_mobile";
	public static final String PARAM_REMEMBER_ME = "remember_me";
	public static final String PARAM_SPACE = "space";
	public static final String PARAM_NODE = "node";

	FederationList loadFederations();

	FederationList loadTrustedFederations();

	Federation loadFederation(String name);

	Federation loadFederationById(String federationId);

	boolean existsFederation(String name);

	Federation createFederation(Federation federation);

	void saveFederation(Federation federation);

	void removeFederation(String id);

	void createFederationTrustRequest(Federation federation, String validationCode);

	boolean existsFederationTrustRequest(String federationId, String validationCode);

	void removeFederationTrustRequest(String id);

	FederationUnitList loadMemberBusinessUnits();

	FederationUnitList loadMemberBusinessUnitsWithServicesOrFeeders();

	FederationUnitList loadPartnerBusinessUnits();

    boolean existsPartnerBusinessUnit(String id);

	FederationUnit loadPartnerBusinessUnit(String id);

	FederationUnit locatePartnerBusinessUnit(String name);

	BusinessUnitList loadBusinessUnitsOfType(String type);

	BusinessUnitList loadBusinessUnits(String federationName);

	BusinessUnit loadBusinessUnit(String name);

	BusinessUnit loadBusinessUnitById(String id);

	BusinessUnit loadBusinessUnit(String federationName, String name);

	boolean existsBusinessUnit(String federationName, String name);

	BusinessUnit createBusinessUnit(BusinessUnit businessUnit);

	void saveBusinessUnit(BusinessUnit businessUnit);

	void removeBusinessUnit(String id);

	void createBusinessUnitPartnerRequest(BusinessUnit businessUnit, String validationCode);

	boolean existsBusinessUnitPartnerRequest(String businessUnitId, String validationCode);

	void removeBusinessUnitPartnerRequest(String id);

	ServiceList loadServices(String businessUnit);

	Service createService(Service service);

	void clearServices(String businessUnitId);

	FeederList loadFeeders(String businessUnit);

	Feeder createFeeder(Feeder feeder);

	void clearFeeders(String businessUnit);

	String generateIdForUser(User user);

	UserList searchUsers(String condition, int startPos, int limit);

	boolean existsUser(String idOrUsername);

	User loadUser(String businessUnit);

	User loadUserFromUsername(String username);

	User loadUserFromEmail(String email);

	User createUser(User user);

	void saveUser(User user);

	void addIpWrongAccess(String remoteAddr);

	boolean isIpBanned(String remoteAddr);

	void removeIpBan(String remoteAddr);

	HashMap<String, Session> loadSessions();

	int loadSessionsCount();

	Session loadSession(String token);

	Session[] loadSessionsFromUsername(String username);

	void registerSession(Session session);

	void saveSession(Session session);

	void unregisterSession(String token);

	void unregisterSessions(Date to);

	DeviceList loadMobileDevices();

	DeviceList loadMobileDevices(String userId);

	boolean existsMobileDevice(String userId, String deviceId);

	void registerMobileDevice(String userId, String deviceId);

	void unRegisterMobileDevice(String userId, String deviceId);

}
