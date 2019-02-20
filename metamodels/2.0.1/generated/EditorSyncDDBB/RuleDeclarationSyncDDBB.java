package org.monet.modelling.ide.builders.stages.sync.gen;

import java.util.Collection;

import org.monet.modelling.kernel.model.*;

public class RuleDeclarationSyncDDBB extends DeclarationSync {

  public void sync(RuleDeclaration metaitem) throws Exception {
  if(metaitem == null) return;
  
  

for(RuleDeclaration.Condition item : metaitem.getConditionList()) {
}
for(RuleDeclaration.Action item : metaitem.getActionList()) {
}
  }
}


















