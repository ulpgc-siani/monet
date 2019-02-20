package client.core.system.workmap;

import client.core.model.definition.entity.PlaceDefinition;

public abstract class Action<Definition extends PlaceDefinition.ActionDefinition> implements client.core.model.workmap.Action<Definition> {
	private Definition definition;

	public Action() {
	}

	public Action(Definition definition) {
		this.definition = definition;
	}

	@Override
	public Definition getDefinition() {
		return this.definition;
	}

	@Override
	public void setDefinition(Definition definition) {
		this.definition = definition;
	}

}