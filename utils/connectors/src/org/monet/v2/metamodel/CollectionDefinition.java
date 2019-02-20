package org.monet.v2.metamodel;


import org.simpleframework.xml.core.Commit;

// CollectionDefinition
// Declaración que se utiliza para modelar una colección

public class CollectionDefinition extends CollectionDefinitionBase {

  @Commit
  public void commit() {
    for (SetViewDeclaration view : this._setViewDeclarationList) {

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
