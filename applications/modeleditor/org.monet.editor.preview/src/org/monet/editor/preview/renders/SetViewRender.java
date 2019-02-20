package org.monet.editor.preview.renders;

import java.util.ArrayList;
import java.util.HashMap;

import org.monet.metamodel.AttributeProperty;
import org.monet.metamodel.Definition;
import org.monet.metamodel.IndexDefinition;
import org.monet.metamodel.IndexDefinitionBase.IndexViewProperty;
import org.monet.metamodel.IndexDefinitionBase.IndexViewProperty.ShowProperty;
import org.monet.metamodel.NodeViewProperty;
import org.monet.metamodel.SetDefinition;
import org.monet.metamodel.SetDefinition.SetViewProperty;
import org.monet.metamodel.SetDefinitionBase.SetViewPropertyBase.ShowProperty.IndexProperty;
import org.monet.metamodel.SetDefinitionBase.SetViewPropertyBase.ShowProperty.LocationsProperty;
import org.monet.metamodel.internal.Ref;

public abstract class SetViewRender extends NodeViewRender {
  protected HashMap<String, HashMap<String, String>> nodes;
  
  private static enum RegionEnumeration {
    TITLE, PICTURE, ICON, HIGHLIGHT, LINE, LINE_BELOW, FOOTER;
  }

  public SetViewRender(String language) {
    super(language);
  }

  private String initRegion(IndexDefinition indexDefinition, ArrayList<Ref> showList, RegionEnumeration region, String template) {
    String result = "";
    
    for (Ref show : showList)
      result += this.initRegion(indexDefinition, show, region, template);
    
    return result;
  }

  private String initRegion(IndexDefinition indexDefinition, Ref show, RegionEnumeration region, String template) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    String result = "";
    
    if (show == null)
      return "";
    
    AttributeProperty attributeDefinition = indexDefinition.getAttribute(show.getValue());
    String regionValue;
    
    regionValue = region.toString().toLowerCase();
    
    map.put("code", attributeDefinition.getCode());
    map.put("label", language.getModelResource(attributeDefinition.getLabel(), this.codeLanguage));
    map.put("length", attributeDefinition.getCode() + "_length");
    map.put("imagesUrl", this.getParameterAsString("imagesPath"));
    
    if (existsBlock(template + "." + regionValue))
      result += block(template + "." + regionValue, map);
    else
      result += block(template, map);
    
    map.clear();
    
    return result;
  }
  
  private String initIndexTemplate(HashMap<String, Object> viewMap, SetViewProperty viewDefinition) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    SetDefinition definition = (SetDefinition)this.definition;
    IndexDefinition indexDefinition = dictionary.getIndexDefinition(definition.getIndex().getValue());
    
    if (viewDefinition.getShow().getIndex() == null)
      return "";
    
    IndexProperty showIndexDefinition = viewDefinition.getShow().getIndex();
    IndexViewProperty indexViewDefinition = showIndexDefinition.getWithView() != null ? indexDefinition.getIndexViewProperty(showIndexDefinition.getWithView().getValue()) : indexDefinition.getDefaultView();
    ShowProperty showDefinition = indexViewDefinition.getShow();

    map.put("blankImage", this.getParameterAsString("imagesPath") + "s.gif");
    map.put("code", "label");
    map.put("label", block("indexTemplate$label", map));
    
    map.put(RegionEnumeration.PICTURE.toString().toLowerCase(), this.initRegion(indexDefinition, showDefinition.getPicture(), RegionEnumeration.PICTURE, "indexTemplate$attribute"));
    map.put(RegionEnumeration.ICON.toString().toLowerCase(), this.initRegion(indexDefinition, showDefinition.getIcon(), RegionEnumeration.ICON, "indexTemplate$attribute"));
    map.put(RegionEnumeration.HIGHLIGHT.toString().toLowerCase(), this.initRegion(indexDefinition, showDefinition.getHighlight(), RegionEnumeration.HIGHLIGHT, "indexTemplate$attribute"));
    map.put(RegionEnumeration.LINE.toString().toLowerCase(), this.initRegion(indexDefinition, showDefinition.getLine(), RegionEnumeration.LINE, "indexTemplate$attribute"));
    map.put(RegionEnumeration.LINE_BELOW.toString().toLowerCase(), this.initRegion(indexDefinition, showDefinition.getLineBelow(), RegionEnumeration.LINE_BELOW, "indexTemplate$attribute"));
    map.put(RegionEnumeration.FOOTER.toString().toLowerCase(), this.initRegion(indexDefinition, showDefinition.getFooter(), RegionEnumeration.FOOTER, "indexTemplate$attribute"));

    return block("indexTemplate:client-side", map);
  }

  private String initSortByList(HashMap<String, Object> viewMap, SetViewProperty viewDefinition) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    SetDefinition definition = (SetDefinition)this.definition;
    IndexDefinition indexDefinition = dictionary.getIndexDefinition(definition.getIndex().getValue());
    String sortByList = "";

    sortByList += block("sortByList$item.default", map);
    map.clear();

    if (viewDefinition.getAnalyze() != null && viewDefinition.getAnalyze().getSorting() != null) {
      for (Ref sort : viewDefinition.getAnalyze().getSorting().getAttribute()) {
        String nameAttribute = sort.getValue();
        AttributeProperty attributeDefinition = indexDefinition.getAttribute(nameAttribute);

        map.put("code", attributeDefinition.getCode());
        map.put("label", language.getModelResource(attributeDefinition.getLabel(), this.codeLanguage));
        sortByList += block("sortByList$item", map);
        map.clear();
      }
    }

    return sortByList;
  }

  private String initGroupByListTypeItem(HashMap<String, Object> viewMap, SetViewProperty viewDefinition) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    String groupByListOptions = "";
    int position;

    position = 0;
    groupByListOptions = "";

    for (HashMap<String, String> nodeMap : this.nodes.values()) {
      map.put("position", String.valueOf(position));
      map.put("code", "code");
      map.put("optionCode", nodeMap.get("code"));
      String option = nodeMap.get("label");
      if (option != null)
        option = option.replaceAll("(\\n|\\t)", " ");
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
    HashMap<String, Object> map = new HashMap<String, Object>();
    String groupByList = "";
    SetDefinition definition = (SetDefinition)this.definition;
    IndexDefinition indexDefinition = dictionary.getIndexDefinition(definition.getIndex().getValue());

    groupByList += this.initGroupByListTypeItem(viewMap, viewDefinition);

    if (viewDefinition.getAnalyze() != null && viewDefinition.getAnalyze().getDimension() != null) {
      for (Ref group : viewDefinition.getAnalyze().getDimension().getAttribute()) {
        String nameAttribute = group.getValue();
        AttributeProperty attributeDefinition = indexDefinition.getAttribute(nameAttribute);

        map.put("code", attributeDefinition.getCode());
        map.put("label", language.getModelResource(attributeDefinition.getLabel(), this.codeLanguage));
        map.put("options", "");
        groupByList += block("groupByList$item", map);
        map.clear();
      }
    }

    return groupByList;
  }

  private void initContentView(HashMap<String, Object> viewMap, HashMap<String, Object> contentMap, SetViewProperty viewDefinition) {
    SetDefinition definition = (SetDefinition)this.definition;
    Definition indexDefinition = dictionary.getDefinition(definition.getIndex().getValue());

    contentMap.put("code", this.definition.getCode());
    contentMap.put("name", this.definition.getName());
    contentMap.put("codeReference", dictionary.getDefinition(definition.getIndex().getValue()).getCode());
    contentMap.put("codeView", viewDefinition.getCode());
    contentMap.put("page", this.getParameterAsString("page"));
    contentMap.put("labelReference", indexDefinition.getLabelString(this.codeLanguage).toLowerCase());
    contentMap.put("from", this.getParameterAsString("from"));
  }

  private String initContentViewIndex(HashMap<String, Object> viewMap, SetViewProperty viewDefinition) {
    HashMap<String, Object> map = new HashMap<String, Object>();

    this.initContentView(viewMap, map, viewDefinition);

    map.put("path", this.getParameterAsString("path"));
    map.put("indexTemplate", this.initIndexTemplate(map, viewDefinition));
    map.put("addList", this.initAddList(map, viewDefinition));
    map.put("sortByList", this.initSortByList(map, viewDefinition));
    map.put("groupByList", this.initGroupByList(map, viewDefinition));
    map.put("language", this.codeLanguage);

    return block("view.index", map);
  }
  
  private String initContentLayerMapInfoTemplate(HashMap<String, Object> viewMap, SetViewProperty viewDefinition) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    SetDefinition definition = (SetDefinition)this.definition;
    IndexDefinition indexDefinition = dictionary.getIndexDefinition(definition.getIndex().getValue());

    if (viewDefinition.getShow().getLocations() == null)
      return "";

    LocationsProperty locationsDefinition = viewDefinition.getShow().getLocations();
    IndexViewProperty indexViewDefinition = locationsDefinition.getWithView() != null ? indexDefinition.getIndexViewProperty(locationsDefinition.getWithView().getValue()) : indexDefinition.getDefaultView();
    ShowProperty showDefinition = indexViewDefinition.getShow();

    map.put("blankImage", "images/s.gif");
    if (showDefinition.getTitle() != null) {
      AttributeProperty attributeDefinition = indexDefinition.getAttribute(showDefinition.getTitle().getValue());
      map.put("code", attributeDefinition.getCode());
    }
    else map.put("code", "label");
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

    viewMap.put("clec", "clec");
    viewMap.put("codeDefinition", definition.getCode());
    viewMap.put("codeView", viewDefinition.getCode());
    viewMap.put("place", place != null ? place : "");
    viewMap.put("infoTemplate", this.initContentLayerMapInfoTemplate(viewMap, viewDefinition));

    return block("view.locationsMap", viewMap);
  }

/*
  private HashMap<String, Object> initContentViewReportList(HashMap<String, String> viewMap, SetViewProperty summaryViewDefinition, NodeViewProperty tabViewDefinition, EnableSummary enableSummary, String idNode, String codeView) {
    HashMap<String, String> map = new HashMap<String, String>();
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
      label = block("view.summary$view$list$definitionLabel", new HashMap<String, String>());
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
    return "";/*
    HashMap<String, String> map = new HashMap<String, String>();
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

    if (showDefinition.getIndex() != null)
      map.put("view", this.initContentViewIndex(viewMap, viewDefinition));
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

    contentMap.put("type", "prototypes");

    contentMap.put("addList", this.initAddList(map, setViewDefinition));
    contentMap.put("sortByList", this.initSortByList(map, setViewDefinition));
    contentMap.put("groupByList", this.initGroupByList(map, setViewDefinition));

    return block("content.prototypes", contentMap);
  }

  protected abstract ArrayList<String> getViewSelects(SetViewProperty view);

  protected abstract void fillNodesMap(SetViewProperty view);

  protected abstract String initMagnets(HashMap<String, Object> viewMap, SetViewProperty view);

  protected abstract String initAddList(HashMap<String, Object> viewMap, SetViewProperty view);

}
