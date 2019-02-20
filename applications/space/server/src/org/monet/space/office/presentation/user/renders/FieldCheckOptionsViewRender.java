package org.monet.space.office.presentation.user.renders;

import org.monet.metamodel.CheckFieldProperty;
import org.monet.metamodel.internal.Ref;
import org.monet.space.kernel.model.*;
import org.monet.space.office.configuration.Configuration;

import java.util.HashMap;
import java.util.LinkedHashSet;

public class FieldCheckOptionsViewRender extends ViewRender {

	public FieldCheckOptionsViewRender() {
		super();
	}

	@Override
	public void setTarget(Object target) {
	}

	private LinkedHashSet<String> getCheckFieldFilters(CheckFieldProperty.SelectProperty selectDefinition) {
		LinkedHashSet<String> filtersSet = new LinkedHashSet<String>();
		Node node = (Node) this.getParameter("node");

		if (selectDefinition == null || selectDefinition.getFilter() == null)
			return filtersSet;

		for (Object tag : selectDefinition.getFilter().getTag()) {
			String filterValue = "";

			if (tag instanceof String)
				filterValue = (String) tag;
			else if (tag instanceof Ref) {
				String fieldName = ((Ref) tag).getValue();
				filterValue = node.getFieldValue(fieldName);
			}

			if (filterValue.isEmpty()) continue;

			filtersSet.add(filterValue);
		}

		return filtersSet;
	}

	private String initCheckFieldTerm(Term term, Term parent, boolean flatten, String id, HashMap<String, Boolean> checkedAttributes, int pos) {
		CheckFieldProperty definition = (CheckFieldProperty) this.getParameter("definition");
		HashMap<String, Object> map = new HashMap<String, Object>();
		Boolean checked = checkedAttributes.get(term.getCode());
		String checkedBlock;
		Configuration configuration = Configuration.getInstance();
		StringBuilder result = new StringBuilder();

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
			result.append(block("field.check$item.category", map));
		} else if (term.isSuperTerm()) {
			result.append(block("field.check$item.super", map));
		} else {
			result.append(block("field.check$item", map));
		}

		map.clear();

		for (Term childTerm : term.getTermList()) {
			pos++;
			String termValue = this.initCheckFieldTerm(childTerm, term, flatten, id, checkedAttributes, pos);
			result.append(termValue);
		}

		return result.toString();
	}

	private TermList loadTermList(CheckFieldProperty fieldDefinition) {
		TermList termList;
		String sourceId = (String) this.getParameter("sourceId");
		String from = (String) this.getParameter("from");
		Node node = (Node) this.getParameter("node");

		if (fieldDefinition.getTerms() != null)
			termList = new TermList(fieldDefinition.getTerms().getTermPropertyList());
		else {
			CheckFieldProperty.SelectProperty selectDefinition = fieldDefinition.getSelect();

			if (from == null)
				from = selectDefinition != null ? this.getFieldSourceFrom(node, selectDefinition.getRoot()) : this.getFieldSourceFrom(node, null);

			DataRequest dataRequest = new DataRequest();
			dataRequest.addParameter(DataRequest.MODE, (selectDefinition != null && selectDefinition.getFlatten() != null) ? DataRequest.Mode.FLATTEN : DataRequest.Mode.TREE);
			dataRequest.addParameter(DataRequest.FLATTEN, (selectDefinition != null && selectDefinition.getFlatten() != null) ? selectDefinition.getFlatten().toString() : org.monet.metamodel.CheckFieldProperty.SelectProperty.FlattenEnumeration.ALL.toString());
			dataRequest.addParameter(DataRequest.DEPTH, (selectDefinition != null && selectDefinition.getDepth() != null) ? String.valueOf(selectDefinition.getDepth()) : null);
			dataRequest.addParameter(DataRequest.FROM, from);
			dataRequest.addParameter(DataRequest.FILTERS, SerializerData.serializeSet(this.getCheckFieldFilters(selectDefinition)));

			if (sourceId != null && !sourceId.isEmpty())
				termList = this.renderLink.loadSourceTerms(sourceId, dataRequest);
			else
				termList = new TermList();
		}

		return termList;
	}

	@Override
	protected void init() {
		int pos = 0;
		CheckFieldProperty definition = (CheckFieldProperty) this.getParameter("definition");
		String fieldId = (String) this.getParameter("fieldId");
		TermList termList = this.loadTermList(definition);
		boolean flatten = false;
		StringBuilder checkTerms = new StringBuilder();

		loadCanvas("view.field.check.options");

		if (definition.getSelect() != null) {
			CheckFieldProperty.SelectProperty selectDefinition = definition.getSelect();
			flatten = selectDefinition != null && selectDefinition.getFlatten() != null;
		}

		for (Term term : termList) {
			String termValue = this.initCheckFieldTerm(term, null, flatten, fieldId, new HashMap<String, Boolean>(), pos);
			checkTerms.append(termValue);
			pos++;
		}

		addMark("items", checkTerms.toString());
	}

}
