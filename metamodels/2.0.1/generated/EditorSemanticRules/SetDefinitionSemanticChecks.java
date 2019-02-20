package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class SetDefinitionSemanticChecks extends NodeDefinitionSemanticChecks {

  @Override
  public boolean activateRule(Definition definition) {
    return definition instanceof SetDefinition;
  }
  public void checkNodes(Definition definition) {
      super.checkNodes(definition);
      SetDefinition metaitem = (SetDefinition) definition;

  

    
            if(metaitem.getUse() != null) {
      SetDefinition.Use item = metaitem.getUse();
                    if(item.getReference() != null && !item.getReference().isEmpty())
      this.checkName(item.getReference(), ReferenceDefinition.class);
            }
          
            if(metaitem.getExport() != null) {
      SetDefinition.Export item = metaitem.getExport();
                    if(item.getName() != null && !item.getName().isEmpty())
      this.checkName(item.getName(), ExporterDefinition.class);
                                  if(item.getKey() != null && !item.getKey().isEmpty())
      this.checkName(item.getKey(), AttributeDeclaration.class);
            }
                  SetViewDeclarationSemanticChecks setViewDeclaration = new SetViewDeclarationSemanticChecks();
      setViewDeclaration.setProblems(this.getProblems());
      setViewDeclaration.setModule(this.getModule());
      for(SetViewDeclaration include : metaitem.getSetViewDeclarationList()) {
        setViewDeclaration.check(include);
      }
        }
}


















