package client.services.http.builders;

import client.core.model.Account;
import client.core.model.List;
import client.core.model.Node;
import client.core.system.MonetList;
import client.services.http.HttpInstance;
import client.services.http.HttpList;

public class AccountBuilder implements Builder<Account, List<Account>> {
	@Override
	public client.core.model.Account build(HttpInstance instance) {
		if (instance == null)
			return null;

		client.core.system.Account account = new client.core.system.Account();
		initialize(account, instance);
		return account;
	}

	@Override
	public void initialize(client.core.model.Account object, HttpInstance instance) {
		client.core.system.Account account = (client.core.system.Account)object;
		account.setUser(new UserBuilder().build(instance.getObject("user")));
		account.setRootNode((Node)new NodeBuilder().build(instance.getObject("rootNode")));
	}

	@Override
	public List<client.core.model.Account> buildList(HttpList instances) {
		return new MonetList<>();
	}
}
