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

import org.monet.space.kernel.constants.Common;
import org.monet.space.kernel.constants.Strings;

import java.util.HashMap;
import java.util.Iterator;

public class PermissionList extends BaseModelList<Permission> {
	private MonetHashMap<HashMap<Integer, String>> usersPermissions;

	public PermissionList() {
		super();
		this.usersPermissions = new MonetHashMap<HashMap<Integer, String>>();
	}

	public PermissionList(PermissionList permissionList) {
		this();
		this.code = permissionList.code;
		this.items.putAll(permissionList.items);
		this.codes.putAll(permissionList.codes);
		this.usersPermissions.putAll(permissionList.usersPermissions);
	}

	public void add(Permission nodePermission) {
		String id, userId, code;
		HashMap<Integer, String> userPermissions;

		if ((userPermissions = this.getByUser(nodePermission.getIdUser())) != null) {
			if (userPermissions.containsKey(nodePermission.getType())) {
				Permission currentPermission = this.items.get(userPermissions.get(nodePermission.getType()));
				currentPermission.setExpireDate(nodePermission.getInternalExpireDate());
				return;
			}
		}

		code = nodePermission.getCode();
		id = nodePermission.getId();
		if ((id.equals(Strings.UNDEFINED_ID)) || (id.equals(Strings.EMPTY))) {
			id = String.valueOf(this.items.keySet().size()) + Common.Suffix.ARTIFICIAL_ID;
		}

		this.items.put(id, nodePermission);
		if (!code.equals(Strings.UNDEFINED_ID)) this.codes.put(code, id);

		userId = nodePermission.getIdUser();
		if (!this.usersPermissions.containsKey(userId))
			this.usersPermissions.put(userId, new HashMap<Integer, String>());

		userPermissions = this.usersPermissions.get(userId);
		userPermissions.put(nodePermission.getType(), nodePermission.getId());
	}

	public HashMap<Integer, String> getByUser(String idUser) {
		if (!this.usersPermissions.containsKey(idUser)) return new HashMap<Integer, String>();
		return this.usersPermissions.get(idUser);
	}

	public boolean allowAllUsers() {
		return this.usersPermissions.containsKey(Strings.UNDEFINED_ID) || this.usersPermissions.containsKey(null);
	}

	public void deleteNodePermissions(String nodeId) {
		Iterator<Permission> iterPermissions = this.items.values().iterator();
		Iterator<Integer> iterUserPermissions;

		while (iterPermissions.hasNext()) {
			Permission oNodePermission = iterPermissions.next();

			if (oNodePermission.getIdObject() == nodeId) {
				HashMap<Integer, String> hmUserPermissions;

				hmUserPermissions = this.getByUser(oNodePermission.getIdUser());

				iterUserPermissions = hmUserPermissions.keySet().iterator();
				while (iterUserPermissions.hasNext()) {
					Integer iKey = iterUserPermissions.next();
					String sIdPermission = hmUserPermissions.get(iKey);
					if (sIdPermission == oNodePermission.getId()) hmUserPermissions.remove(iKey);
				}

				this.items.remove(oNodePermission.getId());
			}

		}
	}

	public void deleteUserPermissions(String userId) {
		HashMap<Integer, String> userPermissions = this.getByUser(userId);
		Iterator<Integer> iterUserPermissions;

		iterUserPermissions = userPermissions.keySet().iterator();
		while (iterUserPermissions.hasNext()) {
			int iKey = iterUserPermissions.next();
			String idPermission = userPermissions.get(iKey);
			this.delete(idPermission);
		}

	}

}