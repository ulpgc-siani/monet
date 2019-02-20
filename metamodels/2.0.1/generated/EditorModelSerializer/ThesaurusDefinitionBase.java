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

// ThesaurusDefinition
// Declaración que se utiliza para modelar un tesauro

@Root(name="thesaurus")
public  class ThesaurusDefinitionBase extends EntityDefinition 
 {
@Root(name = "external"
)
public static class External {
protected @Attribute(name="provider") String _provider;
public String getProvider() { return _provider; }
public void setProvider(String value) { _provider = value; }
}
@Root(name = "use"
)
public static class Use {
protected @Attribute(name="reference") String _reference;
protected @Attribute(name="key") String _key;
protected @Attribute(name="parent") String _parent;
protected @Attribute(name="label") String _label;
public String getReference() { return _reference; }
public void setReference(String value) { _reference = value; }
public String getKey() { return _key; }
public void setKey(String value) { _key = value; }
public String getParent() { return _parent; }
public void setParent(String value) { _parent = value; }
public String getLabel() { return _label; }
public void setLabel(String value) { _label = value; }
}

protected @Element(name="external",required=false) External _external;
protected @Element(name="use",required=false) Use _use;
protected @ElementList(inline=true,required=false) ArrayList<TermIndexDeclaration> _termIndexDeclarationList = new ArrayList<TermIndexDeclaration>();

public External getExternal() { return _external; }
public void setExternal(External value) { _external = value; }
public Use getUse() { return _use; }
public void setUse(Use value) { _use = value; }
public ArrayList<TermIndexDeclaration> getTermIndexDeclarationList() { return _termIndexDeclarationList; }

}







































