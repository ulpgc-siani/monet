package client.services.http.builders;

import client.core.model.List;
import client.core.system.View;
import client.services.http.HttpInstance;

public class ViewBuilder<T extends client.core.model.View> extends EntityBuilder<View, T, List<T>> implements Builder<T, List<T>> {

	@Override
	public T build(HttpInstance instance) {
		return super.build(instance);
	}

	@Override
	public void initialize(T object, HttpInstance instance) {
		super.initialize(object, instance);

		View view = (View) object;
		view.setDefault(instance.getBoolean("isDefault"));
	}
}
