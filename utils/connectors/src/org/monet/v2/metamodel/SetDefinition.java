package org.monet.v2.metamodel;


import java.util.ArrayList;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

// SetDefinition
// Declaración que se utiliza para modelar un conjunto de datos

public abstract class SetDefinition extends NodeDefinition 
 {
@Root(name="use")
public static class Use {
protected @Attribute(name="reference",required=false) String _reference;
public String getReference() { return _reference; }
}
@Root(name="export")
public static class Export {
public enum TypeEnumeration { MAP,THESAURO }
protected @Attribute(name="name",required=false) String _name;
protected @Attribute(name="type",required=false) TypeEnumeration _type;
protected @Attribute(name="key",required=false) String _key;
public String getName() { return _name; }
public TypeEnumeration getType() { return _type; }
public String getKey() { return _key; }
}

protected @Element(name="use",required=false) Use _use;
protected @Element(name="export",required=false) Export _export;
protected @ElementList(inline=true,required=false) ArrayList<SetViewDeclaration> _setViewDeclarationList = new ArrayList<SetViewDeclaration>();

public Use getUse() { return _use; }
public Export getExport() { return _export; }
public ArrayList<SetViewDeclaration> getSetViewDeclarationList() { return _setViewDeclarationList; }

}







































