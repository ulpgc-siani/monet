package client.core.system;

import client.core.model.definition.entity.EntityDefinition;

public class Command<T extends EntityDefinition> extends Entity<T> implements client.core.model.Command<T> {
	private boolean enabled;
	private boolean visible;

	public Command() {
	}

	public Command(Key key, String label, boolean enabled) {
		super(null, label);
		setKey(key);
		this.enabled = enabled;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public boolean isVisible() {
		return this.visible;
	}

	@Override
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof Key)
			return getKey().equals(object);

		if (object instanceof String)
			return object.equals(getKey().getCode()) || object.equals(getKey().getName());

		if (!(object instanceof Command))
			return false;

		return getKey().equals(((Command) object).getKey());
	}

	@Override
	public final ClassName getClassName() {
		return client.core.model.Command.CLASS_NAME;
	}

}
