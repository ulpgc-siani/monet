package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class PatternFieldDeclarationSemanticChecks extends MultipleableFieldDeclarationSemanticChecks {

  public void check(PatternFieldDeclaration metaitem) {
  if(metaitem == null) return;
      super.check(metaitem);
  
  

            PatternDeclarationSemanticChecks patternDeclaration = new PatternDeclarationSemanticChecks();
      patternDeclaration.setProblems(this.getProblems());
      patternDeclaration.setModule(this.getModule());
      for(PatternDeclaration include : metaitem.getPatternDeclarationList()) {
        patternDeclaration.check(include);
      }
        }
}


















