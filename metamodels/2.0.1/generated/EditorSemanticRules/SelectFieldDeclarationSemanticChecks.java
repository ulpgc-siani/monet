package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class SelectFieldDeclarationSemanticChecks extends MultipleableFieldDeclarationSemanticChecks {

  public void check(SelectFieldDeclaration metaitem) {
  if(metaitem == null) return;
      super.check(metaitem);
  
  

    
            if(metaitem.getImport() != null) {
      SelectFieldDeclaration.Import item = metaitem.getImport();
                    if(item.getThesaurus() != null && !item.getThesaurus().isEmpty())
      this.checkName(item.getThesaurus(), ThesaurusDefinition.class);
                                                                    }
          
            
            
            
            
                    TermIndexDeclarationSemanticChecks termIndexDeclaration = new TermIndexDeclarationSemanticChecks();
      termIndexDeclaration.setProblems(this.getProblems());
      termIndexDeclaration.setModule(this.getModule());
      termIndexDeclaration.check(metaitem.getTermIndexDeclaration());
        }
}


















