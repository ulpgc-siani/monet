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

package org.monet.space.kernel.model;

import net.minidev.json.JSONObject;

public class UserRole extends Role {
	private String userId;
	private User user;

	public static final String MANAGER = "manager";
	public static final String HOLDER = "holder";

	public String getLabel() {
		User user = this.getUser();
		if (user == null) return this.getDefinition().getLabelString();
		return user.getInfo().getFullname();
	}

	public User getUser() {
		if (this.userId == null)
			return null;

		onLoad(this, Role.USER);

		if (!this.user.getId().equals(this.userId)) {
			this.removeLoadedAttribute(Role.USER);
			onLoad(this, Role.USER);
		}

		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
		this.setUserId(user != null ? user.getId() : null);
		this.addLoadedAttribute(Role.USER);
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String id) {
		this.userId = id;
	}

	@Override
	public void addJsonAttributes(JSONObject result) {
		result.put("type", Type.User.toString().toLowerCase());
		result.put("user", this.getUser().toJson());
	}

}
