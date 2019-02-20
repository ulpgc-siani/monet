package org.monet.modelling.ide.builders.stages.sync.gen;

import java.util.Collection;

import org.monet.modelling.kernel.model.*;

public class DocumentDefinitionSyncDDBB extends NodeDefinitionSyncDDBB {

  public void sync(Definition definition) throws Exception {
      super.sync(definition);
      DocumentDefinition metaitem = (DocumentDefinition) definition;

  

for(DocumentDefinition.Signature item : metaitem.getSignatureList()) {
}
            DocumentViewDeclarationSyncDDBB documentViewDeclaration = new DocumentViewDeclarationSyncDDBB();
      documentViewDeclaration.setModule(this.getModule());
      for(DocumentViewDeclaration include : metaitem.getDocumentViewDeclarationList()) {
        documentViewDeclaration.sync(include);
      }
        }
}


















