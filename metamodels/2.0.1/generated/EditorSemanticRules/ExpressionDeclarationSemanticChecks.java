package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class ExpressionDeclarationSemanticChecks extends DeclarationSemanticChecks {

  public void check(ExpressionDeclaration metaitem) {
  if(metaitem == null) return;
      super.check(metaitem);
  
  

      if(metaitem.getIndicator() != null && !metaitem.getIndicator().isEmpty())
      this.checkName(metaitem.getIndicator(), IndicatorDeclaration.class);
  }
}


















