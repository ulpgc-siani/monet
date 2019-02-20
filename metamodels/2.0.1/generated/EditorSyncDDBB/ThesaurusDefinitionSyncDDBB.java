package org.monet.modelling.ide.builders.stages.sync.gen;

import java.util.Collection;

import org.monet.modelling.kernel.model.*;

public class ThesaurusDefinitionSyncDDBB extends EntityDefinitionSyncDDBB {

  public void sync(Definition definition) throws Exception {
      super.sync(definition);
      ThesaurusDefinition metaitem = (ThesaurusDefinition) definition;

  

            TermIndexDeclarationSyncDDBB termIndexDeclaration = new TermIndexDeclarationSyncDDBB();
      termIndexDeclaration.setModule(this.getModule());
      for(TermIndexDeclaration include : metaitem.getTermIndexDeclarationList()) {
        termIndexDeclaration.sync(include);
      }
        }
}


















