package org.monet.v2.metamodel;


import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

// PictureFieldDeclaration
// Declaración que se utiliza para modelar un campo de imagen

@Root(name="field-picture")
public  class PictureFieldDeclaration extends MultipleableFieldDeclaration 
 {
@Root(name="size")
public static class Size {
protected @Attribute(name="width") int _width;
protected @Attribute(name="height") int _height;
public int getWidth() { return _width; }
public int getHeight() { return _height; }
}

protected @Element(name="size",required=false) Size _size;

public Size getSize() { return _size; }

}







































