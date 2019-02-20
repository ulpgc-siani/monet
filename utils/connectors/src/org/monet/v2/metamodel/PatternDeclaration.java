package org.monet.v2.metamodel;


import java.util.ArrayList;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

// PatternDeclaration
// Declaración que se utiliza para modelar el hecho de un cubo

@Root(name="pattern")
public  class PatternDeclaration extends Declaration 
 {
@Root(name="meta")
public static class Meta {
protected @Attribute(name="index",required=false) int _index;
protected @Attribute(name="indicator",required=false) String _indicator;
public int getIndex() { return _index; }
public String getIndicator() { return _indicator; }
}

protected @Attribute(name="language") String _language;
protected @Attribute(name="regexp") String _regexp;
protected @ElementList(inline=true,required=false) ArrayList<Meta> _metaList = new ArrayList<Meta>();

public String getLanguage() { return _language; }
public String getRegexp() { return _regexp; }
public ArrayList<Meta> getMetaList() { return _metaList; }

}







































