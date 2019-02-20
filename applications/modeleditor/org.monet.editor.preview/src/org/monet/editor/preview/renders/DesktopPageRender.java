package org.monet.editor.preview.renders;


public class DesktopPageRender extends NodePageRender {
  
  public DesktopPageRender(String language) {
    super(language);
  }

  @Override protected void init() {
  	loadCanvas("node.desktop");
    super.init();
  }
  
}
