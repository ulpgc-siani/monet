package org.monet.space.explorer.model;

import com.google.inject.Inject;
import org.monet.space.explorer.control.exceptions.ParentNotFoundException;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.model.Attribute;
import org.monet.space.kernel.model.AttributeList;
import org.monet.space.kernel.model.Node;

// sample: attr1/attr2:1/attr3:0/attr4

public class PathResolver {
	private AgentLogger logger;

	private static final String ATTRIBUTE_SEPARATOR = "/";
	private static final String MULTIPLE_ATTRIBUTE_INDEX_SEPARATOR = ":";

	@Inject
	public void inject(AgentLogger logger) {
		this.logger = logger;
	}

	public Attribute resolveAttribute(Node node, String path) throws ParentNotFoundException {

		if (path == null || path.isEmpty()) {
			this.logger.error("incorrect field path");
			throw new RuntimeException("incorrect field path");
		}

		Attribute attribute = null;
		AttributeList attributeList = node.getAttributeList();
		String[] attributes = path.split(ATTRIBUTE_SEPARATOR);

		for (int i = 0; i < attributes.length; i++) {
			String pathAttribute = attributes[i];
			attribute = resolveAttribute(attributeList, pathAttribute);

			if (attribute == null) {
				if (i != attributes.length - 1) {
					this.logger.error(String.format("attribute not found with path '%s'", path));
					throw new ParentNotFoundException(String.format("attribute not found with path '%s'", path));
				}
				return null;
			}

			attributeList = attribute.getAttributeList();
		}

		return attribute;
	}

	public String parentCode(String path) {
		String[] attributes = path.split(ATTRIBUTE_SEPARATOR);
		return attributes.length >= 2 ? attributes[attributes.length - 2] : null;
	}

	public String lastFieldCode(String path) {
		String[] attributes = path.split(ATTRIBUTE_SEPARATOR);
		return attributes.length == 1 ? attributes[0] : attributes[attributes.length - 1];
	}

	public String getLeafAttributeCode(String path) {
		String[] attributes = path.split(ATTRIBUTE_SEPARATOR);

		if (attributes.length == 0)
			return null;

		return attributes[attributes.length-1];
	}

	private Attribute resolveAttribute(AttributeList attributeList, String attribute) {
		String attributeCode = attribute;
		int pos = 0;
		boolean multiple = false;

		if (attribute.contains(MULTIPLE_ATTRIBUTE_INDEX_SEPARATOR)) {
			attributeCode = attribute.split(MULTIPLE_ATTRIBUTE_INDEX_SEPARATOR)[0];
			pos = Integer.valueOf(attribute.split(MULTIPLE_ATTRIBUTE_INDEX_SEPARATOR)[1]);
			multiple = true;
		}

		Attribute result = attributeList.getFirstByCode(attributeCode);
		if (!multiple)
			return result;

		int current = 0;
		for (Attribute childAttribute : result.getAttributeList()) {
			if (current == pos)
				return childAttribute;
			current++;
		}

		return null;
	}

	public Attribute getParent(Node node, String path) {
		if (path.lastIndexOf(ATTRIBUTE_SEPARATOR) == -1)
			return null;

		return resolveAttribute(node, path.substring(0, path.lastIndexOf(ATTRIBUTE_SEPARATOR)));
	}
}
