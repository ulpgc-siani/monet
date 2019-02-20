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

// ImporterDefinition
// Definición de un importador de datos que permite la lectura y proceso de datos externos

@Root(name="importer")
public  class ImporterDefinition extends Definition 
 {
@Root(name="is-singleton")
public static class IsSingleton {
}
@Root(name="source")
public static class Source {
protected @Attribute(name="file",required=false) String _file;
protected @Attribute(name="url",required=false) String _url;
public String getFile() { return _file; }
public String getUrl() { return _url; }
}
@Root(name="schema")
public static class Schema {
protected @Attribute(name="tag") String _tag;
protected @Text(data = true) String content;
public String getTag() { return _tag; }
public String getContent() { return content; }
}

protected @Element(name="is-singleton",required=false) IsSingleton _isSingleton;
protected @Element(name="source",required=false) Source _source;
protected @Element(name="schema",required=false) Schema _schema;

public boolean isSingleton() { return (_isSingleton != null); }
public IsSingleton getIsSingleton() { return _isSingleton; }
public Source getSource() { return _source; }
public Schema getSchema() { return _schema; }

}







































