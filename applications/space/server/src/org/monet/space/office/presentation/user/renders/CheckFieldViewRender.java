package org.monet.space.office.presentation.user.renders;

import org.monet.metamodel.CheckFieldProperty;
import org.monet.metamodel.SourceDefinition;
import org.monet.metamodel.internal.Ref;
import org.monet.space.office.configuration.Configuration;
import org.monet.space.kernel.model.*;

import java.util.HashMap;

public class CheckFieldViewRender extends FieldViewRender {

	public CheckFieldViewRender() {
		super();
	}

	@Override
	protected String getFieldType() {
		return "check";
	}

	@Override
	protected String initBody(String id, HashMap<String, Object> viewMap, HashMap<String, Object> declarationsMap, Attribute attribute, boolean isTemplate) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		Attribute[] optionAttributes = new Attribute[0];
		CheckFieldProperty definition = (CheckFieldProperty) this.definition;
		TermList termList = new TermList();
		HashMap<String, Object> termsMap;
		HashMap<String, Boolean> checkedAttributes = new HashMap<String, Boolean>();
		boolean flatten = false;
		StringBuilder sources = new StringBuilder();
		String sourceId = "";

		if (attribute != null) {
			optionAttributes = attribute.getAttributeList().get().values().toArray(new Attribute[0]);

			for (Attribute optionAttribute : optionAttributes) {
				String code = this.getIndicatorValue(optionAttribute, Indicator.CODE);
				String checked = this.getIndicatorValue(optionAttribute, Indicator.CHECKED);
				checkedAttributes.put(code, (checked.equals("true") || checked.equals("yes")));
			}
		}

		if (definition.getTerms() != null) {
			termList = new TermList(definition.getTerms().getTermPropertyList());
			if (termList.getCount() == 0)
				termList = new TermList(attribute!=null?attribute.getAttributeList():new AttributeList());
		} else if (definition.getSource() != null) {
			HashMap<String, Object> localMap = new HashMap<String, Object>();
			CheckFieldProperty.SelectProperty selectDefinition = definition.getSelect();
			Ref sourceRef = definition.getSource();
			SourceDefinition sourceDefinition = this.dictionary.getSourceDefinition(sourceRef.getValue());
			SourceList sourceList = this.renderLink.loadSourceList(sourceDefinition.getCode(), this.field.getNode().getPartnerContext());
			sourceId = this.getSourceId(attribute, sourceRef.getValue());

			for (Source<SourceDefinition> source : sourceList.get().values()) {
				localMap.put("id", source.getId());
				localMap.put("label", source.getLabel());
				localMap.put("partner", source.getPartnerName());
				sources.append(block("field.check$declarationSource", localMap));
			}

			Node node = this.field.getNode();
			String from = this.getIndicatorValue(attribute, Indicator.FROM);
			if (from.isEmpty())
				from = selectDefinition != null ? this.getFieldSourceFrom(node, selectDefinition.getRoot()) : this.getFieldSourceFrom(node, null);

			DataRequest dataRequest = new DataRequest();
			dataRequest.addParameter(DataRequest.MODE, (selectDefinition != null && selectDefinition.getFlatten() != null && selectDefinition.getFlatten().equals(CheckFieldProperty.SelectProperty.FlattenEnumeration.ALL)) ? DataRequest.Mode.FLATTEN : DataRequest.Mode.TREE);
			dataRequest.addParameter(DataRequest.FLATTEN, (selectDefinition != null && selectDefinition.getFlatten() != null) ? selectDefinition.getFlatten().toString() : CheckFieldProperty.SelectProperty.FlattenEnumeration.NONE.toString());
			dataRequest.addParameter(DataRequest.DEPTH, (selectDefinition != null && selectDefinition.getDepth() != null) ? String.valueOf(selectDefinition.getDepth()) : null);
			dataRequest.addParameter(DataRequest.FROM, from);
			dataRequest.addParameter(DataRequest.FILTERS, SerializerData.serializeSet(this.getFieldFilters(definition, false)));
			flatten = dataRequest.getParameter(DataRequest.MODE).equals(DataRequest.Mode.FLATTEN);

			if (sourceId != null && !sourceId.isEmpty())
				termList = this.renderLink.loadSourceTerms(sourceId, dataRequest);
			else
				termList = new TermList();

			map.put("from", selectDefinition != null ? this.getFieldSourceFromMask(node, selectDefinition.getRoot()) : this.getFieldSourceFromMask(node, null));
		} else if (attribute != null)
			termList = new TermList(attribute.getAttributeList());

		map.put("declarationSources", sources.toString());
		map.put("nodeId", this.field.getNode().getId());
		declarationsMap.put("concreteDeclarations", block("field.check$concreteDeclarations", map));

		termsMap = this.initCheckFieldTermList(termList, flatten, id, checkedAttributes);
		map.put("source", sourceId);
		map.put("items", termsMap.get("checkTerms"));

		return block("field.check", map);
	}

	private HashMap<String, Object> initCheckFieldTermList(TermList termList, boolean flatten, String id, HashMap<String, Boolean> checkedAttributes) {
		int pos = 0;
		HashMap<String, Object> result = new HashMap<String, Object>();
		String checkTerms = "";

		for (Term term : termList) {
			HashMap<String, Object> termMap = this.initCheckFieldTerm(term, null, flatten, id, checkedAttributes, pos);
			checkTerms += termMap.get("checkTerms");
			pos++;
		}

		result.put("checkTerms", checkTerms);

		return result;
	}

	private HashMap<String, Object> initCheckFieldTerm(Term term, Term parent, boolean flatten, String id, HashMap<String, Boolean> checkedAttributes, int pos) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		HashMap<String, Object> result = new HashMap<String, Object>();
		Boolean checked = checkedAttributes.get(term.getCode());
		String checkedBlock;
		Configuration configuration = Configuration.getInstance();
		CheckFieldProperty definition = (CheckFieldProperty) this.definition;

		map.put("themeSource", configuration.getApiUrl() + "?op=loadthemefile");
		map.put("id", id + pos);
		map.put("code", term.getCode());
		map.put("label", flatten ? term.getFlattenLabel() : term.getLabel());
		map.put("ancestorLevel", String.valueOf(term.getAncestorLevel()));
		map.put("checked", checked != null ? "true" : "");
		map.put("parent", parent != null ? parent.getCode() : "");

		checkedBlock = (checked != null && checked) ? block("field.check$item$checked", map) : block("field.check$item$unchecked", map);
		map.put("checked", checkedBlock);
		map.put("allowCode", definition.allowKey() ? block("field.check$allowCode", map) : "");

		if (term.isCategory()) {
			result.put("checkTerms", block("field.check$item.category", map));
		} else if (term.isSuperTerm()) {
			result.put("checkTerms", block("field.check$item.super", map));
		} else {
			result.put("checkTerms", block("field.check$item", map));
		}

		map.clear();

		for (Term childTerm : term.getTermList()) {
			pos++;
			HashMap<String, Object> termsMap = this.initCheckFieldTerm(childTerm, term, flatten, id, checkedAttributes, pos);
			result.put("checkTerms", result.get("checkTerms").toString() + termsMap.get("checkTerms").toString());
		}

		return result;
	}

}
