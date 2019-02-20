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

// SectionFieldViewDeclaration
// Declaración que se utiliza para modelar la vista de un	campo sección

@Root(name="view")
public  class SectionFieldViewDeclaration extends FieldViewDeclaration 
 {
public enum TypeEnumeration { LIST,TABLE }
@Root(name = "rows"
)
public static class Rows {
protected @Attribute(name="count",required=false) int _count;
public int getCount() { return _count; }
public void setCount(int value) { _count = value; }
}
@Root(name = "column"
)
public static class Column {
protected @Attribute(name="field",required=false) String _field;
protected @Attribute(name="width",required=false) int _width;
public String getField() { return _field; }
public void setField(String value) { _field = value; }
public int getWidth() { return _width; }
public void setWidth(int value) { _width = value; }
}
@Root(name = "show"
)
public static class Show {
protected @Attribute(name="field") String _field;
public String getField() { return _field; }
public void setField(String value) { _field = value; }
}

protected @Attribute(name="type",required=false) TypeEnumeration _type;
protected @Element(name="rows",required=false) Rows _rows;
protected @ElementList(inline=true,required=false) ArrayList<Column> _columnList = new ArrayList<Column>();
protected @ElementList(inline=true,required=false) ArrayList<Show> _showList = new ArrayList<Show>();

public TypeEnumeration getType() { return _type; }
public void setType(TypeEnumeration value) { _type = value; }
public Rows getRows() { return _rows; }
public void setRows(Rows value) { _rows = value; }
public ArrayList<Column> getColumnList() { return _columnList; }
public ArrayList<Show> getShowList() { return _showList; }

}







































