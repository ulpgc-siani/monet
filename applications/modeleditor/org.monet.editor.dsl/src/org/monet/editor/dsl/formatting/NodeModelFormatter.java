package org.monet.editor.dsl.formatting;

import org.eclipse.xtext.formatting.impl.DefaultNodeModelFormatter;
import org.eclipse.xtext.nodemodel.ICompositeNode;

public class NodeModelFormatter extends DefaultNodeModelFormatter {

  /***
   * rjarana: this fixes the bug of root don't have a semanticElement introduced in Xtext version 2.3 M7
   */
  @Override
  public IFormattedRegion format(ICompositeNode root, int offset, int length) {
    return super.format((ICompositeNode) (root.getSemanticElement() != null ? root : root.getFirstChild()), offset, length);
  }
  
}
