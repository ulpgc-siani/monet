package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class MemoFieldDeclarationSemanticChecks extends MultipleableFieldDeclarationSemanticChecks {

  public void check(MemoFieldDeclaration metaitem) {
  if(metaitem == null) return;
      super.check(metaitem);
  
  

    
            
            if(metaitem.getLength() != null) {
      MemoFieldDeclaration.Length item = metaitem.getLength();
                                        }
          
            if(metaitem.getEdition() != null) {
      MemoFieldDeclaration.Edition item = metaitem.getEdition();
                          }
        }
}


















