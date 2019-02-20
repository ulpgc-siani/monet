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

// CollectionDefinition
// Declaración que se utiliza para modelar una colección

@Root(name="collection")
public  class CollectionDefinitionBase extends SetDefinition 
 {
@Root(name = "add"
)
public static class Add {
protected @Attribute(name="node") String _node;
public String getNode() { return _node; }
public void setNode(String value) { _node = value; }
}
@Root(name = "import"
)
public static class Import {
protected @Attribute(name="name",required=false) String _name;
public String getName() { return _name; }
public void setName(String value) { _name = value; }
}

protected @ElementList(inline=true,required=false) ArrayList<Add> _addList = new ArrayList<Add>();
protected @Element(name="import",required=false) Import _import;

public ArrayList<Add> getAddList() { return _addList; }
public Import getImport() { return _import; }
public void setImport(Import value) { _import = value; }

}







































