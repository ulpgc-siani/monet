package org.monet.editor.preview.renders;

import java.util.HashMap;

public class DocumentViewRender extends NodeViewRender {

  public DocumentViewRender(String language) {
    super(language);
  }

  protected void initContent(HashMap<String, Object> viewMap) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("imagesPath", this.getParameterAsString("imagesPath"));
    viewMap.put("content", block("content", map));
  }

  protected String initDocumentInForm(HashMap<String, Object> viewMap) {
    
    viewMap.put("label", language.getModelResource(this.definition.getLabel(), this.codeLanguage));
    viewMap.put("codeView", "form");
    viewMap.put("previewImageSource", this.getParameterAsString("imagesPath"));
    viewMap.put("content", block("content.form", viewMap));
    return block("view", viewMap);
  }

  protected String initSignTemplate() {
    HashMap<String, Object> map = new HashMap<String, Object>();
    return block("signTemplate:client-side", map);
  }
  
  protected String initDocumentSignView(HashMap<String, Object> viewMap) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    
    map.put("path", this.getParameterAsString("path"));
    map.put("signTemplate", this.initSignTemplate());
    map.put("code", this.definition.getCode());
    map.put("name", this.definition.getName());
    map.put("language", this.codeLanguage);

    viewMap.put("label", language.getModelResource(this.definition.getLabel(), this.codeLanguage));
    viewMap.put("codeView", "form");
    viewMap.put("content", block("content.signs", map));
    
    return block("view", viewMap);
  }

  @Override
  protected String initView(String codeView) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    boolean isLocationView = codeView.equals("location");
    boolean isFormView = codeView.equals("form");
    boolean isSignsView = codeView.equals("signs");

    if (isLocationView) {
      this.initMapWithoutView(map, "location");
      return this.initLocationView(map);
    } 
    else if (isFormView) {
      this.initMapWithoutView(map, "form");
      return this.initDocumentInForm(map);
    } 
    else if (isSignsView) {
      this.initMapWithoutView(map, "signs");
      return this.initDocumentSignView(map);
    } 

    this.initMap(map, null);
    this.initContent(map);

    return block("view", map);
  }

  @Override
  protected void init() {
    loadCanvas("node.document");
    super.init();
  }

}
