package org.monet.editor.preview.renders;

public class CollectionPageRender extends NodePageRender {
  
  public CollectionPageRender(String language) {
    super(language);
  }
  
  @Override
  protected void init() {
  	loadCanvas("node.collection");
  	super.init();
  }
  
}
