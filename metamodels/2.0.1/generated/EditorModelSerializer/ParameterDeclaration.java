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

// ParameterDeclaration
// Declaración que se utiliza para modelar un parámetro de un catálogo/informe

@Root(name="parameter")
public  class ParameterDeclaration extends IndexedDeclaration 
 {
public enum TypeEnumeration { TEXT,NUMBER,DATE,DATE_RANGE,DIMENSION }
public enum PrecisionEnumeration { YEARS,MONTHS,DAYS,HOURS,MINUTES,SECONDS }
@Root(name = "use"
)
public static class Use {
protected @Attribute(name="attribute",required=false) String _attribute;
public String getAttribute() { return _attribute; }
public void setAttribute(String value) { _attribute = value; }
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

protected @Attribute(name="type") TypeEnumeration _type;
protected @Attribute(name="precision",required=false) PrecisionEnumeration _precision;
protected @Attribute(name="dimension",required=false) String _dimension;
protected @Attribute(name="default-value",required=false) String _defaultValue;
protected @Element(name="use",required=false) Use _use;
  protected @ElementMap(entry="label",key="language",attribute=true,inline=true,required=false) Map<String,String> _labelMap = new HashMap<String,String>();  

public TypeEnumeration getType() { return _type; }
public void setType(TypeEnumeration value) { _type = value; }
public PrecisionEnumeration getPrecision() { return _precision; }
public void setPrecision(PrecisionEnumeration value) { _precision = value; }
public String getDimension() { return _dimension; }
public void setDimension(String value) { _dimension = value; }
public String getDefaultValue() { return _defaultValue; }
public void setDefaultValue(String value) { _defaultValue = value; }
public Use getUse() { return _use; }
public void setUse(Use value) { _use = value; }
public String getLabel(String language) { if(_labelMap.get(language) == null) return ""; return _labelMap.get(language); }
public Collection<String> getLabels() { return _labelMap.values(); }

}







































