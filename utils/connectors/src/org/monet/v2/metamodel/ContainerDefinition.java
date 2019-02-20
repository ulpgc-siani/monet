package org.monet.v2.metamodel;


import org.simpleframework.xml.core.Commit;

// ContainerDefinition
// Declaración que se utiliza para modelar un contenedor

public class ContainerDefinition extends ContainerDefinitionBase {

  @Commit
  public void commit() {
    for (ContainerViewDeclaration view : this._containerViewDeclarationList) {
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
