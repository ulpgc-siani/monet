package org.monet.space.office.presentation.user.renders;

import org.monet.metamodel.FileFieldProperty;
import org.monet.space.kernel.model.Attribute;
import org.monet.space.kernel.model.Indicator;
import org.monet.space.office.configuration.Configuration;

import java.util.HashMap;

public class FileFieldViewRender extends FieldViewRender {

	public FileFieldViewRender() {
		super();
	}

	@Override
	protected String getFieldType() {
		return "file";
	}

	@Override
	protected String initBody(String id, HashMap<String, Object> viewMap, HashMap<String, Object> declarationsMap, Attribute attribute, boolean isTemplate) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String value = this.getIndicatorValue(attribute, Indicator.VALUE);
		String details = this.getIndicatorValue(attribute, Indicator.DETAILS);
		Configuration configuration = Configuration.getInstance();
		FileFieldProperty definition = (FileFieldProperty) this.definition;

		map.put("id", id);
		map.put("action", configuration.getFmsServletUrl() + "?op=downloaddocument&nid=" + this.field.getNode().getId() + "&f=" + value);
		map.put("value", value);
		map.put("label", details != null && !details.isEmpty() ? details : value);

		map.put("limit", definition.getLimit() != null ? definition.getLimit() : "");
		declarationsMap.put("concreteDeclarations", block("field.file$concreteDeclarations", map));

		return (value.isEmpty()) ? block("field.file.empty", map) : block("field.file", map);
	}

}
