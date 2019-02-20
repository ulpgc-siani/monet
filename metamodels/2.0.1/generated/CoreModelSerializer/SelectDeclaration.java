package org.monet.kernel.model.definition;

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

// SelectDeclaration
// Se utiliza para indicar qué nodos se muestran en la colección

@Root(name="select")
public  class SelectDeclaration extends Declaration 
 {

protected @Attribute(name="node") String _node;

public String getNode() { return _node; }

}







































