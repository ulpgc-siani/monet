package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class DimensionDeclarationSemanticChecks extends IndexedDeclarationSemanticChecks {

  public void check(DimensionDeclaration metaitem) {
  if(metaitem == null) return;
      super.check(metaitem);
  
  

      if(metaitem.getReference() != null && !metaitem.getReference().isEmpty())
      this.checkName(metaitem.getReference(), ReferenceDefinition.class);
      if(metaitem.getThesaurus() != null && !metaitem.getThesaurus().isEmpty())
      this.checkName(metaitem.getThesaurus(), ThesaurusDefinition.class);
    
            
                              AttributeDeclarationSemanticChecks attributeDeclaration = new AttributeDeclarationSemanticChecks();
      attributeDeclaration.setProblems(this.getProblems());
      attributeDeclaration.setModule(this.getModule());
      for(AttributeDeclaration include : metaitem.getAttributeDeclarationList()) {
        attributeDeclaration.check(include);
      }
        }
}


















