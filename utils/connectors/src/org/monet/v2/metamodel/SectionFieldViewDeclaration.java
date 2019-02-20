package org.monet.v2.metamodel;


import java.util.ArrayList;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

// SectionFieldViewDeclaration
// Declaración que se utiliza para modelar la vista de un	campo sección

@Root(name="view")
public  class SectionFieldViewDeclaration extends FieldViewDeclaration 
 {
public enum TypeEnumeration { LIST,TABLE }
@Root(name="rows")
public static class Rows {
protected @Attribute(name="count",required=false) int _count;
public int getCount() { return _count; }
}
@Root(name="column")
public static class Column {
protected @Attribute(name="field",required=false) String _field;
protected @Attribute(name="width",required=false) int _width;
public String getField() { return _field; }
public int getWidth() { return _width; }
}
@Root(name="show")
public static class Show {
protected @Attribute(name="field") String _field;
public String getField() { return _field; }
}

protected @Attribute(name="type",required=false) TypeEnumeration _type;
protected @Element(name="rows",required=false) Rows _rows;
protected @ElementList(inline=true,required=false) ArrayList<Column> _columnList = new ArrayList<Column>();
protected @ElementList(inline=true,required=false) ArrayList<Show> _showList = new ArrayList<Show>();

public TypeEnumeration getType() { return _type; }
public Rows getRows() { return _rows; }
public ArrayList<Column> getColumnList() { return _columnList; }
public ArrayList<Show> getShowList() { return _showList; }

}







































