package org.monet.space.office.presentation.user.renders;

import org.monet.metamodel.*;
import org.monet.metamodel.FieldPropertyBase.DisplayProperty;
import org.monet.metamodel.FieldPropertyBase.DisplayProperty.WhenEnumeration;
import org.monet.metamodel.internal.Ref;
import org.monet.space.kernel.library.LibraryHTML;
import org.monet.space.kernel.model.*;
import org.monet.space.office.configuration.Configuration;
import org.monet.space.office.presentation.user.constants.RenderParameter;
import org.monet.space.office.core.model.Language;

import java.util.*;

public abstract class FieldViewRender extends ViewRender {
	protected Field field;
	protected FieldProperty definition;
	protected org.monet.space.office.core.model.Language language;

	public FieldViewRender() {
		super();
		this.language = Language.getInstance();
	}

	@Override
	public void setTarget(Object target) {
		this.field = (Field) target;
		this.definition = this.field.getFieldDefinition();
	}

	@Override
	protected String initView(String codeView) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String id = this.getParameterAsString(RenderParameter.ID);
		Boolean isTemplate = (Boolean) this.getParameter(RenderParameter.IS_TEMPLATE);
		HashMap<String, Object> declarationsMap = new HashMap<String, Object>();

		if (id.isEmpty())
			id = this.field.getNode().getId();
		id = id + "." + this.definition.getCode();

		map.put("value", this.field.serializeToXML().toString().replaceAll("\"", "'"));
		map.put("id", id);

		boolean isCompositeItemView = codeView.equalsIgnoreCase("compositeitem");
		if (this.definition.isMultiple() && (!isCompositeItemView || (isCompositeItemView && definition.isSelect()) || (isCompositeItemView && definition.isPicture())))
			this.initMultipleField(id, map, declarationsMap, (isTemplate != null) ? isTemplate : false);
		else
			map.put("field", this.initSingleField(id, map, declarationsMap, this.field.getAttribute(), (isTemplate != null) ? isTemplate : false));

		this.initAttributes(map, id);
		this.initDeclarations(map, id, declarationsMap);
		this.initLabel(map, id, codeView);
		this.initInput(map, id);

		return block("view", map);
	}

	protected String getIndicatorValue(Attribute attribute, String code) {
		if (attribute == null)
			return "";
		return LibraryHTML.escape(attribute.getIndicatorValue(code));
	}

	protected boolean isTableView() {
		boolean isTableView = false;

		if (this.definition.isComposite()) {
			CompositeFieldProperty.ViewProperty view = ((CompositeFieldProperty) this.definition).getView();
			isTableView = (view != null && view.getMode() != null && view.getMode().equals(CompositeFieldProperty.ViewProperty.ModeEnumeration.COMPACT));
		}

		return isTableView;
	}

	protected void initAttributes(HashMap<String, Object> viewMap, String id/*, List<RuleDeclaration> rules*/) {
		viewMap.put("code", this.field.getCode());
		viewMap.put("idNode", this.field.getNode().getId());
		viewMap.put("type", this.getFieldType());
		viewMap.put("super", /* disabled isSuperField this.definition.isSuperfield() ? "super" : */"");
		viewMap.put("required", (this.definition.isRequired()) ? "required" : "");
		viewMap.put("extended", (this.definition.isExtended()) ? "extended" : "");
		viewMap.put("multiple", (this.definition.isMultiple()) ? "multiple" : "");
		viewMap.put("tableView", (this.isTableView()) ? "tableView" : "");
		viewMap.put("allowOthers", "false");

		String lock = "";
		if (this.field.getNode().isPrototype() && (this.definition.isUnivocal() || this.definition.isSerial()))
			lock = "lock";
		if (this.field.getNode().isPrototyped() && this.definition.isStatic())
			lock = "lock";
		if (this.definition.isReadonly())
			lock = "lock";
		viewMap.put("lock", lock);
	}

	protected void initLabel(HashMap<String, Object> viewMap, String id, String codeView) {
		HashMap<String, Object> map = new HashMap<String, Object>();

		String label = Language.getInstance().getModelResource(this.definition.getLabel());
		if (label == null || label.isEmpty()) {
			viewMap.put("label", block("label.empty", map));
			return;
		}

		map.put("label", label);

		viewMap.put("label", block("label", map));
	}

	private void initDeclarations(HashMap<String, Object> viewMap, String id, HashMap<String, Object> declarationsMap) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		//List<DefaultValue> defaultValueList = this.declaration.getDefaultValueList();

		String attributes = "";
	/*
    for (DefaultValue defaultValue : defaultValueList) {
      map.put("code", this.declaration.getCode());
      map.put("codeIndicator", defaultValue.getCode());
      map.put("checkedIndicator", String.valueOf(true));
      map.put("valueIndicator", defaultValue.getContent());
      attributes += block("declarations$default.single", map);
      map.clear();
    }*/

		if (this.definition.isMultiple()) {
			map.put("code", this.definition.getCode());
			map.put("attributes", attributes);
			declarationsMap.put("default", block("declarations$default.multiple", map));
		} else
			declarationsMap.put("default", attributes);

		declarationsMap.put("description", this.language.getModelResource(this.definition.getDescription()));

		declarationsMap.put("messageIfEmpty", "");
		declarationsMap.put("messageIfEditing", "");
		declarationsMap.put("messageIfRequired", "");
		for (DisplayProperty displayDefinition : this.definition.getDisplayList()) {
			HashMap<String, Object> localMap = new HashMap<String, Object>();
			localMap.put("message", this.language.getModelResource(displayDefinition.getMessage()));
			int i = 0; //TODO: Refactorizar los mensajes que aparecen en los campos
			if (displayDefinition.getWhen() == null)
				declarationsMap.put("messageIfEditing", block("declarations$messageIfEditing", localMap));
			else if (displayDefinition.getWhen() == WhenEnumeration.EMPTY)
				declarationsMap.put("messageIfEmpty", block("declarations$messageIfEmpty", localMap));
			else if (displayDefinition.getWhen() == WhenEnumeration.REQUIRED)
				declarationsMap.put("messageIfRequired", block("declarations$messageIfRequired", localMap));
			localMap.clear();
		}

		if (!declarationsMap.containsKey("concreteDeclarations"))
			declarationsMap.put("concreteDeclarations", "");

		viewMap.put("declarations", block("declarations", declarationsMap));
	}

	protected void initInput(HashMap<String, Object> viewMap, String id) {
		String isRootValue = this.getParameterAsString("isRoot");
		if (isRootValue.isEmpty())
			isRootValue = "true";
		viewMap.put("root", (Boolean.valueOf(isRootValue)) ? "root" : "");
	}

	protected String getMultipleFieldTableViewLabel(Attribute attribute, int pos) {
		String label = "";

		if (this.definition.isComposite()) {
			CompositeFieldProperty compositeDefinition = (CompositeFieldProperty) this.definition;
			CompositeFieldProperty.ViewProperty viewDefinition = compositeDefinition.getView();
			ArrayList<Ref> fieldRefList = viewDefinition.getSummary().getField();

			if (fieldRefList.size() > 0) {
				Ref firstFieldRef = fieldRefList.get(0);
				String codeField = compositeDefinition.getField(firstFieldRef.getValue()).getCode();

				if (attribute != null) {
					label = this.getIndicatorValue(attribute.getAttribute(codeField), Indicator.VALUE);
					if (label.equals(""))
						label = this.getIndicatorValue(attribute.getAttribute(codeField + "." + Attribute.OPTION), Indicator.VALUE);
				}
			}

			if (label.equals(""))
				label = block("field.multiple.table$tableView$emptyLabel", "position", String.valueOf(pos));

		} else if (this.definition.isNode()) {
			String idNode = this.getIndicatorValue(attribute, Indicator.CODE);
			if (!idNode.equals(""))
				label = this.renderLink.loadNode(idNode).getLabel();
			else
				label = block("field.multiple.table$tableView$emptyLabel", "position", String.valueOf(pos));
		} else {
			label = "";
		}

		return label;
	}

	protected String getMultipleFieldTableViewValue(Attribute attribute) {
		FieldProperty fieldDeclaration;
		FormDefinition formDefinition;

		if (attribute == null)
			return "";

		formDefinition = (FormDefinition) this.field.getNode().getDefinition();
		fieldDeclaration = formDefinition.getField(attribute.getCode());

		if (fieldDeclaration == null)
			return "";

		if (fieldDeclaration.isCheck()) {
			String result = "";
			for (Attribute checkAttribute : attribute.getAttributeList()) {
				if (!Boolean.valueOf(checkAttribute.getIndicatorValue(Indicator.CHECKED)))
					continue;
				result += checkAttribute.getIndicatorValue(Indicator.VALUE) + ",&nbsp;";
			}
			if (result.length() > 0)
				result = result.substring(0, result.length() - ",&nbsp;".length());
			return result;
		} else if (fieldDeclaration.isSelect()) {
			String result = "";
			Attribute optionAttribute = attribute.getAttribute(Attribute.OPTION);
			if (optionAttribute != null)
				result = optionAttribute.getIndicatorValue(Indicator.VALUE);
			return result;
		} else if (fieldDeclaration.isFile()) {
			String details = attribute.getIndicatorValue(Indicator.DETAILS);
			if (details != null && !details.isEmpty()) return details;
			return attribute.getIndicatorValue(Indicator.VALUE);
		} else
			return attribute.getIndicatorValue(Indicator.VALUE);
	}

	protected ArrayList<Integer> calculateMultipleFieldTableViewColumnWidths(ArrayList<Ref> fieldList) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		int size = fieldList.size();
		int width = Math.round(100 / (size + 1));

		for (int i = 0; i < size; i++) {
			if (i == 0)
				result.add(width * 2);
			else
				result.add(width);
		}

		return result;
	}

	protected String initMultipleFieldTableView(HashMap<String, Object> viewMap, HashMap<String, Object> declarationsMap) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		int rowPos = 0, columnPos = 0;
		Attribute fieldAttribute = this.field.getAttribute();
		AttributeList attributeList = (fieldAttribute != null) ? fieldAttribute.getAttributeList() : new AttributeList();
		String elements = "";
		String headerColumns = "", templateColumns = "";
		LinkedHashMap<Integer, LinkedHashMap<Integer, String>> rows = new LinkedHashMap<Integer, LinkedHashMap<Integer, String>>();
		FormDefinition definition = (FormDefinition) this.field.getNode().getDefinition();
		ArrayList<Ref> fieldList;
		ArrayList<Integer> columnWidths;
		Language language = Language.getInstance();

		if (this.definition.isComposite()) {
			CompositeFieldProperty.ViewProperty viewDefinition = ((CompositeFieldProperty) this.definition).getView();

			fieldList = viewDefinition.getSummary().getField();
			columnWidths = this.calculateMultipleFieldTableViewColumnWidths(fieldList);
			for (Ref fieldRef : fieldList) {
				FieldProperty field = definition.getField(fieldRef.getValue());
				map.put("code", field.getCode());
				map.put("label", language.getModelResource(field.getLabel()));
				map.put("width", String.valueOf(columnWidths.get(columnPos)));
				map.put("link", (columnPos == 0) ? "link" : "");
				headerColumns += block("field.multiple.table$tableView$headerColumn", map);
				templateColumns += block("field.multiple.table$tableView$templateColumn", map);

				rowPos = 0;
				for (Attribute attribute : attributeList.get().values()) {
					LinkedHashMap<Integer, String> row = rows.get(rowPos);
					String value;

					if (row == null) {
						row = new LinkedHashMap<Integer, String>();
						rows.put(rowPos, row);
					}

					if (columnPos == 0)
						value = this.getMultipleFieldTableViewLabel(attribute, 0);
					else
						value = this.getMultipleFieldTableViewValue(attribute.getAttribute(field.getCode()));

					if (value.isEmpty())
						value = "&nbsp;";

					map.put("code", field.getCode());
					map.put("value", value);
					map.put("width", String.valueOf(columnWidths.get(columnPos)));
					map.put("link", (columnPos == 0) ? "link" : "");
					row.put(columnPos, block("field.multiple.table$tableView$element$column", map));
					map.clear();
					rowPos++;
				}

				columnPos++;
			}

			rowPos = 0;
			for (HashMap<Integer, String> row : rows.values()) {
				String columns = "";

				for (String value : row.values())
					columns += value;

				map.put("id", this.field.getNode().getId());
				map.put("code", this.field.getCode());
				map.put("position", String.valueOf(rowPos));
				map.put("columns", columns);
				elements += block("field.multiple.table$tableView$element", map);

				rowPos++;
			}

		} else if (this.definition.isNode()) {
			// TODO
		}

		map.put("headerColumns", headerColumns);
		map.put("templateColumns", templateColumns);
		map.put("elements", elements);
		map.put("themeSource", Configuration.getInstance().getApiUrl() + "?op=loadthemefile");

		return block("field.multiple.table$tableView", map);
	}

	protected void initMultipleField(String id, HashMap<String, Object> viewMap, HashMap<String, Object> declarationsMap, boolean isTemplate) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		boolean isComposite = this.definition.isComposite();
		String elements = "";
		int pos = 0;
		Attribute fieldAttribute = this.field.getAttribute();
		AttributeList attributeList = (fieldAttribute != null) ? fieldAttribute.getAttributeList() : new AttributeList();
		boolean isTableView = this.isTableView();
		String emptyElement = "";

		if (isTableView) {
			Iterator<Attribute> iterator = attributeList.get().values().iterator();
			Attribute attribute = iterator.hasNext() ? iterator.next() : null;
			map.put("label", this.getMultipleFieldTableViewLabel(attribute, pos));
			map.put("fieldSingle", this.initSingleField(id + ".0", viewMap, declarationsMap, attribute, isTemplate));
			elements += block("field.multiple.table$element", map);
			map.clear();
		} else {
			MonetHashMap<Attribute> attributes = attributeList.get();

			if (attributes.size() <= 0) {
				map.put("label", "");
				map.put("fieldSingle", this.initSingleField(id + ".empty", viewMap, declarationsMap, new Attribute(this.definition), isTemplate));
				emptyElement = block("field.multiple$element", map);
			}

			for (Attribute attribute : attributes.values()) {
				map.put("label", "");
				map.put("fieldSingle", this.initSingleField(id + "." + pos, viewMap, declarationsMap, attribute, isTemplate));
				elements += block("field.multiple$element", map);
				map.clear();
				pos++;
			}
		}

		map.put("id", id);
		map.put("extended", (isComposite) ? this.getIndicatorValue(this.field.getAttribute(), "extended") : "");
		map.put("conditioned", (isComposite) ? this.getIndicatorValue(this.field.getAttribute(), "conditioned") : "");
		map.put("emptyElement", emptyElement);
		map.put("elements", elements);
		map.put("template", this.initSingleField(id, viewMap, declarationsMap, this.field.getAttribute(), true));
		map.put("themeSource", Configuration.getInstance().getApiUrl() + "?op=loadthemefile");
		map.put("tableView", "");
		if (isTableView)
			map.put("tableView", this.initMultipleFieldTableView(viewMap, declarationsMap));

		viewMap.put("field", isTableView ? block("field.multiple.table", map) : block("field.multiple", map));
	}

	protected String initSingleField(String id, HashMap<String, Object> viewMap, HashMap<String, Object> declarationsMap, Attribute attribute, boolean isTemplate) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		boolean isComposite = this.definition.isComposite();
		CompositeFieldProperty compositeDefinition = isComposite ? (CompositeFieldProperty) this.definition : null;
		String conditioned = (isComposite) ? this.getIndicatorValue(this.field.getAttribute(), "conditioned") : "";
		boolean compositeHidden = false;

		if (isComposite && compositeDefinition.isConditional())
			compositeHidden = conditioned.equals("conditioned") ? false : true;

		map.put("super", /* disabled isSuperField this.definition.isSuperfield() ? this.getIndicatorValue(this.field.getAttribute(), Indicator.SUPER) : */"");
		map.put("extended", (isComposite) ? this.getIndicatorValue(this.field.getAttribute(), "extended") : "");
		map.put("compositeHidden", compositeHidden ? "yes" : "");
		map.put("conditioned", conditioned);
		map.put("type", this.getFieldType());
		map.put("memo", (this.definition.isMemo()) ? "memo" : "");
		map.put("body", this.initBody(id, viewMap, declarationsMap, attribute, isTemplate));

		return block("field.single", map);
	}

	protected String getSourceId(Attribute attribute, String sourceKey) {
		String sourceId = this.getIndicatorValue(attribute, Indicator.SOURCE);

		if ((sourceId == null || sourceId.isEmpty()) && sourceKey != null) {
			String codeSource = this.dictionary.getDefinitionCode(sourceKey);
			Source<SourceDefinition> source = this.renderLink.locateSource(codeSource, null);
			if (source != null) sourceId = source.getId();
		}

		return sourceId;
	}

	protected LinkedHashSet<String> getFieldFilters(FieldProperty fieldDefinition, boolean mask) {
		LinkedHashSet<String> filtersSet = new LinkedHashSet<String>();
		ArrayList<Object> tagList = null;

		if (fieldDefinition.isCheck()) {
			CheckFieldProperty.SelectProperty selectDefinition = ((CheckFieldProperty) fieldDefinition).getSelect();
			if (selectDefinition != null && selectDefinition.getFilter() != null)
				tagList = selectDefinition.getFilter().getTag();
		} else if (fieldDefinition.isSelect()) {
			SelectFieldProperty.SelectProperty selectDefinition = ((SelectFieldProperty) fieldDefinition).getSelect();
			if (selectDefinition != null && selectDefinition.getFilter() != null)
				tagList = selectDefinition.getFilter().getTag();
		}

		if (tagList == null)
			return filtersSet;

		for (Object tag : tagList) {
			String filterValue = "";

			if (tag instanceof String)
				filterValue = (String) tag;
			else if (tag instanceof Ref) {
				String fieldName = ((Ref) tag).getValue();
				Node node = this.field.getNode();
				filterValue = (mask) ? "_field:" + ((FormDefinition) node.getDefinition()).getField(fieldName).getCode() : node.getFieldValue(fieldName);
			}

			if (filterValue.isEmpty()) continue;

			filtersSet.add(filterValue);
		}

		return filtersSet;
	}

	protected LinkedHashSet<String> getFieldFiltersMask(FieldProperty fieldDefinition) {
		return this.getFieldFilters(fieldDefinition, true);
	}

	@Override
	protected void init() {
		loadCanvas("view.field");
		super.init();
	}

	protected abstract String getFieldType();

	protected abstract String initBody(String id, HashMap<String, Object> viewMap, HashMap<String, Object> declarationsMap, Attribute attribute, boolean isTemplate);

}
