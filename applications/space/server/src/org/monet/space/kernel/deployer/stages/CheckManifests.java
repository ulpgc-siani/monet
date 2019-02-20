package org.monet.space.kernel.deployer.stages;

import org.monet.space.kernel.deployer.GlobalData;
import org.monet.space.kernel.deployer.Stage;
import org.monet.space.kernel.model.BusinessModel;
import org.monet.space.kernel.model.BusinessUnit;

import java.io.File;

public class CheckManifests extends Stage {

	@Override
	public void execute() {
		File newModelDirectory = new File(this.globalData.getData(String.class, GlobalData.WORKING_DIRECTORY));

		if (!BusinessUnit.checkDistributionExists(newModelDirectory))
			throw new RuntimeException("Distribution file not found in model. Try clean eclipse project and upload again.");

		if (!BusinessModel.checkProjectExists(newModelDirectory))
			throw new RuntimeException("Project file not found in model. Try clean eclipse project and upload again.");

	}

	@Override
	public String getStepInfo() {
		return "Checking manifests";
	}
}
