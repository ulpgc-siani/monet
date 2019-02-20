package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class GenericDeclarationSemanticChecks extends DeclarationSemanticChecks {

  public void check(GenericDeclaration metaitem) {
  if(metaitem == null) return;
      super.check(metaitem);
  
  

      if(metaitem.getExtends() != null && !metaitem.getExtends().isEmpty())
      this.checkName(metaitem.getExtends(), Definition.class);
  }
}


















