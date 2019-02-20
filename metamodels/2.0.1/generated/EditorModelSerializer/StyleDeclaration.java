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

// StyleDeclaration
// Declaración de un término

@Root(name="style")
public  class StyleDeclaration extends Declaration 
 {
@Root(name = "when"
)
public static class When {
protected @Attribute(name="attribute") String _attribute;
protected @Attribute(name="value") String _value;
public String getAttribute() { return _attribute; }
public void setAttribute(String value) { _attribute = value; }
public String getValue() { return _value; }
public void setValue(String value) { _value = value; }
}

protected @Attribute(name="color") String _color;
protected @ElementList(inline=true,required=false) ArrayList<When> _whenList = new ArrayList<When>();

public String getColor() { return _color; }
public void setColor(String value) { _color = value; }
public ArrayList<When> getWhenList() { return _whenList; }

}







































