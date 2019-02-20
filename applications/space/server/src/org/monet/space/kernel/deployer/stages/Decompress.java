package org.monet.space.kernel.deployer.stages;

import org.monet.space.kernel.agents.AgentFilesystem;
import org.monet.space.kernel.deployer.GlobalData;
import org.monet.space.kernel.deployer.Stage;
import org.monet.space.kernel.library.LibraryZip;

import java.io.File;

public class Decompress extends Stage {

	@Override
	public void execute() {
		boolean isValidZip = false;

		String destination = this.globalData.getData(String.class, GlobalData.WORKING_DIRECTORY);
		File modelFile = this.globalData.getData(File.class, GlobalData.MODEL_ZIP_FILE);
		File businessModelZipLocation = new File(this.globalData.getData(String.class, GlobalData.BUSINESS_MODEL_ZIP_LOCATION));

		try {
			AgentFilesystem.copyFile(modelFile, businessModelZipLocation);
			isValidZip = LibraryZip.decompress(modelFile, destination);
		} catch (Exception e) {
			this.deployLogger.error(e);
		}

		if (!isValidZip) {
			this.deployLogger.error("Can't decompress file");
			throw new RuntimeException("Can't decompress file, maybe corrupt. Try again later.");
		}

	}

	@Override
	public String getStepInfo() {
		return "Decompressing";
	}

}
