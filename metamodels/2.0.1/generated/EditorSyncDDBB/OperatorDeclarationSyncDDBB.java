package org.monet.modelling.ide.builders.stages.sync.gen;

import java.util.Collection;

import org.monet.modelling.kernel.model.*;

public class OperatorDeclarationSyncDDBB extends DeclarationSyncDDBB {

  public void sync(OperatorDeclaration metaitem) throws Exception {
  if(metaitem == null) return;
      super.sync(metaitem);
  
  

            ExpressionDeclarationSyncDDBB expressionDeclaration = new ExpressionDeclarationSyncDDBB();
      expressionDeclaration.setModule(this.getModule());
      for(ExpressionDeclaration include : metaitem.getExpressionDeclarationList()) {
        expressionDeclaration.sync(include);
      }
                  OperatorDeclarationSyncDDBB operatorDeclaration = new OperatorDeclarationSyncDDBB();
      operatorDeclaration.setModule(this.getModule());
      for(OperatorDeclaration include : metaitem.getOperatorDeclarationList()) {
        operatorDeclaration.sync(include);
      }
        }
}


















