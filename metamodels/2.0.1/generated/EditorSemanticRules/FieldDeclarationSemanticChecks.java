package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class FieldDeclarationSemanticChecks extends IndexedDeclarationSemanticChecks {

  public void check(FieldDeclaration metaitem) {
  if(metaitem == null) return;
      super.check(metaitem);
  
  

    
            
            
            
            
            
                    for(FieldDeclaration.DefaultValue item : metaitem.getDefaultValueList()) {
                                                    }
                
                      
                      
                    for(FieldDeclaration.Message item : metaitem.getMessageList()) {
                                                    }
              }
}


















