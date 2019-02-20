package org.monet.v2.metamodel;


import java.util.ArrayList;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

// NodeFieldDeclaration
// Declaración que se utiliza para modelar un campo nodo

@Root(name="field-node")
public  class NodeFieldDeclarationBase extends MultipleableFieldDeclaration 
 {
@Root(name="contain")
public static class Contain {
protected @Attribute(name="node") String _node;
public String getNode() { return _node; }
}
@Root(name="add")
public static class Add {
protected @Attribute(name="node") String _node;
public String getNode() { return _node; }
}

protected @Element(name="contain",required=false) Contain _contain;
protected @ElementList(inline=true,required=false) ArrayList<Add> _addList = new ArrayList<Add>();
protected @Element(name="view",required=false) NodeFieldViewDeclaration _nodeFieldViewDeclaration;

public Contain getContain() { return _contain; }
public ArrayList<Add> getAddList() { return _addList; }
public NodeFieldViewDeclaration getNodeFieldViewDeclaration() { return _nodeFieldViewDeclaration; }

}







































