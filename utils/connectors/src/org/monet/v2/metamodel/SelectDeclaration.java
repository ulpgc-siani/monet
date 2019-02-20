package org.monet.v2.metamodel;


import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

// SelectDeclaration
// Se utiliza para indicar qué nodos se muestran en la colección

@Root(name="select")
public  class SelectDeclaration extends Declaration 
 {

protected @Attribute(name="node") String _node;

public String getNode() { return _node; }

}







































