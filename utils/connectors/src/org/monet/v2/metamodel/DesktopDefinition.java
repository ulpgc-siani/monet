package org.monet.v2.metamodel;


import org.simpleframework.xml.core.Commit;

public class DesktopDefinition extends DesktopDefinitionBase {

  @Commit
  public void commit() {
    for (DesktopViewDeclaration view : this._desktopViewDeclarationList) {
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
