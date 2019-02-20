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

// IndicatorDeclaration
// TODO

@Root(name="indicator")
public  class IndicatorDeclaration extends IndexedDeclaration 
 {
@Root(name="label")
public static class Label {
protected @Attribute(name="language") String _language;
protected @Text(data = true) String content;
public String getLanguage() { return _language; }
public String getContent() { return content; }
}
@Root(name="is-hidden")
public static class IsHidden {
}
@Root(name="data")
public static class Data {
public enum OperatorEnumeration { SUM,AVERAGE,COUNT,MAX,MIN }
protected @Attribute(name="attribute") String _attribute;
protected @Attribute(name="operator") OperatorEnumeration _operator;
public String getAttribute() { return _attribute; }
public OperatorEnumeration getOperator() { return _operator; }
}

  protected @ElementMap(entry="label",key="language",attribute=true,inline=true,required=false) Map<String,String> _labelMap = new HashMap<String,String>();  
protected @Element(name="is-hidden",required=false) IsHidden _isHidden;
protected @Element(name="data",required=false) Data _data;
protected @Element(name="formula",required=false) FormulaDeclaration _formulaDeclaration;

public String getLabel(String language) { if(_labelMap.get(language) == null) return ""; return _labelMap.get(language); }
public Collection<String> getLabels() { return _labelMap.values(); }
public boolean isHidden() { return (_isHidden != null); }
public IsHidden getIsHidden() { return _isHidden; }
public Data getData() { return _data; }
public FormulaDeclaration getFormulaDeclaration() { return _formulaDeclaration; }

}







































