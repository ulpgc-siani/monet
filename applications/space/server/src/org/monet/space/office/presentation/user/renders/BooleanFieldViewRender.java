package org.monet.space.office.presentation.user.renders;

import org.monet.space.office.configuration.Configuration;
import org.monet.space.office.core.model.Language;
import org.monet.space.kernel.model.Attribute;
import org.monet.space.kernel.model.Indicator;

import java.util.HashMap;

public class BooleanFieldViewRender extends FieldViewRender {

	public BooleanFieldViewRender() {
		super();
	}

	@Override
	protected String getFieldType() {
		return "boolean";
	}

	@Override
	protected String initBody(String id, HashMap<String, Object> viewMap, HashMap<String, Object> declarationsMap, Attribute attribute, boolean isTemplate) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		Configuration configuration = Configuration.getInstance();
		String code, checkedBlock, label;
		Attribute optionAttribute = null;
		boolean checked;

		if (attribute != null)
			optionAttribute = attribute.getAttributeList().get(Attribute.OPTION);
		if (optionAttribute != null)
			attribute = optionAttribute;
		code = this.getIndicatorValue(attribute, Indicator.CODE);
		checked = (code.equals("true") || code.equals("yes"));

		map.put("id", id);
		map.put("themeSource", configuration.getApiUrl() + "?op=loadthemefile");
		checkedBlock = checked ? block("field.boolean$checked", map) : block("field.boolean$unchecked", map);

		map.clear();
		map.put("unchecked", checked ? "" : "unchecked");
		map.put("declarationLabel", Language.getInstance().getModelResource(this.definition.getLabel()).toLowerCase());
		label = block("field.boolean$label", map);

		map.clear();
		map.put("id", id);
		map.put("label", label);
		map.put("checked", checkedBlock);

		return block("field.boolean", map);
	}

}
