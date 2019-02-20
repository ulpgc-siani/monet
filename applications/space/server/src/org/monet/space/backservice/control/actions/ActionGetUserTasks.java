package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.backservice.core.constants.ErrorCode;
import org.monet.space.kernel.components.ComponentFederation;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.FederationLayer;
import org.monet.space.kernel.model.DataRequest;
import org.monet.space.kernel.model.TaskList;

public class ActionGetUserTasks extends Action {

	public ActionGetUserTasks() {
	}

	@Override
	public String execute() {
		String code = (String) this.parameters.get(Parameter.CODE);
		TaskList taskList;
		FederationLayer federationLayer = ComponentFederation.getInstance().getLayer(createConfiguration());
		DataRequest dataRequest = new DataRequest();
		org.monet.space.kernel.model.Account account;

		if (code == null) {
			return ErrorCode.WRONG_PARAMETERS;
		}

		account = federationLayer.loadAccount(code);
		taskList = ComponentPersistence.getInstance().getTaskLayer().loadTasks(account, dataRequest);

		return taskList.serializeToXML();
	}

}
