package org.monet.space.kernel.model;

import net.minidev.json.JSONObject;

public class FeederRole extends Role {
	private String partnerId;
	private FederationUnit partner;
	private String partnerFeederName;
	private FederationUnitFeeder partnerFeeder;

	public String getLabel() {
		return this.getPartner().getLabel() + " - " + this.getPartnerFeeder().getLabel();
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
		this.partnerFeeder = null;
		this.removeLoadedAttribute(Role.PARTNER);
	}

	public void setPartner(FederationUnit partner) {
		this.partner = partner;
		this.addLoadedAttribute(Role.PARTNER);
	}

	public String getPartnerFeederName() {
		return this.partnerFeederName;
	}

	public FederationUnitFeeder getPartnerFeeder() {
		onLoad(this, Role.PARTNER);
		return this.partnerFeeder;
	}

	public void setPartnerFeederName(String name) {
		this.partnerFeederName = name;
		this.partner = null;
		this.partnerFeeder = null;
		this.removeLoadedAttribute(Role.PARTNER);
	}

	public void setPartnerFeeder(FederationUnitFeeder partnerFeeder) {
		this.partnerFeeder = partnerFeeder;
		this.addLoadedAttribute(Role.PARTNER);
	}

	@Override
	public void addJsonAttributes(JSONObject result) {
		result.put("type", Type.Feeder.toString().toLowerCase());
		result.put("partnerId", this.partnerId);
		result.put("partnerFeederName", this.partnerFeederName);
	}

	public String getPartnerName() {
		return this.getPartner().getName();
	}

	public String getPartnerFeederUrl() {
		FederationUnit partner = this.getPartner();
        return partner.getFeederUrl(this.partnerFeederName);
	}

}
