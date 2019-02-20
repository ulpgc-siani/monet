package org.monet.v2.metamodel;


import java.util.ArrayList;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

// FormViewDeclaration

@Root(name="view")
public  class FormViewDeclaration extends NodeViewDeclaration 
 {
@Root(name="show")
public static class Show {
protected @Attribute(name="field") String _field;
public String getField() { return _field; }
}

protected @ElementList(inline=true,required=false) ArrayList<Show> _showList = new ArrayList<Show>();

public ArrayList<Show> getShowList() { return _showList; }

}







































