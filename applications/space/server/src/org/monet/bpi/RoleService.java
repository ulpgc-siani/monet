package org.monet.bpi;

import org.monet.bpi.types.Date;

import java.util.List;

public abstract class RoleService {

	protected static RoleService instance;

	public static List<Role> getNonExpiredRoles(String name) {
		return instance.getNonExpiredRolesImpl(name);
	}

	public static Role getRole(String id) {
		return instance.getRoleImpl(id);
	}

	public static Role assignRoleToUser(String name, String username, Date from, Date to) {
		return instance.assignRoleToUserImpl(name, username, from, to);
	}

	public static Role assignRoleToService(String name, String serviceName, String serviceUrl, Date from, Date to) {
		return instance.assignRoleToServiceImpl(name, serviceName, serviceUrl, from, to);
	}

	protected abstract List<Role> getNonExpiredRolesImpl(String name);

	protected abstract Role getRoleImpl(String id);

	protected abstract Role assignRoleToUserImpl(String name, String username, Date from, Date to);

	protected abstract Role assignRoleToServiceImpl(String name, String serviceName, String serviceUrl, Date from, Date to);

}