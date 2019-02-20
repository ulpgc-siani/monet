package org.monet.bpi.java;

import org.monet.bpi.Properties;
import org.monet.metamodel.internal.DescriptorDefinition;

public class PropertiesImpl extends IndexEntryImpl implements Properties {

	@Override
	public String getLabel() {
		return this.getAttribute(DescriptorDefinition.ATTRIBUTE_LABEL);
	}

	@Override
	public void setLabel(String value) {
		this.setAttribute(DescriptorDefinition.ATTRIBUTE_LABEL, value);
	}

	@Override
	public String getDescription() {
		return this.getAttribute(DescriptorDefinition.ATTRIBUTE_DESCRIPTION);
	}

	@Override
	public void setDescription(String value) {
		this.setAttribute(DescriptorDefinition.ATTRIBUTE_DESCRIPTION, value);
	}

	@Override
	public String getColor() {
		return this.getAttribute(DescriptorDefinition.ATTRIBUTE_COLOR);
	}

	@Override
	public void setColor(String value) {
		this.setAttribute(DescriptorDefinition.ATTRIBUTE_COLOR, value);
	}

}
