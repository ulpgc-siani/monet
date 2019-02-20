package org.monet.federation.accountoffice.core.model;

public class MonetCertificateInfo {
  private String type;
  private String federation;
  private String businessUnit;
  
  public static enum Type {
    federation,
    business_unit
  }
  
  public MonetCertificateInfo() {
    this.type = null;
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

  public void fromExtractor(org.monet.encrypt.extractor.MonetCertificateInfo extractorInfo) {
    this.type = extractorInfo.getType().toLowerCase();
    this.federation = extractorInfo.getFederation();
    this.businessUnit = extractorInfo.getBusinessUnit();
  }

}