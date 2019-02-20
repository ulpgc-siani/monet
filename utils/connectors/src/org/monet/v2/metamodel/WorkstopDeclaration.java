package org.monet.v2.metamodel;


// WorkstopDeclaration
// Declaración que se utiliza para modelar un	workstop de un workline

public class WorkstopDeclaration extends WorkstopDeclarationBase {

  public String getLabel() {
    return this.getLabel("es");
  }

}
