package org.monet.bpi;

import java.util.List;

public interface RoleChooser {

	List<Role> getCandidates();

	Role findByPartnerName(String partnerName);

	void choose(Role role);

	void chooseNone();

}
