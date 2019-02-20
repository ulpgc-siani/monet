package org.monet.metamodel;

/**
 * SyncLockProperty Esta propiedad se utiliza para añadir un bloqueo de
 * sincronización de tareas Estos bloqueos se resuelven automáticamente cuando
 * una tarea termina
 */

public class SyncLockProperty extends WorkLockProperty {

  public static class WaitProperty {
    protected org.monet.metamodel.internal.Ref _task;

    public org.monet.metamodel.internal.Ref getTask() {
      return _task;
    }

    public void setTask(org.monet.metamodel.internal.Ref value) {
      _task = value;
    }

    protected void merge(WaitProperty child) {
      if (child._task != null)
        this._task = child._task;
    }
  }

  protected WaitProperty _waitProperty;

  public WaitProperty getWait() {
    return _waitProperty;
  }

  public void setWait(WaitProperty value) {
    if (_waitProperty != null)
      _waitProperty.merge(value);
    else {
      _waitProperty = value;
    }
  }

  public void merge(SyncLockProperty child) {
    super.merge(child);

    if (_waitProperty == null)
      _waitProperty = child._waitProperty;
    else {
      _waitProperty.merge(child._waitProperty);
    }

  }

  public Class<?> getMetamodelClass() {
    return SyncLockProperty.class;
  }

}
