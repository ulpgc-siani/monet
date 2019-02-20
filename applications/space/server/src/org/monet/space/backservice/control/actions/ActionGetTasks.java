package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Parameter;
import org.monet.space.kernel.components.ComponentFederation;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.FederationLayer;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.library.LibraryEncoding;
import org.monet.space.kernel.model.Account;
import org.monet.space.kernel.model.Task;
import org.monet.space.kernel.model.TaskList;
import org.monet.space.kernel.model.TaskSearchRequest;

public class ActionGetTasks extends Action {

	public ActionGetTasks() {
	}

	@Override
	public String execute() {
		String inbox = (String)this.parameters.get(Parameter.INBOX);
		String folder = LibraryEncoding.decode((String) this.parameters.get(Parameter.FOLDER));
		String condition = LibraryEncoding.decode((String) this.parameters.get(Parameter.CONDITION));
		String owner = LibraryEncoding.decode((String) this.parameters.get(Parameter.OWNER));
		int start = Integer.valueOf((String) this.parameters.get(Parameter.START));
		int limit = Integer.valueOf((String) this.parameters.get(Parameter.LIMIT));
		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
		FederationLayer federationLayer = ComponentFederation.getInstance().getLayer(createConfiguration());
		TaskSearchRequest searchRequest;
		TaskList taskList;
		Account account;

		if (folder == null || folder.isEmpty()) folder = Task.Situation.ACTIVE;

		searchRequest = new TaskSearchRequest();
		searchRequest.setCondition(condition);
		searchRequest.addParameter(Task.Parameter.TYPE, null);
		searchRequest.addParameter(Task.Parameter.STATE, null);
		searchRequest.addParameter(Task.Parameter.INBOX, inbox);
		searchRequest.addParameter(Task.Parameter.SITUATION, folder);
		searchRequest.addParameter(Task.Parameter.ROLE, null);
		searchRequest.addParameter(Task.Parameter.URGENT, null);
		searchRequest.addParameter(Task.Parameter.OWNER, null);
		searchRequest.setStartPos(start);
		searchRequest.setLimit(limit);

		account = federationLayer.loadAccount(owner);
		taskList = taskLayer.searchTasks(account, searchRequest);
		taskList.setTotalCount(taskLayer.searchTasksCount(account, searchRequest));

		return taskList.serializeToXML();
	}

}
