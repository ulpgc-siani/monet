package client.presenters.operations;

import client.core.model.Entity;
import client.core.model.View;

public interface EntityOperation<T extends Entity, V extends View> {
	T getEntity();
	V getView();
	String getDescription();
}
