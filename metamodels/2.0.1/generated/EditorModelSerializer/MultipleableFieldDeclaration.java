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

// MultipleableFieldDeclaration
// Declaración que se utiliza para modelar un campo vínculo

public abstract class MultipleableFieldDeclaration extends FieldDeclaration 
 {
@Root(name = "is-multiple"
)
public static class IsMultiple {
}

protected @Element(name="is-multiple",required=false) IsMultiple _isMultiple;

public boolean isMultiple() { return (_isMultiple != null); }
public void setIsMultiple(boolean value) { if(value) _isMultiple = new IsMultiple(); else _isMultiple = null;}

}







































