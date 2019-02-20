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

// FormViewDeclaration

@Root(name="view")
public  class FormViewDeclaration extends NodeViewDeclaration 
 {
@Root(name = "show"
)
public static class Show {
protected @Attribute(name="field") String _field;
public String getField() { return _field; }
public void setField(String value) { _field = value; }
}

protected @ElementList(inline=true,required=false) ArrayList<Show> _showList = new ArrayList<Show>();

public ArrayList<Show> getShowList() { return _showList; }

}







































