package org.monet.editor.preview.renders;

public class CatalogPageRender extends NodePageRender {
  
  public CatalogPageRender(String language) {
    super(language);
  }
  
  @Override
  protected void init() {
  	loadCanvas("node.catalog");
  	super.init();
  }
  
}
