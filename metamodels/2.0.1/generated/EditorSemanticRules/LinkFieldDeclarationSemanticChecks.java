package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class LinkFieldDeclarationSemanticChecks extends MultipleableFieldDeclarationSemanticChecks {

  public void check(LinkFieldDeclaration metaitem) {
  if(metaitem == null) return;
      super.check(metaitem);
  
  

    
            if(metaitem.getSource() != null) {
      LinkFieldDeclaration.Source item = metaitem.getSource();
                    this.checkName(item.getSet(), SetDefinition.class);
                    if(item.getFilter() != null && !item.getFilter().isEmpty())
      this.checkName(item.getFilter(), NodeDefinition.class);
            }
          
                    for(LinkFieldDeclaration.Bind item : metaitem.getBindList()) {
                        this.checkName(item.getParameter(), ParameterDeclaration.class);
                        this.checkName(item.getField(), FieldDeclaration.class);
                }
                
            
            
          }
}


















