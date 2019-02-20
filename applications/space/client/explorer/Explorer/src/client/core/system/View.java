package client.core.system;

import client.core.model.definition.views.ViewDefinition;

public abstract class View<T extends ViewDefinition> extends Entity<T> implements client.core.model.View<T> {
	private boolean isDefault;

	public View() {
	}

	public View(client.core.model.Key key, String label, boolean isDefault) {
		super(null, label);
		setKey(key);
		this.isDefault = isDefault;
	}

	@Override
	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof String || object instanceof client.core.model.Key)
			return getKey().equals(object);

		if (!(object instanceof View))
			return false;

		return getKey().equals(((View)object).getKey());
	}
}
