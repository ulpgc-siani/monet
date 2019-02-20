package org.monet.v2.metamodel;


// WorklineDeclaration
// Declaración que se utiliza para modelar un workline de un workmap

public class WorklineDeclaration extends WorklineDeclarationBase {

  public String getLabel() {
    return this.getLabel("es");
  }

  public String getResult() {
    return this.getResult("es");
  }

}
