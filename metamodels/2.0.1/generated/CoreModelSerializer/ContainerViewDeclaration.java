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

// ContainerViewDeclaration

@Root(name="view")
public  class ContainerViewDeclaration extends NodeViewDeclaration 
 {
@Root(name="show")
public static class Show {
protected @Attribute(name="node") String _node;
protected @Attribute(name="view") String _view;
public String getNode() { return _node; }
public String getView() { return _view; }
}

protected @ElementList(inline=true,required=false) ArrayList<Show> _showList = new ArrayList<Show>();

public ArrayList<Show> getShowList() { return _showList; }

}







































