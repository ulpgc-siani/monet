package org.monet.v2.metamodel;


import java.util.ArrayList;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

// StyleDeclaration
// Declaración de un término

@Root(name="style")
public  class StyleDeclaration extends Declaration 
 {
@Root(name="when")
public static class When {
protected @Attribute(name="attribute") String _attribute;
protected @Attribute(name="value") String _value;
public String getAttribute() { return _attribute; }
public String getValue() { return _value; }
}

protected @Attribute(name="color") String _color;
protected @ElementList(inline=true,required=false) ArrayList<When> _whenList = new ArrayList<When>();

public String getColor() { return _color; }
public ArrayList<When> getWhenList() { return _whenList; }

}







































