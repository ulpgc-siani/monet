package org.monet.v2.metamodel;


import org.monet.api.backservice.impl.model.AttributeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// NodeDefinition
// Declaración abstracta de un nodo. Un nodo es un elemento navegable del sistema de información

public abstract class NodeDefinition extends NodeDefinitionBase {
  protected NodeViewDeclaration              defaultTabView;
  protected NodeViewDeclaration              defaultEmbeddedView;
  protected List<NodeViewDeclaration>        tabViews      = new ArrayList<NodeViewDeclaration>();
  protected List<NodeViewDeclaration>        embeddedViews = new ArrayList<NodeViewDeclaration>();
  protected Map<String, NodeViewDeclaration> viewsMap      = new HashMap<String, NodeViewDeclaration>();

  public boolean isPublic() {
    return this._isPrivate == null;
  }

  public AttributeList buildAttributes() {
    return new AttributeList();
  }

  public boolean isContainer() {
    return this instanceof ContainerDefinition;
  }

  public boolean isDesktop() {
    return this instanceof DesktopDefinition;
  }

  public boolean isCollection() {
    return this instanceof CollectionDefinition;
  }

  public boolean isCatalog() {
    return this instanceof CatalogDefinition;
  }

  public boolean isForm() {
    return this instanceof FormDefinition;
  }

  public boolean isDocument() {
    return this instanceof DocumentDefinition;
  }

  public NodeViewDeclaration getDefaultTabView() {
    return this.defaultTabView;
  }

  public NodeViewDeclaration getDefaultEmbeddedView() {
    return this.defaultEmbeddedView;
  }

  public NodeViewDeclaration getNodeViewDeclaration(String key) {
    return this.viewsMap.get(key);
  }

  public List<NodeViewDeclaration> getTabViewDeclarationList() {
    return this.tabViews;
  }

  public List<NodeViewDeclaration> getEmbeddedViewDeclarationList() {
    return this.embeddedViews;
  }

}
