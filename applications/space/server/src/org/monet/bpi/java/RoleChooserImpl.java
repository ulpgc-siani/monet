package org.monet.bpi.java;

import org.monet.bpi.Role;
import org.monet.bpi.RoleChooser;

import java.util.ArrayList;
import java.util.List;

public class RoleChooserImpl implements RoleChooser {

	private org.monet.space.kernel.model.RoleChooser roleChooser;

	void inject(org.monet.space.kernel.model.RoleChooser roleChooser) {
		this.roleChooser = roleChooser;
	}

	@Override
	public List<Role> getCandidates() {
		ArrayList<Role> roles = new ArrayList<Role>();
		for (org.monet.space.kernel.model.Role role : roleChooser.getRoleList()) {
			RoleImpl roleImpl = new RoleImpl();
			roleImpl.injectRole(role);
			roles.add(roleImpl);
		}
		return roles;
	}

	@Override
	public Role findByPartnerName(String partnerName) {
		org.monet.space.kernel.model.Role role = this.roleChooser.findByPartnerName(partnerName);

		RoleImpl roleImpl = new RoleImpl();
		roleImpl.injectRole(role);

		return roleImpl;
	}

	@Override
	public void choose(Role role) {
		this.roleChooser.setChoosedRole(((RoleImpl) role).role);
	}

	@Override
	public void chooseNone() {
		this.roleChooser.setChoosedNone(true);
	}

}
