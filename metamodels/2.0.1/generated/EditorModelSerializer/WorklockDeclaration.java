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

// WorklockDeclaration
// Declaración que se utiliza para modelar un bloqueo en un workline

public abstract class WorklockDeclaration extends IndexedDeclaration 
 {
@Root(name = "is-main"
)
public static class IsMain {
}
@Root(name = "label"
)
public static class Label {
protected @Attribute(name="language") String _language;
protected @Text(data = true) String content;
public String getLanguage() { return _language; }
public void setLanguage(String value) { _language = value; }
public String getContent() { return content; }
public void setContent(String content) { this.content = content; }
}
@Root(name = "out"
)
public static class Out {
protected @Attribute(name="workstop",required=false) String _workstop;
public String getWorkstop() { return _workstop; }
public void setWorkstop(String value) { _workstop = value; }
}

protected @Element(name="is-main",required=false) IsMain _isMain;
  protected @ElementMap(entry="label",key="language",attribute=true,inline=true,required=false) Map<String,String> _labelMap = new HashMap<String,String>();  
protected @ElementList(inline=true) ArrayList<Out> _outList = new ArrayList<Out>();

public boolean isMain() { return (_isMain != null); }
public void setIsMain(boolean value) { if(value) _isMain = new IsMain(); else _isMain = null;}
public String getLabel(String language) { if(_labelMap.get(language) == null) return ""; return _labelMap.get(language); }
public Collection<String> getLabels() { return _labelMap.values(); }
public ArrayList<Out> getOutList() { return _outList; }

}







































