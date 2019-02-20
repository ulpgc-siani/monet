package org.monet.v2.metamodel;


import java.util.ArrayList;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

// MapDefinition
// Definición que se utiliza para modelar un mapa

@Root(name="map")
public  class MapDefinition extends EntityDefinition 
 {
@Root(name="contain")
public static class Contain {
protected @Attribute(name="location",required=false) String _location;
protected @Attribute(name="node",required=false) String _node;
public String getLocation() { return _location; }
public String getNode() { return _node; }
}

protected @ElementList(inline=true,required=false) ArrayList<Contain> _containList = new ArrayList<Contain>();

public ArrayList<Contain> getContainList() { return _containList; }

}







































