package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.MessageCode;
import org.monet.space.kernel.components.layers.FederationLayer;
import org.monet.space.kernel.components.monet.federation.ComponentFederationMonet;
import org.monet.space.kernel.library.LibraryEncoding;
import org.monet.space.kernel.model.Account;
import org.monet.space.kernel.model.User;

public class ActionSaveUser extends Action {
	private FederationLayer federationLayer;

	public ActionSaveUser() {
		this.federationLayer = ComponentFederationMonet.getInstance().getLayer(createConfiguration());
	}

	@Override
	public String execute() {
		String code = (String) this.parameters.get(Parameter.CODE);
		String data = (String) this.parameters.get(Parameter.DATA);

		User user = new User();
		user.deserializeFromXML(LibraryEncoding.decode(data), null);

		Account account = new Account();
		account.setCode(code);
		account.setUser(user);

		this.federationLayer.saveAccount(account);

		return MessageCode.ACCOUNT_SAVED;
	}

}
