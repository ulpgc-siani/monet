package client.presenters.displays;

import client.core.model.Entity;
import client.core.model.View;

public abstract class EntityDisplay<T extends Entity, V extends View> extends Display {
	private T entity;
	private V view;

	public static final Type TYPE = new Type("EntityDisplay", Display.TYPE);

    public EntityDisplay(T entity, V view) {
        this.entity = entity;
	    this.view = view;
    }

    public T getEntity() {
        return entity;
    }

	public void setEntity(T entity) {
		this.entity = entity;
	}

	public String getLabel() {
		return "No label";
	}

	public V getView() {
		return view;
	}

	public void setView(V view) {
		this.view = view;
	}

}
