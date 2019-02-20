package org.monet.metamodel;

import java.util.LinkedHashMap;

/**
 * WorkLockProperty Declaración del tipo abstracto de un bloqueo de un workline
 */

public abstract class WorkLockPropertyBase extends ReferenceableProperty {

  protected Object _label;

  public Object getLabel() {
    return _label;
  }

  public void setLabel(Object value) {
    _label = value;
  }

  public static class IsMain {
    protected void merge(IsMain child) {
    }
  }

  protected IsMain _isMain;

  public boolean isMain() {
    return (_isMain != null);
  }

  public IsMain getIsMain() {
    return _isMain;
  }

  public void setIsMain(boolean value) {
    if (value)
      _isMain = new IsMain();
    else {
      _isMain = null;
    }
  }

  public static class WorkstopPropertyBase {
    protected String _code;

    public String getCode() {
      return _code;
    }

    public void setCode(String value) {
      _code = value;
    }

    protected String _name;

    public String getName() {
      return _name;
    }

    public void setName(String value) {
      _name = value;
    }

    protected org.monet.metamodel.internal.Ref _workplace;

    public org.monet.metamodel.internal.Ref getWorkplace() {
      return _workplace;
    }

    public void setWorkplace(org.monet.metamodel.internal.Ref value) {
      _workplace = value;
    }

    protected Object _label;

    public Object getLabel() {
      return _label;
    }

    public void setLabel(Object value) {
      _label = value;
    }

    protected void merge(WorkstopPropertyBase child) {
      if (child._code != null)
        this._code = child._code;
      if (child._name != null)
        this._name = child._name;
      if (child._workplace != null)
        this._workplace = child._workplace;
      if (child._label != null)
        this._label = child._label;
    }
  }

  protected LinkedHashMap<String, WorkLockProperty.WorkstopProperty> _workstopPropertyMap = new LinkedHashMap<String, WorkLockProperty.WorkstopProperty>();

  public void addWorkstop(WorkLockProperty.WorkstopProperty value) {
    String key = value.getName() != null ? value.getName() : value.getCode();
    WorkLockProperty.WorkstopProperty current = _workstopPropertyMap.get(key);
    if (current != null) {
      current.merge(value);
    } else {
      _workstopPropertyMap.put(key, value);
    }
  }

  public java.util.Map<String, WorkLockProperty.WorkstopProperty> getWorkstopMap() {
    return _workstopPropertyMap;
  }

  public java.util.Collection<WorkLockProperty.WorkstopProperty> getWorkstopList() {
    return _workstopPropertyMap.values();
  }

  public void merge(WorkLockPropertyBase child) {
    super.merge(child);

    if (child._label != null)
      this._label = child._label;

    if (_isMain == null)
      _isMain = child._isMain;
    else {
      _isMain.merge(child._isMain);
    }
    for (WorkLockProperty.WorkstopProperty item : child._workstopPropertyMap.values())
      this.addWorkstop(item);

  }

  public Class<?> getMetamodelClass() {
    return WorkLockPropertyBase.class;
  }

}
