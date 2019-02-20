package org.monet.space.kernel.model;

import org.monet.space.kernel.utils.Resources;
import org.monet.space.kernel.utils.StreamHelper;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.EventObject;

public class InternalComponentInfo extends ComponentInfo implements ILoadListener {

	public static final String INTERNAL_COMPONENT_PATH = "/kernel/components/";

	public InternalComponentInfo(String code) {
		super(code);

	}

	private void loadInfo() {
		InputStream stream = null;
		try {
			stream = Resources.getAsStream(INTERNAL_COMPONENT_PATH + this.code + "/component.xml");
			InputStreamReader reader = new InputStreamReader(stream);
			this.deserializeFromXML(reader);
		} finally {
			StreamHelper.close(stream);
		}
	}

	public void loadAttribute(EventObject eventObject, String attribute) {
		this.loadInfo();
	}
}
