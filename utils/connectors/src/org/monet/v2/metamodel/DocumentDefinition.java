package org.monet.v2.metamodel;


import org.simpleframework.xml.core.Commit;

// DocumentDefinition
// Declaración que se utiliza para modelar un documento

public class DocumentDefinition extends DocumentDefinitionBase {

  @Commit
  public void commit() {
    for (DocumentViewDeclaration view : this._documentViewDeclarationList) {

      switch (view.getType()) {
        case TAB:
          this.tabViews.add(view);
          if (view.isDefault())
            this.defaultTabView = view;
          break;
        case EMBEDDED:
          this.embeddedViews.add(view);
          if (view.isDefault())
            this.defaultEmbeddedView = view;
          break;
      }

      this.viewsMap.put(view.getCode(), view);
      this.viewsMap.put(view.getName(), view);
    }

  }

}
