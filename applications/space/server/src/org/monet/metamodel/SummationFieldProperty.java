package org.monet.metamodel;

import org.monet.metamodel.interfaces.IsInitiable;

import java.util.HashMap;

// SelectFieldDeclaration
// Declaración que se utiliza para modelar un	campo de selección

public class SummationFieldProperty extends SummationFieldPropertyBase implements IsInitiable {

	@Override
	public void init() {
//SUMMATION NOT USED		SelectProperty selectDefinition = this.getSelect();
//
//		if (selectDefinition == null || selectDefinition.getDepth() == null)
//			selectDefinition._depth = 1L;
//
//		for (SummationItemProperty itemDeclaration : this._termsProperty._summationItemPropertyList) {
//			map.put(itemDeclaration.getKey(), itemDeclaration);
//		}
	}

	private HashMap<String, SummationItemProperty> map = new HashMap<String, SummationItemProperty>();

	public SummationItemProperty getChild(String key) {
		return this.map.get(key);
	}

}
