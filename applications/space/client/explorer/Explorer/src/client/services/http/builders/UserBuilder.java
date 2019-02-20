package client.services.http.builders;

import client.core.model.List;
import client.core.model.User;
import client.core.system.MonetList;
import client.services.http.HttpInstance;
import client.services.http.HttpList;

public class UserBuilder implements Builder<User, List<User>> {
	@Override
	public client.core.model.User build(HttpInstance instance) {
		if (instance == null)
			return null;

		client.core.system.User user = new client.core.system.User();
		initialize(user, instance);
		return user;
	}

	@Override
	public void initialize(client.core.model.User object, HttpInstance instance) {
		client.core.system.User user = (client.core.system.User)object;

		user.setId(instance.getString("id"));
		user.setFullName(instance.getString("fullName"));
		user.setEmail(instance.getString("email"));
		user.setPhoto(instance.getString("photo"));
	}

	@Override
	public List<client.core.model.User> buildList(HttpList instance) {
		return new MonetList<>();
	}
}
