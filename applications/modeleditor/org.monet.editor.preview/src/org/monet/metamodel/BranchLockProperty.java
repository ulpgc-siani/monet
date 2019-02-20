package org.monet.metamodel;

/**
 * BranchLockProperty Esta propiedad se utiliza para añadir un bloqueo de desvio
 * Estos bloqueos se resuelven automaticamente por BPI
 */

public class BranchLockProperty extends WorkLockProperty {

  public void merge(BranchLockProperty child) {
    super.merge(child);

  }

  public Class<?> getMetamodelClass() {
    return BranchLockProperty.class;
  }

}
