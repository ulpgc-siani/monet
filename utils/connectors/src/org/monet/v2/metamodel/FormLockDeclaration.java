package org.monet.v2.metamodel;


import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

// FormLockDeclaration
// Declaración de un bloqueo con formulario. Estos bloqueos se resuelven cuando el usuario rellena el formulario

@Root(name="form-lock")
public  class FormLockDeclaration extends WorklockDeclaration 
 {
@Root(name="use")
public static class Use {
protected @Attribute(name="form") String _form;
protected @Attribute(name="view",required=false) String _view;
public String getForm() { return _form; }
public String getView() { return _view; }
}

protected @Element(name="use") Use _use;

public Use getUse() { return _use; }

}







































