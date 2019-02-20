package org.monet.space.office.presentation.user.renders;

import org.monet.metamodel.SerialFieldProperty;
import org.monet.space.kernel.model.Attribute;
import org.monet.space.kernel.model.Indicator;

import java.util.HashMap;

public class SerialFieldViewRender extends FieldViewRender {

	public SerialFieldViewRender() {
		super();
	}

	@Override
	protected String getFieldType() {
		return "serial";
	}

	@Override
	protected String initBody(String id, HashMap<String, Object> viewMap, HashMap<String, Object> declarationsMap, Attribute attribute, boolean isTemplate) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		SerialFieldProperty definition = (SerialFieldProperty) this.definition;
		SerialFieldProperty.SerialProperty serialDefinition = definition.getSerial();

		map.put("format", serialDefinition != null ? serialDefinition.getFormat() : "");
		declarationsMap.put("concreteDeclarations", block("field.serial$concreteDeclarations", map));

		map.put("id", id);
		map.put("code", this.getIndicatorValue(attribute, Indicator.CODE));
		map.put("value", this.getIndicatorValue(attribute, Indicator.VALUE));

		return block("field.serial", map);
	}

}
