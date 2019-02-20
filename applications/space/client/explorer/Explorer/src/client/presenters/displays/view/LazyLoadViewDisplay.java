package client.presenters.displays.view;

import client.core.model.Entity;
import client.core.model.View;

public class LazyLoadViewDisplay<EntityType extends Entity, ViewType extends View> extends ViewDisplay<EntityType, ViewType> {

	public LazyLoadViewDisplay(EntityType entity, ViewType view) {
		super(entity, view);
	}

	@Override
	protected void onInjectServices() {
	}
}
