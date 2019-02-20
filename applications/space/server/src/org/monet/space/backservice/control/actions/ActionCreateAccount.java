package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.ErrorCode;
import org.monet.space.kernel.components.ComponentFederation;
import org.monet.space.kernel.components.layers.FederationLayer;
import org.monet.space.kernel.components.layers.RoleLayer;
import org.monet.space.kernel.library.LibraryEncoding;
import org.monet.space.kernel.model.Account;
import org.monet.space.kernel.model.UserInfo;

import java.util.Date;

public class ActionCreateAccount extends Action {

	public ActionCreateAccount() {
	}

	@Override
	public String execute() {
		String code = (String) this.parameters.get(Parameter.CODE);
		String fullname = (String) this.parameters.get(Parameter.FULLNAME);
		String email = (String) this.parameters.get(Parameter.EMAIL);
		String roles = (String) this.parameters.get(Parameter.ROLES);

		if (code == null || fullname == null || email == null)
			return ErrorCode.WRONG_PARAMETERS;

		ComponentFederation componentFederation = ComponentFederation.getInstance();
		FederationLayer federationLayer = componentFederation.getLayer(createConfiguration());
		RoleLayer roleLayer = componentFederation.getRoleLayer();

		UserInfo userInfo = new UserInfo();
		userInfo.setFullname(LibraryEncoding.decode(fullname));
		userInfo.setEmail(LibraryEncoding.decode(email));

		Account account = federationLayer.createAccount(null, code, userInfo);
		for (String role : LibraryEncoding.decode(roles).split(",")) {
			String roleCode = this.dictionary.getDefinitionCode(role);
			roleLayer.addUserRole(roleCode, account.getUser(), new Date(), null);
		}

		federationLayer.createOrUpdateAccount(account);

		return account.serializeToXML();
	}

}
