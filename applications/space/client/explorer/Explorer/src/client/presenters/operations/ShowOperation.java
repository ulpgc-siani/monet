package client.presenters.operations;

import client.core.model.Entity;
import client.core.model.View;

public abstract class ShowOperation<E extends Entity, V extends View> extends Operation implements EntityOperation<E, V> {
	protected E entity;
	protected V view;

	public static final Type TYPE = new Type("Operation", cosmos.presenters.Operation.TYPE);

	protected ShowOperation(Context context, E entity, V view) {
		super(context);
		this.entity = entity;
		this.view = view;
	}

	public E getEntity() {
		return entity;
	}

	public V getView() {
		return view;
	}

	public void setView(V view) {
		this.view = view;
	}

}
