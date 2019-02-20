package org.monet.editor.preview.renders;

import java.util.ArrayList;
import java.util.HashMap;

import org.monet.metamodel.Definition;
import org.monet.metamodel.SetDefinition.SetViewProperty;
import org.monet.metamodel.internal.Ref;

public class CatalogViewRender extends SetViewRender {

  public CatalogViewRender(String language) {
    super(language);
  }

  @Override
  protected ArrayList<String> getViewSelects(SetViewProperty viewDefinition) {
    ArrayList<Ref> selectList = viewDefinition.getSelect().getNode();
    ArrayList<String> result = new ArrayList<String>();

    for (Ref select : selectList) {
      result.add(dictionary.getDefinition(select.getValue()).getCode());
    }

    return result;
  }

  @Override
  protected void fillNodesMap(SetViewProperty viewDefinition) {
    ArrayList<Ref> selectList = viewDefinition.getSelect().getNode();

    this.nodes = new HashMap<String, HashMap<String, String>>();

    if (selectList.size() > 0) {
      for (Ref select : selectList) {
        Definition definition = dictionary.getDefinition(select.getValue());
        HashMap<String, String> nodeMap = new HashMap<String, String>();
        nodeMap.put("code", definition.getCode());
        nodeMap.put("label", definition.getLabelString(this.codeLanguage));
        nodeMap.put("description", definition.getDescriptionString(this.codeLanguage));
        this.nodes.put(definition.getCode(), nodeMap);
      }
    }
  }

  @Override
  protected String initAddList(HashMap<String, Object> viewMap, SetViewProperty viewDefinition) {
    return "";
  }

  @Override
  protected String initMagnets(HashMap<String, Object> viewMap, SetViewProperty viewDefinition) {
    return "";
  }

  @Override
  protected String initView(String codeView) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    SetViewProperty view = (SetViewProperty) this.definition.getNodeViewProperty(codeView);

    boolean isLocationView = codeView.equals("location");

    if (isLocationView) {
      this.initMapWithoutView(map, "location");
      return this.initLocationView(map);
    } else if (view == null) {
      map.put("codeView", codeView);
      map.put("labelDefinition", this.definition.getLabelString(this.codeLanguage));
      return block("view.undefined", map);
    }

    this.fillNodesMap(view);
    this.initMap(map, view);
    map.put("clec", "clec");

    if (this.isSystemView(view))
      return this.initSystemView(map, view);

    this.initContent(map, view);

    return block("view", map);
  }

  @Override
  protected void init() {
    loadCanvas("node.catalog");
    super.init();
  }

}
