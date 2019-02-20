package org.monet.space.kernel.components.layers;

import org.monet.space.kernel.model.*;

import java.util.Date;
import java.util.List;

public interface RoleLayer extends Layer {
	public RoleList loadRoleList(String code, DataRequest dataRequest);

	public List<String> loadRoleListUsersIds(String code);

	public Role loadRole(String id);

	public boolean saveRole(Role role);

	public boolean existsNonExpiredUserRole(String code, String userId);

	public boolean existsNonExpiredUserRole(String code, User user);

	public boolean existsNonExpiredServiceRole(String code, FederationUnit partner, FederationUnitService partnerService);

	public boolean existsNonExpiredFeederRole(String code, FederationUnit partner, FederationUnitFeeder partnerFeeder);

	public Role addUserRole(String code, User user, Date beginDate, Date expireDate);

	public Role addServiceRole(String code, FederationUnit partner, FederationUnitService partnerService, Date beginDate, Date expireDate);

	public Role addFeederRole(String code, FederationUnit partner, FederationUnitFeeder partnerFeeder, Date beginDate, Date expireDate);

	public boolean deleteRole(String id);
}
