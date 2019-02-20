package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class SyncLockDeclarationSemanticChecks extends WorklockDeclarationSemanticChecks {

  public void check(SyncLockDeclaration metaitem) {
  if(metaitem == null) return;
      super.check(metaitem);
  
  

    
            if(metaitem.getWait() != null) {
      SyncLockDeclaration.Wait item = metaitem.getWait();
                    this.checkName(item.getTask(), TaskDefinition.class);
            }
        }
}


















