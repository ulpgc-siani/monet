package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class TimerLockDeclarationSemanticChecks extends WorklockDeclarationSemanticChecks {

  public void check(TimerLockDeclaration metaitem) {
  if(metaitem == null) return;
      super.check(metaitem);
  
  

    
            if(metaitem.getTimer() != null) {
      TimerLockDeclaration.Timer item = metaitem.getTimer();
                                        }
        }
}


















