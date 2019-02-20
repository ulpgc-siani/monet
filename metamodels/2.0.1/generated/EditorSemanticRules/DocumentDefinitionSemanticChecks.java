package org.monet.modelling.ide.builders.stages.semantic.gen;

import java.util.Collection;

import org.monet.modelling.ide.problems.Problem;
import org.monet.modelling.kernel.model.*;

public class DocumentDefinitionSemanticChecks extends NodeDefinitionSemanticChecks {

  @Override
  public boolean activateRule(Definition definition) {
    return definition instanceof DocumentDefinition;
  }
  public void checkNodes(Definition definition) {
      super.checkNodes(definition);
      DocumentDefinition metaitem = (DocumentDefinition) definition;

  

    
            if(metaitem.getFormat() != null) {
      DocumentDefinition.Format item = metaitem.getFormat();
                          }
          
                    for(DocumentDefinition.Signature item : metaitem.getSignatureList()) {
                        if(item.getRole() != null && !item.getRole().isEmpty())
      this.checkName(item.getRole(), RoleDefinition.class);
                }
                        DocumentViewDeclarationSemanticChecks documentViewDeclaration = new DocumentViewDeclarationSemanticChecks();
      documentViewDeclaration.setProblems(this.getProblems());
      documentViewDeclaration.setModule(this.getModule());
      for(DocumentViewDeclaration include : metaitem.getDocumentViewDeclarationList()) {
        documentViewDeclaration.check(include);
      }
        }
}


















