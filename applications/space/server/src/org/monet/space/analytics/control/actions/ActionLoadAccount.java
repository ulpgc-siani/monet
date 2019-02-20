package org.monet.space.analytics.control.actions;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.monet.space.kernel.model.Account;
import org.monet.space.kernel.model.BusinessUnit;
import org.monet.space.kernel.model.FederationUnit;
import org.monet.space.kernel.model.FederationUnitList;

import java.util.UUID;

public class ActionLoadAccount extends Action {

	public ActionLoadAccount() {
		super();
	}

	@Override
	public String execute() {

		if (!this.isLogged())
			return this.launchAuthenticateAction();

		Account account = this.getFederationLayer().loadAccount();

		JSONObject jsonAccount = new JSONObject();
		jsonAccount = account.toJson(null);
		jsonAccount.put("units", this.getMembers());
		jsonAccount.put("instanceId", UUID.randomUUID().toString());

		return jsonAccount.toJSONString();
	}

	private JSONArray getMembers() {
		FederationUnitList federationUnitList = this.getFederationLayer().loadMembers();
		JSONArray result = new JSONArray();
		String businessUnitName = BusinessUnit.getInstance().getName();

		for (FederationUnit federationUnit : federationUnitList) {
			String name = federationUnit.getName();

			JSONObject jsonPartner = new JSONObject();
			jsonPartner.put("id", name);
			jsonPartner.put("label", federationUnit.getLabel());
			jsonPartner.put("url", federationUnit.getUrl());
			jsonPartner.put("active", businessUnitName.equals(name));
			jsonPartner.put("disabled", false);

			result.add(jsonPartner);
		}

		return result;
	}

}
