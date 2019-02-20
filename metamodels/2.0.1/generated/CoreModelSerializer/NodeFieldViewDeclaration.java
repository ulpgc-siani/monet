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

// NodeFieldViewDeclaration
// Declaración que se utiliza para modelar la vista de un	campo nodo

@Root(name="view")
public  class NodeFieldViewDeclaration extends FieldViewDeclaration 
 {
public enum TypeEnumeration { LIST,TABLE }
@Root(name="rows")
public static class Rows {
protected @Attribute(name="count",required=false) int _count;
public int getCount() { return _count; }
}

protected @Attribute(name="type",required=false) TypeEnumeration _type;
protected @Element(name="rows",required=false) Rows _rows;

public TypeEnumeration getType() { return _type; }
public Rows getRows() { return _rows; }

}







































