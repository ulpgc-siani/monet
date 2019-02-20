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

// ThesaurusDefinition
// Declaración que se utiliza para modelar un tesauro

@Root(name="thesaurus")
public  class ThesaurusDefinitionBase extends EntityDefinition 
 {
@Root(name="external")
public static class External {
protected @Attribute(name="provider") String _provider;
public String getProvider() { return _provider; }
}
@Root(name="use")
public static class Use {
protected @Attribute(name="reference") String _reference;
protected @Attribute(name="key") String _key;
protected @Attribute(name="parent") String _parent;
protected @Attribute(name="label") String _label;
public String getReference() { return _reference; }
public String getKey() { return _key; }
public String getParent() { return _parent; }
public String getLabel() { return _label; }
}

protected @Element(name="external",required=false) External _external;
protected @Element(name="use",required=false) Use _use;
protected @ElementList(inline=true,required=false) ArrayList<TermIndexDeclaration> _termIndexDeclarationList = new ArrayList<TermIndexDeclaration>();

public External getExternal() { return _external; }
public Use getUse() { return _use; }
public ArrayList<TermIndexDeclaration> getTermIndexDeclarationList() { return _termIndexDeclarationList; }

}







































