package org.monet.v2.metamodel;


// TaskDefinition
// Declaración que se utiliza para modelar una tarea

public class TaskDefinition extends TaskDefinitionBase {

  public boolean isPublic() {
    return this._isPrivate == null;
  }

  public String getWorkmapDeclarationLabel(String codePosition) {

    WorklineDeclaration worklineDeclaration = this._workmapDeclaration.getWorkline(codePosition);
    if (worklineDeclaration != null)
      return worklineDeclaration.getLabel();

    WorkstopDeclaration workstopDeclaration = this._workmapDeclaration.getWorkstop(codePosition);
    if (workstopDeclaration != null)
      return workstopDeclaration.getLabel();

    return "";
  }

}
