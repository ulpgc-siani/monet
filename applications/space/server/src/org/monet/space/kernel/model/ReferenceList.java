package org.monet.space.kernel.model;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

public class ReferenceList extends BaseModelList<Reference> {

	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {
		serializer.startTag("", "referencelist");
		for(Reference reference : this.get().values())
			reference.serializeToXML(serializer, depth);
		serializer.endTag("", "referencelist");
	}

}
