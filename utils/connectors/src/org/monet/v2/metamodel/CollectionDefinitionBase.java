package org.monet.v2.metamodel;


import java.util.ArrayList;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

// CollectionDefinition
// Declaración que se utiliza para modelar una colección

@Root(name="collection")
public  class CollectionDefinitionBase extends SetDefinition 
 {
@Root(name="add")
public static class Add {
protected @Attribute(name="node") String _node;
public String getNode() { return _node; }
}
@Root(name="import")
public static class Import {
protected @Attribute(name="name",required=false) String _name;
public String getName() { return _name; }
}

protected @ElementList(inline=true,required=false) ArrayList<Add> _addList = new ArrayList<Add>();
protected @Element(name="import",required=false) Import _import;

public ArrayList<Add> getAddList() { return _addList; }
public Import getImport() { return _import; }

}







































