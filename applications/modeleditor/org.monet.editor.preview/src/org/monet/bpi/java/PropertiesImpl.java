package org.monet.bpi.java;

import org.monet.bpi.Properties;
import org.monet.metamodel.internal.DescriptorDefinition;

public class PropertiesImpl extends IndexEntryImpl implements Properties {

  @Override
  public String getLabel() {
    return null;
  }

  @Override
  public String getDescription() {
    return null;
  }

  @Override
  public void setLabel(String value) {
    this.setAttribute(DescriptorDefinition.ATTRIBUTE_LABEL, value);
  }

  @Override
  public void setDescription(String value) {
    this.setAttribute(DescriptorDefinition.ATTRIBUTE_DESCRIPTION, value);
  }

}
