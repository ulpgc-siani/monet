package org.monet.space.office.presentation.user.renders;

import org.apache.commons.lang.StringEscapeUtils;
import org.monet.metamodel.SummationFieldProperty;
import org.monet.metamodel.SummationItemProperty;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.model.Attribute;
import org.monet.space.kernel.model.Indicator;
import org.monet.space.kernel.model.SummationItem;
import org.monet.space.kernel.model.SummationItemList;
import org.monet.space.office.configuration.Configuration;
import org.simpleframework.xml.core.Persister;

import java.util.HashMap;

public class SummationFieldViewRender extends FieldViewRender {

	public SummationFieldViewRender() {
		super();
	}

	@Override
	protected String getFieldType() {
		return "summation";
	}

	@Override
	protected String initBody(String id, HashMap<String, Object> viewMap, HashMap<String, Object> declarationsMap, Attribute attribute, boolean isTemplate) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String items = "";
		String value = this.getIndicatorValue(attribute, Indicator.VALUE);
		String details = this.getIndicatorValue(attribute, Indicator.DETAILS);
		SummationFieldProperty definition = (SummationFieldProperty) this.definition;
//SUMMATION NOT USED		SummationFieldProperty.SelectProperty selectDefinition = definition.getSelect();
//		String formatDefinition = definition.getFormat();
		SummationItemList summationItemList = null;
		Persister persister = new Persister();

//SUMMATION NOT USED		if (details.isEmpty())
//			summationItemList = new SummationItemList(definition.getTerms().getSummationItemPropertyList());
//		else {
			try {
				details = StringEscapeUtils.unescapeHtml(details);
				summationItemList = persister.read(SummationItemList.class, details);
			} catch (Exception e) {
				AgentLogger.getInstance().error(e);
			}
//		}

		declarationsMap.put("concreteDeclarations", block("field.summation$concreteDeclarations", map));

		for (SummationItem item : summationItemList.Items)
			items += this.initSummationFieldItem(item, 0);

		map.put("id", id);
		map.put("items", items);

//SUMMATION NOT USED		map.put("source", this.getSourceId(attribute, definition.getSource().getValue()));
//		map.put("flatten", (selectDefinition.getFlatten() != null) ? selectDefinition.getFlatten().toString().toLowerCase() : SelectFieldPropertyBase.SelectProperty.FlattenEnumeration.ALL.toString().toLowerCase());
//		map.put("depth", String.valueOf(selectDefinition.getDepth()));
//		map.put("from", (selectDefinition.getFrom() != null) ? selectDefinition.getFrom() : "");
//		map.put("format", formatDefinition != null ? formatDefinition : "");
		declarationsMap.put("concreteDeclarations", block("field.summation$concreteDeclarations", map));

		map.put("themeSource", Configuration.getInstance().getApiUrl() + "?op=loadthemefile");
		map.put("value", value);

		return block("field.summation", map);
	}

	private String initSummationFieldItem(SummationItem item, int position) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String items = "";
		String blockName = "", labelBlock = "";

		if (item.Children.size() > 0)
			blockName = "field.summation$item.composite";
		else {
			if (item.Type == SummationItemProperty.TypeEnumeration.SIMPLE)
				blockName = "field.summation$item.simple";
		}

		if (item.IsMultiple)
			labelBlock = "field.summation$item.simple$label.multiple";
		else {
			if (item.Children.size() > 0)
				labelBlock = "field.summation$item.simple$label.single$expandable";
			else
				labelBlock = "field.summation$item.simple$label.single";
		}

		map.put("value", String.valueOf(item.Value));
		map.put("type", item.Type.toString().toLowerCase());
		map.put("themeSource", Configuration.getInstance().getApiUrl() + "?op=loadthemefile");
		map.put("label", item.Label); // Do not delete this map attribute although
		// been override on next line
		map.put("label", block(labelBlock, map));
		map.put("multiple", item.IsMultiple ? "multiple" : "");
		map.put("negative", item.IsNegative ? "negative" : "");
		map.put("evenOdd", (position % 2 == 0) ? "even" : "odd");

		for (SummationItem child : item.Children) {
			position = position + 1;
			items += this.initSummationFieldItem(child, position);
		}
		map.put("items", items);

		return block(blockName, map);
	}

}
