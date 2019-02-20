package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class ThesaurusDefinitionSemanticChecks extends EntityDefinitionSemanticChecks {

  @Override
  public boolean activateRule(Definition definition) {
    return definition instanceof ThesaurusDefinition;
  }
  public void checkNodes(Definition definition) {
      super.checkNodes(definition);
      ThesaurusDefinition metaitem = (ThesaurusDefinition) definition;

  

    
            if(metaitem.getExternal() != null) {
      ThesaurusDefinition.External item = metaitem.getExternal();
                    this.checkName(item.getProvider(), ThesaurusProviderDefinition.class);
            }
          
            if(metaitem.getUse() != null) {
      ThesaurusDefinition.Use item = metaitem.getUse();
                    this.checkName(item.getReference(), ReferenceDefinition.class);
                    this.checkName(item.getKey(), AttributeDeclaration.class);
                    this.checkName(item.getParent(), AttributeDeclaration.class);
                    this.checkName(item.getLabel(), AttributeDeclaration.class);
            }
                  TermIndexDeclarationSemanticChecks termIndexDeclaration = new TermIndexDeclarationSemanticChecks();
      termIndexDeclaration.setProblems(this.getProblems());
      termIndexDeclaration.setModule(this.getModule());
      for(TermIndexDeclaration include : metaitem.getTermIndexDeclarationList()) {
        termIndexDeclaration.check(include);
      }
        }
}


















