package org.monet.space.kernel.model;

import net.minidev.json.JSONObject;

public class ServiceRole extends Role {
	private String partnerId = null;
	private FederationUnit partner;
	private String partnerServiceName;
	private FederationUnitService partnerService;

	public String getLabel() {
		FederationUnit partner = this.getPartner();
		FederationUnitService partnerService = this.getPartnerService();
		if (partner == null) return this.getDefinition().getLabelString();
		if (partnerService == null) return this.getDefinition().getLabelString();
		return partner.getLabel() + " - " + partnerService.getLabel();
	}

	public String getPartnerId() {
		return this.partnerId;
	}

	public FederationUnit getPartner() {
		onLoad(this, Role.PARTNER);
		return this.partner;
	}

	public void setPartnerId(String id) {
		this.partnerId = id;
		this.partner = null;
		this.partnerService = null;
		this.removeLoadedAttribute(Role.PARTNER);
	}

	public void setPartner(FederationUnit partner) {
		this.partner = partner;
		this.addLoadedAttribute(Role.PARTNER);
	}

	public String getPartnerServiceName() {
		return this.partnerServiceName;
	}

	public FederationUnitService getPartnerService() {
		onLoad(this, Role.PARTNER);
		return this.partnerService;
	}

	public void setPartnerServiceName(String name) {
		this.partnerServiceName = name;
		this.partner = null;
		this.partnerService = null;
		this.removeLoadedAttribute(Role.PARTNER);
	}

	public void setPartnerService(FederationUnitService partnerService) {
		this.partnerService = partnerService;
		this.addLoadedAttribute(Role.PARTNER);
	}

	@Override
	public void addJsonAttributes(JSONObject result) {
		result.put("type", Type.Service.toString().toLowerCase());
		result.put("partnerId", this.partnerId);
		result.put("partnerServiceName", this.partnerServiceName);
	}

	public String getPartnerName() {
		return this.getPartner().getName();
	}

	public String getPartnerServiceUrl() {
		FederationUnit partner = this.getPartner();
        return partner.getServiceUrl(this.partnerServiceName);
	}

}
