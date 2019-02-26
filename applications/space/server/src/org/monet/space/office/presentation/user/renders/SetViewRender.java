package org.monet.space.office.presentation.user.renders;

import org.monet.metamodel.*;
import org.monet.metamodel.IndexDefinitionBase.IndexViewProperty;
import org.monet.metamodel.IndexDefinitionBase.IndexViewProperty.ShowProperty;
import org.monet.metamodel.SetDefinition.SetViewProperty;
import org.monet.metamodel.SetDefinitionBase.SetViewPropertyBase.ShowProperty.IndexProperty;
import org.monet.metamodel.SetDefinitionBase.SetViewPropertyBase.ShowProperty.LocationsProperty;
import org.monet.metamodel.internal.DescriptorDefinition;
import org.monet.metamodel.internal.Ref;
import org.monet.space.kernel.model.Node;
import org.monet.space.mobile.model.Language;
import org.monet.space.office.configuration.Configuration;

import java.util.*;

public abstract class SetViewRender extends NodeViewRender {
	protected SetDefinition definition;

	private static enum RegionEnumeration {
		TITLE, PICTURE, ICON, HIGHLIGHT, LINE, LINE_BELOW, FOOTER;
	}

	public SetViewRender() {
		super();
	}

	@Override
	public void setTarget(Object target) {
		this.node = (Node) target;
		this.definition = (SetDefinition) this.node.getDefinition();
	}

	private String initRegion(IndexDefinition indexDefinition, ArrayList<Ref> showList, RegionEnumeration region, String template) {
		String result = "";

		for (Ref show : showList)
			result += this.initRegion(indexDefinition, show, region, template);

		return result;
	}

	private String initRegion(IndexDefinition indexDefinition, Ref show, RegionEnumeration region, String template) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		Language language = Language.getInstance();
		Configuration configuration = Configuration.getInstance();
		String result = "";

		if (show == null)
			return "";

		AttributeProperty attributeDefinition = indexDefinition.getAttribute(show.getValue());
		String regionValue;

		regionValue = region.toString().toLowerCase();

		map.put("code", attributeDefinition.getCode());
		map.put("label", language.getModelResource(attributeDefinition.getLabel()));
		map.put("length", attributeDefinition.getCode() + "_length");
		map.put("imagesUrl", configuration.getFmsServletUrl() + "?op=downloadimage");

		if (existsBlock(template + "." + attributeDefinition.getType().toString().toLowerCase() + "." + regionValue))
			result += block(template + "." + regionValue, map);
		else if (existsBlock(template + "." + regionValue))
			result += block(template + "." + regionValue, map);
		else if (existsBlock(template + "." + attributeDefinition.getType().toString().toLowerCase()))
			result += block(template + "." + attributeDefinition.getType().toString().toLowerCase(), map);
		else
			result += block(template, map);

		map.clear();

		return result;
	}

	private String initIndexTemplate(HashMap<String, Object> viewMap, SetViewProperty viewDefinition) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		IndexDefinition indexDefinition = this.dictionary.getIndexDefinition(this.definition.getIndex().getValue());
		Configuration configuration = Configuration.getInstance();

		if (viewDefinition.getShow().getIndex() == null)
			return "";

		IndexProperty showIndexDefinition = viewDefinition.getShow().getIndex();
		IndexViewProperty indexViewDefinition = showIndexDefinition.getWithView() != null ? indexDefinition.getView(showIndexDefinition.getWithView().getValue()) : indexDefinition.getDefaultView();
		ShowProperty showDefinition = indexViewDefinition.getShow();

		map.put("blankImage", configuration.getUrl() + "/images/s.gif");
		if (showDefinition.getTitle() != null) {
			AttributeProperty attributeDefinition = indexDefinition.getAttribute(showDefinition.getTitle().getValue());
			map.put("code", attributeDefinition.getCode());
		} else map.put("code", "label");
		map.put("label", block("indexTemplate$label", map));

		map.put(RegionEnumeration.PICTURE.toString().toLowerCase(), this.initRegion(indexDefinition, showDefinition.getPicture(), RegionEnumeration.PICTURE, "indexTemplate$attribute"));
		map.put(RegionEnumeration.ICON.toString().toLowerCase(), this.initRegion(indexDefinition, showDefinition.getIcon(), RegionEnumeration.ICON, "indexTemplate$attribute"));
		map.put(RegionEnumeration.HIGHLIGHT.toString().toLowerCase(), this.initRegion(indexDefinition, showDefinition.getHighlight(), RegionEnumeration.HIGHLIGHT, "indexTemplate$attribute"));
		map.put(RegionEnumeration.LINE.toString().toLowerCase(), this.initRegion(indexDefinition, showDefinition.getLine(), RegionEnumeration.LINE, "indexTemplate$attribute"));
		map.put(RegionEnumeration.LINE_BELOW.toString().toLowerCase(), this.initRegion(indexDefinition, showDefinition.getLineBelow(), RegionEnumeration.LINE_BELOW, "indexTemplate$attribute"));
		map.put(RegionEnumeration.FOOTER.toString().toLowerCase(), this.initRegion(indexDefinition, showDefinition.getFooter(), RegionEnumeration.FOOTER, "indexTemplate$attribute"));

		return block("indexTemplate:client-side", map);
	}

	private String initItemsTemplate(HashMap<String, Object> viewMap, SetViewProperty viewDefinition) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		Configuration configuration = Configuration.getInstance();

		if (viewDefinition.getShow().getItems() == null)
			return "";

		map.put("blankImage", configuration.getUrl() + "/images/s.gif");
		map.put("code", "label");
		map.put("label", block("indexTemplate$label", map));

		map.put(RegionEnumeration.PICTURE.toString().toLowerCase(), "");
		map.put(RegionEnumeration.ICON.toString().toLowerCase(), "");
		map.put(RegionEnumeration.HIGHLIGHT.toString().toLowerCase(), "");
		map.put(RegionEnumeration.LINE.toString().toLowerCase(), "");
		map.put(RegionEnumeration.LINE_BELOW.toString().toLowerCase(), "");
		map.put(RegionEnumeration.FOOTER.toString().toLowerCase(), "");

		return block("indexTemplate:client-side", map);
	}

	private String initSortByList(HashMap<String, Object> viewMap, SetViewProperty viewDefinition) {

		if (this.definition.getIndex() == null)
			return "";

		IndexDefinition indexDefinition = this.dictionary.getIndexDefinition(this.definition.getIndex().getValue());
		HashMap<String, Object> map = new HashMap<String, Object>();
		String sortByList = block("sortByList$items.default", map);
		Language language = Language.getInstance();
		String defaultSortCode = block("sortByList$item.default.code", map);
		String defaultSortMode = block("sortByList$item.default.mode", map);

		if (viewDefinition.getShow() != null && viewDefinition.getShow().getIndex() != null
				&& viewDefinition.getShow().getIndex().getSortBy() != null)
			defaultSortCode = indexDefinition.getAttribute(viewDefinition.getShow().getIndex().getSortBy().getValue()).getCode();

		if (viewDefinition.getShow() != null && viewDefinition.getShow().getIndex() != null
				&& viewDefinition.getShow().getIndex().getSortMode() != null)
			defaultSortMode = block(String.format("sortByList$item.mode.%s", viewDefinition.getShow().getIndex().getSortMode().toString().toLowerCase()), map);

		viewMap.put("defaultSortBy", defaultSortCode);
		viewMap.put("defaultSortMode", defaultSortMode);

		if (viewDefinition.getAnalyze() != null && viewDefinition.getAnalyze().getSorting() != null) {
			for (Ref sort : viewDefinition.getAnalyze().getSorting().getAttribute()) {
				String nameAttribute = sort.getValue();
				AttributeProperty attributeDefinition = indexDefinition.getAttribute(nameAttribute);
				String codeAttribute = attributeDefinition.getCode();
				String mode = block("sortByList$item.default.mode", map);

				if (codeAttribute.equals(defaultSortCode))
					mode = defaultSortMode;

				map.put("code", codeAttribute);
				map.put("label", language.getModelResource(attributeDefinition.getLabel()));
				map.put("mode", mode);
				sortByList += block("sortByList$item", map);
				map.clear();
			}
		}

		return sortByList;
	}

	private String initGroupByListTypeItem(HashMap<String, Object> viewMap, SetViewProperty viewDefinition) {
		Map<String, Map<String, Object>> nodesMap = nodesMap(viewDefinition);
		HashMap<String, Object> map = new HashMap<String, Object>();
		String groupByListOptions = "";
		int position;

		position = 0;
		groupByListOptions = "";

		if (nodesMap.size() <= 1)
			return "";

		for (Map<String, Object> nodeMap : nodesMap.values()) {
			map.put("position", String.valueOf(position));
			map.put("code", "code");
			map.put("optionCode", nodeMap.get("code"));
			String option = nodeMap.get("label").toString();
			if (option != null)
				option = option.replaceAll("(\\n|\\t)", " ").replaceAll("\'", "\\\\\'");
			map.put("optionLabel", option);
			position++;
			groupByListOptions += block("groupByList$item$option", map);
		}

		map.put("code", "code");
		map.put("label", block("groupByList$item$typeLabel", map));
		map.put("options", groupByListOptions);

		return block("groupByList$item", map);
	}

	private String initGroupByList(HashMap<String, Object> viewMap, SetViewProperty viewDefinition) {

		if (this.definition.getIndex() == null)
			return "";

		HashMap<String, Object> map = new HashMap<String, Object>();
		String groupByListOptions = "";
		String groupByList = this.initGroupByListTypeItem(viewMap, viewDefinition);
		IndexDefinition indexDefinition = this.dictionary.getIndexDefinition(this.definition.getIndex().getValue());
		Language language = Language.getInstance();

		if (viewDefinition.getAnalyze() != null && viewDefinition.getAnalyze().getDimension() != null) {
			ArrayList<String> nodes = new ArrayList<>(nodesMap(viewDefinition).keySet());

			for (Ref group : viewDefinition.getAnalyze().getDimension().getAttribute()) {
				String nameAttribute = group.getValue();
				AttributeProperty attributeDefinition = indexDefinition.getAttribute(nameAttribute);
				String attributeCode = attributeDefinition.getCode();

//				Map<String, Integer> countMap = this.node.getGroupOptionsCount(attributeDefinition.getCode(), nodes, viewDefinition.getFilterList());
//				int count = countMap.size();
//				if (count > 1) {
					groupByListOptions = /*count<50?this.initGroupByOptions(attributeDefinition.getCode(), viewDefinition):*/"";

					map.put("code", attributeDefinition.getCode());
					map.put("label", language.getModelResource(attributeDefinition.getLabel()));
					map.put("options", groupByListOptions);
					groupByList += block("groupByList$item", map);

					map.clear();
//				}
			}
		}

		return groupByList;
	}

	protected String initGroupByOptions(String groupByCode, SetViewProperty viewDefinition) {

		if (this.definition.getIndex() == null)
			return "";

		HashMap<String, Object> map = new HashMap<>();
		IndexDefinition indexDefinition = this.dictionary.getIndexDefinition(this.definition.getIndex().getValue());
		AttributeProperty attributeDefinition = indexDefinition.getAttribute(groupByCode);
		List<String> optionsList;

		optionsList = this.node.getGroupOptions(attributeDefinition.getCode(), new ArrayList<>(nodesMap(viewDefinition).keySet()), viewDefinition.getFilterList());
		int position = 0;
		String groupByListOptions = "";
        boolean standAlone = this.getParameter("standalone")!=null?(Boolean)this.getParameter("standalone"):false;
        String blockName = standAlone?"groupByList$item$option.standalone":"groupByList$item$option";

		for (String option : optionsList) {
			map.put("position", String.valueOf(position));
			map.put("code", attributeDefinition.getCode());
			if (option != null)
				option = option.replaceAll("(\\n|\\t)", " ").replaceAll("\'", "\\\\\'");
			map.put("optionCode", option);
			map.put("optionLabel", option);
			position++;
			groupByListOptions += block(blockName, map);
			map.clear();
		}

		return groupByListOptions;
	}

	private void initContentView(HashMap<String, Object> viewMap, HashMap<String, Object> contentMap, SetViewProperty viewDefinition) {
		Definition indexDefinition = null;
		String codeReference = DescriptorDefinition.CODE;

		if (definition.getIndex() != null) {
			indexDefinition = this.dictionary.getDefinition(definition.getIndex().getValue());
			codeReference = indexDefinition.getCode();
		}

		contentMap.put("id", this.node.getId());
		contentMap.put("ancestorsIds", this.node.getAncestorsIds(","));
		contentMap.put("code", this.node.getCode());
		contentMap.put("codeReference", codeReference);
		contentMap.put("codeView", viewDefinition.getCode());
		contentMap.put("page", this.getParameterAsString("page"));
		contentMap.put("labelReference", indexDefinition != null ? indexDefinition.getLabelString().toLowerCase() : block("view.index$defaultLabelReference", new HashMap<String, Object>()));
		contentMap.put("from", this.getParameterAsString("from"));
	}

	private String initContentViewIndex(HashMap<String, Object> viewMap, SetViewProperty viewDefinition) {
		HashMap<String, Object> map = new HashMap<String, Object>();

		this.initContentView(viewMap, map, viewDefinition);

		map.put("indexTemplate", this.initIndexTemplate(map, viewDefinition));
		map.put("addList", this.initAddList(map, viewDefinition));
		map.put("sortByList", this.initSortByList(map, viewDefinition));
		map.put("groupByList", this.initGroupByList(map, viewDefinition));

		return block("view.index", map);
	}

	private String initContentViewItems(HashMap<String, Object> viewMap, SetViewProperty viewDefinition) {
		HashMap<String, Object> map = new HashMap<String, Object>();

		this.initContentView(viewMap, map, viewDefinition);

		map.put("indexTemplate", this.initItemsTemplate(map, viewDefinition));
		map.put("addList", this.initAddList(map, viewDefinition));
		map.put("sortByList", this.initSortByList(map, viewDefinition));
		map.put("groupByList", this.initGroupByList(map, viewDefinition));

		return block("view.index", map);
	}

	private String initContentLayerMapInfoTemplate(HashMap<String, Object> viewMap, SetViewProperty viewDefinition) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		IndexDefinition indexDefinition = this.dictionary.getIndexDefinition(this.definition.getIndex().getValue());
		Configuration configuration = Configuration.getInstance();

		if (indexDefinition == null)
			return "";

		if (viewDefinition.getShow().getLocations() == null)
			return "";

		LocationsProperty locationsDefinition = viewDefinition.getShow().getLocations();
		IndexViewProperty indexViewDefinition = locationsDefinition.getWithView() != null ? indexDefinition.getView(locationsDefinition.getWithView().getValue()) : indexDefinition.getDefaultView();
		ShowProperty showDefinition = indexViewDefinition.getShow();

		map.put("blankImage", configuration.getUrl() + "/images/s.gif");
		if (showDefinition.getTitle() != null) {
			AttributeProperty attributeDefinition = indexDefinition.getAttribute(showDefinition.getTitle().getValue());
			map.put("code", attributeDefinition.getCode());
		} else map.put("code", "label");
		map.put("label", block("indexTemplate$label", map));

		map.put(RegionEnumeration.PICTURE.toString().toLowerCase(), this.initRegion(indexDefinition, showDefinition.getPicture(), RegionEnumeration.PICTURE, "view.locationsMap$infoTemplate$attribute"));
		map.put(RegionEnumeration.ICON.toString().toLowerCase(), this.initRegion(indexDefinition, showDefinition.getIcon(), RegionEnumeration.ICON, "view.locationsMap$infoTemplate$attribute"));
		map.put(RegionEnumeration.HIGHLIGHT.toString().toLowerCase(), this.initRegion(indexDefinition, showDefinition.getHighlight(), RegionEnumeration.HIGHLIGHT, "view.locationsMap$infoTemplate$attribute"));
		map.put(RegionEnumeration.LINE.toString().toLowerCase(), this.initRegion(indexDefinition, showDefinition.getLine(), RegionEnumeration.LINE, "view.locationsMap$infoTemplate$attribute"));
		map.put(RegionEnumeration.LINE_BELOW.toString().toLowerCase(), this.initRegion(indexDefinition, showDefinition.getLineBelow(), RegionEnumeration.LINE_BELOW, "view.locationsMap$infoTemplate$attribute"));
		map.put(RegionEnumeration.FOOTER.toString().toLowerCase(), this.initRegion(indexDefinition, showDefinition.getFooter(), RegionEnumeration.FOOTER, "view.locationsMap$infoTemplate$attribute"));

		return block("view.locationsMap$infoTemplate:client-side", map);
	}

	private String initContentViewLocationsMap(HashMap<String, Object> viewMap, SetViewProperty viewDefinition) {
		//NodeDefinition definition = this.node.getDefinition();
		//String place = viewDefinition.getPlace();
		String place = null; // TODO mario. Pendiente de decidir si el lugar es un parametro que viene del espacio o del xml del modelo

		this.initContentView(viewMap, viewMap, viewDefinition);

		viewMap.put("clec", "clec");
		viewMap.put("idNode", this.node.getId());
		viewMap.put("codeDefinition", definition.getCode());
		viewMap.put("codeView", viewDefinition.getCode());
		viewMap.put("place", place != null ? place : "");
		viewMap.put("infoTemplate", this.initContentLayerMapInfoTemplate(viewMap, viewDefinition));

		LocationsProperty locationsDefinition = viewDefinition.getShow().getLocations();
		if (locationsDefinition != null && locationsDefinition.getLayer() != null)
			viewMap.put("layer", locationsDefinition.getLayer().toString().toLowerCase());

		viewMap.put("groupByList", this.initGroupByList(viewMap, viewDefinition));

		return block("view.locationsMap", viewMap);
	}

	/*
	  private HashMap<String, Object> initContentViewReportList(HashMap<String, Object> viewMap, SetViewProperty summaryViewDefinition, NodeViewProperty tabViewDefinition, EnableSummary enableSummary, String idNode, String codeView) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		HashMap<String, Object> result = new HashMap<String, Object>();
		String codeReference, codeAttribute;
		IndexDefinition indexDefinition;
		Boolean isDrillByDefinition;
		String itemsValue = "";
		HashMap<String, Integer> items;
		Integer count = 0;
		String label = "";
		SetViewProperty setViewDefinition = (SetViewProperty) tabViewDefinition;

		if (enableSummary.getMode() == ModeEnumeration.DRILL_BY_DEFINITION) {
		  indexDefinition = new DescriptorDefinition();
		  codeReference = indexDefinition.getCode();
		  codeAttribute = DescriptorDefinition.ATTRIBUTE_CODE;
		  isDrillByDefinition = true;
		} else {
		  indexDefinition = this.dictionary.getIndexDefinition(this.definition.getIndex().getValue());
		  codeReference = indexDefinition.getCode();
		  codeAttribute = indexDefinition.getAttribute(enableSummary.getDrillAttribute().getValue()).getCode();
		  isDrillByDefinition = false;
		}

		items = this.renderLink.loadReferenceAttributeValuesCount(this.node.getId(), codeReference, codeAttribute, this.getViewSelects(setViewDefinition), setViewDefinition.getToolsDeclaration().getFilterList());
		for (Map.Entry<String, Integer> item : items.entrySet()) {
		  map.put("idMainNode", idNode);
		  map.put("idNode", this.node.getId());
		  map.put("codeView", codeView);
		  map.put("code", codeAttribute);
		  map.put("value", item.getKey());
		  map.put("label", (isDrillByDefinition) ? LibraryString.cleanSpecialChars(this.dictionary.getDefinition(item.getKey()).getLabelString()) : item.getKey());
		  map.put("count", String.valueOf(item.getValue()));
		  count += item.getValue();
		  itemsValue += block("view.summary$view$list$item", map);
		  map.clear();
		}

		if (items.size() == 0)
		  itemsValue = block("view.summary$view$list$noItems", map);

		if (indexDefinition instanceof DescriptorDefinition)
		  label = block("view.summary$view$list$definitionLabel", new HashMap<String, Object>());
		else
		  label = LibraryString.cleanSpecialChars(Language.getInstance().getModelResource(indexDefinition.getAttribute(codeAttribute).getLabel()));

		map.put("label", label);
		map.put("items", itemsValue);

		result.put("content", block("view.summary$view$list", map));
		result.put("count", count);

		return result;
	  }
	*/
	private String initContentViewReport(HashMap<String, Object> viewMap, SetViewProperty viewDefinition) {
		return "PENDIENTE DE REHACER";/*
	HashMap<String, Object> map = new HashMap<String, Object>();
    String viewsValue = "";
    List<NodeViewProperty> viewDefinitions;
    boolean isComponent = this.definition.isComponent();
    Node mainNode = null;
    Language language = Language.getInstance();

    this.initContentView(viewMap, map, viewDefinition);

    if (isComponent) {
      mainNode = this.node.getMainNode();
      viewDefinitions = mainNode.getDefinition().getTabViewList();
    } else
      viewDefinitions = this.definition.getTabViewList();

    for (NodeViewProperty tabViewDefinition : viewDefinitions) {
      String summaryListValue = "";
      HashMap<String, Object> summaryList;
      Integer count = 0;
      String codeView = tabViewDefinition.getCode();
      String idNode = this.node.getId();
      String labelView = language.getModelResource(tabViewDefinition.getLabel());
      boolean found = false;

      if (isComponent) {
        if (mainNode.isContainer()) {
          List<Ref> showList = ((ContainerViewProperty) tabViewDefinition).getShowWidget();
          for (Ref show : showList) {
            if (show.getDefinition().equals(this.definition.getName())) {
              codeView = tabViewDefinition.getCode();
              idNode = mainNode.getId();
              tabViewDefinition = this.definition.getNodeViewProperty(show.getValue());
              found = true;
              break;
            }
          }
        }
      } else
        found = true;

      if (!found)
        continue;

      SetViewProperty setViewDefinition = (SetViewProperty) tabViewDefinition;
      List<EnableSummary> enableSummaryList = setViewDefinition.getEnableSummaryList();

      if (enableSummaryList.size() <= 0)
        continue;

      for (EnableSummary enableSummary : enableSummaryList) {
        summaryList = this.initContentViewReportList(viewMap, viewDefinition, tabViewDefinition, enableSummary, idNode, codeView);
        summaryListValue += (String) summaryList.get("content");
        if (count == 0)
          count = (Integer) summaryList.get("count");
      }

      if (count > 0) {
        map.put("idMainNode", idNode);
        map.put("idNode", this.node.getId());
        map.put("code", codeView);
        map.put("label", labelView);
        map.put("count", String.valueOf(count));
        map.put("list", summaryListValue);
        viewsValue += block("view.summary$view", map);
        map.clear();
      }
    }

    map.put("views", viewsValue);

    return block("view.summary", map);*/
	}

	protected void initContent(HashMap<String, Object> viewMap, SetViewProperty viewDefinition) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		SetDefinition.SetViewProperty.ShowProperty showDefinition = viewDefinition.getShow();

		viewMap.put("defaultSortBy", block("sortByList$item.default.code", map));

		if (showDefinition.getIndex() != null)
			map.put("view", this.initContentViewIndex(viewMap, viewDefinition));
		else if (showDefinition.getItems() != null)
			map.put("view", this.initContentViewItems(viewMap, viewDefinition));
		else if (showDefinition.getLocations() != null)
			map.put("view", this.initContentViewLocationsMap(viewMap, viewDefinition));
		else if (showDefinition.getReport() != null)
			map.put("view", this.initContentViewReport(viewMap, viewDefinition));

		map.put("magnets", this.initMagnets(viewMap, viewDefinition));

		viewMap.put("content", block("content", map));
	}

	@Override
	protected String initPrototypesSystemView(NodeViewProperty viewDefinition, HashMap<String, Object> contentMap) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		SetViewProperty setViewDefinition = (SetViewProperty) viewDefinition;
		SetViewProperty.ShowProperty showDefinition = setViewDefinition.getShow();
		String type = "";

		if (showDefinition.getOwnedPrototypes() != null)
			type = "ownedprototypes";
		else
			type = "sharedprototypes";

		contentMap.put("type", type);
		contentMap.put("addList", this.initAddList(map, setViewDefinition));
		contentMap.put("sortByList", this.initSortByList(map, setViewDefinition));
		contentMap.put("groupByList", this.initGroupByList(map, setViewDefinition));

		return block("content.prototypes", contentMap);
	}

	protected abstract ArrayList<String> getViewSelects(SetViewProperty view);

	protected Map<String, Map<String, Object>> nodesMap(SetViewProperty view) {
		if (view.getSelect() == null)
			return Collections.emptyMap();

		ArrayList<Ref> selectList = view.getSelect().getNode();

		if (selectList.size() == 0)
			return Collections.emptyMap();

		Map<String, Map<String, Object>> result = new HashMap<>();
		for (Ref select : selectList) {
			Definition definition = this.dictionary.getDefinition(select.getValue());
			result.put(definition.getCode(), nodeMapOf(definition));
		}

		return result;
	}

	protected abstract String initMagnets(HashMap<String, Object> viewMap, SetViewProperty view);

	protected abstract String initAddList(HashMap<String, Object> viewMap, SetViewProperty view);

	Map<String, Map<String, Object>> nodeMapOf(ArrayList<Ref> refList) {
		Map<String, Map<String, Object>> result = new HashMap<>();
		for (Ref enable : refList) {
			for (Definition child : this.dictionary.getAllImplementersOfNodeDefinition(enable.getValue())) {
				result.put(child.getCode(), nodeMapOf(definition));
			}
		}
		return result;
	}

	HashMap<String, Object> nodeMapOf(Definition definition) {
		if (definition.isDisabled()) return null;
		HashMap<String, Object> nodeMap = new HashMap<String, Object>();
		nodeMap.put("code", definition.getCode());
		nodeMap.put("label", definition.getLabelString());
		nodeMap.put("description", definition.getDescription());
		return nodeMap;
	}

}
