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

// ImporterDefinition
// Definición de un importador de datos que permite la lectura y proceso de datos externos

@Root(name="importer")
public  class ImporterDefinition extends Definition 
 {
@Root(name = "is-singleton"
)
public static class IsSingleton {
}
@Root(name = "source"
)
public static class Source {
protected @Attribute(name="file",required=false) String _file;
protected @Attribute(name="url",required=false) String _url;
public String getFile() { return _file; }
public void setFile(String value) { _file = value; }
public String getUrl() { return _url; }
public void setUrl(String value) { _url = value; }
}
@Root(name = "schema"
,strict = false
)
public static class Schema {
protected @Attribute(name="tag") String _tag;
protected @Text(data = true) String content;
public String getTag() { return _tag; }
public void setTag(String value) { _tag = value; }
public String getContent() { return content; }
public void setContent(String content) { this.content = content; }
}

protected @Element(name="is-singleton",required=false) IsSingleton _isSingleton;
protected @Element(name="source",required=false) Source _source;
protected @Element(name="schema",required=false) Schema _schema;

public boolean isSingleton() { return (_isSingleton != null); }
public void setIsSingleton(boolean value) { if(value) _isSingleton = new IsSingleton(); else _isSingleton = null;}
public Source getSource() { return _source; }
public void setSource(Source value) { _source = value; }
public Schema getSchema() { return _schema; }
public void setSchema(Schema value) { _schema = value; }

}







































