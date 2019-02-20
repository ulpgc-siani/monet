package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class SetViewDeclarationSemanticChecks extends NodeViewDeclarationSemanticChecks {

  public void check(SetViewDeclaration metaitem) {
  if(metaitem == null) return;
      super.check(metaitem);
  
  

    
                    for(SetViewDeclaration.ShowSummary item : metaitem.getShowSummaryList()) {
                                          if(item.getAttribute() != null && !item.getAttribute().isEmpty())
      this.checkName(item.getAttribute(), AttributeDeclaration.class);
                }
                
            if(metaitem.getShowList() != null) {
      SetViewDeclaration.ShowList item = metaitem.getShowList();
                                  if(item.getImage() != null && !item.getImage().isEmpty())
      this.checkName(item.getImage(), AttributeDeclaration.class);
                    this.checkName(item.getReferenceView(), ReferenceViewDeclaration.class);
            }
          
            if(metaitem.getShowGrid() != null) {
      SetViewDeclaration.ShowGrid item = metaitem.getShowGrid();
                                  if(item.getImage() != null && !item.getImage().isEmpty())
      this.checkName(item.getImage(), AttributeDeclaration.class);
                    this.checkName(item.getReferenceView(), ReferenceViewDeclaration.class);
            }
          
            if(metaitem.getShowLocation() != null) {
      SetViewDeclaration.ShowLocation item = metaitem.getShowLocation();
                          }
          
            if(metaitem.getShowLayer() != null) {
      SetViewDeclaration.ShowLayer item = metaitem.getShowLayer();
            }
          
          }
}


















