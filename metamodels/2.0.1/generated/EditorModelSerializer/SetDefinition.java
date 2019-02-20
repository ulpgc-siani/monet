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

// SetDefinition
// Declaración que se utiliza para modelar un conjunto de datos

public abstract class SetDefinition extends NodeDefinition 
 {
@Root(name = "use"
)
public static class Use {
protected @Attribute(name="reference",required=false) String _reference;
public String getReference() { return _reference; }
public void setReference(String value) { _reference = value; }
}
@Root(name = "export"
)
public static class Export {
public enum TypeEnumeration { MAP,THESAURO }
protected @Attribute(name="name",required=false) String _name;
protected @Attribute(name="type",required=false) TypeEnumeration _type;
protected @Attribute(name="key",required=false) String _key;
public String getName() { return _name; }
public void setName(String value) { _name = value; }
public TypeEnumeration getType() { return _type; }
public void setType(TypeEnumeration value) { _type = value; }
public String getKey() { return _key; }
public void setKey(String value) { _key = value; }
}

protected @Element(name="use",required=false) Use _use;
protected @Element(name="export",required=false) Export _export;
protected @ElementList(inline=true,required=false) ArrayList<SetViewDeclaration> _setViewDeclarationList = new ArrayList<SetViewDeclaration>();

public Use getUse() { return _use; }
public void setUse(Use value) { _use = value; }
public Export getExport() { return _export; }
public void setExport(Export value) { _export = value; }
public ArrayList<SetViewDeclaration> getSetViewDeclarationList() { return _setViewDeclarationList; }

}







































