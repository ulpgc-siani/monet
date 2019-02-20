package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class WorkmapDeclarationSemanticChecks extends DeclarationSemanticChecks {

  public void check(WorkmapDeclaration metaitem) {
  if(metaitem == null) return;
      super.check(metaitem);
  
  

    
                      
                              WorkplaceDeclarationSemanticChecks workplaceDeclaration = new WorkplaceDeclarationSemanticChecks();
      workplaceDeclaration.setProblems(this.getProblems());
      workplaceDeclaration.setModule(this.getModule());
      for(WorkplaceDeclaration include : metaitem.getWorkplaceDeclarationList()) {
        workplaceDeclaration.check(include);
      }
                  WorklineDeclarationSemanticChecks worklineDeclaration = new WorklineDeclarationSemanticChecks();
      worklineDeclaration.setProblems(this.getProblems());
      worklineDeclaration.setModule(this.getModule());
      for(WorklineDeclaration include : metaitem.getWorklineDeclarationList()) {
        worklineDeclaration.check(include);
      }
        }
}


















