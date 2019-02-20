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

// ReferenceDefinition
// TODO

@Root(name="reference")
public  class ReferenceDefinitionBase extends Definition 
 {
@Root(name="add")
public static class Add {
protected @Attribute(name="reference") String _reference;
protected @Attribute(name="on",required=false) String _on;
public String getReference() { return _reference; }
public String getOn() { return _on; }
}

protected @Element(name="add",required=false) Add _add;
protected @ElementList(inline=true) ArrayList<AttributeDeclaration> _attributeDeclarationList = new ArrayList<AttributeDeclaration>();
protected @ElementList(inline=true) ArrayList<ReferenceViewDeclaration> _referenceViewDeclarationList = new ArrayList<ReferenceViewDeclaration>();

public Add getAdd() { return _add; }
public ArrayList<AttributeDeclaration> getAttributeDeclarationList() { return _attributeDeclarationList; }
public ArrayList<ReferenceViewDeclaration> getReferenceViewDeclarationList() { return _referenceViewDeclarationList; }

}







































