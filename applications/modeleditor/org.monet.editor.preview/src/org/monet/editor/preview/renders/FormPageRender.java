package org.monet.editor.preview.renders;

public class FormPageRender extends NodePageRender {
  
  public FormPageRender(String language) {
    super(language);
  }

  @Override protected void init() {
    loadCanvas("node.form");
    super.init();
  }

}
