package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class FormulaDeclarationSemanticChecks extends DeclarationSemanticChecks {

  public void check(FormulaDeclaration metaitem) {
  if(metaitem == null) return;
      super.check(metaitem);
  
  

            ExpressionDeclarationSemanticChecks expressionDeclaration = new ExpressionDeclarationSemanticChecks();
      expressionDeclaration.setProblems(this.getProblems());
      expressionDeclaration.setModule(this.getModule());
      expressionDeclaration.check(metaitem.getExpressionDeclaration());
                  OperatorDeclarationSemanticChecks operatorDeclaration = new OperatorDeclarationSemanticChecks();
      operatorDeclaration.setProblems(this.getProblems());
      operatorDeclaration.setModule(this.getModule());
      operatorDeclaration.check(metaitem.getOperatorDeclaration());
        }
}


















