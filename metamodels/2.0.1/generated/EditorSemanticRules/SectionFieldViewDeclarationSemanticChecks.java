package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class SectionFieldViewDeclarationSemanticChecks extends FieldViewDeclarationSemanticChecks {

  public void check(SectionFieldViewDeclaration metaitem) {
  if(metaitem == null) return;
      super.check(metaitem);
  
  

    
            if(metaitem.getRows() != null) {
      SectionFieldViewDeclaration.Rows item = metaitem.getRows();
                          }
          
                    for(SectionFieldViewDeclaration.Column item : metaitem.getColumnList()) {
                        if(item.getField() != null && !item.getField().isEmpty())
      this.checkName(item.getField(), FieldDeclaration.class);
                                  }
                
                    for(SectionFieldViewDeclaration.Show item : metaitem.getShowList()) {
                        this.checkName(item.getField(), FieldDeclaration.class);
                }
              }
}


















