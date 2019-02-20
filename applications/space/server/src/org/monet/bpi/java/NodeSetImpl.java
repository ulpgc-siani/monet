package org.monet.bpi.java;

import org.monet.bpi.Expression;
import org.monet.bpi.NodeSet;
import org.monet.space.kernel.model.wrappers.NodeSetWrapper;

import java.util.Map;

public abstract class NodeSetImpl extends NodeImpl implements NodeSet {

	@Override
	public long getCount(Expression where) {
		return new NodeSetIterableImpl(this.node, where, null).getTotalCount();
	}

	@Override
	public Map<String, String> getParameters() {
		return new NodeSetWrapper(node).getFilterParameters();
	}

	@Override
	public void addParameter(String name, String value) {
		new NodeSetWrapper(node).addFilterParameter(name, value);
	}

	@Override
	public void deleteParameter(String name) {
		new NodeSetWrapper(node).deleteFilterParameter(name);
	}
}
