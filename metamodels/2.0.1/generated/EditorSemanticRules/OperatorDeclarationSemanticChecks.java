package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class OperatorDeclarationSemanticChecks extends DeclarationSemanticChecks {

  public void check(OperatorDeclaration metaitem) {
  if(metaitem == null) return;
      super.check(metaitem);
  
  

            ExpressionDeclarationSemanticChecks expressionDeclaration = new ExpressionDeclarationSemanticChecks();
      expressionDeclaration.setProblems(this.getProblems());
      expressionDeclaration.setModule(this.getModule());
      for(ExpressionDeclaration include : metaitem.getExpressionDeclarationList()) {
        expressionDeclaration.check(include);
      }
                  OperatorDeclarationSemanticChecks operatorDeclaration = new OperatorDeclarationSemanticChecks();
      operatorDeclaration.setProblems(this.getProblems());
      operatorDeclaration.setModule(this.getModule());
      for(OperatorDeclaration include : metaitem.getOperatorDeclarationList()) {
        operatorDeclaration.check(include);
      }
        }
}


















