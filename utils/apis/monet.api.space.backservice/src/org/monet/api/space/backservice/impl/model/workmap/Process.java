package org.monet.api.space.backservice.impl.model.workmap;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;

import java.util.HashMap;

public class Process extends ProcessBase {

	@Attribute
	private String id;
	@Attribute
	private String code;
	@Attribute(name = "edition-form-id", required = false)
	private String editionFormId;
	@Attribute(name = "current-job-order-id", required = false)
	private String currentJobOrderId;
	@Element(required = false)
	private Customer customer;
	@Element(required = false)
	private Contestants contestants;
	@ElementMap
	private HashMap<String, Provider> providers = new HashMap<String, Provider>();
	@ElementMap
	private HashMap<String, Contest> contests = new HashMap<String, Contest>();
	@ElementMap(required = false)
	private HashMap<String, String> flags = new HashMap<String, String>();

	public Process() {
	}

	public String getId() {
		return id;
	}

	public String getCode() {
		return code;
	}

	public String getEditionFormId() {
		return editionFormId;
	}

	public void setEditionFormId(String editionFormId) {
		this.editionFormId = editionFormId;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Contestants getContestants() {
		return contestants;
	}

	public void setContestants(Contestants contestants) {
		this.contestants = contestants;
	}

	public HashMap<String, Provider> getProviders() {
		return providers;
	}

	public void setProviders(HashMap<String, Provider> providers) {
		this.providers = providers;
	}

	public HashMap<String, Contest> getContests() {
		return contests;
	}

	public void setContests(HashMap<String, Contest> collaborators) {
		this.contests = collaborators;
	}

	public boolean isInitialized() {
		return isInitialized;
	}

	public void setInitialized(boolean isInitialized) {
		this.isInitialized = isInitialized;
	}

	public boolean isFinished() {
		return isFinished;
	}

	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}

	public void setFlag(String name, String value) {
		this.flags.put(name, value);
	}

	public HashMap<String, String> getFlags() {
		return this.flags;
	}

	public String getFlag(String name) {
		return this.flags.get(name);
	}

	public void removeFlag(String name) {
		this.flags.remove(name);
	}

	public String getCurrentJobOrderId() {
		return this.currentJobOrderId;
	}

	public void setCurrentJobOrderId(String value) {
		this.currentJobOrderId = value;
	}

}
