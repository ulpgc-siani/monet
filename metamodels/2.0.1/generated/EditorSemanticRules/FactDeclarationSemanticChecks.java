package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class FactDeclarationSemanticChecks extends DeclarationSemanticChecks {

  public void check(FactDeclaration metaitem) {
  if(metaitem == null) return;
      super.check(metaitem);
  
  

            AttributeDeclarationSemanticChecks attributeDeclaration = new AttributeDeclarationSemanticChecks();
      attributeDeclaration.setProblems(this.getProblems());
      attributeDeclaration.setModule(this.getModule());
      for(AttributeDeclaration include : metaitem.getAttributeDeclarationList()) {
        attributeDeclaration.check(include);
      }
        }
}


















