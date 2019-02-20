package org.monet.space.kernel.model;

import org.jdom.Element;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class CubeFactList extends MonetList<CubeFact> {
	private static final long serialVersionUID = 1L;

	public CubeFactList() {
		super();
	}

	public void deserializeFromXML(Element cubeFactList) {
		List<Element> cubeFacts;
		Iterator<Element> iterator;

		if (cubeFactList == null) return;

		cubeFacts = cubeFactList.getChildren("cubefact");
		iterator = cubeFacts.iterator();

		this.clear();

		while (iterator.hasNext()) {
			CubeFact cubeFact = new CubeFact(new Date());
			cubeFact.unserializeFromXML(iterator.next());
			this.add(cubeFact);
		}

	}

}