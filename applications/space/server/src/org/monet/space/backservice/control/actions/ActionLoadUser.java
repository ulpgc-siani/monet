package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.ErrorCode;
import org.monet.space.kernel.components.layers.FederationLayer;
import org.monet.space.kernel.components.monet.federation.ComponentFederationMonet;
import org.monet.space.kernel.model.Account;

public class ActionLoadUser extends Action {
	private FederationLayer federationLayer;

	public ActionLoadUser() {
		this.federationLayer = ComponentFederationMonet.getInstance().getLayer(createConfiguration());
	}

	@Override
	public String execute() {
		Account account;
		String codeAccount = (String) this.parameters.get(Parameter.CODE);

		if (codeAccount == null) {
			return ErrorCode.WRONG_PARAMETERS;
		}

		account = this.federationLayer.loadAccount(codeAccount);

		return account.getUser().serializeToXML();
	}

}
