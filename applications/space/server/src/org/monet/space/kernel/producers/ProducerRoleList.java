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

import org.monet.space.kernel.constants.Database;
import org.monet.space.kernel.constants.ErrorCode;
import org.monet.space.kernel.constants.Producers;
import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.*;
import org.monet.space.kernel.model.Dictionary;
import org.monet.space.kernel.model.Role.Nature;
import org.monet.space.kernel.sql.QueryBuilder;

import java.sql.ResultSet;
import java.util.*;

public class ProducerRoleList extends ProducerList {

	public ProducerRoleList() {
		super();
	}

	public RoleList load(DataRequest dataRequest) {
		ResultSet result = null;
		RoleList roleList;
		Map<String, Object> parameters = new HashMap<>();
		Map<String, String> subQueries = new HashMap<>();
		ProducerRole producerRole = (ProducerRole) this.producersFactory.get(Producers.ROLE);

		try {
			roleList = new RoleList();

			this.addCodeToQuery(dataRequest, parameters, subQueries);
			this.addTypeToQuery(dataRequest, parameters, subQueries);
			this.addNatureToQuery(dataRequest, parameters, subQueries);
			this.addNonExpiredToQuery(dataRequest, parameters, subQueries);
			this.addConditionToQuery(dataRequest, parameters, subQueries);

			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.ROLE_LIST_LOAD_COUNT, parameters, subQueries);
			if (!result.next())
				throw new Exception("Can't get total count of roles");
			roleList.setTotalCount(result.getInt("counter"));

			this.agentDatabase.closeQuery(result);

			int limit = dataRequest.getLimit();
			if (limit == -1) limit = roleList.getTotalCount();

			parameters.put(Database.QueryFields.START_POS, this.agentDatabase.getQueryStartPos(dataRequest.getStartPos()));
			parameters.put(Database.QueryFields.LIMIT, limit);

			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.ROLE_LIST_LOAD, parameters, subQueries);

			while (result.next()) {
				Role role = producerRole.fill(result);
				roleList.add(role);
			}

		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_ROLELIST, "role list", exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return checkServiceRoles(roleList);
	}

	public List<String> loadUsersIds(String roleCode) {
		ResultSet result = null;
		ArrayList<String> userList = new ArrayList<String>();
		HashMap<String, Object> parameters = new HashMap<String, Object>();

		try {
			roleCode = Dictionary.getInstance().getDefinitionCode(roleCode);

			parameters.put(Database.QueryFields.CODE, roleCode);
			parameters.put(Database.QueryFields.DATE, this.agentDatabase.getDateAsTimestamp(new Date()));

			result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.ROLE_LIST_LOAD_USERS_IDS, parameters);

			while (result.next()) {
				String userId = result.getString("id_user");
				if (userId != null)
					userList.add(userId);
			}
		} catch (Exception exception) {
			throw new DataException(ErrorCode.LOAD_USERS_OF_ROLE, roleCode, exception);
		} finally {
			this.agentDatabase.closeQuery(result);
		}

		return userList;
	}

	private void addNonExpiredToQuery(DataRequest dataRequest, Map<String, Object> parameters, Map<String, String> subQueries) {
		String nonExpired = dataRequest.getParameter(DataRequest.NON_EXPIRED);

		if (nonExpired != null && Boolean.valueOf(nonExpired) == true) {
			QueryBuilder queryBuilder = new QueryBuilder(this.agentDatabase.getRepositoryQuery(Database.Queries.ROLE_LIST_LOAD_SUBQUERY_NON_EXPIRED));
			parameters.put(Database.QueryFields.DATE, this.agentDatabase.getDateAsTimestamp(new Date()));
			subQueries.put(Database.QueryFields.NON_EXPIRED, queryBuilder.build());
		} else {
			subQueries.put(Database.QueryFields.NON_EXPIRED, Strings.EMPTY);
		}
	}

	private void addCodeToQuery(DataRequest dataRequest, Map<String, Object> parameters, Map<String, String> subQueries) {
		String code = dataRequest.getCode();

		if (code != null && !code.isEmpty()) {
			subQueries.put(Database.QueryFields.CODE_SUBQUERY, this.agentDatabase.getRepositoryQuery(Database.Queries.ROLE_LIST_LOAD_SUBQUERY_CODE));
			parameters.put(Database.QueryFields.CODE, code);
		} else
			subQueries.put(Database.QueryFields.CODE_SUBQUERY, Strings.EMPTY);
	}

	private void addTypeToQuery(DataRequest dataRequest, Map<String, Object> parameters, Map<String, String> subQueries) {
		String type = dataRequest.getParameter(DataRequest.MODE);

		if (type != null && !type.isEmpty()) {
			subQueries.put(Database.QueryFields.TYPE_SUBQUERY, this.agentDatabase.getRepositoryQuery(Database.Queries.ROLE_LIST_LOAD_SUBQUERY_TYPE));
			parameters.put(Database.QueryFields.TYPE, type);
		} else
			subQueries.put(Database.QueryFields.TYPE_SUBQUERY, Strings.EMPTY);
	}

	private void addNatureToQuery(DataRequest dataRequest, Map<String, Object> parameters, Map<String, String> subQueries) {
		String type = dataRequest.getParameter(DataRequest.NATURE);

		if (type != null && !type.equals(Nature.Both.toString())) {
			if (type.equals(Nature.Internal.toString()))
				subQueries.put(Database.QueryFields.NATURE, this.agentDatabase.getRepositoryQuery(Database.Queries.ROLE_LIST_LOAD_SUBQUERY_NATURE_INTERNAL));
			else if (type.equals(Nature.External.toString()))
				subQueries.put(Database.QueryFields.NATURE, this.agentDatabase.getRepositoryQuery(Database.Queries.ROLE_LIST_LOAD_SUBQUERY_NATURE_EXTERNAL));
			else
				subQueries.put(Database.QueryFields.NATURE, Strings.EMPTY);
		} else
			subQueries.put(Database.QueryFields.NATURE, Strings.EMPTY);
	}

	private void addConditionToQuery(DataRequest dataRequest, Map<String, Object> parameters, Map<String, String> subQueries) {
		String condition = dataRequest.getCondition();

		if (condition != null && !condition.isEmpty()) {
			subQueries.put(Database.QueryFields.CONDITION, this.agentDatabase.getRepositoryQuery(Database.Queries.ROLE_LIST_LOAD_SUBQUERY_CONDITION));
			parameters.put(Database.QueryFields.CONDITION, condition);
		} else
			subQueries.put(Database.QueryFields.CONDITION, Strings.EMPTY);
	}

	private RoleList checkServiceRoles(RoleList roleList) {
		RoleList result = new RoleList();
		int rejectedRoles = 0;

		for (Role role : roleList) {
			if (role.isServiceRole() && (((ServiceRole)role).getPartnerId() == null)) {
				rejectedRoles++;
				continue;
			}
			result.add(role);
		}

		int totalCount = roleList.getTotalCount()-rejectedRoles;
		result.setTotalCount(totalCount>0?totalCount:0);

		return result;
	}

	public Object newObject() {
		return null;
	}

	public void loadAttribute(EventObject eventObject, String attribute) {
	}

}
