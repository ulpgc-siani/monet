package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class DesktopViewDeclarationSemanticChecks extends NodeViewDeclarationSemanticChecks {

  public void check(DesktopViewDeclaration metaitem) {
  if(metaitem == null) return;
      super.check(metaitem);
  
  

    
                    for(DesktopViewDeclaration.Show item : metaitem.getShowList()) {
                        this.checkName(item.getEntity(), Definition.class);
                        if(item.getView() != null && !item.getView().isEmpty())
      this.checkName(item.getView(), ViewDeclaration.class);
                }
                
            if(metaitem.getShowNews() != null) {
      DesktopViewDeclaration.ShowNews item = metaitem.getShowNews();
            }
        }
}


















