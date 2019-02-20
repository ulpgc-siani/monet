package org.monet.space.office.presentation.user.renders;

import org.monet.metamodel.FormDefinition;
import org.monet.metamodel.SelectFieldProperty;
import org.monet.metamodel.SelectFieldPropertyBase;
import org.monet.metamodel.SourceDefinition;
import org.monet.metamodel.internal.Ref;
import org.monet.space.kernel.model.*;

import java.util.HashMap;

public class SelectFieldViewRender extends FieldViewRender {

	public SelectFieldViewRender() {
		super();
	}

	@Override
	protected String getFieldType() {
		return "select";
	}

	@Override
	protected String initBody(String id, HashMap<String, Object> viewMap, HashMap<String, Object> declarationsMap, Attribute attribute, boolean isTemplate) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		SelectFieldProperty definition = (SelectFieldProperty) this.definition;
		boolean allowOther = definition.allowOther();
		SelectFieldProperty.SelectProperty selectDefinition = definition.getSelect();
		String code, value, other, datastore = "";
		Attribute optionAttribute = null;
		String blockName = "field.select";
		HashMap<String, Object> termsMap = null;
		boolean flatten = false;
		StringBuilder sources = new StringBuilder();
		String sourceId = "";
		boolean isEmbedded = selectDefinition != null && selectDefinition.isEmbedded();
		boolean isInline = !isEmbedded && definition.getTerms() != null;

		if (attribute != null)
			optionAttribute = attribute.getAttributeList().get(Attribute.OPTION);
		if (optionAttribute != null)
			attribute = optionAttribute;

		code = this.getIndicatorValue(attribute, Indicator.CODE);
		value = this.getIndicatorValue(attribute, Indicator.VALUE);
		other = this.getIndicatorValue(attribute, Indicator.OTHER);

		if (definition.getEnableHistory() != null) {
			datastore = definition.getEnableHistory().getDatastore();
			if (datastore == null || datastore.isEmpty())
				datastore = definition.getCode();
		}

		if (allowOther && (!other.isEmpty())) {
			value = this.getIndicatorValue(attribute, Indicator.OTHER);
			blockName = "field.select.other";
		}

		TermList termList = null;

		if (isEmbedded) {
			if (definition.getTerms() != null)
				termList = new TermList(definition.getTerms().getTermPropertyList());
			else {
				sourceId = this.getSourceId(attribute, definition.getSource().getValue());

				Node node = this.field.getNode();
				DataRequest dataRequest = new DataRequest();
				dataRequest.addParameter(DataRequest.MODE, (selectDefinition.getFlatten() != null && selectDefinition.getFlatten().equals(SelectFieldPropertyBase.SelectProperty.FlattenEnumeration.ALL)) ? DataRequest.Mode.FLATTEN : DataRequest.Mode.TREE);
				dataRequest.addParameter(DataRequest.FLATTEN, (selectDefinition.getFlatten() != null) ? selectDefinition.getFlatten().toString() : SelectFieldPropertyBase.SelectProperty.FlattenEnumeration.NONE.toString());
				dataRequest.addParameter(DataRequest.DEPTH, (selectDefinition.getDepth() != null) ? String.valueOf(selectDefinition.getDepth()) : null);
				dataRequest.addParameter(DataRequest.FROM, this.getFieldSourceFrom(node, selectDefinition.getRoot()));
				dataRequest.addParameter(DataRequest.FILTERS, SerializerData.serializeSet(this.getFieldFilters(definition, false)));
				flatten = dataRequest.getParameter(DataRequest.MODE).equals(DataRequest.Mode.FLATTEN);

				termList = this.renderLink.loadSourceTerms(sourceId, dataRequest);
			}
			blockName = "field.select.embedded";
		} else if (isInline) {
			termList = new TermList(definition.getTerms().getTermPropertyList());
		}

		if (termList != null) {
			HashMap<String, Object> localMap = new HashMap<String, Object>();

			termsMap = this.initSelectFieldTermList(termList, flatten, id, code, value, other);
			localMap.put("allowCode", definition.allowKey() ? "allowCode" : "");
			localMap.put("terms", termsMap.get("terms"));

			map.put("declarationTerms", block("field.select$declarationTerms.inline", localMap));
		} else {
			HashMap<String, Object> localMap = new HashMap<String, Object>();
			String sourceDefinitionCode = this.dictionary.getDefinitionCode(definition.getSource().getValue());
			SourceList sourceList = this.renderLink.loadSourceList(sourceDefinitionCode, this.field.getNode().getPartnerContext());
			sourceId = this.getSourceId(attribute, sourceDefinitionCode);
			Node node = this.field.getNode();

			for (Source<SourceDefinition> source : sourceList.get().values()) {
				localMap.put("id", source.getId());
				localMap.put("label", source.getInstanceLabel());
				localMap.put("partner", source.getPartnerName());
				sources.append(block("field.select$declarationSource", localMap));
			}

			localMap.clear();
			localMap.put("source", sourceId);
			localMap.put("flatten", (selectDefinition != null && selectDefinition.getFlatten() != null) ? selectDefinition.getFlatten().toString().toLowerCase() : SelectFieldPropertyBase.SelectProperty.FlattenEnumeration.ALL.toString());
			localMap.put("depth", selectDefinition != null ? String.valueOf(selectDefinition.getDepth()) : -1);
			localMap.put("from", selectDefinition != null ? this.getFieldSourceFromMask(node, selectDefinition.getRoot()) : this.getFieldSourceFromMask(node, null));
			localMap.put("partnerContext", selectDefinition != null ? this.getSelectFieldSourcePartnerContextMask(selectDefinition.getContext()) : this.getSelectFieldSourcePartnerContextMask(null));
			localMap.put("allowCode", definition.allowKey() ? "allowCode" : "");
			localMap.put("filters", SerializerData.serializeSet(this.getFieldFiltersMask(definition)));

			map.put("declarationTerms", block("field.select$declarationTerms", localMap));
		}

		map.put("allowCode", definition.allowKey() ? "allowCode" : "");
		map.put("historyDatastore", datastore);
		map.put("declarationSources", sources.toString());
		declarationsMap.put("concreteDeclarations", block("field.select$concreteDeclarations", map));

		map.put("id", id);
		map.put("code", code);
		map.put("value", value);
		map.put("other", other);
		map.put("items", termsMap != null ? termsMap.get("radioTerms") : "");
		map.put("source", sourceId);

		return block(blockName, map);
	}

	private String getSelectFieldSourcePartnerContextMask(Object partner) {
		String partnerParameter = "";

		if (partner == null)
			return partnerParameter;

		if (partner instanceof String)
			partnerParameter = (String) partner;
		else if (partner instanceof Ref) {
			String fieldName = ((Ref) partner).getValue();
			Node node = this.field.getNode();
			partnerParameter = "_field:" + ((FormDefinition) node.getDefinition()).getField(fieldName).getCode();
		}

		return partnerParameter;
	}

	private HashMap<String, Object> initSelectFieldTerm(Term term, boolean flatten, String id, String code, String value, String other, int pos) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String termCode = term.getCode();
		HashMap<String, Object> result = new HashMap<String, Object>();
		SelectFieldProperty definition = (SelectFieldProperty) this.definition;

		map.put("id", id);
		map.put("code", term.getCode());
		map.put("label", flatten ? term.getFlattenLabel() : term.getLabel());
		map.put("ancestorLevel", String.valueOf(term.getAncestorLevel()));

		map.put("checked", ((pos == 0 && (code.isEmpty() || other.isEmpty())) || (termCode.equals(code) || termCode.equals(other))) ? "true" : "");
		map.put("allowCode", definition.allowKey() ? block("field.select$allowCode", map) : "");

		if (term.isCategory()) {
			result.put("terms", block("field.select$declarationTerms.inline$term.category", map));
			result.put("radioTerms", block("field.select.embedded$item.category", map));
		} else if (term.isSuperTerm()) {
			result.put("terms", block("field.select$declarationTerms.inline$term.super", map));
			result.put("radioTerms", block("field.select.embedded$item.super", map));
		} else {
			result.put("terms", block("field.select$declarationTerms.inline$term", map));
			result.put("radioTerms", block("field.select.embedded$item", map));
		}

		map.clear();
		if (value.isEmpty() && pos == 0) {
			code = term.getCode();
			value = term.getLabel();
		}

		for (Term childTerm : term.getTermList()) {
			pos++;
			HashMap<String, Object> termsMap = this.initSelectFieldTerm(childTerm, flatten, id, code, value, other, pos);
			result.put("terms", result.get("terms").toString() + termsMap.get("terms").toString());
			result.put("radioTerms", result.get("radioTerms").toString() + termsMap.get("radioTerms").toString());
		}

		return result;
	}

	private HashMap<String, Object> initSelectFieldTermList(TermList termList, boolean flatten, String id, String code, String value, String other) {
		int pos = 0;
		HashMap<String, Object> result = new HashMap<String, Object>();
		String terms = "", radioTerms = "";

		for (Term term : termList) {
			HashMap<String, Object> termMap = this.initSelectFieldTerm(term, flatten, id, code, value, other, pos);
			terms += termMap.get("terms");
			radioTerms += termMap.get("radioTerms");
			pos++;
		}

		result.put("terms", terms);
		result.put("radioTerms", radioTerms);

		return result;
	}

}
