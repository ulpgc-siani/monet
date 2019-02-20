package org.monet.space.office.presentation.user.renders;

import org.monet.metamodel.NumberFieldProperty;
import org.monet.space.kernel.model.Attribute;
import org.monet.space.kernel.model.Indicator;

import java.util.HashMap;

public class NumberFieldViewRender extends FieldViewRender {

	public NumberFieldViewRender() {
		super();
	}

	@Override
	protected String getFieldType() {
		return "number";
	}

	@Override
	protected String initBody(String id, HashMap<String, Object> viewMap, HashMap<String, Object> declarationsMap, Attribute attribute, boolean isTemplate) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		NumberFieldProperty definition = (NumberFieldProperty) this.definition;
		NumberFieldProperty.RangeProperty rangeDefinition = definition.getRange();
		String metrics = "", metricLabel = "";

		//TODO: REVISAR ESTO!!
//    Collection<MetricProperty> metricDefinitionList = definition.getMetricPropertyList();
//    String metricValue = this.getIndicatorValue(attribute, Indicator.METRIC);
//
//    Language language = Language.getInstance();
//
//    for (MetricProperty metricDefinition : metricDefinitionList) {
//      HashMap<String, Object> localMap = new HashMap<String, Object>();
//      Boolean isSelected = false;
//
//      if (metricValue.isEmpty()) {
//        isSelected = metricDefinition.isDefault();
//        metricLabel = language.getModelResource(metricDefinition.getLabel());
//      } else {
//        isSelected = metricValue.equals(metricDefinition.getCode());
//        if (metricValue.equals(metricDefinition.getCode()))
//          metricLabel = language.getModelResource(metricDefinition.getLabel());
//      }
//
//      localMap.put("code", metricDefinition.getCode());
//      localMap.put("label", language.getModelResource(metricDefinition.getLabel()));
//      localMap.put("equivalence", (metricDefinition.getEquivalence() != null) ? String.valueOf(metricDefinition.getEquivalence().getValue()) : "");
//      localMap.put("isDefault", metricDefinition.isDefault() ? "default" : "");
//      localMap.put("isSelected", isSelected ? "selected" : "");
//      declarationMetrics += block("field.number$metrics$item.declaration", localMap);
//      metrics += block("field.number$metrics$item", localMap);
//    }
//    map.put("metrics", block("field.number$metrics.declaration", "items", declarationMetrics));

		map.put("range", "");
		if (definition.getRange() != null) {
			HashMap<String, Object> localMap = new HashMap<String, Object>();
			localMap.put("min", String.valueOf(rangeDefinition.getMin()));
			localMap.put("max", String.valueOf(rangeDefinition.getMax()));
			map.put("range", block("field.number$range", localMap));
		}
		map.put("format", definition.getFormat() != null ? definition.getFormat() : "");
		declarationsMap.put("concreteDeclarations", block("field.number$concreteDeclarations", map));

		map.put("id", id);
		map.put("value", this.getIndicatorValue(attribute, Indicator.VALUE));
		map.put("metrics", block("field.number$metrics", "items", metrics));
		map.put("metricLabel", metricLabel);

		return block("field.number", map);
	}

}
