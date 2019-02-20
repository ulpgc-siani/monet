package org.monet.federation.accountoffice.core.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "service")
public class Service extends NamedObject {
  private String businessUnitId;
  private @Element(name = "label") String label;
  private @Attribute(name = "ontology") String ontology;
  private @Attribute(name = "enable", required=false) boolean enable = false;

  public String getBusinessUnitId() {
    return this.businessUnitId;
  }
  
  public void setBusinessUnitId(String id) {
    this.businessUnitId = id;
  }

  public String getLabel() {
    return this.label;
  }

  public void setLabel(String value) {
    this.label = value;
  }

  public String getOntology() {
    return this.ontology;
  }

  public void setOntology(String value) {
    this.ontology = value;
  }

  public boolean isEnable() {
    return this.enable;
  }

  public void setIsEnable(boolean value) {
    this.enable = value;
  }
}
