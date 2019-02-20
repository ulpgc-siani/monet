package org.monet.space.kernel.machines.ttm.model;

import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

import java.util.HashMap;
import java.util.Map.Entry;

@Root(name = "snapshot")
public class Snapshot extends ProcessBase {

	@ElementMap(name = "open-providers")
	private HashMap<String, String> openProviders = new HashMap<String, String>();

	@ElementMap(name = "open-collaborators")
	private HashMap<String, String> openCollaborators = new HashMap<String, String>();

	@SuppressWarnings("unchecked")
	public static Snapshot from(Process model) {
		Snapshot snapshot = new Snapshot();
		snapshot.place = model.place;
		snapshot.lockStates = (HashMap<String, Integer>) model.lockStates.clone();
		snapshot.isInitialized = model.isInitialized;
		snapshot.isFinished = model.isFinished;

		for (Entry<String, Provider> entry : model.getProviders().entrySet()) {
			if (entry.getValue().isOpen())
				snapshot.openProviders.put(entry.getKey(), entry.getValue().getLocalMailBox().toString());
		}

		return snapshot;
	}

	public void restoreTo(Process model) {
		model.place = this.place;
		model.lockStates = this.lockStates;
		model.isInitialized = this.isInitialized;
		model.isFinished = this.isFinished;
	}

	public HashMap<String, String> getOpenProviders() {
		return openProviders;
	}

	public HashMap<String, String> getOpenCollaborators() {
		return openCollaborators;
	}

}
