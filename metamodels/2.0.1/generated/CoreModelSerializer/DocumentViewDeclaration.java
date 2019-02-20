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







































