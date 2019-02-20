package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class WorklockDeclarationSemanticChecks extends IndexedDeclarationSemanticChecks {

  public void check(WorklockDeclaration metaitem) {
  if(metaitem == null) return;
      super.check(metaitem);
  
  

    
            
                      
                    for(WorklockDeclaration.Out item : metaitem.getOutList()) {
                        if(item.getWorkstop() != null && !item.getWorkstop().isEmpty())
      this.checkName(item.getWorkstop(), WorkstopDeclaration.class);
                }
              }
}


















