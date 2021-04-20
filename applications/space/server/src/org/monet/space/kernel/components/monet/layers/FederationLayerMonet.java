/*
    Monet will assist business to process re-engineering. Monet separate the
    business logic from the underlying technology to allow Model-Driven
    Engineering (MDE). These models guide all the development process over a
    Service Oriented Architecture (SOA).

    Copyright (C) 2009  Grupo de Ingenieria del Sofware y Sistemas de la Universidad de Las Palmas de Gran Canaria

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see http://www.gnu.org/licenses/.
 */

package org.monet.space.kernel.components.monet.layers;

import net.minidev.json.parser.ParseException;
import org.monet.api.space.backservice.BackserviceApi;
import org.monet.api.space.backservice.impl.BackserviceApiImpl;
import org.monet.federation.accountservice.accountactions.impl.messagemodel.FederationAccount;
import org.monet.federation.accountservice.accountactions.impl.messagemodel.FederationAccountResponse;
import org.monet.federation.accountservice.client.FederationService;
import org.monet.metamodel.*;
import org.monet.metamodel.ProjectBase.TypeEnumeration;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.agents.AgentSession;
import org.monet.space.kernel.components.ComponentFederation;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.FederationLayer;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.components.layers.SourceLayer;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.components.monet.federation.ComponentFederationMonet;
import org.monet.space.kernel.components.monet.federation.FederationApi;
import org.monet.space.kernel.constants.*;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.exceptions.SessionException;
import org.monet.space.kernel.exceptions.SystemException;
import org.monet.space.kernel.library.LibraryString;
import org.monet.space.kernel.model.*;
import org.monet.space.kernel.model.Dictionary;
import org.monet.space.kernel.producers.ProducerFederation;
import org.monet.space.kernel.producers.ProducerFederationList;
import org.monet.space.kernel.producers.ProducerRole;
import org.monet.space.kernel.producers.ProducersFactory;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import java.util.*;
import java.util.Map.Entry;

public class FederationLayerMonet extends LayerMonet implements FederationLayer {
	private final ProducersFactory producersFactory = ProducersFactory.getInstance();
	private final Context context = Context.getInstance();
	private final Configuration configuration;
	private FederationService accountService;
	private OAuthService service;
	private Dictionary dictionary;

	public static final String TIMESTAMP_PARAMETER = "timestamp";
	public static final String SIGNATURE_PARAMETER = "signature";
	public static final String ADDRESS_PARAMETER = "address";
	public static final String SERVICE_NAME_PARAMETER = "service-name";
	public static final String HASH_PARAMETER = "hash";
	public static final String OPERATION_PARAMETER = "op";

	private static final HashSet<String> keys = new HashSet<String>(Arrays.asList(SIGNATURE_PARAMETER, TIMESTAMP_PARAMETER, ADDRESS_PARAMETER, SERVICE_NAME_PARAMETER, HASH_PARAMETER, OPERATION_PARAMETER));

	public FederationLayerMonet(Configuration configuration, ComponentFederation componentFederationManager) {
		BusinessUnit businessUnit = BusinessUnit.getInstance();
		Federation federation = businessUnit.getFederation();

		this.configuration = configuration;
		this.accountService = componentFederationManager.getFederationService();
		this.dictionary = Dictionary.getInstance();
		ServiceBuilder builder = new ServiceBuilder().provider(this.getFederationApi()).apiKey(businessUnit.getName()).apiSecret(federation.getSecret());

		if (this.configuration.getCallbackUrl() != null)
			builder.callback(this.configuration.getCallbackUrl());

		this.service = builder.build();
	}

	protected boolean isStarted() {
		return ComponentFederationMonet.started();
	}

	protected Account getAccount() {
		Session session = this.context.getCurrentSession();
		if (session == null)
			return null;
		return session.getAccount();
	}

	@Override
	public String getAuthorizationUrl() {
		Token requestToken = service.getRequestToken();
		this.setRequestToken(requestToken);
		this.setAccessToken(null);
		return service.getAuthorizationUrl(requestToken);
	}

	@Override
	public String getUserLanguage() {
		Account account = this.loadAccount();
		String codeLanguage;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}
		if (account == null)
			return null;

		codeLanguage = account.getUser().getLanguage();
		if (codeLanguage.equals(Strings.EMPTY))
			return Common.DEFAULT_LANGUAGE;

		return codeLanguage;
	}

	@Override
	public boolean isLogged() {
		Boolean result = false;
		Token accessToken;

		if (!this.isStarted()) throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		try {
			String token = null;
			accessToken = this.getAccessToken();

            if (accessToken != null)
				token = accessToken.getToken();

            if (this.accountService == null)
                return false;

			result = this.accountService.isLogged(token, this.configuration.getRequest());
		} catch (Exception e) {
			AgentLogger.getInstance().error(e);
			e.printStackTrace();
			return false;
		}

		return result;
	}

	@Override
	public boolean existsAccount(String id) {
		ProducerFederation producerFederation;

		if (!this.isStarted()) throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		producerFederation = this.producersFactory.get(Producers.FEDERATION);

		return producerFederation.exists(id);
	}

	@Override
	public Banner loadBanner() {
		BusinessUnit businessUnit = BusinessUnit.getInstance();
		Distribution distribution = businessUnit.getDistribution();
		Project project = businessUnit.getBusinessModel().getProject();
		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
		Session currentSession = Context.getInstance().getCurrentSession();
		String accountId = currentSession.getAccount() != null ? currentSession.getAccount().getId() : null;
		Account account = accountId != null && !accountId.equals("-1") ? loadAccount(accountId) : null;

		Banner banner = new Banner();
		banner.setHasPermissions(account != null);
		banner.setTitle(BusinessUnit.getTitle(distribution, project));
		banner.setSubTitle(businessUnit.getLabel());
		banner.setSpaceUrl(org.monet.space.kernel.configuration.Configuration.getInstance().getUrl());
		banner.setCountActiveTasks(account != null ? getCountTasks(taskLayer, account, createTaskRequest(Task.Situation.ACTIVE, Task.Inbox.TASKBOARD)) : -1);
		banner.setCountAliveTasks(account != null ? getCountTasks(taskLayer, account, createTaskRequest(Task.Situation.ALIVE, Task.Inbox.TASKTRAY)) : -1);

		return banner;
	}

	private TaskSearchRequest createTaskRequest(String situation, String inbox) {
		TaskSearchRequest searchRequest = new TaskSearchRequest();
		searchRequest.addParameter(Task.Parameter.SITUATION, situation);
		searchRequest.addParameter(Task.Parameter.INBOX, inbox);
		return searchRequest;
	}

	private int getCountTasks(TaskLayer taskLayer, Account account, TaskSearchRequest searchRequest) {
		return taskLayer.searchTasksCount(account, searchRequest);
	}

	@Override
	public Account loadAccount() {
		Session session = Context.getInstance().getCurrentSession();

		if (!this.isStarted()) throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		if (session == null)
			return null;

		return session.getAccount();
	}

	@Override
	public Account loadAccount(String id) {
		ProducerFederation producerFederation;
		Account account;

		if (!this.isStarted()) throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		producerFederation = this.producersFactory.get(Producers.FEDERATION);
		account = producerFederation.load(id);

		return account;
	}

	@Override
	public User loadUser(String id) {
		ProducerFederation producerFederation;

		if (!this.isStarted()) throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		producerFederation = this.producersFactory.get(Producers.FEDERATION);

		return producerFederation.loadUser(id);
	}

	@Override
	public boolean existsUserByUsername(String username) {
		ProducerFederation producerFederation;

		if (!this.isStarted()) throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		producerFederation = this.producersFactory.get(Producers.FEDERATION);

		return producerFederation.existsUserByUsername(username);
	}

	@Override
	public User loadUserByUsername(String username) {
		ProducerFederation producerFederation;

		if (!this.isStarted()) throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		producerFederation = this.producersFactory.get(Producers.FEDERATION);

		return producerFederation.loadUserByUsername(username);
	}

	@Override
	public User loadUserLogged() {
		Session session;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}
		if (!this.isLogged()) {
			return null;
		}

		session = (Session) this.context.getCurrentSession();
		return session.getAccount().getUser();
	}

	@Override
	public User loadUserLinkedToNode(Node node) {
		ProducerFederation producerFederation;
		User user;

		if (!this.isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		producerFederation = this.producersFactory.get(Producers.FEDERATION);
		user = producerFederation.getUserLinkedToNode(node);

		return user;
	}

    @Override
    public String validateRequest(String signature, String hash) {
        String result = null;

        if (!this.isStarted())
            throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

        FederationAccountResponse accountResponse = null;
        try {
            accountResponse = this.accountService.validateRequest(signature, hash);
            return accountResponse.getAccount().getId();
        } catch (Exception e) {
            AgentLogger.getInstance().debug(e.getMessage());
        }

        return result;
    }

	@Override
	public ValidationResult validateRequest(String signature, Long timestamp, ArrayList<Entry<String, Object>> parameters) {
		ProducerFederation producerFederation;
		FederationAccount account = null;
		ValidationResult result = new ValidationResult();

		if (!this.isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		try {
			StringBuilder builder = new StringBuilder();
			HashSet<String> addedKeys = new HashSet<String>();
			for (Entry<String, Object> entry : parameters) {
				String key = entry.getKey();
				if (!(entry.getValue() instanceof String) || keys.contains(key) || addedKeys.contains(key))
					continue;
				addedKeys.add(key);
				builder.append(key);
				builder.append("=");
				builder.append(entry.getValue());
				builder.append("&");
			}
			builder.append(String.format("%s=%d", TIMESTAMP_PARAMETER, timestamp));
			String requestArgs = LibraryString.cleanSpecialChars(builder.toString());

			AgentLogger.getInstance().debug("FederationLayer:validateRequest. Signature: %s. RequestArgs: %s.", signature, requestArgs);

			// Check that there aren't any space in the base64 field
			signature = signature.replaceAll(" ", "+");

			FederationAccountResponse accountResponse = this.accountService.validateRequest(signature, requestArgs);
			account = accountResponse.getAccount();
			if (account == null) {
				AgentLogger.getInstance().debug(accountResponse.getError());
				result.setValid(false);
				result.setReason(accountResponse.getError());
				return result;
			}

		} catch (Exception e) {
			AgentLogger.getInstance().debug(e.getMessage());
			result.setValid(false);
			result.setReason(e.getMessage());
			return result;
		}

		this.createOrUpdateAccount(account.getId(), account.getUsername(), getUserInfo(account));

		producerFederation = this.producersFactory.get(Producers.FEDERATION);
		producerFederation.injectAsCurrentAccount(account.getUsername(), account.getLang(), this.getUserInfo(account));

		return result;
	}

	@Override
	public boolean existsAccessToken(Token token) {
		return existsAccountFromToken(token);
	}

	@Override
	public void injectAccessToken(Token token) {
		this.setAccessToken(token);
		loadAccountFromToken(token);
	}

	@Override
	public void injectRequestToken(Token token) {
		this.setRequestToken(token);
	}

	@Override
	public void loginAsSystem() {
		ProducerFederation producerFederation;

		producerFederation = this.producersFactory.get(Producers.FEDERATION);
		producerFederation.injectAsCurrentAccount("system", Language.getCurrent(), new UserInfo());
	}

	@Override
	public void login(String verifierValue) {
		Token requestToken, accessToken;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		requestToken = this.getRequestToken();
		accessToken = this.getAccessToken();

 		if (requestToken == null)
			return;
		if (accessToken != null)
			return;

		Verifier verifier = new Verifier(verifierValue);
		try {
			accessToken = service.getAccessToken(requestToken, verifier);
		} catch (IllegalArgumentException e) {
			this.setRequestToken(null);
			this.setAccessToken(null);
			return;
		}

		if (accessToken == null)
			return;
		this.setAccessToken(accessToken);

		loadAccountFromToken(accessToken);
	}

	@Override
	public void logout() {
		Token accessToken;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		try {
			accessToken = this.getAccessToken();
			this.accountService.logout(accessToken.getToken(), this.configuration.getRequest());
			this.setRequestToken(null);
			this.setAccessToken(null);
			Session session = (Session) this.context.getCurrentSession();
			session.setAccount(null);
		} catch (Exception e) {
			AgentLogger.getInstance().error(e);
		}
	}

	@Override
	public void saveAccount(String data) {
		Account account;
		ProducerFederation producerFederation;
		Session session = (Session) this.context.getCurrentSession();

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		if (session == null) {
			throw new SessionException(ErrorCode.SAVE_ACCOUNT, this.context.getIdSession(Thread.currentThread().getId()));
		}

		account = session.getAccount();
		try {
			account.fromJson(data);
		} catch (ParseException e) {
			throw new SystemException(ErrorCode.SAVE_ACCOUNT, null);
		}

		producerFederation = this.producersFactory.get(Producers.FEDERATION);
		producerFederation.save(account);

	}

	@Override
	public void saveAccount(Account account) {
		ProducerFederation producerFederation;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerFederation = this.producersFactory.get(Producers.FEDERATION);
		producerFederation.save(account);

	}

	@Override
	public Account createAccount(String id, String username, UserInfo info) {
		ProducerFederation producerFederation;
		Account account = null;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		try {
			if (id == null)
				id = this.accountService.generateAccountId(username, info.getFullname(), info.getEmail());

			producerFederation = this.producersFactory.get(Producers.FEDERATION);
			account = producerFederation.create(id, username, info);

			this.updateAccountRoles(id, username, info, null);
		} catch (Exception e) {
			AgentLogger.getInstance().error(e);
		}

		return account;
	}

	@Override
	public void removeAccount(String id) {

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		NodeLayer nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
		ProducerFederation producerFederation = this.producersFactory.get(Producers.FEDERATION);
		Account account = this.loadAccount(id);

		HashMap<String, ArrayList<Node>> environmentNodes = account.getEnvironmentNodesByRole();
		for (String role : environmentNodes.keySet()) {
			for (Node node : environmentNodes.get(role)) {
				nodeLayer.deleteNode(node);
				nodeLayer.removeNodeFromTrash(node.getId());
				producerFederation.unlinkFromNode(account.getId(), role);
			}
		}

		HashMap<String, ArrayList<Dashboard>> dashboards = account.getDashboardsByRole();
		for (String role : dashboards.keySet())
			producerFederation.unlinkFromDashboard(account.getId(), role);

		producerFederation.remove(account.getId());
	}

	@Override
	public void removeAccounts(String accounts) {
		String[] accountsArray = accounts.split(Strings.COMMA);

		if (!this.isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		for (int pos = 0; pos < accountsArray.length; pos++)
			this.removeAccount(accountsArray[pos]);

	}

	@Override
	public UserList searchUsers(DataRequest dataRequest) {

		if (!this.isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		ProducerFederationList producerFederationList = this.producersFactory.get(Producers.FEDERATIONLIST);
		return producerFederationList.searchUsers(dataRequest);
	}

	@Override
	public UserList searchUsersWithRoles(DataRequest dataRequest) {

		if (!this.isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		ProducerFederationList producerFederationList = this.producersFactory.get(Producers.FEDERATIONLIST);
		return producerFederationList.searchUsersWithRoles(dataRequest);
	}

	@Override
	public UserList searchFederationUsers(DataRequest dataRequest) {

		if (!this.isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		UserList userList = new UserList();

		try {
		  org.monet.federation.accountservice.accountactions.impl.messagemodel.UserList federationUserList;
		  federationUserList = this.accountService.searchUsers(dataRequest.getCondition(), dataRequest.getStartPos(), dataRequest.getLimit());

		  for (org.monet.federation.accountservice.accountactions.impl.messagemodel.User federationUser : federationUserList.getAll()) {
			User user = new User();
			user.setId(federationUser.getId());
			user.setName(federationUser.getUsername());
			user.getInfo().setFullname(federationUser.getFullname());
			user.getInfo().setEmail(federationUser.getEmail());
			userList.add(user);
		  }

		  userList.setTotalCount(federationUserList.getTotalCount());
		} catch (Exception e) {
		  AgentLogger.getInstance().error(e);
		}

		return userList;
	}

	@Override
	public FederationUnitList loadMembers() {

		if (!this.isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		FederationUnitList memberList = new FederationUnitList();

		try {
			org.monet.federation.accountservice.accountactions.impl.messagemodel.FederationUnitList federationPartnerList = this.accountService.loadMembers();

			for (org.monet.federation.accountservice.accountactions.impl.messagemodel.FederationUnit federationPartner : federationPartnerList.getAll()) {
				FederationUnit member = new FederationUnit();
				member.loadFromFederation(federationPartner);
				memberList.add(member);
				this.registerFeeders(member);
			}

		} catch (Exception e) {
			AgentLogger.getInstance().error(e);
		}

		return memberList;
	}

	@Override
	public FederationUnitList loadMembers(Account account) {
		Session currentSession = Context.getInstance().getCurrentSession();
		FederationUnitList memberList = currentSession.getVariable("memberList");

		if (!this.isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		try {
			if (memberList != null)
				return memberList;

			memberList = new FederationUnitList();
			org.monet.federation.accountservice.accountactions.impl.messagemodel.FederationUnitList federationPartnerList = this.accountService.loadMembers();

			for (org.monet.federation.accountservice.accountactions.impl.messagemodel.FederationUnit federationPartner : federationPartnerList.getAll()) {

				if (!this.hasPermissionToFederationUnit(account, federationPartner))
					continue;

				FederationUnit member = new FederationUnit();
				member.loadFromFederation(federationPartner);
				memberList.add(member);
				this.registerFeeders(member);
			}

			currentSession.setVariable("memberList", memberList);
		} catch (Exception e) {
			AgentLogger.getInstance().error(e);
		}

		return memberList;
	}

	private boolean hasPermissionToFederationUnit(Account account, org.monet.federation.accountservice.accountactions.impl.messagemodel.FederationUnit federationUnit) {
		org.monet.space.kernel.configuration.Configuration monetConfiguration = org.monet.space.kernel.configuration.Configuration.getInstance();

		try {

			if (federationUnit.getName().equals(BusinessUnit.getInstance().getName()))
				return true;

			String url = federationUnit.getUri().toString() + monetConfiguration.getBackserviceServletPath();
			BackserviceApi api = new BackserviceApiImpl(url, monetConfiguration.getCertificateFilename(), monetConfiguration.getCertificatePassword());

			return api.hasPermissions(account.getUser().getName());
		} catch (Exception e) {
			AgentLogger.getInstance().info("Could not connect to space %s for checking if %s has permissions to access", federationUnit.getName(), account.getUser().getInfo().getFullname());
			return false;
		}
	}

	@Override
	public FederationUnitList loadPartners() {

		if (!this.isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		FederationUnitList partnerList = new FederationUnitList();

		try {
			org.monet.federation.accountservice.accountactions.impl.messagemodel.FederationUnitList federationPartnerList = this.accountService.loadPartners();

			for (org.monet.federation.accountservice.accountactions.impl.messagemodel.FederationUnit federationPartner : federationPartnerList.getAll()) {
				FederationUnit partner = new FederationUnit();
				partner.loadFromFederation(federationPartner);
				partnerList.add(partner);
				this.registerFeeders(partner);
			}

		} catch (Exception e) {
			AgentLogger.getInstance().error(e);
		}

		return partnerList;
	}

	@Override
	public FederationUnit loadPartner(String partnerId) {

		if (!this.isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		FederationUnit partner = new FederationUnit();

		try {
			org.monet.federation.accountservice.accountactions.impl.messagemodel.FederationUnit federationPartner = this.accountService.loadPartner(partnerId);
			partner.loadFromFederation(federationPartner);
			this.registerFeeders(partner);
		} catch (Exception e) {
			AgentLogger.getInstance().error(e);
		}

		return partner;
	}

	@Override
	public FederationUnit locatePartner(String partnerName) {
		FederationUnit partner = null;

		if (!this.isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

		try {
			org.monet.federation.accountservice.accountactions.impl.messagemodel.FederationUnit federationPartner = this.accountService.locatePartner(partnerName);

			if (federationPartner == null)
				return partner;

			partner = new FederationUnit();
			partner.loadFromFederation(federationPartner);
			this.registerFeeders(partner);
		} catch (Exception e) {
			AgentLogger.getInstance().error(e);
		}

		return partner;
	}

	@Override
	public Account locateAccount(String data) {
		ProducerFederation producerFederation;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerFederation = this.producersFactory.get(Producers.FEDERATION);

		return producerFederation.locateAccount(data);
	}

	@Override
	public void createOrUpdateAccount(Account account) {
		this.createOrUpdateAccount(account.getId(), account.getUser().getName(), account.getUser().getInfo());
	}

	private void createOrUpdateAccount(String id, String username, UserInfo userInfo) {
		try {

			if (!this.existsAccount(id))
				this.createAccount(id, username, userInfo);
			else {
				Account systemAccount = this.loadAccount(id);
				UserInfo systemInfo = systemAccount.getUser().getInfo();
				systemInfo.setFullname(userInfo.getFullname());
				systemInfo.setEmail(userInfo.getEmail());
				this.saveAccount(systemAccount);
				if (this.getAccessToken() != null)
					this.updateAccountRoles(id, username, userInfo, this.dictionary.getRolesNames());
			}

		} catch (Exception e) {
			AgentLogger.getInstance().error(e);
		}
	}

	private FederationApi getFederationApi() {
		BusinessUnit businessUnit = BusinessUnit.getInstance();
		Federation federation = businessUnit.getFederation();

		FederationApi federationApi = new FederationApi();
        federationApi.setUrl(federation.getUri());

		return federationApi;
	}

	private UserInfo getUserInfo(FederationAccount account) {
		UserInfo userInfo = new UserInfo();
		userInfo.setFullname(account.getFullname());
		userInfo.setEmail(account.getEmail());
		return userInfo;
	}

	private Token getRequestToken() {
		AgentSession agentSession = AgentSession.getInstance();
		Session session = agentSession.get(this.configuration.getSessionId());
		return session != null ? (Token) session.getVariable("requestToken") : null;
	}

	private void setRequestToken(Token requestToken) {
		AgentSession agentSession = AgentSession.getInstance();
		Session session = agentSession.get(this.configuration.getSessionId());
		if (session == null) return;
		session.setVariable("requestToken", requestToken);
	}

	private Token getAccessToken() {
		AgentSession agentSession = AgentSession.getInstance();
		Session session = agentSession.get(this.configuration.getSessionId());
		return session != null ? (Token) session.getVariable("accessToken") : null;
	}

	private void setAccessToken(Token accessToken) {
		AgentSession agentSession = AgentSession.getInstance();
		Session session = agentSession.get(this.configuration.getSessionId());
		if (session == null) return;
		session.setVariable("accessToken", accessToken);
	}

	private void updateAccountRoles(String id, String userName, UserInfo info, List<String> roles) {
		List<String> obsoleteRoles = new ArrayList<>();
		Project manifest = BusinessUnit.getInstance().getBusinessModel().getProject();
		boolean isFront = manifest.getType() != null ? manifest.getType().equals(TypeEnumeration.FRONT) : false;
		Map<String, List<NodeDefinition>> environmentDefinitions = this.dictionary.getEnvironmentDefinitionListByRole();
		Map<String, List<DashboardDefinition>> dashboardDefinitions = this.dictionary.getDashboardDefinitionListByRole();
		ProducerFederation producerFederation = this.producersFactory.get(Producers.FEDERATION);
		ProducerRole producerRole = this.producersFactory.get(Producers.ROLE);
		Account account = producerFederation.load(id);
		HashMap<String, ArrayList<Node>> environmentNodes = account.getEnvironmentNodesByRole();
		List<Dashboard> dashboards = account.getDashboards();
		Map<String, ArrayList<Node>> environmentNodesByRole = account.getEnvironmentNodesByRole();
		Map<String, ArrayList<Dashboard>> dashboardsByRole = account.getDashboardsByRole();

		if (roles == null)
			return;

		for (String role : environmentNodesByRole.keySet()) {
			if (roles.contains(role))
				continue;
			else
				obsoleteRoles.add(role);
		}

		for (String role : roles) {
			if (!this.dictionary.existsRoleDefinition(role))
				continue;

			if (!producerRole.existsNonExpiredUserRole(role, account.getUser().getId()))
				continue;

			RoleDefinition roleDefinition = this.dictionary.getRoleDefinition(role);
			if (roleDefinition.isDisabled())
				continue;

			addEnvironmentNodesToAccount(info, isFront, environmentDefinitions, account, environmentNodes, role, roleDefinition);
			addDashboardsToAccount(info, isFront, dashboardDefinitions, account, dashboards, role, roleDefinition);
		}

		for (String obsoleteRole : obsoleteRoles) {
			environmentNodesByRole.remove(obsoleteRole);
			dashboardsByRole.remove(obsoleteRole);
		}

	}

	private boolean containsEnvironmentNode(NodeDefinition environmentDefinition, HashMap<String, ArrayList<Node>> environmentNodes, String role) {

		if (environmentNodes == null)
			return false;

		for (Map.Entry<String, ArrayList<Node>> entry : environmentNodes.entrySet())
			if (entry.getKey().equals(role)) {
				for (Node node : entry.getValue()) {
					if (node.getDefinition().getCode().equals(environmentDefinition.getCode())) {
						return true;
					}
				}
			}

		return false;
	}

	private void addEnvironmentNodesToAccount(UserInfo info, boolean isFront, Map<String, List<NodeDefinition>> environmentDefinitions, Account account, HashMap<String, ArrayList<Node>> environmentNodes, String role, RoleDefinition roleDefinition) {
		List<NodeDefinition> environmentDefinitionList = environmentDefinitions.get(roleDefinition.getCode());
		for (NodeDefinition environmentDefinition : environmentDefinitionList) {
			if (this.containsEnvironmentNode(environmentDefinition, environmentNodes, role))
				continue;
			if (!environmentNodes.containsKey(role)) environmentNodes.put(role, new ArrayList<Node>());
			environmentNodes.get(role).add(this.addEnvironmentNodeToAccount(environmentDefinition, account, info, isFront, role));
		}
	}

	private Node addEnvironmentNodeToAccount(NodeDefinition environmentDefinition, Account account, UserInfo info, boolean isFront, String role) {
		NodeLayer nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
		ProducerFederation producerFederation = this.producersFactory.get(Producers.FEDERATION);
		String definitionCode = environmentDefinition.getCode();
		PermissionList permissionList;
		Node node;

		if (environmentDefinition.isSingleton()) node = nodeLayer.locateNode(definitionCode);
		else {
			node = nodeLayer.addNode(definitionCode, account.getUser());

			if (isFront) {
				if (node.getLabel().isEmpty()) node.setLabel(info.getEmail());
				nodeLayer.saveNode(node);
			}
		}

		permissionList = node.getPermissionList();

		Permission managerPermission = new Permission();
		managerPermission.setIdObject(node.getId());
		managerPermission.setIdUser(account.getUser().getId());
		managerPermission.setType(PermissionType.READ_WRITE_CREATE_DELETE);
		managerPermission.setBeginDate(account.getUser().getInternalRegistrationDate());
		managerPermission.setExpireDate(null);
		permissionList.add(managerPermission);

		nodeLayer.saveNodePermissions(node, permissionList);
		producerFederation.linkToNode(account, node, role);

		return node;
	}

	private boolean containsDashboard(DashboardDefinition dashboardDefinition, List<Dashboard> dashboards) {

		if (dashboards == null)
			return false;

		for (Dashboard dashboard : dashboards)
			if (dashboard.getDefinition().getCode().equals(dashboardDefinition.getCode()))
				return true;

		return false;
	}

	private void addDashboardsToAccount(UserInfo info, boolean isFront, Map<String, List<DashboardDefinition>> dashboardDefinitions, Account account, List<Dashboard> dashboards, String role, RoleDefinition roleDefinition) {
		List<DashboardDefinition> dashboardDefinitionList = dashboardDefinitions.get(roleDefinition.getCode());
		for (DashboardDefinition dashboardDefinition : dashboardDefinitionList) {
			if (this.containsDashboard(dashboardDefinition, dashboards))
				continue;
			dashboards.add(this.addDashboardToAccount(dashboardDefinition, account, info, isFront, role));
		}
	}

	private Dashboard addDashboardToAccount(DashboardDefinition dashboardDefinition, Account account, UserInfo info, boolean isFront, String role) {
		ProducerFederation producerFederation = this.producersFactory.get(Producers.FEDERATION);
		Dashboard dashboard = new Dashboard(dashboardDefinition);

		for (Dashboard accountDashboard : account.getDashboards()) {
			if (accountDashboard.getCode().equals(dashboard.getCode()))
				return accountDashboard;
		}

		producerFederation.linkToDashboard(account, dashboard, role);

		return dashboard;
	}

	private void registerFeeders(FederationUnit partner) {
		SourceLayer sourceLayer = ComponentPersistence.getInstance().getSourceLayer();
		List<GlossaryDefinition> glossaryDefinitionList = this.dictionary.getGlossaryDefinitionList();

		for (GlossaryDefinition glossaryDefinition : glossaryDefinitionList) {
			for (FederationUnitFeeder feeder : partner.getFeederList()) {
				if (glossaryDefinition.getOntology().equals(feeder.getOntology()) && !sourceLayer.existsSource(glossaryDefinition.getCode(), feeder.getUri(partner))) {
					Source<SourceDefinition> source = sourceLayer.createSource(glossaryDefinition.getCode());

					source.setPartnerName(partner.getName());
					source.setPartnerLabel(partner.getLabel());
					source.setUri(feeder.getUri(partner));
					source.setEnabled(true);

					sourceLayer.saveSource(source);
					sourceLayer.populateSource(source);
				}
			}
		}

	}

	private boolean existsAccountFromToken(Token accessToken) {
		try {
			return this.accountService.existsAccount(accessToken.getToken(), this.configuration.getRequest());
		} catch (Exception ex) {
			AgentLogger.getInstance().debug("User not found for token %s", accessToken.getToken());
			return false;
		}
	}

	private void loadAccountFromToken(Token accessToken) {
		AgentLogger.getInstance().debug("Load account from token %s", accessToken.getToken());

		ProducerFederation producerFederation;
		FederationAccount account = null;
		try {
			account = this.accountService.getAccount(accessToken.getToken(), this.configuration.getRequest());
		} catch (Exception e) {
			AgentLogger.getInstance().error(e);
		}

		if (account == null)
			return;

		AgentLogger.getInstance().debug("Account found %s", account.getId());
		this.createOrUpdateAccount(account.getId(), account.getUsername(), getUserInfo(account));

		producerFederation = this.producersFactory.get(Producers.FEDERATION);
		producerFederation.injectAsCurrentAccount(account.getUsername(), account.getLang(), this.getUserInfo(account));
	}

}