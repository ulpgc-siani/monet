package org.monet.bpi.java;

import org.apache.commons.lang.NotImplementedException;
import org.monet.bpi.Role;
import org.monet.bpi.RoleChooser;

import java.util.List;

public class RoleChooserImpl implements RoleChooser {

	@Override
	public List<Role> getCandidates() {
		throw new NotImplementedException();
	}

	@Override
	public Role findByPartnerName(String partnerName) {
		throw new NotImplementedException();
	}

	@Override
	public void choose(Role role) {
		throw new NotImplementedException();
	}

	@Override
	public void chooseNone() {
		throw new NotImplementedException();
	}

}
