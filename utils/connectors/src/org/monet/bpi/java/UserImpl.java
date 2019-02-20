package org.monet.bpi.java;

import org.monet.bpi.User;

public class UserImpl implements User {

	private org.monet.api.space.backservice.impl.model.User user;

	void injectUser(org.monet.api.space.backservice.impl.model.User user) {
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
