package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.ErrorCode;
import org.monet.space.backservice.core.constants.MessageCode;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.model.Node;

public class ActionSaveNodeParent extends Action {

  public ActionSaveNodeParent(){
  }

  @Override
  public String execute() {
    String id = (String)this.parameters.get(Parameter.ID);
    String parentId = (String)this.parameters.get(Parameter.PARENT);
    Node node, parent;
    
    if (id == null || parentId == null)
       return ErrorCode.WRONG_PARAMETERS;
    
    NodeLayer nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
    node = nodeLayer.loadNode(id);
    parent = nodeLayer.loadNode(parentId);
    nodeLayer.setParentNode(node, parent);

    return MessageCode.NODE_PARENT_SAVED;
  }

}
