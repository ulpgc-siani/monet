package client.services.http.builders;

import client.core.model.List;
import client.core.system.MonetList;
import client.core.system.Role;
import client.services.http.HttpInstance;
import client.services.http.HttpList;

import java.util.HashMap;
import java.util.Map;

import static client.core.model.Role.Type;

public class RoleBuilder<T extends client.core.model.Role> extends EntityBuilder<Role, T, List<T>> implements Builder<T, List<T>> {

	private static final Map<Type, RoleBuilder> roleCreators = new HashMap<Type, RoleBuilder>() {{
		put(Type.USER, new UserRoleBuilder());
		put(Type.SERVICE, new ServiceRoleBuilder());
	}};

	@Override
	public T build(HttpInstance instance) {
		Type type = Type.fromString(instance.getString("type"));
		return (T)roleCreators.get(type).build(instance);
	}

	@Override
	public void initialize(client.core.model.Role object, HttpInstance instance) {
		super.initialize(object, instance);

		Role role = (Role)object;
		role.setType(Type.fromString(instance.getString("type")));
		role.setId(instance.getString("id"));
		role.setLabel(instance.getString("label"));
	}

	@Override
	public List<T> buildList(HttpList instance) {
		List<T> result = new MonetList<>();

		for (int i = 0; i < instance.getItems().length(); i++)
			result.add(build(instance.getItems().get(i)));

		return result;
	}

}
