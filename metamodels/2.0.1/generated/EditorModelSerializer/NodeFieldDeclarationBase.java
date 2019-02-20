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

// NodeFieldDeclaration
// Declaración que se utiliza para modelar un campo nodo

@Root(name="field-node")
public  class NodeFieldDeclarationBase extends MultipleableFieldDeclaration 
 {
@Root(name = "contain"
)
public static class Contain {
protected @Attribute(name="node") String _node;
public String getNode() { return _node; }
public void setNode(String value) { _node = value; }
}
@Root(name = "add"
)
public static class Add {
protected @Attribute(name="node") String _node;
public String getNode() { return _node; }
public void setNode(String value) { _node = value; }
}

protected @Element(name="contain",required=false) Contain _contain;
protected @ElementList(inline=true,required=false) ArrayList<Add> _addList = new ArrayList<Add>();
protected @Element(name="view",required=false) NodeFieldViewDeclaration _nodeFieldViewDeclaration;

public Contain getContain() { return _contain; }
public void setContain(Contain value) { _contain = value; }
public ArrayList<Add> getAddList() { return _addList; }
public NodeFieldViewDeclaration getNodeFieldViewDeclaration() { return _nodeFieldViewDeclaration; }

}







































