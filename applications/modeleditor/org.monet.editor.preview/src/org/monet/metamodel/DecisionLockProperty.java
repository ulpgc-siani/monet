package org.monet.metamodel;

/**
 * DecisionLockProperty Esta propiedad se utiliza para añadir un bloqueo de
 * decisión Estos bloqueos se resuelven cuando el usuario selecciona un workstop
 */

public class DecisionLockProperty extends WorkLockProperty {

  public void merge(DecisionLockProperty child) {
    super.merge(child);

  }

  public Class<?> getMetamodelClass() {
    return DecisionLockProperty.class;
  }

}
