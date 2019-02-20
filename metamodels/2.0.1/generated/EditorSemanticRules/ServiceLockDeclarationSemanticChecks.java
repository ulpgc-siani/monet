package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class ServiceLockDeclarationSemanticChecks extends WorklockDeclarationSemanticChecks {

  public void check(ServiceLockDeclaration metaitem) {
  if(metaitem == null) return;
      super.check(metaitem);
  
  

    
            if(metaitem.getUse() != null) {
      ServiceLockDeclaration.Use item = metaitem.getUse();
                    this.checkName(item.getServiceProvider(), ServiceProviderDefinition.class);
            }
          
            if(metaitem.getRequest() != null) {
      ServiceLockDeclaration.Request item = metaitem.getRequest();
                    this.checkName(item.getDocument(), DocumentDefinition.class);
            }
          
            if(metaitem.getResponse() != null) {
      ServiceLockDeclaration.Response item = metaitem.getResponse();
                    this.checkName(item.getDocument(), DocumentDefinition.class);
            }
        }
}


















