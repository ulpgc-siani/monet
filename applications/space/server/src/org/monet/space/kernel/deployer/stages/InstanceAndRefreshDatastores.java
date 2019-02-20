package org.monet.space.kernel.deployer.stages;

import com.google.inject.Inject;
import org.monet.metamodel.DatastoreDefinition;
import org.monet.space.kernel.components.layers.DatastoreLayer;
import org.monet.space.kernel.deployer.GlobalData;
import org.monet.space.kernel.deployer.Stage;
import org.monet.space.kernel.deployer.errors.ServerError;
import org.monet.space.kernel.model.BusinessUnit;
import org.monet.space.kernel.model.Dictionary;

import java.util.ArrayList;
import java.util.List;

public class InstanceAndRefreshDatastores extends Stage {
	@Inject
	private DatastoreLayer datastoreLayer;

	@SuppressWarnings("unchecked")
	@Override
	public void execute() {
		List<DatastoreDefinition> oldDatastoreDefinitions = this.globalData.getData(List.class, GlobalData.DATASTORE_DEFINITIONS);
		BusinessUnit businessUnit = BusinessUnit.getInstance();
		Dictionary dictionary = businessUnit.getBusinessModel().getDictionary();
		List<DatastoreDefinition> datastoreDefinitions;
		ArrayList<String> existingDatastoreSet, obsoleteDatastoreSet;

		try {
			datastoreDefinitions = dictionary.getDatastoreDefinitionList();

			existingDatastoreSet = this.getExistingDatastores(oldDatastoreDefinitions, datastoreDefinitions);
			obsoleteDatastoreSet = this.getObsoleteDatastores(oldDatastoreDefinitions, datastoreDefinitions);

			this.updateDatastores(datastoreDefinitions, oldDatastoreDefinitions, existingDatastoreSet);
			this.createDatastores(datastoreDefinitions, existingDatastoreSet);
			this.removeDatastores(datastoreDefinitions, obsoleteDatastoreSet);

		} catch (Exception exception) {
			problems.add(new ServerError("", exception.getMessage()));
			this.deployLogger.error(exception);
		}

	}

	private void updateDatastores(List<DatastoreDefinition> datastoreDefinitions, List<DatastoreDefinition> oldDatastoreDefinitions, ArrayList<String> existingDatastoreSet) {

		for (DatastoreDefinition definition : datastoreDefinitions) {
			if (definition.isAbstract())
				continue;

			try {
				if (!existingDatastoreSet.contains(definition.getCode()))
					continue;

				DatastoreDefinition oldDefinition = this.getDatastoreDefinition(definition.getCode(), oldDatastoreDefinitions);

				if (!this.isDatastoreModified(oldDefinition, definition))
					continue;

				try {
					datastoreLayer.update(oldDefinition, definition);
				} catch (Exception exception) {
					problems.add(new ServerError(definition.getName(), exception.getMessage()));
					this.deployLogger.error(exception);
				}

			} catch (Exception e) {
				problems.add(new ServerError(definition.getFileName(), e.getMessage()));
				this.deployLogger.error(e);
			}
		}
	}

	private void createDatastores(List<DatastoreDefinition> datastoreDefinitions, ArrayList<String> existingDatastoreSet) {

		for (DatastoreDefinition definition : datastoreDefinitions) {
			if (definition.isAbstract())
				continue;

			try {
				if (!existingDatastoreSet.contains(definition.getCode())) {
					try {
						datastoreLayer.create(definition.getCode());
					} catch (Exception exception) {
						problems.add(new ServerError(definition.getName(), exception.getMessage()));
						this.deployLogger.error(exception);
					}
				}
			} catch (Exception e) {
				problems.add(new ServerError(definition.getFileName(), e.getMessage()));
				this.deployLogger.error(e);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void removeDatastores(List<DatastoreDefinition> datastoreDefinitions, ArrayList<String> obsoleteDatastoreSet) {
		if (obsoleteDatastoreSet.size() > 0) return;

		BusinessUnit businessUnit = BusinessUnit.getInstance();
		Dictionary dictionary = businessUnit.getBusinessModel().getDictionary();
		List<DatastoreDefinition> oldDatastoreDefinitions = this.globalData.getData(List.class, GlobalData.DATASTORE_DEFINITIONS);

		for (String obsoleteCubeCode : obsoleteDatastoreSet) {
			DatastoreDefinition definition = this.getDatastoreDefinition(obsoleteCubeCode, oldDatastoreDefinitions);

			try {
				if (definition == null)
					definition = dictionary.getDatastoreDefinition(obsoleteCubeCode);
			} catch (Exception e) {
				continue;
			}

			try {
				datastoreLayer.remove(definition.getCode());
			} catch (Exception exception) {
				problems.add(new ServerError(definition.getName(), exception.getMessage()));
				this.deployLogger.error(exception);
			}
		}
	}

	private ArrayList<String> getExistingDatastores(List<DatastoreDefinition> oldDatastoreDefinitions, List<DatastoreDefinition> datastoreDefinitions) {
		ArrayList<String> result = new ArrayList<String>();

		for (DatastoreDefinition oldDatastoreDefinition : oldDatastoreDefinitions) {
			String code = oldDatastoreDefinition.getCode();
			DatastoreDefinition datastoreDefinition = this.getDatastoreDefinition(code, datastoreDefinitions);
			if (datastoreDefinition != null)
				result.add(code);
		}

		return result;
	}

	private ArrayList<String> getObsoleteDatastores(List<DatastoreDefinition> oldDatastoreDefinitions, List<DatastoreDefinition> datastoreDefinitions) {
		ArrayList<String> result = new ArrayList<String>();

		for (DatastoreDefinition oldDatastoreDefinition : oldDatastoreDefinitions) {
			String code = oldDatastoreDefinition.getCode();
			DatastoreDefinition datastoreDefinition = this.getDatastoreDefinition(code, datastoreDefinitions);
			if (datastoreDefinition == null)
				result.add(code);
		}

		return result;
	}

	private boolean isDatastoreModified(DatastoreDefinition oldDefinition, DatastoreDefinition definition) {

		if (definition.getDimensionList().size() != oldDefinition.getDimensionList().size())
			return true;

		if (definition.getCubeList().size() != oldDefinition.getCubeList().size())
			return true;

		return false;
	}

	private DatastoreDefinition getDatastoreDefinition(String code, List<DatastoreDefinition> datastoreDefinitions) {

		for (DatastoreDefinition datastoreDefinition : datastoreDefinitions)
			if (datastoreDefinition.getCode().equals(code))
				return datastoreDefinition;

		return null;
	}

	@Override
	public String getStepInfo() {
		return "Instancing and refreshing datastores";
	}
}
