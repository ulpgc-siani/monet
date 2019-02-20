package org.monet.space.kernel.model;

public class RoleChooser {

	private RoleList roleList;
	private Role choosedRole = null;
	private boolean choosedNone = false;

	public RoleChooser(RoleList roleList) {
		this.roleList = roleList;
	}

	public RoleList getRoleList() {
		return roleList;
	}

	public Role findByPartnerName(String partnerName) {

		for (Role role : roleList) {
			if (role.isServiceRole()) {
				ServiceRole instance = (ServiceRole) role;
				if (instance.getPartner().getName().equals(partnerName))
					return role;
			}
		}

		return null;
	}

	public void setRoleList(RoleList roleList) {
		this.roleList = roleList;
	}

	public Role getChoosedRole() {
		return choosedRole;
	}

	public void setChoosedRole(Role choosedRole) {
		this.choosedRole = choosedRole;
	}

	public Boolean isNoneChoosed() {
		return this.choosedNone;
	}

	public void setChoosedNone(boolean value) {
		this.choosedNone = value;
	}

}