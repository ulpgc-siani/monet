package org.monet.space.office.presentation.user.renders;

import org.monet.metamodel.*;
import org.monet.metamodel.LinkFieldProperty.SourceProperty.FilterProperty.OperatorEnumeration;
import org.monet.metamodel.internal.Ref;
import org.monet.space.kernel.model.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static org.monet.space.kernel.model.DataRequest.OPERATOR_SEPARATOR;

public class LinkFieldViewRender extends FieldViewRender {

	public LinkFieldViewRender() {
		super();
	}

	@Override
	protected String getFieldType() {
		return "link";
	}

	@Override
	protected String initBody(String id, HashMap<String, Object> viewMap, HashMap<String, Object> declarationsMap, Attribute attribute, boolean isTemplate) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String code = this.getIndicatorValue(attribute, Indicator.CODE);
		LinkFieldProperty fieldDefinition = (LinkFieldProperty) this.definition;
		LinkFieldProperty.SourceProperty fieldSourceDefinition = fieldDefinition.getSource();
		IndexDefinition indexDefinition = this.dictionary.getIndexDefinition(fieldSourceDefinition.getIndex().getValue());
		String datastore = "", attributes = "", header;
		int pos = 0;
		String nodeTypes = "";
		String value = this.getIndicatorValue(attribute, Indicator.VALUE);
		String collectionId = null;

		if (fieldDefinition.getEnableHistory() != null) {
			datastore = fieldDefinition.getEnableHistory().getDatastore();
			if (datastore == null || datastore.isEmpty())
				datastore = fieldDefinition.getCode();
		}

		Collection<AttributeProperty> attributeDefinitions = new ArrayList<AttributeProperty>();
		if (fieldSourceDefinition.getView() != null)
			attributeDefinitions = indexDefinition.getAttributes(indexDefinition.getViewMap().get(fieldSourceDefinition.getView().getValue()));
		else if (indexDefinition.getReference() != null)
			attributeDefinitions = indexDefinition.getReference().getAttributePropertyList();

		for (AttributeProperty attributeDefinition : attributeDefinitions) {
			HashMap<String, Object> localMap = new HashMap<String, Object>();

			localMap.put("code", attributeDefinition.getCode());
			localMap.put("label", org.monet.space.office.core.model.Language.getInstance().getModelResource(attributeDefinition.getLabel()));
			localMap.put("class", (pos == 0) ? ""/*"valuecl"*/ : "");

			attributes += block("field.link$concreteDeclarations$header$attribute", localMap);
			pos++;
		}

		if (fieldDefinition.allowAdd()) {
			String codeSourceDefinition = this.dictionary.getDefinitionCode(fieldDefinition.getSource().getCollection().getValue());
			NodeDefinition sourceDefinition = this.dictionary.getNodeDefinition(codeSourceDefinition);

			collectionId = this.renderLink.locateNode(codeSourceDefinition).getId();

			pos = 0;
			HashMap<String, Object> localMap = new HashMap<String, Object>();
			if (((CollectionDefinition) sourceDefinition).getAdd() != null) {
				for (Ref add : ((CollectionDefinition) sourceDefinition).getAdd().getNode()) {
					for (Definition definition : this.dictionary.getAllImplementersOfNodeDefinition(add.getValue())) {
						if (definition.isDisabled())
							continue;
						String codeNode = definition.getCode();
						localMap.put("comma", (pos > 0) ? "comma" : "");
						localMap.put("nodeType", codeNode);
						nodeTypes += block("field.link$concreteDeclarations$nodeType", localMap);
						pos++;
					}
				}
			}
		}

		map.put("attributes", attributes);
		header = block("field.link$concreteDeclarations$header", map);

		map.clear();
		map.put("source", indexDefinition.getCode());
		map.put("collection", collectionId != null ? collectionId : "");
		map.put("nodeTypes", nodeTypes);
		map.put("allowOther", (fieldDefinition.allowAdd()) ? "other" : "");
		map.put("allowHistory", datastore);
		map.put("allowSearch", fieldDefinition.allowSearch() ? indexDefinition.getCode() : "");
		map.put("allowLocations", fieldDefinition.allowLocations() ? indexDefinition.getCode() : "");
		map.put("header", header);
		map.put("filters", SerializerData.serialize(this.getLinkFieldSourceFiltersMask(fieldSourceDefinition, indexDefinition)));
		declarationsMap.put("concreteDeclarations", block("field.link$concreteDeclarations", map));

		map.put("id", id);
		map.put("idNode", this.field.getNode().getId());
		map.put("code", code);
		map.put("value", value);

		return value.isEmpty() ? block("field.link.empty", map) : block("field.link", map);
	}

	private LinkedHashMap<String, Object> getLinkFieldSourceFiltersMask(LinkFieldProperty.SourceProperty sourceDefinition, IndexDefinition indexDefinition) {
		LinkedHashMap<String, Object> filtersMap = new LinkedHashMap<String, Object>();

		for (LinkFieldProperty.SourceProperty.FilterProperty filterDefinition : sourceDefinition.getFilterList()) {
			Object value = filterDefinition.getValue();
			String attributeName = filterDefinition.getAttribute().getValue();
			String operator = filterDefinition.getOperator() != null ? filterDefinition.getOperator().toString() : OperatorEnumeration.EQUALS.toString();
			String filterValue = "";

			if (value instanceof String)
				filterValue = (String) value;
			else if (value instanceof Ref) {
				String fieldName = ((Ref) value).getValue();
				Node node = this.field.getNode();
				filterValue = "_field:" + ((FormDefinition) node.getDefinition()).getField(fieldName).getCode();
			}

			if (filterValue.isEmpty()) continue;

			filtersMap.put(indexDefinition.getAttribute(attributeName).getCode(), filterValue + OPERATOR_SEPARATOR + operator);
		}

		return filtersMap;
	}

}
