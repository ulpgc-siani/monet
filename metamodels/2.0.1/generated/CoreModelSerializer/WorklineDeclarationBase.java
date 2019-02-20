package org.monet.kernel.model.definition;

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

// WorklineDeclaration
// Declaración que se utiliza para modelar un workline de un workmap

@Root(name="workline")
public  class WorklineDeclarationBase extends IndexedDeclaration 
 {
@Root(name="label")
public static class Label {
protected @Attribute(name="language") String _language;
protected @Text(data = true) String content;
public String getLanguage() { return _language; }
public String getContent() { return content; }
}
@Root(name="result")
public static class Result {
protected @Attribute(name="language") String _language;
protected @Text(data = true) String content;
public String getLanguage() { return _language; }
public String getContent() { return content; }
}
@Root(name="from")
public static class From {
protected @Attribute(name="workplace",required=false) String _workplace;
public String getWorkplace() { return _workplace; }
}

protected @Attribute(name="weight",required=false) int _weight;
  protected @ElementMap(entry="label",key="language",attribute=true,inline=true,required=false) Map<String,String> _labelMap = new HashMap<String,String>();  
  protected @ElementMap(entry="result",key="language",attribute=true,inline=true,required=false) Map<String,String> _resultMap = new HashMap<String,String>();  
protected @Element(name="from",required=false) From _from;
protected @ElementList(inline=true,required=false) ArrayList<WorkstopDeclaration> _workstopDeclarationList = new ArrayList<WorkstopDeclaration>();
protected @ElementList(inline=true,required=false) ArrayList<BranchLockDeclaration> _branchLockDeclarationList = new ArrayList<BranchLockDeclaration>();
protected @ElementList(inline=true,required=false) ArrayList<DecisionLockDeclaration> _decisionLockDeclarationList = new ArrayList<DecisionLockDeclaration>();
protected @ElementList(inline=true,required=false) ArrayList<FormLockDeclaration> _formLockDeclarationList = new ArrayList<FormLockDeclaration>();
protected @ElementList(inline=true,required=false) ArrayList<ServiceLockDeclaration> _serviceLockDeclarationList = new ArrayList<ServiceLockDeclaration>();
protected @ElementList(inline=true,required=false) ArrayList<SyncLockDeclaration> _syncLockDeclarationList = new ArrayList<SyncLockDeclaration>();
protected @ElementList(inline=true,required=false) ArrayList<TimerLockDeclaration> _timerLockDeclarationList = new ArrayList<TimerLockDeclaration>();

public int getWeight() { return _weight; }
public String getLabel(String language) { if(_labelMap.get(language) == null) return ""; return _labelMap.get(language); }
public Collection<String> getLabels() { return _labelMap.values(); }
public String getResult(String language) { if(_resultMap.get(language) == null) return ""; return _resultMap.get(language); }
public Collection<String> getResults() { return _resultMap.values(); }
public From getFrom() { return _from; }
public ArrayList<WorkstopDeclaration> getWorkstopDeclarationList() { return _workstopDeclarationList; }
public ArrayList<BranchLockDeclaration> getBranchLockDeclarationList() { return _branchLockDeclarationList; }
public ArrayList<DecisionLockDeclaration> getDecisionLockDeclarationList() { return _decisionLockDeclarationList; }
public ArrayList<FormLockDeclaration> getFormLockDeclarationList() { return _formLockDeclarationList; }
public ArrayList<ServiceLockDeclaration> getServiceLockDeclarationList() { return _serviceLockDeclarationList; }
public ArrayList<SyncLockDeclaration> getSyncLockDeclarationList() { return _syncLockDeclarationList; }
public ArrayList<TimerLockDeclaration> getTimerLockDeclarationList() { return _timerLockDeclarationList; }
      
    protected ArrayList<WorklockDeclaration> _worklockDeclarationList;
  protected void createWorklockDeclarationList() {
    _worklockDeclarationList = new ArrayList<WorklockDeclaration>();
                  _worklockDeclarationList.addAll(_branchLockDeclarationList);
                _worklockDeclarationList.addAll(_decisionLockDeclarationList);
                _worklockDeclarationList.addAll(_formLockDeclarationList);
                _worklockDeclarationList.addAll(_serviceLockDeclarationList);
                _worklockDeclarationList.addAll(_syncLockDeclarationList);
                _worklockDeclarationList.addAll(_timerLockDeclarationList);
        }
  public ArrayList<WorklockDeclaration> getWorklockDeclarationList() {
    if (_worklockDeclarationList == null) createWorklockDeclarationList();
    return _worklockDeclarationList;
  }
      
    protected HashMap<String,WorklockDeclaration> _worklockDeclarationMap;
  protected void createWorklockDeclarationMap() {
    _worklockDeclarationMap = new HashMap<String,WorklockDeclaration>();
    if (_worklockDeclarationList == null) createWorklockDeclarationList();
    for (WorklockDeclaration item : _worklockDeclarationList){
      _worklockDeclarationMap.put(item.getCode(), item);
      _worklockDeclarationMap.put(item.getName(), item);
    }
  }
  public WorklockDeclaration getWorklockDeclaration(String key) {
    if (_worklockDeclarationMap == null) createWorklockDeclarationMap();
    return _worklockDeclarationMap.get(key);
  }

}







































