package org.monet.metamodel;

import org.monet.metamodel.interfaces.IsInitiable;

import java.util.ArrayList;

/**
 * DoorActionProperty
 */

public class DoorActionProperty extends DoorActionPropertyBase implements IsInitiable {
	ArrayList<ExitProperty> exitPropertyList = new ArrayList<ExitProperty>();

	@Override
	public void init() {
		this.exitPropertyList.addAll(this._exitPropertyMap.values());

		for (ExitProperty exitDefinition : this.exitPropertyList)
			this._exitPropertyMap.put(exitDefinition.getCode(), exitDefinition);
	}

	@Override
	public java.util.Map<String, ExitProperty> getExitMap() {
		return this._exitPropertyMap;
	}

	@Override
	public java.util.Collection<ExitProperty> getExitList() {
		return this.exitPropertyList;
	}

}
