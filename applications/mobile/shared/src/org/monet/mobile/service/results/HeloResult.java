package org.monet.mobile.service.results;

import org.monet.mobile.service.Result;

public class HeloResult extends Result {

  private String businessUnit;
  private String businessUnitLabel;
  private String federation;
  private String federationUrl;
  private String title;
  private String subtitle;


  public HeloResult(){}
  
  public HeloResult(String businessUnit, String businessUnitLabel, String federation, String federationUrl, String title, String subtitle) {
    this.businessUnit = businessUnit;
    this.businessUnitLabel = businessUnitLabel;
    this.federation = federation;
    this.federationUrl = federationUrl;
  }
  
  public void setBusinessUnit(String businessUnit) {
    this.businessUnit = businessUnit;
  }
  
  public String getBusinessUnit() {
    return businessUnit;
  }

  public void setBusinessUnitLabel(String businessUnitLabel) {
    this.businessUnitLabel = businessUnitLabel;
  }

  public String getBusinessUnitLabel() {
    return businessUnitLabel;
  }

  public String getFederation() {
    return federation;
  }

  public void setFederation(String federation) {
    this.federation = federation;
  }
  
  public void setFederationUrl(String federationUrl) {
    this.federationUrl = federationUrl;
  }
  
  public String getFederationUrl() {
    return federationUrl;
  }

  public String getTitle() {
    return title;
  }

  public HeloResult setTitle(String title) {
    this.title = title;
    return this;
  }

  public String getSubtitle() {
    return subtitle;
  }

  public void setSubtitle(String subtitle) {
    this.subtitle = subtitle;
  }
}
