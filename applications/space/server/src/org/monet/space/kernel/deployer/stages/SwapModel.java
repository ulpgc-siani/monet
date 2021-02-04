package org.monet.space.kernel.deployer.stages;

import org.monet.metamodel.Distribution;
import org.monet.space.kernel.agents.AgentFilesystem;
import org.monet.space.kernel.deployer.GlobalData;
import org.monet.space.kernel.deployer.Stage;
import org.monet.space.kernel.deployer.errors.CantSwapModelError;
import org.monet.space.kernel.model.BusinessModel;
import org.monet.space.kernel.model.BusinessUnit;
import org.monet.space.kernel.model.Dictionary;

import java.io.File;
import java.util.UUID;

public class SwapModel extends Stage {
	private File newModelDirectory;
	private File modelDirectory;
	private File tempDirectory;

	@Override
	public void execute() {
		this.newModelDirectory = new File(this.globalData.getData(String.class, GlobalData.WORKING_DIRECTORY));
		this.modelDirectory = new File(this.globalData.getData(String.class, GlobalData.MODEL_INSTALL_DIRECTORY));
		this.tempDirectory = new File(modelDirectory.getParentFile(), UUID.randomUUID().toString());

		try {
			saveModel(this.modelDirectory, this.tempDirectory);
			cleanModel(this.modelDirectory);
			addNewModel(this.newModelDirectory, this.modelDirectory);
		}
		catch (Exception exception) {
			this.problems.add(new CantSwapModelError());
			throw new RuntimeException("Can't swap models, maybe already in use. Try again later.", exception);
		}
		finally {
			cleanTemps();
		}

		AgentFilesystem.removeDir(this.tempDirectory);

		BusinessUnit businessUnit = BusinessUnit.getInstance();
		businessUnit.setBusinessModel(BusinessModel.reload());
		businessUnit.setDistribution(BusinessUnit.reloadDistribution());
		businessUnit.setName(businessUnit.getDistribution().getSpace().getName());
		Dictionary.getInstance().reset(businessUnit.getDistribution(), businessUnit.getBusinessModel().getProject());
	}

	private void saveModel(File modelDirectory, File destinationDirectory) {
		if (!modelDirectory.exists())
			return;

		AgentFilesystem.copyDir(modelDirectory, destinationDirectory);
	}

	private void cleanModel(File modelDirectory) {
		AgentFilesystem.removeDirContent(modelDirectory);
	}

	private void addNewModel(File newModelDirectory, File modelDirectory) {
		AgentFilesystem.copyDir(newModelDirectory, modelDirectory);
	}

	private void cleanTemps() {
		AgentFilesystem.removeDir(this.newModelDirectory);
		AgentFilesystem.removeDir(this.tempDirectory);
	}

	@Override
	public String getStepInfo() {
		return "Swapping Model";
	}
}
