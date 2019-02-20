package org.monet.editor.dsl.generator;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmVisibility;
import org.eclipse.xtext.xbase.jvmmodel.JvmTypesBuilder;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("restriction")
public class MonetJvmTypesBuilder extends JvmTypesBuilder {

  /**
   * Creates a public method with the given name and the given return type and associates it with the given
   * sourceElement.
   */
  public JvmStaticBlock toStaticBlock(EObject sourceElement, Procedure1<JvmOperation> init) {
    JvmStaticBlock result = new JvmStaticBlockImpl();
    result.setSimpleName("");
    result.setVisibility(JvmVisibility.PUBLIC);
    if (init != null)
      init.apply(result);
    return associate(sourceElement, result);
  }
  
}
