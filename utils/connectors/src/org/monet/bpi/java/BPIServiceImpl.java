package org.monet.bpi.java;

import org.monet.bpi.BPIBaseNode;
import org.monet.bpi.BPIBehaviorService;
import org.monet.bpi.BPICube;
import org.monet.bpi.BPISchema;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class BPIServiceImpl<Schema extends BPISchema,
	Request extends BPIBaseNode<?>,
	Response extends BPIBaseNode<?>>
	implements BPIBehaviorService<Schema, Request, Response> {

	HashMap<String, String> dynNames = new HashMap<String, String>();
	ArrayList<BPICubeImpl<?>> cubes = new ArrayList<BPICubeImpl<?>>();

	protected void setDynamicName(String key, String value) {
		this.dynNames.put(key, value);
	}

	protected String resolve(String key) {
		return this.dynNames.get(key);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Request> T getRequest() {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Response> T getResponse() {
		return null;
	}

	@SuppressWarnings("unchecked")
	protected <T extends BPICube<?>> T getCube(String cubeName) {
		return null;
	}

}
