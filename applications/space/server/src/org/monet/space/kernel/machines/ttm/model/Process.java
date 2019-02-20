package org.monet.space.kernel.machines.ttm.model;

import org.monet.metamodel.*;
import org.monet.space.kernel.model.Dictionary;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.core.Commit;

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

	private ProcessDefinition definition;


	public Process() {
	}

	public Process(String id, ProcessDefinition definition) {
		this.id = id;
		this.code = definition.getCode();
		this.definition = definition;

		if (definition instanceof ServiceDefinition) {
			this.customer = new Customer();
		} else if (definition instanceof ActivityDefinition) {
			ActivityDefinition activityDefinition = (ActivityDefinition) definition;
// CONTESTANTS
//			if (activityDefinition.getContestants() != null)
//				this.contestants = new Contestants();
		}

		for (TaskProviderProperty providerProperty : definition.getTaskProviderPropertyList())
			this.providers.put(providerProperty.getCode(), new Provider());

		for (TaskContestProperty contestProperty : definition.getTaskContestPropertyList())
			this.contests.put(contestProperty.getCode(), new Contest());
	}

	@Commit
	public void commit() {
		this.definition = (ProcessDefinition) Dictionary.getInstance().getTaskDefinition(this.code);
	}

	public String getId() {
		return id;
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

	public ProcessDefinition getDefinition() {
		return definition;
	}

	public PlaceProperty getPlaceProperty() {
		return this.definition.getPlace(this.place);
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

	public boolean isFlagActive(String name) {

		if (!this.flags.containsKey(name))
			return false;

		String value = getFlag(name);
		return value != null && Boolean.valueOf(value.replaceAll("[\n\r]", "")) == true;
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
