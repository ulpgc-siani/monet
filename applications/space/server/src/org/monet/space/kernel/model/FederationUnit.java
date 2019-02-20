package org.monet.space.kernel.model;

import net.minidev.json.JSONObject;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FederationUnit extends BaseObject implements Serializable {
	private String label;
	private String url;
	private boolean isMember;
	private boolean isVisible;
	private FederationUnitServiceList serviceList = new FederationUnitServiceList();
	private FederationUnitFeederList feederList = new FederationUnitFeederList();
	private List<String> ontologies = new ArrayList<String>();

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isMember() {
		return isMember;
	}

	public void setMember(boolean isMember) {
		this.isMember = isMember;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	public void setServiceList(FederationUnitServiceList serviceList) {
		this.serviceList = serviceList;
	}

	public FederationUnitServiceList getServiceList() {
		return this.serviceList;
	}

	public FederationUnitService getService(String name) {
		for (FederationUnitService service : this.getServiceList())
			if (service.getName().equals(name))
				return service;
		return null;
	}

	public void setFeederList(FederationUnitFeederList feederList) {
		this.feederList = feederList;
	}

	public FederationUnitFeederList getFeederList() {
		return this.feederList;
	}

	public List<String> getOntologies() {
		return this.ontologies;
	}

	public void setOntologies(ArrayList<String> ontologies) {
		this.ontologies = ontologies;
	}

	public boolean complyOntologies(List<String> compliantOntologies) {

		for (String compliantOntology : compliantOntologies)
			if (!this.ontologies.contains(compliantOntology))
				return false;

		return true;
	}

	@Override
	public JSONObject toJson() {
		JSONObject result = new JSONObject();

		result.put("id", this.id);
		result.put("name", this.name);
		result.put("label", this.label);
		result.put("url", this.url);
		result.put("isMember", this.isMember);
		result.put("isVisible", this.isVisible);
		result.put("services", this.serviceList.toJson());
		result.put("feeders", this.feederList.toJson());

		return result;
	}

	public String getServiceUrl(String serviceName) {
        return this.getUrl() + "/service/business/" + serviceName + "/";
	}

    public ServiceUri getServiceUri(String serviceName) {
        return ServiceUri.build(this.getName(), serviceName);
    }

	public String getFeederUrl(String feederName) {
        return this.getUrl() + "/service/source/" + feederName + "/";
	}

    public FeederUri getFeederUri(String feederName) {
        return FeederUri.build(this.getName(), feederName);
    }

    public String getMailBoxUrl(String mailBoxId) {
        return this.getUrl() + "/service/mailbox/" + mailBoxId + "/";
    }

    public MailBoxUri getMailBoxUri(String mailBoxId) {
        return MailBoxUri.build(this.getName(), mailBoxId);
    }

	public void loadFromFederation(org.monet.federation.accountservice.accountactions.impl.messagemodel.FederationUnit federationPartner) {
		this.setId(federationPartner.getId());
		this.setName(federationPartner.getName());
		this.setLabel(federationPartner.getLabel());
		this.setUrl(federationPartner.getUri().toString());
		this.setMember(federationPartner.isMember());
		this.setVisible(federationPartner.isVisible());

		this.loadServices(federationPartner);
		this.loadFeeders(federationPartner);
	}

    public String getUrl(Uri uri) {

        if (!uri.getPartner().equals(this.getName()))
            return null;

        if (uri.isToService())
            return this.getServiceUrl(uri.getId());
        else if (uri.isToFeeder())
            return this.getFeederUrl(uri.getId());
        else if (uri.isToMailbox())
            return this.getMailBoxUrl(uri.getId());

        return null;
    }

	private void loadServices(org.monet.federation.accountservice.accountactions.impl.messagemodel.FederationUnit federationPartner) {
		FederationUnitServiceList serviceList = this.getServiceList();

		serviceList.clear();
		for (org.monet.federation.accountservice.accountactions.impl.messagemodel.Service federationPartnerService : federationPartner.getServiceList().getAll()) {
			FederationUnitService federationUnitService = new FederationUnitService();
			federationUnitService.setPartnerId(this.getId());
			federationUnitService.setName(federationPartnerService.getName());
			federationUnitService.setLabel(federationPartnerService.getLabel());
			federationUnitService.setOntology(federationPartnerService.getOntology());

			serviceList.add(federationUnitService);
		}
	}

	private void loadFeeders(org.monet.federation.accountservice.accountactions.impl.messagemodel.FederationUnit federationPartner) {
		FederationUnitFeederList feederList = this.getFeederList();

		feederList.clear();
		for (org.monet.federation.accountservice.accountactions.impl.messagemodel.Feeder federationPartnerFeeder : federationPartner.getFeederList().getAll()) {
			FederationUnitFeeder federationUnitFeeder = new FederationUnitFeeder();
			federationUnitFeeder.setPartnerId(this.getId());
			federationUnitFeeder.setName(federationPartnerFeeder.getName());
			federationUnitFeeder.setLabel(federationPartnerFeeder.getLabel());
			federationUnitFeeder.setOntology(federationPartnerFeeder.getOntology());

			feederList.add(federationUnitFeeder);
		}
	}

	@Override
	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {
	}

}
