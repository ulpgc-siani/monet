package org.monet.v2.metamodel;


import org.simpleframework.xml.core.Commit;

// CatalogDefinition
// Declaración que se utiliza para modelar un catálogo

public class CatalogDefinition extends CatalogDefinitionBase {

  @Commit
  public void commit() {
    for (SetViewDeclaration view : this._setViewDeclarationList) {
      this.viewsMap.put(view.getCode(), view);
      this.viewsMap.put(view.getName(), view);

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
    }
  }

}
