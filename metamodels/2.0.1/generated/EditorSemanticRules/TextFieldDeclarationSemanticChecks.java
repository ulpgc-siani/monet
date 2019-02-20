package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class TextFieldDeclarationSemanticChecks extends MultipleableFieldDeclarationSemanticChecks {

  public void check(TextFieldDeclaration metaitem) {
  if(metaitem == null) return;
      super.check(metaitem);
  
  

    
            
            if(metaitem.getLength() != null) {
      TextFieldDeclaration.Length item = metaitem.getLength();
                                        }
          
            if(metaitem.getEdition() != null) {
      TextFieldDeclaration.Edition item = metaitem.getEdition();
                          }
        }
}


















