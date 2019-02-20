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

// LocationFieldDeclaration
// Declaración que se utiliza para modelar un campo vínculo

@Root(name="field-location")
public  class LocationFieldDeclaration extends MultipleableFieldDeclaration 
 {
@Root(name = "use"
)
public static class Use {
protected @Attribute(name="map") String _map;
public String getMap() { return _map; }
public void setMap(String value) { _map = value; }
}

protected @Element(name="use",required=false) Use _use;
protected @ElementList(inline=true) ArrayList<AttributeDeclaration> _attributeDeclarationList = new ArrayList<AttributeDeclaration>();
protected @ElementList(inline=true,required=false) ArrayList<StyleDeclaration> _styleDeclarationList = new ArrayList<StyleDeclaration>();

public Use getUse() { return _use; }
public void setUse(Use value) { _use = value; }
public ArrayList<AttributeDeclaration> getAttributeDeclarationList() { return _attributeDeclarationList; }
public ArrayList<StyleDeclaration> getStyleDeclarationList() { return _styleDeclarationList; }

}







































