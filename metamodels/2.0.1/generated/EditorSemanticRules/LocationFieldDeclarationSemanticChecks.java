package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class LocationFieldDeclarationSemanticChecks extends MultipleableFieldDeclarationSemanticChecks {

  public void check(LocationFieldDeclaration metaitem) {
  if(metaitem == null) return;
      super.check(metaitem);
  
  

    
            if(metaitem.getUse() != null) {
      LocationFieldDeclaration.Use item = metaitem.getUse();
                    this.checkName(item.getMap(), MapDefinition.class);
            }
                  AttributeDeclarationSemanticChecks attributeDeclaration = new AttributeDeclarationSemanticChecks();
      attributeDeclaration.setProblems(this.getProblems());
      attributeDeclaration.setModule(this.getModule());
      for(AttributeDeclaration include : metaitem.getAttributeDeclarationList()) {
        attributeDeclaration.check(include);
      }
                  StyleDeclarationSemanticChecks styleDeclaration = new StyleDeclarationSemanticChecks();
      styleDeclaration.setProblems(this.getProblems());
      styleDeclaration.setModule(this.getModule());
      for(StyleDeclaration include : metaitem.getStyleDeclarationList()) {
        styleDeclaration.check(include);
      }
        }
}


















