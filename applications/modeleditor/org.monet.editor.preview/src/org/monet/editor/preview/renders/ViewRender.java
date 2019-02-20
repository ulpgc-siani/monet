package org.monet.editor.preview.renders;

import java.util.HashMap;

public abstract class ViewRender extends PreviewRender {
  
  public ViewRender(String language) {
    super(language);
  }

  @Override protected void init() {
    HashMap<String, Object> map = new HashMap<String, Object>();
    String codeView = this.getParameterAsString("view");
    
    loadCanvas("view");
    
    if (codeView.isEmpty()) {
      addMark("view", block("view.isNull", map));
      return;
    }
    
    addMark("view", this.initView(codeView));
  }
  
  protected String initView(String codeView) {
    return "";
  }
  
}
