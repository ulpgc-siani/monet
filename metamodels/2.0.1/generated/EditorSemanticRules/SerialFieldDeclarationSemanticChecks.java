package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class SerialFieldDeclarationSemanticChecks extends MultipleableFieldDeclarationSemanticChecks {

  public void check(SerialFieldDeclaration metaitem) {
  if(metaitem == null) return;
      super.check(metaitem);
  
  

    
            if(metaitem.getGenerator() != null) {
      SerialFieldDeclaration.Generator item = metaitem.getGenerator();
                          }
          
            if(metaitem.getFormat() != null) {
      SerialFieldDeclaration.Format item = metaitem.getFormat();
                          }
        }
}


















