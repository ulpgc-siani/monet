package org.monet.modelling.kernel.model;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;

import org.simpleframework.xml.Root;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;

// ContainerDefinition
// Declaración que se utiliza para modelar un contenedor

@Root(name="container")
public  class ContainerDefinitionBase extends NodeDefinition 
 {
@Root(name = "contain"
)
public static class Contain {
protected @Attribute(name="node") String _node;
public String getNode() { return _node; }
public void setNode(String value) { _node = value; }
}

protected @ElementList(inline=true,required=false) ArrayList<Contain> _containList = new ArrayList<Contain>();
protected @ElementList(inline=true,required=false) ArrayList<ContainerViewDeclaration> _containerViewDeclarationList = new ArrayList<ContainerViewDeclaration>();

public ArrayList<Contain> getContainList() { return _containList; }
public ArrayList<ContainerViewDeclaration> getContainerViewDeclarationList() { return _containerViewDeclarationList; }

}







































