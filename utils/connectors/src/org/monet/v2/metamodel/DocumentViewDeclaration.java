package org.monet.v2.metamodel;


import java.util.ArrayList;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

// DocumentViewDeclaration

@Root(name="view")
public  class DocumentViewDeclaration extends NodeViewDeclaration 
 {
@Root(name="use")
public static class Use {
protected @Attribute(name="template",required=false) String _template;
public String getTemplate() { return _template; }
}

protected @ElementList(inline=true,required=false) ArrayList<Use> _useList = new ArrayList<Use>();

public ArrayList<Use> getUseList() { return _useList; }

}







































