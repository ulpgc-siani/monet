package org.monet.v2.metamodel;


import java.util.ArrayList;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

// TaskViewDeclaration
// Declaración la vista de una tarea

@Root(name="view")
public  class TaskViewDeclaration extends ViewDeclaration 
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







































