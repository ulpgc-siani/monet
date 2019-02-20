package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class ReferenceViewDeclarationSemanticChecks extends ViewDeclarationSemanticChecks {

  public void check(ReferenceViewDeclaration metaitem) {
  if(metaitem == null) return;
      super.check(metaitem);
  
  

    
            
                    for(ReferenceViewDeclaration.Show item : metaitem.getShowList()) {
                        this.checkName(item.getAttribute(), AttributeDeclaration.class);
                }
                        StyleDeclarationSemanticChecks styleDeclaration = new StyleDeclarationSemanticChecks();
      styleDeclaration.setProblems(this.getProblems());
      styleDeclaration.setModule(this.getModule());
      for(StyleDeclaration include : metaitem.getStyleDeclarationList()) {
        styleDeclaration.check(include);
      }
        }
}


















