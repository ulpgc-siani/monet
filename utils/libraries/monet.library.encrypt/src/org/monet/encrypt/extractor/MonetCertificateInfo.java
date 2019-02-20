package org.monet.encrypt.extractor;

public class MonetCertificateInfo {
  private String type;
  private String federation;
  private String businessUnit;
  
  public static enum Type {
    federation,
    business_unit
  }

  public MonetCertificateInfo(String type) {
    this.type = type.toLowerCase();
    this.federation = null;
    this.businessUnit = null;
  }

  public String getType() {
    return this.type;
  }

  public String getFederation() {
    return this.federation;
  }

  public void setFederation(String federation) {
    this.federation = federation;
  }

  public String getBusinessUnit() {
    return this.businessUnit;
  }
  
  public void setBusinessUnit(String businessUnit) {
    this.businessUnit = businessUnit;
  }
  
  public boolean isFederationCertificate() {
    return this.type.equals(Type.federation.toString());
  }
  
  public boolean isBusinessUnitCertificate() {
    return this.type.equals(Type.business_unit.toString());
  }
}
