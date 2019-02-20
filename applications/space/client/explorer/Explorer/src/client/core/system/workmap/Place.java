package client.core.system.workmap;

import client.core.model.workmap.Action;

public class Place<T extends Action> implements client.core.model.workmap.Place<T> {
	private String code;
	private T action;

	public Place() {
	}

	public Place(T action) {
		this.action = action;
	}

	@Override
	public final ClassName getClassName() {
		return client.core.model.workmap.Place.CLASS_NAME;
	}

	@Override
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public T getAction() {
		return this.action;
	}

	@Override
	public void setAction(T action) {
		this.action = action;
	}

}

