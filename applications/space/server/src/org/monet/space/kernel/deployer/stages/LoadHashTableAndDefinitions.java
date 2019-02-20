package org.monet.space.kernel.deployer.stages;

import org.monet.metamodel.DashboardDefinition;
import org.monet.metamodel.DatastoreDefinition;
import org.monet.metamodel.IndexDefinition;
import org.monet.metamodel.SourceDefinition;
import org.monet.space.kernel.agents.AgentFilesystem;
import org.monet.space.kernel.deployer.GlobalData;
import org.monet.space.kernel.deployer.Stage;
import org.monet.space.kernel.exceptions.FilesystemException;
import org.monet.space.kernel.model.Dictionary;
import org.monet.space.kernel.utils.StreamHelper;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class LoadHashTableAndDefinitions extends Stage {

	public LoadHashTableAndDefinitions() {
	}

	@Override
	public void execute() {
		HashMap<String, String> hashTable;
		String hashFilePath = this.globalData.getData(String.class, GlobalData.MODEL_INSTALL_DIRECTORY).toString() + "/files.hash";

		try {
			if (AgentFilesystem.existFile(hashFilePath))
				hashTable = loadHashMap(hashFilePath);
			else
				hashTable = new HashMap<String, String>();
			this.globalData.setData(GlobalData.HASH_TABLE, hashTable);

			// Load current reference definitions
			ArrayList<IndexDefinition> referenceDefinitions = new ArrayList<IndexDefinition>();
			referenceDefinitions.addAll(Dictionary.getInstance().getIndexDefinitionList());
			this.globalData.setData(GlobalData.REFERENCE_DEFINITIONS, referenceDefinitions);

			// Load current source definitions
			HashMap<String, SourceDefinition> sourceDefinitions = new HashMap<String, SourceDefinition>();
			sourceDefinitions.putAll(Dictionary.getInstance().getSourceDefinitionMap());
			this.globalData.setData(GlobalData.SOURCE_DEFINITIONS, sourceDefinitions);

			// Load current datastore definitions
			ArrayList<DatastoreDefinition> datastoreDefinitions = new ArrayList<DatastoreDefinition>();
			datastoreDefinitions.addAll(Dictionary.getInstance().getDatastoreDefinitionList());
			this.globalData.setData(GlobalData.DATASTORE_DEFINITIONS, datastoreDefinitions);

			// Load current dashboard definitions
			ArrayList<DashboardDefinition> dashboardDefinitions = new ArrayList<DashboardDefinition>();
			dashboardDefinitions.addAll(Dictionary.getInstance().getDashboardDefinitionList());
			this.globalData.setData(GlobalData.DASHBOARD_DEFINITIONS, dashboardDefinitions);

		} catch (Exception e) {
			this.deployLogger.error(e);
		}
	}

	private HashMap<String, String> loadHashMap(String sFilename) {
		HashMap<String, String> oHashMap;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader;
		String sLine;

		try {
			oHashMap = new HashMap<String, String>();
			inputStreamReader = new InputStreamReader(new FileInputStream(sFilename), "UTF-8");
			bufferedReader = new BufferedReader(inputStreamReader);
			while ((sLine = bufferedReader.readLine()) != null) {
				String[] tokens = sLine.split("#");
				String sName = tokens[0];
				String sHash = tokens[1];
				oHashMap.put(sName, sHash);
			}
		} catch (IOException e) {
			this.deployLogger.error(e);
			throw new FilesystemException("Could not read file", sFilename, e);
		} finally {
			StreamHelper.close(inputStreamReader);
		}

		return oHashMap;
	}

	@Override
	public String getStepInfo() {
		return "Initializing";
	}
}
