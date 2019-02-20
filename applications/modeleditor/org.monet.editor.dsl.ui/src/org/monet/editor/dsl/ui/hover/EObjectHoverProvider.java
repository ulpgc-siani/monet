package org.monet.editor.dsl.ui.hover;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.common.types.xtext.ui.JdtHoverProvider;
import org.eclipse.xtext.xbase.ui.hover.XbaseHoverProvider;
import org.monet.editor.dsl.helper.XtendHelper;
import org.monet.editor.dsl.metamodel.Item;
import org.monet.editor.dsl.metamodel.MetaModelStructure;

import com.google.inject.Inject;

@SuppressWarnings("restriction")
public class EObjectHoverProvider extends XbaseHoverProvider {

  @Inject
  private JdtHoverProvider jdtHoverProvider;
  
  @Inject
  private MetaModelStructure structure;
  
  @SuppressWarnings("deprecation")
  @Override
  protected String getFirstLine(EObject o) {
    Item item = XtendHelper.checkElement(o, this.structure, true);
    if(item == null)
      return super.getFirstLine(o);
    return String.format("<b>%s</b>", item.getName());
  }
  
  @Override
  protected String computeSignature(EObject call, EObject o) {
    Item item = XtendHelper.checkElement(o, this.structure, true);
    if(item == null)
      return super.computeSignature(call, o);
    return String.format("<b>%s</b>", item.getName());
  }
  
  @Override
  protected String getDocumentation(EObject o) {
    Item item = XtendHelper.checkElement(o, this.structure, true);
    if(item == null)
      return super.getDocumentation(o);
    StringBuilder builder = new StringBuilder();
    builder.append(item.getDescription());
    builder.append("</p>");
    String hint = item.getHint();
    if(hint != null && !hint.isEmpty()) {
      builder.append("<i>Hint: </i> ");
      builder.append(hint);
    }
    return builder.toString();
  }
  
  @Override
  protected boolean hasHover(EObject o) {
    return true;
  }
  
}
