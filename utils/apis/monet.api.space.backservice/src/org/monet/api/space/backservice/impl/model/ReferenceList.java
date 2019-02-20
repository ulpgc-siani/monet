package org.monet.api.space.backservice.impl.model;

import org.jdom.Element;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.text.ParseException;
import java.util.Iterator;
import java.util.List;

public class ReferenceList extends BaseModelList<Reference> {
  
  public void serializeToXML(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
    serializer.startTag("", "referencelist");
    for(Reference reference : this.get().values())
      reference.serializeToXML(serializer);
    serializer.endTag("", "referencelist");
  }

  @SuppressWarnings("unchecked")
  public void deserializeFromXML(Element nodeList) throws ParseException {
    List<Element> nodes;
    Iterator<Element> iterator;
    
    if (nodeList == null) return;
    
    nodes = nodeList.getChildren("reference");
    iterator = nodes.iterator();

    this.clear();

    while (iterator.hasNext()) {
      Reference reference = new Reference();
      reference.deserializeFromXML(iterator.next());
      if (reference.getId().equals("-1")) reference.setId(String.valueOf(this.items.size()+1)); 
      this.add(reference);
    }
    
  }

  
}
