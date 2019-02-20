package org.monet.v2.metamodel;


// FieldDeclaration
// Declaración abstracta que se utiliza para  modelar un campo de un formulario

public abstract class FieldDeclaration extends FieldDeclarationBase {

  public String getLabel() {
    return this.getLabel("es");
  }

  public boolean isBoolean() {
    return this instanceof BooleanFieldDeclaration;
  }

  public boolean isCheck() {
    return this instanceof CheckFieldDeclaration;
  }

  public boolean isDate() {
    return this instanceof DateFieldDeclaration;
  }

  public boolean isFile() {
    return this instanceof FileFieldDeclaration;
  }

  public boolean isLink() {
    return this instanceof LinkFieldDeclaration;
  }

  public boolean isLocation() {
    return this instanceof LocationFieldDeclaration;
  }

  public boolean isMemo() {
    return this instanceof MemoFieldDeclaration;
  }

  public boolean isNode() {
    return this instanceof NodeFieldDeclaration;
  }

  public boolean isNumber() {
    return this instanceof NumberFieldDeclaration;
  }

  public boolean isPattern() {
    return this instanceof PatternFieldDeclaration;
  }

  public boolean isPicture() {
    return this instanceof PictureFieldDeclaration;
  }

  public boolean isSection() {
    return this instanceof SectionFieldDeclaration;
  }

  public boolean isSelect() {
    return this instanceof SelectFieldDeclaration;
  }

  public boolean isSerial() {
    return this instanceof SerialFieldDeclaration;
  }

  public boolean isText() {
    return this instanceof TextFieldDeclaration;
  }

  public boolean isMultiple() {
    return false;
  }

}
