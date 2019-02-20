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

// ReferenceViewDeclaration
// Declaración de una vista de una referencia

@Root(name="view")
public  class ReferenceViewDeclaration extends ViewDeclaration 
 {
@Root(name = "is-default"
)
public static class IsDefault {
}
@Root(name = "show"
)
public static class Show {
protected @Attribute(name="attribute") String _attribute;
public String getAttribute() { return _attribute; }
public void setAttribute(String value) { _attribute = value; }
}

protected @Element(name="is-default",required=false) IsDefault _isDefault;
protected @ElementList(inline=true,required=false) ArrayList<Show> _showList = new ArrayList<Show>();
protected @ElementList(inline=true,required=false) ArrayList<StyleDeclaration> _styleDeclarationList = new ArrayList<StyleDeclaration>();

public boolean isDefault() { return (_isDefault != null); }
public void setIsDefault(boolean value) { if(value) _isDefault = new IsDefault(); else _isDefault = null;}
public ArrayList<Show> getShowList() { return _showList; }
public ArrayList<StyleDeclaration> getStyleDeclarationList() { return _styleDeclarationList; }

}







































