package org.monet.v2.metamodel;


import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

// LinkFieldViewDeclaration
// Declaración para modelar cómo se visualiza un campo de tipo link

@Root(name = "view")
public class LinkFieldViewDeclaration extends FieldViewDeclaration {
  public static class Render {
    protected @Attribute(name = "view")
    String view;

    public String getView() {
      return view;
    }
  }

  protected @Element(name = "render")
  Render render;

  public Render getRender() {
    return render;
  }

}
