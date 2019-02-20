package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.MessageCode;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.library.LibraryEncoding;
import org.monet.space.kernel.model.Node;

public class ActionSaveNodePartnerContext extends Action {

  public ActionSaveNodePartnerContext(){
  }

  @Override
  public String execute() {
    String id = (String)this.parameters.get(Parameter.ID);
    String context = (String)this.parameters.get(Parameter.CONTEXT);
    NodeLayer nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
    Node node;

    node = nodeLayer.loadNode(id);
    nodeLayer.saveNodePartnerContext(node, LibraryEncoding.decode(context));
    
    return MessageCode.NODE_CONTEXT_SAVED;
  }

}
