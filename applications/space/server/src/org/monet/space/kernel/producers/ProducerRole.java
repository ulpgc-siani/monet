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

package org.monet.space.kernel.producers;

import org.monet.federation.accountservice.client.FederationService;
import org.monet.space.kernel.constants.Database;
import org.monet.space.kernel.constants.ErrorCode;
import org.monet.space.kernel.constants.Producers;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.*;
import org.monet.space.kernel.model.Role.Type;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.EventObject;
import java.util.HashMap;

public class ProducerRole extends ProducerList {

	public ProducerRole() {
		super();
	}

	public Role fill(ResultSet result) throws SQLException {
		String type = result.getString("type");
		Role role = null;

		if (type.equals(Type.User.toString())) {
			UserRole userRole = new UserRole();
			userRole.setUserId(result.getString("id_user"));
			userRole.setType(Type.User);
			role = userRole;
		} else if (type.equals(Type.Service.toString())) {
			ServiceRole serviceRole = new ServiceRole();
			serviceRole.setPartnerId(result.getString("partner_id"));
			serviceRole.setPartnerServiceName(result.getString("partner_service_name"));
			serviceRole.setType(Type.Service);
			role = serviceRole;
		} else if (type.equals(Type.Feeder.toString())) {
			FeederRole feederRole = new FeederRole();
			feederRole.setPartnerId(result.getString("partner_id"));
			feederRole.setPartnerFeederName(result.getString("partner_service_name"));
			feederRole.setType(Type.Feeder);
			role = feederRole;
		}

		role.setId(result.getString("id"));
		role.setCode(result.getString("code"));
		role.setBeginDate(result.getTimestamp("begin_date"));

		Date expireDate = result.getTimestamp("expire_date");
		if (expireDate != null) role.setExpireDate(expireDate);

		role.linkLoadListener(this);

		return role;
	}

	private void loadUser(Role role) {

		if (!role.isUserRole())
			return;

		UserRole userRole = (UserRole) role;
		ProducerFederation producerFederation = (ProducerFederation) this.producersFactory.get(Producers.FEDERATION);
		User user = producerFederation.loadUser(userRole.getUserId());
		userRole.setUser(user);
	}

	private void loadPartner(Role role) {
		FederationService service = this.getAccountService();
		FederationUnit partner = new FederationUnit();

		if (role.isServiceRole()) {
			ServiceRole serviceRole = (ServiceRole) role;
			FederationUnitService partnerService = null;

			try {
				partner.loadFromFederation(service.loadPartner(serviceRole.getPartnerId()));
				partnerService = partner.getServiceList().get(serviceRole.getPartnerServiceName());

				serviceRole.setPartner(partner);
				serviceRole.setPartnerService(partnerService);
			} catch (Exception exception) {
				this.agentLogger.error(String.format("partner %s not found for service %s in space %s", serviceRole.getPartnerId(), serviceRole.getId(), BusinessUnit.getInstance().getName()), exception);
			}

		} else if (role.isFeederRole()) {
			FeederRole feederRole = (FeederRole) role;
			FederationUnitFeeder partnerFeeder = null;

			try {
				partner.loadFromFederation(service.loadPartner(feederRole.getPartnerId()));
				partnerFeeder = partner.getFeederList().get(feederRole.getPartnerFeederName());

				feederRole.setPartner(partner);
				feederRole.setPartnerFeeder(partnerFeeder);
			} catch (Exception exception) {
				this.agentLogger.error(String.format("partner %s not found for feeder %s in space %s", feederRole.getPartnerId(), feederRole.getId(), BusinessUnit.getInstance().getName()), exception);
			}
		}
	}

	public Role load(String id) {
		Role role = null;
		ResultSet result = null;
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		try {

			parameters.put(Database.QueryFields.ID, id);
			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.ROLE_LOAD, parameters);

			if (!result.next())
				throw new DataException(ErrorCode.LOAD_ROLE, "role");

			role = this.fill(result);

		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_ROLE, id, exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return role;
	}

	public boolean existsNonExpiredUserRole(String code, String userId) {
		ResultSet result = null;
		boolean exists = false;
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		try {
			code = Dictionary.getInstance().getDefinitionCode(code);

			parameters.put(Database.QueryFields.CODE, code);
			parameters.put(Database.QueryFields.ID_USER, userId);
			parameters.put(Database.QueryFields.DATE, this.agentDatabase.getDateAsTimestamp(new Date()));

			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.ROLE_USER_EXISTS, parameters);
			exists = result.next();

		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_USERS_OF_ROLE, code, exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return exists;
	}

	public boolean existsNonExpiredServiceRole(String code, FederationUnit partner, FederationUnitService partnerService) {
		ResultSet result = null;
		boolean exists = false;
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		try {
			code = Dictionary.getInstance().getDefinitionCode(code);

			parameters.put(Database.QueryFields.CODE, code);
			parameters.put(Database.QueryFields.PARTNER_ID, partner.getId());
			parameters.put(Database.QueryFields.PARTNER_SERVICE_NAME, partnerService.getName());
			parameters.put(Database.QueryFields.DATE, this.agentDatabase.getDateAsTimestamp(new Date()));

			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.ROLE_SERVICE_EXISTS, parameters);
			exists = result.next();

		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_USERS_OF_ROLE, code, exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return exists;
	}

	public boolean existsNonExpiredFeederRole(String code, FederationUnit partner, FederationUnitFeeder partnerFeeder) {
		ResultSet result = null;
		boolean exists = false;
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		try {
			code = Dictionary.getInstance().getDefinitionCode(code);

			parameters.put(Database.QueryFields.CODE, code);
			parameters.put(Database.QueryFields.PARTNER_ID, partner.getId());
			parameters.put(Database.QueryFields.PARTNER_SERVICE_NAME, partnerFeeder.getName());
			parameters.put(Database.QueryFields.DATE, this.agentDatabase.getDateAsTimestamp(new Date()));

			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.ROLE_FEEDER_EXISTS, parameters);
			exists = result.next();

		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_USERS_OF_ROLE, code, exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return exists;
	}

	public Role create(String code, Type type, String userId, Date beginDate, Date expireDate, String partnerId, String serviceName, String serviceUrl, String cache) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(Database.QueryFields.CODE, code);
		parameters.put(Database.QueryFields.TYPE, type.toString());
		parameters.put(Database.QueryFields.ID_USER, userId);
		parameters.put(Database.QueryFields.BEGIN_DATE, this.agentDatabase.getDateAsTimestamp(beginDate));
		parameters.put(Database.QueryFields.EXPIRE_DATE, expireDate != null ? this.agentDatabase.getDateAsTimestamp(expireDate) : null);
		parameters.put(Database.QueryFields.PARTNER_ID, partnerId);
		parameters.put(Database.QueryFields.PARTNER_SERVICE_NAME, serviceName);
		parameters.put(Database.QueryFields.CACHE, cache);

		String id = this.agentDatabase.executeRepositoryUpdateQueryAndGetGeneratedKey(Database.Queries.ROLE_CREATE, parameters);
		Role role = this.load(id);

		return role;
	}

	public Role createForUser(String code, User user, Date beginDate, Date expireDate) {
		String cache = user.getInfo().getFullname();
		return this.create(code, Type.User, user.getId(), beginDate, expireDate, null, null, null, cache);
	}

	public Role createForService(String code, FederationUnit partner, FederationUnitService partnerService, Date beginDate, Date expireDate) {
		String cache = partner.getLabel() + " " + partnerService.getLabel();
		String partnerServiceUrl = partner.getServiceUrl(partnerService.getName());
		return this.create(code, Type.Service, null, beginDate, expireDate, partner.getId(), partnerService.getName(), partnerServiceUrl, cache);
	}

	public Role createForFeeder(String code, FederationUnit partner, FederationUnitFeeder partnerFeeder, Date beginDate, Date expireDate) {
		String cache = partner.getLabel() + " " + partnerFeeder.getLabel();
		String partnerFeederUrl = partner.getFeederUrl(partnerFeeder.getName());
		return this.create(code, Type.Feeder, null, beginDate, expireDate, partner.getId(), partnerFeeder.getName(), partnerFeederUrl, cache);
	}

	public boolean save(Role role) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		Date expireDate = role.getInternalExpireDate();
		String partnerId = null;
		String partnerServiceName = null;

		if (role.isServiceRole()) {
			ServiceRole serviceRole = (ServiceRole) role;
			partnerId = serviceRole.getPartnerId();
			partnerServiceName = serviceRole.getPartnerServiceName();
		} else if (role.isFeederRole()) {
			FeederRole feederRole = (FeederRole) role;
			partnerId = feederRole.getPartnerId();
			partnerServiceName = feederRole.getPartnerFeederName();
		}

		parameters.put(Database.QueryFields.ID, role.getId());
		parameters.put(Database.QueryFields.CODE, role.getCode());
		parameters.put(Database.QueryFields.TYPE, role.getType().toString());
		parameters.put(Database.QueryFields.ID_USER, (role.isUserRole()) ? ((UserRole) role).getUserId() : null);
		parameters.put(Database.QueryFields.BEGIN_DATE, this.agentDatabase.getDateAsTimestamp(role.getInternalBeginDate()));
		parameters.put(Database.QueryFields.EXPIRE_DATE, expireDate != null ? this.agentDatabase.getDateAsTimestamp(expireDate) : null);
		parameters.put(Database.QueryFields.PARTNER_ID, partnerId);
		parameters.put(Database.QueryFields.PARTNER_SERVICE_NAME, partnerServiceName);
		parameters.put(Database.QueryFields.CACHE, role.getLabel());

		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.ROLE_SAVE, parameters);

		return true;
	}

	public boolean remove(Role role) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		parameters.put(Database.QueryFields.ID, role.getId());
		this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.ROLE_REMOVE, parameters);

		return true;
	}

	public Object newObject() {
		return null;
	}

	public void loadAttribute(EventObject eventObject, String attribute) {
		Role role = (Role) eventObject.getSource();

		if (attribute.equals(Role.USER))
			this.loadUser(role);
		else if (attribute.equals(Role.PARTNER))
			this.loadPartner(role);

	}

}
