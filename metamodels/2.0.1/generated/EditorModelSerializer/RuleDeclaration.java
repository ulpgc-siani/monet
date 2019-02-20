package org.monet.modelling.kernel.model;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;

import org.simpleframework.xml.Root;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;

// RuleDeclaration
// Declaración abstracta que se utiliza para  modelar una regla de un nodo

@Root(name="rule")
public  class RuleDeclaration  
 {
@Root(name = "condition"
)
public static class Condition {
public enum CheckEnumeration { EQ,GT,GE,LT,LE,NEQ,EMPTY }
protected @Attribute(name="field") String _field;
protected @Attribute(name="check") CheckEnumeration _check;
protected @Attribute(name="value",required=false) String _value;
public String getField() { return _field; }
public void setField(String value) { _field = value; }
public CheckEnumeration getCheck() { return _check; }
public void setCheck(CheckEnumeration value) { _check = value; }
public String getValue() { return _value; }
public void setValue(String value) { _value = value; }
}
@Root(name = "action"
)
public static class Action {
public enum NameEnumeration { SHOW,HIDE,LOCK,UNLOCK,EXPAND,COLLAPSE }
protected @Attribute(name="name") NameEnumeration _name;
protected @Attribute(name="field",required=false) String _field;
protected @Attribute(name="view",required=false) String _view;
protected @Attribute(name="operation",required=false) String _operation;
public NameEnumeration getName() { return _name; }
public void setName(NameEnumeration value) { _name = value; }
public String getField() { return _field; }
public void setField(String value) { _field = value; }
public String getView() { return _view; }
public void setView(String value) { _view = value; }
public String getOperation() { return _operation; }
public void setOperation(String value) { _operation = value; }
}

protected @ElementList(inline=true,required=false) ArrayList<Condition> _conditionList = new ArrayList<Condition>();
protected @ElementList(inline=true,required=false) ArrayList<Action> _actionList = new ArrayList<Action>();

public ArrayList<Condition> getConditionList() { return _conditionList; }
public ArrayList<Action> getActionList() { return _actionList; }

}







































