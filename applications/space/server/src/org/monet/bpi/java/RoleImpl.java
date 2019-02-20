package org.monet.bpi.java;

import org.monet.bpi.Role;
import org.monet.bpi.User;
import org.monet.space.kernel.model.FeederRole;
import org.monet.space.kernel.model.ServiceRole;
import org.monet.space.kernel.model.UserRole;

public class RoleImpl implements Role {

	org.monet.space.kernel.model.Role role;

	void injectRole(org.monet.space.kernel.model.Role role) {
		this.role = role;
	}

	@Override
	public String getId() {
		return this.role.getId();
	}

	@Override
	public String getName() {
		return this.role.getName();
	}

	@Override
	public String getLabel() {
		return this.role.getLabel();
	}

	@Override
	public User getUser() {
		UserImpl user = new UserImpl();

		if (this.role.isUserRole())
			user.injectUser(((UserRole) this.role).getUser());

		return user;
	}

	@Override
	public String getPartnerName() {

		if (this.role.isServiceRole())
			return ((ServiceRole) this.role).getPartnerName();
		else if (this.role.isFeederRole())
			return ((FeederRole) this.role).getPartnerName();

		return "";
	}

	@Override
	public String getServiceUrl() {

		if (this.role.isServiceRole())
			return ((ServiceRole) this.role).getPartnerServiceUrl();

		return "";
	}

	@Override
	public String getFeederUrl() {

		if (this.role.isFeederRole())
			return ((FeederRole) this.role).getPartnerFeederUrl();

		return "";
	}

}
