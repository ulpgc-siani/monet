package org.monet.v2.metamodel;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

// MultipleableFieldDeclaration
// Declaración que se utiliza para modelar un campo vínculo

public abstract class MultipleableFieldDeclaration extends FieldDeclaration 
 {
@Root(name="is-multiple")
public static class IsMultiple {
}

protected @Element(name="is-multiple",required=false) IsMultiple _isMultiple;

public boolean isMultiple() { return (_isMultiple != null); }
public IsMultiple getIsMultiple() { return _isMultiple; }

}







































