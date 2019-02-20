package org.monet.metamodel;

import java.util.ArrayList;

/**
 * ServiceLockProperty Esta propiedad se utiliza para añadir un bloqueo de
 * servicio Estos bloqueos realizan una petición de servicio y se resuelven
 * automáticamente cuando el servicio termina
 */

public class ServiceLockProperty extends WorkLockProperty {

  public static class WaitProperty {
    protected org.monet.metamodel.internal.Ref _for;

    public org.monet.metamodel.internal.Ref getFor() {
      return _for;
    }

    public void setFor(org.monet.metamodel.internal.Ref value) {
      _for = value;
    }

    protected void merge(WaitProperty child) {
      if (child._for != null)
        this._for = child._for;
    }
  }

  protected ArrayList<WaitProperty> _waitPropertyList = new ArrayList<WaitProperty>();

  public ArrayList<WaitProperty> getWaitList() {
    return _waitPropertyList;
  }

  public void merge(ServiceLockProperty child) {
    super.merge(child);

    _waitPropertyList.addAll(child._waitPropertyList);

  }

  public Class<?> getMetamodelClass() {
    return ServiceLockProperty.class;
  }

}
