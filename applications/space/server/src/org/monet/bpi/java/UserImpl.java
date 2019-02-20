package org.monet.bpi.java;

import org.monet.bpi.User;

public class UserImpl implements User {

	protected org.monet.space.kernel.model.User user;

	void injectUser(org.monet.space.kernel.model.User user) {
		this.user = user;
	}

	@Override
	public String getId() {
		return this.user.getId();
	}

	@Override
	public String getName() {
		return this.user.getName();
	}

	@Override
	public String getEmail() {
		return this.user.getInfo().getEmail();
	}

	@Override
	public String getFullName() {
		return this.user.getInfo().getFullname();
	}

}
