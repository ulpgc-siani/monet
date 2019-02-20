package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class IndexedDeclarationSemanticChecks extends DeclarationSemanticChecks {

  public void check(IndexedDeclaration metaitem) {
  if(metaitem == null) return;
      super.check(metaitem);
  
  

  if (metaitem.getCode() == null || metaitem.getCode().isEmpty())
    metaitem.setCode(this.getCode(metaitem.getName(), metaitem.getClass()));
  else
    this.checkUnique(metaitem.getCode());
  }
}


















