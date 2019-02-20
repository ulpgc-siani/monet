package org.monet.metamodel;

import org.monet.metamodel.interfaces.IsInitiable;

import java.util.ArrayList;

/**
 * LineActionProperty
 */

public class LineActionProperty extends LineActionPropertyBase implements IsInitiable {
	ArrayList<LineStopProperty> lineStopPropertyList = new ArrayList<LineStopProperty>();

	@Override
	public void init() {

		this.lineStopPropertyList.addAll(this._lineStopPropertyMap.values());

		for (LineStopProperty stopDefinition : this.lineStopPropertyList) {
			this._lineStopPropertyMap.put(stopDefinition.getCode(), stopDefinition);
		}
	}

	@Override
	public java.util.Map<String, LineStopProperty> getStopMap() {
		return this._lineStopPropertyMap;
	}

	@Override
	public java.util.Collection<LineStopProperty> getStopList() {
		return this.lineStopPropertyList;
	}

}
