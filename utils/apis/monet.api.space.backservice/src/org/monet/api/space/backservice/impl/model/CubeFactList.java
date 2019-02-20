package org.monet.api.space.backservice.impl.model;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

public class CubeFactList extends BaseModelList<CubeFact> {
	private static final long serialVersionUID = 1L;

	public CubeFactList() {
		super();
	}

	public void serializeToXML(XmlSerializer serializer) throws IllegalArgumentException, IllegalStateException, IOException {
		serializer.startTag("", "cubefactlist");
		for (CubeFact fact : items.values())
			fact.serializeToXML(serializer);
		serializer.endTag("", "cubefactlist");
	}

}