package org.monet.space.office.presentation.user.renders;

import org.monet.metamodel.DateFieldProperty;
import org.monet.space.kernel.model.Attribute;
import org.monet.space.kernel.model.Indicator;
import org.monet.space.office.core.model.Language;
import org.monet.space.office.presentation.user.constants.RenderParameter;

import java.util.HashMap;

import static org.monet.metamodel.DateFieldPropertyBase.PurposeEnumeration.DISTANT_DATE;

public class DateFieldViewRender extends FieldViewRender {

	public DateFieldViewRender() {
		super();
	}

	@Override
	protected String getFieldType() {
		return "date";
	}

	@Override
	protected String initBody(String id, HashMap<String, Object> viewMap, HashMap<String, Object> declarationsMap, Attribute attribute, boolean isTemplate) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		DateFieldProperty declaration = (DateFieldProperty) this.definition;
		String edition = this.getParameterAsString(RenderParameter.EDITION);

		if ((edition == null || edition.isEmpty()) && declaration.getPurpose() != null)
			edition = declaration.getPurpose().equals(DISTANT_DATE) ? "random" : "sequential";

		map.put("format", declaration.getFormat(Language.getCurrent()));
		map.put("edition", !edition.isEmpty() ? edition : "sequential");
		declarationsMap.put("concreteDeclarations", block("field.date$concreteDeclarations", map));

		map.put("id", id);
		map.put("value", this.getIndicatorValue(attribute, Indicator.VALUE));
		map.put("internal", this.getIndicatorValue(attribute, Indicator.INTERNAL));

		return block("field.date", map);
	}

}
