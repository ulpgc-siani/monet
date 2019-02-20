package org.monet.editor.preview.renders;

import org.monet.metamodel.NodeViewProperty;

public class ContainerPageRender extends NodePageRender {
  
  public ContainerPageRender(String language) {
    super(language);
  }

  @Override protected void init() {
    loadCanvas("node.container");
    super.init();
  }
  
  @Override
  protected String initTab(NodeViewProperty viewDefinition) {
    return super.initTab(viewDefinition);
  }
  
}
