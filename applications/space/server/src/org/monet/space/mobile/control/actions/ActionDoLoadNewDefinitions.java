package org.monet.space.mobile.control.actions;

import org.monet.mobile.exceptions.ActionException;
import org.monet.mobile.model.TaskDefinition;
import org.monet.mobile.service.requests.LoadNewDefinitionsRequest;
import org.monet.mobile.service.results.DownloadDefinitionsResult;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.SourceLayer;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.model.BusinessUnit;
import org.monet.space.kernel.model.Dictionary;

import java.util.ArrayList;
import java.util.Date;

public class ActionDoLoadNewDefinitions extends AuthenticatedTypedAction<LoadNewDefinitionsRequest, DownloadDefinitionsResult> {

	TaskLayer taskLayer;
	SourceLayer sourceLayer;

	public ActionDoLoadNewDefinitions() {
		super(LoadNewDefinitionsRequest.class);
		ComponentPersistence componentPersistence = ComponentPersistence.getInstance();
		this.taskLayer = componentPersistence.getTaskLayer();
		this.sourceLayer = componentPersistence.getSourceLayer();
	}

	@Override
	protected DownloadDefinitionsResult onExecute(LoadNewDefinitionsRequest request) throws ActionException {
		DownloadDefinitionsResult result = new DownloadDefinitionsResult();

		result.Definitions = new ArrayList<TaskDefinition>();

		long modelTimestamp = BusinessUnit.getInstance().getUpdateInfo().getDate().getTime();

		if (request.SyncMark < modelTimestamp) {
			for (org.monet.metamodel.JobDefinition definition : Dictionary.getInstance().getJobDefinitions()) {
				result.Definitions.add(definition.toMobileDefinition());
			}
		}

		result.SyncMark = (new Date()).getTime();

		return result;
	}

}
