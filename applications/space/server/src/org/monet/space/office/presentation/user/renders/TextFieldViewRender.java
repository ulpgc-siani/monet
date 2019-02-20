package org.monet.space.office.presentation.user.renders;

import org.monet.metamodel.TextFieldProperty;
import org.monet.space.kernel.model.Attribute;
import org.monet.space.kernel.model.Indicator;

import java.util.HashMap;

public class TextFieldViewRender extends FieldViewRender {

	public TextFieldViewRender() {
		super();
	}

	@Override
	protected String getFieldType() {
		return "text";
	}

	@Override
	protected String initBody(String id, HashMap<String, Object> viewMap, HashMap<String, Object> declarationsMap, Attribute attribute, boolean isTemplate) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		TextFieldProperty definition = (TextFieldProperty) this.definition;
		TextFieldProperty.EditionProperty editionDefinition = definition.getEdition();
		TextFieldProperty.LengthProperty lengthDefinition = definition.getLength();
		String datastore = "";

		if (definition.getEnableHistory() != null) {
			datastore = definition.getEnableHistory().getDatastore();
			if (datastore == null || datastore.isEmpty())
				datastore = definition.getCode();
		}

		map.put("historyDatastore", datastore);
		map.put("edition", (editionDefinition != null) ? editionDefinition.getMode().toString().toLowerCase() : "");
		map.put("length", (lengthDefinition != null) ? String.valueOf(lengthDefinition.getMax()) : "");

		String patterns = "";
		for (TextFieldProperty.PatternProperty patternDefinition : definition.getPatternList()) {
			int pos = 0;
			String indicators = "";
			HashMap<String, Object> localMap = new HashMap<String, Object>();

			for (TextFieldProperty.PatternProperty.MetaProperty metaDefinition : patternDefinition.getMetaList()) {
				localMap.put("comma", (pos != 0) ? "comma" : "");
				localMap.put("indicator", metaDefinition.getIndicator());
				indicators += block("field.text$pattern$indicator", localMap);
				localMap.clear();
				pos++;
			}

			localMap.put("indicators", indicators);
			localMap.put("regexp", this.language.getModelResource(patternDefinition.getRegexp()));

			patterns += block("field.text$pattern", localMap);
		}
		map.put("patterns", patterns);
		declarationsMap.put("concreteDeclarations", block("field.text$concreteDeclarations", map));

		map.put("id", id);
		map.put("code", this.getIndicatorValue(attribute, Indicator.CODE));
		map.put("value", this.getIndicatorValue(attribute, Indicator.VALUE));

		return block("field.text", map);
	}

}
