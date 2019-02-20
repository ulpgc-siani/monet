package org.monet.space.kernel.deployer;

import jonelo.jacksum.JacksumAPI;
import jonelo.jacksum.algorithm.AbstractChecksum;
import org.monet.space.kernel.deployer.problems.Problem;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public abstract class Stage {

	protected GlobalData globalData;
	protected List<Problem> problems = new ArrayList<Problem>();
	protected StageState state = StageState.NOT_EXECUTED;
	protected DeployLogger deployLogger;

	public List<Problem> getProblems() {
		return problems;
	}

	public void setLogger(DeployLogger logger) {
		this.deployLogger = logger;
	}

	public abstract void execute();

	public StageState getState() {
		if (problems.size() > 0) {
			return StageState.COMPLETE_WITH_ERRORS;
		}
		return StageState.COMPLETE;
	}

	public void setGlobalData(GlobalData gd) {
		this.globalData = gd;
	}

	public abstract String getStepInfo();

	protected boolean isModified(File absoluteFile, String key) {
		HashMap<String, String> hashTable = (HashMap<String, String>) this.globalData.getData(HashMap.class, GlobalData.HASH_TABLE);

		String newHash = this.calculateHash(absoluteFile);
		String oldHash = hashTable.get(key);
		hashTable.put(key, newHash);
		return oldHash == null || !newHash.equals(oldHash);
	}

	protected void removeFromModified(File file) {
		HashMap<String, String> hashTable = (HashMap<String, String>) this.globalData.getData(HashMap.class, GlobalData.HASH_TABLE);
		hashTable.remove(file.getName());
	}

	private String calculateHash(File file) {
		String algorithm = "sha1";
		AbstractChecksum checksum = null;
		try {
			checksum = JacksumAPI.getChecksumInstance(algorithm, true);
			if (file.exists() && !file.isDirectory())
				checksum.readFile(file.getAbsolutePath());
		} catch (Exception e) {
			this.deployLogger.error(e);
		}
		String hash = checksum.getFormattedValue();
		return hash;
	}
}
