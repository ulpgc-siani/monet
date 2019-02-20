package org.monet.v2.metamodel;


import java.util.ArrayList;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

// ContainerDefinition
// Declaración que se utiliza para modelar un contenedor

@Root(name="container")
public  class ContainerDefinitionBase extends NodeDefinition 
 {
@Root(name="contain")
public static class Contain {
protected @Attribute(name="node") String _node;
public String getNode() { return _node; }
}

protected @ElementList(inline=true,required=false) ArrayList<Contain> _containList = new ArrayList<Contain>();
protected @ElementList(inline=true,required=false) ArrayList<ContainerViewDeclaration> _containerViewDeclarationList = new ArrayList<ContainerViewDeclaration>();

public ArrayList<Contain> getContainList() { return _containList; }
public ArrayList<ContainerViewDeclaration> getContainerViewDeclarationList() { return _containerViewDeclarationList; }

}







































