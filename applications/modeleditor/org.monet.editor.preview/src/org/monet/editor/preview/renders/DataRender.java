package org.monet.editor.preview.renders;

import java.util.HashMap;

public class DataRender extends PreviewRender {
  
  public DataRender(String language) {
    super(language);
  }
  
  @Override protected void init() {
    loadCanvas("data");
    
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("nrows", "0");
    map.put("rows", "");
    
    addMark("data", block("data", map));
  }
  
}
