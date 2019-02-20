package org.monet.bpi.java;

import org.monet.bpi.Role;
import org.monet.bpi.RoleService;
import org.monet.bpi.types.Date;
import org.monet.metamodel.RoleDefinition;
import org.monet.space.kernel.components.ComponentFederation;
import org.monet.space.kernel.components.layers.FederationLayer;
import org.monet.space.kernel.components.layers.RoleLayer;
import org.monet.space.kernel.model.*;
import org.monet.space.kernel.model.Role.Nature;

import java.util.ArrayList;
import java.util.List;

public class RoleServiceImpl extends RoleService {

	@Override
	public List<Role> getNonExpiredRolesImpl(String name) {
		RoleLayer roleLayer = ComponentFederation.getInstance().getRoleLayer();
		RoleDefinition roleDefinition = Dictionary.getInstance().getRoleDefinition(name);

		DataRequest dataRequest = new DataRequest();
		dataRequest.setStartPos(0);
		dataRequest.setLimit(-1);
		dataRequest.addParameter(DataRequest.NATURE, Nature.Both.toString());
		dataRequest.addParameter(DataRequest.NON_EXPIRED, "true");
		org.monet.space.kernel.model.RoleList monetRoleList = roleLayer.loadRoleList(roleDefinition.getCode(), dataRequest);

		ArrayList<Role> roles = new ArrayList<Role>();
		for (org.monet.space.kernel.model.Role role : monetRoleList) {
			RoleImpl roleImpl = new RoleImpl();
			roleImpl.injectRole(role);
			roles.add(roleImpl);
		}

		return roles;
	}

	@Override
	public Role getRoleImpl(String id) {
		RoleLayer roleLayer = ComponentFederation.getInstance().getRoleLayer();
		org.monet.space.kernel.model.Role role = roleLayer.loadRole(id);

		RoleImpl roleImpl = new RoleImpl();
		roleImpl.injectRole(role);

		return roleImpl;
	}

	public static void init() {
		instance = new RoleServiceImpl();
	}

	@Override
	protected Role assignRoleToUserImpl(String name, String username, Date from, Date to) {
		ComponentFederation componentFederation = ComponentFederation.getInstance();
		RoleLayer roleLayer = componentFederation.getRoleLayer();
		FederationLayer federationLayer = componentFederation.getDefaultLayer();
		String roleCode = Dictionary.getInstance().getDefinitionCode(name);

		Account account = federationLayer.locateAccount(username);
		if (account == null) {
			UserInfo userInfo = new UserInfo();
			userInfo.setFullname(username);
			account = federationLayer.createAccount(null, username, userInfo);
		}

		User user = account.getUser();
		org.monet.space.kernel.model.Role role = roleLayer.addUserRole(roleCode, user, from.getValue(), to != null ? to.getValue() : null);
		federationLayer.createOrUpdateAccount(account);

		RoleImpl roleImpl = new RoleImpl();
		roleImpl.injectRole(role);

		return roleImpl;
	}

	@Override
	protected Role assignRoleToServiceImpl(String name, String partnerName, String serviceName, Date from, Date to) {
		ComponentFederation componentFederation = ComponentFederation.getInstance();
		RoleLayer roleLayer = componentFederation.getRoleLayer();
		FederationLayer federationLayer = componentFederation.getDefaultLayer();
		String roleCode = Dictionary.getInstance().getDefinitionCode(name);

		FederationUnit partner = federationLayer.locatePartner(partnerName);
		if (partner == null)
			return null;

		FederationUnitService partnerService = partner.getService(serviceName);
		if (partnerService == null)
			return null;

		if (roleLayer.existsNonExpiredServiceRole(roleCode, partner, partnerService))
			return null;

		org.monet.space.kernel.model.Role role = roleLayer.addServiceRole(roleCode, partner, partnerService, from.getValue(), to != null ? to.getValue() : null);

		RoleImpl roleImpl = new RoleImpl();
		roleImpl.injectRole(role);

		return roleImpl;
	}

}
