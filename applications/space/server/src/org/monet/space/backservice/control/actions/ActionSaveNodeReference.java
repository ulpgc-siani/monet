package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.MessageCode;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.library.LibraryEncoding;
import org.monet.space.kernel.model.Node;
import org.monet.space.kernel.model.Reference;

public class ActionSaveNodeReference extends Action {

  public ActionSaveNodeReference(){
  }

  @Override
  public String execute() {
    String id = (String)this.parameters.get(Parameter.ID);
    String code = (String)this.parameters.get(Parameter.CODE);
    String attributesValue = (String)this.parameters.get(Parameter.ATTRIBUTES);
    NodeLayer nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
    Reference reference = new Reference(code);
    Node node;
    
    String[] attributesArray = LibraryEncoding.decode(attributesValue).split(PARAMETER_SEPARATOR);
    for (int i=0; i<attributesArray.length; i++) {
      String[] attributeArray = attributesArray[i].split("=");
      if (attributeArray.length < 2) continue;
      reference.setAttributeValue(attributeArray[0], attributeArray[1]);
    }

    node = nodeLayer.loadNode(id);
    nodeLayer.saveNodeReference(node, reference);
    
    return MessageCode.NODE_REFERENCE_SAVED;
  }

}
