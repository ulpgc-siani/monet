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

package org.monet.space.kernel.components.monet;

import org.monet.space.kernel.components.ComponentSecurity;
import org.monet.space.kernel.constants.PermissionType;
import org.monet.space.kernel.exceptions.SystemException;
import org.monet.space.kernel.model.Account;
import org.monet.space.kernel.model.ISecurable;
import org.monet.space.kernel.model.Role;

import java.util.HashMap;
import java.util.Set;

public class ComponentSecurityMonet extends ComponentSecurity {

	public static final String NAME = "nodesmonet";

	private enum Access {NONE, DENY, GRANT}

	private ComponentSecurityMonet() {
		super();
	}

	public synchronized static ComponentSecurity getInstance() {
		if (instance == null) instance = new ComponentSecurityMonet();
		return instance;
	}

	public void run() throws SystemException {
		if (this.isRunning) return;
		this.isRunning = true;
	}

	public void stop() throws SystemException {
		if (!this.isRunning) return;
		instance = null;
		this.isRunning = false;
	}

	private Access hasExplicitReadPermissions(ISecurable securable, Account account) {
		HashMap<Integer, String> hmUserPermissions;

		if (account == null) return Access.NONE;

		if (securable.getPermissionList().allowAllUsers())
			return Access.GRANT;

		hmUserPermissions = securable.getPermissionList().getByUser(account.getUser().getId());

		if (hmUserPermissions.containsKey(PermissionType.DENY_READ)) return Access.DENY;
		if (hmUserPermissions.containsKey(PermissionType.READ)) return Access.GRANT;
		if (hmUserPermissions.containsKey(PermissionType.READ_WRITE)) return Access.GRANT;
		if (hmUserPermissions.containsKey(PermissionType.READ_WRITE_CREATE_DELETE)) return Access.GRANT;

		return Access.NONE;
	}

	private Access hasExplicitWritePermissions(ISecurable securable, Account account) {
		HashMap<Integer, String> hmUserPermissions;

		if (account == null) return Access.NONE;

		hmUserPermissions = securable.getPermissionList().getByUser(account.getUser().getId());

		if (hmUserPermissions.containsKey(PermissionType.DENY_WRITE)) return Access.DENY;
		if (hmUserPermissions.containsKey(PermissionType.READ_WRITE)) return Access.GRANT;
		if (hmUserPermissions.containsKey(PermissionType.READ_WRITE_CREATE_DELETE)) return Access.GRANT;

		return Access.NONE;
	}

	private Access hasExplicitCreateDeletePermissions(ISecurable securable, Account account) {
		HashMap<Integer, String> hmUserPermissions;

		if (account == null) return Access.NONE;

		hmUserPermissions = securable.getPermissionList().getByUser(account.getUser().getId());

		if (hmUserPermissions.containsKey(PermissionType.DENY_CREATE_DELETE)) return Access.DENY;
		if (hmUserPermissions.containsKey(PermissionType.READ_WRITE)) return Access.GRANT;
		if (hmUserPermissions.containsKey(PermissionType.READ_WRITE_CREATE_DELETE)) return Access.GRANT;

		return Access.NONE;
	}

	private Access hasImplicitPermissions(ISecurable securable, Account account) {
		Set<String> hsUsers, hsRoles;
		String idUser;

		if (securable.isPublic()) return Access.GRANT;
		if (account == null) return Access.NONE;

		idUser = account.getUser().getId();
		hsRoles = securable.getRoles();
		for (Role role : account.getRoleList()) {
			if (hsRoles.contains(role.getCode())) return Access.GRANT;
		}

		hsUsers = securable.getGrantedUsers();
		if (hsUsers.contains(idUser)) return Access.GRANT;

		return Access.NONE;
	}

	private Access hasRulesPermissions(ISecurable securable, Account account) {
		//HashSet<String> hsRules = securable.getRules();
		return Access.NONE;
	}

	@Override
	public HashMap<Integer, Boolean> getSupportedFeatures() {
		return new HashMap<Integer, Boolean>();
	}

	@Override
	public Boolean canCreate(ISecurable securable, Account account) {
		Access Result;

		Result = this.hasExplicitCreateDeletePermissions(securable, account);
		if (Result == Access.DENY) return false;
		if (Result == Access.GRANT) return true;

		Result = this.hasImplicitPermissions(securable, account);
		if (Result == Access.DENY) return false;
		if (Result == Access.GRANT) return true;

		Result = this.hasRulesPermissions(securable, account);
		if (Result == Access.DENY) return false;
		if (Result == Access.GRANT) return true;

		return false;
	}

	@Override
	public Boolean canDelete(ISecurable securable, Account account) {
		if (!securable.isDeletable()) return false;
		if (securable.isLinked()) return false;
		return this.canCreate(securable, account);
	}

	@Override
	public Boolean canRead(ISecurable securable, Account account) {
		Access Result;

		Result = this.hasExplicitReadPermissions(securable, account);
		if (Result == Access.DENY) return false;
		if (Result == Access.GRANT) return true;

		Result = this.hasImplicitPermissions(securable, account);
		if (Result == Access.DENY) return false;
		if (Result == Access.GRANT) return true;

		Result = this.hasRulesPermissions(securable, account);
		if (Result == Access.DENY) return false;
		if (Result == Access.GRANT) return true;

		return false;
	}

	@Override
	public Boolean canWrite(ISecurable securable, Account account) {
		Access Result;

		Result = this.hasExplicitWritePermissions(securable, account);
		if (Result == Access.DENY) return false;
		if (Result == Access.GRANT) return true;

		Result = this.hasImplicitPermissions(securable, account);
		if (Result == Access.DENY) return false;
		if (Result == Access.GRANT) return true;

		Result = this.hasRulesPermissions(securable, account);
		if (Result == Access.DENY) return false;
		if (Result == Access.GRANT) return true;

		return false;
	}

	@Override
	public void reset() {
	}

	@Override
	public void create() {
	}

	@Override
	public void destroy() {
	}

}