package org.monet.space.kernel.deployer.stages;

import org.monet.space.kernel.Kernel;
import org.monet.space.kernel.deployer.Stage;

public class Reset extends Stage {

	@Override
	public void execute() {
		Kernel.getInstance().reset();
	}

	@Override
	public String getStepInfo() {
		return "Reseting monet caches";
	}
}
