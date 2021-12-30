package org.monet.space.kernel.deployer.stages;

import org.monet.space.kernel.deployer.Stage;
import org.monet.space.kernel.model.BusinessModel;
import org.monet.space.kernel.model.BusinessUnit;
import org.monet.space.kernel.model.Dictionary;

public class ReloadBusinessUnit extends Stage {

	@Override
	public void execute() {
		BusinessUnit businessUnit = BusinessUnit.getInstance();
		businessUnit.setBusinessModel(BusinessModel.reload());
		businessUnit.setDistribution(BusinessUnit.reloadDistribution());
		businessUnit.setName(businessUnit.getDistribution().getSpace().getName());
		Dictionary.getInstance().reset(businessUnit.getDistribution(), businessUnit.getBusinessModel().getProject());
		businessUnit.save();
	}

	@Override
	public String getStepInfo() {
		return "Reloading business unit";
	}
}