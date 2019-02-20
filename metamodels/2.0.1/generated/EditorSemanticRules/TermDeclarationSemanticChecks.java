package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class TermDeclarationSemanticChecks extends DeclarationSemanticChecks {

  public void check(TermDeclaration metaitem) {
  if(metaitem == null) return;
      super.check(metaitem);
  
  

            TermDeclarationSemanticChecks termDeclaration = new TermDeclarationSemanticChecks();
      termDeclaration.setProblems(this.getProblems());
      termDeclaration.setModule(this.getModule());
      for(TermDeclaration include : metaitem.getTermDeclarationList()) {
        termDeclaration.check(include);
      }
        }
}


















