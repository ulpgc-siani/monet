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

// MapDefinition
// Definición que se utiliza para modelar un mapa

@Root(name="map")
public  class MapDefinition extends EntityDefinition 
 {
@Root(name = "contain"
)
public static class Contain {
protected @Attribute(name="location",required=false) String _location;
protected @Attribute(name="node",required=false) String _node;
public String getLocation() { return _location; }
public void setLocation(String value) { _location = value; }
public String getNode() { return _node; }
public void setNode(String value) { _node = value; }
}

protected @ElementList(inline=true,required=false) ArrayList<Contain> _containList = new ArrayList<Contain>();

public ArrayList<Contain> getContainList() { return _containList; }

}







































