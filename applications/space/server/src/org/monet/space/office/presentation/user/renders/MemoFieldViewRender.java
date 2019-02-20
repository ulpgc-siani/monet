package org.monet.space.office.presentation.user.renders;

import org.monet.metamodel.MemoFieldProperty;
import org.monet.space.office.presentation.user.constants.RenderParameter;
import org.monet.space.kernel.model.Attribute;
import org.monet.space.kernel.model.Indicator;

import java.util.HashMap;

public class MemoFieldViewRender extends FieldViewRender {

	public MemoFieldViewRender() {
		super();
	}

	@Override
	protected String getFieldType() {
		return "text";
	}

	@Override
	protected String initBody(String id, HashMap<String, Object> viewMap, HashMap<String, Object> declarationsMap, Attribute attribute, boolean isTemplate) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String value = this.getIndicatorValue(attribute, Indicator.VALUE);
		MemoFieldProperty definition = (MemoFieldProperty) this.definition;
		String edition = this.getParameterAsString(RenderParameter.EDITION);
		MemoFieldProperty.LengthProperty lengthDefinition = definition.getLength();

		map.put("historyDatastore", definition.getEnableHistory() != null ? definition.getEnableHistory().getDatastore() : "");
		map.put("edition", edition);
		map.put("length", (lengthDefinition != null) ? String.valueOf(lengthDefinition.getMax()) : "");
		declarationsMap.put("concreteDeclarations", block("field.memo$concreteDeclarations", map));

		map.put("id", id);
		map.put("value", value != null ? value.replace("\n", "<br/>") : "");

		return block("field.memo", map);
	}

}
