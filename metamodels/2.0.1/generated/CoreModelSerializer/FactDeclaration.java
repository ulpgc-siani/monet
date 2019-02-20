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

// FactDeclaration
// Declaración que se utiliza para modelar el hecho de un cubo

@Root(name="fact")
public  class FactDeclaration extends Declaration 
 {

protected @ElementList(inline=true) ArrayList<AttributeDeclaration> _attributeDeclarationList = new ArrayList<AttributeDeclaration>();

public ArrayList<AttributeDeclaration> getAttributeDeclarationList() { return _attributeDeclarationList; }

}







































