package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class ResolveDeclarationSemanticChecks extends DeclarationSemanticChecks {

  public void check(ResolveDeclaration metaitem) {
  if(metaitem == null) return;
      super.check(metaitem);
  
  

      this.checkName(metaitem.getWith(), Definition.class);
  }
}


















