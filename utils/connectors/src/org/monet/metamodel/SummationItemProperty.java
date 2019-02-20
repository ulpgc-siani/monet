package org.monet.metamodel;

import org.monet.metamodel.interfaces.IsInitiable;

import java.util.HashMap;

// SummationItemDeclaration

public class SummationItemProperty extends SummationItemPropertyBase implements IsInitiable {

	private HashMap<String, SummationItemProperty> map = new HashMap<String, SummationItemProperty>();

	@Override
	public void init() {
		for (SummationItemProperty itemDeclaration : this._summationItemPropertyList) {
			map.put(itemDeclaration.getKey(), itemDeclaration);
		}
	}

	public SummationItemProperty getChild(String key) {
		return this.map.get(key);
	}

}
