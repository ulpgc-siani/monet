package org.monet.metamodel;

/**
 * TimerLockProperty Esta propiedad se utiliza para añadir un bloqueo de
 * temporizador Estos bloqueos se resuelven automáticamente al cabo del tiempo
 * indicado
 */

public class TimerLockProperty extends WorkLockProperty {

  public static class TimerProperty {
    protected Long _delay;

    public Long getDelay() {
      return _delay;
    }

    public void setDelay(Long value) {
      _delay = value;
    }

    protected Long _extend;

    public Long getExtend() {
      return _extend;
    }

    public void setExtend(Long value) {
      _extend = value;
    }

    protected void merge(TimerProperty child) {
      if (child._delay != null)
        this._delay = child._delay;
      if (child._extend != null)
        this._extend = child._extend;
    }
  }

  protected TimerProperty _timerProperty;

  public TimerProperty getTimer() {
    return _timerProperty;
  }

  public void setTimer(TimerProperty value) {
    if (_timerProperty != null)
      _timerProperty.merge(value);
    else {
      _timerProperty = value;
    }
  }

  public void merge(TimerLockProperty child) {
    super.merge(child);

    if (_timerProperty == null)
      _timerProperty = child._timerProperty;
    else {
      _timerProperty.merge(child._timerProperty);
    }

  }

  public Class<?> getMetamodelClass() {
    return TimerLockProperty.class;
  }

}
