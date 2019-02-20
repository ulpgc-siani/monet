package org.monet.space.kernel.deployer.stages;

import org.monet.metamodel.Project;
import org.monet.space.kernel.Kernel;
import org.monet.space.kernel.deployer.GlobalData;
import org.monet.space.kernel.deployer.Stage;
import org.monet.space.kernel.deployer.errors.IncompatibleModelError;
import org.monet.space.kernel.deployer.errors.IncompatibleVersionError;
import org.monet.space.kernel.model.BusinessUnit;
import org.monet.space.kernel.utils.PackageProjectHelper;

import java.io.File;
import java.util.HashMap;

public class CheckVersion extends Stage {

	@Override
	public void execute() {
		Kernel kernel = Kernel.getInstance();
		String spaceVersion = kernel.getVersion();
		String modelVersion;
		File newModelDirectory;

		try {
			newModelDirectory = new File(this.globalData.getData(String.class, GlobalData.WORKING_DIRECTORY));

			HashMap<String, String> packageManifest = PackageProjectHelper.getPackageManifest(newModelDirectory);
			modelVersion = packageManifest.get("version");

			if (!kernel.isCompatible(modelVersion))
				this.problems.add(new IncompatibleVersionError(spaceVersion, modelVersion));

			String modelUUID = packageManifest.get("uuid");
			Project project = BusinessUnit.getInstance().getBusinessModel().getProject();
			if (project != null) {
				String currentModelUUID = project.getUUID();
				if (currentModelUUID != null && !currentModelUUID.equals(modelUUID)) {
					this.problems.add(new IncompatibleModelError(currentModelUUID, modelUUID));
				}
			}
		} catch (Exception e) {
			this.deployLogger.error(e);
			throw new RuntimeException("Can't check version compatibility.");
		}
	}

	@Override
	public String getStepInfo() {
		return "Checking version";
	}
}
