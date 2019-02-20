package org.monet.kernel.model.definition;

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







































