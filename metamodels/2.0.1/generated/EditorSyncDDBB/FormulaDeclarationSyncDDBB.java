package org.monet.modelling.ide.builders.stages.sync.gen;

import java.util.Collection;

import org.monet.modelling.kernel.model.*;

public class FormulaDeclarationSyncDDBB extends DeclarationSyncDDBB {

  public void sync(FormulaDeclaration metaitem) throws Exception {
  if(metaitem == null) return;
      super.sync(metaitem);
  
  

            ExpressionDeclarationSyncDDBB expressionDeclaration = new ExpressionDeclarationSyncDDBB();
      expressionDeclaration.setModule(this.getModule());
      expressionDeclaration.sync(metaitem.getExpressionDeclaration());
                  OperatorDeclarationSyncDDBB operatorDeclaration = new OperatorDeclarationSyncDDBB();
      operatorDeclaration.setModule(this.getModule());
      operatorDeclaration.sync(metaitem.getOperatorDeclaration());
        }
}


















